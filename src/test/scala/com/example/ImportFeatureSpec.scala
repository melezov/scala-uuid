package com.example

class ImportFeatureSpec
    extends org.specs2.Specification {

  def is = s2"""
  Importing the functionality of the `io.jvm.uuid` package object
    type alias          ${ViaPackageObjectImport.typeAlias}
    static forwarder    ${ViaPackageObjectImport.staticForwarder}
    rich functionality  ${ViaPackageObjectImport.richFunctionality}
    ordering            ${ViaPackageObjectImport.ordering}

  Extending the trait `io.jvm.uuid.Imports`
    type alias          ${ViaExtendingImportsTrait.typeAlias}
    static forwarder    ${ViaExtendingImportsTrait.staticForwarder}
    rich functionality  ${ViaExtendingImportsTrait.richFunctionality}
    ordering            ${ViaExtendingImportsTrait.ordering}
"""

  object ViaPackageObjectImport {
    import io.jvm.uuid._

    def typeAlias =
      classOf[UUID] ==== classOf[java.util.UUID]

    def staticForwarder =
      UUID.fromString("1-2-3-4-5") ==== java.util.UUID.fromString("1-2-3-4-5")

    def richFunctionality =
      UUID(Array.fill(16)(0xff.toByte)) ==== new java.util.UUID(~0L, ~0L)

    def ordering =
      (UUID(-1, 0) > UUID(1, 0)) ==== true
  }

  object ViaExtendingImportsTrait
      extends io.jvm.uuid.Imports {

    def typeAlias =
      classOf[UUID] ==== classOf[java.util.UUID]

    def staticForwarder =
      UUID.fromString("1-2-3-4-5") ==== java.util.UUID.fromString("1-2-3-4-5")

    def richFunctionality =
      UUID(Array.fill(16)(0xff.toByte)) ==== new java.util.UUID(~0L, ~0L)

    def ordering =
      (UUID(-1, 0) > UUID(1, 0)) ==== true
  }
}
