package io.jvm.uuid

/** Singleton object to be bound with the `UUID` value in the package object. */
object StaticUUID extends StaticUUID

/** This class holds all static forwarders and `UUID` factories.
  *
  * Extend this class from the client code in order to add new functionality to the library. */
class StaticUUID {
  /** Generates a random `UUID` (type 4) using a cryptographically strong pseudo random number generator. */
  @inline final def random: UUID =
    java.util.UUID.randomUUID()

  /** Creates a new `UUID` by concatenating two 64-bit values.
    * @param  mostSigBits Most significant bits of the new `UUID`
    * @param  leastSigBits Least significant bits of the new `UUID` */
  @inline final def apply(mostSigBits: Long, leastSigBits: Long): UUID =
    new UUID(mostSigBits, leastSigBits)

  /** Helper length checker which will be inlined into `UUID` factories. */
  @inline private[this] def lengthCheck(tpe: String, expected: Int, actual: Int): Unit =
    if (expected != actual) throwInvalidLength(tpe, expected, actual)

  /** Throws an exception because there was a length mismatch.
    * If the actual length was greater than what was expected, suggest using the alternative constructor. */
  private[this] final def throwInvalidLength(tpe: String, expected: Int, actual: Int): Nothing =
    throw new IllegalArgumentException(
      (if (tpe == "int") "Expecting an " else "Expecting a ") + tpe + " array of length " + expected + ", but length was " + actual + (
        if (actual < expected) " (too short)" else "; if you wish to skip this check use UUID.from" + tpe + "Array instead!"
      )
    )

  /** Creates a new `UUID` by concatenating two 64-bit values.
    * @throws  IllegalArgumentException In case `buffer.length` != 2 */
  final def apply(buffer: Array[Long]): UUID = {
    lengthCheck("Long", 2, buffer.length)
    fromLongArray(buffer, 0)
  }

  /** Creates a new `UUID` by concatenating two 64-bit values.
    * @param  offset Offset of the most significant `Long` inside the array. */
  @inline final def fromLongArray(buffer: Array[Long], offset: Int): UUID =
    UUID(
      buffer(offset    )
    , buffer(offset + 1)
    )

  /** Creates a new `UUID` by concatenating four 32-bit values.
    * @throws  IllegalArgumentException In case `buffer.length != 4` */
  final def apply(buffer: Array[Int]): UUID = {
    lengthCheck("Int", 4, buffer.length)
    fromIntArray(buffer, 0)
  }

  /** Creates a new `UUID` by concatenating four 32-bit values.
    * @param  offset Offset of the most significant `Int` inside the array. */
  @inline final def fromIntArray(buffer: Array[Int], offset: Int): UUID = {
    UUID(
      (buffer(offset    ).toLong) << 32
    | (buffer(offset + 1) & 0xffffffffL)
    , (buffer(offset + 2).toLong) << 32
    | (buffer(offset + 3) & 0xffffffffL)
    )
  }

  /** Creates a new `UUID` by concatenating eight 16-bit values.
    * @throws  IllegalArgumentException In case `buffer.length != 8` */
  final def apply(buffer: Array[Short]): UUID = {
    lengthCheck("Short", 8, buffer.length)
    fromShortArray(buffer, 0)
  }

  /** Creates a new `UUID` by concatenating eight 16-bit values.
    * @param  offset Offset of the most significant `Short` inside the array. */
  @inline final def fromShortArray(buffer: Array[Short], offset: Int): UUID =
    UUID(
      (buffer(offset    )          ).toLong << 48
    | (buffer(offset + 1) & 0xffffL)        << 32
    | (buffer(offset + 2) & 0xffffL)        << 16
    | (buffer(offset + 3) & 0xffffL)
    , (buffer(offset + 4)          ).toLong << 48
    | (buffer(offset + 5) & 0xffffL)        << 32
    | (buffer(offset + 6) & 0xffffL)        << 16
    | (buffer(offset + 7) & 0xffffL)
    )

  /** Creates a new `UUID` by concatenating 16 bytes.
    * @throws  IllegalArgumentException In case `buffer.length != 16` */
  final def apply(buffer: Array[Byte]): UUID = {
    lengthCheck("Byte", 16, buffer.length)
    fromByteArray(buffer, 0)
  }

