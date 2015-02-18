package com.farorigins.gameoflife.v1

import akka.actor.ActorSystem

/**
 * Created by murat.ozkan on 18/02/15.
 */
object ApplicationMain extends App {
  val system = ActorSystem("LifeSystem")
  val gameActor = system.actorOf(GameActor.props, "gameActor")
  gameActor ! GameActor.Initialize
  system.awaitTermination()
}
