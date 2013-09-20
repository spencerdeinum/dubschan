package controllers

import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.db._
import play.api.libs.EventSource
import play.api.libs.iteratee.Concurrent
import play.api.libs.iteratee.Enumeratee
import play.api.libs.json.JsValue
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
          val post = Posts.forInsert insert((thread.id, now, content, imageName))

          Logger.info(s"Pushing $threadId onto threadChannel")
          Logger.info(s"data: $threadId")
          threadChannel.push("data: " + threadId)

          Redirect(routes.Thread.show(boardShortName, thread.id))
        }
      }
    )
  }

  val (postsOut, threadChannel) = Concurrent.broadcast[String]

  def filter(threadId: Int) = Enumeratee.filter[String] {
    message: String => message.contains(threadId.toString)
  }

  def feed(threadId: Int) = Action {
    Logger.info(s"Streaming for $threadId")
    Ok.stream(postsOut &> filter(threadId) &> Concurrent.buffer(20) &> EventSource()).as("text/event-stream")
  }
}
