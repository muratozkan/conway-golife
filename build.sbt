name := """conway-golife"""

version := "1.1"

scalaVersion := "2.11.7"

scalacOptions := Seq(
  "-target:jvm-1.8",
  "-encoding", "utf8"
)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.0",
  "com.typesafe.akka" %% "akka-testkit" % "2.4.0" % "test",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test")

fork in run := true
