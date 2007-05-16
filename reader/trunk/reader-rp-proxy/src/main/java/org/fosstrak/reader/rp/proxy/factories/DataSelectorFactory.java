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

import org.accada.reader.rp.proxy.DataSelector;
import org.accada.reader.rp.proxy.RPProxyException;
import org.accada.reader.rp.proxy.ReaderDevice;
import org.accada.reader.rp.proxy.invocationHandlers.ProxyInvocationHandler;
import org.accada.reader.rp.proxy.msg.ParameterTypeException;
import org.accada.reader.rp.proxy.msg.ProxyConnection;

/**
 * This class creates new data selectors and new data selector proxies. 
 * 
 * @author regli
 */
public class DataSelectorFactory {

	/**
	 * This method returns a new data selector proxy which belongs to an existing data selector.
	 * 
	 * @param name of the existing data selector
	 * @param proxyConnection of the corresponding reader device proxy
	 * @return new data selector proxy
	 */
	public static DataSelector getDataSelector(String name, ProxyConnection proxyConnection) {
		
		InvocationHandler handler = new ProxyInvocationHandler("DataSelector", name, proxyConnection);
		return (DataSelector) Proxy.newProxyInstance(DataSelector.class.getClassLoader(), new Class[] { DataSelector.class }, handler);
		
	}

	/**
	 * This method creates a new data selector and returns a corresponding proxy.
	 * 
	 * @param name of the new data selector
	 * @param readerDevice to which the new trigger should be added
	 * @return corresponding data selector proxy
	 * @throws RPProxyException if the new data selector could not be created
	 */
	public static DataSelector createDataSelector(String name, ReaderDevice readerDevice) throws RPProxyException {
		
		try {
			ProxyConnection proxyConnection = ReaderDeviceFactory.getConnection(readerDevice);
			proxyConnection.executeCommand("DataSelector", "create", new Class[] {String.class}, new Object[] {name}, "", false);
			return getDataSelector(name, proxyConnection);
		} catch (ParameterTypeException e) {
			throw new RPProxyException(e.getClass() + ": " + e.getMessage());
		}
		
	}

}