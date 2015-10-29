package com.farorigins.gameoflife.v1

import akka.actor._
import akka.event.LoggingReceive
import com.farorigins.gameoflife.v1.UIActor.{Render, ShowUI}

import scala.collection.mutable
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by murat.ozkan on 18/02/15.
 */
class GameActor extends Actor with ActorLogging with Domain {
  val cellStates: mutable.MutableList[Cell] = mutable.MutableList()

  val uiActor = context.actorOf(UIActor.props, "ui")

  override def receive = LoggingReceive {
    case Init =>
      val size = GameWorld.size
      log.info(s"Initializing game ... World Size: ${size._1}x${size._2}")

      uiActor ! ShowUI(size._1, size._2, GameWorld.getAllCells)

      GameWorld.getAllCells.foreach(c => context.actorOf(CellActor.props(c.pos, c.status), cellName(c.pos)))

      context.system.scheduler.scheduleOnce(1000 millis, self, Tick)
    case State(p: Pos, s: Boolean) =>
      cellStates += Cell(p, s)
      if (cellStates.size == GameWorld.getAllCells.length) {

        uiActor ! Render(cellStates.toList)

        if (cellStates.count(c => c.status) > 0)
          context.system.scheduler.scheduleOnce(500 millis, self, Tick)
        else
          self ! PoisonPill

        cellStates.clear()
      }
    case Tick =>
      context.actorSelection("cell-*-*") ! Tick
      log.info("Completed sending TICK message")

    case _ => log.warning("Unknown Message Received")
  }

  override def postStop() = {
    context.system.terminate()
  }
}


  object GameActor {
  lazy val props = Props[GameActor].withDispatcher("golife-dispatcher")
}

case object Init
case object End
case object Tick
case class State(pos: Pos, state: Boolean)
