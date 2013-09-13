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

import java.io.BufferedInputStream
import java.io.FileInputStream

object Post extends Controller {

  lazy val database = Database.forDataSource(DB.getDataSource())

  val postForm = Form("Content" -> text)

  def create(boardShortName: String, threadId: Int) = Action(parse.multipartFormData) { implicit request =>

    val content = postForm.bindFromRequest.get

    database withSession {
      val thread = Query(Threads).filter( _.id === threadId).first

      val now = new java.sql.Timestamp( (new java.util.Date()).getTime() )

      val image = request.body.file("Image")

      val imageData = image match {
        case Some(image) => {
          val bufferedInputStream = new BufferedInputStream(new FileInputStream(image.ref.file))
          val imageData = Stream.continually(bufferedInputStream.read).takeWhile(_ != -1).map(_.toByte).toArray
          (Some(image.filename), Some(imageData))
        }
        case None => (None, None)
      }

      val post = Posts.forInsert insert((thread.id, now, content, imageData._1, imageData._2))

      Redirect(routes.Thread.show(boardShortName, thread.id))
    }
  }
}
