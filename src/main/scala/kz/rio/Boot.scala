package kz.rio


import akka.actor.{Props, ActorSystem}
import com.github.sstone.amqp.{Consumer, Amqp, ConnectionOwner, ChannelOwner}
import com.rabbitmq.client.ConnectionFactory
import com.typesafe.config.{Config, ConfigFactory}
import kz.rio.domain.{EcoService, Endpoint}
import kz.rio.endpoint.{ListenerActor, ResponderActor}
import scala.concurrent.duration._


/**
 * Created by irybakov on 1/12/16.
 */
object Boot extends App {

  implicit val system = ActorSystem("async-stub")

  private val config =  ConfigFactory.load()

  val amqpConnection = getAmqpConnection(config)

  val gateway = config.getConfig("ecosystem.gateway")
  val endpoint = loadEndpoint(config.getConfig("ecosystem.endpoint"))

  val inboundGate =  gateway.getString("inbound")
  val outboundGate = gateway.getString("outbound")

  val responder = system.actorOf(ResponderActor.props(amqpConnection,endpoint,outboundGate))

  // Listener
  val listener = system.actorOf(ListenerActor.props(amqpConnection,endpoint,inboundGate ,responder))

  def getAmqpConnection(config: Config) = {
    // create an AMQP connection
    val connFactory = new ConnectionFactory()

    val host = config.getString("amqp.host")
    val port = config.getInt("amqp.port")
    val user = config.getString("amqp.user")
    val password = config.getString("amqp.password")

    connFactory.setHost(host)
    connFactory.setPort(5672)
    connFactory.setUsername(user)
    connFactory.setPassword(password)

    system.actorOf(ConnectionOwner.props(connFactory, 1 second))
  }

  def loadEndpoint(config: Config): Endpoint = {
    val serviceConfig = config.getConfig("service")

    val service = EcoService(
      serviceConfig.getString("system"),
      serviceConfig.getString("subSystem"),
      serviceConfig.getString("microservice")
    )

    Endpoint(
      config.getString("instanceId"),
      service
    )
  }

}
