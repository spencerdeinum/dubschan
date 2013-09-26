package controllers


import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.db._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.EventSource
import play.api.libs.EventSource.EventNameExtractor
import play.api.libs.iteratee.Concurrent
import play.api.libs.iteratee.Enumeratee
import play.api.libs.json._
import play.api.mvc._
import play.api.Play.current

import models.Boards
import models.Threads
import models.Posts

import scala.slick.driver.PostgresDriver.simple._
import Database.threadLocalSession

import libraries.ImageUploader
import libraries.Forms


object Post extends Controller {

  lazy val database = Database.forDataSource(DB.getDataSource())

  val postForm = Forms.postForm

  def create(boardShortName: String, threadId: Int) = Action(parse.multipartFormData) { implicit request =>

    postForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.homepage()),
      content => {
        database withSession {
          val thread = Query(Threads).filter( _.id === threadId).first

          val image = request.body.file("Image")

          val imageName = image match {
            case Some(image) => {
              ImageUploader.upload(image)
            }
            case None => (None)
          }

          val now = new java.sql.Timestamp( (new java.util.Date()).getTime() )
          val post = Posts.forInsert returning Posts insert((thread.id, now, content, imageName))

          val message = Json.obj(
            "threadId"  -> threadId,
            "html"      -> views.html.post(post).toString
          )

          Logger.info(s"Pushing $message.toString")

          threadChannel.push(message)

          Redirect(routes.Thread.show(boardShortName, thread.id))
        }
      }
    )
  }

  val (postsOut, threadChannel) = Concurrent.broadcast[JsValue]

  def filter(threadId: Int) = Enumeratee.filter[JsValue] {
    json => {
      val int = (json \ "threadId").as[Int]
      int == threadId
    }
  }

  def feed(threadId: Int) = Action {
    Logger.info(s"Streaming for $threadId")
    Ok.chunked(postsOut &> filter(threadId) &> Concurrent.buffer(20) &> EventSource()).as("text/event-stream")
  }
}
