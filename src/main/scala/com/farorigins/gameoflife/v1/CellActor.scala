package com.farorigins.gameoflife.v1

import collection.immutable.List
import collection.{mutable => m}
import akka.actor.{PoisonPill, ActorLogging, Props, Actor}

/**
 * Created by murat.ozkan on 18/02/15.
 */
class CellActor(pos: Pos, var state: Boolean) extends Actor with ActorLogging with Domain {
  val neighborNames: List[String] =  GameWorld.getNeighborsPos(pos).map(p => cellName(p))
  val neighborStates: m.Map[Pos, Boolean] = m.Map()

  var sent: Boolean = false

  override def receive = {
    case Tick =>
      sent = false
      neighborStates.clear()
      neighborNames.foreach(p => context.actorSelection(s"../$p") ! State(pos, state))
    case State(p, s) =>
      // Received state update from neighbor
      neighborStates.update(p, s)

      if (!sent && neighborStates.size == neighborNames.length) {
        state = GameWorld.nextState(state, neighborNames.length, neighborStates.count(_._2))

        context.parent ! State(pos, state)

        sent = true
      }
  }

  override def postStop()  = {
    if (neighborStates.size != neighborNames.length)
      log.info(s"DUMP: $sent $neighborStates ")
  }
}

object CellActor {
  def props(pos: Pos, state: Boolean): Props = Props(classOf[CellActor], pos, state).withDispatcher("golife-dispatcher")
}
