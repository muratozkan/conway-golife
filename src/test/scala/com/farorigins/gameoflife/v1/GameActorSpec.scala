package com.farorigins.gameoflife.v1

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

/**
 * Created by murat.ozkan on 18/02/15.
 */
class GameActorSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  def this() = this(ActorSystem("MySpec"))

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  /*
    "A Ping actor" must {
      "send back a ping on a pong" in {
        val pingActor = system.actorOf(PingActor.props)
        pingActor ! PongActor.PongMessage("pong")
        expectMsg(PingActor.PingMessage("ping"))
      }
    }

    "A Pong actor" must {
      "send back a pong on a ping" in {
        val pongActor = system.actorOf(PongActor.props)
        pongActor ! PingActor.PingMessage("ping")
        expectMsg(PongActor.PongMessage("pong"))
      }
    }
    */
}