  /** Creates a new `UUID` by concatenating 16 bytes.
    * @param  offset Offset of the most significant `Byte` inside the array. */
  @inline final def fromByteArray(buffer: Array[Byte], offset: Int): UUID =
    UUID(
      (buffer(offset     )       ).toLong << 56
    | (buffer(offset +  1) & 0xff).toLong << 48
    | (buffer(offset +  2) & 0xff).toLong << 40
    | (buffer(offset +  3) & 0xff).toLong << 32
    | (buffer(offset +  4) & 0xff).toLong << 24
    | (buffer(offset +  5) & 0xff)        << 16
    | (buffer(offset +  6) & 0xff)        <<  8
    | (buffer(offset +  7) & 0xff)
    , (buffer(offset +  8)       ).toLong << 56
    | (buffer(offset +  9) & 0xff).toLong << 48
    | (buffer(offset + 10) & 0xff).toLong << 40
    | (buffer(offset + 11) & 0xff).toLong << 32
    | (buffer(offset + 12) & 0xff).toLong << 24
    | (buffer(offset + 13) & 0xff)        << 16
    | (buffer(offset + 14) & 0xff)        <<  8
    | (buffer(offset + 15) & 0xff)
    )

  /** Hexadecimal character to integer value mapping used in parser.
    * Invalid characters are marked with a value of `-1`. */
  private[this] final val Lookup: Array[Int] = {
    val buffer = new Array[Int]('f' + 1)
    var i = buffer.length
    while (i > 0) {
      buffer{ i -= 1; i } = i - (
        if (i >= '0' && i <= '9') '0'
        else if (i >= 'A' && i <= 'F') 'A' - 10
        else if (i >= 'a' /* && i <= 'f' */) 'a' - 10
        else i + 1
      )
    }
    buffer
  }

  /** Creates a new `UUID` by parsing 36 chars.
    * @throws  IllegalArgumentException In case `buffer.length != 36` */
  final def apply(buffer: Array[Char]): UUID = {
    lengthCheck("Char", 36, buffer.length)
    fromCharArray(buffer, 0)
  }

  /** Creates a new `UUID` by parsing 36 chars in `xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx` format.
    * @param  offset Position of the first `Char` in the array. */
  @inline final def fromCharArray(buffer: Array[Char], offset: Int): UUID = {
    val res = try {
      fromCharArrayViaLookup(buffer, offset, Lookup)
    } catch {
      case _: ArrayIndexOutOfBoundsException => null
    }
    if (res eq null) throw new IllegalArgumentException(
      "UUID must be in format xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx, where x is a hexadecimal digit (got: " +
        new String(buffer, offset, math.min(buffer.length - offset, 36))+ ")")
    res
  }

