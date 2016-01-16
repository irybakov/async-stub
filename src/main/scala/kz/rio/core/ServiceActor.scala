package kz.rio.core

import akka.actor.{Props, ActorRef, Actor, ActorLogging}
import kz.rio.core.ServiceActor.{Pong, Echo, Ping}
import kz.rio.domain.DomainMessage

/**
 * Created by irybakov on 1/12/16.
 */

object ServiceActor {

  case class Ping(ping: String) extends DomainMessage
  case class Pong(pong: String) extends DomainMessage
  case class Echo(echo: String) extends DomainMessage

  def props(responder: ActorRef): Props =  Props(classOf[ServiceActor],responder)
}


class ServiceActor(responder: ActorRef) extends Actor with ActorLogging {

  override def receive = {
    case Ping(ping) =>
      responder ! Pong("PONG: " + ping)

    case Echo(echo) =>
      responder ! Echo(echo)

  }
}
