organization in ThisBuild := "kz.rio"

name := "async-stub"

version := "1.0"

scalaVersion := "2.11.7"

scalacOptions := Seq("-feature", "-unchecked", "-deprecation", "-encoding", "utf8")

val akkaVersion = "2.3.12"
val json4sVersion = "3.2.11"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "org.json4s" %% "json4s-native" % json4sVersion,
  "com.rabbitmq"      %   "amqp-client"       % "3.5.3",
  "com.github.sstone" %   "amqp-client_2.11"  % "1.5"
)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)


enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)

