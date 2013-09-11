package models

import play.api.db._
import play.api.Play.current

import scala.slick.driver.PostgresDriver.simple._
import Database.threadLocalSession

case class Thread(id: Int, title: String)

object Threads extends Table[Thread]("threads") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def title = column[String]("title")

  def * = id ~ title <> (Thread, Thread.unapply _)

  def forInsert = title

  def createNewThread(title: String, content: String): Thread = {
    lazy val database = Database.forDataSource(DB.getDataSource())
    database withSession {
      val thread = Threads.forInsert returning Threads insert title

      Posts.forInsert insert (thread.id, content)

      thread
    }
  }
}
