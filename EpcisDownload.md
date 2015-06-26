# Fosstrak EPCIS Download #



## Latest Release ##

| **Module** | **Version** | **Download** |
|:-----------|:------------|:-------------|
| EPCIS Repository | 0.5.0       | [epcis-repository-0.5.0-bin-with-dependencies.zip](https://oss.sonatype.org/content/repositories/public/org/fosstrak/epcis/epcis-repository/0.5.0/epcis-repository-0.5.0-bin-with-dependencies.zip) |
| EPCIS Capture Client | 0.5.0       | [epcis-captureclient-0.5.0-bin-with-dependencies.zip](https://oss.sonatype.org/content/repositories/public/org/fosstrak/epcis/epcis-captureclient/0.5.0/epcis-captureclient-0.5.0-bin-with-dependencies.zip) |
| EPCIS Query Client | 0.5.0       | [epcis-queryclient-0.5.0-bin-with-dependencies.zip](https://oss.sonatype.org/content/repositories/public/org/fosstrak/epcis/epcis-queryclient/0.5.0/epcis-queryclient-0.5.0-bin-with-dependencies.zip) |
|            |             |              |
| EPCIS Webadapter | 0.1.0       | [epcis-webadapter-0.1.0-bin-with-dependencies.zip](https://oss.sonatype.org/content/repositories/public/org/fosstrak/webadapters/epcis-webadapter/0.1.0/epcis-webadapter-0.1.0-bin-with-dependencies.zip) |

The source code for these binaries can be found in the Subversion repository at http://fosstrak.googlecode.com/svn/epcis/tags/epcis-0.5.0 and http://fosstrak.googlecode.com/svn/webadapters/tags/epcis-webadapter-0.1.0.

## System Requirements ##
  * JDK 1.6.0\_05 or higher

## Installation and Usage Instructions ##
Please see the [User Guide](EpcisUserGuide.md) for installation and usage instructions.

## Changelog ##

### Version 0.5 (released December 28, 2010) ###
#### EPCIS Repository ####
  * Fixed parsing of query parameter with more than one value in the list of strings when querying for extension fields (e.g., EQ\_fieldname).
  * Added support for capturing EPCIS masterdata. Please see the user-guide or the mailing list for details on how to use the new interface. Thanks to Nikos Kefalakis.
  * Updated CXF dependencies to 2.2.9 and JAXB to 2.2
  * Added support for storing milliseconds of eventTime and recordTime to the repository. Fixes issues with fine-grained time queries and with rounded time values in query responses. Thanks to Adrien Laurence.
  * Added a JSP with an HTML form to quickly debug and submit capture requests directly via browser.
  * Introduced JSP pages for the repository to show the requested information for browser GET requests.
  * Fixed handling of dbReset scripts. Furthermore, use Hibernate to run the scripts (removes the need of deprecated method).
  * Disabled the Hibernate query\_cache since we're mainly running updates/inserts and no queries.
  * Set the antiResourceLocking flag in the application context to true in order to prevent JAR file locking and allow clean undeploys on Windows systems.
  * Use correct number of SQL joins for queries with two or more EQ\_fieldname parameters. Fixes [2495950](http://sourceforge.net/tracker/?func=detail&aid=2495950&group_id=170911&atid=856005). Thanks to Oscar Saiz.`
#### EPCIS Capture Client ####
  * Added support for capturing EPCISMasterDataDocument in CaptureClient.
  * Code refactoring and cleanup.
#### EPCIS Query Client ####
  * Updated CXF dependencies to 2.2.9
  * Code refactoring and cleanup.

### Version 0.4.2 (released February 3, 2009) ###
#### EPCIS Repository ####
  * Subscriptions not tolerant to server restart. Fixes [2379949](http://sourceforge.net/tracker/?func=detail&aid=2379949&group_id=170911&atid=856005). Thanks to Marc-Antoine Mouilleron.
  * Fixed query subscription error (unable to serialize and store objects of type ArrayOfString to database) Fixes [2379542](http://sourceforge.net/tracker/?func=detail&aid=2379542&group_id=170911&atid=856005). Thanks to Nektarios Leontiadis.
  * Updated demo data. Example events are now compliant with EPCglobal's Core Business Vocabulary.
  * Added "resource-ref" mapping in web.xml for easier deployment in containers other than Tomcat.
#### EPCIS Capture Client ####
  * Added support for connecting to EPCIS repositories that require client authentication (HTTP Basic and HTTPS with client certificates).
  * Updated example events. They are now compliant with EPCglobal's Core Business Vocabulary.
#### EPCIS Query Client ####
  * Added support for connecting to EPCIS repositories that require client authentication (HTTP Basic and HTTPS with client certificates).
  * Fixed some issues with huge pop-up windows showing stack traces.
  * Removed MATCH\_childEPC from list of operators that can be selected by users. (MATCH\_childEPC was defined in a draft version of the EPCIS specification but does not exist in the final release.)
  * Updated example queries. They are now compliant with EPCglobal's Core Business Vocabulary.

### Version 0.4.1 (released September 2, 2008) ###
#### EPCIS Repository ####
  * Results of subscribed queries were not reported correctly. Fixes [2088556](http://sourceforge.net/tracker/?func=detail&aid=2088556&group_id=170911&atid=856005).
  * Subscriptions were not stored in the database. Fixes [2088551](http://sourceforge.net/tracker/?func=detail&aid=2088551&group_id=170911&atid=856005).

### Version 0.4.0 (released July 29, 2008) ###
Renamed packages from org.accada to org.fosstrak.

### Version 0.3.2 (released June 4, 2008) ###
#### EPCIS Repository ####
  * Fixed parsing of values for extension fields. Fixes [1964350](http://sourceforge.net/tracker/?func=detail&aid=1964350&group_id=170911&atid=856005).
  * Fixed epcis\_demo\_data.sql. (Demo data did not match latest schema.)
#### EPCIS Query Client ####
  * Fixed interoperability issues of query client GUI with other vendors' EPCIS repository. (The GUI used a wrong query name when requesting subscription IDs from repositories.) - Thanks to Stefan Schweizer for pointing this out.

### Version 0.3.1 (released March 24, 2008) ###
#### EPCIS Repository ####
  * Fixed inconsistent capitalization of table names, which caused problems on case-sensitive platforms (e.g., Linux).
#### EPCIS Query Client ####
  * Disabled chunked HTTP transfers in order to make the query client work with repositories operating behind an Apache proxy server. (Apache's mod\_proxy\_http module does not support chunked requests.)

### Version 0.3.0 (released March 20, 2008) ###
#### EPCIS Repository ####
  * Introduced Spring IoC container for bean wiring and application initialization.
  * Introduced interfaces wherever reasonable (program to an interface, not an implementation), and improved extensibility and interoperability through application layering.
  * Added a context listener which performs the initialization work (e.g., initializing the logging subsystem) when the application is deployed.
  * Added support for storing custom, non-standard vocabulary fields.
  * Improved and centralized all configuration parameters in a consistent application.properties file.
  * Use StringBuilder instead of StringBuffer or string concatenation wherever reasonable, for improved performance.
  * Use the commons-logging API as a wrapper for Log4J for all the log statements.
  * Added database indexes wherever reasonable for improved performance.
  * Moved EPCIS WSDL and schema files to module epcis-commons.
  * Removed 'vocabularies' table from the database schema - the mapping of vocabulary names to the corresponding database table is now handled in the code.
  * Added utility class for parsing and printing QueryResults.
  * Replaced Axis-generated Web service stubs with CXF-generated Java beans.
  * Fixed TimeParser to catch any exceptions caused by invalid input and wrap them into QueryParameterExceptions.
  * Query Interface: replaced the Web service framework Apache Axis 1 with Apache CXF which prevents many problems and avoids many workarounds implied by Axis, and improves performance.
  * Query Interface: introduced a JAXB data binding to map Web service requests and responses to Java beans.
  * Query Interface: added support for HTTPS binding for query callback interface.
  * Query Interface: added support for querying custom, non-standard vocabulary fields.
  * Query Interface: added a servlet to perform the initialization work for the QueryOperationsModule in cases where Spring cannot be used.
  * Query Interface: added support for database transactions - queries which modify the database (e.g., add vocabularies) either succeed or fail in their entirety.
  * Query Interface: standing query results are returned within an EPCISQueryDocument whose EPCISBody contains the response payload XML, as specified in the EPCIS standard.
  * Query Interface: callback of results for standing queries is retried (up to 3 times) if the destination cannot be reached or in cases of network problems.
  * Query Interface: improved handling of children-relationships for masterdata vocabularies.
  * Query Interface: throw QueryParameterException for any invalid user-provided query parameter values.
  * Query Interface: fixed issues with not releasing database connections which prevented sessions from being closed under certain conditions and crashed the entire repository.
  * Query Interface: 'getVendorVersion' must return a valid URI-formated value.
  * Query Interface: ordering of events according to the 'orderBy' and 'orderDirection' query parameters must be global across all events.
  * Query Interface: restriction of events according to 'eventCountLimit' must be global across all events.
  * Query Interface: query returns events out of specified time frame. Fixes [1876336](http://sourceforge.net/tracker/?func=detail&aid=1876336&group_id=170911&atid=856005).
  * Capture Interface: added initialization logic to the CaptureOperationsServlet in cases where Spring cannot be used.
  * Capture Interface: introduced Hibernate which replaces the JDBC statements for persisting events to the database, adds an additional database abstraction layer, and thus allows for the possibility of integrating other (non-MySQL) data stores. Thanks to Sean Wellington.
  * Capture Interface: added support for database transactions - requests either succeed or fail in their entirety. Thanks to Sean Wellington.
  * Set the "defaultAutoCommit" property of the database connection pool to false. Thanks to Sean Wellington.
  * Capture Interface: check value of the 'eventTimeZoneOffset' field to match the expected pattern.
  * Capture Interface: check elements in the 'epcList', 'childEPCs', and 'parentID' fields to match the pure identity URI pattern as defined in EPCglobal Tag Data Standard 1.3.1.
  * Capture Interface: return HTTP status code different from 200 for any invalid user-provided input (e.g., invalid XML schema, invalid XML values).
#### EPCIS Capture Client ####
  * Added possibility to construct EPCIS events using the JAXB data binding API and capture them via the CaptureClient.
  * Added some utility classes for quickly sending capture requests to an EPCIS repository.
#### EPCIS Query Client ####
  * Replaced the Web service framework Apache Axis 1 with Apache CXF.
  * Introduced a JAXB data binding to map Web service requests and responses to Java beans.
  * Added some utility classes for quickly sending query requests to an EPCIS repository.
  * Configured QueryClientGui to reuse functionality provided by QueryControlClient.

### Version 0.2.3 (released December 14, 2007) ###
#### EPCIS Repository ####
  * Made CaptureOperationsModule more thread-safe and made sure that database resources are released under all circumstances. Fixes [1789785](http://sourceforge.net/tracker/?func=detail&aid=1789785&group_id=170911&atid=856005). Thanks to Sean Wellington.
  * Extracted Tomcat-specific initialization logic from CaptureOperationsModule into a new class CaptureOperationsServlet, such that CaptureOperationsModule can be used in other application servers. Thanks to Sean Wellington.
  * MATCH\_anyEPC query did not match against URIs in the parentID field of Transaction- and AggregationEvents. Fixes [1844046](http://sourceforge.net/tracker/?func=detail&aid=1844046&group_id=170911&atid=856005).
  * parentID field of TransactionEvents was ignored in CaptureOperationsModule. Fixes [1844041](http://sourceforge.net/tracker/?func=detail&aid=1844041&group_id=170911&atid=856005).
  * Fixed storing and retrieving of time values to/from repository. Time zone offset does not need to be subtracted/added from event time or record time. Fixes [1789796](http://sourceforge.net/tracker/?func=detail&aid=1789796&group_id=170911&atid=856005).
  * Fixed QuerySchedule to correctly handle leap years.
  * Added test case for TimeParser.
  * Corrected serialization of time values into XML. Fixes [1845531](http://sourceforge.net/tracker/?func=detail&aid=1845531&group_id=170911&atid=856005).
  * Fixed parsing of event time when time value does not have exactly three digits for millisecond. Fixes [1789784](http://sourceforge.net/tracker/?func=detail&aid=1789784&group_id=170911&atid=856005).
#### EPCIS Capture Client ####
  * Corrected serialization of time values into XML. Fixes [1845531](http://sourceforge.net/tracker/?func=detail&aid=1845531&group_id=170911&atid=856005).
  * Display time values in ISO8601 format.
  * Added field for setting eventTimeZoneOffset in capture client GUI and adjusted example event.

### Version 0.2.2 (released August 31, 2007) ###
#### EPCIS Repository ####
  * HTTP Capture Interface in 0.2.1 was not EPCIS compliant. Fixed capture operation which now expects the EPCISDocument as payload in the HTTP POST request. Fixes [1781884](http://sourceforge.net/tracker/?func=detail&aid=1781884&group_id=170911&atid=856005).
  * Fixed Null pointer exception. Fixes [1781885](http://sourceforge.net/tracker/?func=detail&aid=1781885&group_id=170911&atid=856005).
#### EPCIS Capture Client ####
  * Changed access method for invoking EPCIS capture interface in CaptureClient. The capture interface expects an XML document conforming to the EPCISDocument in the payload of the HTTP POST request.
  * Changed return type of capture method in CaptureClient. It will now return the HTTP response code (received from the EPCIS repository) instead of the message given in payload of the HTTP response. The CaptureClientGui will display an appropriate success or fail message.
#### EPCIS Query Client ####
  * Corrected parsing of time values from repository response. Fixes [1845531](http://sourceforge.net/tracker/?func=detail&aid=1845531&group_id=170911&atid=856005).
  * Display time values in ISO8601 format.
  * Display error message when no event type is selected in GUI.

### Version 0.2.1 (released August 10, 2007) ###
#### EPCIS Repository ####
  * Scheduled queries were not restored properly after a service restart. Fixes [1725569](http://sourceforge.net/tracker/?func=detail&aid=1725569&group_id=170911&atid=856005).
  * Changed demo database to make all examples shown in Query Client work.
  * Changed build environment: Added profiles (dev, prod) to the build environment with specific settings for a development and a productive environment (you can activate a profile by adding e.g. "-Denv=dev" to your mvn build command).
#### EPCIS Query Client ####
  * axis-wsdl4j was not properly included in Webstart application.

### Version 0.2.0 (released April 27, 2007) ###
Initial release.