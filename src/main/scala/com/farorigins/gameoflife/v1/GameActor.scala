package com.farorigins.gameoflife.v1

import akka.actor._
import com.farorigins.gameoflife.v1.GameActor._

import scala.collection.mutable
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by murat.ozkan on 18/02/15.
 */
class GameActor extends Actor with ActorLogging with Domain {
  val cellStates: mutable.MutableList[Cell] = mutable.MutableList()
  val gui = new Gui()

  override def receive = {
    case Init =>
      val size = GameWorld.size
      log.info(s"Initializing game ... World Size: ${size._1}x${size._2}")
      gui.show(size._1 * Gui.CellSize, size._2 * Gui.CellSize)
      gui.Paint.setWorld(GameWorld.getAllCells)
      gui.Paint.repaint()

      GameWorld.getAllCells.foreach(c => context.actorOf(CellActor.props(c.pos, c.status), cellName(c.pos)))

      context.system.scheduler.scheduleOnce(1000 millis, self, Tick)
    case End =>
      context.system.shutdown()
    case State(p: Pos, s: Boolean) =>
      cellStates += Cell(p, s)
      if (cellStates.size == GameWorld.getAllCells.length) {
        gui.Paint.setWorld(cellStates.toList)
        gui.Paint.repaint()

        if (cellStates.count(c => c.status) > 0)
          context.system.scheduler.scheduleOnce(1000 millis, self, Tick)
        else
          context.system.scheduler.scheduleOnce(1000 millis, self, End)

        cellStates.clear()
      }
    case Tick =>
      context.actorSelection("cell-*-*") ! CellActor.Tick
      log.info("Completed sending TICK message")
    case _ => log.warning("Unknown Message Received")
  }
}

object GameActor {
  case object Init
  case object End
  case object Tick
  case class State(pos: Pos, state: Boolean)

  val props = Props[GameActor]
}
