package com.farorigins.gameoflife.v1

import akka.actor.ActorSystem

/**
 * Created by murat.ozkan on 18/02/15.
 */
object ApplicationMain extends App {
  val system = ActorSystem("LifeSystem")

  val gameActor = system.actorOf(GameActor.props, "game")
  gameActor ! GameActor.Init

  Thread.sleep(5000)

  gameActor ! GameActor.End

  system.awaitTermination()
}
