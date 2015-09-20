package io.jvm.uuid

class RichUUID private[uuid] (val uuid: UUID) extends AnyVal {
  def string: String = uuid.toString

  def mostSigBits: Long = uuid.getMostSignificantBits
  def leastSigBits: Long = uuid.getLeastSignificantBits

  def longs: (Long, Long) = (uuid.getMostSignificantBits, uuid.getLeastSignificantBits)

  def byteArray: Array[Byte] = {
    val msb = uuid.getMostSignificantBits
    val lsb = uuid.getLeastSignificantBits

    Array(
      (msb >>> 56).toByte
    , (msb >>> 48).toByte
    , (msb >>> 40).toByte
    , (msb >>> 32).toByte
    , (msb >>> 24).toByte
    , (msb >>> 16).toByte
    , (msb >>>  8).toByte
    , (msb       ).toByte
    , (lsb >>> 56).toByte
    , (lsb >>> 48).toByte
    , (lsb >>> 40).toByte
    , (lsb >>> 32).toByte
    , (lsb >>> 24).toByte
    , (lsb >>> 16).toByte
    , (lsb >>>  8).toByte
    , (lsb       ).toByte
    )
  }

  def toByteArray(buffer: Array[Byte]): Unit = {
    val msb = uuid.getMostSignificantBits
    val lsb = uuid.getLeastSignificantBits

    buffer( 0) = (msb >>> 56).toByte
    buffer( 1) = (msb >>> 48).toByte
    buffer( 2) = (msb >>> 40).toByte
    buffer( 3) = (msb >>> 32).toByte
    buffer( 4) = (msb >>> 24).toByte
    buffer( 5) = (msb >>> 16).toByte
    buffer( 6) = (msb >>>  8).toByte
    buffer( 7) = (msb       ).toByte
    buffer( 8) = (lsb >>> 56).toByte
    buffer( 9) = (lsb >>> 48).toByte
    buffer(10) = (lsb >>> 40).toByte
    buffer(11) = (lsb >>> 32).toByte
    buffer(12) = (lsb >>> 24).toByte
    buffer(13) = (lsb >>> 16).toByte
    buffer(14) = (lsb >>>  8).toByte
    buffer(15) = (lsb       ).toByte
  }
}
