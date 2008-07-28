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

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class EventSink {
	public static void main(String[] args) throws Exception {
        int port;
        // check if args[0] is tcp-port
        if (args.length == 1){
        	port = Integer.parseInt(args[0]);
        } else {
        	port = 9000;
        }
		ServerSocket ss = new ServerSocket(port);
		while(true) {
			Socket s = ss.accept();
			InputStream in = s.getInputStream();
			int data;
			do {
				data = in.read();
				System.out.print((char)data);
			} while (data != -1);
		}
	}


}