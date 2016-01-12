package kz.rio.endpoint

import akka.actor.{Props, ActorRef, ActorLogging, Actor}
import com.github.sstone.amqp.Amqp.Publish
import kz.rio.core.ServiceActor

/**
 * Created by irybakov on 1/12/16.
 */

object ResponderActor {
  def props(producer: ActorRef): Props =  Props(classOf[ResponderActor],producer)
}


class ResponderActor(producer: ActorRef) extends Actor with ActorLogging {

  override def receive = {
    case ServiceActor.Pong(pong) => producer ! Publish("amq.topic", "pong.replay", pong.getBytes, properties = None, mandatory = true, immediate = false)
    case ServiceActor.Echo(echo) => producer ! Publish("amq.topic", "echo.replay", echo.getBytes, properties = None, mandatory = true, immediate = false)
  }
}
