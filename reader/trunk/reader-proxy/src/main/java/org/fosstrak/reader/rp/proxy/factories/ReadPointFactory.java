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

import org.accada.reader.rp.proxy.ReadPoint;
import org.accada.reader.rp.proxy.invocationHandlers.ProxyInvocationHandler;
import org.accada.reader.rp.proxy.msg.ProxyConnection;

/**
 * This class creates new read point proxies. 
 * 
 * @author regli
 */
public class ReadPointFactory {

	/**
	 * This method returns a new read point proxy which belongs to an existing read point.
	 * 
	 * @param name of the existing read point
	 * @param proxyConnection of the corresponding reader device proxy
	 * @return new read point proxy
	 */
	public static ReadPoint getReadPoint(String name, ProxyConnection proxyConnection) {
		
		InvocationHandler handler = new ProxyInvocationHandler("ReadPoint", name, proxyConnection);
		return (ReadPoint) Proxy.newProxyInstance(ReadPoint.class.getClassLoader(), new Class[] { ReadPoint.class }, handler);
		
	}

}