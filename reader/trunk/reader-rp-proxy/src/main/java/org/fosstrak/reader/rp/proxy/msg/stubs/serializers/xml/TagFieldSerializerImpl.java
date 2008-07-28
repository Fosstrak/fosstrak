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

import java.math.BigInteger;

import org.fosstrak.reader.rp.proxy.msg.stubs.serializers.TagFieldSerializer;
import org.fosstrak.reader.rprm.core.msg.command.TagFieldCommand;

/**
 * @author Andreas
 * 
 */
public class TagFieldSerializerImpl extends CommandSerializerImpl implements
		TagFieldSerializer {

	private TagFieldCommand tfCommand = null;

	/**
	 * @param targetName
	 */
	public TagFieldSerializerImpl(String targetName) {
		super(targetName);
		init();
	}

	/**
	 * @param id
	 */
	public TagFieldSerializerImpl(int id) {
		super(id);
		init();
	}

	/**
	 * @param id
	 * @param targetName
	 */
	public TagFieldSerializerImpl(int id, String targetName) {
		super(id, targetName);
		init();
	}

	private void init() {
		tfCommand = cmdFactory.createTagFieldCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.TagFieldSerializer#create(java.lang.String)
	 */
	public String create(String name) {
		resetCommand();
		TagFieldCommand.Create c = cmdFactory
				.createTagFieldCommandCreate();
		c.setName(name);
		tfCommand.setCreate(c);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.TagFieldSerializer#getName()
	 */
	public String getName() {
		resetCommand();
		tfCommand.setGetName(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.TagFieldSerializer#getTagFieldName()
	 */
	public String getTagFieldName() {
		resetCommand();
		tfCommand.setGetTagFieldName(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.TagFieldSerializer#setTagFieldName(java.lang.String)
	 */
	public String setTagFieldName(String pTagFieldName) {
		resetCommand();
		TagFieldCommand.SetTagFieldName tagFieldName = cmdFactory
				.createTagFieldCommandSetTagFieldName();
		tagFieldName.setTagFieldName(pTagFieldName);
		tfCommand.setSetTagFieldName(tagFieldName);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.TagFieldSerializer#getMemoryBank()
	 */
	public String getMemoryBank() {
		resetCommand();
		tfCommand.setGetMemoryBank(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.TagFieldSerializer#setMemoryBank(int)
	 */
	public String setMemoryBank(int pMemoryBank) {
		resetCommand();
		TagFieldCommand.SetMemoryBank memoryBank = cmdFactory
				.createTagFieldCommandSetMemoryBank();
		memoryBank.setMemoryBank(BigInteger.valueOf(pMemoryBank));
		tfCommand.setSetMemoryBank(memoryBank);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.TagFieldSerializer#getOffset()
	 */
	public String getOffset() {
		resetCommand();
		tfCommand.setGetOffset(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.TagFieldSerializer#setOffset(int)
	 */
	public String setOffset(int pOffset) {
		resetCommand();
		TagFieldCommand.SetOffset offset = cmdFactory
				.createTagFieldCommandSetOffset();
		offset.setOffset(pOffset);
		tfCommand.setSetOffset(offset);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.TagFieldSerializer#getLength()
	 */
	public String getLength() {
		resetCommand();
		tfCommand.setGetLength(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.TagFieldSerializer#setLength(int)
	 */
	public String setLength(int pLength) {
		resetCommand();
		TagFieldCommand.SetLength length = cmdFactory
				.createTagFieldCommandSetLength();
		length.setLength(pLength);
		tfCommand.setSetLength(length);
		return serializeCommand();
	}

	/**
	 * Serializes an TagField command.
	 */
	public String serializeCommand() {
		command.setTagField(tfCommand);
		return super.serializeCommand();
	}

}
