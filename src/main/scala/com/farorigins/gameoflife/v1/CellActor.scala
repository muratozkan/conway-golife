package com.farorigins.gameoflife.v1

import akka.actor.{Props, ActorLogging, Actor}
import com.farorigins.gameoflife.v1.CellActor._

/**
 * Created by murat.ozkan on 18/02/15.
 */
class CellActor(pos: Pos) extends Actor with ActorLogging {

  var cell = Cell(pos.row, pos.col, status = false)

  override def receive = {
    case Init(state: Boolean) => cell = Cell(pos.row, pos.col, status = state)
    case State(pos: Pos, state: Boolean) => {
      // Received state update from neighbor

    }
    case Tick => {
      // Send state to neighbors
      context.actorSelection("")
    }
  }
}

object CellActor {
  case object Tick
  case class Init(state: Boolean)
  case class State(pos: Pos, state: Boolean)

  def props(pos: Pos): Props = Props(new CellActor(pos))

  val props = Props[CellActor]
}