  /** Parses an `UUID` in `xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx` format from an array of characters, helper utility.
    * @throws ArrayIndexOutOfBoundsException in case of character whose value is > max of hex lookup
    *         Scalac is incompetent when inlining error handling, so this needs to be handled by the callee */
  @inline private[this] final def fromCharArrayViaLookup(buffer: Array[Char], offset: Int, lookup: Array[Int]): UUID = {
    val msb3 = (
      (lookup(buffer(offset +  0).toInt) << 12)
    | (lookup(buffer(offset +  1).toInt) <<  8)
    | (lookup(buffer(offset +  2).toInt) <<  4)
    | (lookup(buffer(offset +  3).toInt)      )
    )

    val msb2 = (
      (lookup(buffer(offset +  4).toInt) << 12)
    | (lookup(buffer(offset +  5).toInt) <<  8)
    | (lookup(buffer(offset +  6).toInt) <<  4)
    | (lookup(buffer(offset +  7).toInt)      )
    )

    val msb1 = (
      (lookup(buffer(offset +  9).toInt) << 12)
    | (lookup(buffer(offset + 10).toInt) <<  8)
    | (lookup(buffer(offset + 11).toInt) <<  4)
    | (lookup(buffer(offset + 12).toInt)      )
    )

    val msb0 = (
      (lookup(buffer(offset + 14).toInt) << 12)
    | (lookup(buffer(offset + 15).toInt) <<  8)
    | (lookup(buffer(offset + 16).toInt) <<  4)
    | (lookup(buffer(offset + 17).toInt)      )
    )

    val lsb3 = (
      (lookup(buffer(offset + 19).toInt) << 12)
    | (lookup(buffer(offset + 20).toInt) <<  8)
    | (lookup(buffer(offset + 21).toInt) <<  4)
    | (lookup(buffer(offset + 22).toInt)      )
    )

    val lsb2 = (
      (lookup(buffer(offset + 24).toInt) << 12)
    | (lookup(buffer(offset + 25).toInt) <<  8)
    | (lookup(buffer(offset + 26).toInt) <<  4)
    | (lookup(buffer(offset + 27).toInt)      )
    )

    val lsb1 = (
      (lookup(buffer(offset + 28).toInt) << 12)
    | (lookup(buffer(offset + 29).toInt) <<  8)
    | (lookup(buffer(offset + 30).toInt) <<  4)
    | (lookup(buffer(offset + 31).toInt)      )
    )

    val lsb0 = (
      (lookup(buffer(offset + 32).toInt) << 12)
    | (lookup(buffer(offset + 33).toInt) <<  8)
    | (lookup(buffer(offset + 34).toInt) <<  4)
    | (lookup(buffer(offset + 35).toInt)      )
    )

    // check for separators and if any of the characters were not a hexadecimal value
    if (buffer(offset +  8) != '-' || buffer(offset + 13) != '-' ||
        buffer(offset + 18) != '-' || buffer(offset + 23) != '-' ||
        (msb3 | msb2 | msb1 | msb0 | lsb3 | lsb2 | lsb1 | lsb0) < 0) null else {
      UUID(
        (((msb3 << 16) | msb2).toLong << 32) | (msb1.toLong << 16) | msb0
      , (((lsb3 << 16) | lsb2).toLong << 32) | (lsb1.toLong << 16) | lsb0
      )
    }
  }

  /** Strict parser proxy, should be inlined & optimized */
  @inline private[this] final def fromStrictString(uuid: String): UUID =
    if (uuid.length == 36) {
      fromCharArrayViaLookup(uuid.toCharArray, 0, Lookup)
    } else {
      null
    }

  /** Creates a new `UUID` by parsing a `String` in `xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx` format. */
  final def apply(uuid: String): UUID = {
    val res = try {
      fromStrictString(uuid)
    } catch {
      case _: ArrayIndexOutOfBoundsException => null
    }
    if (res eq null) throw new IllegalArgumentException(
      "UUID must be in format xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx, where x is a hexadecimal digit (got: " + uuid + ")")
    res
  }

  /** Extractor which parses a `String` in `xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx` format. */
  final def unapply(uuid: String): Option[UUID] = try {
    Option(fromStrictString(uuid))
  } catch {
    case _: ArrayIndexOutOfBoundsException => None
  }

  /** Allows for parsing using the legacy, non-strict parser used in `java.util.UUID.fromString` */
  @inline final def apply(uuid: String, strict: Boolean): UUID =
    if (strict) {
      apply(uuid)
    } else {
      fromString(uuid)
    }

  /** Generates a random `UUID` (type 4) and returns its lowercase String representation */
  final def randomString: String =
    randomUUID().string // TODO: optimize this not to bother GC with throwaway UUIDs

  // --- Static forwarders ---

  /** Creates a new `UUID` by parsing a `String` in legacy (non-strict) format. */
  @inline final def fromString(name: String): UUID =
    java.util.UUID.fromString(name)

  /** Generates a random `UUID` (type 4) using a cryptographically strong pseudo random number generator. */
  @inline final def randomUUID(): UUID =
    java.util.UUID.randomUUID()

  /** Digests the provided byte array using MD5 and returns a type 3 (name based) `UUID`. */
  @inline final def nameUUIDFromBytes(name: Array[Byte]): UUID =
    java.util.UUID.nameUUIDFromBytes(name)

  // --- Orderings ---

  /** Default JDK UUID ordering */
  val signedOrdering: Ordering[UUID] = Ordering.fromLessThan((x, y) => (x compareTo y) == -1)

  /** Default scala-uuid ordering */
  val unsignedOrdering: Ordering[UUID] = Ordering.fromLessThan(_ < _)
}
