package kz.rio.core


import akka.actor.{Props, ActorRef, ActorLogging, Actor}
import kz.rio.domain.DomainMessage


/**
 * Created by irybakov on 1/16/16.
 */
object RequestHandler {

  def props(): Props =  Props(classOf[RequestHandler])
}

class RequestHandler extends Actor with ActorLogging {

  override def receive: Receive = {
    case request: DomainMessage =>


  }
}
