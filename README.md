# scala-uuid
[![Build Status](https://travis-ci.org/melezov/scala-uuid.svg?branch=master)](https://travis-ci.org/melezov/scala-uuid)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.jvm.uuid/scala-uuid_2.11/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.jvm.uuid/scala-uuid_2.11)
[![Scaladoc](https://javadoc-badge.appspot.com/io.jvm.uuid/scala-uuid_2.11.svg?label=scaladoc)](http://javadoc-badge.appspot.com/io.jvm.uuid/scala-uuid_2.11)
[![License](https://img.shields.io/badge/license-BSD%203--Clause-brightgreen.svg)](https://opensource.org/licenses/BSD-3-Clause)
[![Codecov](https://img.shields.io/codecov/c/github/melezov/scala-uuid/master.svg)](http://codecov.io/github/melezov/scala-uuid?branch=master)
[![Codacy](https://api.codacy.com/project/badge/786c3c5e6fe24eed85733fd1848eef7e)](https://www.codacy.com/app/melezov/scala-uuid)

An optimized Scala wrapper for `java.util.UUID` - inspired by [scala-time](https://github.com/jorgeortiz85/scala-time/ "A Scala wrapper for Joda Time").

#### This is the **development** branch (0.2.2-SNAPSHOT).

Latest version (0.2.1) has been published against all reasonable versions of Scala:  
**2.8.x**: 2.8.1, 2.8.2  
**2.9.x**: 2.9.0, 2.9.0-1, 2.9.1, 2.9.1-1, 2.9.2, 2.9.3  
**2.10.x**: 2.10.6  
**2.11.x**: 2.11.7

#### Installation:

**scala-uuid** is being published to OSSRH / Maven Central and should be available without adding additional repositories.  
To add the library dependency to your project, simply add:

```scala
    libraryDependencies += "io.jvm.uuid" %% "scala-uuid" % "0.2.1"
```

#### In order to use:

    scala> import io.jvm.uuid._
    import io.jvm.uuid._

You will now have the `UUID` type and object available:

    scala> classOf[UUID]
    res0: Class[io.jvm.uuid.UUID] = class java.util.UUID

*Alternatively*, you can extend `io.jvm.uuid.Imports` and bring the implicits into scope that way.  
This is fairly useful for [importing via (package) objects](src/test/scala/com/example/ImportFeatureSpec.scala#L32 "Open ImportFeatureSpec source"):

    scala> object MyClass extends io.jvm.uuid.Imports
    defined object MyClass

    scala> import MyClass._
    import MyClass._

    scala> classOf[UUID]
    res0: Class[MyClass.UUID] = class java.util.UUID

#### Constructors:

    scala> UUID.random
    res1: io.jvm.uuid.UUID = 5f4bdbef-0417-47a6-ac9e-ffc5a4905f7f

    scala> UUID(1, 2)
    res2: io.jvm.uuid.UUID = 00000000-0000-0001-0000-000000000002

    scala> UUID("00112233-4455-6677-8899-aAbBcCdDeEfF")
    res3: io.jvm.uuid.UUID = 00112233-4455-6677-8899-aabbccddeeff

#### Array factory methods:

    scala>  UUID(Array[Long](1L, 2L))
    res4: io.jvm.uuid.UUID = 00000000-0000-0001-0000-000000000002

    scala>  UUID(Array[Int](1, 2, 3, 4))
    res5: io.jvm.uuid.UUID = 00000001-0000-0002-0000-000300000004

    scala> UUID(Array[Short](1, 2, 3, 4, 5, 6, 7, 8))
    res6: io.jvm.uuid.UUID = 00010002-0003-0004-0005-000600070008

    scala>  UUID(Array[Byte](1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16))
    res7: io.jvm.uuid.UUID = 01020304-0506-0708-090a-0b0c0d0e0f10

    scala> UUID("5ca1ab1e-Feed-Dead-Beef-CafeBabeC0de".toCharArray)
    res8: io.jvm.uuid.UUID = 5ca1ab1e-feed-dead-beef-cafebabec0de

Bare in mind that the `String` constructor requires an **exact**, 36 character String representation:

    scala> UUID("01020304-0506-0708-090a-0b0c0d0e0f10")
    res9: io.jvm.uuid.UUID = 01020304-0506-0708-090a-0b0c0d0e0f10

    scala> UUID("01020304-0506-0708-090a-0b0c0d0e0f1") // missing hexadecimal digit
    java.lang.IllegalArgumentException: UUID must be in format xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx,
        where x is a hexadecimal digit (got: 01020304-0506-0708-090a-0b0c0d0e0f1)
      at io.jvm.uuid.StaticUUID.apply(StaticUUID.scala:218)
      ... 43 elided

There is a legacy factory method available through providing `false` as the second argument,
but it has a lot of interesting *features*:

    scala> UUID("1-2-3-4-00000000000000004000000000000005", false)
    res10: io.jvm.uuid.UUID = 00000001-0002-0003-4004-000000000005

#### Accessors:

    scala> val foo = UUID.random
    foo: io.jvm.uuid.UUID = 17fa3a17-a302-4fd7-81f8-54882cdd7d78

    scala> foo.string
    res11: String = 17fa3a17-a302-4fd7-81f8-54882cdd7d78

    scala> foo.mostSigBits
    res12: Long = 1727757280243503063

    scala> foo.leastSigBits formatted "%016x"
    res13: String = 81f854882cdd7d78

    scala> foo.longArray
    res14: Array[Long] = Array(1727757280243503063, -9081415704747606664)

    scala> foo.intArray
    res15: Array[Int] = Array(402274839, -1560129577, -2114431864, 752713080)

    scala> foo.shortArray
    res16: Array[Short] = Array(6138, 14871, -23806, 20439, -32264, 21640, 11485, 32120)

    scala> foo.byteArray
    res17: Array[Byte] = Array(23, -6, 58, 23, -93, 2, 79, -41, -127, -8, 84, -120, 44, -35, 125, 120)

    scala> foo.charArray
    res18: Array[Char] = Array(1, 7, f, a, 3, a, 1, 7, -, ..., -, 5, 4, 8, 8, 2, c, d, d, 7, d, 7, 8)

String accessors are much more optimized than vanilla `toString` ([**3x** speedup](src/main/scala/io/jvm/uuid/RichUUID.scala#L125 "Open RichUUID.scala source")), and come in two flavors:

    scala> foo.string // lower-case by default
    res19: String = 17fa3a17-a302-4fd7-81f8-54882cdd7d78

    scala> foo.toLowerCase // explicitly lower-case
    res20: String = 17fa3a17-a302-4fd7-81f8-54882cdd7d78

    scala> foo.toUpperCase // explicitly upper case
    res21: String = 17FA3A17-A302-4FD7-81F8-54882CDD7D78

#### Extractors:

An optimized `String` extractor is also available for your needs (e.g. when extracting URL parameters):

    scala> val SubmitEventRoute = "/event/([^/]+)/submit".r
    SubmitEventRoute: scala.util.matching.Regex = /event/([^/]+)/submit

    scala> val SubmitEventRoute(UUID(eventId)) = "/event/EF72505A-A9A6-4CD7-A14C-8F27C96FD727/submit"
    eventId: io.jvm.uuid.UUID = ef72505a-a9a6-4cd7-a14c-8f27c96fd727

For more information, check out the [feature spec](src/test/scala/io/jvm/uuid/UUIDFeatureSpec.scala "Open UUIDFeatureSpec source").  
Contributions are more than welcome!
