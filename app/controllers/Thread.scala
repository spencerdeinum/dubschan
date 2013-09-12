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

object Thread extends Controller {

  lazy val database = Database.forDataSource(DB.getDataSource())

  val threadForm = Form(
    tuple(
      "Title" -> text,
      "Content" -> text
    )
  )

  def show(boardShortName: String, threadId: Int) = Action {
    database withSession {
      val board = Query(Boards).filter( _.shortName === boardShortName ).first

      val thread = Query(Threads).filter( _.id === threadId).first

      val posts = thread.posts

      Ok(views.html.thread(board, thread, posts))
    }
  }

  def create(boardShortName: String) = Action { implicit request =>

    val (title, content) = threadForm.bindFromRequest.get

    val newThread = Threads.createNewThread(title, content)

    Ok(views.html.homepage())
  }

  //def show(boardLetter: String) = Action {
  //  database withSession {

  //    val boards = for {
  //      b <- models.Boards if b.id === 1
  //    } yield (b)

  //    Ok(views.html.board(boards.list.head))
  //  }
  //}
}
