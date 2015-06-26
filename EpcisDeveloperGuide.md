# EPCIS Developer Guide #



## About this Guide ##

This developer guide is intended for people who wish to work on Fosstrak's EPCIS repository implementation and contribute to the project. It outlines the steps to follow for setting up the development environment and helps developers to find their way through the code.

If you just want to set up your own EPCIS repository for use in your own project, you should follow the [User Guide](EpcisUserGuide.md). Generally, it is advisable if you first read the [User Guide](EpcisUserGuide.md) before continuing with the developer guide.

If you cannot find your answer here, feel free to ask the Fosstrak EPCIS mailing list.

## Getting Started ##

This section of the developer guide describes the first steps for getting started with the project. It includes a description on how to set up the development environment, how to obtain the sources, and how to build and deploy the application.

### Setting up the Development Environment ###

Make sure you have the following prerequisites installed:

  1. JDK version 5.0 or higher. Also check that the `JAVA_HOME` environment variable points to your Java bin directory.
  1. Apache Tomcat servlet container version 5.5 or higher. It will be used to deploy and run the EPCIS repository web application. Also check that the `CATALINA_HOME` environment variable points to your Tomcat installation directory.
  1. MySQL server version 5.0 or higher. It will be used by the EPCIS repository to store event data.
  1. Maven 2. It will be used for building and configuring the project and for deploying the application. Also check that the `M2_HOME` environment variable points to your Maven installation directory and make sure that you are familiar with the Maven build tool.

### Getting the Source Code ###

First thing you need to do is checking out the sourcecode from the SVN repository (see the [Download](EpcisDownload.md) section). In the following we will use the `EPCIS_HOME` variable to refer to the location where you checked out the sources.

### Setting up Eclipse ###

We recommend using the Eclipse IDE to work on the Fosstrak projects. Please follow these steps to set up the projects in Eclipse:

  * Add the classpath variable `M2_REPO` to Eclipse:
```
mvn eclipse:configure-workspace -Declipse.workspace=<path-to-your-eclipse-workspace>
```

  * In order to generate the `.project` and `.classpath` files used by Eclipse, run the following command from the `EPCIS_HOME` directory:
```
mvn eclipse:eclipse -DdownloadSources
```
> If you do not want to download all the sources, ignore the `-DdownloadSources` switch.

  * From inside Eclipse, import all the sub-modules: go to _File > Import > Existing Projects into Workspace_ and select all four projects available there.

  * In Eclipse, import the code formatter stylesheet: go to _Window > Preferences..._, navigate to the branch _Java > Code Style > Formatter_, and import the file from `EPCIS_HOME/src/config/epcis_codeformatter.xml`. Now either set the EPCIS code style profile active for your entire workspace, or go to the preferences of each individual EPCIS project (_Project > Properties_) and set the active code style profile to EPCIS. Prior to committing code to SVN hit `CTRL+SHIRT+F` to format the code according to the Fosstrak EPCIS code style.

### Building and Deploying the Project ###

The first time you work on the project you need to get all the dependencies and install them into your local Maven repository. The following command will do all that for you:

```
mvn install
```

Building the project is straight forward:

```
mvn compile
```

In order to deploy and undeploy the application to and from Tomcat, we use the tomcat-maven-plugin. This plugin communicates with the Tomcat Manager and thus needs to know the username and password to access the Manager application. Add the following block to your `M2_HOME/conf/settings.xml` file and specify the correct username and password:

```
<server>
  <id>tomcat-manager</id>
  <username>admin</username>
  <password>1234</password>
</server>
```

Now, you can use the following commands to deploy, undeploy, or redeploy the application. Make sure you run these commands from the `EPCIS_HOME/epcis-repository` directory because the maven-tomcat-plugin is only available to the epcis-repository sub-module.

```
mvn tomcat:deploy
mvn tomcat:undeploy
mvn tomcat:redeploy
```

Note: if you use Tomcat 7, then you need to adjust the URL to the manager application in the tomcat-maven-plugin configuration to match the required path for Tomcat 7. Just un-comment the `<url>` element in the corresponding plugin section in `EPCIS_HOME/epcis-repository/pom.xml`.

