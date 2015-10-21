// ### BASIC SETTINGS ### //
organization := "io.jvm.uuid"
name := "scala-uuid"
version := "0.2.1"

unmanagedSourceDirectories in Compile := Seq((scalaSource in Compile).value)
unmanagedSourceDirectories in Test := Seq((scalaSource in Test).value)

// ### COMPILE SETTINGS ### //
crossScalaVersions := Seq("2.9.0", "2.9.0-1", "2.9.1", "2.9.1-1", "2.9.2", "2.9.3")
scalaVersion := crossScalaVersions.value.head
scalacOptions := Seq(
  "-deprecation"
, "-encoding", "UTF-8"
, "-optimise"
, "-unchecked"
, "-Xfatal-warnings"
, "-Xmax-classfile-name", "72"
, "-Xno-forwarders"
, "-Yclosure-elim"
, "-Ydead-code"
, "-Yinline"
, "-Ywarn-dead-code"
)
scalacOptions in (Compile, doc) ++= Seq(
  "-sourcepath", (scalaSource in Compile).value.toString
, "-doc-source-url", s"""https://github.com/melezov/scala-uuid/blob/${version.value}-${
    CrossVersion.partialVersion(scalaVersion.value).get.productIterator.mkString(".")
  }.x/src/main/scala€{FILE_PATH}.scala"""
)
