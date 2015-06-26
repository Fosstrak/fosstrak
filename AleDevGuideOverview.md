# Developer Guide: Overview #

## Objective ##

The Filtering and Collection middleware provides an interface through which clients may interact with filtered, consolidated EPC data and related data from a variety of sources. Its an implementation of the EPCglobal standart ALE 1.1.

## Overview ##

From a global view one can split the Fosstrak Filtering and Collection framework into 3 layers that are built on top of each other.

  * org.fosstrak.ale.server: This layer provides the interface to the client through the class ALE. This includes:
    * Tag filtering and reports generation
    * EventCycle and ReportsGenerator administration
    * Client subscription/unsubscription
  * org.fosstrak.ale.server.readers: This layer models the logical reader API. This includes:
    * Tag aggregation
    * Reader management
  * org.fosstrak.ale.server.readers.XYZ: This layer acts as the layer to the physical hardware. This includes:
    * Aquiring of the tags
    * Maintenance of reader connections

![http://fosstrak.googlecode.com/svn/wikires/ale/classes_overview.png](http://fosstrak.googlecode.com/svn/wikires/ale/classes_overview.png)

## Detailed information ##

For detailed information please refer to the chapters:
  * [Application Layer Events](AleDevGuideAle.md)
  * [Event Cycle](AleDevGuideEventCycle.md)
  * [Reports generation](AleDevGuideReportsGeneration.md)
  * [Reports notification](AleDevGuideReportsNotification.md)
  * [LogicalReader Concept](AleDevGuideLogicalReaderConcept.md) (logical reader API)