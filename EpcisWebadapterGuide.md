# EPCIS Webadapter Guide #



## About this Guide ##

This guide is intended to help users getting started with the Fosstrak EPCIS Webadapter module. The EPCIS Webadapter adds a RESTful, fully Web-enabled, API to a standard EPCIS repository. While the EPCIS Webadapter can be deployed on top of any standard compliant EPCIS Repository, this guide outlines the steps to follow in order to set up the EPCIS Webadapter to work with the Fosstrak EPCIS Repository.

**Please note that the EPCIS Webadapter is not an EPCglobal standardized interface and currently is still a beta Fosstrak module.**

As the project is part of an [ongoing research project](http://www.webofthings.com/rfid), your feedback is very valuable to us! We created a questionnaire where we try to better understand your experience and where you can ask us for new features or improvements, thanks a lot for your answers: [Feedback form](https://spreadsheets.google.com/viewform?formkey=dGplWlFEc3pwajNJVldnUjZTZzh3ZkE6MA). If this guide does not answer the questions you may have, feel free to post them to the mailing list.

The project was started in frame of the Web of Things initiative at ETH Zurich. It is a collaboration between the [Distributed Systems Lab of ETH](http://www.vs.inf.ethz.ch/), the [Auto-ID Labs (MIT and Zurich)](http://www.autoidlabs.org/), the [Software Engineering Group of the University of Fribourg](http://diuf.unifr.ch/drupal/softeng/) and [SAP Research Zurich](http://www.sap.com/about/company/research/centers/zurich.epx).

## Introduction to the EPCIS Webadapter ##

This module offers an additional, RESTful, integration interface, or API (Application Programming Interface) for the EPCIS upon which a different range of applications can be built thanks to a lightweight and Web-enabled RESTful architecture. It offers access to all standard EPCIS functionality through a native Web interface.

Using the EPCIS Webadapter:

  * Each query, tagged object, location or RFID reader gets a unique URL that can be linked to, exchanged in emails, browsed for, bookmarked, etc. (on the Web or in an Intranet).
  * You can use Web languages like HTML and JavaScript to directly use RFID data.
  * You create light-weight applications that are using RFID data on resource-limited environements such as mobile phones, wireless sensor nodes or Web mashups.

### Features ###

The WS-**type of integration architecture is well adapted to combine business applications. For example, it can be used to integrate EPCIS data about the status of a shipment with an ERP (Enterprise Resource Planning) application.**

However, WS-**applications are complex systems with high entry barriers and require developer expertise in the domain. Hence, they are not well adapted for more light-weight and ad-hoc application scenarios. Furthermore, the WS-** protocols are known to be rather verbose. Moreover, they do not fully meet the requirements of resource-constrained devices such as mobile phones and wireless sensor/actuator networks often not providing WS-**server or even client stacks.**

The EPCIS Webadapter is a optional plugin that that offers an additional RESTful interface on top of any standard-compliant EPCIS. Using this approach, each query, tagged object, location or RFID reader gets a unique URL that can be linked to, exchanged in emails, browsed for, bookmarked, and consumed from any scripting or programming language understanding the HTTP protocol.

The EPCIS Webadapter has three main goals:

  * lowering the entry barrier for developers and foster rapid prototyping by bringing the EPCIS data closer to Web languages such as Javascript or Python.
  * allowing users to directly access, share and bookmark EPCIS queries and their results through any standard Web-browser, without the need to install any additional software. With this module, every resource of the EPC network gets a unique URL and subscription to queries are delivered as Web Feeds (using the Atom standard).
  * Offering a more light-weight access to the data. This enables creating applications in which the EPCIS data are directly consumed by resource-constrained devices (e.g., Wireless sensor networks, mobile phones, etc.) without requiring proxy or translation gateways.

### Architecture ###

The module basically translates the incoming RESTful request into SOAP requests on the original EPCIS. It further takes care of reformatting the results into several Web formats such as HTML, JSON, Atom and XML. The plugin is based on [Jersey](http://jersey.dev.java.net/), an implementation of the Java Standard for RESTful Web Services (JAX-RS). For generating Atom feeds for query subscriptions, it uses the [Apache Abdera](http://abdera.apache.org/) framework.

![http://fosstrak.googlecode.com/svn/wikires/epcis/webadapter_architecture.png](http://fosstrak.googlecode.com/svn/wikires/epcis/webadapter_architecture.png)

More information about the architecture of the project can be found in [this paper](http://www.inf.ethz.ch/personal/dguinard/publications/bibtex.html?file=/home/webvs/www/htdocs/publ/papers/dguinard-giving-2010).

## Online Demonstration ##

The EPCIS Webadapter and some of the applications built upon can be directly tested from our [demo server](http://www.fosstrak.org/epcis/demo.html).

## Installing the EPCIS Webadapter ##

This section includes a step-by-step tutorial describing how to set up the EPCIS Webadapter to work with the Fosstrak EPCIS Repository.

### Setting up an EPCIS Repository ###

The EPCIS Webadapter can be deployed on top of any standard compliant EPCIS Repository. To install the Fosstrak EPCIS Repository please refer to EPCIS Repository User Guide. To go on with this installation guide, all you need is the root URL of an EPCIS Repository (e.g., `http://localhost:8080/epcis-repository-0.4.2`).

### Deploying the EPCIS Webadapter ###

The EPCIS Webadapter can be deployed in any Java compliant Web/Application server that supports the deployment of JAX-RS based applications. It was successfully tested with Tomcat (6.x) and the Glassfish (3.x) Application Server.

To deploy it on Tomcat simply copy the `EPCIS Webadapter-VERSION_NUMBER.war` archive to `CATALINA_BASE/webapps` folder of Tomcat which will auto-deploy the application. Alternatively you can also use the Tomcat manager Webapp and upload the `EPCIS Webadapter-VERSION_NUMBER.war` archive there.

If the deployment was successful you should be able to browse to `http://HOST:PORT/epcis-webadapter-VERSION_NUMBER` and see a Web-page there. From there click on the "EPCIS Webadapter" link. Since this triggers the initial configuration phase of the EPCIS Webadapter (creation of the internal databased, etc.), this might take a while. Please do not interrupt this phase.

### Configuring the EPCIS Webadapter ###

The EPCIS Webadapter is configured to automatically connect to a local EPCIS instance at `http://localhost:8080/epcis-repository-0.4.2`. To change the instance it connects to go to:

```
http://HOST:PORT/epcis-webadapter-VERSION_NUMBER/rest/API_VERSION/config/epcisurl
```

There, you can specify a new instance of an EPCIS repository the Webadapter should connect to. Any standard-compliant EPCIS repository should work. However, the EPCIS Webadapter was tested on the Fosstrak EPCIS Repository.

The EPCIS Webadapter also offers clients to register to queries for which updates are delivered to standard Atom feeds. This enables clients to register to EPC queries without requiring them to run an HTTP server. The EPCIS Webadapter embeds an Atom server (based on [Apache Abdera](http://abdera.apache.org/)). However, the Webadapter can be configured to publish the queries updates to any AtomPub compliant server.

To change the AtomPub server it pushes updates to, go to:

```
http://HOST:PORT/epcis-webadapter-VERSION_NUMBER/rest/API_VERSION/config/feedurl
```

and provide a valid URL to an AtomPub server endpoint.

## Using the EPCIS Webadapter ##

The EPCIS Webadapter adds a RESTful, fully Web-enabled, API to a standard EPCIS repository. If you are not familiar with REST, we invite you to read [this paper](http://www.inf.ethz.ch/personal/dguinard/publications/bibtex.html?file=/home/webvs/www/htdocs/publ/papers/dguinard-giving-2010), which briefly presents REST in the context of the EPCIS Webadapter.

Using this approach, each query, tagged object, location or RFID reader gets a unique URL that can be linked to, exchanged in emails, browsed for bookmarked, etc. Furthermore, these resources can be accessed using the standard HTTP 1.1 protocol. Thus, any platform that understands HTTP protocol can consume data from the EPCIS Webadapter in a straightforward way.

The simplest way to used the EPCIS Webadapter is to use the HTML representation of resources. Pointing any browser to:

```
http://HOST:PORT/epcis-webadapter-VERSION_NUMBER
```

will give you access to the HTML representation. From there you can:

  * Query the EPCIS using an HTML form by clicking on "Query Form Browsable Event Finder".
  * Navigate through the EPCIS data directly, by clicking on "Browsable Event Finder".
  * Manage Atom Feeds generated for queries, by clicking on "Subscription List".
  * Simulate RFID events for testing purposes using a simple HTML form, by clicking on "Capture Interface".
  * Configure the EPCIS Webadapter by clicking on "Configuration".

Note that each URL you will encounter in the HTML representation is unique and referenceable. Thus, you can bookmark any result for later use or sharing.

## Developing Applications Using the RESTful API ##

The EPCIS Webadapter makes it easier to create lightweight (e.g., mobile, Web) applications that consume EPC data. From a developer view point, all the interaction with the EPCIS Webadapter occurs through the exhaustive use of HTTP 1.1.

Basically, the Webadapter covers all the functionality of a standard EPCIS repository, plus some more advanced query capabilities. The best way to find how to access this functionality is to explore the HTML representation. There are basically two ways of formulating queries:

  * By using the "Browsable Event Finder", which lets you explore the available resources by clicking on hyper-links.
  * By using the "Query Form", which lets you formulate queries through an HTML form.

Both interfaces will directly display the query results. You can then use the resulting URLs from your application.

For example, let's say we are looking for all the EPC events for the business location: `urn:ch:sap:regensdorf:frc`. We first use the "Browsable Event Finder" to formulate this query. We directly see the query result and copy the query URL to use it in our application, we just need to `GET`:

```
http://HOST:PORT/epcis-webadapter-VERSION_NUMBER/rest/API_VERSION/eventquery/result?location=urn:ch:sap:regensdorf:frc
```

### Tools for Interacting with the RESTful API ###

Any client that can communicate with the Web (e.g., using the java.net package) can consume RESTful Web services. However, several tools can help you make this even more straightforward:

  * HTTP Client Libraries such as the [Apache HTTP Client](http://hc.apache.org/httpcomponents-client-ga) are available on most platforms (e.g., Android) and help you formulate RESTful request within a few lines of code.
  * Programming languages such as PHP, Python, Javascript, Ruby have full built in support for consuming RESTful Web Services over HTTP.
  * In the Java world, implementations of the JAX-RS standard provide high level client API (e.g., [Jersey Client API](http://jersey.dev.java.net/)) that literally reduce the complexity of communicating with RESTful service to a couple of lines.
  * For testing, tools such as the [Poster Firefox Extension](http://code.google.com/p/poster-extension/), or the [cURL command-line tool](http://curl.haxx.se/) are great to test all the features of HTTP and RESTful APIs.

### Requesting Different Formats ###

As a RESTful API, the EPCIS Webadapter offers several representations (i.e., formats) for resources identified by the following mime-types:

  * `text/html`: a Web page, including page layout, navigation menu and logos. This representation is human-friendly and meant for Web browsers.
  * `application/xhtml+xml`: contains just the core business of the resource, without layout, navigation or images. It is meant to be embedded into other Web pages or used in Web Mashups.
  * `text/xml`: is an XML representation, strictly following the XML Schema of the EPCIS standard.
  * `application/xml`: is an XML representation containing the whole state of the resource (e.g., links to sub-resources, etc.).
  * `application/json`: is a [JSON](http://www.json.org/) representation of a resource and its state. JSON is a lightweight alternative to XML which is often used in Web Mashups or mobile applications.

A client requests a particular format through the standard HTTP 1.1 [content-negotiation](http://en.wikipedia.org/wiki/Content_negotiation) mechanism.

More concretely it does this by setting the Accept header of the HTTP packet to one (or a list) of accepted/preferred formats. As an example, we can use [cURL](http://curl.haxx.se/) to get the list of all EPC events for the business location: `urn:ch:sap:regensdorf:frc` in JSON:

```
curl -X GET -H Accept:application/json http://localhost:8080/EPCIS Webadapter-0.0.1-SNAPSHOT/rest/0.0.1/eventquery/result?reader=urn:br:maxhavelaar:natal:shipyard:incoming
```

The EPCIS Webadapter also provides a WADL (Web Application Description Language) file to describe the resources it offers. While this can be practical in some cases, WADL-based REST clients are subject to many critisims in the REST community as a clean and browsable HTML representation covers almost all the expressive power of WADL already.

However, if you do need a WADL for your application, you can find it by sending `OPTIONS` to:

```
http://HOST:PORT/epcis-webadapter-VERSION_NUMBER/rest/API_VERSION/
```

### Working with Feeds ###

The EPCIS Webadapter also features an [Atom](http://en.wikipedia.org/wiki/Atom_%28standard%29) Web feed subscription and generation system on top of the EPCIS Subscription Standard. This is especially useful since, unlike the original EPCIS Subscription system, it does not require clients to run a server with a tailored Web application that listens to the subscribed endpoint. Thus, it is more adapted to more lightweight clients such as mobile phones, sensor networks, Web browsers, etc.

To create an Atom feed you first need to create the query URL. This is best achieved by using the "Browsable Event Finder" or the "Query Form". Once you have the query URL you can subscribe to it by calling PUT, for example on:

```
http://HOST:PORT/epcis-webadapter-VERSION_NUMBER/rest/API_VERSION/eventquery/subscription?reader=urn:epc:id:sgln:unifr.perolles.cafeteria
```

Or with cURL:

```
curl -X PUT -H Accept:application/xml http://HOST:PORT/EPCIS Webadapter-VERSION_NUMBER/rest/API_VERSION/eventquery/subscription?reader=urn:epc:id:sgln:unifr.perolles.cafeteria
```

You can then get the feeds by calling `GET` on:

```
http://HOST:PORT/epcis-webadapter-VERSION_NUMBER/rest/feed
```

### Sample Calls using HTTP and REST Client Libraries ###

The EPCIS Webadapter can be easily used from any language supporting sockets. However, it is very straightforward to use in combination with HTTP client libraries. Here, we provide two examples in Java using two different HTTP library.

### Simple Example Using the Apache HTTP Client ###

This an example of a query on the EPCIS Webadapter using the [Apache HTTP Client](http://hc.apache.org/httpcomponents-client-ga) in Java. This call uses the "query" resource to get all the EPC events that occurred at `urn:ch:sap:regensdorf:frc:storage`. Note that this query was successfully tested on a desktop computer as well as on an Android 2.2 mobile device:

```
import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

[...]

    public String querywebadapter() {
        String result = null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet request = new HttpGet("http://localhost:8080/EPCIS Webadapter-0.0.1-SNAPSHOT/rest/0.0.1/eventquery/result?reader=urn:ch:sap:regensdorf:frc:storage");
        request.addHeader("Accept", "application/json");

        ResponseHandler<String> handler = new BasicResponseHandler();
        try {
            result = httpclient.execute(request, handler);
        } catch (ClientProtocolException e) {
            //handle exception
        } catch (IOException e) {
            //handle exception
        }
        httpclient.getConnectionManager().shutdown();

        return result;
    }
```

### Simple Example Using the Jersey JAX-RS Client Library ###

In this more compact version, we use the [Jersey JAX-RS Client](http://jersey.dev.java.net/) library in Java to get all the locations registered by the EPCIS Repository on which the EPCIS Webadapter is plugged:

```
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
  
    public String queryWebAdapterJersey() {
        Client client = Client.create();
        WebResource webResource = client.resource("http://localhost:8080/EPCIS Webadapter-0.0.1-SNAPSHOT/rest/0.0.1/location");        
        return webResource.accept("application/xml").get(String.class);
    }
```

## Known Issues ##

_"The XML and JSON links of the HTML representations do not work"_

We identified that on some browsers (e.g., Chrome) the Javascript that offers to visualize different representations directly from the HTML page does not work. It was successfully tested on Firefox. If you want to see the other representations please refer to _Requesting Different Formats_.

_"I get the following exception when deploying the EPCIS Webadapter"_

```
java.lang.NullPointerException
        org.fosstrak.webadapters.epcis.db.InternalDatabase.getConfigurationEntry(InternalDatabase.java:202)
        org.fosstrak.webadapters.epcis.db.InternalDatabase.initializeIfNotPresent(InternalDatabase.java:121)
        org.fosstrak.webadapters.epcis.db.InternalDatabase.<init>(InternalDatabase.java:73)
        org.fosstrak.webadapters.epcis.db.InternalDatabase.getInstance(InternalDatabase.java:84)
...
```

This is a known bug due to the fact that the SQLite embedded database-driver the EPCIS Webadapter uses is based on JNI. Thus, in some containers (e.g., Tomcat) it can only be loaded once and is not released when undeploying the application. The only workaround we currently have is to restart the Tomcat instance. We are working on migrating the SQLite database to one that does not require the use of a JNI driver.

As the project is part of an ongoing research project, your feedback is very valuable to us. We created a questionnaire where we try to better understand your experience and where you can ask us for new features or improvements, thanks a lot for your answers: [Feedback form](https://spreadsheets.google.com/viewform?formkey=dGplWlFEc3pwajNJVldnUjZTZzh3ZkE6MA).