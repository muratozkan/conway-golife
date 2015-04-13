package com.farorigins.golife

/**
 * Created by ozkanm on 13/04/15.
 */
trait GameDomain {

  def parseLines(lines: List[String]): Vector[Vector[Cell]] =
    lines.zipWithIndex.map {
      case(line, rowIndex) => line.toCharArray.zipWithIndex.map {
        case(char, colIndex) => Cell(rowIndex, colIndex, char == 'x')
      }.toVector
    }.toVector
}

case class Cell(row: Int, col: Int, state: Boolean = false)
