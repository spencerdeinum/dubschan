package models

import scala.slick.driver.H2Driver.simple._

case class Board(id: Int, shortName: String, name: String)

object Boards extends Table[Board]("BOARD") {
  def id = column[Int]("ID", O.PrimaryKey)

  def name = column[String]("NAME")

  def shortName = column[String]("SHORT_NAME")

  def * = id ~ shortName ~ name <> (Board, Board.unapply _)
}
