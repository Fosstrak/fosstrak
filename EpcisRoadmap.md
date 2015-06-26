# Fosstrak EPCIS - Roadmap #

This page lists all tasks that we would like to address in the next major releases. If you feel like contributing to one of these tasks or if you have any other ideas for features you'd love to see implemented, please contact the developer mailing list (see the project mailing lists).

## Support attributes for extensions ##

The Fosstrak EPCIS implementation accepts EPCIS events with custom event fields (event field extensions), but does not support attributes for these event fields. According to the specification, extension elements may have attributes and thus support for such attributes should be added to the Fosstrak EPCIS implementation.

For example, Fosstrak EPCIS should be able to handle to following EPCIS event:

```
<ObjectEvent>
  <eventTime>2008-11-09T13:30:17Z</eventTime>
  <eventTimeZoneOffset>+00:00</eventTimeZoneOffset>
  <epcList>
    <epc>urn:epc:id:sgtin:0057000.123780.7788</epc>
  </epcList>
  ...
  <myns:myextension myattribute="My Attribute">My Extension</myns:myextension>
</ObjectEvent>
```

Above EPCIS event can be successfully captured to the Fosstrak EPCIS repository, but the information in the attribute (e.g., the value "My Attribute") is lost.

## Refactoring of resource layer and performance evaluation ##

The repository is split into the capture operations and the query operations modules. The capture part makes use of Hibernate to separate the database from the domain model. For the query module, we implemented an SQL backend which uses JDBC to connect to the database (see the architecture guide for more details). Unfortunately the query backend is still too much coupled with the actual query operations module. The goal would be to have a fully decoupled front- and backend, such that the DBMS could be easily replaced.

One idea is to use the Java Persistence API, e.g., OpenJPA. As Hibernate implements the JPA interfaces, we could use the same approach for both the capture and query modules.

In order to measure the repository throughput, a thorough performance analysis is also required which should result in a performance benchmark. This benchmark should then indicate whether further work on the resource layer is required in order to improve performance.

## Improvement of test environment and quality assurance ##

The epcis-interop-test module provides a way to test the interoperability of the repository implementation. Unfortunately, the test setup is rather cumbersome and the tests require manual modifications in order to run successfully. Therefore we need an improved test setup with integration tests and further unit tests in order to assure code quality.

The EPCglobal EPCIS Conformance Requirements document is a good place to start.

## More user-friendly object model ##

The current internal object model is automatically generated from XML schema files using JAXB and is used in the repository as well as in the clients. This rather counterintuitive model should be hidden from the user API and instead a new object model wrapping the current model should be provided to the user.

For example, in order to print the timestamps of a number of RFID events in a list, a developer currently has to write the following code:

```
for (int i = 0; i < events.getObjectEventOrAggregationEventOrQuantityEvent().size(); i++) {
  JAXBElement element = (JAXBElement) events.getObjectEventOrAggregationEventOrQuantityEvent().get(i);
  EPCISEventType event = element.getValue();
  System.out.println(event.getEventTime());
}
```

It would be much easier for a developer if he or she could write something like the following:

```
for (EpcisEvent event : events) {
  System.out.println(event.getEventTime());
}
```

## Access control ##

Add a simple access control mechanism to define which events a given user is allowed to retrieve. Restrictions can be based on EPC, read point, biz location, biz step, disposition, time. See also section 8.2.2 in the EPCIS specification.

## Improvement of capture and query clients ##

The Fosstrak EPCIS capture and query clients do not support the entire range of event fields or query parameters, especially when dealing with extensions. For example, adding events or querying for events with event field extensions is not possible. Also, the GUIs are not very appealing from a usability point of view. Improvements and refactoring are necessary.

## Refactoring of triggered query subscriptions ##

Triggered queries are currently implemented with the help of scheduled queries, i.e., the trigger condition is checked using a scheduled query which runs every once in a while (see the [user guide](EpcisUserGuide.md) for information on how to configure at which intervals the repository should check for the trigger condition). This checking for trigger condition adds up to a lot of overhead and might be implemented more efficiently using a different approach.

## Cleanup tasks ##
### Generic ISO8601 time parser ###
The current `org.fosstrak.epcis.utils.TimeParser` can only handle specific cases when event times are parsed. This time parser should be made more generic or could be replaced by an existing ISO8601 parser (http://joda-time.sourceforge.net might be an alternative).
### Generic EPC parser ###
Incoming EPCs are parsed and validated in a very basic way. EPC validation should be handled by [Fosstrak TDT](TdtMain.md) instead.