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
package org.fosstrak.reader.rp.proxy.msg.stubs.serializers.xml;

import org.fosstrak.reader.rp.proxy.msg.stubs.serializers.FieldNameSerializer;
import org.fosstrak.reader.rprm.core.msg.command.FieldNameCommand;

/**
 * @author Andreas
 *
 */
public class FieldNameSerializerImpl extends CommandSerializerImpl implements
		FieldNameSerializer {

	private FieldNameCommand fnCommand = null;
	
	/**
	 * @param targetName
	 */
	public FieldNameSerializerImpl(String targetName) {
		super(targetName);
		init();
	}

	/**
	 * @param id
	 */
	public FieldNameSerializerImpl(int id) {
		super(id);
		init();
	}

	/**
	 * @param id
	 * @param targetName
	 */
	public FieldNameSerializerImpl(int id, String targetName) {
		super(id, targetName);
		init();
	}
	
	private void init() {
		fnCommand = cmdFactory.createFieldNameCommand();
	}

	/* (non-Javadoc)
	 * @see org.fosstrak.reader.testclient.command.FieldNameSerializer#getSupportedNames()
	 */
	public String getSupportedNames() {
		resetCommand();
		fnCommand.setGetSupportedNames(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/**
	 * Serializes an FieldName command.
	 */
	public String serializeCommand() {
		command.setFieldName(fnCommand);
		return super.serializeCommand();
	}
}
