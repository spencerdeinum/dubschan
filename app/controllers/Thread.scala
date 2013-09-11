package controllers

import play.api._
import play.api.db._
import play.api.mvc._
import play.api.Play.current

import models.Threads

import scala.slick.driver.PostgresDriver.simple._
import Database.threadLocalSession

object Thread extends Controller {

  lazy val database = Database.forDataSource(DB.getDataSource())

  def show(boardShortName: String, threadId: Int) = TODO

  def create(boardShortName: String) = Action {
    database withSession {
      Threads insert models.Thread(1, "Test")
    }

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
