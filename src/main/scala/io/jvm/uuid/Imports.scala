package io.jvm.uuid

/** This trait holds all the components required for completing the pimp-my-library pattern:
  *  - an `UUID` type alias
  *  - an `UUID` singleton object with static forwarders and new `UUID` factories
  *  - an implicit def to provide extensions on the legacy `java.util.UUID` class
  *
  * Use case is to use it by extending it with your package object:
  * {{{
  * package com
  * package object example extends io.jvm.uuid.Imports
  * }}}
  *
  * Now any class in the `com.example` package will be able to access rich `UUID` functionality:
  * {{{
  * package com.example
  * class User(val id: UUID = UUID.random)
  * }}} */
trait Imports {
  final type UUID = java.util.UUID
  final val UUID: StaticUUID = StaticUUID

  implicit final def toRichUUID(uuid: UUID): RichUUID =
    new RichUUID(uuid)
}
