# User Guide: Server Installation and Configuration #

## Overview ##

In order to run the Fosstrak ALE Middleware, you need to deploy the filtering and collection server (fc-server) module in a servlet container, such as Apache Tomcat.

The individual steps include:
  1. Install a Java servlet container such as [Apache Tomcat](http://tomcat.apache.org/)
  1. Download the latest Fosstrak fc-server binary
  1. Deploy the latest Fosstrak fc-server module in the servlet container
  1. Configure the Fosstrak fc-server

## Install a Java Servlet Container ##

Information on how to download and install the Apache Tomcat servlet container are available here.

Any other Java Servlet Container should work as well.

## Download latest Fosstrak fc-server binary ##

The latest fc-server web application archive (war) is available from the Fosstrak ALE Middleware download section.

## Deploying the Fosstrak fc-server ##

Copy the war-file into the webapps-folder of your the java servlet container and start the server. The war-file will be automatically placed into a new folder.

Under Tomcat and windows you will usually find the webapps folder inside the tomcat installation directory (c:\Programme\Apache Tomcat \webapps). Under linux/unix this will depend on your distribution. Some possible locations:
  * /var/lib/tomcat/webapps
  * /usr/local/lib/tomcat/webapps

## Configuring the Fosstrak fc-server via the configuration files ##

This section will give a short overview of the configuration files available. These files allow you to configure the fc-server at start-up without the need to configure the fc-server via the ALE interface. For Tomcat, you will find these configuration files inside the folder _TOMCAT\_DIRECTORY_/webapps/fc-VERSION/WEB-INF/classes.

<pre>
example: /var/lib/tomcat/webapps/fc-server-1.0.0/WEB-INF/classes<br>
</pre>

### InputGenerators.properties ###
This propertie-file is the main configuration file for the Fosstrak ALE middleware. You will find it in the folder WEB-INF/classes.

The properties file has only a single parameter, namely the xml-file that configures the ALE logical reader API with the RFID readers available at startup.

Sample InputGenerators.properties:
<pre>
# you can specify your initial readers file here<br>
# eg<br>
readerAPI = /LogicalReaders.xml<br>
</pre>

### LogicalReaders.xml ###

This file specifies the readers that are loaded during startup of the Fosstrak ALE middleware.

Continue with the user guide to [Logical Reader Definitions](AleLogicalReaderDefinitions.md) which explains how to use this file to configure the Fosstrak fc-server.

Note: The Tomcat Server needs to be restarted for any changes to the configuration files to take effect.