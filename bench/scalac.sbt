scalaVersion := "2.12.9"
scalacOptions in Compile ++= Seq(
  "-deprecation",              // Emit warning and location for usages of deprecated APIs.
  "-encoding", "UTF-8",        // Specify character encoding used by source files.
  "-target:jvm-1.8",           // Target platform for object files. All JVM 1.5 - 1.7 targets are deprecated. Choices: (jvm-1.5,jvm-1.6,jvm-1.7,jvm-1.8), default: jvm-1.8.
  "-unchecked",                // Enable additional warnings where generated code depends on assumptions.
  "-Yrangepos",                // Use range positions for syntax trees.
  "-Ywarn-numeric-widen",      // Warn when numerics are widened.
  "-Ywarn-value-discard",      // Warn when non-Unit expression results are unused.

  "-opt:l:method,inline",      // Enable optimizations, `-opt:help' to list choices.
  "-opt-inline-from:io.**",    // Patterns for classfile names from which to allow inlining, `help` for details.
  "-opt-warnings:_",           // Enable optimizer warnings

// causing issues with SBT 1.3.0-RC3 && Scala 2.12.9 TODO: investigate
//, "-Ygen-asmp", "target/asmp" // Generate a parallel output directory of .asmp files (ie ASM Textifier output).
)

scalacOptions in Test -= "-opt:l:method,inline" // do not inline tests
