package com.farorigins.golife

import akka.actor.{ActorRef, ActorSystem}
import akka.event.Logging

import scala.annotation.tailrec

object GoLifeApp {

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("golife-system")
    val goLifeApp = new GoLifeApp(system)
    goLifeApp.run()
  }
}

class GoLifeApp(system: ActorSystem) {

  private val log = Logging(system, getClass.getName)

  private val world: ActorRef = system.actorOf(GoLife.props, "golife")

  def run(): Unit = {
    log.info("Starting Conway's Game of Life")
    world ! GoLife.Initialize("patterns/initial.txt")
    system.awaitTermination()
  }
}
