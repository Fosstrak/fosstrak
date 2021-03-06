
Accada Reader Project
=====================

This directory contains the modules of the Accada Reader project. The objective of the Accada Reader project is to implement the reader role in the EPC Network and to develop the appropriate tools that facilitate communication with the reader instance.

This version of the Accada Reader implements the EPCglobal Reader Protocol and Reader Management Specification and uses a graphical user interface to simulate tag reads and errors. Moreover, it provides a visual reader simulator and a Java Swing-based test client.

The Accada Reader is part of the Accada Open Source RFID prototyping platform that implements the EPC Network specifications. At www.accada.org, there is also software available that implements the ALE, TDT, and EPCIS specification of EPCglobal.

For more information,  please see the README.txt in the subdirectories of the modules and the information at http://www.accada.org/reader/



Development using Eclipse
=========================

In order to work on the Accada Reader module using Eclipse, please follow these instructions:


1. Eclipse needs to know the path to the local maven repository. Execute
   the following command to automatically set up the corresponding
   classpath variable (M2_REPO) accordingly:

      mvn -Declipse.workspace=<path-to-eclipse-workspace> eclipse:add-maven-repo


2. In the "reader" directory, run the following command:

      mvn eclipse:eclipse


3. Import the four modules reader-hal, reader-rp-client, reader-rp-proxy, reader-rprm-core
   in Eclipse (from the menu bar, select File > Import > Existing Projects into Workspace).
