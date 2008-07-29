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

import java.util.Date;

import org.fosstrak.reader.rp.proxy.msg.stubs.DataSelector;
import org.fosstrak.reader.rp.proxy.msg.stubs.NotificationChannel;
import org.fosstrak.reader.rp.proxy.msg.stubs.Source;
import org.fosstrak.reader.rp.proxy.msg.stubs.TagField;
import org.fosstrak.reader.rp.proxy.msg.stubs.TagSelector;
import org.fosstrak.reader.rp.proxy.msg.stubs.Trigger;

/**
 * ReaderDeviceSerializer objects serialize a command on a ReaderDevice into a
 * String representation.
 * 
 * @author Andreas Fürer, ETH Zurich Switzerland, Winter 2005/06
 */
public interface ReaderDeviceSerializer {

	/**
	 * @return The EPC of the reader
	 */
	public String getEPC();

	/**
	 * @return The name of the reader manufacturer
	 */
	public String getManufacturer();

	/**
	 * @return The manufacturer-defined model description of the reader
	 */
	public String getModel();

	/**
	 */
	public String getHandle();

	/**
	 * @param handle
	 *            The new handle of the reader
	 */
	public String setHandle(final int handle);

	/**
	 * @return The name of the reader
	 */
	public String getName();

	/**
	 * @param name
	 *            The new name of the reader
	 */
	public String setName(final String name);

	/**
	 * @return The role of the reader
	 */
	public String getRole();

	/**
	 * @param role
	 *            The new role of the reader
	 */
	public String setRole(final String role);

	/**
	 */
	public String getTimeTicks();

	/**
	 * Returns the current absolute (UTC) time of the reader.
	 */
	public String getTimeUTC();

	/**
	 * @param utc
	 *            The new UTC of the reader
	 */
	public String setTimeUTC(final Date utc);

	/**
	 */
	public String getManufacturerDescription();

	
	/**
	 * 
	 */
	public String getCurrentSource();

	/**
	 * @param currentSource
	 *            The new current source
	 */
	public String setCurrentSource(final Source currentSource);

	/**
	 */
	public String getCurrentDataSelector();

	/**
	 * @param currentDataSelector
	 *            The new current DataSelector of the reader
	 */
	public String setCurrentDataSelector(
			final DataSelector currentDataSelector);

	/**
	 * @param sourceList
	 *            The list of sources to remove
	 */
	public String removeSources(final Source[] sourceList);

	/**
	 */
	public String removeAllSources();

	/**
	 * @param name
	 *            The name of the source
	 */
	public String getSource(final String name);

	/**
	 */
	public String getAllSources();

	/**
	 * @param dataSelectorList
	 *            The list of DataSelectors to remove
	 */
	public String removeDataSelectors(
			final DataSelector[] dataSelectorList);

	/**
	 */
	public String removeAllDataSelectors();

	/**
	 * @param name
	 *            The name of the DataSelector
	 */
	public String getDataSelector(final String name);

	/**
	 */
	public String getAllDataSelectors();

	/**
	 * @param notificationChannelList
	 *            The list of NotificationChannels to remove
	 */
	public String removeNotificationChannels(
			final NotificationChannel[] notificationChannelList);

	/**
	 */
	public String removeAllNotificationChannels();

	/**
	 * @param name
	 *            The name of the NotificationChannel
	 */
	public String getNotificationChannel(final String name);

	/**
	 * @return The list of NotificationChannels
	 */
	public String getAllNotificationChannels();

	/**
	 * @param triggerList
	 *            The list of Triggers to remove
	 */
	public String removeTriggers(final Trigger[] triggerList);

	/**
	 */
	public String removeAllTriggers();

	/**
	 * @param name
	 *            The name of the Trigger
	 */
	public String getTrigger(final String name);

	/**
	 * @return The list of Triggers
	 */
	public String getAllTriggers();

	/**
	 * @param tagSelectorList
	 *            The list of TagSelectors to remove
	 */
	public String removeTagSelectors(final TagSelector[] tagSelectorList);

	/**
	 */
	public String removeAllTagSelectors();

	/**
	 * @param name
	 *            The name of the TagSelector
	 */
	public String getTagSelector(final String name);

	/**
	 */
	public String getAllTagSelectors();

	/**
	 * @param tagFieldList
	 *            The list of TagFields to remove
	 */
	public String removeTagFields(final TagField[] tagFieldList);

	/**
	 */
	public String removeAllTagFields();

	/**
	 * @param name
	 *            The name of the TagField
	 */
	public String getTagField(final String name);

	/**
	 */
	public String getAllTagFields();

	/**
	 */
	public String resetToDefaultSettings();

	/**
	 */
	public String reboot();

	/**
	 */
	public String goodbye();

	/**
	 * @param name
	 *            The name of the ReadPoint
	 */
	public String getReadPoint(final String name);

	/**
	 */
	public String getAllReadPoints();

	/**
	 * Serializes a ReaderDevice command.
	 */
	public String serializeCommand();
}
