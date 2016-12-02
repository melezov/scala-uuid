package io.jvm.uuid

private[this] object RichUUID {
  /** Upper-case hexadecimal translation lookup. */
  private final val UppercaseLookup: Array[Char] = "0123456789ABCDEF".toCharArray
  /** Lower-case hexadecimal translation lookup. */
  private final val LowercaseLookup: Array[Char] = "0123456789abcdef".toCharArray
}

/** Pimp-my-library pattern, wrapping the underlying `java.util.UUID`.
  *
  * This class extends AnyVal, making all the extension methods have
  * little-to-no runtime overhead.
  *
  * The pimp is complete through an implicit conversion in the
  * [[Imports]] trait or the [[io.jvm.uuid.package uuid]] package object. */
class RichUUID private[uuid] (val uuid: UUID) extends AnyVal {
  /** Returns the most significant 64 bits of this `UUID`. */
  final def mostSigBits: Long = uuid.getMostSignificantBits

  /** Returns the least significant 64 bits of this `UUID`. */
  final def leastSigBits: Long = uuid.getLeastSignificantBits

  /** Encodes this `UUID` as a `Long` array with 2 elements. */
  final def longArray: Array[Long] = {
    val buffer = new Array[Long](2)
    toLongArray(buffer, 0)
    buffer
  }

  /** Writes this `UUID` to the provided `Long` array. */
  @inline final def toLongArray(buffer: Array[Long], offset: Int): Unit = {
    buffer(offset    ) = uuid.getMostSignificantBits
    buffer(offset + 1) = uuid.getLeastSignificantBits
  }

  /** Encodes this `UUID` as an `Int` array with 4 elements. */
  final def intArray: Array[Int] = {
    val buffer = new Array[Int](4)
    toIntArray(buffer, 0)
    buffer
  }

  /** Writes this `UUID` to the provided `Int` array. */
  @inline final def toIntArray(buffer: Array[Int], offset: Int): Unit = {
    val msb = uuid.getMostSignificantBits
    buffer(offset    ) = (msb >> 32).toInt
    buffer(offset + 1) = msb.toInt

    val lsb = uuid.getLeastSignificantBits
    buffer(offset + 2) = (lsb >> 32).toInt
    buffer(offset + 3) = lsb.toInt
  }

  /** Encodes this `UUID` as a `Short` array with 8 elements. */
  final def shortArray: Array[Short] = {
    val buffer = new Array[Short](8)
    toShortArray(buffer, 0)
    buffer
  }

  /** Writes this `UUID` to the provided `Short` array. */
  @inline final def toShortArray(buffer: Array[Short], offset: Int): Unit = {
    val msb = uuid.getMostSignificantBits
    val msbh = (msb >> 32).toInt
    buffer(offset    ) = (msbh >> 16).toShort
    buffer(offset + 1) = msbh.toShort

    val msbl = msb.toInt
    buffer(offset + 2) = (msbl >> 16).toShort
    buffer(offset + 3) = msbl.toShort

    val lsb = uuid.getLeastSignificantBits
    val lsbh = (lsb >> 32).toInt
    buffer(offset + 4) = (lsbh >> 16).toShort
    buffer(offset + 5) = lsbh.toShort

    val lsbl = lsb.toInt
    buffer(offset + 6) = (lsbl >> 16).toShort
    buffer(offset + 7) = lsbl.toShort
  }

  /** Encodes this `UUID` as a `Byte` array with 16 elements. */
  final def byteArray: Array[Byte] = {
    val buffer = new Array[Byte](16)
    toByteArray(buffer, 0)
    buffer
  }

  /** Writes this `UUID` to the provided `Byte` array. */
  @inline final def toByteArray(buffer: Array[Byte], offset: Int): Unit = {
    val msb = uuid.getMostSignificantBits
    buffer(offset     ) = (msb >>> 56).toByte
    buffer(offset +  1) = (msb >>> 48).toByte
    buffer(offset +  2) = (msb >>> 40).toByte
    buffer(offset +  3) = (msb >>> 32).toByte
    buffer(offset +  4) = (msb >>> 24).toByte
    buffer(offset +  5) = (msb >>> 16).toByte
    buffer(offset +  6) = (msb >>>  8).toByte
    buffer(offset +  7) = (msb       ).toByte

    val lsb = uuid.getLeastSignificantBits
    buffer(offset +  8) = (lsb >>> 56).toByte
    buffer(offset +  9) = (lsb >>> 48).toByte
    buffer(offset + 10) = (lsb >>> 40).toByte
    buffer(offset + 11) = (lsb >>> 32).toByte
    buffer(offset + 12) = (lsb >>> 24).toByte
    buffer(offset + 13) = (lsb >>> 16).toByte
    buffer(offset + 14) = (lsb >>>  8).toByte
    buffer(offset + 15) = (lsb       ).toByte
  }

