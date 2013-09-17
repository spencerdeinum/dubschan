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

import libraries.ImageUploader
import libraries.Forms

object Thread extends Controller {

  lazy val database = Database.forDataSource(DB.getDataSource())

  val threadForm = Forms.threadForm

  val postForm = Forms.postForm

  def show(boardShortName: String, threadId: Int) = Action {
    database withSession {
      val boardQuery = Query(Boards).filter( _.shortName === boardShortName ).firstOption

      boardQuery match {
        case None => NotFound
        case Some(board) => {
          val threadQuery = Query(Threads).filter( _.id === threadId).firstOption
          threadQuery match {
            case None => NotFound
            case Some(thread) => Ok(views.html.thread(board, thread, thread.posts, postForm))
          }
        }
      }

    }
  }

  def create(boardShortName: String) = Action(parse.multipartFormData) { implicit request =>

    threadForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.homepage()),
      form => {
        val image = request.body.file("Image")
        val imageName = image match {
          case Some(image) => {
            Some(ImageUploader.upload(image))
          }
          case None => None
        }

        database withSession {
          val board = Query(Boards).filter( _.shortName === boardShortName ).firstOption

          board match {
            case None => NotFound
            case Some(board) => {
              val newThread = Threads.createNewThread(board.id, form._1, form._2, imageName)
              Redirect(routes.Thread.show(board.shortName, newThread.id))
            }
          }
        }
      }
    )
  }
}
