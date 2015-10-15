package io.jvm.uuid

object StaticUUID {
  def random: UUID =
    UUID.randomUUID()

  def apply(uuid: String): UUID =
    UUID.fromString(uuid)

  private[this] val Lookup = {
    val buffer = new Array[Int]('f' + 1)
    for (i <- 0 to 'f') {
      buffer(i) =
        if (i >= '0' && i <= '9') i - '0'
        else if (i >= 'A' && i <= 'F') i - 'A' + 10
        else if (i >= 'a' && i <= 'f') i - 'a' + 10
        else -1
    }
    buffer
  }

  def apply(uuid: String, strict: Boolean): UUID = if (strict) {
    unapply(uuid).getOrElse(throw new IllegalArgumentException(
      s"UUID must be in format xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx, where x is a hexadecimal digit (got: ${uuid})"))
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
    new UUID(
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
  def unapply(uuid: String): Option[UUID] = {
    if (uuid.length != 36 ||
        uuid.charAt( 8) != '-' ||
        uuid.charAt(13) != '-' ||
        uuid.charAt(18) != '-' ||
        uuid.charAt(23) != '-') {
      None
    } else try {
      val msb3 = (
        (Lookup(uuid.charAt( 0).toInt) << 12)
      | (Lookup(uuid.charAt( 1).toInt) <<  8)
      | (Lookup(uuid.charAt( 2).toInt) <<  4)
      | (Lookup(uuid.charAt( 3).toInt)      )
      )

      val msb2 = (
        (Lookup(uuid.charAt( 4).toInt) << 12)
      | (Lookup(uuid.charAt( 5).toInt) <<  8)
      | (Lookup(uuid.charAt( 6).toInt) <<  4)
      | (Lookup(uuid.charAt( 7).toInt)      )
      )

      val msb1 = (
        (Lookup(uuid.charAt( 9).toInt) << 12)
      | (Lookup(uuid.charAt(10).toInt) <<  8)
      | (Lookup(uuid.charAt(11).toInt) <<  4)
      | (Lookup(uuid.charAt(12).toInt)      )
      )

      val msb0 = (
        (Lookup(uuid.charAt(14).toInt) << 12)
      | (Lookup(uuid.charAt(15).toInt) <<  8)
      | (Lookup(uuid.charAt(16).toInt) <<  4)
      | (Lookup(uuid.charAt(17).toInt)      )
      )

      val lsb3 = (
        (Lookup(uuid.charAt(19).toInt) << 12)
      | (Lookup(uuid.charAt(20).toInt) <<  8)
      | (Lookup(uuid.charAt(21).toInt) <<  4)
      | (Lookup(uuid.charAt(22).toInt)      )
      )

      val lsb2 = (
        (Lookup(uuid.charAt(24).toInt) << 12)
      | (Lookup(uuid.charAt(25).toInt) <<  8)
      | (Lookup(uuid.charAt(26).toInt) <<  4)
      | (Lookup(uuid.charAt(27).toInt)      )
      )

      val lsb1 = (
        (Lookup(uuid.charAt(28).toInt) << 12)
      | (Lookup(uuid.charAt(29).toInt) <<  8)
      | (Lookup(uuid.charAt(30).toInt) <<  4)
      | (Lookup(uuid.charAt(31).toInt)      )
      )

      val lsb0 = (
        (Lookup(uuid.charAt(32).toInt) << 12)
      | (Lookup(uuid.charAt(33).toInt) <<  8)
      | (Lookup(uuid.charAt(34).toInt) <<  4)
      | (Lookup(uuid.charAt(35).toInt)      )
      )

      if ((msb3 | msb2 | msb1 | msb0 | lsb3 | lsb2 | lsb1 | lsb0) < 0) None else {
        Some(new UUID(
          (((msb3 << 16) | msb2).toLong << 32) | (msb1.toLong << 16) | msb0
        , (((lsb3 << 16) | lsb2).toLong << 32) | (lsb1.toLong << 16) | lsb0
        ))
      }
    } catch {
      case _: ArrayIndexOutOfBoundsException =>
        None
    }
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
