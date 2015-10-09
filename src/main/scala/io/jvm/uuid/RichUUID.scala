package io.jvm.uuid

private object RichUUID {
  private val UppercaseLookup = "0123456789ABCDEF".toCharArray
  private val LowercaseLookup = "0123456789abcdef".toCharArray
}

class RichUUID private[uuid] (val uuid: UUID) extends AnyVal {
  def string: String = toStringViaLookup(RichUUID.LowercaseLookup)
  def toLowerCase: String = string
  def toUpperCase: String = toStringViaLookup(RichUUID.UppercaseLookup)

  private def toStringViaLookup(lookup: Array[Char]): String = {
    val msb = uuid.getMostSignificantBits
    val lsb = uuid.getLeastSignificantBits

    val msbh = (msb >>> 32).toInt
    val msbl = msb.toInt
    val lsbh = (lsb >>> 32).toInt
    val lsbl = lsb.toInt

    new String(Array(
      lookup((msbh >>> 28)      )
    , lookup((msbh >>> 24) & 0xf)
    , lookup((msbh >>> 20) & 0xf)
    , lookup((msbh >>> 16) & 0xf)
    , lookup((msbh >>> 12) & 0xf)
    , lookup((msbh >>>  8) & 0xf)
    , lookup((msbh >>>  4) & 0xf)
    , lookup((msbh       ) & 0xf), '-'
    , lookup((msbl >>> 28)      )
    , lookup((msbl >>> 24) & 0xf)
    , lookup((msbl >>> 20) & 0xf)
    , lookup((msbl >>> 16) & 0xf), '-'
    , lookup((msbl >>> 12) & 0xf)
    , lookup((msbl >>>  8) & 0xf)
    , lookup((msbl >>>  4) & 0xf)
    , lookup((msbl       ) & 0xf), '-'
    , lookup((lsbh >>> 28)      )
    , lookup((lsbh >>> 24) & 0xf)
    , lookup((lsbh >>> 20) & 0xf)
    , lookup((lsbh >>> 16) & 0xf), '-'
    , lookup((lsbh >>> 12) & 0xf)
    , lookup((lsbh >>>  8) & 0xf)
    , lookup((lsbh >>>  4) & 0xf)
    , lookup((lsbh       ) & 0xf)
    , lookup((lsbl >>> 28)      )
    , lookup((lsbl >>> 24) & 0xf)
    , lookup((lsbl >>> 20) & 0xf)
    , lookup((lsbl >>> 16) & 0xf)
    , lookup((lsbl >>> 12) & 0xf)
    , lookup((lsbl >>>  8) & 0xf)
    , lookup((lsbl >>>  4) & 0xf)
    , lookup((lsbl       ) & 0xf)
    ))
  }

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

  def toByteArray(buffer: Array[Byte], offset: Int): Unit = {
    val msb = uuid.getMostSignificantBits
    val lsb = uuid.getLeastSignificantBits

    buffer(offset     ) = (msb >>> 56).toByte
    buffer(offset +  1) = (msb >>> 48).toByte
    buffer(offset +  2) = (msb >>> 40).toByte
    buffer(offset +  3) = (msb >>> 32).toByte
    buffer(offset +  4) = (msb >>> 24).toByte
    buffer(offset +  5) = (msb >>> 16).toByte
    buffer(offset +  6) = (msb >>>  8).toByte
    buffer(offset +  7) = (msb       ).toByte
    buffer(offset +  8) = (lsb >>> 56).toByte
    buffer(offset +  9) = (lsb >>> 48).toByte
    buffer(offset + 10) = (lsb >>> 40).toByte
    buffer(offset + 11) = (lsb >>> 32).toByte
    buffer(offset + 12) = (lsb >>> 24).toByte
    buffer(offset + 13) = (lsb >>> 16).toByte
    buffer(offset + 14) = (lsb >>>  8).toByte
    buffer(offset + 15) = (lsb       ).toByte
  }
}
