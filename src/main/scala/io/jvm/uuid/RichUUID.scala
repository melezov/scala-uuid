package io.jvm

package object uuid extends Imports {
  implicit def toRichUUID(uuid: java.util.UUID) =
    new RichUUID(uuid)
}

package uuid {

class RichUUID private[uuid] (uuid: java.util.UUID) {
  def mostSigBits: Long = uuid.getMostSignificantBits
  def leastSigBits: Long = uuid.getLeastSignificantBits

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
}

}
