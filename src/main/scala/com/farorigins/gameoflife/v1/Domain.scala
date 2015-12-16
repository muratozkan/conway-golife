package com.farorigins.gameoflife.v1

/**
 * Created by murat.ozkan on 20/02/15.
 */
trait Domain {
  def cellName(pos: Pos) = s"cell-${pos.row}-${pos.col}"
}

case class Pos(row: Int, col: Int) {

  def -(other: Pos): Pos = {
    Pos(row - other.row, col - other.col)
  }

  def +(other: Pos): Pos = {
    Pos(row + other.row, col + other.col)
  }
}

object Pos {
  val Empty     = Pos(0, 0)

  val Up        = Pos(-1, 0)
  val Down      = Pos(1, 0)
  val Left      = Pos(0, -1)
  val Right     = Pos(0, 1)
  val UpLeft    = Pos(-1, -1)
  val UpRight   = Pos(-1, 1)
  val DownLeft  = Pos(1, -1)
  val DownRight = Pos(1, 1)

  val Neighbors = List(Up, UpLeft, Left, DownLeft, Down, DownRight, Right, UpRight)
}

case class Cell(pos: Pos, status: Boolean)

