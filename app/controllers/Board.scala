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

object Board extends Controller {

  lazy val database = Database.forDataSource(DB.getDataSource())

  val threadForm = Form(
    tuple(
      "Title" -> text,
      "Content" -> text
    )
  )

  def show(boardLetter: String) = Action {
    database withSession {

      val boards = for {
        b <- Boards if b.id === 1
      } yield (b)

      val threads = for {
        t <- Threads
      } yield(t)

      val posts = threads.list.map { case t: models.Thread => t.posts }

      val threadsWithPosts = threads.list zip posts

      Ok(views.html.board(boards.list.head, threadsWithPosts, threadForm))
    }
  }
}
