package io.jvm.uuid

trait Imports {
  type UUID = java.util.UUID
  val UUID = StaticUUID

  implicit def richUUID(uuid: UUID) =
    new RichUUID(uuid)
}
