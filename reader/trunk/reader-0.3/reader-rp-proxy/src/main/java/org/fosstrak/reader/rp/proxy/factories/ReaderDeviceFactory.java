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

package org.accada.reader.rp.proxy.factories;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.accada.reader.rp.proxy.RPProxyException;
import org.accada.reader.rp.proxy.ReaderDevice;
import org.accada.reader.rp.proxy.invocationHandlers.ProxyInvocationHandler;
import org.accada.reader.rp.proxy.msg.Handshake;
import org.accada.reader.rp.proxy.msg.ProxyConnection;

/**
 * This class creates new reader device proxies and manages the proxy connections 
 * 
 * @author regli
 */
public class ReaderDeviceFactory {

	/** the proxy connections */
	private static final Map<String, ProxyConnection> connections = new HashMap<String, ProxyConnection>();
	
	/**
	 * This method returns a new reader device proxy which belongs to an existing reader device.
	 * 
	 * @param host of the reader device
	 * @param port of the reader device
	 * @param handshake indicates how to communicate with the reader device
	 * @return new reader device proxy
	 * @throws RPProxyException if the proxy could not connect to the reader device
	 */
	public static ReaderDevice getReaderDevice(String host, int port, Handshake handshake) throws RPProxyException {
		
		// connect
		ProxyConnection proxyConnection = new ProxyConnection(host, port, handshake);
		proxyConnection.connect();
		
		// create proxy
		InvocationHandler handler = new ProxyInvocationHandler("ReaderDevice", "", proxyConnection);
		ReaderDevice readerDeviceProxy = (ReaderDevice) Proxy.newProxyInstance(ReaderDevice.class.getClassLoader(), new Class[] { ReaderDevice.class }, handler);

		connections.put(readerDeviceProxy.toString(), proxyConnection);
		return readerDeviceProxy; 
		
	}

	/**
	 * This method returns the proxy connections to the corresponding reader device proxies.
	 * 
	 * @param readerDeviceProxy reader device proxy 
	 * @return the corresponding proxy connection
	 */
	public static ProxyConnection getConnection(ReaderDevice readerDeviceProxy) {
		
		return (ProxyConnection)connections.get(readerDeviceProxy.toString());
		
	}
	
}