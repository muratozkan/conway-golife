package com.farorigins.gameoflife.v1

import collection.immutable.List
import collection.{mutable => m}
import akka.actor.{ ActorLogging, Props, Actor}

/**
 * Created by murat.ozkan on 18/02/15.
 */
class CellActor(pos: Pos, var state: Boolean) extends Actor with ActorLogging with Domain {
  val neighborNames: List[String] = Pos.Neighbors.map(p => p + pos).filter(p => GameWorld.isValid(p)).map(p => cellName(p))
  val neighborStates: m.Map[Pos, Boolean] = m.Map()

  var sent: Boolean = false
  override def receive = {
    case Tick =>
      sent = false
      neighborStates.clear()
      neighborNames.foreach(p => context.actorSelection(s"../$p") ! State(pos, state))
    case State(np: Pos, ns: Boolean) =>
      // Received state update from neighbor
      neighborStates.update(np, ns)

      if (!sent && neighborStates.size == neighborNames.length) {
        state = neighborStates.count(ns => ns._2) match {
          case n if n > 3 => false
          case 3 => true
          case 2 => state
          case n if n < 2 => false
        }
        context.parent ! State(pos, state)

        sent = true
      }
    case End =>
      if (neighborStates.size != neighborNames.length)
        log.info(s"DUMP: $sent $neighborStates ")
  }
}

object CellActor {
  def props(pos: Pos, state: Boolean): Props = Props(classOf[CellActor], pos, state).withDispatcher("golife-dispatcher")
}
