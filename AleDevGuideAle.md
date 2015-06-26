# Developer Guide: ALE Service Binding #



## Overview ##

This document shall give a short overview to the ALE class from the Fosstrak filtering and collection, to the ALEServicePort and to the ALEServiceBinding.

The ALE is the responsible class for the communication with the applications of the Layer above. It provides:
  * Interface for the definition and modification of EventCycles
  * Subscription/unsubscription of report notifications
  * Administrative methods (eg. VendorVersion).
The ALEServicePort provides the interface that allows clients to connect to the ALE. The service port provides:
  * Serialization and deserialization of ECSpecs and other control messages.
  * Binding from the ALE interface to the actual implementation of the Filtering and Collection.

## EventCycle modification ##

The ALE provides the following methods to create or modify EventCycles:
  * define: Define takes a two parameters, a name for the EventCycle to be created and an ECSpec for the definition of the EventCycle.
  * undefine: When you want to destroy an EventCycle you can call this method with the appropriate name for the EventCycle to be destroyed.
  * getECSpecNames: Returns the names of all EventCycles available in the ALE.

## Client Subscription ##

Whenever a client wants to use an ALE service it has to register.
  * subscribe: A notificationURI will be subscribed to one of the EventCycles. The EventCycle can be selected through the parameter specName.
  * unsubscribe: When a client does no more need the service of an EventCycle it can unsubscribe the notifcationURI.
  * getSubscribers: Returns a list of all subscribers for the EventCycle specified.

## Immediate and Polling ##

The ALE interface provides 2 mechanisms for "one-off-queries":
  * immediate: A client provides an anonymous ECSpec that will be executed once. The resulting ECReports are sent back.
  * poll: A client can poll an existing ECSpec. The Filtering and Collection immediately starts this ECSpec and returns the ECReports.

## WSDL Interface ##

The interface to the ALE is modelled in the WebServices Description Language WSDL.

The interface basically describes the methods that are available on the webservice. Consequently the messages that are exchanged (this includes even exceptions) are modelled in the WSDL as well.

This guide will not discuss how a WSDL file has to be written. For further information refer to Apache CXF. This guide just gives a short explanation how the Java stubs can be created out of the WSDL file and what changes have to be done to the generated stubs.

### Hint ###

For technical references you can refer to the W3C standard documentation for WSDL 1.1: http://www.w3.org/TR/wsdl

If you ever encounter a problem with the serialization of an XML the following procedure is suggested:
  * Write a handcrafted java-class holding all the parameters that shall be serialized.
  * Add a specific serializer/deserializer to the SerializerUtils (see `org.fosstrak.ale.util.SerializerUtil`).
  * Call that serializer/deserializer and look at the xml that is generated.

### Requirements ###

  * A working copy of [Apache CXF](http://incubator.apache.org/cxf/)
  * The EPC base XSD definition: [EPCGlobal.xsd](http://fosstrak.googlecode.com/svn/wikires/ale/EPCGlobal.xsd)
  * The ALE definitions: [EPCglobal-ale-1\_1-ale.xsd](http://fosstrak.googlecode.com/svn/wikires/ale/EPCglobal-ale-1_1-ale.xsd)
  * The ALE commons: [EPCglobal-ale-1\_1-common.xsd](http://fosstrak.googlecode.com/svn/wikires/ale/EPCglobal-ale-1_1-common.xsd)
  * The ALE Serviceport WSDL definition: [EPCglobal-ale-1\_1-ale.wsdl](http://fosstrak.googlecode.com/svn/wikires/ale/EPCglobal-ale-1_1-ale.wsdl)

### Compiling ###

To compile the wsdl file you need Apache CXF.

We will use the wsdl2java program to translate the WSDL to Java classes.

Compile the WSDL:

```
wsdl2java -impl -v 
        -p urn:epcglobal:xsd:1=org.fosstrak.ale.xsd.epcglobal 
        -p urn:epcglobal:ale:xsd:1=org.fosstrak.ale.xsd.ale.epcglobal 
        -p urn:epcglobal:ale:wsdl:1=org.fosstrak.ale.wsdl.ale.epcglobal 
        EPCglobal-ale-1_1-ale.wsdl
```

The flags in detail:
  * "-impl": This tells wsdl2java to generate a stub for the service binding implementation.
  * "-p ." : if you omit the namespace mapping all packages get generated into the java namespace _1.
To implement the service binding you need to implement the java class ALEServicePortTypeImpl._

In the Fosstrak Filtering and Collection most parts of the wsdl are auto-generated and you need not to change anything (see the pom.xml if you are interested).

The ALEServicePortTypeImpl is stored in the module fc-server (see the next chapter of this guide).

### ServiceBinding ###

The stubs described in the former chapter are used for the communication between two services. However the binding from these stubs to the actual implementation of the ALE has to be defined explicitly. This task is performed by the ALEServicePortTypeImpl.

For each method defined in [EPCglobal-ale-1\_1-ale.wsdl](http://fosstrak.googlecode.com/svn/wikires/ale/EPCglobal-ale-1_1-ale.wsdl) there exists a method in the java file ALEServicePortTypeImpl. Whenever a client calls a method on its local method stub, the messages involved get serialized into xml, sent over the communication channel and then deserialized by the server stub. The stub then calls the corresponding method on the ALEServicePortTypeImpl. The ALEServicePortTypeImpl reformatts the incomming message (if necessary) and calls the ALE accordingly.

Example for getStandardVersion:

```
    public java.lang.String getStandardVersion(EmptyParms parms) 
        throws ImplementationExceptionResponse    {
        
        if (!isInitialized) { initialize(); }
        return ALE.getStandardVersion();
    }
```

You need to implement all the methods that shall be provided to clients in the ALEServicePortTypeImpl.

### Status ###

Currently the ALE supports the functionality of the logical reader API. For further reading refer to the [features list](AleFeatures.md) and the Javadoc.