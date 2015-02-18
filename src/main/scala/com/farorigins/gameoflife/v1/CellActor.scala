package com.farorigins.gameoflife.v1

import akka.actor.{Props, ActorLogging, Actor}

/**
 * Created by murat.ozkan on 18/02/15.
 */
class CellActor(pos: Pos) extends Actor with ActorLogging {
  override def receive = ???
}

object CellActor {
  def props(pos: Pos): Props = Props(new CellActor(pos))

  val props = Props[CellActor]
}


