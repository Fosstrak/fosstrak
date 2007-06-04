Accada Reader
=============

The objective of the Accada Reader Module is to provide an implementation
of the EPCglobal Reader Protocol 1.1 (RP) and Reader Management Specification 1.0 (RM).
The elements of the protocol implemented include:
- Transport Binding: TCP and HTTP
- Message Binding: XML and Text
- Synchronous and Asynchronous Messaging (Notification Channels)
- Triggers
- Data Selectors
- SNMP Binding of RM 1.0

Existing non-RP/RM RFID readers can be integrated into the Accada Reader using the 
Accada Reader Hardware Abstraction Layer (see Accada Reader HAL module).

How to use the Accada Reader
============================

- Download the reader binaries with the dependencies included from the download section of the website.
- Unzip the downloaded file
- Make sure a JRE 1.5 or higher is installed and it is added to the path variable.
- Start the reader instance by invoking the executable jar 

The reader is now listening for incoming http requests on port 8000, incoming tcp requests on port 5566, and incoming snmp requests on port 161. To communicate with the reader instance, you might want to consider using the Accada Reader Proxy and Accada Reader Client or a SNMP Manager such as MIB Explorer. You might also want to consider unpacking the jar and adjusting the settings in the ReaderDevice.properties file.

For more information,  please see http://www.accada.org/reader/reader-rprm-core/
