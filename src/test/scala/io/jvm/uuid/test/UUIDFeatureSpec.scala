package io.jvm.uuid
package test

import org.junit.runner.RunWith
import org.scalatest.FeatureSpec
import org.scalatest.GivenWhenThen
import org.scalatest.MustMatchers
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class UUIDFeatureSpec
    extends FeatureSpec with GivenWhenThen with MustMatchers {

  feature("Converters (to UUID)"){
    val zeros = java.util.UUID.fromString("00000000-0000-0000-0000-000000000000")

    scenario("From all-zero String") {
      UUID("0-0-0-0-0") must be (zeros)
    }

    scenario("From all-zero msb and lsb Longs") {
      UUID(0, 0) must be (zeros)
    }

    scenario("From all-zero Array[Byte]") {
      UUID(new Array[Byte](16)) must be (zeros)
    }
  }

  feature("Converters (from UUID)"){
    scenario("To 12345 String") {
      UUID("1-2-3-4-5").toString must be ("00000001-0002-0003-0004-000000000005")
    }

    scenario("To 123 msb and 45 lsb Longs") {
      UUID("1-2-3-4-5").mostSigBits must be (0x0000000100020003L)
      UUID("1-2-3-4-5").leastSigBits must be (0x0004000000000005L)
    }

    scenario("To 12345 Array[Byte]") {
      UUID("1-2-3-4-5").byteArray must be (Array[Byte](0,0,0,1,0,2,0,3,0,4,0,0,0,0,0,5))
    }
  }

  feature("Converters (random round trips)"){
    scenario("Round trip to and from a String") {
      val randomUUID = UUID.random
      UUID(randomUUID.toString) must be (randomUUID)
    }

    scenario("Round trip to and from Array[Byte]") {
      val randomUUID = UUID.random
      UUID(randomUUID.byteArray) must be (randomUUID)
    }

    scenario("Round trip to and from msb and lsb Longs") {
      val randomUUID = UUID.random
      UUID(randomUUID.mostSigBits, randomUUID.leastSigBits) must be (randomUUID)
    }
  }

  feature("Extractors") {
    scenario("String extractor") {
      val UUID(strVal) = UUID("1-2-3-4-5")
      strVal must be ("00000001-0002-0003-0004-000000000005")
    }

    scenario("UUID extractor") {
      val UUID(uuidVal) = "1-2-3-4-5"
      uuidVal must be (UUID("1-2-3-4-5"))
    }

    scenario("UUID extractor [error]") {
      intercept[scala.MatchError] {
        val UUID(uuidVal) = "1-2-3-4"
      }
    }
  }
}
