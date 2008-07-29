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



public interface ReaderDevice {

	public String getEPC() throws RPProxyException;
	
	public String getManufacturer() throws RPProxyException;

	public String getModel() throws RPProxyException;

	public int getHandle() throws RPProxyException;
	
	public void setHandle(int handle) throws RPProxyException;
	
	public String getName() throws RPProxyException;

	public void setName(String name) throws RPProxyException;

	public String getRole() throws RPProxyException;
	
	public void setRole(String role) throws RPProxyException;
	
	public int getTimeTicks() throws RPProxyException;

	public String getTimeUTC() throws RPProxyException;
	
	public void setTimeUTC(String time) throws RPProxyException;
	
	public String getManufacturerDescription() throws RPProxyException;
	
	public Source getCurrentSource() throws RPProxyException;

	public void setCurrentSource(Source source) throws RPProxyException;
	
	public DataSelector getCurrentDataSelector() throws RPProxyException;

	public void setCurrentDataSelector(DataSelector dataSelector) throws RPProxyException;

	public void removeSources(Source[] sources) throws RPProxyException;
	
	public void removeAllSources() throws RPProxyException;
	
	public Source getSource(String sourceName) throws RPProxyException;
	
	public Source[] getAllSources() throws RPProxyException;

	public void removeDataSelectors(DataSelector[] dataSelectors) throws RPProxyException;
	
	public void removeAllDataSelectors() throws RPProxyException;
	
	public DataSelector getDataSelector(String dataSelectorName) throws RPProxyException;

	public DataSelector[] getAllDataSelectors() throws RPProxyException;

	public void removeNotificationChannels(NotificationChannel[] notificationChannels) throws RPProxyException;
	
	public void removeAllNotificationChannels() throws RPProxyException;
	
	public NotificationChannel getNotificationChannel(String notificationChannelName) throws RPProxyException;

	public NotificationChannel[] getAllNotificationChannels() throws RPProxyException;

	public void removeTriggers(Trigger[] triggers) throws RPProxyException;
	
	public void removeAllTriggers() throws RPProxyException;
	
	public Trigger getTrigger(String triggerName) throws RPProxyException;

	public Trigger[] getAllTriggers() throws RPProxyException;

	public void removeTagSelectors(TagSelector[] tagSelectors) throws RPProxyException;
	
	public void removeAllTagSelectors() throws RPProxyException;
	
	public TagSelector getTagSelector(String tagSelectorName) throws RPProxyException;

	public TagSelector[] getAllTagSelectors() throws RPProxyException;

	public void removeTagFields(TagField[] tagFields) throws RPProxyException;
	
	public void removeAllTagFields() throws RPProxyException;
	
	public TagField getTagField(String tagFieldName) throws RPProxyException;

	public TagField[] getAllTagFields() throws RPProxyException;

	public void resetToDefaultSettings() throws RPProxyException;

	public void reboot() throws RPProxyException;

	public void goodbye() throws RPProxyException;

	public ReadPoint getReadPoint(String readPointName) throws RPProxyException;

	public ReadPoint[] getAllReadPoints() throws RPProxyException;

}