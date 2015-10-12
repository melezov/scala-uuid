// ### BASIC SETTINGS ### //
organization := "io.jvm.uuid"
name := "scala-uuid"
version := "0.1.7"

unmanagedSourceDirectories in Compile := Seq((scalaSource in Compile).value)
unmanagedSourceDirectories in Test := Seq((scalaSource in Test).value)

// ### COMPILE SETTINGS ### //
crossScalaVersions := Seq("2.8.1", "2.8.2")
scalaVersion := crossScalaVersions.value.head
scalacOptions := Seq(
  "-deprecation"
, "-encoding", "UTF-8"
, "-optimise"
, "-unchecked"
, "-Xno-forwarders"
, "-Yclosure-elim"
, "-Ydead-code"
, "-Yinline"
, "-Ywarn-dead-code"
)
