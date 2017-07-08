// ### BASIC SETTINGS ### //
organization := "io.jvm.uuid"
name := "scala-uuid"
version := "0.2.3"

unmanagedSourceDirectories in Compile := Seq((scalaSource in Compile).value)
unmanagedSourceDirectories in Test := Seq((scalaSource in Test).value)

// ### DEPENDENCIES ### //
libraryDependencies += "org.specs2" %% "specs2-scalacheck" % "3.9.2" % "test"

// ### COMPILE SETTINGS ### //
scalaVersion := "2.12.2"
scalacOptions ++= Seq(
  "-deprecation"
, "-encoding", "UTF-8"
, "-feature"
, "-language:_"
, "-opt-warnings:_"
, "-opt:_"
, "-target:jvm-1.8"
, "-unchecked"
, "-Xdev"
, "-Xexperimental"
, "-Xfuture"
, "-Xlint:_"
, "-Xmax-classfile-name", "72"
, "-Xno-forwarders"
, "-Xstrict-inference"
, "-Xverify"
, "-Yno-adapted-args"
, "-Yno-generic-signatures"
, "-Yopt-log-inline", "_"
, "-Yrangepos"
, "-Yrepl-sync"
, "-Ywarn-dead-code"
, "-Ywarn-numeric-widen"
, "-Ywarn-unused-import"
, "-Ywarn-unused"
, "-Ywarn-value-discard"
)

scalacOptions in (Compile, doc) ++= Seq(
  "-no-link-warnings"
, "-sourcepath", (scalaSource in Compile).value.toString
, "-doc-source-url", s"""https://github.com/melezov/scala-uuid/blob/${version.value}-${
    CrossVersion.partialVersion(scalaVersion.value).get.productIterator.mkString(".")
  }.x/src/main/scala\u20AC{FILE_PATH}.scala"""
)

wartremoverWarnings in (Compile, compile) := Warts.allBut(
  Wart.Equals
, Wart.ImplicitConversion
, Wart.Null
, Wart.Overloading
, Wart.StringPlusAny
, Wart.Throw
, Wart.Var
, Wart.While
)
