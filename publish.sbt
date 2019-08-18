// ### SCALADOC SETTINGS ### //

scalacOptions in (Compile, doc) ++= Seq(
  "-no-link-warnings",
  "-sourcepath", (scalaSource in Compile).value.toString,
  "-doc-source-url", s"""https://github.com/melezov/scala-uuid/blob/${version.value}-${
    CrossVersion.partialVersion(scalaVersion.value).get.productIterator.mkString(".")
  }.x/src/main/scala\u20AC{FILE_PATH}.scala""",
)

// ### PUBLISH SETTINGS ### //

publishTo := Some(
  if (version.value endsWith "-SNAPSHOT")
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
)

licenses += (("BSD-style", url("http://opensource.org/licenses/BSD-3-Clause")))
startYear := Some(2013)

scmInfo := Some(ScmInfo(
  url("https://github.com/melezov/scala-uuid"),
  "scm:git:https://github.com/melezov/scala-uuid.git",
  Some("scm:git:git@github.com:melezov/scala-uuid.git"),
))

pomExtra :=
<developers>
  <developer>
    <id>melezov</id>
    <name>Marko Elezovi&#263;</name>
    <url>https://github.com/melezov</url>
  </developer>
</developers>

publishMavenStyle := true
publishArtifact in Test := false
pomIncludeRepository := { _ => false }

homepage := Some(url("https://github.com/melezov/scala-uuid"))

/*
// ### PROGUARD SETTINGS ### //

enablePlugins(SbtProguard)
proguardVersion in Proguard := "5.3"

proguardOptions in Proguard := {
  val programVer = version.value
  val scalaVer = scalaVersion.value
  val scalaBinVer = scalaBinaryVersion.value

  val baseDir = baseDirectory.value
  val binaryDeps = (proguardBinaryDeps in Proguard).value

  // read lib path from binary relations, failover to default Ivy cache location
  val scalaJarName = s"scala-library-${scalaVer}.jar"
  val scalaJarFile = binaryDeps find(_.name == scalaJarName) getOrElse (
    Path.userHome / s"/.ivy2/cache/org.scala-lang/scala-library/jars/${scalaJarName}"
  )

  // Try javaHome, then runtime JVM
  val jreMin = (javaHome.value).getOrElse(
    new File(sys.props("java.home"))
  )

  val runtimeCandidates = Seq("/jre/lib/rt.jar", "/lib/rt.jar") map { jreMin / }
  var runtimeJar = runtimeCandidates find { _.exists } getOrElse {
    sys.error("Could not locate runtime jar in path: " + jreMin)
  }

  val jarName = s"scala-uuid_${scalaBinVer}-${programVer}.jar"
  val inJar = s"${baseDir}/target/scala-${scalaBinVer}/${jarName}"
  val outJar = s"${baseDir}/target/scala-${scalaBinVer}/proguard/${jarName}"

  s"""
-injars '${inJar}'
-target 1.8
-libraryjars '${runtimeJar}'
-libraryjars '${scalaJarFile}'
-outjars '${outJar}'
-dontobfuscate
-optimizationpasses 32
-keep class io.jvm.uuid.** { *; }
""".trim.split("\n")
}
*/
