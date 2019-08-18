scalaVersion := "2.10.7"
scalacOptions in Compile ++= Seq(
  "-deprecation",              // Emit warning and location for usages of deprecated APIs.
  "-encoding", "UTF-8",        // Specify character encoding used by source files.
  "-target:jvm-1.6",           // Target platform for object files. All JVM 1.5 targets are deprecated. (jvm-1.5,jvm-1.6,jvm-1.7,jvm-1.8) default:jvm-1.6
  "-unchecked",                // Enable additional warnings where generated code depends on assumptions.
  "-Yrangepos",                // Use range positions for syntax trees.
  "-Ywarn-numeric-widen",      // Warn when numerics are widened.
  "-Ywarn-value-discard",      // Warn when non-Unit expression results are unused.

  "-optimise",                 // Generates faster bytecode by applying optimisations to the program
)

scalacOptions in Test -= "-optimise" // do not inline tests
