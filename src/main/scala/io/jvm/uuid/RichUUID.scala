package io.jvm.uuid

private[uuid] object RichUUID {
  /** Upper-case hexadecimal translation lookup. */
  private val UppercaseLookup: Array[Char] = "0123456789ABCDEF".toCharArray
  /** Lower-case hexadecimal translation lookup. */
  private val LowercaseLookup: Array[Char] = "0123456789abcdef".toCharArray

  /** Oracle optimized toString in 9, no sense to compete with future versions */
  private val UseNativeToString: Boolean =
    try {
      sys.props("java.vendor") == "Oracle Corporation" &&
      sys.props("java.specification.version").toInt >= 9
    } catch {
      case _: Exception => false
    }

  /** Char buffer to be used by the optimized .string method */
  private val charBuffer: ThreadLocal[Array[Char]] = new ThreadLocal[Array[Char]] {
    override def initialValue(): Array[Char] = new Array[Char](36)
  }
}

/** Pimp-my-library pattern, wrapping the underlying `java.util.UUID`.
  *
  * This class extends AnyVal, making all the extension methods have
  * little-to-no runtime overhead.
  *
  * The pimp is complete through an implicit conversion in the
  * [[Imports]] trait or the [[io.jvm.uuid.package uuid]] package object. */
final class RichUUID private[uuid](private val uuid: UUID) extends AnyVal with Ordered[UUID] {
  /** Returns the most significant 64 bits of this `UUID`. */
  @inline def mostSigBits: Long = uuid.getMostSignificantBits

  /** Returns the least significant 64 bits of this `UUID`. */
  @inline def leastSigBits: Long = uuid.getLeastSignificantBits

  /** Encodes this `UUID` as a `Long` array with 2 elements. */
  def longArray: Array[Long] = {
    val buffer = new Array[Long](2)
    toLongArray(buffer, 0)
    buffer
  }

  /** Writes this `UUID` to the provided `Long` array. */
  @inline def toLongArray(buffer: Array[Long], offset: Int): Unit = {
    buffer(offset    ) = uuid.getMostSignificantBits
    buffer(offset + 1) = uuid.getLeastSignificantBits
  }

  /** Encodes this `UUID` as an `Int` array with 4 elements. */
  def intArray: Array[Int] = {
    val buffer = new Array[Int](4)
    toIntArray(buffer, 0)
    buffer
  }

  /** Writes this `UUID` to the provided `Int` array. */
  @inline def toIntArray(buffer: Array[Int], offset: Int): Unit = {
    val msb = uuid.getMostSignificantBits
    buffer(offset    ) = (msb >> 32).toInt
    buffer(offset + 1) = msb.toInt

    val lsb = uuid.getLeastSignificantBits
    buffer(offset + 2) = (lsb >> 32).toInt
    buffer(offset + 3) = lsb.toInt
  }

  /** Encodes this `UUID` as a `Short` array with 8 elements. */
  def shortArray: Array[Short] = {
    val buffer = new Array[Short](8)
    toShortArray(buffer, 0)
    buffer
  }

  /** Writes this `UUID` to the provided `Short` array. */
  @inline def toShortArray(buffer: Array[Short], offset: Int): Unit = {
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
  def byteArray: Array[Byte] = {
    val buffer = new Array[Byte](16)
    toByteArray(buffer, 0)
    buffer
  }

  /** Writes this `UUID` to the provided `Byte` array. */
  @inline def toByteArray(buffer: Array[Byte], offset: Int): Unit = {
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
  def charArray: Array[Char] = {
    val buffer = new Array[Char](36)
    toCharArrayViaLookup(buffer, 0, RichUUID.LowercaseLookup)
    buffer
  }

  /** Writes this `UUID` to the provided `Char` array in `xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx` format. */
  def toCharArray(buffer: Array[Char], offset: Int): Unit =
    toCharArrayViaLookup(buffer, offset, RichUUID.LowercaseLookup)

  /** Serializes this `UUID` to the provided `Char` array via a translation matrix. */
  @inline private[this] def toCharArrayViaLookup(buffer: Array[Char], offset: Int, lookup: Array[Char]): Unit = {
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
  def string: String =
    if (RichUUID.UseNativeToString) {
      uuid.toString
    } else {
      toStringViaLookup(RichUUID.LowercaseLookup)
    }

  /** Alias for `string` which implicitly returns a lower-cased `String`. */
  @inline def toLowerCase: String = string

  /** Returns this `UUID` as a `String` in `xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx` format.
    * Hexadecimal characters will be upper-cased. */
  def toUpperCase: String = toStringViaLookup(RichUUID.UppercaseLookup)

  /** Translate this `UUID` to a `String` via the provided lookup.
    * This method should be inlined, to constant-fold the offset. */
  @inline private[this] def toStringViaLookup(lookup: Array[Char]): String = {
    val buffer = RichUUID.charBuffer.get()
    toCharArrayViaLookup(buffer, 0, lookup)
    new String(buffer) // return ownership of the buffer to ThreadLocal
  }

  /** WARNING: JVM sorts UUIDs differently to the rest of the world (languages and databases).
    * This is due to default signed Long ordering and has been marked as a Will Not Fix
    * due to legacy code: https://bugs.java.com/bugdatabase/view_bug.do?bug_id=7025832 */
  override def compareTo(that: UUID): Int = uuid compareTo that

  /** This comparison allows for sanity compatible unsigned ordering */
  override def compare(that: UUID): Int = {
    val umsb = uuid.getMostSignificantBits
    val tmsb = that.getMostSignificantBits
    if (umsb != tmsb) {
      if (umsb + Long.MinValue < tmsb + Long.MinValue) -1 else 1
    } else {
      val ulsb = uuid.getLeastSignificantBits
      val tlsb = that.getLeastSignificantBits
      if (ulsb != tlsb) {
        if (ulsb + Long.MinValue < tlsb + Long.MinValue) -1 else 1
      } else {
        0
      }
    }
  }
}
