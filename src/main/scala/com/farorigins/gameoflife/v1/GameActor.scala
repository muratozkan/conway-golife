package com.farorigins.gameoflife.v1

import java.util.concurrent.TimeUnit

import akka.actor._
import akka.event.LoggingReceive
import com.farorigins.gameoflife.v1.UIActor.{Render, ShowUI}

import scala.collection.mutable

/**
 * Created by murat.ozkan on 18/02/15.
 */
class GameActor extends Actor with ActorLogging with Domain {
  val config = context.system.settings.config

  val uiActor = context.actorOf(UIActor.props, "ui")

  val maxGenerations = config.getInt("go-life.max-generations")
  val uiDelay = config.getDuration("go-life.ui-delay", TimeUnit.MILLISECONDS)

  val cellStates: mutable.MutableList[Cell] = mutable.MutableList()
  var generation = 0

  override def receive = LoggingReceive {
    case Init =>
      val size = GameWorld.size
      log.info(s"Initializing game ... World Size: ${size._1}x${size._2}")

      uiActor ! ShowUI(size._1, size._2, GameWorld.getAllCells)

      Thread.sleep(uiDelay)

      GameWorld.getAllCells.foreach(c => context.actorOf(CellActor.props(c.pos, c.status), cellName(c.pos)))

      context.actorSelection("cell-*-*") ! Tick(generation)
      log.info(s"TICK $generation")
    case Update(g, p, s) =>
      cellStates += Cell(p, s)

      if (cellStates.size == GameWorld.getAllCells.length) {

        uiActor ! Render(cellStates.toList)

        Thread.sleep(uiDelay)

        if (cellStates.count(c => c.status) > 0 && generation < maxGenerations) {
          generation = generation + 1
          context.actorSelection("cell-*-*") ! Tick(generation)
          log.info(s"TICK $generation")
        } else {
          self ! PoisonPill
          log.info("POISON PILL")
        }

        cellStates.clear()
      }
    case _ => log.warning("Unknown Message Received")
  }

  override def postStop() = {
    log.debug(s"Total ${cellStates.size} states received")
    context.system.terminate()
  }
}

object GameActor {
  lazy val props = Props[GameActor].withDispatcher("golife-dispatcher")
}

case object Init
case object End
case class Tick(generation: Int)
case class Update(generation: Int, pos: Pos, state: Boolean)
