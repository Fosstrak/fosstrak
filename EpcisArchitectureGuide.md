# EPCIS Architecture Guide #



## About this Guide ##

This guide describes the architecture and design of Fosstrak's EPCIS implementation. It is intended as an introduction to the project for future developers who are somewhat familiar with the EPCglobal EPC Information Services (EPCIS) standard. The latest version of the standard is available at EPCglobal (http://www.epcglobalinc.org/standards/epcis).

## Introduction ##

The goal of EPCIS is to enable applications to incorporate EPC-related data into their businesses. It provides means to store EPC data persistently and offers a framework to add data to a repository as well as query it. In order to implement these means, Fosstrak's EPCIS project provides three separate modules: an EPCIS repository, an EPCIS capture application, and an EPCIS query application. The following figure gives a short overview of the basic interaction scenarios for these three modules.

![http://fosstrak.googlecode.com/svn/wikires/epcis/architecture.png](http://fosstrak.googlecode.com/svn/wikires/epcis/architecture.png)

The architectural style is client server; a client is either an EPCIS capture application, an EPCIS query application, or both. The server is an EPCIS repository providing the interfaces to which clients connect. The repository parses client requests and processes them according to the rules defined in the specification. The transport protocols used by the client applications are XML over HTTP and SOAP over HTTP, respectively.

In the following sections we will describe the repository and the client applications in more details.

## EPCIS Repository ##

The Fosstrak EPCIS repository implements the following five bindings as defined by the EPCIS specification:

  * XML binding for the data definition layer
  * HTTP binding for the capture interface
  * SOAP/HTTP binding for the query control interface
  * HTTP binding for the query callback interface
  * HTTPS binding for the query callback interface

In the following sections we will describe how these bindings are implemented.

### Access Tier ###

The access tier is the entry point into the EPCIS repository and provides the interfaces used by the client applications to access the repository.

#### Capture Interface ####

The HTTP binding for the capture interface is implemented by providing a Java Servlet which is registered with the Servlet container (we use Apache Tomcat). The Servlet receives capture requests from an EPCIS capture application. These requests must include EPCIS events serialized in XML and must be given in the payload of an HTTP POST request (see the EPCIS specification for details). The Servlet validates the incoming XML documents against the EPCIS schema and passes it to the CaptureOperationsModule in the business tier.

#### Query Control Interface ####

In order to implement the SOAP/HTTP binding for the query control interface, we use the [Apache CXF Web service framework](http://incubator.apache.org/cxf/) in combination with the [JAX-WS API](https://jax-ws.dev.java.net/) and a [JAXB data binding](https://jaxb.dev.java.net/) to map Java objects to XML and vice versa. CXF maps the contents of the incoming SOAP requests to Java objects and hands them over to the `QueryOperationsModule` in the business tier for processing.

#### Business Tier ####

The business tier receives input from the access tier. As indicated above, it consists of two seperate modules: the `CaptureOperationsModule` and the `QueryOperationsModule`.

#### Capture Operations Module ####

The `CaptureOperationsModule` simply takes EPCIS events and stores their contents into the RDBMS. We use Hibernate to map the domain model objects to the relational database and generate the required SQL calls.

#### Query Operations Module ####

The `QueryOperationsModule` generates SQL queries dynamically from the given query parameter values and sends them to the resource tier for processing. The result sets returned by the resource tier are mapped to the EPC event domain model and are handed over to the access tier which in turn sends the results back to the client application.

#### Query Callback Interface ####

Unlike the query control interface, the query callback interface is not directly accessible for client applications. Instead, a client application subscribes a query via the query callback interface whereupon the repository returns the query results via the query callback interface.

Unlike polled queries which are stateless, subscribed queries have to keep track of things like their last execution time, the destination URL for reporting results, the query parameters, and when or on what event the query should be executed. To meet these requirenments we keep references to subscriptions in the Web application context. This avoids subscriptions from being garbage collected and retains them across service invocations. Furthermore, we also store the subscriptions in the database in order to save them across application restarts.

The component which implements the query callback interface is the `QuerySubscription`. It represents subscribed queries which are either scheduled or triggered: A `QuerySubscriptionScheduled` represents a subscription associated with a schedule and a query. The query is invoked whenever the timer of the schedule times out. A `QuerySubscriptionTriggered`, on the other side, represents a subscription associated with a trigger URI and a query. The query is invoked whenever an event is captured that includes the specified trigger URI.

Subscribed queries are executed just like polled queries, i.e., using the `poll` method of the `QueryOperationsModule`. The results are returned to the client by serializing the query results into XML and sending it in the payload of an HTTP/HTTPS POST request.

#### Resource Tier ####

The resource tier consists of a relational database management system (RDBMS) which stores all the event data, the vocabularies, and the subscriptions. The figure below shows the database schema for ObjectEvents. The schemas for Transaction-, Quantity-, and AggregationsEvents are similar to the one below and are not reproduced here.

![http://fosstrak.googlecode.com/svn/wikires/epcis/db_objectevents.png](http://fosstrak.googlecode.com/svn/wikires/epcis/db_objectevents.png)

Standard vocabularies each have their own database table. Below, we show the schema for _Business Transaction_, _Disposition_, and _Read Point_ vocabularies. Non-standard vocabularies are stored in the `voc_any` database table.

![http://fosstrak.googlecode.com/svn/wikires/epcis/db_vocabularies.png](http://fosstrak.googlecode.com/svn/wikires/epcis/db_vocabularies.png)

As mentioned before we also store the subscriptions in the RDBMS. The corresponding schema looks as follows:

![http://fosstrak.googlecode.com/svn/wikires/epcis/db_subscriptions.png](http://fosstrak.googlecode.com/svn/wikires/epcis/db_subscriptions.png)

## EPCIS Capture Application ##

An EPCIS capture application is used for capturing events to the repository. Because we implement the HTTP/POST binding of the capture interface, a capture application must send the XML events in the payload of an HTTP POST requests to the repository. We use the JAXB data binding to map EPCIS event domain model objects to XML and vice versa.

The Fosstrak EPCIS capture application provides both a capture client API and a capture client GUI. The API can be used by other applications to send capture requests to the repository, and the GUI can be used to manually insert EPCIS events into a repository. Please refer to the User Guide for further information on how to use the capture client API or GUI.

## EPCIS Query Application ##

An EPCIS query application submits queries to an EPCIS repository using SOAP requests. The query application also uses the CXF Web service framework to generate the SOAP requests, and uses the JAXB data binding to map the Java objects to XML.

Similar to the capture application, the query application provides both a query client API and a query client GUI. Please refer to the User Guide for details about how to use the query client API or GUI.