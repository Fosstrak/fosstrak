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


public interface NotificationChannel {

	/**
	 * Returns the name of the given NotificationChannel object.
	 * @return The name of the NotificationChannel
	 */
	String getName() throws RPProxyException;

	/**
	 * Returns the address to which this NotificationChannel object sends its
	 * notifications as specified by either commands NotificationChannel.create
	 * or NotificationChannel.setAddress.
	 * @return The address of the host
	 */
	String getAddress() throws RPProxyException;

	/**
	 * Returns the effective address to which this NotificationChannel object
	 * sends its notifications.
	 * @return The effective address of the host
	 */
	String getEffectiveAddress() throws RPProxyException;

	/**
	 * Sets the address to which this NotificationChannel object sends its
	 * notifications.
	 * @param addr
	 *           The address of the host
	 * @return If connect mode was specified, then the return value does not
	 *         apply and the Reader returns zero. Otherwise, if listen mode was
	 *         specified, then the return returns the port number assigned by the
	 *         Reader to listen for host NotificationChannel connections.
	 */
	int setAddress(final String addr) throws RPProxyException;

	/**
	 * Returns the DataSelector currently associated with this
	 * NotificationChannel object. If there is no DataSelector object associated,
	 * the error ERROR_DATASELECTOR_NOT_FOUND is raised.
	 * @return The dataSelector associated to this notification channel
	 * @throws RPProxyException
	 *            "ERROR_DATASELECTOR_NOT_FOUND"
	 */
	DataSelector getDataSelector() throws RPProxyException;

	/**
	 * Sets the DataSelector object to be used with this NotificationChannel.
	 * @param dataSelector
	 *           The dataSelector to use
	 */
	void setDataSelector(final DataSelector dataSelector) throws RPProxyException;

	/**
	 * Adds the specified Sources to the list of Sources currently associated
	 * with this NotificationChannel. If some of the Sources to be added are
	 * already associated with this NotificationChannel, only the not yet
	 * associated Sources shall be added and the command completes successfully.
	 * @param sourceList
	 *           The sources to add
	 */
	void addSources(final Source[] sourceList) throws RPProxyException;

	/**
	 * Removes the specified Sources from the list of Sources currently
	 * associated with this NotificationChannel.
	 * @param sourceList
	 *           The sources to remove
	 */
	void removeSources(final Source[] sourceList) throws RPProxyException;

	/**
	 * Removes all Sources currently associated with this NotificationChannel.
	 */
	void removeAllSources() throws RPProxyException;

	/**
	 * Returns the Source with the specified name currently associated with this
	 * NotificationChannel object. If no Source object with the given name
	 * exists, the error ERROR_SOURCE_NOT_FOUND is raised.
	 * @param name
	 *           The name of the source
	 * @return The instance of the source
	 * @throws RPProxyException
	 *            "ERROR_SOURCE_NOT_FOUND"
	 */
	Source getSource(final String name) throws RPProxyException;

	/**
	 * Returns all Sources currently associated with this NotificationChannel
	 * object. If no Sources are currently associated with this object, the
	 * command completes successfully and an empty list will be returned.
	 * @return A list of sources
	 */
	Source[] getAllSources() throws RPProxyException;

	/**
	 * Adds the specified Triggers to the list of Notification Triggers currently
	 * associated with this NotificationChannel. If some of the Triggers to be
	 * added are already associated with this NotificationChannel, only the not
	 * yet associated Triggers shall be added and the command completes
	 * successfully. Once a Trigger has been added, it is immediately activated.
	 * @param triggerList
	 *           The triggers to add
	 * @throws RPProxyException
	 *            "ERROR_TOO_MANY_TRIGGERS"
	 */
	void addNotificationTriggers(final Trigger[] triggerList)
			throws RPProxyException;

	/**
	 * Removes the specified Triggers from the list of Notification Triggers
	 * currently associated with this NotificationChannel. Once a Trigger isnt
	 * associated to any object anymore, it is deactivated.
	 * @param triggerList
	 *           The triggers to remove
	 */
	void removeNotificationTriggers(final Trigger[] triggerList) throws RPProxyException;

	/**
	 * Removes all Triggers currently associated with this NotificationChannel.
	 */
	void removeAllNotificationTriggers() throws RPProxyException;

	/**
	 * Returns the Trigger with the specified name currently associated with this
	 * NotificationChannel object. If no Trigger object with the given name
	 * exists, the error ERROR_TRIGGER_NOT_FOUND is raised.
	 * @param name
	 *           The name of the notification trigger
	 * @return The notification trigger
	 * @throws RPProxyException
	 *            "ERROR_TRIGGER_NOT_FOUND"
	 */
	Trigger getNotificationTrigger(final String name) throws RPProxyException;

	/**
	 * Returns all Triggers currently associated with this NotificationChannel
	 * object. If no Triggers are currently associated with this object, the
	 * command will complete successfully and an empty list will be returned.
	 * @return A list of triggers
	 */
	Trigger[] getAllNotificationTriggers() throws RPProxyException;

	/**
	 * This command returns the data currently queued for delivery in the report
	 * buffer. What data is reported depends on the DataSelector associated with
	 * the NotificationChannel.
	 * @param clearBuffer
	 *           An optional flag to indicate if the report buffer should be
	 *           cleared after the ReadReport is returned
	 * @return The resulting ReadReport
	 */
	ReadReport readQueuedData(final boolean clearBuffer) throws RPProxyException;

	/**
	 * This command returns the data currently queued for delivery in the report
	 * buffer. What data is reported depends on the DataSelector associated with
	 * the NotificationChannel.
	 * @param trigger
	 *           The trigger that cause this readQueuedData request
	 * @param clearBuffer
	 *           An optional flag to indicate if the report buffer should be
	 *           cleared after the ReadReport is returned
	 * @return The resulting ReadReport
	 */
	ReadReport readQueuedData(final boolean clearBuffer, final Trigger trigger) throws RPProxyException;

	int getMaxNumberSupported() throws RPProxyException;

}