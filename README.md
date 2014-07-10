scala-uuid
==========

A Scala wrapper for java.util.UUID - inspired by scala-time

Latest version (0.1.3) has been published against all reasonable versions of Scala:  
**2.8.x**: 2.8.1, 2.8.2  
**2.9.x**: 2.9.0, 2.9.0-1, 2.9.1, 2.9.1-1, 2.9.2, 2.9.3  
**2.10.x**: 2.10.4  
**2.11.x**: 2.11.1

In order to add the library dependency to your project, add the Element resolver:

    resolvers += "Element Releases" at "http://repo.element.hr/nexus/content/repositories/releases/"

And then

    libraryDependencies += "io.jvm" %% "scala-uuid" % "0.1.3"

In order to use:

    scala> import io.jvm.uuid._
    import io.jvm.uuid._

You will now have an UUID type and object available:

    scala> classOf[UUID]
    res0: Class[io.jvm.uuid.UUID] = class java.util.UUID

Constructors:

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

Accessors:

    scala> val foo = UUID.random
    foo: io.jvm.uuid.UUID = bf96f2e0-37c3-4aed-b223-76771aa81a6f

    scala> foo.string
    res6: String = bf96f2e0-37c3-4aed-b223-76771aa81a6f

    scala> foo.mostSigBits
    res7: Long = -4641255321136575763

    scala> foo.leastSigBits formatted "%016x"
    res8: String = b22376771aa81a6f

    scala> val (a, b) = foo.longs
    a: Long = -4641255321136575763
    b: Long = -5610510456853095825

    scala> foo.byteArray
    res9: Array[Byte] = Array(-65, -106, -14, -32, 55, -61, 74, -19, -78, 35, 118, 119, 26, -88, 26, 111)

Extractors:

    scala> "9-9-9-9-9" match { case UUID(x) => x }
    res10: io.jvm.uuid.UUID = 00000009-0009-0009-0009-000000000009

Symmetry:

    scala> val UUID(zeros) = UUID("0-0-0-0-0")
    zeros: String = 00000000-0000-0000-0000-000000000000

For more information, check out the feature spec.  
Contributions are more than welcome!
