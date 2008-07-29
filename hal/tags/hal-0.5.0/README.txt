
Fosstrak Hardware Abstraction Layer (HAL) Project
===============================================

This directory contains the modules of the Fosstrak HAL project. The objective of the Fosstrak HAL is to provide support for the integration of physical RFID readers into the Fosstrak Reader and Fosstrak Filtering and Collection module. It provides an interface and the appropriate implementations to communicate with reader devices that do not implement any of the standardized interfaces. Moreover, a simulator framework is provides that facilitates the development of different reader simulators/emulators.

The Fosstrak Reader is part of the Fosstrak Open Source RFID prototyping platform that implements the EPC Network specifications. At www.fosstrak.org, there is also software available that implements the ALE, TDT, and EPCIS specification of EPCglobal.

For more information,  please see the README.txt in the subdirectories of the modules and the information at http://www.fosstrak.org/hal/


Development using Eclipse
=========================

In order to work on the Fosstrak HAL module using Eclipse, please follow these instructions:


1. Eclipse needs to know the path to the local maven repository. Execute
   the following command to automatically set up the corresponding
   classpath variable (M2_REPO) accordingly:

      mvn -Declipse.workspace=<path-to-eclipse-workspace> eclipse:add-maven-repo


2. In the "hal" directory, run the following command:

      mvn eclipse:eclipse


3. Import the different hal modules hal-commons, hal-impl-sim and the different
   reader implementations such as hal-impl-impinj in Eclipse (from the menu bar,
   select File > Import > Existing Projects into Workspace).