package kz.rio.core

import akka.actor.{Props, ActorRef, Actor, ActorLogging}
import kz.rio.domain.{Echo, Pong, Ping, DomainMessage}
import kz.rio.endpoint.ResponderActor

/**
 * Created by irybakov on 1/12/16.
 */

object ServiceActor {

  def props(responder: ActorRef, correlationId: String): Props =  Props(classOf[ServiceActor],responder, correlationId)
}


class ServiceActor(responder: ActorRef,correlationId: String) extends Actor with ActorLogging {

  override def receive = {
    case Ping(ping) =>
      responder ! ResponderActor.PublishReplay(correlationId,Pong("PONG: " + ping))

    case Echo(echo) =>
      responder ! ResponderActor.PublishReplay(correlationId,Echo(echo))

  }
}
