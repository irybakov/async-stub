package kz.rio.endpoint

import java.util.concurrent.TimeUnit

import akka.actor.{Props, ActorRef, ActorLogging, Actor}
import com.github.sstone.amqp.Amqp.Publish
import com.github.sstone.amqp.{Amqp, ChannelOwner, ConnectionOwner}
import com.rabbitmq.client.AMQP.BasicProperties
import kz.rio.core.ServiceActor
import kz.rio.domain.{DomainMessage, Echo, Pong}
import kz.rio.endpoint.ResponderActor.PublishReplay
import org.json4s.ShortTypeHints
import org.json4s.native.Serialization
import org.json4s.native.Serialization._

/**
 * Created by irybakov on 1/12/16.
 */

object ResponderActor {

  case class PublishReplay(correlationId: String, dm: DomainMessage)

  def props(amqpConnection: ActorRef): Props =  Props(classOf[ResponderActor],amqpConnection)

}

class ResponderActor(amqpConnection: ActorRef) extends Actor with ActorLogging {

  import context._
  implicit val formats = Serialization.formats(ShortTypeHints(List(classOf[Pong],classOf[Echo])))

  val producer = ConnectionOwner.createChildActor(amqpConnection, ChannelOwner.props())
  Amqp.waitForConnection(system, amqpConnection, producer).await(5, TimeUnit.SECONDS)

  override def receive = {
    case p @ PublishReplay(correlationId, dm) => publish(write[DomainMessage](p.dm),p.correlationId,"")

    //case Pong(pong) => producer ! Publish("amq.topic", "pong.replay", pong.getBytes, properties = None, mandatory = true, immediate = false)
    //case Echo(echo) => producer ! Publish("amq.topic", "echo.replay", echo.getBytes, properties = None, mandatory = true, immediate = false)
  }

  def publish (body: String, correlationId: String, replayTo: String)= {

    val props = new BasicProperties(null,null,null,1,null,correlationId,null,null,null,null,null,null,null,null)
    producer ! Publish("amq.topic", "pong.replay", body.getBytes(), properties = Some(props), mandatory = true, immediate = false)
    log.info("Replay {}", body)
  }
}
