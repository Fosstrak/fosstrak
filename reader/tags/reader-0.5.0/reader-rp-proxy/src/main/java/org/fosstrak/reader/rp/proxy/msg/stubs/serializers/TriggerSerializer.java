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

package org.fosstrak.reader.rp.proxy.msg.stubs.serializers;


/**
 * Trigger objects serialize a command on a Trigger into a String
 * representation.
 * 
 * @author Andreas Fürer, ETH Zurich Switzerland, Winter 2005/06
 * 
 */
public interface TriggerSerializer {

	/**
	 * @param name
	 *            The name of the trigger
	 * @param type
	 *            The type of the trigger ((Continuous, Timer, IOEdge, IOValue))
	 * @param value
	 *            The value of the trigger
	 */
	public String create(final String name, final String type,
			final String value);

	/**
	 */
	public String getMaxNumberSupported();

	/**
	 */
	public String getName();

	/**
	 */
	public String getType();

	/**
	 */
	public String getValue();

	/**
	 */
	public String fire();

	/**
	 * Serializes a Trigger command.
	 */
	public String serializeCommand();
}
