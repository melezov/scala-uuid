# scala-uuid
[![Build Status](https://travis-ci.org/melezov/scala-uuid.svg?branch=2.11.x)](https://travis-ci.org/melezov/scala-uuid)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.jvm.uuid/scala-uuid_2.11/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.jvm.uuid/scala-uuid_2.11)
[![Scaladoc](https://javadoc-badge.appspot.com/io.jvm.uuid/scala-uuid_2.11.svg?label=scaladoc)](http://javadoc-badge.appspot.com/io.jvm.uuid/scala-uuid_2.11)
[![License](https://img.shields.io/badge/license-BSD%203--Clause-brightgreen.svg)](https://opensource.org/licenses/BSD-3-Clause)
[![Codecov](https://img.shields.io/codecov/c/github/melezov/scala-uuid/2.11.x.svg)](http://codecov.io/github/melezov/scala-uuid?branch=2.11.x)
[![Codacy](https://api.codacy.com/project/badge/786c3c5e6fe24eed85733fd1848eef7e)](https://www.codacy.com/app/melezov/scala-uuid)

A Scala wrapper for `java.util.UUID` - inspired by [scala-time](https://github.com/jorgeortiz85/scala-time/ "A Scala wrapper for Joda Time").

Latest version (0.1.7) has been published against all reasonable versions of Scala:  
**2.8.x**: 2.8.1, 2.8.2  
**2.9.x**: 2.9.0, 2.9.0-1, 2.9.1, 2.9.1-1, 2.9.2, 2.9.3  
**2.10.x**: 2.10.6  
**2.11.x**: 2.11.7

#### Installation:

**scala-uuid** is being published to OSSRH / Maven Central and should be available without adding additional repositories.  
To add the library dependency to your project, simply add:

```scala
    libraryDependencies += "io.jvm.uuid" %% "scala-uuid" % "0.1.7"
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

    scala> UUID("1-2-3-4-5")
    res2: io.jvm.uuid.UUID = 00000001-0002-0003-0004-000000000005

    scala> UUID(1, 2)
    res3: io.jvm.uuid.UUID = 00000000-0000-0001-0000-000000000002

    scala> UUID((3L, 4L))
    res4: io.jvm.uuid.UUID = 00000000-0000-0003-0000-000000000004

    scala> UUID(Array[Byte](1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16))
    res5: io.jvm.uuid.UUID = 01020304-0506-0708-090a-0b0c0d0e0f10

There is also a [**strict** constructor](src/main/scala/io/jvm/uuid/StaticUUID.scala#L18 "Open StaticUUID source"), which must accept an **exact**, 36 character String representation:

    scala> UUID("11111111-2222-3333-4444-555555555555", true)
    res6: io.jvm.uuid.UUID = 11111111-2222-3333-4444-555555555555

    scala> UUID("11111111-2222-3333-4444-55555555555", true) // one hexadecimal digit missing
    java.lang.IllegalArgumentException: requirement failed: UUID must be in format xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
      at io.jvm.uuid.StaticUUID$.apply(StaticUUID.scala:19)
      ... 43 elided

    scala> UUID("11111111-2222-3333-4444-55555555555", false) // false enables legacy behavior
    res7: io.jvm.uuid.UUID = 11111111-2222-3333-4444-055555555555

#### Accessors:

    scala> val foo = UUID.random
    foo: io.jvm.uuid.UUID = bf96f2e0-37c3-4aed-b223-76771aa81a6f

    scala> foo.string
    res8: String = bf96f2e0-37c3-4aed-b223-76771aa81a6f

    scala> foo.mostSigBits
    res9: Long = -4641255321136575763

    scala> foo.leastSigBits formatted "%016x"
    res10: String = b22376771aa81a6f

    scala> val (a, b) = foo.longs
    a: Long = -4641255321136575763
    b: Long = -5610510456853095825

    scala> foo.byteArray
    res11: Array[Byte] = Array(-65, -106, -14, -32, 55, -61, 74, -19, -78, 35, 118, 119, 26, -88, 26, 111)

String accessors are much more optimized than vanilla `toString` ([**3x** speedup](src/main/scala/io/jvm/uuid/RichUUID.scala#L13 "Open RichUUID.scala source")), and come in two flavors:

    scala> foo.string // lower-case by default
    res12: String = bf96f2e0-37c3-4aed-b223-76771aa81a6f

    scala> foo.toLowerCase // explicitly lower-case
    res13: String = bf96f2e0-37c3-4aed-b223-76771aa81a6f

    scala> foo.toUpperCase // explicitly upper case
    res14: String = BF96F2E0-37C3-4AED-B223-76771AA81A6F

#### Extractors:
Take care when using the unapply excractor to notice that it operates in **strict** mode, to allow for the use case of extracting URL parameters:

    scala> val UUID(uuid) = "00010002-0003-0004-0005-000600070008"
    uuid: io.jvm.uuid.UUID = 00010002-0003-0004-0005-000600070008

    scala> val UUID(uuid) = "00010002-0003-0004-0005-00060007008" // missing hexadecimal digit
      scala.MatchError: 00010002-0003-0004-0005-00060007008 (of class java.lang.String)
      ... 43 elided

    scala> val UUID(zeroes) = UUID("0-0-0-0-0")
    zeroes: String = 00000000-0000-0000-0000-000000000000

For more information, check out the [feature spec](src/test/scala/io/jvm/uuid/UUIDFeatureSpec.scala "Open UUIDFeatureSpec source").  
Contributions are more than welcome!
