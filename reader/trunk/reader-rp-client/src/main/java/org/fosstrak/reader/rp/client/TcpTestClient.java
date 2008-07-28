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
import java.io.OutputStreamWriter;
import java.net.Socket;

public class TcpTestClient {

	private static final String xmlMessage = "<?xml version=\"1.0\"?>\n<command xmlns=\"urn:epcglobal:rp:xsd:1\">\n<id xmlns=\"\">1234</id><targetName xmlns=\"\">channel1</targetName><notificationChannel xmlns=\"\">\n<setAddress xmlns=\"\"><address xmlns=\"\">http://myhost.com:8080/EPCApp</address></setAddress></notificationChannel></command>";
	private static final String xmlGoodbye = "<?xml version=\"1.0\"?>\n<command xmlns=\"urn:epcglobal:rp:xsd:1\">\n<id xmlns=\"\">1234</id><targetName xmlns=\"\">channel1</targetName><readerDevice xmlns=\"\">\n<goodbye xmlns=\"\" /></readerDevice></command>";
	private static final String handShake = "RPS111X1X1AR0000END1";
	
	public void run() throws Exception {
		Socket socket = new Socket("localhost",5566);
		
		OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());
		DataInputStream in = new DataInputStream( socket.getInputStream());
		
		out.write(handShake);
		out.write(xmlMessage + "\n");
		out.flush();
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
		System.out.println("Sending goodbye!");
		out.write(xmlGoodbye + "\n");
		out.flush();
		
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
		TcpTestClient tc = new TcpTestClient();
		tc.run();
		
		

	}

}
