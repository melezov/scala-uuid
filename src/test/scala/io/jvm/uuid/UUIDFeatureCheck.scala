package io.jvm.uuid

import java.util.Locale
import org.specs2._
import org.scalacheck._
import scala.util._

class UUIDFeatureCheck
    extends Specification
    with ScalaCheck {

  def is = s2"""
  Long roundtrips
    msb, lsb longs                $msbLsbLongs
    longs tuple                   $longsTuple

  String roundtrips
    legacy strict string          $legacyStrictString
    legacy non-strict string      $legacyNonStrictString
    strict string with check      $strictStringWithCheck
    non-strict string with check  $nonStrictStringWithCheck

  Byte array roundtrips
    byte array                    $byteArray
"""

  /* Long roundtrips */

  def msbLsbLongs = prop { (msb: Long, lsb: Long) =>
    val uuid = UUID(msb, lsb)
    msb === uuid.getMostSignificantBits &&
    msb === uuid.mostSigBits &&
    lsb === uuid.getLeastSignificantBits &&
    lsb === uuid.leastSigBits
  }

  def longsTuple = prop { longs: (Long, Long) =>
    val uuid = UUID(longs)
    uuid.longs === longs
  }

  /* String roundtrips */

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

  def legacyStrictString = Prop.forAllNoShrink(strictStringGen) { ss =>
    UUID(ss).toString === ss.toLowerCase(Locale.ENGLISH)
  }

  def legacyNonStrictString = Prop.forAllNoShrink(nonStrictStringGen) { nss =>
    val w0 :: w1 :: w2 :: w3 :: w4 :: Nil = nss.split("-").toList
    val ss = s"${w0.untrim(8)}-${w1.untrim(4)}-${w2.untrim(4)}-${w3.untrim(4)}-${w4.untrim(12)}"
    UUID(nss).toString === ss.toLowerCase(Locale.ENGLISH)
  }

  def strictStringWithCheck = Prop.forAllNoShrink(strictStringGen) { ss =>
    UUID(ss, true).string === ss.toLowerCase(Locale.ENGLISH)
  }

  def nonStrictStringWithCheck = Prop.forAllNoShrink(nonStrictStringGen) { nss =>
    val w0 :: w1 :: w2 :: w3 :: w4 :: Nil = nss.split("-").toList
    val ss = s"${w0.untrim(8)}-${w1.untrim(4)}-${w2.untrim(4)}-${w3.untrim(4)}-${w4.untrim(12)}"
    val result = Try { UUID(nss, true).string }
    if (nss == ss) {
      result == Success(ss.toLowerCase(Locale.ENGLISH))
    } else {
      result.isFailure
    }
  }

  /* Byte array roundtrips */

  private val byteGen = Gen.choose(0, 255).map(_.toByte)
  private val byteArrayGen = Gen.listOfN[Byte](16, byteGen).map(_.toArray)

  def byteArray = Prop.forAllNoShrink(byteArrayGen) { ba =>
    UUID(ba).byteArray === ba
  }
}
