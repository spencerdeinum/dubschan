package controllers

import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.db._
import play.api.mvc._
import play.api.Play.current

import models.Boards
import models.Threads

import scala.slick.driver.PostgresDriver.simple._
import Database.threadLocalSession

import java.io.BufferedInputStream
import java.io.FileInputStream

object Thread extends Controller {

  lazy val database = Database.forDataSource(DB.getDataSource())

  val threadForm = Form(
    tuple(
      "Title" -> text,
      "Content" -> text
    )
  )

  val postForm =  Form("Content" -> text)

  def show(boardShortName: String, threadId: Int) = Action {
    database withSession {
      val board = Query(Boards).filter( _.shortName === boardShortName ).first

      val thread = Query(Threads).filter( _.id === threadId).first

      val posts = thread.posts

      Ok(views.html.thread(board, thread, posts, postForm))
    }
  }

  def create(boardShortName: String) = Action(parse.multipartFormData) { implicit request =>

    val (title, content) = threadForm.bindFromRequest.get

    val image = request.body.file("Image")

    val imageData = image match {
      case Some(image) => {
        val bufferedInputStream = new BufferedInputStream(new FileInputStream(image.ref.file))
        val imageData = Stream.continually(bufferedInputStream.read).takeWhile(_ != -1).map(_.toByte).toArray
        (Some(image.filename), Some(imageData))
      }
      case None => (None, None)
    }

    val newThread = Threads.createNewThread(title, content, imageData)

    Redirect(routes.Thread.show(boardShortName, newThread.id))
  }

}
