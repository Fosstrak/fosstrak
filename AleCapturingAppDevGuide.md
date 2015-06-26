# Capturing Application: Developer Guide #



## Overview ##

From an event notification from the Fosstrak Filtering and Collection server (ECReports) you can easily create an event for the EPCIS repository. You can either implement your own capturing application (section "My own Capturing App") or use the generic capturing application provided by Fosstrak (section "Generic Capturing App").

## Generic Capturing App ##

The generic fosstrak capturing application tries to reduce the coding amount to an absolute minimum. Retrieval and de-serialization of ECReports is handled automatically for you, as well as the delivery of ECPIS documents to the EPCIS repository. All you need to do, is to implement a set of [JBoss rules](http://www.jboss.org/drools/) (Drools) and adjust a few configuration files.

To execute the capturing application, you simply place the WAR file into the `webapps` folder of your tomcat instance. The capturing application will deploy and start automatically.

### Requirements ###

We assume that you already installed an instance of Apache Tomcat, the Fosstrak ALE and the Fosstrak EPCIS repository.
  * A copy of the Fosstrak capturing application
  * Optional: [7-zip](http://www.7-zip.org/) (allows to modify WAR files inplace)

### Configuration files ###

There are two ways, how you can edit the configuration files for the capturing application. First: You deploy the WAR file, modify the configuration files and then re-assemble the WAR file. Second: You use 7-zip to open the archive and use the embedded text editor to edit the configuration files within the war. Feel free to choose the variant you like to most.
  * `WEB-INF/classes/log4j.properties`: Contains the log4j logging directives. Here you can adjust the logging verbosity of the capturing application.
  * `WEB-INF/classes/captureapplication.properties`: Allows you to specify any number of event sinks for the ALE (where the ECReports are sent to from the ALE). By default, no event sink is enabled.
```
# specifies how many event sinks are present in the configuration file.
n=2

# configuration for capturing app 0
# unique port where the capturing app is listening for ECReports
cap.0.port=9999
# unique name for the capturing app.
cap.0.name=theFirstCaptureApp
# url where to send the collected EPCIS-documents to
cap.0.epcis=http://localhost:8080/epcis-repository-0.4.2/capture

# configuration for capturing app 1
cap.1.port=9998
cap.1.name=theSecondCaptureApp
cap.1.epcis=http://someOtherEPCISserverIP:8080/epcis-repository-0.4.2/capture
# specifies the changeset file for this capturing app (in essence the rules...)
cap.2.changeset=changeset-secondCaptureApp.xml
```
  * `WEB-INF/classes/changeset.xml`: Allows you to specify a set of drools rules active for the capturing application (consult the [Drools manual](http://www.jboss.org/drools/documentation.html) in section 2.3.1). If you place your drools rules in the subfolder `WEB-INF/classes/drools`, you can simply copy one of existing directives within the `changeset.xml` and adjust it to your needs. You can create _custom changeset files_ and specify them for each capturing application in the configuration file `captureapplication.properties`. Please place the custom files in the folder `WEB-INF/classes`.

### Implement Drools rules ###

We will not cover how to program Drools rules as there exists an excellent [Drools manual](http://www.jboss.org/drools/documentation.html). However, we will give a few useful hints you need to obey to use the Fosstrak capturing application.

  1. All Drools rules must define the global collector `epcisResults` in the head of the Drools rules file. This collector implements a `LinkedList` where you can append your EPCIS documents. Whenever new items are added to the collector, the capturing application retrieves those items and tries to deliver them to the EPCIS repository.
```
// the global collector for all the EPCIS documents for further processing.
global java.util.List epcisResults
```
  1. To dispatch an assembled EPCIS document use the collector `epcisResults`.
```
// add an EPCIS-document to the capturing app.
epcisResults.add(myEPCISdocument);
```
    1. Fosstrak provides some helpers to retrieve data from ECReports and to assemble EPCIS documents. The helper functions can be found in the two libraries `org.fosstrak.capturingapp.util.Util` and `org.fosstrak.capturingapp.util.SimpleEPCISDocument`. Please consult the Javadoc for detailed instructions.

### Complete Example ###

The code below shows you an example of a Drools rule file (This rule is shipped together with the Fosstrak capturing application). The rule retrieves all ECReports, extracts all the tags as hex values in any contained report and dispatches them as a new EPCIS document.

```
package org.fosstrak.capturingapp
 
import org.fosstrak.capturingapp.util.Util; 
import org.fosstrak.ale.xsd.ale.epcglobal.ECReport;
import org.fosstrak.ale.xsd.ale.epcglobal.ECReports;
import org.fosstrak.ale.xsd.epcglobal.EPC;
import org.fosstrak.capturingapp.util.SimpleEPCISDocument;
import org.fosstrak.epcis.model.ActionType;

import java.util.LinkedList;
import function org.fosstrak.capturingapp.util.Util.extractEPC;

// the global collector for all the EPCIS documents for further processing.
global java.util.List epcisResults

rule "Create EPCIS event from EPCs"
        dialect "java"
        when
                $reports : ECReports()
                $epcs : LinkedList( size > 0 ) from collect (
                        EPC() from extractEPC(Util.selectRawHex, $reports)
                        ) 
        then
        
                SimpleEPCISDocument simpleDocument = new SimpleEPCISDocument();
                simpleDocument.addObjectEvent(
                        $epcs, 
                        ActionType.ADD, 
                        "urn:fosstrak:demo:bizstep:testing", 
                        "urn:fosstrak:demo:disp:testing",
                        "urn:fosstrak:demo:rp:1.1",
                        "urn:fosstrak:demo:loc:1.1"
                        );
                epcisResults.add(simpleDocument.getDocument());
end
```

### Extending the Capturing Application ###

If you need more control on the drools session loading etc., the fosstrak capturing application provides an extendable API.

#### Handlers ####

Incoming ECReports are processed by a number of so-called handlers. You can implement your own handler, pack it into a JAR file and place this JAR file into the library folder of the capturing application (`WEB-INF/lib`). Each handler needs to implement the abstract class `org.fosstrak.capturingapp.ECReportsHandler.java`.

For a detailed explanation how to write your own handler, please refer to the Javadoc of the class `ECReportsHandler.java` or use the default handler `DefaultECReportHandler`.

#### Handler loading ####

To load your custom handler at runtime, you need to specify the class-name of your handler in the capturing application properties file `captureapplication.properties` (If you omit the configuration, the Fosstrak default handler will be loaded automatically).

```
n=1

# configuration for capturing app 0 with a custom handler and a custom changeset
cap.0.port=9999
cap.0.name=theFirstCaptureApp
cap.0.epcis=http://localhost:8080/epcis-repository-0.4.2/capture
# custom changeset
cap.0.changeset=myChangeset.xml
# custom handler
cap.0.handler=org.fosstrak.capturingapp.DefaultECReportHandler
```

## My own Capturing App ##

This guide will explain the different steps that have to be taken to implement a capturing application from scratch. The capturing application discussed will retrieve an ECReports over the HTTP protocol. And generate an EPCIS Event through the ECPIS capture client.

The guide will omit error handling to keep the documentation short. The complete example at the end of the guide will implement the whole error handling.

Download the source code: [CaptureApp.java](http://fosstrak.googlecode.com/svn/capturingapp/trunk/src/site/resources/files/CaptureApp.java)

To run the capturing app you will need the following packages in your classpath:
  * jaxb-api (eg. jaxb-api-2.0.jar)
  * jaxb-impl (eg. jaxb-impl-2.0.1.jar)
  * fc-commons (eg. fc-commons-0.3.0.jar)
  * epcis-captureclient (eg. epcis-captureclient-0.3.0.jar)
  * epcis-commons (eg. epcis-commons-0.3.0.jar)
  * xml-apis (eg. xml-apis-1.3.03.jar from javax)
  * log4j (eg. log4j-1.2.12.jar)

### Retrieve ECReports ###

First you need to open a socket to retrieve the ECReports from the Filtering and Collection server.

```
// create a new server socket to retrieve ECReports
ServerSocket ss = new ServerSocket(port);
while (true) {
        // accept an incoming message
        Socket s = ss.accept();
        
        // ... (more to come)
}
```

After the server socket has been created and incoming messages get accepted by another socket, you have to read the HTTP message from the socket. Therefor you create an `InputStream` and read the message. We throw away the http header as we are just interested into the XML content holding the ECReports.

```
// create a new server socket to retrieve ECReports
ServerSocket ss = new ServerSocket(port);
while (true) {
        // accept an incoming message
        Socket s = ss.accept();

        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        
        String data = in.readLine();
        String buf = "";
        // ignore the http header
        data = in.readLine();
        data = in.readLine();
        data = in.readLine();
        data = in.readLine();

        while (data != null) {
                buf += data;
                data = in.readLine();
        }
        
        // ... (more to come)
}
```

After this step the ECReports are stored in the string `buf`. Now we have to deserialize the XML into an instance of an ECReports. Fosstrak offers you some helper methods that will do that job for you. You can find the whole serializer/deserializer methods in the package `org.fosstrak.ale.util` in the classes `DeserializerUtil` and `SerializerUtil` respectively.

For a detailed discussion of the serializer/deserializer utilties you can refer to the section on [serializers and deserializers](AleDevGuideSerializers.md) in the developer guide.

The serializer/deserializer methods require the input as an `InputStream`, we therefor create a `ByteArrayInputStream` from `buf`.

```
// create a new server socket to retrieve ECReports
ServerSocket ss = new ServerSocket(port);
while (true) {
        // accept an incoming message
        Socket s = ss.accept();

        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        
        String data = in.readLine();
        String buf = "";
        // ignore the http header
        data = in.readLine();
        data = in.readLine();
        data = in.readLine();
        data = in.readLine();

        while (data != null) {
                buf += data;
                data = in.readLine();
        }
        
        // create a stream from the buf
        InputStream parseStream = new ByteArrayInputStream(buf.getBytes());
        
        // parse the string through the serializer/deserializer utils.
        ECReports reports = DeserializerUtil.deserializeECReports(parseStream);
        if (reports != null) {
                // call the handler that will process the ECReports
                handleReports(reports);
        }
}
```

### Generate EPCIS Event ###

To create an EPCIS Event the EPCIS capture client is used. This guide will not discuss the generation of the event itself but outline the steps that have to be taken to retrieve the EPC tags from the ECReports.

In this example we extract all the epcs from the ECReports and put them collectively into one EPCIS event. Thatfor we store all the EPCs into a list of EPC.

```
List<ECReport> theReports = reports.getReports().getReport();
// collect all the tags
List<EPC> epcs = new LinkedList<EPC>();
if (theReports != null) {
        for (ECReport report : theReports) {
                if (report.getGroup() != null) {
                        for (ECReportGroup group : report.getGroup()) {
                                if (group.getGroupList() != null) {
                                        for (ECReportGroupListMember member : group.getGroupList().getMember()) {
                                                if (member.getRawDecimal() != null) {
                                                        epcs.add(member.getRawDecimal());
                                                }
                                        }                                                       
                                }
                        }
                }
        }
}

if (epcs.size() == 0) {
        System.out.println("no epc received - generating no event");
        return;
}
```

After that step you have to create an `EPCISDocumentType` instance. Please refer to the users guide in the EPCIS documentation for a discussion. In this place we will just show you how to put the EPCs we just extracted from the ECReports into the `EPCISDocumentType`.

```
// ... (more code in front)

EPCListType epcList = new EPCListType();
// add the epcs
for (EPC epc : epcs) {
        org.fosstrak.epcis.model.EPC nepc = new org.fosstrak.epcis.model.EPC(); 
        nepc.setValue(epc.getValue());
        epcList.getEpc().add(nepc);
}
objEvent.setEpcList(epcList);

// ... (more code behind)
```

When you created the whole `EPCISEvent` and populated it with epc data, you are ready to call the capture client.

```
EPCISDocumentType epcisDoc = new EPCISDocumentType();

// ... (code in between)

// send the event to the repository
int httpResponseCode = client.capture(epcisDoc);
if (httpResponseCode != 200) {
    System.out.println("The event could NOT be captured!");
}
```

### Full Code Example ###

This section gives you an overview to the code for a whole capturing application.

Download the source code: [CaptureApp.java](http://fosstrak.googlecode.com/svn/capturingapp/trunk/src/site/resources/files/CaptureApp.java)

```

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.fosstrak.ale.util.DeserializerUtil;
import org.fosstrak.ale.xsd.ale.epcglobal.ECReport;
import org.fosstrak.ale.xsd.ale.epcglobal.ECReportGroup;
import org.fosstrak.ale.xsd.ale.epcglobal.ECReportGroupListMember;
import org.fosstrak.ale.xsd.ale.epcglobal.ECReports;
import org.fosstrak.ale.xsd.epcglobal.EPC;
import org.fosstrak.epcis.captureclient.CaptureClient;
import org.fosstrak.epcis.model.ActionType;
import org.fosstrak.epcis.model.BusinessLocationType;
import org.fosstrak.epcis.model.EPCISBodyType;
import org.fosstrak.epcis.model.EPCISDocumentType;
import org.fosstrak.epcis.model.EPCListType;
import org.fosstrak.epcis.model.EventListType;
import org.fosstrak.epcis.model.ObjectEventType;
import org.fosstrak.epcis.model.ReadPointType;

public class CaptureApp {
        
        private int port;
        
        private String epcisRepository;
        
        private CaptureClient client = null;
        
        public CaptureApp(int port, String epcisRepository) {
                this.port = port;
                this.epcisRepository = epcisRepository;
        }

        private void handleReports(ECReports reports) throws IOException, JAXBException {
                System.out.println("Handling incomming reports");
                        
                List<ECReport> theReports = reports.getReports().getReport();
                // collect all the tags
                List<EPC> epcs = new LinkedList<EPC>();
                if (theReports != null) {
                        for (ECReport report : theReports) {
                                if (report.getGroup() != null) {
                                        for (ECReportGroup group : report.getGroup()) {
                                                if (group.getGroupList() != null) {
                                                        for (ECReportGroupListMember member : group.getGroupList().getMember()) {
                                                                if (member.getRawDecimal() != null) {
                                                                        epcs.add(member.getRawDecimal());
                                                                }
                                                        }                                                       
                                                }
                                        }
                                }
                        }
                }
                
                if (epcs.size() == 0) {
                        System.out.println("no epc received - generating no event");
                        return;
                }
                
                // create the ecpis event
                ObjectEventType objEvent = new ObjectEventType();

                // get the current time and set the eventTime
                XMLGregorianCalendar now = null;
                try {
                    DatatypeFactory dataFactory = DatatypeFactory.newInstance();
                    now = dataFactory.newXMLGregorianCalendar(new GregorianCalendar());
                    objEvent.setEventTime(now);
                } catch (DatatypeConfigurationException e) {
                    e.printStackTrace();
                }

                // get the current time zone and set the eventTimeZoneOffset
                if (now != null) {
                    int timezone = now.getTimezone();
                    int h = Math.abs(timezone / 60);
                    int m = Math.abs(timezone % 60);
                    DecimalFormat format = new DecimalFormat("00");
                    String sign = (timezone < 0) ? "-" : "+";
                    objEvent.setEventTimeZoneOffset(sign + format.format(h) + ":" + format.format(m));
                }
                
                EPCListType epcList = new EPCListType();
                // add the epcs
                for (EPC epc : epcs) {
                        org.fosstrak.epcis.model.EPC nepc = new org.fosstrak.epcis.model.EPC(); 
                        nepc.setValue(epc.getValue());
                        epcList.getEpc().add(nepc);
                }
                objEvent.setEpcList(epcList);

                // set action
                objEvent.setAction(ActionType.ADD);

                // set bizStep
                objEvent.setBizStep("urn:fosstrak:demo:bizstep:testing");

                // set disposition
                objEvent.setDisposition("urn:fosstrak:demo:disp:testing");

                // set readPoint
                ReadPointType readPoint = new ReadPointType();
                readPoint.setId("urn:fosstrak:demo:rp:1.1");
                objEvent.setReadPoint(readPoint);

                // set bizLocation
                BusinessLocationType bizLocation = new BusinessLocationType();
                bizLocation.setId("urn:fosstrak:demo:loc:1.1");
                objEvent.setBizLocation(bizLocation);

                // create the EPCISDocument containing a single ObjectEvent
                EPCISDocumentType epcisDoc = new EPCISDocumentType();
                EPCISBodyType epcisBody = new EPCISBodyType();
                EventListType eventList = new EventListType();
                eventList.getObjectEventOrAggregationEventOrQuantityEvent().add(objEvent);
                epcisBody.setEventList(eventList);
                epcisDoc.setEPCISBody(epcisBody);
                epcisDoc.setSchemaVersion(new BigDecimal("1.0"));
                epcisDoc.setCreationDate(now);
                                
                int httpResponseCode = client.capture(epcisDoc);
                if (httpResponseCode != 200) {
                    System.out.println("The event could NOT be captured!");
                }
        }
        
        public void run() {
                client = new CaptureClient(epcisRepository);
                                
                ServerSocket ss = null;
                try {
                        ss = new ServerSocket(port);
                        while(true) {
                                try {
                                        Socket s = ss.accept();
                                        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                                   
                                        String data = in.readLine();
                                        String buf = "";
                                        // ignore the http header
                                        data = in.readLine();
                                        data = in.readLine();
                                        data = in.readLine();
                                        data = in.readLine();
                                        
                                        while (data != null) {
                                                buf += data;
                                                data = in.readLine();
                                        }
                                        System.out.println(buf);
                                        
                                        // create a stream from the buf
                                        InputStream parseStream = new ByteArrayInputStream(buf.getBytes());
                                        
                                        // parse the string
                                        
                                        ECReports reports = DeserializerUtil.deserializeECReports(parseStream);
                                        if (reports != null) {
                                                handleReports(reports);
                                        }
                                } catch (Exception e) {
                                        System.out.println("ERROR: " + e.getMessage());
                                }
                        }
              } catch (IOException e1) {
                 System.out.println(e1.getMessage());
              }
        }
        
        public static void help() {
                System.out.println("You need to specify the port where to listen and the url of the epcis repository");
                System.out.println("Example: ");
        }
        
        /**
         * starts the CaptureApp.
         * 
         * @param args the first command line parameter is the TCP port. if omitted port 9999 is used.
         */
        public static void main(String[] args) {
        CaptureApp client;
        int port;
        String epcisRepository;
        // check if args[0] is tcp-port
        // and args[1] is epcis repository
        if (args.length == 2){
                port = Integer.parseInt(args[0]);
                epcisRepository = args[1];
            client = new CaptureApp(port, epcisRepository);
            
        } else {
                help();
                return;
        }
        
        client.run();
        }
}
```