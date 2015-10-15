package io.jvm.uuid

import scala.util.Try

class UUIDFeatureSpec
    extends org.specs2.Specification {

  def is = s2"""
  Constructors
    from strict string                 $fromStrictString
    from non-strict string             $fromNonStrictString
    from strict string with check      $fromStrictStringWithCheck
    from non-strict string with check  $fromNonStrictStringWithCheck
    from two longs                     $fromTwoLongs
    from longs tuple                   $fromLongsTuple
    from byte array                    $fromByteArray
    from byte array (failure)          $fromByteArrayFailure

  Accessors
    to string                          $toDefaultCaseString
    to lower-case string               $toLowerCaseString
    to upper-case string               $toUpperCaseString
    to msb, lsb longs                  $longAccessors
    to longs tuple                     $toLongsTuple
    to byte array                      $toByteArray

  Extractors
    string extractor                   $stringExtractor
    uuid extractor                     $uuidExtractor
    uuid extractor (failure)           $uuidExtractorFailure
"""

  private val u159bf = java.util.UUID.fromString("1-5-9-b-f")

  // -- Constructors --

  def fromStrictString =
    UUID("00000001-0005-0009-000b-00000000000f") === u159bf

  def fromNonStrictString =
    UUID("1-5-9-b-f") === u159bf

  def fromStrictStringWithCheck =
    UUID("00000001-0005-0009-000b-00000000000f", true) === u159bf

  def fromNonStrictStringWithCheck =
    Try { UUID("1-5-9-b-f", true) } isFailure

  def fromTwoLongs =
    UUID(0x100050009L, 0xb00000000000fL) === u159bf

  def fromLongsTuple =
    UUID((0x100050009L, 0xb00000000000fL)) === u159bf

  def fromByteArray =
    UUID(Array[Byte](0, 0, 0, 1, 0, 5, 0, 9, 0, 0xb, 0, 0, 0, 0, 0, 0xf)) === u159bf

  def fromByteArrayFailure = Try {
    UUID(new Array[Byte](17))
  } isFailure

  // -- Accessors --

  def toDefaultCaseString =
    u159bf.string === "00000001-0005-0009-000b-00000000000f"

  def toLowerCaseString =
    u159bf.toLowerCase === "00000001-0005-0009-000b-00000000000f"

  def toUpperCaseString =
    u159bf.toUpperCase === "00000001-0005-0009-000B-00000000000F"

  def longAccessors =
    u159bf.mostSigBits === 0x100050009L &&
    u159bf.leastSigBits === 0xb00000000000fL

  def toLongsTuple =
    u159bf.longs === ((0x100050009L, 0xb00000000000fL))

  def toByteArray =
    u159bf.byteArray === Array[Byte](0, 0, 0, 1, 0, 5, 0, 9, 0, 0xb, 0, 0, 0, 0, 0, 0xf)

  // -- Extractors --

  def stringExtractor = {
    val UUID(strValue) = u159bf
    strValue === "00000001-0005-0009-000b-00000000000f"
  }

  def uuidExtractor = {
    val UUID(uuidValue) = "00000001-0005-0009-000b-00000000000f" // will fail if not strict!
    uuidValue === UUID("1-5-9-b-f")
  }

  def uuidExtractorFailure = {
    "1-5-9-b-f" match {
      case UUID(uuid) => sys.error("Should not match - it wasn't strict!")
      case _ => true
    }
  }
}
