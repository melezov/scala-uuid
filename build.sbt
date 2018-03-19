organization := "io.jvm.uuid"
name := "scala-uuid"
version := "0.2.4"

unmanagedSourceDirectories in Compile := Seq((scalaSource in Compile).value)
unmanagedSourceDirectories in Test := Seq((scalaSource in Test).value)

// ### DEPENDENCIES ### //
libraryDependencies += "org.specs2" %% "specs2-scalacheck" % "4.0.3" % "test"

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
