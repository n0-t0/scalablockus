package controllers
import akka.actor.Actor
import akka.actor.ActorRef
import play.api.libs.json.{JsValue, Json}

class PlayerInteractManager extends Actor {
  private var playerRefs = List.empty[ActorRef]
  var x: Int = 0
  var y: Int = 0
  var placed = false
  @Override def receive = {
    case inJson: JsValue => {
      val clientMessage = (inJson \ "message").as[String]
      clientMessage match {
        case "left" => {
          x = x - 1
          placed = false
        }
        case "right" => {
          x = x + 1
          placed = false
        }
        case "up" => {
          y = y - 1
          placed = false
        }
        case "down" => {
          y = y + 1
          placed = false
        }
        case "place" => {
          println(s"placed at $x, $y")
          placed = true
        }
      }
      println(clientMessage)
      for (playerRef <- playerRefs) playerRef ! Message(x, y, placed)
    }
    case playerSocketRef: ActorRef => {
      println("new player detected")
      playerRefs ::=playerSocketRef
    }
    case m => println("Unknown message is sent to PlayerInteractManager: " + m)
  }
}