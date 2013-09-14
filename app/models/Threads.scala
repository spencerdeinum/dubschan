package models

import play.api.db._
import play.api.Play.current

import scala.slick.driver.PostgresDriver.simple._
import Database.threadLocalSession

case class Thread(id: Int, created_at: java.sql.Timestamp, title: String) {
  def posts = (for {
    p <- Posts if p.threadID === id
  } yield(p)).list
}

object Threads extends Table[Thread]("threads") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def created_at = column[java.sql.Timestamp]("created_at") 

  def title = column[String]("title")

  def * = id ~ created_at ~ title <> (Thread, Thread.unapply _)

  def forInsert = created_at ~ title 

  def createNewThread(title: String, content: String, imageName: Option[String]): Thread = {
    lazy val database = Database.forDataSource(DB.getDataSource())
    database withSession {
      val now = new java.sql.Timestamp( (new java.util.Date()).getTime() )

      val thread = Threads.forInsert returning Threads insert (now, title)

      Posts.forInsert insert (thread.id, now, content, imageName)

      thread
    }
  }
}
