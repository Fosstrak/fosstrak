# FAQ #



# Fosstrak General #

## Is this a reference implementation of the EPC Network? ##
No.

## Are there other open source projects incorporating the EPC Network standards? ##
Yes, there are. There have been several attempts to release open source software in the RFID domain. A number of projects never ended up releasing any software though. Two very promising efforts are the [LLRP Toolkit Project](http://www.llrp.org/) to which the Fosstrak team contributed the Java implementation and the [Rifidi](http://www.rifidi.org/) project.

## Where can I find more information about the EPC Network? ##
Check out the website of [EPCglobal](http://www.epcglobalinc.org/) and in particular the architecture framework document. Or check out this paper we recently wrote on RFID system interfaces.

## Which components of the EPC Network have you implemented? ##
Have a look at the [overview page](http://code.google.com/p/fosstrak).

## What is EPCglobal? ##
EPCglobal is a subscriber-driven organisation comprised of industry leaders and organisations focused on creating global standards for the EPCglobal Network.

## Can I contribute to this project even, if I am not part of the Auto-ID Labs or EPCglobal? ##
Yes. Fosstrak is an independent, community-driven open source project initiated by the Auto-ID Lab in Switzerland with contributions from the lab at the University of Cambridge. While initiated by the Auto-ID Lab at ETH Zurich/University St. Gallen and the Distributed Systems Group at ETH Zurich, it is open for participation by everyone.

## How is this Fosstrak project different from other RFID open source efforts? ##
The Fosstrak project has an active developer community with significant RFID middleware experience and insights into the RFID/EPC standardisation process. There is a strong focus on implementing the specifications developed by EPCglobal and its members and providing useful tools for these implementations.

## Why are you using Maven and how can I use it? ##
The Fosstrak project uses [Maven](http://maven.apache.org/) for the build and project management. An introduction to running maven can be found here. There is also information available on the [maven eclipse intregration](http://m2eclipse.codehaus.org/).


# Fosstrak EPCIS #

## What is EPCIS? ##
EPCIS is a standard to exchange RFID-related events between businesses. For more information, please see EPCglobal's EPCIS FAQ at http://www.epcglobalinc.org/standards/epcis/epcis_1_0-faq-20070427.pdf.

## Is this a reference implementation of the EPCglobal EPC Information Services (EPCIS) Specification? ##
No, but the Fosstrak EPCIS repository has been EPCglobal certified.

## Can I contribute to this project even if I am not part of the Auto-ID Labs or EPCglobal? ##
Yes. While initiated by the Auto-ID Lab at ETH Zurich/University St. Gallen and the Distributed Systems Group at ETH Zurich, it is open for participation by everyone.

## Where can I get further information and/or support? ##
First, you should read the [user guide](EpcisUserGuide.md). If you can't find the information you are looking for you should check the mailing list archive and, as a last resort, you can post your question to the mailing list.

## How do I interact with the repository? ##
Please consult the [user guide](EpcisUserGuide.md).

## How do I build the project? ##
Please consult the [developer guide](EpcisDeveloperGuide.md).

## How can I get authentication and authorization in order to secure my repository? ##
You can easily add these features to an Fosstrak-based EPCIS installation by setting up an instance of the Apache Webserver as a proxy and using Apache's authentication mechanisms.

## How can I use Fosstrak with other database backends (e.g., PostgreSQL)? ##
The database backend officially supported by Fosstrak EPCIS is MySQL. However, Thomas Rudfoss wrote a very nice [tutorial](http://sourceforge.net/mailarchive/message.php?msg_id=23157984) on how to run Fosstrak EPCIS on top of PostgreSQL or Oracle. Stephen Tan also provided some great [hints](http://sourceforge.net/mailarchive/message.php?msg_id=21116989) on how to use Fosstrak EPCIS with Microsoft SQL Server.

# Fosstrak ALE #

## Can I start a graphical HAL Simulator from within Apache Tomcat? ##
Yes, you can. You need to modify the runtime permissions for Apache Tomcat

Option 1: Tomcat is started through the Service Manager (System tray)
  * Start the Service Manager
  * Select "Configure...".
  * Select tab "Log On".
  * Enable the checkbox "Allow service to interact with desktop".
  * Start Apache Tomcat.

Option 2: Tomcat is started through the command line
  * In the Windows system settings select the "Services" within "Administrative Control".
  * Select Apache Tomcat
  * Select tab "Log On".
  * Enable the checkbox "Allow service to interact with desktop".
  * Start Apache Tomcat.

## In Windows Vista the graphical HAL Simulator apears on a different desktop (with black background). ##
This is a caused by the security profile. Simply run tomcat as Administrator. (right mouse click on the executable and select "run as Administrator").

## Is this a reference implementation of the EPCglobal Reader Protocol Version 1.1? ##
No.

## Which RFID readers do you support? ##
We support out of the box any reader that support the EPCglobal LLRP protocol. This includes among others the Impinj Speedway reader, the Motorola FX7400, the Intermec IF61 and the open source Rifidi Tag Simulator. In addition, we also support the proprietary FEIG protocol (ID ISC.LRU1000/ID ISC.MR101-A).

## I have a reader, but Fosstrak does not support its proprietary protocol. ##
Implement your own HAL wrapper for your reader. As a benefit you will get all the filtering, event generation, messaging and tool support we already implemented for free.

## I don't have a reader, but I would like to check out the ALE middleware features anyway. ##
Use the Rifidi Tag Emulator and configure the Fosstrak middleware to communicate with the simulator via LLRP

## Can I contribute to this project even, if I am not part of the Auto-ID Labs or EPCglobal? ##
Yes. While initiated by the Auto-ID Lab at ETH Zurich/University St. Gallen and the Distributed Systems Group at ETH Zurich, it is open for participation by everyone.

## How can I contribute to this project? ##
There are a number of ways you can contribute. Contact us for details. We currently support only a limited number of RFID readers. If you implement a wrapper for your reader, we would like you to contribute your implementation to this effort.