You can also manually deploy the application by placing the WAR file directly into Tomcat's webapps directory. Run the following command and grab the WAR file from `EPCIS_HOME/epcis-repository/target`

```
mvn package
```

Now you are ready to go and start working on the project.

## Project Layout ##

This section helps developers to find their way through the code.

### Project Modules ###

Fosstrak's EPCIS project is a multimodule project. Currently, it includes five modules which inherit from the base epcis-module:

  * **epcis-commons**
> > This module contains the common functionality (such as parsers) as well as the JAXB data binding of the EPCIS schema (which is used by the _epcis-repository_, the _epcis-queryclient_, and the _epcis-captureclient_ modules).

  * **epcis-captureclient**
> > This module consists of a capture client GUI and a simple capture client API library which can be used to send EPCIS events to an EPCIS repository.

  * **epcis-queryclient**
> > This module consists of a query client GUI, a query callback client, and a query control client API which can be used to send queries to an EPCIS repository.

  * **epcis-repository**
> > This module contains the actual implementation of the bindings defined in the EPCIS specification.

  * **epcis-interop-test**
> > This module contains tests to check interoperability compliance with the EPICS standard.

### Directory Layout ###

We use the standard Maven directory layout for all the modules:

<pre>
+- pom.xml                             -> top-level POM file<br>
+- src/<br>
|   +- config/                         -> top-level configuration files, e.g., checkstyle configs<br>
|   +- main/<br>
|   |   +- assembly/                   -> assembly descriptor for generating the source distribution<br>
|   |   `- filters/                    -> filter properties for the build profiles<br>
|   `- site/                           -> project documentation<br>
|<br>
+- epcis-captureclient/<br>
+- epcis-commons/<br>
+- epcis-queryclient/<br>
+- epcis-repository/<br>
+- pom.xml                         -> module POM file<br>
`- src/<br>
+- changes/                    -> documentation for all changes<br>
+- main/<br>
|   +- assembly/               -> assembly descriptor for the binary distribution<br>
|   +- java/                   -> Java source files<br>
|   +- resources/              -> class path resources, e.g., config files, hibernate mappings, WSDLs, XSDs<br>
|   +- sql/                    -> database scripts<br>
|   `- webapp/<br>
|       +- META-INF/           -> webapp context configurations<br>
|       `- static/             -> static web content, e.g., images, CSS<br>
|       `- WEB-INF/            -> deployment descriptor, JSPs<br>
+- site/                       -> module documentation<br>
`- test/                       -> Test resources and source files<br>
</pre>

## Other Features ##

### Build Profiles ###

An introduction to build profiles is given in the [Maven documentation](http://maven.apache.org/guides/introduction/introduction-to-profiles.html). We currently use two different build profiles, called _env-dev_ and _env-prod_. The build properties for the two profiles are given in the _dev.properties_ and _prod.properties_ file, respectively. They are located at `EPCIS_HOME/src/main/filters`.

By default the _env-prod_ profile is activated. A different profile can be activated on the command line by specifying a profile explicitly using the `-P` switch, or by providing a property named env with the corresponding value. The following examples show the two possibilities for building the project using the _env-dev_ profile:

```
mvn compile -Penv-dev
mvn compile -Denv=dev
```

A developer can activate the developement profile permanently by specifying an `activeProfile` element in his local `settings.xml` file. That way, he can also override properties from the development environment without touching the `dev.properties` file. The following is an example excerpt from the `settings.xml` file which shows how to permanently activate the _env-dev_ profile:

```
<profiles>
  <profile>
    <id>env-dev</id>
    <properties>
      <db.username>myname</db.username>
      <db.password>mysecret</db.password>
      <db.database>mydb</db.database>
    </properties>
  </profile>
</profiles>

<activeProfiles>
  <activeProfile>env-dev</activeProfile>
</activeProfiles>
```

### Generating the Project Website ###

_This section is deprecated, as the project is now hosted on Google Code._

The main web site (including this documentation) can be generated by using the following command:

```
mvn site -N
```

In order to generate all module reports and to test the whole site navigation before a live deploy, you can execute the following command which will generate the web site in the specified directory:

```
mvn site:stage -DstagingDirectory=d:\testsite
```