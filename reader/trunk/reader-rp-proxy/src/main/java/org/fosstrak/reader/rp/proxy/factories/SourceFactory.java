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

package org.fosstrak.reader.rp.proxy.factories;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.fosstrak.reader.rp.proxy.RPProxyException;
import org.fosstrak.reader.rp.proxy.ReaderDevice;
import org.fosstrak.reader.rp.proxy.Source;
import org.fosstrak.reader.rp.proxy.invocationHandlers.ProxyInvocationHandler;
import org.fosstrak.reader.rp.proxy.msg.ParameterTypeException;
import org.fosstrak.reader.rp.proxy.msg.ProxyConnection;

/**
 * This class creates new sources and new source proxies. 
 * 
 * @author regli
 */
public class SourceFactory {

	/**
	 * This method returns a new source proxy which belongs to an existing source.
	 * 
	 * @param name of the existing source
	 * @param proxyConnection of the corresponding reader device proxy
	 * @return new source proxy
	 */
	public static Source getSource(String name, ProxyConnection proxyConnection) {
		
		InvocationHandler handler = new ProxyInvocationHandler("Source", name, proxyConnection); //new SourceInvocationHandler(name, proxyConnection);
		return (Source) Proxy.newProxyInstance(Source.class.getClassLoader(), new Class[] {Source.class}, handler);
		
	}

	/**
	 * This method creates a new source and returns a corresponding proxy.
	 * 
	 * @param name of the new source
	 * @param readerDevice to which the new source should be added
	 * @return corresponding source proxy
	 * @throws RPProxyException if the new source could not be created
	 */
	public static Source createSource(String name, ReaderDevice readerDevice) throws RPProxyException {
		
		try {
			ProxyConnection proxyConnection = ReaderDeviceFactory.getConnection(readerDevice);
			proxyConnection.executeCommand("Source", "create", new Class[] {String.class}, new Object[] {name}, "", false);
			return getSource(name, proxyConnection);
		} catch (ParameterTypeException e) {
			throw new RPProxyException(e.getClass() + ": " + e.getMessage());
		}
		
	}

}