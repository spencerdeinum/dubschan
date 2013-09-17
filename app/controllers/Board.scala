package controllers

import play.api._
import play.api.data._
import play.api.db._
import play.api.mvc._
import play.api.Play.current

import models.Boards
import models.Threads
import models.Posts

import libraries.Forms

import scala.slick.driver.PostgresDriver.simple._
import Database.threadLocalSession

object Board extends Controller {

  lazy val database = Database.forDataSource(DB.getDataSource())

  val threadForm = Forms.threadForm

  def show(boardShortName: String) = Action {
    database withSession {

      val boardQuery = Query(Boards).filter(_.shortName === boardShortName).firstOption

      boardQuery match {
        case None => NotFound
        case Some(board) => {
          val threads = Query(Threads).list

          val posts = threads.map(_.posts)

          val threadsWithPosts = threads zip posts

          Ok(views.html.board(board, threadsWithPosts, threadForm))
        }
      }
    }
  }
}
