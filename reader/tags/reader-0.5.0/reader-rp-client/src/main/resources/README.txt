
Fosstrak Reader Client
====================

The objective of the Fosstrak Reader Client Module is to provide Java Swing based
test clients to execute Reader Protocoll (RP) commands on a RFID reader that
implements the RP.


How to use the Reader Client
============================

Unpack the zip file 'reader-rp-client-x.x.x-bin-with-dependencies.zip' and execute
the class 'org.fosstrak.reader.rp.client.TestClient' in the jar file. In order to be
able to display notifications from the reader execute the class
'org.fosstrak.reader.rp.client.EventSinkUI' in the jar file. Use the TCP port where
the reader is sending the notifications as the command line argument of EventSinkUI.
The default port 9999.

For more information,  please see http://www.fosstrak.org/reader/reader-rp-client/
