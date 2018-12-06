package io.jvm.uuid

import java.security.MessageDigest
import java.util.Locale

import org.scalacheck._
import org.specs2._

import scala.util._

class UUIDFeatureCheck
    extends Specification
    with ScalaCheck {

  def is = s2"""
  Long roundtrips
    msb, lsb longs                $msbLsbLongs

  String roundtrips
    strict                        $strictString
    non-strict (fail)             $nonStrictString
    strict with check             $strictStringWithCheck
    non-strict with check (fail)  $invalidStrictStringFailure
    unapply with invalid (fail)   $unapplyWithInvalidStringFailure
    non-strict with check         $nonStrictStringWithCheck
    apply with suffix (fail)      $applyWithSuffixFailure
    unapply with suffix (fail)    $unapplyWithSuffixFailure

  Array roundtrips
    long array                    $longArray
    long array wrong size         $longArrayWrongSize
    long array with offset        $longArrayWithOffset
    long array with offsets       $longArrayWithOffsets

    int array                     $intArray
    int array wrong size          $intArrayWrongSize
    int array with offset         $intArrayWithOffset
    int array with offsets        $intArrayWithOffsets

    short array                   $shortArray
    short array wrong size        $shortArrayWrongSize
    short array with offset       $shortArrayWithOffset
    short array with offsets      $shortArrayWithOffsets

    byte array                    $byteArray
    byte array wrong size         $byteArrayWrongSize
    byte array with offset        $byteArrayWithOffset
    byte array with offsets       $byteArrayWithOffsets

    char array                    $charArray
    char array (fail)             $charArrayFailure
    char array wrong size         $charArrayWrongSize
    char array with offset        $charArrayWithOffset
    char array with offsets       $charArrayWithOffsets

    randomString                  $randomString

  Version conformism
    naming is version 3           $namingIsVersion3
    random is version 4           $randomIsVersion4

  Comparison
    Signed via legacy    $signedComparison
    Unsigned by default  $unsignedComparison
"""

  // Long roundtrips

  def msbLsbLongs = prop { (msb: Long, lsb: Long) =>
    val uuid = UUID(msb, lsb)
    msb ==== uuid.getMostSignificantBits &&
    msb ==== uuid.mostSigBits &&
    lsb ==== uuid.getLeastSignificantBits &&
    lsb ==== uuid.leastSigBits
  }

  // String roundtrips

  private val hexValueGen = Gen.choose(0, 16)
  private val hexLowerGen = hexValueGen.map(v => "%x".format(v).head)
  private val hexUpperGen = hexLowerGen.map(_.toUpper)
  private val hexCharGen = Gen.oneOf(hexLowerGen, hexUpperGen)

  private def fixedHexWordGen(length: Int) = Gen.listOfN(length, hexCharGen).map(_.mkString)
  private def variableHexWordGen(minLength: Int, maxLength: Int) = fixedHexWordGen(maxLength).map(_ take minLength)

  private implicit class RichString(underlying: String) {
    def untrim(length: Int) = (("0" * length) + underlying) takeRight length
  }

  private val strictStringGen = for {
    w0 <- fixedHexWordGen(8)
    w1 <- fixedHexWordGen(4)
    w2 <- fixedHexWordGen(4)
    w3 <- fixedHexWordGen(4)
    w4 <- fixedHexWordGen(12)
  } yield s"$w0-$w1-$w2-$w3-$w4"

  private val nonStrictStringGen = for {
    w0 <- variableHexWordGen(1, 8)
    w1 <- variableHexWordGen(1, 4)
    w2 <- variableHexWordGen(1, 4)
    w3 <- variableHexWordGen(1, 4)
    w4 <- variableHexWordGen(1, 12)
  } yield s"$w0-$w1-$w2-$w3-$w4"

  def strictString = Prop.forAllNoShrink(strictStringGen) { ss =>
    UUID(ss       ).string ==== ss.toLowerCase(Locale.ROOT) &&
    UUID(ss, false).string ==== ss.toLowerCase(Locale.ROOT)
    UUID(ss       ).toLowerCase ==== ss.toLowerCase(Locale.ROOT) &&
    UUID(ss, false).toLowerCase ==== ss.toLowerCase(Locale.ROOT)
    UUID(ss       ).toUpperCase ==== ss.toUpperCase(Locale.ROOT) &&
    UUID(ss, false).toUpperCase ==== ss.toUpperCase(Locale.ROOT)
  }

  def nonStrictString = Prop.forAllNoShrink(nonStrictStringGen) { nss =>
    Try { UUID(nss      ) }.isFailure &&
    Try { UUID(nss, true) }.isFailure
  }

  def strictStringWithCheck = Prop.forAllNoShrink(strictStringGen) { ss =>
    UUID(ss, true).string ==== ss.toLowerCase(Locale.ROOT)
  }

  private val placeInStrictStringGen = Gen.choose(0, UUID.random.string.length - 1)
  private val nonHexUpperCharGen = Gen.choose('G', 'Z')
  private val nonHexLowerCharGen = nonHexUpperCharGen.map(_.toLower)
  private val nonHexCharGen = Gen.oneOf(nonHexUpperCharGen, nonHexLowerCharGen)

  private val invalidStrictStringGen = for {
    ss <- strictStringGen
    index <- placeInStrictStringGen
    lower <- nonHexCharGen
  } yield ss.updated(index, lower)

  def invalidStrictStringFailure = Prop.forAllNoShrink(invalidStrictStringGen) { iss =>
    UUID(iss, true) must throwA(new IllegalArgumentException("UUID must be in format xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx, where x is a hexadecimal digit (got: " + iss + ")"))
  }

  def unapplyWithInvalidStringFailure = Prop.forAllNoShrink(invalidStrictStringGen) { iss =>
    UUID.unapply(iss) ==== None
  }

  def nonStrictStringWithCheck = Prop.forAllNoShrink(nonStrictStringGen) { nss =>
    val Array(w0, w1, w2, w3, w4) = nss.split("-")
    val ss = s"${w0.untrim(8)}-${w1.untrim(4)}-${w2.untrim(4)}-${w3.untrim(4)}-${w4.untrim(12)}"
    val result = Try { UUID(nss, true).string }
    if (nss == ss) {
      result == Success(ss.toLowerCase(Locale.ROOT))
    } else {
      result.isFailure
    }
  }

  private val hexOrNoHexGen = Gen.oneOf(hexCharGen, nonHexCharGen)
  private val strictStringWithSuffixGen = for {
    ss <- strictStringGen
    ch <- hexOrNoHexGen
  } yield ss + ch

  def applyWithSuffixFailure = Prop.forAllNoShrink(strictStringWithSuffixGen) { sss =>
    UUID(sss) must throwA(new IllegalArgumentException("UUID must be in format xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx, where x is a hexadecimal digit (got: " + sss + ")"))
  }

  def unapplyWithSuffixFailure = Prop.forAllNoShrink(strictStringWithSuffixGen) { sss =>
    UUID.unapply(sss) ==== None
  }

  // Array roundtrips

  private val longGen = Arbitrary.arbLong.arbitrary
  private val longArrayGen = Gen.listOfN[Long](2, longGen).map(_.toArray)
  private val longArrayTooShortGen = Gen.listOfN[Long](1, longGen).map(_.toArray)
  private val longArrayTooLongGen = Gen.listOfN[Long](4, longGen).map(_.toArray)

  def longArray = Prop.forAllNoShrink(longArrayGen) { la =>
    UUID(la).longArray ==== la
  }

  def longArrayWrongSize = Prop.forAllNoShrink(longArrayTooShortGen, longArrayTooLongGen) { (tooShort, tooLong) =>
    Try { UUID(tooShort) }.isFailure &&
    Try { UUID(tooLong ) }.isFailure
  }

  def longArrayWithOffset = Prop.forAllNoShrink(longArrayGen, longArrayTooLongGen) { (src, dst) =>
    UUID(src).toLongArray(dst, 1)
    dst.tail.init ==== src
  }

  def longArrayWithOffsets = Prop.forAllNoShrink(longArrayTooLongGen, longArrayTooLongGen) { (src, dst) =>
    UUID.fromLongArray(src, 1).toLongArray(dst, 1)
    dst.tail.init ==== src.tail.init
  }

  private val intGen = Arbitrary.arbInt.arbitrary
  private val intArrayGen = Gen.listOfN[Int](4, intGen).map(_.toArray)
  private val intArrayTooShortGen = Gen.listOfN[Int](3, intGen).map(_.toArray)
  private val intArrayTooLongGen = Gen.listOfN[Int](6, intGen).map(_.toArray)

  def intArray = Prop.forAllNoShrink(intArrayGen) { ia =>
    UUID(ia).intArray ==== ia
  }

  def intArrayWrongSize = Prop.forAllNoShrink(intArrayTooShortGen, intArrayTooLongGen) { (tooShort, tooLong) =>
    Try { UUID(tooShort) }.isFailure &&
    Try { UUID(tooLong ) }.isFailure
  }

  def intArrayWithOffset = Prop.forAllNoShrink(intArrayGen, intArrayTooLongGen) { (src, dst) =>
    UUID(src).toIntArray(dst, 1)
    dst.tail.init ==== src
  }

  def intArrayWithOffsets = Prop.forAllNoShrink(intArrayTooLongGen, intArrayTooLongGen) { (src, dst) =>
    UUID.fromIntArray(src, 1).toIntArray(dst, 1)
    dst.tail.init ==== src.tail.init
  }

  private val shortGen = Arbitrary.arbShort.arbitrary
  private val shortArrayGen = Gen.listOfN[Short](8, shortGen).map(_.toArray)
  private val shortArrayTooShortGen = Gen.listOfN[Short](7, shortGen).map(_.toArray)
  private val shortArrayTooLongGen = Gen.listOfN[Short](10, shortGen).map(_.toArray)

  def shortArray = Prop.forAllNoShrink(shortArrayGen) { sa =>
    UUID(sa).shortArray ==== sa
  }

  def shortArrayWrongSize = Prop.forAllNoShrink(shortArrayTooShortGen, shortArrayTooLongGen) { (tooShort, tooLong) =>
    Try { UUID(tooShort) }.isFailure &&
    Try { UUID(tooLong ) }.isFailure
  }

  def shortArrayWithOffset = Prop.forAllNoShrink(shortArrayGen, shortArrayTooLongGen) { (src, dst) =>
    UUID(src).toShortArray(dst, 1)
    dst.tail.init ==== src
  }

  def shortArrayWithOffsets = Prop.forAllNoShrink(shortArrayTooLongGen, shortArrayTooLongGen) { (src, dst) =>
    UUID.fromShortArray(src, 1).toShortArray(dst, 1)
    dst.tail.init ==== src.tail.init
  }

  private val byteGen = Arbitrary.arbByte.arbitrary
  private val byteArrayGen = Gen.listOfN[Byte](16, byteGen).map(_.toArray)
  private val byteArrayTooShortGen = Gen.listOfN[Byte](15, byteGen).map(_.toArray)
  private val byteArrayTooLongGen = Gen.listOfN[Byte](18, byteGen).map(_.toArray)

  def byteArray = Prop.forAllNoShrink(byteArrayGen) { ba =>
    UUID(ba).byteArray ==== ba
  }

  def byteArrayWrongSize = Prop.forAllNoShrink(byteArrayTooShortGen, byteArrayTooLongGen) { (tooShort, tooLong) =>
    Try { UUID(tooShort) }.isFailure &&
    Try { UUID(tooLong ) }.isFailure
  }

  def byteArrayWithOffset = Prop.forAllNoShrink(byteArrayGen, byteArrayTooLongGen) { (src, dst) =>
    UUID(src).toByteArray(dst, 1)
    dst.tail.init ==== src
  }

  def byteArrayWithOffsets = Prop.forAllNoShrink(byteArrayTooLongGen, byteArrayTooLongGen) { (src, dst) =>
    UUID.fromByteArray(src, 1).toByteArray(dst, 1)
    dst.tail.init ==== src.tail.init
  }

  private val charArrayGen = strictStringGen.map(_.toCharArray)
  private val charArrayTooShortGen = charArrayGen.map(_.init)
  private val charArrayTooLongGen = charArrayGen.map(ca => '?' +: ca :+ '?')
  private val charArrayInvalidGen = invalidStrictStringGen.map(_.toCharArray)

  def charArray = Prop.forAllNoShrink(charArrayGen) { ca =>
    UUID(ca).charArray ==== ca.map(_.toLower)
  }

  def charArrayFailure = Prop.forAllNoShrink(charArrayInvalidGen) { ca =>
    Try { UUID(ca).charArray }.isFailure
  }

  def charArrayWrongSize = Prop.forAllNoShrink(charArrayTooShortGen, charArrayTooLongGen) { (tooShort, tooLong) =>
    Try { UUID(tooShort) }.isFailure &&
    Try { UUID(tooLong ) }.isFailure
  }

  def charArrayWithOffset = Prop.forAllNoShrink(charArrayGen, charArrayTooLongGen) { (src, dst) =>
    UUID(src).toCharArray(dst, 1)
    dst.tail.init ==== src.map(_.toLower)
  }

  def charArrayWithOffsets = Prop.forAllNoShrink(charArrayTooLongGen, charArrayTooLongGen) { (src, dst) =>
    UUID.fromCharArray(src, 1).toCharArray(dst, 1)
    dst.tail.init ==== src.tail.init.map(_.toLower)
  }

  def randomString = Prop.forAll(()) { _ =>
    val uuidString = UUID.randomString
    uuidString ==== uuidString.toLowerCase(Locale.ROOT) &&
    UUID(uuidString).string ==== uuidString
  }

  // Version conformism

  /** Naming conforms to version 3 spec:
    * xxxxxxxx-xxxx-3xxx-yxxx-xxxxxxxxxxxx where x is any hexadecimal digit and y is one of 8, 9, A, or B */
  def namingIsVersion3 = Prop.forAllNoShrink(byteArrayGen) { ba =>
    val uuid = UUID.nameUUIDFromBytes(ba)
    val version = (uuid.getMostSignificantBits >>> 12) & 0xF
    val variant = uuid.getLeastSignificantBits >>> 62
    version ==== 3 && variant ==== 2 && {
      val reconstruct = UUID(MessageDigest.getInstance("MD5").digest(ba))
      (reconstruct.mostSigBits & ~0xF000L) ==== (uuid.mostSigBits & ~0xF000L) &&
      (reconstruct.leastSigBits & ~0xC000000000000000L) ==== (uuid.leastSigBits & ~0xC000000000000000L) // cool :)
    }
  }

  /** Random conforms to version 4 spec:
    * xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx where x is any hexadecimal digit and y is one of 8, 9, A, or B */
  def randomIsVersion4 = Prop.forAll(()) { _ =>
    val uuid = UUID.random
    val version = (uuid.getMostSignificantBits >>> 12) & 0xF
    val variant = uuid.getLeastSignificantBits >>> 62
    version ==== 4 && variant ==== 2
  }

  // Comparisons

  def signedComparison = prop { (msb1: Long, lsb1: Long, msb2: Long, lsb2: Long) =>
    val uuid1 = UUID(msb1, lsb1)
    val uuid2 = UUID(msb2, lsb2)
    (uuid1 compareTo uuid2) ==== {
      val msb = java.lang.Long.compare(uuid1.mostSigBits, uuid2.mostSigBits)
      val lsb = java.lang.Long.compare(uuid1.leastSigBits, uuid2.leastSigBits)
      if (msb != 0) msb else lsb
    }
  }

  def unsignedComparison = prop { (msb1: Long, lsb1: Long, msb2: Long, lsb2: Long) =>
    val uuid1 = UUID(msb1, lsb1)
    val uuid2 = UUID(msb2, lsb2)
    (uuid1 compare uuid2) ==== {
      val msb = java.lang.Long.compareUnsigned(uuid1.mostSigBits, uuid2.mostSigBits)
      val lsb = java.lang.Long.compareUnsigned(uuid1.leastSigBits, uuid2.leastSigBits)
      if (msb != 0) msb else lsb
    }
  }
}
