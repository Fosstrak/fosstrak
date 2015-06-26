# User Guide: Test Scenarios #



## About these Tests ##

These tests should help you test your Fosstrak Filtering & Collection environment. The tests are wide spread, to cover most of the functionallity of the Filtering and Collection middleware.

To define a subscriber to subscribe to an ECSpec, a very simple way is to start and register an [Eventsink](AleUserGuideQuickStart.md).

## Basic Tests ##

Basic tests with these files:

ReaderAPI
  * [SimpleAPI](http://fosstrak.googlecode.com/svn/wikires/ale/testfiles/simpletests/SimpleAPI.xml): One HALReader with LogicalReader1 connecting to it.

ECspecs
  * [ECSpecCurrent](http://fosstrak.googlecode.com/svn/wikires/ale/testfiles/simpletests/ECSpec.xml),
  * [ECSpecAdditions](http://fosstrak.googlecode.com/svn/wikires/ale/testfiles/simpletests/ECSpecADDITIONS.xml),
  * [ECSpecDeletions](http://fosstrak.googlecode.com/svn/wikires/ale/testfiles/simpletests/ECSpecDELETIONS.xml),
  * [ECSpecAdditionsAndDeletions](http://fosstrak.googlecode.com/svn/wikires/ale/testfiles/simpletests/ECSpecADDITIONSandDELETIONS.xml)

Test Scenario:

1. Current
| Define the ECSpecCurrent | |
|:-------------------------|:|
| Define a subscriber, which subscribes to ECSpecCurrent | |
| Put one Tag on Shelf1 of the HALReader | Tag must appear in all Reports until Tag is removed from Shelf1 |
| Remove Tag from Shelf1 of the HALReader | Tag must not appear any Report from now on |

2. Additions
| Define the ECSpecAdditions | |
|:---------------------------|:|
| Define a subscriber, which subscribes to ECSpecAdditions | |
| Put one Tag on Shelf1 of the HALReader | Tag must only appear in the first following Report |

3. Deletions
| Define the ECSpecDeletions | |
|:---------------------------|:|
| Define a subscriber, which subscribes to ECSpecDeletions | |
| Put one Tag on Shelf1 of the HALReader | Tag must not appear in the Report |
| Remove Tag from Shelf1 of the HALReader | Tag must only appear in the first following Report |

4. Additions & Deletions
| Define the ECSpecAdditionsAndDeletions | |
|:---------------------------------------|:|
| Define a subscriber, which subscribes to the mentioned ECSpec | |
| Put one Tag on Shelf1 of the HALReader | Tag must only appear in the first following Report |
| Remove Tag from Shelf1 of the HALReader | Tag must only appear in the first following Report |

## Compatibility Tests ##

Compatibility tests with these ReaderSimulators:

ReaderAPI:
  * [CompReaderAPI](http://fosstrak.googlecode.com/svn/wikires/ale/testfiles/comptests/CompReaderAPI.xml)

RPReader:
  * Shelf: Shelf1, Shelf2
  * Readtime Interval: 1000 ms
  * Readduration: 500 ms
  * _notificationListenTimeout\_25000/_notificationListenTimeout

RPLogRd1:
  * connects to RPReader

LogRd1:
  * connects to HALReader1

LogRd2:
  * connects to HALReader2

CompLogReader
  * Composite Logical Reader, consitsting of LogRd1 and RPLogRd1

ECSpec:
  * [ECSpec1](http://fosstrak.googlecode.com/svn/wikires/ale/testfiles/comptests/ECSpec1.xml) Connects to LogRd1 only
  * [ECSpec2](http://fosstrak.googlecode.com/svn/wikires/ale/testfiles/comptests/ECSpec2.xml) Connects to LogRd1 and to LogRd2
  * [[ECSpecComp](http://fosstrak.googlecode.com/svn/wikires/ale/testfiles/comptests/ECSpecComp.xml) Connects to CompLogReader

Test Scenario:

1. Change Sources

| Define the ECSpec2 | |
|:-------------------|:|
| Define a subscriber, which subscribes to ECSpec2 | |
| Put one Tag on Shelf1 of the HALReader1 | Tag must appear in Report |
| Put one Tag on Shelf1 of the HALReader2 | Tag must appear in Report |

2. Change Spec

| Define the ECSpec1 | |
|:-------------------|:|
| Define a subscriber, which subscribes to ECSpec1 | |
| Put one Tag on Shelf1 of the HALReader1 | Tag must appear in Report |
| Put one Tag on Shelf1 of the HALReader2	| Tag must not appear in Report |
| LogRd1 disconnects from HALReader1; LogRd connects to HALReader2 using this update for the LRSpec of the LogRd1: [newLRSpecLogRd1](http://fosstrak.googlecode.com/svn/wikires/ale/testfiles/comptests/newLRSpecLogRd1.xml) | |
| Put one Tag on Shelf1 of the HALReader1 | Tag must not appear in Report |
| Put one Tag on Shelf1 of the HALReader2 | Tag must appear in Report |

3. Update Spec

| Define the ECSpecComp | |
|:----------------------|:|
| Define a subscriber, which subscribes to ECSpecComp | |
| Put one Tag on Shelf1 of the HALReader1 | Tag must appear in Report |
| Put one Tag on Shelf1 of the RPReader1 | Tag must appear in Report |
| Update LRSpec of CompReader: remove LogRd1 using "removeReaders" | |
| Put one Tag on Shelf1 of the HALReader1 | Tag must not appear in Report |
| Put one Tag on Shelf1 of the RPReader1 | Tag must appear in Report |
| Update LRSpec of CompReader: remove LogRd1 using "removeReaders"; Update LRSpec of CompReader: add LogRd2 using "addReaders" | |
| Put one Tag on Shelf1 of the HALReader1 | Tag must appear in Report |
| Put one Tag on Shelf1 of the RPReader1 | Tag must not appear in Report |

## Modification Tests ##

Modification tests with these ReaderSimulators:

ReaderAPI:
  * [ModReaderAPI](http://fosstrak.googlecode.com/svn/wikires/ale/testfiles/modtests/ModAPI.xml)

LogRd1,2,3:
  * connect to HALReader1,2,3

CompositeReader:
  * readers: LogicalReader1, LogicalReader2, LogicalReader3

ECSpec:
  * [ECSpec1](http://fosstrak.googlecode.com/svn/wikires/ale/testfiles/modtests/ECSpec1.xml) Connects to LogRd1 only
  * [ECSpec2](http://fosstrak.googlecode.com/svn/wikires/ale/testfiles/modtests/ECSpec2.xml) Connects to LogRd2 only
  * [ECSpecComp](http://fosstrak.googlecode.com/svn/wikires/ale/testfiles/modtests/ECSpecComp.xml) Connects to CompLogReader

CompECSpec:
  * LogicalReader: CompositeReader

ECSpec1:
  * LogicalReader: ModLogRd1

ECSpec2:
  * LogicalReader: ModLogRd2

Test Scenario:

1. One2Many

| Define the CompECSpec | |
|:----------------------|:|
| Define a subscriber, which subscribes to CompECSpec | |
| Put one Tag on Shelf1 of the HALReader1 | Tag must appear in Report |
| Put one Tag on Shelf1 of the HALReader2 | Tag must appear in Report |
| Put one Tag on Shelf1 of the HALReader3 | Tag must appear in Report |

2. Subscribe/Unsubscribe

| Define the ECSpec1 | |
|:-------------------|:|
| Define a subscriber, which subscribes to ECSpec1 | |
| Define the ECSpec2 | |
| Define a subscriber, which subscribes to ECSpec2 | |
| Put one Tag on Shelf1 of the HALReader1 | Tag must only appear in Report of Subsc1 |
| Put one Tag on Shelf1 of the HALReader2 | Tag must only appear in Report of Subsc2 |
| LogRd1 disconnects from HALReader1; LogRd1 connects to HALReader3 using this update for the LRSpec of the LogRd1: [newLRSpecLogRd1](http://fosstrak.googlecode.com/svn/wikires/ale/testfiles/modtests/newLRSpecLogRd1.xml) | |
| LogRd2 disconnects from HALReader2; LogRd2 connects to HALReader1 using this update for the LRSpec of the LogRd1: [newLRSpecLogRd3](http://fosstrak.googlecode.com/svn/wikires/ale/testfiles/modtests/newLRSpecLogRd3.xml) | |
| Put one Tag on Shelf1 of the HALReader1 | Tag must only appear in Report of Subsc2 |
| Put one Tag on Shelf1 of the HALReader3 | Tag must only appear in Report of Subsc1 |

## Multiple Event Cycles Tests ##

Multiple Event Cycles tests with these ReaderSimulators:

ReaderAPI
  * [SimpleAPI](http://fosstrak.googlecode.com/svn/wikires/ale/testfiles/simpletests/SimpleAPI.xml): One HALReader with LogicalReader1 connecting to it.

EC1: [ECSpecFast](http://fosstrak.googlecode.com/svn/wikires/ale/testfiles/ectests/ECSpecFast.xml)
  * LogicalReaders: LogicalReader1
  * duration: 5000ms
  * repeatPeriod: 6000ms
  * ReportName: FastAdd, FastDel
  * reportSet set="ADDITIONS"
  * reportSet set="DELETIONS"

EC2: [ECSpecSlow](http://fosstrak.googlecode.com/svn/wikires/ale/testfiles/ectests/ECSpecSlow.xml)
  * LogicalReaders: LogicalReader1
  * duration: 3000ms
  * repeatPeriod: 60000ms
  * ReportName: Slow
  * reportSet set="CURRENT"

EC3: [ECSpecLong](http://fosstrak.googlecode.com/svn/wikires/ale/testfiles/ectests/ECSpecLong.xml)
  * LogicalReaders: LogicalReader1
  * duration:50000ms
  * repeatPeriod: 70000ms
  * ReportName: Long
  * reportSet set="CURRENT"

Test Scenario:

1. All have it

| Define the EC1 | |
|:---------------|:|
| Define a subscriber, which subscribes to EC1 | |
| Define the EC2 | |
| Define a subscriber, which subscribes to EC2 | |
| Define the EC3 | |
| Define a subscriber, which subscribes to EC3 | |
| Put one Tag on Shelf1 of the HALReader1 | all must have the Tag in their first Report; EC1 must not have the Tag in the following Reports |

2. NoDuplicates

| Define the EC1 | |
|:---------------|:|
| Define a subscriber, which subscribes to EC1 | |
| Define the EC2 | |
| Define a subscriber, which subscribes to EC2 | |
| Define the EC3 | |
| Define a subscriber, which subscribes to EC3 | |
| Put one Tag on Shelf1 of the HALReader1; moved away from Shelf1 and back again repeated times (either as addition or deletion in Subsc1, or as current in Subsc2,3) | all must only have one Tag once in their Report |

## Performance Tests ##

Performance tests with these ReaderSimulators:

  * HALReader1-10:
    * [PerformanceAPI](http://fosstrak.googlecode.com/svn/wikires/ale/testfiles/performancetests/PerformanceAPI.xml) contains 10 HALReader.

  * LogicalReader1-10:
    * connects to HALReader1-10

  * ECSpec1-5:
    * various modes on various readers: [ECSpec1](http://fosstrak.googlecode.com/svn/wikires/ale/testfiles/performancetests/ECSpec1.xml), [ECSpec2](http://fosstrak.googlecode.com/svn/wikires/ale/testfiles/performancetests/ECSpec2.xml), [ECSpec3](http://fosstrak.googlecode.com/svn/wikires/ale/testfiles/performancetests/ECSpec3.xml), [ECSpec4](http://fosstrak.googlecode.com/svn/wikires/ale/testfiles/performancetests/ECSpec4.xml), [ECSpec5](http://fosstrak.googlecode.com/svn/wikires/ale/testfiles/performancetests/ECSpec5.xml)

  * TestAdaptor
    * LogicalReader1 subscribes to PerReader1
    * Execute the TestAdaptor on PerReader1 with the following parameters
      * tps (tags per shot) = 100
      * wt (wait time) = 1000
      * gain (tags more per shot) = 5
    * --> monitor processor and memory load on your system
    * NOTICE:
      * The TestAdaptor does not create real tags. These are just random strings/numbers!
      * Pay attention with the gain factor. The load of your system increases fast with increasing tags per shot!

  * ManyReaders
    * PerLogRd1-10 subscribes to a set of PerReader1-10
    * Load all Shelves with Tags
    * Run System some hours
    * --> monitor processor and memory load on your system