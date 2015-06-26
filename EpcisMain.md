# Fosstrak EPCIS #

Fosstrak EPCIS provides an EPCglobal-certified EPCIS Repository as well as Query and Capture clients. In addition to these standards-compliant modules, it also offers a "Webadapter" for easy EPCIS access via web protocols (e.g., REST).

## Status ##

Fosstrak EPCIS is a complete implementation of the EPCIS standard specification (Version 1.0.1 of September 21, 2007). It successfully passed conformance testing and is [EPCglobal-certified](http://www.epcglobalinc.org/certification/).

[![](http://fosstrak.googlecode.com/svn/wikires/epcis/certification_mark.png)](http://www.epcglobalinc.org/certification/)

## How can the Fosstrak EPCIS Project help you? ##

This project allows you to:

  * deploy an EPCIS Repository using our implementation
  * query an existing EPCIS Repository using our graphical user interface
  * fill an existing EPCIS Repository with EPC data using our graphical user interface

## Organization ##

The Fosstrak EPCIS Project comprises three separate modules:

  * an EPCIS Repository implementation
  * an interactive EPCIS Capture Application
  * an interactive EPCIS Query Application

The figure below gives an overview of the Fosstrak EPCIS Implementation. For more information refer to the [Architecture Guide](EpcisArchitectureGuide.md).

![http://fosstrak.googlecode.com/svn/wikires/epcis/architecture.png](http://fosstrak.googlecode.com/svn/wikires/epcis/architecture.png)

## Usage Scenarios ##

Possible usage scenarios include the following:
  * Embed our EPCIS Repository into your own applications to add an EPCIS interface to it
  * Interactively explore any EPCIS Repository using our graphical EPCIS Query Application
  * Use our EPCIS accessing applications as a blueprint to build your own EPCIS Capture or Query Application