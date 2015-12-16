package com.farorigins.gameoflife.v1

import scala.collection.immutable._

/**
 * Created by murat.ozkan on 18/02/15.
 */
trait World {
  def size: (Int, Int)
  def getCell: Pos => Option[Cell]
  def getAllCells: List[Cell]
  def getNeighbors: Pos => List[Cell]
  def getNeighborsPos(pos: Pos): List[Pos]
  def isValid(pos: Pos): Boolean

  def nextState(state: Boolean, neighborCount: Int, activeNeighbors: Int): Boolean
}

object GameWorld extends World {

  private val MapString =
    """---------------
      |---x-----------
      |-x-x-----------
      |--xx-----------
      |---------------
      |---------------
      |---------------""".stripMargin

  override def size: (Int, Int) = (InitialMap.size, InitialMap.head.size)

  override def getCell: Pos => Option[Cell] = cellFunction(InitialMap)

  override def getAllCells: List[Cell] = InitialMap.flatten.toList

  override def getNeighbors: Pos => List[Cell] = neighborFunction(InitialMap)

  override def getNeighborsPos(pos: Pos): List[Pos] = {
    val minRow = Math.max(pos.row - 1, 0)
    val maxRow = Math.min(pos.row + 1, size._1 - 1)

    val minCol = Math.max(pos.col - 1, 0)
    val maxCol = Math.min(pos.col + 1, size._2 - 1)

    val neighbors = for {
      r <- Range(minRow, maxRow).inclusive
      c <- Range(minCol, maxCol).inclusive
      if Pos(r, c) != pos
    } yield Pos(r, c)

    neighbors.toList
  }

  override def isValid(pos: Pos): Boolean = !(pos.row < 0 || pos.col < 0 || pos.row >= size._1 || pos.col >= size._2)

  override def nextState(state: Boolean, neighborCount: Int, activeNeighbors: Int): Boolean = {
    activeNeighbors match {
      case n if n > 3 => false
      case 3 => true
      case 2 => state
      case n if n < 2 => false
    }
  }

  private lazy val InitialMap: Vector[Vector[Cell]] = {
    MapString.split("\n").zipWithIndex.map {
      case(line, rowIndex) => line.toCharArray.zipWithIndex.map {
        case(char, colIndex) => Cell(Pos(rowIndex, colIndex), char == 'x')
      }.toVector
    }.toVector
  }

  private def cellFunction(cells: Vector[Vector[Cell]]): Pos => Option[Cell] = {
    (pos: Pos) => {
      if (isValid(pos))
        Some(cells(pos.row)(pos.col))
      else
        None
    }
  }

  private def neighborFunction(cells: Vector[Vector[Cell]]): Pos => List[Cell] = {
    (pos: Pos) => {
        getNeighborsPos(pos).flatMap(p => getCell(p))
    }
  }
}
