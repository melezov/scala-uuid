package com.example.test

import org.junit.runner.RunWith
import org.scalatest.FeatureSpec
import org.scalatest.GivenWhenThen
import org.scalatest.ShouldMatchers
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ImportFeatureSpec
    extends FeatureSpec with GivenWhenThen with ShouldMatchers {

  feature("scala-uuid functionality can be imported easily"){
    scenario("Direct Import") {
      Given("that we want to import the scala-uuid functionality")

      When("io.jvm.uuid._ is imported")
      import io.jvm.uuid._

      Then("UUID object and UUID type are available")
      assert(UUID.random match { case _: UUID => true })

      And("the static forwarder type should work")
      UUID.random.getClass should be (classOf[UUID])
    }

    scenario("Import by extending") {
      Given("that we want to import the scala-uuid functionality")

      When("io.jvm.uuid.Imports._ is extended")
      object sandbox extends io.jvm.uuid.Imports {
        def check() = {
          Then("UUID object and UUID type are available")
          assert(UUID.random match { case _: UUID => true })

          And("the static forwarder type should work")
          UUID.random.getClass should be (classOf[UUID])
        }
      }

      sandbox.check()
    }
  }
}
