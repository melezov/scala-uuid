package io.jvm.uuid

class UUIDFeatureSpec
    extends org.specs2.Specification {

  def is = s2"""
  Constructors
    from strict string      $fromStrictString
    from non-strict string  $fromNonStrictString
    from two longs          $fromTwoLongs
    from longs tuple        $fromLongsTuple
    from byte array         $fromByteArray

  Accessors
    to lower-case string    $toLowerCaseString
    to msb, lsb longs       $longAccessors
    to longs tuple          $toLongsTuple
    to byte array           $toByteArray

  Extractors
    string extractor        $stringExtractor
    uuid extractor          $uuidExtractor
"""

  private val u159bf = java.util.UUID.fromString("1-5-9-b-f")

  // -- Constructors --

  def fromStrictString =
    UUID("00000001-0005-0009-000b-00000000000f") === u159bf

  def fromNonStrictString =
    UUID("1-5-9-b-f") === u159bf

  def fromTwoLongs =
    UUID(0x100050009L, 0xb00000000000fL) === u159bf

  def fromLongsTuple =
    UUID((0x100050009L, 0xb00000000000fL)) === u159bf

  def fromByteArray =
    UUID(Array[Byte](0, 0, 0, 1, 0, 5, 0, 9, 0, 0xb, 0, 0, 0, 0, 0, 0xf)) === u159bf

  // -- Accessors --

  def toLowerCaseString =
    u159bf.string === "00000001-0005-0009-000b-00000000000f"

  def longAccessors =
    u159bf.mostSigBits === 0x100050009L and
    u159bf.leastSigBits === 0xb00000000000fL

  def toLongsTuple =
    u159bf.longs === ((0x100050009L, 0xb00000000000fL))

  def toByteArray =
    u159bf.byteArray === Array[Byte](0, 0, 0, 1, 0, 2, 0, 3, 0, 4, 0, 0, 0, 0, 0, 5)

  // -- Extractors --

  def stringExtractor = {
    val UUID(strValue) = u159bf
    strValue === "00000001-0005-0009-000b-00000000000f"
  }

  def uuidExtractor = {
    val UUID(uuidValue) = "1-5-9-b-f"
    uuidValue === UUID("1-5-9-b-f")
  }
}
