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

import org.fosstrak.reader.rp.proxy.msg.stubs.serializers.TriggerTypeSerializer;

/**
 * @author Andreas
 * 
 */
public class TriggerTypeSerializerImpl extends CommandSerializerImpl implements
		TriggerTypeSerializer {

	
	/**
	 * @param targetName
	 */
	public TriggerTypeSerializerImpl(String targetName) {
		super(targetName);
		init();
	}

	/**
	 * @param id
	 */
	public TriggerTypeSerializerImpl(int id) {
		super(id);
		init();
	}

	/**
	 * @param id
	 * @param targetName
	 */
	public TriggerTypeSerializerImpl(int id, String targetName) {
		super(id, targetName);
		init();
	}

	private void init() {
		if (shortMode) {
			setObjectTypeName(TextTokens.TT);
		} else {
			setObjectTypeName(TextTokens.TRIGGERTYPE);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.TriggerTypeSerializer#getSupportedTypes()
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
	 * Serializes a TriggerType command.
	 */
	public String serializeCommand() {
		return super.serializeCommand();
	}

}
