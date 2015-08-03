val ElementReleases  = "Element Releases"  at "http://repo.element.hr/nexus/content/repositories/releases/"
val ElementSnapshots = "Element Snapshots" at "http://repo.element.hr/nexus/content/repositories/snapshots/"

// ### BASIC SETTINGS ### //
organization := "io.jvm"
name := "scala-uuid"
version := "0.1.4"

unmanagedSourceDirectories in Compile := Seq((scalaSource in Compile).value)
unmanagedSourceDirectories in Test := Seq((scalaSource in Test).value)

// ### RESOLVERS & DEPENDENCIES ### //
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.5" % "test"
, "junit" % "junit" % "4.12" % "test"
)


publishTo := Some(if (version.value endsWith "-SNAPSHOT") ElementSnapshots else ElementReleases)
publishArtifact in (Compile, packageDoc) := false

credentials ++= {
  val creds = Path.userHome / ".config" / "scala-uuid" / "nexus.config"
  if (creds.exists) Some(Credentials(creds)) else None
}.toSeq

// ### COMPILE SETTINGS ### //
crossScalaVersions := Seq("2.11.7")
scalaVersion := crossScalaVersions.value.head

scalacOptions := Seq(
  "-deprecation"
, "-encoding", "UTF-8"
, "-feature"
, "-language:existentials"
, "-language:implicitConversions"
, "-language:postfixOps"
, "-language:reflectiveCalls"
, "-optimise"
, "-unchecked"
, "-Xcheckinit"
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

// ### ECLIPSE ### //
EclipseKeys.eclipseOutput := Some(".target")
