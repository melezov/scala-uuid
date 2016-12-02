package io.jvm.uuid

import scala.util.Try

class UUIDFeatureSpec
    extends org.specs2.Specification {

  def is = s2"""
  Primary constructor
    from two longs                     $fromTwoLongs

  String constructors
    from strict                        $fromStrictString
    from non-strict (fail)             $fromNonStrictString
    from strict                        $fromStrictStringWithCheck
    from non-strict with check (fail)  $fromNonStrictStringWithCheck
    from strict without check          $fromStrictStringWithoutCheck
    from non-strict without check      $fromNonStrictStringWithoutCheck

  Array constructors
    from long array                    $fromLongArray
    from int array                     $fromIntArray
    from short array                   $fromShortArray
    from byte array                    $fromByteArray

  Accessors
    to msb, lsb longs                  $longAccessors
    to long array                      $toLongArray
    to int array                       $toIntArray
    to short array                     $toShortArray
    to byte array                      $toByteArray
    to char array                      $toCharArray
    to string                          $toDefaultCaseString
    to lower-case string               $toLowerCaseString
    to upper-case string               $toUpperCaseString

  Extractors
    uuid extractor                     $uuidExtractor
    uuid extractor (failure)           $uuidExtractorFailure
"""

  private val u159bf = java.util.UUID.fromString("1-5-9-b-f")

  // Primary constructor

  def fromTwoLongs =
    UUID(0x100050009L, 0xb00000000000fL) ==== u159bf

  // String constructors

  def fromStrictString =
    UUID("00000001-0005-0009-000b-00000000000f") ==== u159bf

  def fromNonStrictString =
    Try { UUID("1-5-9-b-f") ==== u159bf } isFailure

  def fromStrictStringWithCheck =
    UUID("00000001-0005-0009-000b-00000000000f", true) ==== u159bf

  def fromNonStrictStringWithCheck =
    Try { UUID("1-5-9-b-f", true) } isFailure

  def fromStrictStringWithoutCheck =
    UUID("00000001-0005-0009-000b-00000000000f", false) ==== u159bf

  def fromNonStrictStringWithoutCheck =
    UUID("1-5-9-b-f", false) ==== u159bf

  // Array constructors

  def fromLongArray =
    UUID(Array(0x100050009L, 0xb00000000000fL)) ==== u159bf

  def fromIntArray =
    UUID(Array(1, 0x50009, 0xb0000, 0xf)) ==== u159bf

  def fromShortArray =
    UUID(Array[Short](0, 1, 5, 9, 0xb, 0, 0, 0xf)) ==== u159bf

  def fromByteArray =
    UUID(Array[Byte](0, 0, 0, 1, 0, 5, 0, 9, 0, 0xb, 0, 0, 0, 0, 0, 0xf)) ==== u159bf

  def fromCharArray =
    UUID("00000001-0005-0009-000b-00000000000f".toCharArray) ==== u159bf

  // Accessors

  def longAccessors =
    u159bf.mostSigBits ==== 0x100050009L &&
    u159bf.leastSigBits ==== 0xb00000000000fL

  def toLongArray =
    u159bf.longArray ==== Array(0x100050009L, 0xb00000000000fL)

  def toIntArray =
    u159bf.intArray ==== Array(1, 0x50009, 0xb0000, 0xf)

  def toShortArray =
    u159bf.shortArray ==== Array[Short](0, 1, 5, 9, 0xb, 0, 0, 0xf)

  def toByteArray =
    u159bf.byteArray ==== Array[Byte](0, 0, 0, 1, 0, 5, 0, 9, 0, 0xb, 0, 0, 0, 0, 0, 0xf)

  def toCharArray =
    u159bf.charArray ==== "00000001-0005-0009-000b-00000000000f".toCharArray

  def toDefaultCaseString =
    u159bf.string ==== "00000001-0005-0009-000b-00000000000f"

  def toLowerCaseString =
    u159bf.toLowerCase ==== "00000001-0005-0009-000b-00000000000f"

  def toUpperCaseString =
    u159bf.toUpperCase ==== "00000001-0005-0009-000B-00000000000F"

  // -- Extractors --

  def uuidExtractor = {
    val UUID(uuidValue) = "00000001-0005-0009-000b-00000000000f" // will fail if not strict!
    uuidValue ==== u159bf
  }

  def uuidExtractorFailure = {
    "1-5-9-b-f" match {
      case UUID(_) => sys.error("Should not match - it wasn't strict!")
      case _ => true
    }
  }
}
