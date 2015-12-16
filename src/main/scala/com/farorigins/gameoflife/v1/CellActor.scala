package com.farorigins.gameoflife.v1

import akka.event.LoggingReceive

import collection.immutable.List
import collection.{mutable => m}
import akka.actor._

/**
 * Created by murat.ozkan on 18/02/15.
 */
class CellActor(pos: Pos, var state: Boolean) extends Actor with ActorLogging with Stash with Domain {
  val neighborNames: List[String] =  GameWorld.getNeighborsPos(pos).map(p => cellName(p))
  val neighborStates: m.Map[Pos, Boolean] = m.Map()

  var sent: Boolean = false
  var generation = -1

  override def receive = LoggingReceive {
    case Tick(g) =>
      sent = false
      generation = g

      unstashAll()

      neighborStates.clear()
      neighborNames.foreach(p => context.actorSelection(s"../$p") ! Update(g, pos, state))
    case Update(g, p, s) if g == generation =>
      // Received state update from neighbor
      neighborStates.update(p, s)

      if (!sent && neighborStates.size == neighborNames.length) {
        val activeNeighbors = neighborStates.count(_._2)
        val nextState = GameWorld.nextState(state, neighborNames.length, activeNeighbors)
        log.info(s"$state -> $nextState [neighbors: ${neighborNames.length}, active: $activeNeighbors] ")

        context.parent ! Update(generation, pos, nextState)

        state = nextState
        sent = true
      }
    case msg: Update =>
      stash()
  }

  override def postStop()  = {
    if (neighborStates.size != neighborNames.length) {
      val missingNeighbors = GameWorld.getNeighborsPos(pos).filter(p => neighborStates.get(p).isEmpty)
      log.info(s"DUMP: $sent $missingNeighbors ")
    }
  }
}

object CellActor {
  def props(pos: Pos, state: Boolean): Props = Props(classOf[CellActor], pos, state).withDispatcher("golife-dispatcher")
}
