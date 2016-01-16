package kz.rio


import akka.actor.{Props, ActorSystem}
import com.github.sstone.amqp.{Consumer, Amqp, ConnectionOwner, ChannelOwner}
import com.rabbitmq.client.ConnectionFactory
import kz.rio.endpoint.{ListenerActor, ResponderActor}
import scala.concurrent.duration._


/**
 * Created by irybakov on 1/12/16.
 */
object Boot extends App {

  implicit val system = ActorSystem("async-stub")

  val amqpConnection = getAmqpConnection()

  val responder = system.actorOf(ResponderActor.props(amqpConnection))

  // Listener
  val listener = system.actorOf(ListenerActor.props(amqpConnection,responder))

  def getAmqpConnection() = {
    // create an AMQP connection
    val connFactory = new ConnectionFactory()
    connFactory.setHost("192.168.33.10")
    connFactory.setPort(5672)
    connFactory.setUsername("admin")
    connFactory.setPassword("admin")

    system.actorOf(ConnectionOwner.props(connFactory, 1 second))
  }
}
