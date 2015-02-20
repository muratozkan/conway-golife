package com.farorigins.gameoflife.v1

import akka.actor._
import com.farorigins.gameoflife.v1.GameActor._

/**
 * Created by murat.ozkan on 18/02/15.
 */
class GameActor extends Actor with ActorLogging with Domain {

  val gui = new Gui()

  override def receive = {
    case Init =>
      val size = GameWorld.size
      log.info(s"Initializing game ... World Size: ${size._1}x${size._2}")
      // gui.show(size._1 * 20, size._2 * 20)
      GameWorld.getAllCells.foreach(c => context.actorOf(CellActor.props(c.pos, c.status), cellName(c.pos)))

      context.actorSelection("cell-*-*") ! CellActor.Tick
      log.info("Completed sending TICK message")
    case End =>
      context.actorSelection("cell-*-*") ! CellActor.Exit
      log.info("Completed sending EXIT message")
    case Active(p: Pos) =>
      log.info(s"Received Active from $p")
    case Passive(p: Pos) =>
      log.info(s"Received Passive from $p")
    case _ => log.warning("Unknown Message Received")
  }
}

object GameActor {
  case object Init
  case object End
  case class Active(pos: Pos)
  case class Passive(pos: Pos)

  val props = Props[GameActor]
}
