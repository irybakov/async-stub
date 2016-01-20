package kz.rio.domain

/**
 * Created by irybakov on 1/16/16.
 */
trait DomainMessage

case class Ping(ping: String) extends DomainMessage
case class Pong(pong: String) extends DomainMessage
case class Echo(echo: String) extends DomainMessage


