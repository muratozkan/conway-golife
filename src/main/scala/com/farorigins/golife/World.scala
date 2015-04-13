package com.farorigins.golife

import akka.actor.{ActorLogging, Props, Actor}

object World {
  def props(cells: Vector[Vector[Cell]], maxIterations: Int): Props = Props(new World(cells, maxIterations))

  case object Start
}

class World(cells: Vector[Vector[Cell]], maxIterations: Int) extends Actor with ActorLogging with GameDomain {

  import World._

  override def receive: Receive = {
    case Start => context.system.shutdown()
  }
}

