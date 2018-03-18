package io.jvm.uuid
package bench

import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations._

@State(Scope.Thread)
@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
class FromStringBenchmark {
  var randomUUIDString: String = _

  @Setup(Level.Invocation)
  def setup(): Unit = {
    randomUUIDString = UUID.randomString
  }

  @Benchmark
  def legacyFromString: java.util.UUID =
    java.util.UUID.fromString(randomUUIDString)

  @Benchmark
  def optimizedApply: java.util.UUID =
    UUID(randomUUIDString)
}
