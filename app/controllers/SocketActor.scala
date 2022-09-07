package controllers

import akka.actor.{Actor, ActorRef, Props}
import play.api.libs.json.{JsValue, Json}
import controllers.ManageActor.Message

object SocketActor {
  def props(clientActorRef: ActorRef, manager: ActorRef): Props = Props(new SocketActor(clientActorRef, manager))
}
class SocketActor(clientActorRef: ActorRef, manager: ActorRef) extends Actor {
  manager ! self
  @Override
  def receive: Receive = {
    // json message from client >>manager
    case inJson: JsValue =>
      println("json input reach at actor")
      manager ! inJson
    // message from PlayerInteractManager >> client
    case outMsg: Message =>
      println("json output reach at actor")
      val x = outMsg.x
      val y = outMsg.y
      val placed = outMsg.placed
      val json = Json.parse(s"""{"x":$x, "y":$y, "placed":$placed}""")
      clientActorRef ! json
    case m =>
      println("unknown message: " + m)
  }
}
