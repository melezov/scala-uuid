package io.jvm.uuid

object StaticUUID {
  def random: UUID =
    UUID.randomUUID()

  def apply(uuid: String): UUID =
    UUID.fromString(uuid)

  private[this] val Lookup = {
    val buffer = new Array[Int]('f' + 1)
    ('0' to '9') foreach { ch => buffer(ch.toInt) = ch - '0' }
    ('A' to 'F') foreach { ch => buffer(ch.toInt) = ch - 'A' + 10 }
    ('a' to 'f') foreach { ch => buffer(ch.toInt) = ch - 'a' + 10 }
    buffer
  }

  def apply(uuid: String, strict: Boolean): UUID = if (strict) {
    require(
      uuid.length == 36 &&
      uuid.charAt( 8) == '-' &&
      uuid.charAt(13) == '-' &&
      uuid.charAt(18) == '-' &&
      uuid.charAt(23) == '-', "UUID must be in format xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx")

    try {
      val msbh = (
        (Lookup(uuid.charAt( 0).toInt) << 28)
      | (Lookup(uuid.charAt( 1).toInt) << 24)
      | (Lookup(uuid.charAt( 2).toInt) << 20)
      | (Lookup(uuid.charAt( 3).toInt) << 16)
      | (Lookup(uuid.charAt( 4).toInt) << 12)
      | (Lookup(uuid.charAt( 5).toInt) <<  8)
      | (Lookup(uuid.charAt( 6).toInt) <<  4)
      | (Lookup(uuid.charAt( 7).toInt)      )
      ).toLong << 32

      val msbl = (
        (Lookup(uuid.charAt( 9).toInt) << 28)
      | (Lookup(uuid.charAt(10).toInt) << 24)
      | (Lookup(uuid.charAt(11).toInt) << 20)
      | (Lookup(uuid.charAt(12).toInt) << 16)
      | (Lookup(uuid.charAt(14).toInt) << 12)
      | (Lookup(uuid.charAt(15).toInt) <<  8)
      | (Lookup(uuid.charAt(16).toInt) <<  4)
      | (Lookup(uuid.charAt(17).toInt)      )
      ) & 0xffffffffL

      val lsbh = (
        (Lookup(uuid.charAt(19).toInt) << 28)
      | (Lookup(uuid.charAt(20).toInt) << 24)
      | (Lookup(uuid.charAt(21).toInt) << 20)
      | (Lookup(uuid.charAt(22).toInt) << 16)
      | (Lookup(uuid.charAt(24).toInt) << 12)
      | (Lookup(uuid.charAt(25).toInt) <<  8)
      | (Lookup(uuid.charAt(26).toInt) <<  4)
      | (Lookup(uuid.charAt(27).toInt)      )
      ).toLong << 32

      val lsbl = (
        (Lookup(uuid.charAt(28).toInt) << 28)
      | (Lookup(uuid.charAt(29).toInt) << 24)
      | (Lookup(uuid.charAt(30).toInt) << 20)
      | (Lookup(uuid.charAt(31).toInt) << 16)
      | (Lookup(uuid.charAt(32).toInt) << 12)
      | (Lookup(uuid.charAt(33).toInt) <<  8)
      | (Lookup(uuid.charAt(34).toInt) <<  4)
      | (Lookup(uuid.charAt(35).toInt)      )
      ) & 0xffffffffL

      new UUID(msbh | msbl, lsbh | lsbl)
    } catch {
      case _: ArrayIndexOutOfBoundsException =>
        sys.error("UUID must be in format xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx, where x is a hexadecimal digit")
    }
  } else {
    UUID.fromString(uuid)
  }

  def apply(mostSigBits: Long, leastSigBits: Long): UUID =
    new UUID(mostSigBits, leastSigBits)

  def apply(bits: (Long, Long)): UUID = {
    new UUID(bits._1, bits._2)
  }

  def apply(uuid: Array[Byte]): UUID = {
    require(uuid.length == 16, s"Invalid size of input byte array, expected 16 but got ${uuid.length} bytes")

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

  /** UUID must be in format xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx */
  def unapply(uuid: String): Option[UUID] =
    try {
      Some(apply(uuid, true))
    } catch {
      case _: Exception =>
        None
    }

  def unapply(uuid: UUID): Option[String] =
    Some(uuid.string)

  // --- Forwarders ---

  def fromString(name: String): UUID =
    java.util.UUID.fromString(name)

  def randomUUID(): UUID =
    java.util.UUID.randomUUID()

  def nameUUIDFromBytes(name: Array[Byte]): UUID =
    java.util.UUID.nameUUIDFromBytes(name)
}
