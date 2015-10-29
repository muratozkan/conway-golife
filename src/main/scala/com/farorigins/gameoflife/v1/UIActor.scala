package com.farorigins.gameoflife.v1

import akka.actor.{Props, ActorLogging, Actor}

/**
 * Created by ozkanm on 29/10/2015.
 */
class UIActor extends Actor with ActorLogging with Domain {

  import UIActor._

  val gui = new Gui

  override def receive = {
    case ShowUI(x, y, cells) =>
      log.info(s"Initializing UI of size $x * $y...")

      gui.show(x * Gui.CellSize, y * Gui.CellSize)
      gui.setWorld(cells)
    case Render(cells) =>
      gui.setWorld(cells)
  }

  override def postStop() = {
    log.info("Dispose the GUI...")
    gui.dispose()
  }
}

object UIActor {
  case class ShowUI(x: Int, y: Int, cells: List[Cell])
  case class Render(cells: List[Cell])

  lazy val props = Props[UIActor].withDispatcher("golife-dispatcher")
}
