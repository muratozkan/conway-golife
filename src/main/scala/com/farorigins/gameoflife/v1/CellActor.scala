package com.farorigins.gameoflife.v1

import scala.collection.immutable.List
import akka.actor.{PoisonPill, Props, ActorLogging, Actor}
import com.farorigins.gameoflife.v1.CellActor._

/**
 * Created by murat.ozkan on 18/02/15.
 */
class CellActor(pos: Pos, var state: Boolean) extends Actor with ActorLogging with Domain {
  val neighborNames: List[String] = Pos.Neighbors.map(p => p + pos).filter(p => GameWorld.isValid(p)).map(p => cellName(p))

  var sent: Boolean = false
  var active: Int = 0
  var responded: Int = 0

  override def receive = {
    case Tick => {
      // Send state to neighbor
      // log.info("Sending state...")
      sent = false
      active = 0
      responded = 0
      neighborNames.foreach(p => context.actorSelection(s"../$p") ! State(pos, state))
      log.info("State pushed to all neighbors")
    }
    case State(np: Pos, ns: Boolean) => {
      // Received state update from neighbor
      responded = responded + 1
      active = ns match {
        case true => active + 1
        case false => active
      }

      if (!sent && responded == neighborNames.length) {
        state = active >= 3
        state match {
          case true => context.parent ! GameActor.Active(pos)
          case false => context.parent ! GameActor.Passive(pos)
        }
        sent = true
      }
    }
    case Exit =>
      log.info(s"Dump: $sent $active $responded")
      context.self ! PoisonPill
  }
}

object CellActor {
  case object Tick
  case object Exit
  case class State(pos: Pos, state: Boolean)

  def props(pos: Pos, state: Boolean): Props = Props(classOf[CellActor], pos, state).withDispatcher("golife-dispatcher")
}
