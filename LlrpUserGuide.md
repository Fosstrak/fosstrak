# User Guide #

## Contents ##


## Objective ##

The user guide with step-by-step instructions on installation and use is available in [PDF format](http://fosstrak.googlecode.com/svn/wikires/llrp/LLRP_Commander_User_Guide.pdf).

If you just want to install the LLRP Commander, simply follow the instructions provided in the [installation guide](LlrpInstallation.md).

If you would like to use the !ROAccessReports database from Matlab, follow the instructions in the [developer guide](LlrpDevGuideRepository.md).

On this page we present a few easy use cases that show you, how you can use the LLRP Commander.

## Prerequisites ##

  * [Java 1.5](http://java.com/) or higher
  * [Eclipse 3.3.0](http://eclipse.org/) or higher
  * [Rifidi](http://rifidi.org/) reader simulator (this guide does not cover its installation)

## Use case - Local LLRP readers ##

In this use case we will create several virtual LLRP readers in the Rifidi reader simulator. We will connect from the Fosstrak LLRP Commander and will explore, how we can send and retrieve messages from the LLRP readers.

1. Start Rifidi and add a new LLRP reader with the name **port5084** and make sure to set the port to **5084**.

![http://fosstrak.googlecode.com/svn/wikires/llrp/rifidiAddReader.png](http://fosstrak.googlecode.com/svn/wikires/llrp/rifidiAddReader.png)

Right-click on the reader and start it.

2. Start LLRP Commander and connect to the virtual reader. Set the name to **port5084** and make sure to set the port to **5084**. The LLRP Commander should automatically connect to the LLRP reader.

![http://fosstrak.googlecode.com/svn/wikires/llrp/commanderAddReader.png](http://fosstrak.googlecode.com/svn/wikires/llrp/commanderAddReader.png)

3. We would like to know the capabilities of the virtual LLRP reader. We therefore send a _GET\_READER\_CAPABILITIES_ message through the LLRP Commander. Within the Commander right-click on the reader **port5084**, select _Send Message_ and click on _GET\_READER\_CAPABILITIES_.

![http://fosstrak.googlecode.com/svn/wikires/llrp/commanderSendGETCAPABILITIES.png](http://fosstrak.googlecode.com/svn/wikires/llrp/commanderSendGETCAPABILITIES.png)

Click on the reader and double click on the _GET\_READER\_CAPABILITIES\_RESPONSE_.

![http://fosstrak.googlecode.com/svn/wikires/llrp/commanderOpenMessage.png](http://fosstrak.googlecode.com/svn/wikires/llrp/commanderOpenMessage.png)

4. Browse through the LLRP message. In the **Graphical Editor** you can use the tree structure to browse and edit the message, in the **XML Editor** you have an XML editor at hand and in the **Binary Viewer** you can look at the message in binary.

![http://fosstrak.googlecode.com/svn/wikires/llrp/commanderInspectMessage.png](http://fosstrak.googlecode.com/svn/wikires/llrp/commanderInspectMessage.png)

5. Now we will add several LLRP readers in Rifidi. Go back to the reader simulator and add two new readers. For the first reader set the name to **port5055** with port **5055** and for the second reader set name to **port5044** with port **5044**. Start both readers.

Go back to the Commander and connect to the two readers. For the reader names you can select any meaningful name, however for the port you need to make sure, that the values match the settings in the reader simulator!

6. The Commander gives you the capability to send an LLRP message to several readers simultaneously. Open the message in the _Navigator_ and click on the red icon **LLRP** in the eclipse task bar. A dialog box pops up, where you can select the readers, where you would like to send the message to.

![http://fosstrak.googlecode.com/svn/wikires/llrp/commanderSendMessageToAll.png](http://fosstrak.googlecode.com/svn/wikires/llrp/commanderSendMessageToAll.png)

Depending on the selection in the _Reader Explorer View_, only a subset of messages get displayed in the _Message box view_. This helps you to manage your LLRP messages more easily. Please select reader **port5044** and notice that the _Message box_ now contains only the messages sent by the selected reader.

![http://fosstrak.googlecode.com/svn/wikires/llrp/commanderSendMessageToAllResult.png](http://fosstrak.googlecode.com/svn/wikires/llrp/commanderSendMessageToAllResult.png)