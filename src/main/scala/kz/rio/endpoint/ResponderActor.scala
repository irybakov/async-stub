package kz.rio.endpoint

import java.util.concurrent.TimeUnit

import akka.actor.{Props, ActorRef, ActorLogging, Actor}
import com.github.sstone.amqp.Amqp.Publish
import com.github.sstone.amqp.{Amqp, ChannelOwner, ConnectionOwner}
import kz.rio.core.ServiceActor

/**
 * Created by irybakov on 1/12/16.
 */

object ResponderActor {

  def props(amqpConnection: ActorRef): Props =  Props(classOf[ResponderActor],amqpConnection)

}


class ResponderActor(amqpConnection: ActorRef) extends Actor with ActorLogging {

  import context._

  val producer = ConnectionOwner.createChildActor(amqpConnection, ChannelOwner.props())
  Amqp.waitForConnection(system, amqpConnection, producer).await(5, TimeUnit.SECONDS)

  override def receive = {
    case ServiceActor.Pong(pong) => producer ! Publish("amq.topic", "pong.replay", pong.getBytes, properties = None, mandatory = true, immediate = false)
    case ServiceActor.Echo(echo) => producer ! Publish("amq.topic", "echo.replay", echo.getBytes, properties = None, mandatory = true, immediate = false)
  }
}
