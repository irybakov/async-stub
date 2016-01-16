package kz.rio.endpoint

import akka.actor.{Props, ActorRef, ActorLogging, Actor}
import com.github.sstone.amqp.Amqp._
import com.github.sstone.amqp.{Amqp, Consumer, ConnectionOwner}
import kz.rio.core.ServiceActor
import org.json4s.NoTypeHints
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization

/**
 * Created by irybakov on 1/12/16.
 */
object ListenerActor {
  def props(amqpConnection: ActorRef, responder: ActorRef): Props =  Props(classOf[ListenerActor],amqpConnection,responder)
}

class ListenerActor(amqpConnection: ActorRef, responder: ActorRef) extends Actor with ActorLogging {

  import context._

  implicit val formats = Serialization.formats(NoTypeHints)

  // create a consumer that will route incoming AMQP messages to our listener
  val queueParams = QueueParameters("request.queue", passive = false, durable = false, exclusive = false, autodelete = true)
  val consumer = ConnectionOwner.createChildActor(amqpConnection, Consumer.props(Some(self)))

  // wait till everyone is actually connected to the broker

  Amqp.waitForConnection(system, consumer).await()

  consumer ! DeclareQueue(queueParams)

  // tell our consumer to consume from it
  consumer ! AddQueue(queueParams)

  override def receive = {
    case Delivery(consumerTag, envelope, properties, body) => {
      val msgBody = new String(body)
      println("got a message: " + msgBody)

      val ping = parse(msgBody).extract[ServiceActor.Ping]
      val service = system.actorOf(ServiceActor.props(responder))
      service ! ping

      sender ! Ack(envelope.getDeliveryTag)
    }
  }

}
