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
  def getNeighborsPos: Pos => List[Pos]
  def isValid(pos: Pos): Boolean
}

object GameWorld extends World {

  private val MapString =
    """---------------
      |---x-----------
      |-x-x-----------
      |--xx-----------
      |---------------
      |---------------
      |---------------
      |---------------
      |---------------
      |---------------
      |---------------
      |---------------
      |---------------""".stripMargin

  override def size: (Int, Int) = (InitialMap.size, InitialMap(0).size)

  override def getCell: Pos => Option[Cell] = cellFunction(InitialMap)

  override def getAllCells: List[Cell] = InitialMap.flatten.toList

  override def getNeighbors: Pos => List[Cell] = neighborFunction(InitialMap)

  override def getNeighborsPos: Pos => List[Pos] = (pos: Pos) => {
    Range(pos.row - 1, pos.row + 1).flatMap(cIndex => {
      Range(pos.col - 1, pos.col + 1).map(rIndex => {
        val nPos = Pos(rIndex, cIndex)
        if (isValid(nPos) && nPos != pos)
          Some(nPos)
        else
          None
      })
    }).flatten.toList
  }

  override def isValid(pos: Pos): Boolean = !(pos.row < 0 || pos.col < 0 || pos.row >= size._1 || pos.col >= size._2)

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
        getNeighborsPos(pos).map(p => getCell(p)).flatten.toList
    }
  }
}
