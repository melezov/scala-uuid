package io.jvm

/** This package holds the optimized Scala wrapper for `java.util.UUID`.
  *
  * To use the wrapper, either extend the [[uuid.Imports]] trait
  * or import this package object to bring the implicit into scope that way:
  * {{{
  * scala> import io.jvm.uuid._
  * import io.jvm.uuid._
  *
  * scala> val foo = classOf[UUID]
  * foo: Class[io.jvm.uuid.UUID] = class java.util.UUID
  *
  * scala> val bar = UUID(1, 2)
  * bar: io.jvm.uuid.UUID = 00000000-0000-0001-0000-000000000002
  *
  * scala> val UUID(baz) = "00112233-4455-6677-8899-aAbBcCdDeEfF"
  * baz: io.jvm.uuid.UUID = 00112233-4455-6677-8899-aabbccddeeff
  * }}} */
package object uuid extends Imports
