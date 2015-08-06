package io.jvm.uuid

object StaticUUID {
  def random: UUID =
    UUID.randomUUID()

  def apply(uuid: String): UUID =
    UUID.fromString(uuid)

  def apply(mostSigBits: Long, leastSigBits: Long): UUID =
    new UUID(mostSigBits, leastSigBits)

  def apply(bits: (Long, Long)): UUID =
    new UUID(bits._1, bits._2)

  def apply(uuid: Array[Byte]): UUID = {
    require(uuid ne null, "UUID cannot be created from a null byte array")
    require(uuid.length == 16, "Invalid size of input byte array, expected 16 but got " + uuid.length + " bytes")

    apply(
      (uuid( 0)       ).toLong << 56
    | (uuid( 1) & 0xff).toLong << 48
    | (uuid( 2) & 0xff).toLong << 40
    | (uuid( 3) & 0xff).toLong << 32
    | (uuid( 4) & 0xff).toLong << 24
    | (uuid( 5) & 0xff)        << 16
    | (uuid( 6) & 0xff)        <<  8
    | (uuid( 7) & 0xff)
    , (uuid( 8)       ).toLong << 56
    | (uuid( 9) & 0xff).toLong << 48
    | (uuid(10) & 0xff).toLong << 40
    | (uuid(11) & 0xff).toLong << 32
    | (uuid(12) & 0xff).toLong << 24
    | (uuid(13) & 0xff)        << 16
    | (uuid(14) & 0xff)        <<  8
    | (uuid(15) & 0xff)
    )
  }

  def unapply(uuid: String): Option[UUID] =
    try {
      Some(apply(uuid))
    } catch {
      case _: Exception =>
        None
    }

  def unapply(uuid: UUID): Option[String] =
    Some(uuid.toString)

  // --- Forwarders ---

  def fromString(name: String): UUID =
    java.util.UUID.fromString(name)

  def randomUUID(): UUID =
    java.util.UUID.randomUUID()

  def nameUUIDFromBytes(name: Array[Byte]): UUID =
    java.util.UUID.nameUUIDFromBytes(name)
}
