package controllers

import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.db._
import play.api.mvc._
import play.api.Play.current

import models.Boards
import models.Threads
import models.Posts

import scala.slick.driver.PostgresDriver.simple._
import Database.threadLocalSession

import libraries.ImageUploader

object Post extends Controller {

  lazy val database = Database.forDataSource(DB.getDataSource())

  val postForm = Form("Content" -> text)

  def create(boardShortName: String, threadId: Int) = Action(parse.multipartFormData) { implicit request =>

    val content = postForm.bindFromRequest.get

    database withSession {
      val thread = Query(Threads).filter( _.id === threadId).first

      val now = new java.sql.Timestamp( (new java.util.Date()).getTime() )

      val image = request.body.file("Image")

      val imageName = image match {
        case Some(image) => {
          Some(ImageUploader.upload(image))
        }
        case None => (None)
      }

      val post = Posts.forInsert insert((thread.id, now, content, imageName))

      Redirect(routes.Thread.show(boardShortName, thread.id))
    }
  }
}
