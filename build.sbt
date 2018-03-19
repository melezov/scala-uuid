// ### BASIC SETTINGS ### //
organization := "io.jvm.uuid"
name := "scala-uuid"
version := "0.2.4"

unmanagedSourceDirectories in Compile := Seq((scalaSource in Compile).value)
unmanagedSourceDirectories in Test := Seq((scalaSource in Test).value)
