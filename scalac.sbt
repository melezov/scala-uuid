scalaVersion := "2.11.12"

scalacOptions ++= Seq(
  "-deprecation"               // Emit warning and location for usages of deprecated APIs.
, "-encoding", "UTF-8"         // Specify character encoding used by source files.
, "-feature"                   // Emit warning and location for usages of features that should be imported explicitly.
, "-language:_"                // Enable or disable language features: `_' for all, `-language:help' to list choices.
, "-optimise"                  // Generates faster bytecode by applying optimisations to the program
, "-target:jvm-1.6"            // Target platform for object files. All JVM 1.5 targets are deprecated. (jvm-1.5,jvm-1.6,jvm-1.7,jvm-1.8) default:jvm-1.6
, "-unchecked"                 // Enable additional warnings where generated code depends on assumptions.

, "-Xcheckinit"                // Wrap field accessors to throw an exception on uninitialized access.
, "-Xdev"                      // Indicates user is a developer - issue warnings about anything which seems amiss
, "-Xexperimental"             // Enable experimental extensions.
, "-Xfuture"                   // Turn on future language features.
, "-Xlint:_"                   // Enable or disable specific warnings: `_' for all, `-Xlint:help' to list choices.
, "-Xlog-reflective-calls"     // Print a message when a reflective method call is generated
, "-Xmax-classfile-name", "72" // Maximum filename length for generated classes
, "-Xmigration:2.11"           // Warn about constructs whose behavior may have changed since version.
, "-Xsource:2.11"              // Treat compiler input as Scala source for the specified version, see scala/bug#8126.
, "-Xstrict-inference"         // Don't infer known-unsound types
, "-Xverify"                   // Verify generic signatures in generated bytecode.

, "-Ygen-asmp", "target/asmp"  // Generate a parallel output directory of .asmp files (ie ASM Textifier output).
, "-Yno-adapted-args"          // Do not adapt an argument list (either by inserting () or creating a tuple) to match the receiver.
, "-Yrangepos"                 // Use range positions for syntax trees.
, "-Yrepl-sync"                // Do not use asynchronous code for repl startup
, "-Ywarn-inaccessible"        // Warn about inaccessible types in method signatures.
, "-Ywarn-infer-any"           // Warn when a type argument is inferred to be `Any`.
, "-Ywarn-nullary-override"    // Warn when non-nullary `def f()' overrides nullary `def f'.
, "-Ywarn-nullary-unit"        // Warn when nullary methods return Unit.
, "-Ywarn-numeric-widen"       // Warn when numerics are widened.
, "-Ywarn-unused"              // Warn when local and private vals, vars, defs, and types are unused.
, "-Ywarn-unused-import"       // Warn when imports are unused.
, "-Ywarn-value-discard"       // Warn when non-Unit expression results are unused.
)
