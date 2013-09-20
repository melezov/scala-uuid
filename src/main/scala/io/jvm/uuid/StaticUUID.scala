package io.jvm.uuid

object StaticUUID {
  def random: UUID =
    java.util.UUID.randomUUID()

  def apply(uuid: String): UUID =
    java.util.UUID.fromString(uuid)

  def apply(mostSigBits: Long, leastSigBits: Long): UUID =
    new java.util.UUID(mostSigBits, leastSigBits)

  def apply(bits: (Long, Long)): UUID =
    new java.util.UUID(bits._1, bits._2)

  def apply(uuid: Array[Byte]): UUID = {
    require(uuid ne null, "UUID cannot be created from a null byte array")
    require(uuid.length == 16, "Invalid size of input byte array, expected 16 but got " + uuid.length + " bytes")

    val ffl = 0xffL

    apply(
      (uuid( 0) & ffl) << 56
    | (uuid( 1) & ffl) << 48
    | (uuid( 2) & ffl) << 40
    | (uuid( 3) & ffl) << 32
    | (uuid( 4) & ffl) << 24
    | (uuid( 5) & ffl) << 16
    | (uuid( 6) & ffl) <<  8
    | (uuid( 7) & ffl)
    , (uuid( 8) & ffl) << 56
    | (uuid( 9) & ffl) << 48
    | (uuid(10) & ffl) << 40
    | (uuid(11) & ffl) << 32
    | (uuid(12) & ffl) << 24
    | (uuid(13) & ffl) << 16
    | (uuid(14) & ffl) <<  8
    | (uuid(15) & ffl)
    )
  }

  def unapply(uuid: String): Option[UUID] =
    try {
      Some(apply(uuid))
    }
    catch {
      case _: Exception =>
        None
    }

  def unapply(uuid: UUID): Option[String] =
    Some(uuid.toString)

// --- Forwarders ---

  def fromString(name: String) =
    java.util.UUID.fromString(name)

  def randomUUID() =
    java.util.UUID.randomUUID()

  def nameUUIDFromBytes(name: Array[Byte]) =
    java.util.UUID.nameUUIDFromBytes(name)
}
