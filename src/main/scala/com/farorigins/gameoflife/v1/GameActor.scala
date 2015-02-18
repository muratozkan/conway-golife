package com.farorigins.gameoflife.v1

import akka.actor.{Props, ActorLogging, Actor}
import com.farorigins.gameoflife.v1.GameActor.Initialize

/**
 * Created by murat.ozkan on 18/02/15.
 */
class GameActor extends Actor with ActorLogging {

  override def receive = {

    case Initialize => {
      log.info("Initializing game...")

      val size = GameWorld.size
      log.info(s"World size: $size")

      context.system.shutdown()
    }
  }
}

object GameActor {
  case object Initialize

  val props = Props[GameActor]
}
