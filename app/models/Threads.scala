package models

import scala.slick.driver.PostgresDriver.simple._

case class Thread(id: Int, title: String)

object Threads extends Table[Thread]("threads") {
  def id = column[Int]("id", O.PrimaryKey)

  def title = column[String]("title")

  def * = id ~ title <> (Thread, Thread.unapply _)

  def forInsert = title
}
