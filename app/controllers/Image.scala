package controllers

import play.api._
import play.api.data._
import play.api.db._
import play.api.mvc._
import play.api.Play.current

import models.Posts

import scala.slick.driver.PostgresDriver.simple._
import Database.threadLocalSession

object Image extends Controller {

  lazy val database = Database.forDataSource(DB.getDataSource())

  def show(postId: Int, imageName: String) = Action {

    database withSession {

      val post = Query(Posts).filter( _.id === postId ).first

      post.imageData match {
        case Some(imageData) => Ok(imageData)
        case None => NotFound
      }

    }

  }

}
