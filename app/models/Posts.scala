package models

import scala.slick.driver.PostgresDriver.simple._

case class Post(id: Int, threadID: Int, created_at: java.sql.Timestamp, content: String, imageName: Option[String], imageData: Option[Array[Byte]])

object Posts extends Table[Post]("posts") {
  def id = column[Int]("id", O.PrimaryKey)

  def threadID = column[Int]("thread_id")

  def created_at = column[java.sql.Timestamp]("created_at") 

  def content = column[String]("content")

  def imageName = column[Option[String]]("image_name")

  def imageData = column[Option[Array[Byte]]]("image_data")

  def thread = foreignKey("thread_fk", threadID, models.Threads)(_.id)

  def * = id ~ threadID ~ created_at ~ content ~ imageName ~ imageData <> (Post, Post.unapply _)

  def forInsert = threadID ~ created_at ~ content ~ imageName ~ imageData
}
