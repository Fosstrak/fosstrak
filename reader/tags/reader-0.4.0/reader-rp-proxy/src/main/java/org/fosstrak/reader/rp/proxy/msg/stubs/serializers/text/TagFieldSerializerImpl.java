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

import org.accada.reader.rp.proxy.msg.stubs.serializers.TagFieldSerializer;

/**
 * @author Andreas
 * 
 */
public class TagFieldSerializerImpl extends CommandSerializerImpl implements
		TagFieldSerializer {

	
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
		if (shortMode) {
			setObjectTypeName(TextTokens.TF);
		} else {
			setObjectTypeName(TextTokens.TAGFIELD);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.TagFieldSerializer#create(java.lang.String)
	 */
	public String create(String name) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_C);
		} else {
			setCommandName(TextTokens.CMD_CREATE);
		}
		setParameter(name);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.TagFieldSerializer#getName()
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
	 * @see org.accada.reader.testclient.command.TagFieldSerializer#getTagFieldName()
	 */
	public String getTagFieldName() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GTFN);
		} else {
			setCommandName(TextTokens.CMD_GET_TAG_FIELD_NAME);
		}
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.TagFieldSerializer#setTagFieldName(java.lang.String)
	 */
	public String setTagFieldName(String pTagFieldName) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_STFN);
		} else {
			setCommandName(TextTokens.CMD_SET_TAG_FIELD_NAME);
		}
		setParameter(pTagFieldName);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.TagFieldSerializer#getMemoryBank()
	 */
	public String getMemoryBank() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GMB);
		} else {
			setCommandName(TextTokens.CMD_GET_MEMORY_BANK);
		}
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.TagFieldSerializer#setMemoryBank(int)
	 */
	public String setMemoryBank(int pMemoryBank) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_SMB);
		} else {
			setCommandName(TextTokens.CMD_SET_MEMORY_BANK);
		}
		setParameter(pMemoryBank);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.TagFieldSerializer#getOffset()
	 */
	public String getOffset() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GOFF);
		} else {
			setCommandName(TextTokens.CMD_GET_OFFSET);
		}
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.TagFieldSerializer#setOffset(int)
	 */
	public String setOffset(int pOffset) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_SOFF);
		} else {
			setCommandName(TextTokens.CMD_SET_OFFSET);
		}
		setParameter(pOffset);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.TagFieldSerializer#getLength()
	 */
	public String getLength() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GLEN);
		} else {
			setCommandName(TextTokens.CMD_GET_LENGTH);
		}
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.TagFieldSerializer#setLength(int)
	 */
	public String setLength(int pLength) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_SLEN);
		} else {
			setCommandName(TextTokens.CMD_SET_LENGTH);
		}
		setParameter(pLength);
		return serializeCommand();
	}

	/**
	 * Serializes an TagField command.
	 */
	public String serializeCommand() {
		return super.serializeCommand();
	}

}
