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
package org.accada.reader.rp.proxy.msg.stubs.serializers.xml;

import org.accada.reader.rp.proxy.msg.stubs.serializers.TriggerTypeSerializer;
import org.accada.reader.rprm.core.msg.command.TriggerTypeCommand;

/**
 * @author Andreas
 * 
 */
public class TriggerTypeSerializerImpl extends CommandSerializerImpl implements
		TriggerTypeSerializer {

	private TriggerTypeCommand ttCommand = null;

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
		ttCommand = cmdFactory.createTriggerTypeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.TriggerTypeSerializer#getSupportedTypes()
	 */
	public String getSupportedTypes() {
		resetCommand();
		ttCommand.setGetSupportedTypes(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/**
	 * Serializes a TriggerType command.
	 */
	public String serializeCommand() {
		command.setTriggerType(ttCommand);
		return super.serializeCommand();
	}

}
