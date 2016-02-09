package kz.rio.domain

/**
 * Created by irybakov on 1/16/16.
 */
trait DomainMessage

case class Ping(ping: String) extends DomainMessage
case class Pong(pong: String) extends DomainMessage
case class Echo(echo: String) extends DomainMessage


// Rabbit routing
case class EcoService(system: String,subSystem: String, microService: String) extends DomainMessage {

  def serviceEndpoint = s"$microService.$subSystem.$system"
}

case class Endpoint(instanceId: String, ecoService: EcoService) extends DomainMessage {

  val serviceEndpoint = ecoService.serviceEndpoint

  val instanceEndpoint = s"$instanceId.$serviceEndpoint"

  def queue = s"Q:$instanceEndpoint"

  def exchange = s"X:$instanceEndpoint"
}