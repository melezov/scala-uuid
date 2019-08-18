crossScalaVersions := Seq("2.9.0", "2.9.0-1", "2.9.1", "2.9.1-1", "2.9.2", "2.9.3")
scalaVersion := crossScalaVersions.value.head
scalacOptions ++= Seq(
  "-deprecation",              // Emit warning and location for usages of deprecated APIs.
  "-encoding", "UTF-8",        // Specify character encoding used by source files.
  "-target:jvm-1.5",           // Target platform for object files. (jvm-1.5,msil) default:jvm-1.5
  "-unchecked",                // Enable detailed unchecked (erasure) warnings.
  "-optimise"                  // Generates faster bytecode by applying optimisations to the program
)
