# Capturing Application: Quick Start #



## Overview ##

This tutorial will guide you through an example scenario, how the Fosstrak Capturing Application can be used to link the Fosstrak ALE and the Fosstrak EPCIS repository by retrieving ECReports from the ALE and delivering EPCIS events to the EPCIS repository. The tutorial shows two different solutions to the scenario described below (A simple one and a more elaborate one).

## Scenario ##

We model a simple store equipped with three RFID readers. One reader (`Reader_GoodsReceiving`) in the backend, where incoming goods are registered by the staff. A second reader (`Reader_PointOfExit`) installed at the exit door that either warns about stolen goods or thanks the customer for the purchasing. The third reader (`Reader_PointOfSale`) is installed at the cash desk.

  * `Reader_GoodsReceiving`: Upon tag read: write a new entry into the EPCIS repository (new registered item).
  * `Reader_PointOfSale`: Upon tag read: check the EPCIS repository if the tag has already been sold. If not sold, create a new entry in the repository, flaging the tag as sold.
  * `Reader_PointOfExit`: Upon tag read: check the EPCIS repository if the tag has been sold. If not sold, raise an alarm.

## Prerequisites ##

  * a copy of the `fc-server-1.0.1.war` or newer
  * a copy of the `fc-webclient fc-webclient-1.0.1.war` or newer
  * a copy of the `epcis-repository-0.4.2.war` or newer
  * a copy of the `hal-simulator`. Select `hal-impl-sim-0.5.0-bin-with-dependencies.[zip|tar.gz]` or newer
  * Configuration files:
    * [Reader\_GoodsReceiving.xml](http://fosstrak.googlecode.com/svn/capturingapp/trunk/src/site/resources/files/quickstart/Reader_GoodsReceiving.xml) (both scenarios)
    * [Reader\_PointOfExit.xml](http://fosstrak.googlecode.com/svn/capturingapp/trunk/src/site/resources/files/quickstart/Reader_PointOfExit.xml) (both scenarios)
    * [Reader\_PointOfSale.xml](http://fosstrak.googlecode.com/svn/capturingapp/trunk/src/site/resources/files/quickstart/Reader_PointOfSale.xml) (both scenarios)
    * [SimulatorClient.xml](http://fosstrak.googlecode.com/svn/capturingapp/trunk/src/site/resources/files/quickstart/SimulatorClient.xml) (both scenarios)
    * [SimulatorController.xml](http://fosstrak.googlecode.com/svn/capturingapp/trunk/src/site/resources/files/quickstart/SimulatorController.xml) (both scenarios)
    * [EventCycle\_GoodsReceiving.xml](http://fosstrak.googlecode.com/svn/capturingapp/trunk/src/site/resources/files/quickstart/EventCycle_GoodsReceiving.xml) (simple scenario)
    * [EventCycle\_PointOfExit.xml](http://fosstrak.googlecode.com/svn/capturingapp/trunk/src/site/resources/files/quickstart/EventCycle_PointOfExit.xml) (simple scenario)
    * [EventCycle\_PointOfSale.xml](http://fosstrak.googlecode.com/svn/capturingapp/trunk/src/site/resources/files/quickstart/EventCycle_PointOfSale.xml) (simple scenario)
    * [fosstrakDemo.xml](http://fosstrak.googlecode.com/svn/capturingapp/trunk/src/site/resources/files/quickstart/fosstrakDemo.xml) (elaborate scenario)
  * [Apache Tomcat](http://tomcat.apache.org/) Java Servlet Container

## Preparations ##

The preparations need to be performed for both solutions.

### Start the multi reader simulator ###

We use a simulator to generate the tag input for the Filtering & Collection. The multi reader simulator is capable of simulating several readers in one application, allowing you to move tags from one reader to another.

First unpack `hal-impl-sim-VERSION-bin-with-dependencies` to a folder of your choice (example `c:\fosstrakDemo`).

Open a command line and run the simulator.

```
cd c:\fosstrakDemo
java -cp hal-impl-sim-0.5.0\hal-impl-sim-0.5.0.jar org.fosstrak.hal.impl.sim.multi.SimulatorServerController
```

If everything runs fine, an empty window should pop up.

![http://fosstrak.googlecode.com/svn/capturingapp/trunk/src/site/resources/files/quickstart/simulatorController.png](http://fosstrak.googlecode.com/svn/capturingapp/trunk/src/site/resources/files/quickstart/simulatorController.png)

### Prepare EPCIS repository ###

If you already have a running instance of an EPCIS repository, you can skip this step. Otherwise follow the [installation instructions](EpcisUserGuide.md) of the Fosstrak EPCIS repository.

### Prepare the Filtering & Collection ###

Deploy the Fosstrak Filtering & Collection to your Tomcat `webapps` folder (by simply copying the WAR file into the folder). We need access to the configuration files packed into the WAR file, so unpack the war-file into the `webapps` folder.

Copy the two configuration files `SimulatorClient.xml` and `SimulatorController.xml` into the folder `webapps\fc-server-VERSION\WEB-INF\classes\props` (overwrite the existing copies).

We recommend to adjust the log level for the Filtering & Collection from either `DEBUG` or `INFO` to `ERROR`. To do so, open the file `webapps\fc-server-VERSION\WEB-INF\classes\log4j.properties` and adjust the log level:

```
file: <TOMCAT_FOLDER>\webapps\fc-server-VERSION\WEB-INF\classes\log4j.properties

# class specific levels
log4j.logger.org.fosstrak = ERROR
```

## Simple Solution ##

This solution sets up an `EventCycle` for each reader. This allows the capturing application to distinguish between different readers by the name of the `EventCycle`.

Please ensure, that you followed the setup in Preparations.

Copy the fc-webclient and the capturing-app WAR files into the Tomcat `webapps` folder und unpack the capturing-app file (We need to adjust a few configuration parameters).

### Adjust the configuration ###

  1. First, we define a new capturing application that is listening on port 9999 for incoming ECReports, and that is delivering EPCIS events to the EPCIS repository at `http://localhost:8080/epcis-repository-0.4.2/capture`. Open the file `webapps/capturing-app-VERSION/WEB-INF/classes/captureapplication.properties` and set the configuration below:
```
file: <TOMCAT_FOLDER>/webapps/capturing-app-VERSION/WEB-INF/classes/captureapplication.properties

n=1

cap.0.port=9999
cap.0.name=myFirstCaptureApp
cap.0.epcis=http://localhost:8080/epcis-repository-0.4.2/capture
```
  1. Second, we instruct the capturing application to use the simple solution for the scenario. Open the file `webapps/capturing-app-VERSION/WEB-INF/classes/changeset.xml` and set the configuration below:
```
file: <TOMCAT_FOLDER>/webapps/capturing-app-VERSION/WEB-INF/classes/changeset.xml

<change-set xmlns='http://drools.org/drools-5.0/change-set'
            xmlns:xs='http://www.w3.org/2001/XMLSchema-instance'
            xs:schemaLocation='http://drools.org/drools-5.0/change-set.xsd' >
   <add>
      <resource source='classpath:drools/SimpleWareHouse-3EventCycles.drl' type='DRL' />
   </add>
</change-set>
```
  1. Start Tomcat now.

### Setup the readers ###

  1. Start your web browser and point it to the address of the web-based client.
```
http://<SERVER>:<PORT>/<WEBCLIENT_VERSION>/services/ALEWebClient.jsp

example:
http://localhost:8080/fc-webclient-1.0.1/services/ALEWebClient.jsp
```
  1. Set the endpoint to the filtering and collection server's Logical Reader API by selecting `setEndPoint(String endPointName)` in the LogicalReader API. Click "Invoke" to execute the command.
```
endpoint: http://<SERVER>:<PORT>/<FCSERVER_VERSION>/services/ALELRService

example: 
http://localhost:8080/fc-server-1.0.1/services/ALELRService
```
  1. Verify that a connection between the web-based client and the server can be established by clicking `getVendorVersion()`. A version number should be returned.
  1. The next step is to establish a connection from the Filtering & Collection to the reader simulator (basically this means to setup three different readers).
    1. Click on `define(String readerName, LRSpec spec)` in the section LogicalReader API. Do not confuse the `define` method for an EventCycle with the `define` method for a logical reader! Define three readers. Make sure to set the reader names exactly as they are written below.
      1. `Reader_GoodsReceiving` with [Reader\_GoodsReceiving.xml](http://fosstrak.googlecode.com/svn/capturingapp/trunk/src/site/resources/files/quickstart/Reader_GoodsReceiving.xml)
      1. `Reader_PointOfSale` with [Reader\_PointOfSale.xml](http://fosstrak.googlecode.com/svn/capturingapp/trunk/src/site/resources/files/quickstart/Reader_PointOfSale.xml)
      1. `Reader_PointOfExit` with [Reader\_PointOfExit.xml](http://fosstrak.googlecode.com/svn/capturingapp/trunk/src/site/resources/files/quickstart/Reader_PointOfExit.xml)
```
Example:

readerName: Reader_PointOfSale
specFilePath: c:\fosstrakDemo\Reader_PointOfSale.xml
```
    1. Retrieve the logical reader names to check if readers are there by clicking `getLogicalReaderNames()`. The result should be `[Reader_PointOfSale, Reader_PointOfExit, Reader_GoodsReceiving]`.
  1. Now switch to the multi reader simulator and refresh it to display the readers (click "view" and then "refresh").

### Setup the EventCycles ###

  1. Start your web browser and point it to the address of the web-based client.
```
http://<SERVER>:<PORT>/<WEBCLIENT_VERSION>/services/ALEWebClient.jsp

example:
http://localhost:8080/fc-webclient-1.0.1/services/ALEWebClient.jsp
```
  1. Set the endpoint to the filtering and collection server by selecting the `setEndpoint(String endPointName)` method in the Filtering and Collection API. Click "Invoke" to execute the command.
```
endpoint: http://<SERVER>:<PORT>/<FCSERVER_VERSION>/services/ALEService

example: 
http://localhost:8080/fc-server-1.0.1/services/ALEService
```
  1. Verify that a connection between the web-based client and the server can be established by clicking `getVendorVersion()`. A version number should be returned.
  1. In the next step you will define three ALE ECSpecs. These tell the ALE Middleware how the RFID tag reads arriving from the simulator should be filtered and aggregated.
    1. Invoke the method `define(String specName, String specFilePath)`. Do not confuse the `define` method for an EventCycle with the `define` method for a logical reader. For this scenario we need to create three EventCycles. Make sure to set the names of the EventCycles exactly as they are written below:
      1. `EventCycle_GoodsReceiving` with spec [EventCycle\_GoodsReceiving.xml](http://fosstrak.googlecode.com/svn/capturingapp/trunk/src/site/resources/files/quickstart/EventCycle_GoodsReceiving.xml)
      1. `EventCycle_PointOfExit` with spec [EventCycle\_PointOfExit.xml](http://fosstrak.googlecode.com/svn/capturingapp/trunk/src/site/resources/files/quickstart/EventCycle_PointOfExit.xml)
      1. `EventCycle_PointOfSale` with spec [EventCycle\_PointOfSale.xml](http://fosstrak.googlecode.com/svn/capturingapp/trunk/src/site/resources/files/quickstart/EventCycle_PointOfSale.xml)
```
Example:

specName: EventCycle_PointOfExit
specFilePath: c:\fosstrakDemo\EventCycle_PointOfExit.xml
```
    1. Retrieve the EventCycle names to check if they are created correctly by clicking `getECSpecNames()`. The result should be: `[EventCycle_PointOfSale, EventCycle_PointOfExit, EventCycle_GoodsReceiving]`.

### Deliver ECReports to the capturing application ###

Invoke `subscribe(String specName, String notificationUri)`. For each of the three EventCycles we just created, invoke the registration with the target URL `http://localhost:9999`.

```
Example:

notificationURI: http://localhost:9999
specName: EventCycle_GoodsReceiving
```

### Watch the result ###

Now all the applications should be linked together correctly. Switch back to the multi reader simulator and make sure, that you can look at the log-output of Tomcat (the capturing application will print the scenario output to the commandline provided by Tomcat).

Create a few tags (please use the tags from the list below, as they can be translated by the ALE to a meaningful representation).

Tags:
  * 3078E7D4141ADC7C66FB10A5
  * 30842E5663E88618294179E9
  * 3025979BA9244C71AF8A92B6
  * 30E85865E0CB78EA767BBBEC
  * 307AD9A00C520D2585913539
  * 305887668C7F5160AA3CB66F

Move one tag on the reader `Reader_GoodsReceiving` and wait a few seconds. On the commandline a message will be printed informing you about newly registered items.

```
example:

=====================================================
registering new items:
urn:epc:raw:96:15001446348594317971238359205
=====================================================
```

Move the tag on the reader `Reader_PointOfExit` and wait a few seconds. On the commandline alert messages will be printed, as the goods have taken to the exit without being payed for.

```
example:

=====================================================
!!!!!!!!!!!!! FOUND STOLEN GOODS!!!!!!!!!!
urn:epc:raw:96:15001446348594317971238359205
=====================================================
```

Move the tag on the reader `Reader_PointOfSale` and wait a few seconds. On the commandline a message will inform you about a customer having purchased some goods.

```
example:

=====================================================
customer purchased items:
urn:epc:raw:96:15001446348594317971238359205
=====================================================
```

Move the tag back on the reader `Reader_PointOfExit` and a friendly message will be sent to you.

```
example:

=====================================================
Dear customer, thank you for your purchasing. Goodbye
=====================================================
```

### Clean the entries in the EPCIS repository ###

If you want to run the demo with the same EPCs again, you need to clean out all the entries in the EPCIS repository. A clean and dirty way, how you can achieve this, is via the mysql command line.

With the MySQL-Commandline client, connect to the MySQL server. Switch into the EPCIS database `use epcis;`. Then delete all the events `delete from event_objectevent;`.

## Elaborate Solution ##

This solution sets up one EventCycle retrieving tags from all the readers. Aside the tag information (EPC), the EventCycle delivers also tag stats (with the name of the reader that read the EPC). With this information, the capturing application can determine the name of the reader delivering the EPC.

Please ensure, that you followed the setup in Preparations.

Copy the fc-webclient and the capturing-app WAR files into the Tomcat `webapps` folder und unpack the capturing-app file (We need to adjust a few configuration parameters).

### Adjust the configuration ###

  1. First, we define a new capturing application that is listening on port 9999 for incoming ECReports, and that is delivering EPCIS events to the EPCIS repository at `http://localhost:8080/epcis-repository-0.4.2/capture`. Open the file `webapps/capturing-app-VERSION/WEB-INF/classes/captureapplication.properties` and set the configuration below:
```
file: <TOMCAT_FOLDER>/webapps/capturing-app-VERSION/WEB-INF/classes/captureapplication.properties

n=1

cap.0.port=9999
cap.0.name=myFirstCaptureApp
cap.0.epcis=http://localhost:8080/epcis-repository-0.4.2/capture
```
  1. Second, we instruct the capturing application to use the elaborate solution for the scenario. Open the file `webapps/capturing-app-VERSION/WEB-INF/classes/changeset.xml` and set the configuration below:
```
file: <TOMCAT_FOLDER>/webapps/capturing-app-VERSION/WEB-INF/classes/changeset.xml

<change-set xmlns='http://drools.org/drools-5.0/change-set'
            xmlns:xs='http://www.w3.org/2001/XMLSchema-instance'
            xs:schemaLocation='http://drools.org/drools-5.0/change-set.xsd' >
   <add>
      <resource source='classpath:drools/SimpleWareHouse-1EventCycle.drl' type='DRL' />
   </add>
</change-set>
```
  1. Start Tomcat now.

### Setup the readers ###

  1. Start your web browser and point it to the address of the web-based client.
```
http://<SERVER>:<PORT>/<WEBCLIENT_VERSION>/services/ALEWebClient.jsp

example:
http://localhost:8080/fc-webclient-1.0.1/services/ALEWebClient.jsp
```
  1. Set the endpoint to the filtering and collection server's Logical Reader API by selecting `setEndPoint(String endPointName)` in the LogicalReader API. Click Invoke to execute the command.
```
endpoint: http://<SERVER>:<PORT>/<FCSERVER_VERSION>/services/ALELRService

example: 
http://localhost:8080/fc-server-1.0.1/services/ALELRService
```
  1. Verify that a connection between the web-based client and the server can be established by clicking `getVendorVersion()`. A version number should be returned.
  1. The next step is to establish a connection from the Filtering & Collection to the reader simulator (basically this means to setup three different readers).
    1. Click on `define(String readerName, LRSpec spec)` in the section LogicalReader API. Do not confuse the `define` method for an EventCycle with the `define` method for a logical reader.
    1. Define three readers. Make sure to set the reader names exactly as they are written below.
      1. `Reader_GoodsReceiving` with [Reader\_GoodsReceiving.xml](http://fosstrak.googlecode.com/svn/capturingapp/trunk/src/site/resources/files/quickstart/Reader_GoodsReceiving.xml)
      1. `Reader_PointOfSale` with [Reader\_PointOfSale.xml](http://fosstrak.googlecode.com/svn/capturingapp/trunk/src/site/resources/files/quickstart/Reader_PointOfSale.xml)
      1. `Reader_PointOfExit` with [Reader\_PointOfExit.xml](http://fosstrak.googlecode.com/svn/capturingapp/trunk/src/site/resources/files/quickstart/Reader_PointOfExit.xml)
```
Example:

readerName: Reader_PointOfSale
specFilePath: c:\fosstrakDemo\Reader_PointOfSale.xml
```
    1. Retrieve the logical reader names to check if readers are there by clicking `getLogicalReaderNames()`. The result should be `[Reader_PointOfSale, Reader_PointOfExit, Reader_GoodsReceiving]`.
  1. Now switch to the multi reader simulator and refresh it to to display the readers (click "view" and then "refresh").

### Setup the EventCycle ###

  1. Start your web browser and point it to the address of the web-based client.
```
http://<SERVER>:<PORT>/<WEBCLIENT_VERSION>/services/ALEWebClient.jsp

example:
http://localhost:8080/fc-webclient-1.0.1/services/ALEWebClient.jsp
```
  1. Set the endpoint to the filtering and collection server by selecting the `setEndpoint(String endPointName)` method in the Filtering and Collection API. Click "Invoke" to execute the command.
```
endpoint: http://<SERVER>:<PORT>/<FCSERVER_VERSION>/services/ALEService

example: 
http://localhost:8080/fc-server-1.0.1/services/ALEService
```
  1. Verify that a connection between the web-based client and the server can be established by clicking `getVendorVersion()`. A version number should be returned.
  1. In the next step you will define one ALE ECSpec. This one tells the ALE Middleware how the RFID tag reads arriving from the simulator should be filtered and aggregated.
    1. Invoke the method `define(String specName, String specFilePath)`. Do not confuse the `define` method for an EventCycle with the `define` method for a logical reader.
    1. For this scenario we need to create one EventCycle. Make sure to set the name of the EventCycle exactly as it's written below:
      1. `fosstrakDemo` with spec [fosstrakDemo.xml](http://fosstrak.googlecode.com/svn/capturingapp/trunk/src/site/resources/files/quickstart/fosstrakDemo.xml)
```
Example:

specName: fosstrakDemo
specFilePath: c:\fosstrakDemo\fosstrakDemo.xml
```
    1. Retrieve the EventCycle names to check if they are created correctly by clicking `getECSpecNames()`. The result should be: `[fosstrakDemo]`.

### Deliver ECReports to the capturing application ###

Invoke `subscribe(String specName, String notificationUri)`. Subscribe the capturing application to the results of the event cycle.

```
Example:

notificationURI: http://localhost:9999
specName: fosstrakDemo
```

### Watch the result ###

Now all the applications should be linked together correctly. Switch back to the multi reader simulator and make sure, that you can look at the log-output of Tomcat (the capturing application will print the scenario output to the commandline provided by Tomcat).

Create a few tags (please use the tags from the list below, as they can be translated by the ALE to a meaningful representation).

Tags:
  * 3078E7D4141ADC7C66FB10A5
  * 30842E5663E88618294179E9
  * 3025979BA9244C71AF8A92B6
  * 30E85865E0CB78EA767BBBEC
  * 307AD9A00C520D2585913539
  * 305887668C7F5160AA3CB66F

Move one tag on the reader `Reader_GoodsReceiving` and wait a few seconds. On the commandline a message will be printed informing you about newly registered items.

```
example:

=====================================================
registering new items:
urn:epc:raw:96:15001446348594317971238359205
=====================================================
```

Move the tag on the reader `Reader_PointOfExit` and wait a few seconds. On the commandline alert messages will be printed, as the goods have taken to the exit without being payed for.

```
example:

=====================================================
!!!!!!!!!!!!! FOUND STOLEN GOODS!!!!!!!!!!
urn:epc:raw:96:15001446348594317971238359205
=====================================================
```

Move the tag on the reader `Reader_PointOfSale` and wait a few seconds. On the commandline a message will inform you about a customer having purchased some goods.

```
example:

=====================================================
customer purchased items:
urn:epc:raw:96:15001446348594317971238359205
=====================================================
```

Move the tag back on the reader `Reader_PointOfExit` and a friendly message will be sent to you.

```
example:

=====================================================
Dear customer, thank you for your purchasing. Goodbye
=====================================================
```

### Clean the entries in the EPCIS repository ###

If you want to run the demo with the same EPCs again, you need to clean out all the entries in the EPCIS repository. A clean and dirty way, how you can achieve this, is via the mysql command line.

With the MySQL command line client, connect to the MySQL server. Switch into the EPCIS database `use epcis;`. Then delete all the events `delete from event_objectevent;`.