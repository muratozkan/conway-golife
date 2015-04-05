package com.farorigins.gameoflife

import akka.actor.ActorSystem

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

/**
 * Created by murat.ozkan on 18/02/15.
 */
object ApplicationMain extends App {
  val system = ActorSystem("LifeSystem")

  val gameActor = system.actorOf(GameActor.props, "game")
  gameActor ! Init

  system.scheduler.scheduleOnce(20000 millis, gameActor, End)
  system.awaitTermination()
}
