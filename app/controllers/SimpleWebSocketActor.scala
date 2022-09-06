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
    case outMsg: String => {
      println("json output reach at actor")
      val json = Json.parse(s"""{"body": "$outMsg"}""")
      clientActorRef ! json
    }
    case m => {
      println("unknown message: " + m)
    }
  }
}
