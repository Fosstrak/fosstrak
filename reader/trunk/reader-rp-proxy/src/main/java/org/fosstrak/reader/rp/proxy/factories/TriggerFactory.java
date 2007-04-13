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

import org.accada.reader.rp.proxy.RPProxyException;
import org.accada.reader.rp.proxy.ReaderDevice;
import org.accada.reader.rp.proxy.Trigger;
import org.accada.reader.rp.proxy.invocationHandlers.ProxyInvocationHandler;
import org.accada.reader.rp.proxy.msg.ParameterTypeException;
import org.accada.reader.rp.proxy.msg.ProxyConnection;

/**
 * This class creates new triggers and new trigger proxies. 
 * 
 * @author regli
 */
public class TriggerFactory {

	/**
	 * This method returns a new trigger proxy which belongs to an existing trigger.
	 * 
	 * @param name of the existing trigger
	 * @param proxyConnection of the corresponding reader device proxy
	 * @return new trigger proxy
	 */
	public static Trigger getTrigger(String name, ProxyConnection proxyConnection) {
		
		InvocationHandler handler = new ProxyInvocationHandler("Trigger", name, proxyConnection);
		return (Trigger) Proxy.newProxyInstance(Trigger.class.getClassLoader(), new Class[] { Trigger.class }, handler);
		
	}

	/**
	 * This method creates a new trigger and returns a corresponding proxy.
	 * 
	 * @param name of the new trigger
	 * @param type of the new trigger
	 * @param triggerValue of the new trigger
	 * @param readerDevice to which the new trigger should be added
	 * @return corresponding trigger proxy
	 * @throws RPProxyException if the new trigger could not be created
	 */
	public static Trigger createTrigger(String name, String type, String triggerValue, ReaderDevice readerDevice) throws RPProxyException {
		
		try {
			ProxyConnection proxyConnection = ReaderDeviceFactory.getConnection(readerDevice);
			proxyConnection.executeCommand("Trigger", "create", new Class[] {String.class, String.class, String.class}, new Object[] {name, type, triggerValue}, "", false);
			return getTrigger(name, proxyConnection);
		} catch (ParameterTypeException e) {
			throw new RPProxyException(e.getClass() + ": " + e.getMessage());
		}
		
	}

}