scalaVersion := "2.13.0"

scalacOptions ++= Seq(
  "-deprecation"               // Emit warning and location for usages of deprecated APIs.
, "-encoding", "UTF-8"         // Specify character encoding used by source files.
, "-feature"                   // Emit warning and location for usages of features that should be imported explicitly.
, "-language:_"                // Enable or disable language features: `_' for all, `-language:help' to list choices.
, "-opt:l:method,inline"       // Enable optimizations: `_' for all, `-opt:help' to list choices.
, "-opt-inline-from:**"        // Patterns for classfile names from which to allow inlining, `help` for details.
, "-opt-warnings:_"            // Enable optimizer warnings: `_' for all, `-opt-warnings:help' to list choices.
, "-target:jvm-1.8"            // Target platform for object files. All JVM 1.5 - 1.7 targets are deprecated. Choices: (jvm-1.5,jvm-1.6,jvm-1.7,jvm-1.8), default: jvm-1.8.
, "-unchecked"                 // Enable additional warnings where generated code depends on assumptions.

, "-Xcheckinit"                // Wrap field accessors to throw an exception on uninitialized access.
, "-Xdev"                      // Indicates user is a developer - issue warnings about anything which seems amiss
, "-Xfuture"                   // Turn on future language features.
, "-Xlint:_"                   // Enable or disable specific warnings: `_' for all, `-Xlint:help' to list choices.
, "-Xlog-reflective-calls"     // Print a message when a reflective method call is generated
, "-Xmigration:2.11"           // Warn about constructs whose behavior may have changed since version.
, "-Xsource:2.11"              // Treat compiler input as Scala source for the specified version, see scala/bug#8126.
, "-Xverify"                   // Verify generic signatures in generated bytecode.

// causing issues with SBT 1.2.7 && Scala 2.12.7 TODO: investigate
//, "-Ygen-asmp", "target/asmp"  // Generate a parallel output directory of .asmp files (ie ASM Textifier output).

, "-Yrangepos"                 // Use range positions for syntax trees.
, "-Ywarn-extra-implicit"      // Warn when more than one implicit parameter section is defined.
, "-Ywarn-numeric-widen"       // Warn when numerics are widened.
, "-Ywarn-unused:_"            // Enable or disable specific `unused' warnings: `_' for all, `-Ywarn-unused:help' to list choices.
, "-Ywarn-value-discard"       // Warn when non-Unit expression results are unused.
)
