crossScalaVersions := Seq("2.9.0", "2.9.0-1", "2.9.1", "2.9.1-1", "2.9.2", "2.9.3")
scalaVersion := crossScalaVersions.value.head

scalacOptions ++= Seq(
  "-deprecation"               // Emit warning and location for usages of deprecated APIs.
, "-encoding", "UTF-8"         // Specify character encoding used by source files.
, "-optimise"                  // Generates faster bytecode by applying optimisations to the program
, "-target:jvm-1.5"            // Target platform for object files. (jvm-1.5,msil) default:jvm-1.5
, "-unchecked"                 // Enable additional warnings where generated code depends on assumptions.

, "-Xcheckinit"                // Wrap field accessors to throw an exception on uninitialized access.
, "-Xexperimental"             // Enable experimental extensions.
, "-Xfuture"                   // Turn on future language features.
, "-Xmax-classfile-name", "72" // Maximum filename length for generated classes
, "-Xmigration"                // Warn about constructs whose behavior may have changed between 2.7 and 2.8.
)
