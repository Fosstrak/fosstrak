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

/**
 * 
 */
package org.accada.reader.rp.proxy.msg.stubs.serializers.text;

import org.accada.reader.rp.proxy.msg.stubs.serializers.TriggerSerializer;

/**
 * @author Andreas
 * 
 */
public class TriggerSerializerImpl extends CommandSerializerImpl implements
		TriggerSerializer {

	/**
	 * @param targetName
	 */
	public TriggerSerializerImpl(String targetName) {
		super(targetName);
		init();
	}

	/**
	 * @param id
	 */
	public TriggerSerializerImpl(int id) {
		super(id);
		init();
	}

	/**
	 * @param id
	 * @param targetName
	 */
	public TriggerSerializerImpl(int id, String targetName) {
		super(id, targetName);
		init();
	}

	private void init() {
		if (shortMode) {
			setObjectTypeName(TextTokens.TRG);
		} else {
			setObjectTypeName(TextTokens.TRIGGER);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.TriggerSerializer#create(java.lang.String)
	 */
	public String create(final String name, final String type,
			final String value) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_C);
		} else {
			setCommandName(TextTokens.CMD_CREATE);
		}
		setParameter(name + ", " + type + ", " + value);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.TriggerSerializer#getMaxNumberSupported()
	 */
	public String getMaxNumberSupported() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GMAX);
		} else {
			setCommandName(TextTokens.CMD_GET_MAX_NUMBER_SUPPORTED);
		}
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.TriggerSerializer#getName()
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
	 * @see org.accada.reader.testclient.command.TriggerSerializer#getType()
	 */
	public String getType() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GT);
		} else {
			setCommandName(TextTokens.CMD_GET_TYPE);
		}
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.TriggerSerializer#getValue()
	 */
	public String getValue() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GV);
		} else {
			setCommandName(TextTokens.CMD_GET_VALUE);
		}
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.TriggerSerializer#fire()
	 */
	public String fire() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_F);
		} else {
			setCommandName(TextTokens.CMD_FIRE);
		}
		return serializeCommand();
	}

	/**
	 * Serializes an Trigger command.
	 */
	public String serializeCommand() {
		return super.serializeCommand();
	}
}
