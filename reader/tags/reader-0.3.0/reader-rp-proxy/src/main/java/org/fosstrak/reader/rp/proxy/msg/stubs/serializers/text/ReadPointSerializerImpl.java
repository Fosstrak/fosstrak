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

import org.accada.reader.rp.proxy.msg.stubs.serializers.ReadPointSerializer;
import org.accada.reader.rprm.core.msg.command.ReadPointCommand;

/**
 * @author Andreas
 *
 */
public class ReadPointSerializerImpl extends CommandSerializerImpl implements
		ReadPointSerializer {

	private ReadPointCommand rpCommand = null;
	
	/**
	 * @param targetName
	 */
	public ReadPointSerializerImpl(String targetName) {
		super(targetName);
		init();
	}

	/**
	 * @param id
	 */
	public ReadPointSerializerImpl(int id) {
		super(id);
		init();
	}

	/**
	 * @param id
	 * @param targetName
	 */
	public ReadPointSerializerImpl(int id, String targetName) {
		super(id, targetName);
		init();
	}
	
	private void init() {
		if (shortMode) {
			setObjectTypeName(TextTokens.RP);
		} else {
			setObjectTypeName(TextTokens.READPOINT);
		}
	}

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.ReadPointSerializer#getName()
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
	
	/**
	 * Serializes an ReadPoint command.
	 */
	public String serializeCommand() {
		return super.serializeCommand();
	}

}
