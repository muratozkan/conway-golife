package com.farorigins.gameoflife.v1

import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.{WordSpec, Matchers, Inspectors}

/**
 * Created by ozkanm on 29/10/2015.
 */
class WorldSpec extends WordSpec with Matchers with TypeCheckedTripleEquals with Inspectors {

  "a top left cell" should {

    val topLeftCell = Cell(Pos(0, 0), status = false)

    "have exactly 3 neighbors" in {
        val neighbors = GameWorld.getNeighborsPos(topLeftCell.pos)

        neighbors.size shouldBe 3
        neighbors shouldBe List(Pos(0, 1), Pos(1, 0), Pos(1, 1))
    }
  }

  "a bottom right cell" should {
    val bottomRightCell = Cell(Pos(GameWorld.size._1 - 1, GameWorld.size._2 - 1), status = false)

    "have exactly 3 neighbors" in {
      val neighbors = GameWorld.getNeighborsPos(bottomRightCell.pos)

      neighbors.size shouldBe 3
    }
  }

  "next state for " should {
    "corner cell" when {
      val topLeftCell = Cell(Pos(0, 0), status = false)
      val neighborCount = GameWorld.getNeighborsPos(topLeftCell.pos).size

      "all neighbors are inactive should be inactive" in {
        val nextState = GameWorld.nextState(state = false, neighborCount, 0)

        nextState shouldBe false
      }

      "two neighbors are active should be current state" in {
        val nextStateInactive = GameWorld.nextState(state = false, neighborCount, 2)
        val nextStateActive = GameWorld.nextState(state = true, neighborCount, 2)

        nextStateInactive shouldBe false
        nextStateActive shouldBe true
      }

      "three neighbors active should be active" in {
        val nextState = GameWorld.nextState(state = false, neighborCount, 3)

        nextState shouldBe true
      }
    }

    "bottom non corner cell" when {
      val bottomCell = Cell(Pos(6, 4), status = false)
      val neighborCount = GameWorld.getNeighborsPos(bottomCell.pos).size

      "one neighbor active should be inactive" in {
        val nextStateInactive = GameWorld.nextState(state = false, neighborCount, 1)
        val nextStateActive = GameWorld.nextState(state = true, neighborCount, 1)

        nextStateInactive shouldBe false
        nextStateActive shouldBe false
      }

    }

  }
}
