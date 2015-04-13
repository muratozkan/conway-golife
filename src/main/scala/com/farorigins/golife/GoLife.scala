package com.farorigins.golife

import akka.actor.{ActorRef, ActorLogging, Props, Actor}
import com.typesafe.config.Config
import scala.io.Source
import scala.concurrent._

object GoLife {
  def props: Props = Props(new GoLife)

  case class Initialize(path: String)
  case object Shutdown
}

class GoLife extends Actor with ActorLogging with GameDomain {

  import context.dispatcher
  import GoLife._

  val config: Config = context.system.settings.config

  private val maxIterations = config.getInt("golife.max-iterations")

  private var world: ActorRef = ActorRef.noSender

  override def receive: Receive = {
    case Initialize(path) =>
      log.debug(f"Initializing system from {}", path)
      val fileAsync = fileContents(path)
      fileAsync map {
        case lines =>
          val worldCells = parseLines(lines)
          log.debug(f"Creating world ({}x{})...", worldCells.size, worldCells.head.size)
          world = context.actorOf(World.props(worldCells, maxIterations))
          world ! World.Start
      } recover {
        case e: Exception =>
          log.error(f"file {} not accessible", path)
      }

  }

  private def fileContents(path: String): Future[List[String]] = Future {
    val source = Source.fromFile(path, "UTF-8")
    try source.getLines().toList finally source.close()
  }
}

