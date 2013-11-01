package io.jvm.uuid
package test

import org.junit.runner.RunWith
import org.scalatest.FeatureSpec
import org.scalatest.GivenWhenThen
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.junit.JUnitRunner
import scala.annotation.tailrec

@RunWith(classOf[JUnitRunner])
class UUIDFeatureSpec
    extends FeatureSpec with GivenWhenThen with ShouldMatchers {

  val s00000 = "00000000-0000-0000-0000-000000000000"
  val s12345 = "00000001-0002-0003-0004-000000000005"

  val u00000 = java.util.UUID.fromString(s00000)
  val u12345 = java.util.UUID.fromString(s12345)

  feature("Converters (to UUID)"){
    scenario("From all-zero String") {
      UUID("0-0-0-0-0") should be (u00000)
    }

    scenario("From all-zero msb and lsb Longs") {
      UUID(0, 0) should be (u00000)
    }

    scenario("From all-zero Longs tuple") {
      UUID((0L, 0L)) should be (u00000)
    }

    scenario("From all-zero Array[Byte]") {
      UUID(new Array[Byte](16)) should be (u00000)
    }
  }

  feature("Converters (from UUID)"){
    scenario("To 12345 String") {
      UUID("1-2-3-4-5").string should be (s12345)
    }

//    scenario("To 123 msb and 45 lsb Longs") {
//      UUID("1-2-3-4-5").mostSigBits should be (0x0000000100020003L)
//      UUID("1-2-3-4-5").leastSigBits should be (0x0004000000000005L)
//    }

    scenario("To (123, 45) Longs tuple") {
      UUID("1-2-3-4-5").longs should be ((0x0000000100020003L, 0x0004000000000005L))
    }

    scenario("To 12345 Array[Byte]") {
      UUID("1-2-3-4-5").byteArray should be (Array[Byte](0,0,0,1,0,2,0,3,0,4,0,0,0,0,0,5))
    }
  }

  feature("Converters (random round trips)"){
    scenario("Round trip to and from a String") {
      val randomUUID = UUID.random
      UUID(randomUUID.string) should be (randomUUID)
    }

    scenario("Round trip to and from Array[Byte]") {
      val randomUUID = UUID.random
      UUID(randomUUID.byteArray) should be (randomUUID)
    }

//    scenario("Round trip to and from msb and lsb Longs") {
//      val randomUUID = UUID.random
//      UUID(randomUUID.mostSigBits, randomUUID.leastSigBits) should be (randomUUID)
//    }

    scenario("Round trip to and from Longs tuple") {
      val randomUUID = UUID.random
      UUID(randomUUID.longs) should be (randomUUID)
    }
  }

  feature("Extractors") {
    scenario("String extractor") {
      val UUID(strVal) = UUID("1-2-3-4-5")
      strVal should be (s12345)
    }

    scenario("String extractor [alternative]") {
      (UUID("1-2-3-4-5") match {
        case UUID(a) => a
      }) should be (s12345)
    }

    scenario("UUID extractor") {
      val UUID(uuidVal) = "1-2-3-4-5"
      uuidVal should be (UUID("1-2-3-4-5"))
    }

    scenario("UUID extractor [error]") {
      intercept[scala.MatchError] {
        val UUID(uuidVal) = "1-2-3-4"
        uuidVal should not be (UUID("1-2-3-4"))
      }
    }
  }

  feature("Speed test") {
    scenario("Round trip to and from Array[Byte]") {
      given("3 million binary serializations/deserializations")
      val tries = 3 * 1000 * 1000

      when("running on a single 3 Ghz core")
      then("the checksum should take aprox. a second to finish")

      import System.{ currentTimeMillis => now }
      val old = now

      val randomUUIDBytes = UUID.random.byteArray
      UUID(randomUUIDBytes).byteArray should be (randomUUIDBytes)
      val checkResult = randomUUIDBytes.map(_ & 0xffL).sum * (tries >>> 4)

      @tailrec
      def testBinarySpeed(count: Int, checkSum: Long): Long = {
        if (count > 0) {
          val ba = UUID(randomUUIDBytes).byteArray
          val ck = ba(count & 0xf) & 0xffL
          testBinarySpeed(count - 1, checkSum + ck)
        }
        else {
          checkSum
        }
      }

      val result = testBinarySpeed(tries, 0L)
      info("Took: " + (now - old) + " ms")

      result should equal(checkResult)
    }
  }
}
