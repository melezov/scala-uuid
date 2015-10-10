// ### BASIC SETTINGS ### //
organization := "io.jvm.uuid"
name := "scala-uuid"
version := "0.1.7"

unmanagedSourceDirectories in Compile := Seq((scalaSource in Compile).value)
unmanagedSourceDirectories in Test := Seq((scalaSource in Test).value)

// ### DEPENDENCIES ### //
libraryDependencies += "org.specs2" %% "specs2-scalacheck" % "3.6.4" % "test"

// ### COMPILE SETTINGS ### //
scalaVersion := "2.11.7"
scalacOptions := Seq(
  "-deprecation"
, "-encoding", "UTF-8"
, "-feature"
, "-language:implicitConversions"
, "-optimise"
, "-unchecked"
, "-Xlint"
, "-Xmax-classfile-name", "72"
, "-Xno-forwarders"
, "-Xverify"
, "-Yclosure-elim"
, "-Yconst-opt"
, "-Ydead-code"
, "-Yinline-warnings"
, "-Yinline"
, "-Yrepl-sync"
, "-Ywarn-adapted-args"
, "-Ywarn-dead-code"
, "-Ywarn-inaccessible"
, "-Ywarn-infer-any"
, "-Ywarn-nullary-override"
, "-Ywarn-nullary-unit"
, "-Ywarn-numeric-widen"
, "-Ywarn-unused"
)
