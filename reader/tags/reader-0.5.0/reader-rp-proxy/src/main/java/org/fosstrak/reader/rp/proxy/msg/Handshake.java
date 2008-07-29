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

package org.fosstrak.reader.rp.proxy.msg;

public class Handshake {
	public static final int FORMAT_XML = 1;
	public static final int FORMAT_TEXT = 2;
	public static final int TCP = 3;
	public static final int HTTP = 4;
	
	private int transportProtocol;
	private int messageFormat;
	/**
	 * @return Returns the messageFormat.
	 */
	public int getMessageFormat() {
		return messageFormat;
	}
	/**
	 * @param messageFormat The messageFormat to set.
	 */
	public void setMessageFormat(int messageFormat) {
		this.messageFormat = messageFormat;
	}
	/**
	 * @return Returns the transportProtocol.
	 */
	public int getTransportProtocol() {
		return transportProtocol;
	}
	/**
	 * @param transportProtocol The transportProtocol to set.
	 */
	public void setTransportProtocol(int transportProtocol) {
		this.transportProtocol = transportProtocol;
	}
	
	public String serializeTcp() {
		if (messageFormat == FORMAT_XML) {
			return "RPS111X1X1NA0000END1";
		} else {
			return "RPS111T1T1NA0000END1";
		}
	}
	
	public String serializeHttp() {
		StringBuffer buf = new StringBuffer();
		buf.append("Content-Type: ");
		if (messageFormat == FORMAT_XML) {
			buf.append("text/xml; charset=utf-8\r\n");
		} else {
			buf.append("text/plain; charset=utf-8\r\n");
		}
		return buf.toString();
		
	}
}
