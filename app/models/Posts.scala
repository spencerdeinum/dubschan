package models

import scala.slick.driver.PostgresDriver.simple._

case class Post(id: Int, threadID: Int, created_at: java.sql.Timestamp, content: String, imagePath: Option[String])

object Posts extends Table[Post]("posts") {
  def id = column[Int]("id", O.PrimaryKey)

  def threadID = column[Int]("thread_id")

  def created_at = column[java.sql.Timestamp]("created_at") 

  def content = column[String]("content")

  def imagePath = column[Option[String]]("image_path")

  def thread = foreignKey("thread_fk", threadID, models.Threads)(_.id)

  def * = id ~ threadID ~ created_at ~ content ~ imagePath <> (Post, Post.unapply _)

  def forInsert = threadID ~ created_at ~ content ~ imagePath
}
