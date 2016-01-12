package kz.rio.endpoint

import akka.actor.{Props, ActorRef, ActorLogging, Actor}
import com.github.sstone.amqp.Amqp._
import kz.rio.core.ServiceActor
import org.json4s.NoTypeHints
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization

/**
 * Created by irybakov on 1/12/16.
 */
object ListenerActor {
  def props(service: ActorRef): Props =  Props(classOf[ListenerActor],service)
}

class ListenerActor(service: ActorRef) extends Actor with ActorLogging {
  implicit val formats = Serialization.formats(NoTypeHints)

  override def receive = {
    case Delivery(consumerTag, envelope, properties, body) => {
      val msgBody = new String(body)
      println("got a message: " + msgBody)

      val ping = parse(msgBody).extract[ServiceActor.Ping]
      service ! ping

      sender ! Ack(envelope.getDeliveryTag)
    }
  }

}
