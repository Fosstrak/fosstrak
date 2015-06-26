# Fosstrak ALE Download #




## Latest Release ##


| **Module** | **Version** | **Download** |
|:-----------|:------------|:-------------|
| Filtering and Collection Server | 1.2.0       | [fc-server-1.2.0-bin-with-dependencies.zip](https://oss.sonatype.org/content/repositories/public/org/fosstrak/fc/fc-server/1.2.0/fc-server-1.2.0-bin-with-dependencies.zip) |
| Standalone Client | 1.2.0       | [fc-client-1.2.0-bin-with-dependencies.zip](https://oss.sonatype.org/content/repositories/public/org/fosstrak/fc/fc-client/1.2.0/fc-client-1.2.0-bin-with-dependencies.zip) |
| Web-Based Client | 1.2.0       | [fc-webclient-1.2.0-bin-with-dependencies.zip](https://oss.sonatype.org/content/repositories/public/org/fosstrak/fc/fc-webclient/1.2.0/fc-webclient-1.2.0-bin-with-dependencies.zip) |
|            |             |              |
| Capturing Application | 0.1.1       | [capturingapp-0.1.1.war](https://oss.sonatype.org/content/repositories/public/org/fosstrak/capturingapp/capturingapp/0.1.1/capturingapp-0.1.1.war) |

The source code for these binaries can be found in the Subversion repository at http://fosstrak.googlecode.com/svn/fc/tags/fc-1.2.0 and at https://fosstrak.googlecode.com/svn/capturingapp/tags/capturingapp-0.1.1.


## Previous Releases ##

| **Module** | **Version** | **Download** |
|:-----------|:------------|:-------------|
| Filtering and Collection Server | 1.0.2       | [fc-server-1.0.2-bin-with-dependencies.zip](https://oss.sonatype.org/content/repositories/public/org/fosstrak/fc/fc-server/1.0.2/fc-server-1.0.2-bin-with-dependencies.zip) |
| Standalone Client | 1.0.2       | [fc-client-1.0.2-bin-with-dependencies.zip](https://oss.sonatype.org/content/repositories/public/org/fosstrak/fc/fc-client/1.0.2/fc-client-1.0.2-bin-with-dependencies.zip) |
| Web-Based Client | 1.0.2       | [fc-webclient-1.0.2-bin-with-dependencies.zip](https://oss.sonatype.org/content/repositories/public/org/fosstrak/fc/fc-webclient/1.0.2/fc-webclient-1.0.2-bin-with-dependencies.zip) |
|            |             |              |
| Capturing Application | 0.1.1       | [capturingapp-0.1.1.war](https://oss.sonatype.org/content/repositories/public/org/fosstrak/capturingapp/capturingapp/0.1.1/capturingapp-0.1.1.war) |

The source code for these binaries can be found in the Subversion repository at http://fosstrak.googlecode.com/svn/fc/tags/fc-1.0.2 and at https://fosstrak.googlecode.com/svn/capturingapp/tags/capturingapp-0.1.1.

<a href='Hidden comment: 
== Latest Snapshots ==

|| Filtering and Collection Server || 1.1.1-Snapshot || [https://oss.sonatype.org/content/repositories/snapshots/org/fosstrak/fc/ fc-1.1.1-Snapshots] ||

'></a>

## System Requirements ##
  * JDK 1.6.x or higher

## Installation and Usage Instructions ##
Please see the [User Guide](AleUserGuideQuickStart.md) for installation and usage instructions.

## Changelog ##

### Version 1.2.0 ###
#### Filtering and Collection Server ####
  * Use business Exceptions instead of WSDL generated interface exceptions.
  * Use Spring to configure a context and initialize the ALE.
  * Rewrite Logical Reader Manager to provide non-static methods (rewrite as bean).
  * Rewrite ALE to provide non-static methods (rewrite as bean).
  * Upgrade CXF to latest stable version.
  * Abstract EventCycle into Interface and Implementation.
  * Abstract ReportsGenerator into Interface and Implementation.
  * Major code optimizations regarding coding style and performance.
  * Testcases.

#### Filtering and Collection Client ####
  * preset the endpoint with the current release.

#### Filtering and Collection Commons ####
  * Business Exceptions for ALE.


### Version 1.1.1-Snapshot ###
#### Filtering and Collection Server ####
  * Port fix provided by Gianrico D'Angelis
  * Patch to LLRPControllerImpl.java provided by Vladimir Dzalbo

### Version 1.1.0 (released April 8th, 2012) - new Feature Release by Orange Dev Team. ###
#### Filtering and Collection Server ####
  * The configuration files of the ALE middleware are now persistent to eliminate the need to configure instance at each server startup. (by plomion).
  * ALE controller service to start/stop ECSpecs using a SOAP interface. (by plomion).
  * Manage the UserMemory of a Tag in the ECReport and the LLRPAdaptor. Modify the CaptureApp to store the User Memory. (by soubra).
  * LLRP controller service to manage ROSpec using SOAP interface. (by soubra).
  * Automatic configuration of the persisted ROSpec/AccessSpec for LLRP Reader. (by soubra).

### Version 1.0.2 (released August 14, 2009) ###
#### Filtering and Collection Server ####
  * When connecting the ALE to a LLRP reader, it should be possible to create one logical reader per one antenna. Eg there are four antennas on the pysical LLRP reader, therefore we should be allowed to create a logical reader per each antenna. Fixes [2829908](http://sourceforge.net/support/tracker.php?aid=2829908).
  * Switch to newest llrp-adaptor version (1.1.0).

### Version 1.0.1 (released July 24, 2009) ###
#### Filtering and Collection Server ####
  * tag format in ECReports is wrong for rawHex and rawDecimal. Fix implements decimal as epc-decimal and hex as epc-hex. Fixes [2822988](http://sourceforge.net/support/tracker.php?aid=2822988).
  * When sending notifications to URI with resource path, the 'remainder-of-URL' is merged with 'HTTP/1.1'. Fixes [2822918](http://sourceforge.net/support/tracker.php?aid=2822918).
  * tdt within hal adapter crashes when hex-tag with 64bit length is submitted: fixed by using tdt conversion algorithms to convert between binary and hex. further introduced stronger exception handling. Fixes [2816225](http://sourceforge.net/support/tracker.php?aid=2816225).
  * implemented "stat profiles" according to ALE 1.1 Standard 8.3.6.
  * eventcycle crashes with null-pointer exception, when tag-information is not set: catch the exception and set the requested report-field according to ale 1.1 spec to null. Fixes [2816251](http://sourceforge.net/support/tracker.php?aid=2816251).

### Version 1.0.0 (released June 9, 2009) ###
#### Filtering and Collection Server ####
  * update TDT to version 0.9
  * add llrp support through LLRPAdaptor. together with fosstrak LLRP Commander LLRP enabled reader can be configured and maintained.
  * in eventcycle when repeatPeriod and durationValue were the same value, the eventcycle missed tags. Fixes [2481005](http://sourceforge.net/support/tracker.php?aid=2481005).
  * update from apache-cxf-2.0.4-incubator to the stable apache-cxf-2.0.9.
  * fixed issue with war packaging and wrong xerces version (1.2.3 instead of 2.8.0).

### Version 0.4.1 (released July 29, 2008) ###
#### Filtering and Collection Server ####
  * fixed sf [bug 2054573](https://code.google.com/p/fosstrak/issues/detail?id=054573) Fixes [2054573](http://sourceforge.net/support/tracker.php?aid=2054573).

### Version 0.4.0 (released July 29, 2008) ###
  * Renamed packages from org.accada to org.fosstrak.

### Version 0.3.0 (released March 2008) ###
#### Filtering and Collection Server ####
  * fc-server now supports the full logical reader api.
  * adapted fc-webclient to the new fc-commons.
  * modified documentation according to the schema in epcis.
  * fixed sf [bug 1892541](https://code.google.com/p/fosstrak/issues/detail?id=892541). a HALAdaptor now supports several logical readers onto one physical hal device. Fixes [1892541](http://sourceforge.net/support/tracker.php?aid=1892541).
  * Added a TestAdaptor to the project.
  * fixed tag filtering (now additions and deletions work properly for hal and reader protocoll). A nullpointer exception could be caused in Report.java as input was not properly checked. EventCycle.java does no more write tags to the Report.java. Report.java gets the tags from the EventCycle.java when computing the report. Fixes [1876633](http://sourceforge.net/support/tracker.php?aid=1876633).
  * EventCycle crashes by nullpointer exception after one run. Fixes [1873580](http://sourceforge.net/support/tracker.php?aid=1873580).
  * Adapted the new config-file loader from the HAL.
  * RPAdaptor now uses an identifyThread to poll the available tags on the reader in a regular interval. the poll will return all tags, this means filtering needs to be done inside the logical reader API (eg Report.java).
  * removed physicalSourceStub. removed physicalReaderStub. The stubs are no more needed. The InputGenerator contacts the rp-proxy directly.
  * Adapted to changed HAL interface.
  * Adapted to new EventCycle.
  * Adaptor for a reader protocol device. with this adaptor you can use a rp-device inside the LogicalReader API as a LogicalReader.
  * added jaxb accessors to xml.
  * Adaptor for a HAL device. with this adaptor you can use a hal-device inside the LogicalReader API as a LogicalReader.
  * New Logical Reader API is used to support CompositeReaders according the EPC Standard, !LogicalReaderAPI chapter (10.3 ff).
#### Standalone Client ####
  * fc-client now supports the full logical reader api.
  * adapted fc-client to the new fc-commons.
  * modified documentation according to the schema in epcis.
#### Web-Based Client ####
  * fc-webclient now supports the full logical reader api.
  * adapted fc-webclient to the new fc-commons.
  * modified documentation according to the schema in epcis.
  * integration of Logical Reader API into ALEService. adaption of the webclient UI to support the new methods.

### Version 0.2.0 (released April 27, 2007) ###
  * Initial code release.