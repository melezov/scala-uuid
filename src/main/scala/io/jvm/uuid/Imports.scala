package io.jvm.uuid

trait Imports {
  type UUID = java.util.UUID
  val UUID = StaticUUID

  implicit def toRichUUID(uuid: UUID): RichUUID =
    new RichUUID(uuid)
}
