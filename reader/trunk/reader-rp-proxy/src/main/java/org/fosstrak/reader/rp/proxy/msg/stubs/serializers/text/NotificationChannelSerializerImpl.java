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

import org.fosstrak.reader.rp.proxy.msg.stubs.DataSelector;
import org.fosstrak.reader.rp.proxy.msg.stubs.Source;
import org.fosstrak.reader.rp.proxy.msg.stubs.Trigger;
import org.fosstrak.reader.rp.proxy.msg.stubs.serializers.NotificationChannelSerializer;

/**
 * @author Andreas
 *
 */
public class NotificationChannelSerializerImpl extends CommandSerializerImpl
		implements NotificationChannelSerializer {

	/**
	 * @param targetName
	 */
	public NotificationChannelSerializerImpl(String targetName) {
		super(targetName);
		init();
	}

	/**
	 * @param id
	 */
	public NotificationChannelSerializerImpl(int id) {
		super(id);
		init();
	}

	/**
	 * @param id
	 * @param targetName
	 */
	public NotificationChannelSerializerImpl(int id, String targetName) {
		super(id, targetName);
		init();
	}
	
	private void init() {
		if (shortMode) {
			setObjectTypeName(TextTokens.NC);
		} else {
			setObjectTypeName(TextTokens.NOTIFICATIONCHANNEL);
		}

	}
	

	/* (non-Javadoc)
	 * @see org.fosstrak.reader.testclient.command.NotificationChannelSerializer#create(java.lang.String, java.lang.String)
	 */
	public String create(String name, String addr) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_C);
		} else {
			setCommandName(TextTokens.CMD_CREATE);
		}
		setParameter(name + ", " + addr);
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.fosstrak.reader.testclient.command.NotificationChannelSerializer#getName()
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

	/* (non-Javadoc)
	 * @see org.fosstrak.reader.testclient.command.NotificationChannelSerializer#getAddress()
	 */
	public String getAddress() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GADR);
		} else {
			setCommandName(TextTokens.CMD_GET_ADDRESS);
		}
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.fosstrak.reader.testclient.command.NotificationChannelSerializer#getEffectiveAddress()
	 */
	public String getEffectiveAddress() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GEADR);
		} else {
			setCommandName(TextTokens.CMD_GET_EFFECTIVE_ADDRESS);
		}
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.fosstrak.reader.testclient.command.NotificationChannelSerializer#setAddress(java.lang.String)
	 */
	public String setAddress(String addr) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_SADR);
		} else {
			setCommandName(TextTokens.CMD_SET_ADDRESS);
		}
		setParameter(addr);
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.fosstrak.reader.testclient.command.NotificationChannelSerializer#getDataSelector()
	 */
	public String getDataSelector() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GDS);
		} else {
			setCommandName(TextTokens.CMD_GET_DATA_SELECTOR);
		}
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.fosstrak.reader.testclient.command.NotificationChannelSerializer#setDataSelector(org.fosstrak.reader.testclient.command.DataSelectorSerializer)
	 */
	public String setDataSelector(DataSelector dataSelector) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_SDS);
		} else {
			setCommandName(TextTokens.CMD_SET_DATA_SELECTOR);
		}
		setParameter(dataSelector.getName());
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.fosstrak.reader.testclient.command.NotificationChannelSerializer#addSources(org.fosstrak.reader.Source[])
	 */
	public String addSources(Source[] sourceList) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_ASRC);
		} else {
			setCommandName(TextTokens.CMD_ADD_SOURCES);
		}
		setParameterList(sourceList);
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.fosstrak.reader.testclient.command.NotificationChannelSerializer#removeSources(org.fosstrak.reader.Source[])
	 */
	public String removeSources(Source[] sourceList) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_RSRC);
		} else {
			setCommandName(TextTokens.CMD_REMOVE_SOURCES);
		}
		setParameterList(sourceList);
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.fosstrak.reader.testclient.command.NotificationChannelSerializer#removeAllSources()
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

	/* (non-Javadoc)
	 * @see org.fosstrak.reader.testclient.command.NotificationChannelSerializer#getSource(java.lang.String)
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

	/* (non-Javadoc)
	 * @see org.fosstrak.reader.testclient.command.NotificationChannelSerializer#getAllSources()
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

	/* (non-Javadoc)
	 * @see org.fosstrak.reader.testclient.command.NotificationChannelSerializer#addNotificationTriggers(org.fosstrak.reader.Trigger[])
	 */
	public String addNotificationTriggers(Trigger[] triggers) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_ANT);
		} else {
			setCommandName(TextTokens.CMD_ADD_NOTIFICATION_TRIGGERS);
		}
		setParameterList(triggers);
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.fosstrak.reader.testclient.command.NotificationChannelSerializer#removeNotificationTriggers(org.fosstrak.reader.Trigger[])
	 */
	public String removeNotificationTriggers(Trigger[] triggers) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_RNT);
		} else {
			setCommandName(TextTokens.CMD_REMOVE_ALL_NOTIFICATION_TRIGGERS);
		}
		setParameterList(triggers);
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.fosstrak.reader.testclient.command.NotificationChannelSerializer#removeAllNotificationTriggers()
	 */
	public String removeAllNotificationTriggers() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_RANT);
		} else {
			setCommandName(TextTokens.CMD_REMOVE_ALL_NOTIFICATION_TRIGGERS);
		}
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.fosstrak.reader.testclient.command.NotificationChannelSerializer#getNotificationTrigger(java.lang.String)
	 */
	public String getNotificationTrigger(String name) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GNT);
		} else {
			setCommandName(TextTokens.CMD_GET_NOTIFICATION_TRIGGER);
		}
		setParameter(name);
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.fosstrak.reader.testclient.command.NotificationChannelSerializer#getAllNotificationTriggers()
	 */
	public String getAllNotificationTriggers() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GANT);
		} else {
			setCommandName(TextTokens.CMD_GET_ALL_NOTIFICATION_TRIGGERS);
		}
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.fosstrak.reader.testclient.command.NotificationChannelSerializer#readQueuedData(boolean)
	 */
	public String readQueuedData(boolean clearBuffer) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_RQD);
		} else {
			setCommandName(TextTokens.CMD_READ_QUEUED_DATA);
		}
		setParameter(clearBuffer);
		return serializeCommand();
	}
	
	/* (non-Javadoc)
	 * @see org.fosstrak.reader.testclient.command.NotificationChannelSerializer#readQueuedData(boolean)
	 */
	public String readQueuedData() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_RQD);
		} else {
			setCommandName(TextTokens.CMD_READ_QUEUED_DATA);
		}
		return serializeCommand();
	}

	/**
	 * Serializes an NotificationChannel command.
	 */
	public String serializeCommand() {
		return super.serializeCommand();
	}

}
