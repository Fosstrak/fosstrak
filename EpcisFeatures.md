# Features #

The following outlines the features available in Fosstrak's EPCIS Repository implementation and in its Capture and Query Clients.

## Fosstrak EPCIS Modules ##

The Fosstrak EPCIS Project comprises three separate modules:
  * an EPCIS Repository implementation
  * an interactive EPCIS Capture Client
  * an interactive EPCIS Query Client
The modules implement the EPC Information Services (EPCIS) Specification defined by EPCglobal and its members.

## Fosstrak EPCIS Repository Features ##

Our implementation features the EPCIS capture interface and the EPCIS query interface (including the query control interface and the query callback interface) as defined by EPCglobal. We provide an HTTP binding for the capture interface and a SOAP binding for the query interface. Our implementation requires Java 1.5 (or higher) and a MySQL database (version 5.0.15 or higher) as a storage engine.

## Fosstrak EPCIS Capture and Query Client Features ##

The Fosstrak EPCIS Capture and Query Clients provide an intuitive graphical user interface to store EPCIS events to and retrieve EPCIS events from an EPCIS Repository. They can be used to interactively test and explore any EPCIS Repository (based on Fosstrak or third-party products) supporting the HTTP binding for the capture cnterface and the SOAP binding for the query interface.

The following two screenshots show the features available in our EPCIS Capture and Query Clients:

![http://fosstrak.googlecode.com/svn/wikires/epcis/capture_gui.png](http://fosstrak.googlecode.com/svn/wikires/epcis/capture_gui.png)
![http://fosstrak.googlecode.com/svn/wikires/epcis/query_gui.png](http://fosstrak.googlecode.com/svn/wikires/epcis/query_gui.png)

## Detailed Feature List ##

Fosstrak EPCIS supports all mandatory and some optional features of EPCglobal's EPC Information Services (EPCIS) Specification, Version 1.0. The following table gives an overview of which features are supported and which are not.

| **Feature** | **Described in EPCIS Spec. 1.0** | **Supported in Fosstrak EPCIS 0.3** | **Notes** |
|:------------|:---------------------------------|:------------------------------------|:----------|
| Extension mechanism (e.g. new event fields) | Sect. 6.3                        | X                                   | no support for "new vocabulary type" extension |
| Hierarchical vocabularies (e.g. `urn:epc:id:sgtin:123.*`) | Sect. 6.5                        | X                                   |           |
| Core event types (EventData & MasterData) | Sect. 7.2                        | X                                   | EPC syntax is not checked against Tag Data Standard 1.3 |
| Core capture operations (capture interface) | Sect. 8.1                        | X                                   |           |
| Authentication & authorization for capture operations | Sect. 8.1.1                      | -                                   | optional feature |
| Core query operations (query control & query callback interface) | Sect. 8.2                        | X                                   | query paramter `orderBy` does not support ordering for event field extensions, parameters `EQATTR_fieldname_attrname` and `HASATTR_fieldname` do not support event field extensions |
| Authentication & authorization for query operations | Sect. 8.2.1                      | -                                   | optional feature |
| Triggered and scheduled queries | Sect. 8.2.5                      | X                                   |           |
| Error Conditions (exceptions) | Sect. 8.2.6                      | X                                   | `SecurityException` is never thrown, `QueryTooComplexException` is thrown when query execution takes longer than a predefined threshold, `QueryTooLargeException` is thrown when a query returns more results than a predefined threshold |
| Predefined queries (SimpleEventQuery & SimpleMasterDataQuery) | Sect. 8.2.7                      | X                                   |           |
| Message queue binding for capture operations module | Sect. 10.1                       | -                                   | optional feature |
| HTTP binding for the capture operations module | Sect. 10.2                       | X                                   | optional feature |
| SOAP over HTTP binding for query control interface | Sect. 11.2                       | X                                   | optional feature |
| XML over AS2 binding for query control interface | Sect. 11.3                       | -                                   | optional feature |
| XML over HTTP binding for query callback interface | Sect. 11.4.2                     | X                                   | optional feature |
| XML over HTTPS binding for query callback interface | Sect. 11.4.3                     | X                                   | optional feature |
| XML over AS2 binding for query callback interface | Sect. 11.4.4                     | -                                   | optional feature |