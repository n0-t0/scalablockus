package controllers

import akka.actor.{ActorRef, ActorSystem, Props}
import play.api.libs.json.JsValue
import play.api.libs.streams.ActorFlow
import play.api.mvc._

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class WebsocketsController @Inject()(cc: ControllerComponents)(implicit system: ActorSystem) extends AbstractController(cc) {
  def index: Action[AnyContent] = Action{ implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  val manager: ActorRef = system.actorOf(Props[PlayerInteractManager](), "Manager")
  def ws: WebSocket = WebSocket.acceptOrResult[JsValue, JsValue] { implicit request =>
    Future.successful(getUpto2User match {
      case None => Left(Forbidden)
      case Some(_) => Right(
          ActorFlow.actorRef { outActorRef =>
            SimpleWebSocketActor.props(outActorRef, manager)
          }
      )
    })
  }
  var count=0;
  def getUpto2User: Option[Int] = {
    count = count+1
    println(count)
    if(count<=2) Option(count)
    else None
  }
}
