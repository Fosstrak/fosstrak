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

import org.accada.reader.rp.proxy.msg.stubs.TagField;
import org.accada.reader.rp.proxy.msg.stubs.serializers.TagSelectorSerializer;
import org.accada.reader.rprm.core.msg.command.TagSelectorCommand;
import org.accada.reader.rprm.core.msg.util.HexUtil;

/**
 * @author Andreas
 *
 */
public class TagSelectorSerializerImpl extends CommandSerializerImpl implements
		TagSelectorSerializer {

	private TagSelectorCommand tsCommand = null;
	
	/**
	 * @param targetName
	 */
	public TagSelectorSerializerImpl(String targetName) {
		super(targetName);
		init();
	}

	/**
	 * @param id
	 */
	public TagSelectorSerializerImpl(int id) {
		super(id);
		init();
	}

	/**
	 * @param id
	 * @param targetName
	 */
	public TagSelectorSerializerImpl(int id, String targetName) {
		super(id, targetName);
		init();
	}

	private void init() {
		tsCommand = cmdFactory.createTagSelectorCommand();
	}
	
	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.TagSelectorSerializer#create(java.lang.String)
	 */
	public String create(final String name, final TagField tagField,
			final String filterValue, final String filterMask,
			final boolean inclusive) {
		resetCommand();
		TagSelectorCommand.Create c = cmdFactory.createTagSelectorCommandCreate();
		c.setName(name);
		c.setField(tagField.getName());
		c.setValue(HexUtil.hexToByteArray(filterValue));
		c.setMask(HexUtil.hexToByteArray(filterMask));
		c.setInclusiveFlag(inclusive);
		tsCommand.setCreate(c);
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.TagSelectorSerializer#getMaxNumberSupported()
	 */
	public String getMaxNumberSupported() {
		resetCommand();
		tsCommand.setGetMaxNumberSupported(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.TagSelectorSerializer#getName()
	 */
	public String getName() {
		resetCommand();
		tsCommand.setGetName(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.TagSelectorSerializer#getTagField()
	 */
	public String getTagField() {
		resetCommand();
		tsCommand.setGetTagField(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.TagSelectorSerializer#getValue()
	 */
	public String getValue() {
		resetCommand();
		tsCommand.setGetValue(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.TagSelectorSerializer#getMask()
	 */
	public String getMask() {
		resetCommand();
		tsCommand.setGetMask(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.TagSelectorSerializer#getInclusiveFlag()
	 */
	public String getInclusiveFlag() {
		resetCommand();
		tsCommand.setGetInclusiveFlag(cmdFactory.createNoParamType());
		return serializeCommand();
	}
	
	/**
	 * Serializes an TagSelector command.
	 */
	public String serializeCommand() {
		command.setTagSelector(tsCommand);
		return super.serializeCommand();
	}

}
