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

/**
 * 
 */
package org.fosstrak.reader.rp.proxy.msg.stubs.serializers.text;

import java.util.Date;

import org.fosstrak.reader.rp.proxy.msg.stubs.DataSelector;
import org.fosstrak.reader.rp.proxy.msg.stubs.NotificationChannel;
import org.fosstrak.reader.rp.proxy.msg.stubs.Source;
import org.fosstrak.reader.rp.proxy.msg.stubs.TagField;
import org.fosstrak.reader.rp.proxy.msg.stubs.TagSelector;
import org.fosstrak.reader.rp.proxy.msg.stubs.Trigger;
import org.fosstrak.reader.rp.proxy.msg.stubs.serializers.ReaderDeviceSerializer;

/**
 * @author Andreas
 * 
 */
public class ReaderDeviceSerializerImpl extends CommandSerializerImpl implements
ReaderDeviceSerializer {
	
	
	
	/**
	 * @param targetName
	 */
	public ReaderDeviceSerializerImpl(String targetName) {
		super(targetName);
		init();
	}
	
	/**
	 * @param id
	 */
	public ReaderDeviceSerializerImpl(int id) {
		super(id);
		init();
	}
	
	/**
	 * @param id
	 * @param targetName
	 */
	public ReaderDeviceSerializerImpl(int id, String targetName) {
		super(id, targetName);
		init();
	}
	
	private void init() {
		if (shortMode) {
			setObjectTypeName(TextTokens.RD);
		} else {
			setObjectTypeName(TextTokens.READERDEVICE);
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#getEPC()
	 */
	public String getEPC() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GE);
		} else {
			setCommandName(TextTokens.CMD_GETEPC);
		}
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#getManufacturer()
	 */
	public String getManufacturer() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GMAN);
		} else {
			setCommandName(TextTokens.CMD_GETMANUFACTURER);
		}
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#getModel()
	 */
	public String getModel() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GM);
		} else {
			setCommandName(TextTokens.CMD_GETMODEL);
		}
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#getHandle()
	 */
	public String getHandle() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GH);
		} else {
			setCommandName(TextTokens.CMD_GETHANDLE);
		}
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#setHandle(int)
	 */
	public String setHandle(int pHandle) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_SH);
		} else {
			setCommandName(TextTokens.CMD_SETHANDLE);
		}
		setParameter(pHandle);
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#getName()
	 */
	public String getName() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GN);
		} else {
			setCommandName(TextTokens.CMD_GETNAME);
		}
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#setName(java.lang.String)
	 */
	public String setName(String pName) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_SN);
		} else {
			setCommandName(TextTokens.CMD_SETNAME);
		}
		setParameter(pName);
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#getRole()
	 */
	public String getRole() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GR);
		} else {
			setCommandName(TextTokens.CMD_GETROLE);
		}
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#setRole(java.lang.String)
	 */
	public String setRole(String pRole) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_SR);
		} else {
			setCommandName(TextTokens.CMD_SETROLE);
		}
		setParameter(pRole);
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#getTimeTicks()
	 */
	public String getTimeTicks() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GTIC);
		} else {
			setCommandName(TextTokens.CMD_GET_TIME_TICKS);
		}
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#getTimeUTC()
	 */
	public String getTimeUTC() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GUTC);
		} else {
			setCommandName(TextTokens.CMD_GET_TIME_UTC);
		}
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#setTimeUTC(java.util.Date)
	 */
	public String setTimeUTC(Date utc) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_SUTC);
		} else {
			setCommandName(TextTokens.CMD_SET_TIME_UTC);
		}
		setParameter(utc);
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#getManufacturerDescription()
	 */
	public String getManufacturerDescription() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GMD);
		} else {
			setCommandName(TextTokens.CMD_GET_MANUFACTURER_DESCRIPTION);
		}
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#getCurrentSource()
	 */
	public String getCurrentSource() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GCS);
		} else {
			setCommandName(TextTokens.CMD_GET_CURRENT_SOURCE);
		}
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#setCurrentSource(org.fosstrak.reader.Source)
	 */
	public String setCurrentSource(Source currentSource) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_SCS);
		} else {
			setCommandName(TextTokens.CMD_SET_CURRENT_SOURCE);
		}
		setParameter(currentSource.getName());
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#getCurrentDataSelector()
	 */
	public String getCurrentDataSelector() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GCDS);
		} else {
			setCommandName(TextTokens.CMD_GET_CURRENT_DATA_SELECTOR);
		}
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#setCurrentDataSelector(org.fosstrak.reader.testclient.command.DataSelectorSerializer)
	 */
	public String setCurrentDataSelector(DataSelector currentDataSelector) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_SCDS);
		} else {
			setCommandName(TextTokens.CMD_SET_CURRENT_DATA_SELECTOR);
		}
		setParameter(currentDataSelector.getName());
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#removeSources(org.fosstrak.reader.Source[])
	 */
	public String removeSources(Source[] pSources) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_RSRC);
		} else {
			setCommandName(TextTokens.CMD_REMOVE_SOURCES);
		}
		setParameterList(pSources);
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#removeAllSources()
	 */
	public String removeAllSources() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_RASRC);
		} else {
			setCommandName(TextTokens.CMD_REMOVE_ALL_SOURCES);
		}
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#getSource(java.lang.String)
	 */
	public String getSource(String name) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GSRC);
		} else {
			setCommandName(TextTokens.CMD_GET_SOURCE);
		}
		setParameter(name);
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#getAllSources()
	 */
	public String getAllSources() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GASRC);
		} else {
			setCommandName(TextTokens.CMD_GET_ALL_SOURCES);
		}
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#removeDataSelectors(org.fosstrak.reader.testclient.command.DataSelectorSerializer[])
	 */
	public String removeDataSelectors(DataSelector[] dataSelectors) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_RDS);
		} else {
			setCommandName(TextTokens.CMD_REMOVE_DATA_SELECTORS);
		}
		setParameterList(dataSelectors);
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#removeAllDataSelectors()
	 */
	public String removeAllDataSelectors() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_RADS);
		} else {
			setCommandName(TextTokens.CMD_REMOVE_ALL_DATA_SELECTORS);
		}
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#getDataSelector(java.lang.String)
	 */
	public String getDataSelector(String name) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GDS);
		} else {
			setCommandName(TextTokens.CMD_GET_DATA_SELECTOR);
		}
		setParameter(name);
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#getAllDataSelectors()
	 */
	public String getAllDataSelectors() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GADS);
		} else {
			setCommandName(TextTokens.CMD_GET_ALL_DATA_SELECTORS);
		}
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#removeNotificationChannels(org.fosstrak.reader.NotificationChannel[])
	 */
	public String removeNotificationChannels(
			NotificationChannel[] notificationChannels) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_RNC);
		} else {
			setCommandName(TextTokens.CMD_REMOVE_NOTIFICATION_CHANNELS);
		}
		setParameterList(notificationChannels);
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#removeAllNotificationChannels()
	 */
	public String removeAllNotificationChannels() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_RANC);
		} else {
			setCommandName(TextTokens.CMD_REMOVE_ALL_NOTIFICATION_CHANNELS);
		}
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#getNotificationChannel(java.lang.String)
	 */
	public String getNotificationChannel(String name) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GNC);
		} else {
			setCommandName(TextTokens.CMD_GET_NOTIFICATION_CHANNEL);
		}
		setParameter(name);
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#getAllNotificationChannels()
	 */
	public String getAllNotificationChannels() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GANC);
		} else {
			setCommandName(TextTokens.CMD_GET_ALL_NOTIFICATION_CHANNELS);
		}
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#removeTriggers(org.fosstrak.reader.Trigger[])
	 */
	public String removeTriggers(Trigger[] triggerList) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_RTRG);
		} else {
			setCommandName(TextTokens.CMD_REMOVE_TRIGGERS);
		}
		setParameterList(triggerList);
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#removeAllTriggers()
	 */
	public String removeAllTriggers() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_RATRG);
		} else {
			setCommandName(TextTokens.CMD_REMOVE_ALL_TRIGGERS);
		}
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#getTrigger(java.lang.String)
	 */
	public String getTrigger(String name) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GTRG);
		} else {
			setCommandName(TextTokens.CMD_GET_ALL_TRIGGERS);
		}
		setParameter(name);
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#getAllTriggers()
	 */
	public String getAllTriggers() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GATRG);
		} else {
			setCommandName(TextTokens.CMD_GET_ALL_TRIGGERS);
		}
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#removeTagSelectors(org.fosstrak.reader.TagSelector[])
	 */
	public String removeTagSelectors(TagSelector[] tagSelectors) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_RTS);
		} else {
			setCommandName(TextTokens.CMD_REMOVE_TAG_SELECTORS);
		}
		setParameterList(tagSelectors);
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#removeAllTagSelectors()
	 */
	public String removeAllTagSelectors() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_RATS);
		} else {
			setCommandName(TextTokens.CMD_REMOVE_ALL_TAG_SELECTORS);
		}
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#getTagSelector(java.lang.String)
	 */
	public String getTagSelector(String name) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GTS);
		} else {
			setCommandName(TextTokens.CMD_GET_TAG_SELECTOR);
		}
		setParameter(name);
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#getAllTagSelectors()
	 */
	public String getAllTagSelectors() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GATS);
		} else {
			setCommandName(TextTokens.CMD_GET_ALL_TAG_SELECTORS);
		}
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#removeTagFields(org.fosstrak.reader.TagField[])
	 */
	public String removeTagFields(TagField[] tagFields) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_RTF);
		} else {
			setCommandName(TextTokens.CMD_REMOVE_TAG_FIELDS);
		}
		setParameterList(tagFields);
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#removeAllTagFields()
	 */
	public String removeAllTagFields() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_RATF);
		} else {
			setCommandName(TextTokens.CMD_REMOVE_ALL_TAG_FIELDS);
		}
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#getTagField(java.lang.String)
	 */
	public String getTagField(String name) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GTF);
		} else {
			setCommandName(TextTokens.CMD_GET_TAG_FIELD);
		}
		setParameter(name);
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#getAllTagFields()
	 */
	public String getAllTagFields() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GATF);
		} else {
			setCommandName(TextTokens.CMD_GET_ALL_TAG_FIELDS);
		}
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#resetToDefaultSettings()
	 */
	public String resetToDefaultSettings() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_RESET);
		} else {
			setCommandName(TextTokens.CMD_RESET_TO_DEFAULT_SETTINGS);
		}
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#reboot()
	 */
	public String reboot() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_BOOT);
		} else {
			setCommandName(TextTokens.CMD_REBOOT);
		}
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#goodbye()
	 */
	public String goodbye() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_BYE);
		} else {
			setCommandName(TextTokens.CMD_GOODBYE);
		}
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#getReadPoint(java.lang.String)
	 */
	public String getReadPoint(String name) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GRP);
		} else {
			setCommandName(TextTokens.CMD_GET_READ_POINT);
		}
		setParameter(name);
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.ReaderDeviceSerializer#getAllReadPoints()
	 */
	public String getAllReadPoints() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GARP);
		} else {
			setCommandName(TextTokens.CMD_GET_ALL_READ_POINTS);
		}
		return serializeCommand();
	}
	
	/**
	 * Serializes an ReaderDevice command.
	 */
	public String serializeCommand() {
		return super.serializeCommand();
	}
	
}
