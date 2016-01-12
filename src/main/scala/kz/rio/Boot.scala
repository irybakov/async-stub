package kz.rio

import java.util.concurrent.TimeUnit

import akka.actor.{Props, ActorSystem}
import com.github.sstone.amqp.{Consumer, Amqp, ConnectionOwner, ChannelOwner}
import com.rabbitmq.client.ConnectionFactory
import com.github.sstone.amqp.Amqp._
import kz.rio.core.ServiceActor
import kz.rio.endpoint.{ListenerActor, ResponderActor}
import scala.concurrent.duration._

/**
 * Created by irybakov on 1/12/16.
 */
object Boot extends App {

  implicit val system = ActorSystem("async-stub")


  // create an AMQP connection
  val connFactory = new ConnectionFactory()
  connFactory.setHost("192.168.33.10")
  connFactory.setPort(5672)
  connFactory.setUsername("admin")
  connFactory.setPassword("admin")

  val conn = system.actorOf(ConnectionOwner.props(connFactory, 1 second))
  val producer = ConnectionOwner.createChildActor(conn, ChannelOwner.props())



  // create responder
  val responder = system.actorOf(ResponderActor.props(producer))

  val serviceActor = system.actorOf(ServiceActor.props(responder))

  val listener = system.actorOf(ListenerActor.props(serviceActor))


  // create a consumer that will route incoming AMQP messages to our listener
  val queueParams = QueueParameters("request.queue", passive = false, durable = false, exclusive = false, autodelete = true)
  val consumer = ConnectionOwner.createChildActor(conn, Consumer.props(Some(listener)))

  // wait till everyone is actually connected to the broker
  Amqp.waitForConnection(system, conn, producer).await(5, TimeUnit.SECONDS)
  Amqp.waitForConnection(system, consumer).await()

  consumer ! DeclareQueue(queueParams)


  // tell our consumer to consume from it
  consumer ! AddQueue(queueParams)


}
