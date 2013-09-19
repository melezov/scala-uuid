package com.example.test

import org.junit.runner.RunWith
import org.scalatest.FeatureSpec
import org.scalatest.GivenWhenThen
import org.scalatest.matchers.MustMatchers
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ImportFeatureSpec
    extends FeatureSpec with GivenWhenThen with MustMatchers {

  feature("scala-uuid functionality can be imported easily"){
    scenario("Direct Import") {
      given("that we want to import the scala-uuid functionality")

      when("io.jvm.uuid._ is imported")
      import io.jvm.uuid._

      then("UUID object and UUID type are available")
      assert(UUID.random match { case _: UUID => true })

      and("the static forwarder type must work")
      UUID.random.getClass must be (classOf[UUID])
    }

    scenario("Import by extending") {
      given("that we want to import the scala-uuid functionality")

      when("io.jvm.uuid.Imports._ is extended")
      object sandbox extends io.jvm.uuid.Imports {
        def check() = {
          then("UUID object and UUID type are available")
          assert(UUID.random match { case _: UUID => true })

          and("the static forwarder type must work")
          UUID.random.getClass must be (classOf[UUID])
        }
      }

      sandbox.check()
    }
  }
}
