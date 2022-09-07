package controllers

import akka.actor.{Actor, ActorRef, Props}
import play.api.libs.Comet.json
import play.api.libs.json.{JsValue, Json}


object SimpleWebSocketActor {
  def props(clientActorRef: ActorRef, manager: ActorRef) = Props(new SimpleWebSocketActor(clientActorRef, manager))
}
class SimpleWebSocketActor(clientActorRef: ActorRef, manager: ActorRef) extends Actor {
  manager ! self
  @Override
  def receive = {
    // json message from client >>manager
    case inJson: JsValue => {
      println("json input reach at actor")
      manager ! inJson
    }
    // message from PlayerInteractManager >> client
    case outMsg: Message => {
      println("json output reach at actor")
      val x = outMsg.x
      val y = outMsg.y
      val placed = outMsg.placed
      val json = Json.parse(s"""{"x":$x, "y":$y, "placed":$placed}""")
      clientActorRef ! json
    }
    case m => {
      println("unknown message: " + m)
    }
  }
}
