// ### BASIC SETTINGS ### //
organization := "io.jvm.uuid"
name := "scala-uuid"
version := "0.2.2"

unmanagedSourceDirectories in Compile := Seq((scalaSource in Compile).value)
unmanagedSourceDirectories in Test := Seq((scalaSource in Test).value)

// ### DEPENDENCIES ### //
libraryDependencies += "org.specs2" %% "specs2-scalacheck" % "3.8.6" % "test"

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
scalaVersion := "2.12.0"
scalacOptions ++= Seq(
  "-deprecation"
, "-encoding", "UTF-8"
, "-feature"
, "-language:implicitConversions"
, "-opt:_"
, "-unchecked"
, "-Xfatal-warnings"
, "-Xlint"
, "-Xmax-classfile-name", "72"
, "-Xno-forwarders"
, "-Xverify"
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
scalacOptions in (Compile, doc) ++= Seq(
  "-no-link-warnings"
, "-sourcepath", (scalaSource in Compile).value.toString
, "-doc-source-url", s"""https://github.com/melezov/scala-uuid/blob/${version.value}-${
    CrossVersion.partialVersion(scalaVersion.value).get.productIterator.mkString(".")
  }.x/src/main/scala\u20AC{FILE_PATH}.scala"""
)

wartremoverWarnings in (Compile, compile) ++= Seq(
  Wart.Any
, Wart.Any2StringAdd
, Wart.AsInstanceOf
, Wart.DefaultArguments
, Wart.EitherProjectionPartial
, Wart.Enumeration
, Wart.ExplicitImplicitTypes
, Wart.FinalCaseClass
, Wart.FinalVal
, Wart.IsInstanceOf
, Wart.JavaConversions
, Wart.LeakingSealed
, Wart.ListOps
, Wart.MutableDataStructures
, Wart.NoNeedForMonad
, Wart.NonUnitStatements
, Wart.Nothing
, Wart.Null
, Wart.Option2Iterable
, Wart.Product
, Wart.Return
, Wart.Serializable
, Wart.StringPlusAny
, Wart.ToString
, Wart.TraversableOps
, Wart.TryPartial
)
