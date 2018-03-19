package io.jvm.uuid
package bench

import java.util.Locale
import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations._

@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
class ToStringBenchmark {
  var randomUUID: UUID = _

  @Setup(Level.Invocation)
  def setup(): Unit = {
    randomUUID = UUID.random
  }

  @Benchmark
  def legacyToLowerString: String =
    randomUUID.toString

  @Benchmark
  def optimizedToLowerString: String =
    randomUUID.string

  @Benchmark
  def legacyToUpperString: String =
    randomUUID.toString.toUpperCase(Locale.ROOT)

  @Benchmark
  def optimizedToUpperString: String =
    randomUUID.toUpperCase
}
