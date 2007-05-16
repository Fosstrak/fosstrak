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

package org.accada.reader.rp.proxy.msg.stubs.serializers;

import org.accada.reader.rp.proxy.msg.stubs.DataSelector;
import org.accada.reader.rp.proxy.msg.stubs.Source;
import org.accada.reader.rp.proxy.msg.stubs.Trigger;

/**
 * NotificationChannelSerializer objects serialize a command on a
 * NotificationChannel into a String representation.
 * 
 * @author Andreas Fürer, ETH Zurich Switzerland, Winter 2005/06
 */
public interface NotificationChannelSerializer {

	public String create(final String name, final String addr);

	public String getName();

	public String getAddress();

	public String getEffectiveAddress();

	/**
	 * @param addr
	 *            The address of the host
	 */
	public String setAddress(final String addr);

	public String getDataSelector();

	/**
	 * @param dataSelector
	 */
	public String setDataSelector(final DataSelector dataSelector);

	/**
	 * @param sourceList
	 *            The sources to add
	 */
	public String addSources(final Source[] sourceList);

	/**
	 * @param sourceList
	 *            The sources to remove
	 */
	public String removeSources(final Source[] sourceList);

	/**
	 * Removes all Sources currently associated with this NotificationChannel.
	 */
	public String removeAllSources();

	/**
	 * @param name
	 *            The name of the source
	 */
	public String getSource(final String name);

	/**
	 * @return A list of sources
	 */
	public String getAllSources();

	/**
	 * @param triggerList
	 *            The triggers to add
	 */
	public String addNotificationTriggers(final Trigger[] triggerList);

	/**
	 * @param triggerList
	 *            The triggers to remove
	 */
	public String removeNotificationTriggers(final Trigger[] triggerList);

	public String removeAllNotificationTriggers();

	/**
	 * @param name
	 *            The name of the notification trigger
	 */
	public String getNotificationTrigger(final String name);

	/**
	 * @return A list of triggers
	 */
	public String getAllNotificationTriggers();

	/**
	 * @param clearBuffer
	 *            An optional flag to indicate if the report buffer should be
	 *            cleared after the ReadReport is returned
	 */
	public String readQueuedData(final boolean clearBuffer);

	/**
	 */
	public String readQueuedData();

	/**
	 * Serializes a FieldName command.
	 */
	public String serializeCommand();

}
