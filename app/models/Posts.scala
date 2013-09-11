package models

import scala.slick.driver.PostgresDriver.simple._

case class Post(id: Int, threadID: Int, post: String)

object Posts extends Table[Post]("posts") {
  def id = column[Int]("id", O.PrimaryKey)

  def threadID = column[Int]("thread_id")

  def content = column[String]("content")

  def thread = foreignKey("thread_fk", threadID, models.Threads)(_.id)

  def * = id ~ threadID ~ content <> (Post, Post.unapply _)

  def forInsert = threadID ~ content
}