  /** Encodes this `UUID` as a `Char` array with 36 elements in `xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx` format. */
  final def charArray: Array[Char] = {
    val buffer = new Array[Char](36)
    toCharArrayViaLookup(buffer, 0, RichUUID.LowercaseLookup)
    buffer
  }

  /** Writes this `UUID` to the provided `Char` array in `xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx` format. */
  final def toCharArray(buffer: Array[Char], offset: Int): Unit =
    toCharArrayViaLookup(buffer, offset, RichUUID.LowercaseLookup)

  /** Serializes this `UUID` to the provided `Char` array via a translation matrix. */
  @inline private[this] final def toCharArrayViaLookup(buffer: Array[Char], offset: Int, lookup: Array[Char]): Unit = {
    val msb = uuid.getMostSignificantBits
    val msbh = (msb >>> 32).toInt
    buffer(offset     ) = lookup((msbh >>> 28)      )
    buffer(offset +  1) = lookup((msbh >>> 24) & 0xf)
    buffer(offset +  2) = lookup((msbh >>> 20) & 0xf)
    buffer(offset +  3) = lookup((msbh >>> 16) & 0xf)
    buffer(offset +  4) = lookup((msbh >>> 12) & 0xf)
    buffer(offset +  5) = lookup((msbh >>>  8) & 0xf)
    buffer(offset +  6) = lookup((msbh >>>  4) & 0xf)
    buffer(offset +  7) = lookup((msbh       ) & 0xf)
    buffer(offset +  8) = '-'

    val msbl = msb.toInt
    buffer(offset + 9) = lookup((msbl >>> 28)       )
    buffer(offset + 10) = lookup((msbl >>> 24) & 0xf)
    buffer(offset + 11) = lookup((msbl >>> 20) & 0xf)
    buffer(offset + 12) = lookup((msbl >>> 16) & 0xf)
    buffer(offset + 13) = '-'
    buffer(offset + 14) = lookup((msbl >>> 12) & 0xf)
    buffer(offset + 15) = lookup((msbl >>>  8) & 0xf)
    buffer(offset + 16) = lookup((msbl >>>  4) & 0xf)
    buffer(offset + 17) = lookup((msbl       ) & 0xf)
    buffer(offset + 18) = '-'

    val lsb = uuid.getLeastSignificantBits
    val lsbh = (lsb >>> 32).toInt
    buffer(offset + 19) = lookup((lsbh >>> 28)      )
    buffer(offset + 20) = lookup((lsbh >>> 24) & 0xf)
    buffer(offset + 21) = lookup((lsbh >>> 20) & 0xf)
    buffer(offset + 22) = lookup((lsbh >>> 16) & 0xf)
    buffer(offset + 23) = '-'
    buffer(offset + 24) = lookup((lsbh >>> 12) & 0xf)
    buffer(offset + 25) = lookup((lsbh >>>  8) & 0xf)
    buffer(offset + 26) = lookup((lsbh >>>  4) & 0xf)
    buffer(offset + 27) = lookup((lsbh       ) & 0xf)

    val lsbl = lsb.toInt
    buffer(offset + 28) = lookup((lsbl >>> 28)      )
    buffer(offset + 29) = lookup((lsbl >>> 24) & 0xf)
    buffer(offset + 30) = lookup((lsbl >>> 20) & 0xf)
    buffer(offset + 31) = lookup((lsbl >>> 16) & 0xf)
    buffer(offset + 32) = lookup((lsbl >>> 12) & 0xf)
    buffer(offset + 33) = lookup((lsbl >>>  8) & 0xf)
    buffer(offset + 34) = lookup((lsbl >>>  4) & 0xf)
    buffer(offset + 35) = lookup((lsbl       ) & 0xf)
  }

  /** Returns this `UUID` as a `String` in `xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx` format.
    * Hexadecimal characters will be lower-cased.
    * This method is an optimized drop in replacement for the legacy `toString` method. */
  final def string: String = toStringViaLookup(RichUUID.LowercaseLookup)

  /** Alias for `string` which implicitly returns a lower-cased `String`. */
  @inline final def toLowerCase: String = string

  /** Returns this `UUID` as a `String` in `xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx` format.
    * Hexadecimal characters will be upper-cased. */
  final def toUpperCase: String = toStringViaLookup(RichUUID.UppercaseLookup)

  /** Translate this `UUID` to a `String` via the provided lookup.
    * This method should be inlined. */
  @inline private[this] final def toStringViaLookup(lookup: Array[Char]): String = {
    val buffer = new Array[Char](36)
    toCharArrayViaLookup(buffer, 0, lookup)
    new String(buffer)
  }
}
