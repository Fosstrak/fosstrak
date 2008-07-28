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

import org.fosstrak.reader.rp.proxy.msg.stubs.serializers.EventTypeSerializer;

/**
 * @author Andreas
 *
 */
public class EventTypeSerializerImpl extends CommandSerializerImpl implements EventTypeSerializer {

	
	public EventTypeSerializerImpl(int id, String targetName) {
		super(id, targetName);
		init();
	}
	
	public EventTypeSerializerImpl(String targetName) {
		super(targetName);
		init();
	}
	
	private void init() {
		if (shortMode) {
			setObjectTypeName(TextTokens.ET);
		} else {
			setObjectTypeName(TextTokens.EVENTTYPE);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.fosstrak.reader.testclient.command.EventTypeSerializer#getSupportedTypes()
	 */
	public String getSupportedTypes() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GST);
		} else {
			setCommandName(TextTokens.CMD_GET_SUPPORTED_TYPES);
		}
		return serializeCommand();
	}
	
	/**
	 * Serializes an EventType command.
	 */
	public String serializeCommand() {
		return super.serializeCommand();
	}


}
