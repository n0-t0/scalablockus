package controllers
import akka.actor.Actor
import akka.actor.ActorRef
import akka.http.javadsl.model.ws.Message
import play.api.libs.json.{JsValue, Json}

class PlayerInteractManager extends Actor {
  private var playerRefs = List.empty[ActorRef]
  var x: Int = 50
  var y: Int = 50
  @Override def receive = {
    case inJson: JsValue => {
      val clientMessage = (inJson \ "message").as[String]
      clientMessage match {
        case "left" => x = x - 1
        case "right" => x = x + 1
        case "up" => y = y - 1
        case "down" => y = y + 1
      }
      val outMsg= s"($x, $y)"
      println(clientMessage)
      for (playerRef <- playerRefs) playerRef ! outMsg
    }
    case playerSocketRef: ActorRef => {
      println("new player detected")
      playerRefs ::=playerSocketRef
    }
    case m => println("Unknown message is sent to PlayerInteractManager: " + m)
  }
}