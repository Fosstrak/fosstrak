# Background #

## Founders ##

The Fosstrak project was initiated by Christian Floerkemeier, Matthias Lampe and Christof Roduner of the Distributed Systems Group at ETH Zurich led by Friedemann Mattern and the Auto-ID Lab at ETH Zurich/University of St. Gallen led by Elgar Fleisch .

![http://fosstrak.googlecode.com/svn/wikires/ethautoid.gif](http://fosstrak.googlecode.com/svn/wikires/ethautoid.gif)

## History ##

At the Auto-ID Lab and in the Distributed Systems Group at ETH Zurich, we started working on the EPC Network and RFID middleware as early as 2001 through our involvement with the Auto-ID Center, a research program headquartered at MIT to foster the adoption of RFID (more information about the Auto-ID Center). Our early contributions include for example the PML Core language, a mark-up language for Auto-ID events, which was published by EPCglobal in 2003, and the RFIDStack, an RFID middleware that abstracts from the idiosycracies of different reader devices.

In 2005, we decided to make the software packages that we developed over the years to abstract from proprietary reader interfaces, to filter and aggregate RFID data and to interpret the RFID data in a given context publicly available to the RFID community. To foster interoperability, we decided to wrap our existing implementations using the interfaces specified by the EPCglobal working groups, e.g., the reader protocol and EPCIS specification. The project was initially called **Accada**, but we changed the name to **Fosstrak** after a trademark dispute.

## Motivation ##

We believe that the availability of openly available software for an RFID infrastructure will benefit the entire community and accelerate the development of an "Internet of Things":
  * EPC Network Novices can use the software and the simple demos we provide to gain hands-on experience with the EPC Network.
  * RFID Application Developers will benefit from the high-level abstractions available in Fosstrak that hide the low-level message and transport bindings. This will simplify the communication with EPC Network components and thus foster the use of the EPC Network.
  * RFID System Integrators can deploy and configure the Fosstrak implementations to their needs.
  * Researchers and students working on RFID do not have to implement the software components themselves, but can rely on existing modules which they can modify to evaluate novel concepts and algorithms.