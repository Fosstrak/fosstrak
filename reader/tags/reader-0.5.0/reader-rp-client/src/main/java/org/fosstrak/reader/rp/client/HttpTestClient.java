/*
 * Copyright (C) 2007 ETH Zurich
 *
 * This file is part of Fosstrak (www.fosstrak.org).
 *
 * Fosstrak is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1, as published by the Free Software Foundation.
 *
 * Fosstrak is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Fosstrak; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA  02110-1301  USA
 */

package org.fosstrak.reader.rp.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;

public class HttpTestClient {

	private static final String xmlMessage = "<?xml version=\"1.0\"?>\n<command xmlns=\"urn:epcglobal:rp:xsd:1\">\n<id xmlns=\"\">1234</id><targetName xmlns=\"\">channel1</targetName><notificationChannel xmlns=\"\">\n<setAddress xmlns=\"\"><address xmlns=\"\">http://myhost.com:8080/EPCApp</address></setAddress></notificationChannel></command>";
	private static final String createNotificationChannel = "<?xml version=\"1.0\"?>\n<command xmlns=\"urn:epcglobal:rp:xsd:1\">\n<id xmlns=\"\">1234</id><targetName xmlns=\"\">channel1</targetName><notificationChannel xmlns=\"\">\n<create xmlns=\"\"><name>channel1</name><address>http://localhost:9000</address></create></notificationChannel></command>";
	private static final String addSources = "<?xml version=\"1.0\"?>\n<command xmlns=\"urn:epcglobal:rp:xsd:1\">\n<id xmlns=\"\">1234</id><targetName xmlns=\"\">channel1</targetName><notificationChannel xmlns=\"\">\n<addSources xmlns=\"\"><name>channel1</name><addr>http://localhost:8080</addr></create></notificationChannel></command>";
	private static final String getAllNotificationChannels = "<?xml version=\"1.0\"?>\n<command xmlns=\"urn:epcglobal:rp:xsd:1\">\n<id xmlns=\"\">1234</id><targetName xmlns=\"\">channel1</targetName><readerDevice xmlns=\"\">\n<getAllNotificationChannels xmlns=\"\" /></readerDevice></command>";
	private static final String xmlGoodbye = "<?xml version=\"1.0\"?>\n<command xmlns=\"urn:epcglobal:rp:xsd:1\">\n<id xmlns=\"\">1234</id><targetName xmlns=\"\">channel1</targetName><readerDevice xmlns=\"\">\n<goodbye xmlns=\"\" /></readerDevice></command>";
	private static final String handShake = "RPS110X1X1AR0000END1";
	private static final String textMessage = "!123rd.getName \n";
	
	private void sendMessage(Writer out, String message) throws IOException {
		out.write("POST /control/ HTTP/1.1\r\n");
		out.write("Host: localhost\r\n");
		out.write("Content-Type: text/xml; charset=utf-8\r\n");
		out.write("Content-Length: " + message.getBytes().length + "\r\n");
		//out.write(handShake);
		out.write("\r\n");
		out.write(message + "\r\n");
		out.flush();
	}
	public void run() throws Exception {
		Socket socket = new Socket("localhost",8080);
		
		OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());
		DataInputStream in = new DataInputStream( socket.getInputStream());
		
		//sendMessage(out, xmlMessage);
		sendMessage(out, xmlMessage);
		
		System.out.println("Wating for response...");
		String line = in.readLine();
		boolean doRun = true;
		while(line != null && doRun) {
			System.out.println(line);
			line = in.readLine();
			if (line.equals("</reply>")) {
				System.out.println(line);
				doRun = false;
			}
		}
		//System.out.println("Sending goodbye!");
		//sendMessage(out,xmlGoodbye);
		
		line = in.readLine();
		while(line != null ) {
			System.out.println(line);
			line = in.readLine();
		}
		out.close();
		socket.close();
		//String data = in.readUTF();
		//System.out.println("Data received: " + data);
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		HttpTestClient tc = new HttpTestClient();
		tc.run();
		
		

	}

}
