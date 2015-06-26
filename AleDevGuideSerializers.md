# Developer Guide: Serializers and Deserializers #



## Overview ##

Fosstrak offers you some utilities to simplify the handling of the serialization and the deserialization of several xml-specifications. This guide will give you a short overview to the methods available.

For an example where the serializers are used, please refer to the users-guide Capture App Example.

## Serializer Utilities ##

The Fosstrak serializer utilities provide a simple way how you can write an xml-specification into an XML file.

The Fosstrak serializer utilities can be found in the package `org.fosstrak.ale.util` in the class `SerializerUtil`.

Existing serializers for the classes:
  * ECReports
  * ECSpec
  * LRSpec
  * RemoveReaders
  * SetProperties
  * SetReaders

For a detailed discussion refer to the api documentation.


## Deserializer Utilities ##

The Fosstrak deserializer utilities provide a simple way how you can retrieve a specification from an XML file.

The Fosstrak deserializer utilities can be found in the package `org.fosstrak.ale.util` in the class `DeserializerUtil`.

Existing deserializers for the classes:
  * AddReaders
  * ECReports
  * ECSpec
  * LRProperty
  * LRSpec
  * RemoveReaders
  * SetProperties
  * SetReaders

For a detailed discussion refer to the api documentation.