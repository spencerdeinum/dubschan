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

object Post extends Controller {

  lazy val database = Database.forDataSource(DB.getDataSource())

  val postForm = Form("Content" -> text)

  def create(boardShortName: String, threadId: Int) = Action { implicit request =>

    val content = postForm.bindFromRequest.get

    database withSession {
      val thread = Query(Threads).filter( _.id === threadId).first

      val now = new java.sql.Timestamp( (new java.util.Date()) getTime )

      val post = Posts.forInsert insert((thread.id, now, content))

      Redirect(routes.Thread.show(boardShortName, thread.id))
    }
  }
}
