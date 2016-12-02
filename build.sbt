// ### BASIC SETTINGS ### //
organization := "io.jvm.uuid"
name := "scala-uuid"
version := "0.2.2"

unmanagedSourceDirectories in Compile := Seq((scalaSource in Compile).value)
unmanagedSourceDirectories in Test := Seq((scalaSource in Test).value)

// ### COMPILE SETTINGS ### //
javacOptions ++= Seq(
  "-encoding", "UTF-8"
, "-deprecation"
, "-Xlint"
, "-target", "1.6"
, "-source", "1.6"
) ++ (sys.env.get("JAVA6_HOME") match {
  case Some(java6Home) => Seq("-bootclasspath", java6Home + "/lib/rt.jar")
  case _ => Nil
})
crossScalaVersions := Seq("2.8.1", "2.8.2")
scalaVersion := crossScalaVersions.value.head
scalacOptions ++= Seq(
  "-deprecation"
, "-encoding", "UTF-8"
, "-optimise"
, "-unchecked"
, "-Xfatal-warnings"
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
  }.x/src/main/scala\u20AC{FILE_PATH}.scala"""
)
