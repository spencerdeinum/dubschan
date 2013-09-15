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

      val board = Query(Boards).filter(_.id === 1).first

      val threads = Query(Threads).list

      val posts = threads.map(_.posts)

      val threadsWithPosts = threads zip posts

      Ok(views.html.board(board, threadsWithPosts, threadForm))
    }
  }
}
