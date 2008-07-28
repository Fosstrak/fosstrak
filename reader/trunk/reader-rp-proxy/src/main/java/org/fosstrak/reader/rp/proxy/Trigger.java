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

package org.fosstrak.reader.rp.proxy;



public interface Trigger {

	public static final String CONTINUOUS = "continuous";
	public static final String TIMER = "timer";
	public static final String IO_EDGE = "ioEdge";
	public static final String IO_VALUE = "ioValue";
	
	/**
	 * Returns the maximum number of Triggers this Reader supports.
	 * @return The maximum number of Triggers this Reader supports
	 */
	int getMaxNumberSupported() throws RPProxyException;

	/**
	 * Returns the name of the given Trigger object.
	 * @return The name of the trigger
	 */
	String getName() throws RPProxyException;

	/**
	 * Returns the TriggerType of this Trigger.
	 * @return The TriggerType
	 */
	String getType() throws RPProxyException;

	/**
	 * Returns the triggervalue of this Trigger object.
	 * @return The value of the trigger
	 */
	String getValue() throws RPProxyException;

	/**
	 * This command causes this Trigger object to fire (i.e., activate the
	 * trigger) immediately, regardless of the value settings of the Trigger.
	 */
	void fire() throws RPProxyException;

}