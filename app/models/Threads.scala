package models

import play.api.db._
import play.api.Play.current

import scala.slick.driver.PostgresDriver.simple._
import Database.threadLocalSession

case class Thread(id: Int, boardId: Int, createdAt: java.sql.Timestamp, title: String) {
  def posts = Query(Posts).filter(_.threadID === id).sortBy(_.createdAt).list
}

object Threads extends Table[Thread]("threads") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def boardId = column[Int]("board_id")

  def createdAt = column[java.sql.Timestamp]("created_at") 

  def title = column[String]("title")

  def * = id ~ boardId ~ createdAt ~ title <> (Thread, Thread.unapply _)

  def forInsert = boardId ~ createdAt ~ title 

  def createNewThread(boardId: Int, title: String, content: String, imageName: Option[String]): Thread = {
    lazy val database = Database.forDataSource(DB.getDataSource())
    database withSession {
      val now = new java.sql.Timestamp( (new java.util.Date()).getTime() )

      val thread = Threads.forInsert returning Threads insert (boardId, now, title)

      Posts.forInsert insert (thread.id, now, content, imageName)

      thread
    }
  }
}
