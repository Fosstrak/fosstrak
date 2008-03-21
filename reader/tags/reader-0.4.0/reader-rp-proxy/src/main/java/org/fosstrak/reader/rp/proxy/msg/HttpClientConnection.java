/*
 * Copyright (C) 2007 ETH Zurich
 *
 * This file is part of Accada (www.accada.org).
 *
 * Accada is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1, as published by the Free Software Foundation.
 *
 * Accada is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Accada; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA  02110-1301  USA
 */

package org.accada.reader.rp.proxy.msg;


public class HttpClientConnection extends ClientConnection {

	public HttpClientConnection(Client target) {
		super(target);
	}
	
	public void sendHandshake() {
		/* nothing to do (no explicit handshake in HTTP, integrated in HTTP header) */

	}

	public void sendMessage(String message) {
		try {
			out.write("POST /control/ HTTP/1.1\r\n");
			out.write("Host: " + host + "\r\n");
			out.write(handshake.serializeHttp());
			out.write("Content-Length: " + message.getBytes().length + "\r\n");
			out.write("\r\n");
			//out.write(message + "\r\n"); //HTTP erlaubt "\r\n" am schluss nicht
			out.write(message);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	

}
