
Overview Accada Reader 
=================

This directory contains a demo of the Accada Reader Project. The objective of the Accada Reader Project is to implement the reader role in the EPC Network and to develop the appropriate tools that facilitate communication with the reader instance.

This version of the Accada Reader implements the EPCglobal Reader Protocol and Reader Management Specification and uses a graphical user interface to simulate tag reads and errors.

The Accada Reader is part of the Accada Open Source RFID prototyping platform that implements the EPC Network specifications. At www.accada.org, there is also software available that implements the ALE, TDT, and EPCIS specification of EPCglobal.


Installing and Running the Accada Reader Simulator 
==================

The following instructions show how to install and run the Accada Reader Simulator:

1) Make sure there is a Java Runtime Environemnt (JRE) 1.5 installed on your computer and added to the path variable. Otherwise download JRE1.5 from http://java.sun.com/javase/downloads/index_jdk5.jsp

2) Click on the Accada-Reader.jar icon in the Accada-Reader folder. This starts the Accada Reader Simulator. A window should pop up, which shows pictures of a reader and four antennas.

The reader is also listening for incoming http requests on port 8000 and incoming tcp requests on port 5566. The reader also listens to incoming SNMP requests on port 161. 

3) Click on the Accada-DemoApp.jar icon in the Accada-DemoApp folder. This starts a demo application that connects to the  virtual reader started in the previous step. Note that the connection procedure takes as much as 15 seconds and the window will only appear, once the connection is established.

4) You can now go back to the simulator user interface, generate a tag by right clicking, and then drag-and-drop this tag across antennas. The tag movements are then communicated via the RP to the DemoApp which you started in step 3. 

To evaluate the reader management functionality, you will need to use a SNMP manager tool such as MIB Explorer.

For more information, on how to use the Accada Reader and how to configure the reader instance, please see http://www.accada.org/reader. There is also a flash demo in the "Info about Accada" folder.

