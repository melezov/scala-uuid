// ### PUBLISH SETTINGS ###
publishTo := Some(
  if (version.value endsWith "-SNAPSHOT")
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
)

licenses += (("BSD-style", url("http://opensource.org/licenses/BSD-3-Clause")))
startYear := Some(2013)

scmInfo := Some(ScmInfo(
  url("https://github.com/melezov/scala-uuid")
, "scm:git:https://github.com/melezov/scala-uuid.git"
, Some("scm:git:git@github.com:melezov/scala-uuid.git")
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
