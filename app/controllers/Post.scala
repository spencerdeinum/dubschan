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

import java.io.File

object Post extends Controller {

  lazy val database = Database.forDataSource(DB.getDataSource())

  val postForm = Form("Content" -> text)

  def create(boardShortName: String, threadId: Int) = Action(parse.multipartFormData) { implicit request =>

    val content = postForm.bindFromRequest.get

    database withSession {
      val thread = Query(Threads).filter( _.id === threadId).first

      val now = new java.sql.Timestamp( (new java.util.Date()).getTime() )

      val image = request.body.file("Image").map { image =>
        val filename = image.filename
        val contentType = image.contentType
        val imagePath = "/assets/images/" + filename
        image.ref.moveTo(new File("public/images/" + filename))
        imagePath
      }

      val post = Posts.forInsert insert((thread.id, now, content, image))

      Redirect(routes.Thread.show(boardShortName, thread.id))
    }
  }
}
