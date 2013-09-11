package models

import scala.slick.driver.PostgresDriver.simple._

case class Board(id: Int, shortName: String, name: String)

object Boards extends Table[Board]("boards") {
  def id = column[Int]("id", O.PrimaryKey)

  def name = column[String]("name")

  def shortName = column[String]("short_name")

  def * = id ~ shortName ~ name <> (Board, Board.unapply _)
}
