package com.farorigins.gameoflife.v1

import akka.actor.{Props, ActorLogging, Actor}
import com.farorigins.gameoflife.v1.GameActor._

/**
 * Created by murat.ozkan on 18/02/15.
 */
class GameActor extends Actor with ActorLogging {

  val gui = new Gui()

  override def receive = {
    case Init => {
      log.info("Initializing game...")

      // initialize GameWorld
      val size = GameWorld.size
      log.info(s"World size: $size")

      gui.show(size._1 * 20, size._2 * 20)


    }
  }
}

object GameActor {
  case object Init
  case object End
  case class Active(pos: Pos)
  case class Passive(pos: Pos)

  val props = Props[GameActor]
}
