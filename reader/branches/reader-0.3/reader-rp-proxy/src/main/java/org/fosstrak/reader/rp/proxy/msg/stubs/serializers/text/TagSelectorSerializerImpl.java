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

import org.accada.reader.rp.proxy.msg.stubs.TagField;
import org.accada.reader.rp.proxy.msg.stubs.serializers.TagSelectorSerializer;

/**
 * @author Andreas
 *
 */
public class TagSelectorSerializerImpl extends CommandSerializerImpl implements
		TagSelectorSerializer {

	
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
		if (shortMode) {
			setObjectTypeName(TextTokens.TAGSELECTOR); //TODO: abbrev. for TagSelector???
		} else {
			setObjectTypeName(TextTokens.TAGSELECTOR);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.TagSelectorSerializer#create(java.lang.String)
	 */
	public String create(final String name, final TagField tagField,
			final String filterValue, final String filterMask,
			final boolean inclusive) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_C);
		} else {
			setCommandName(TextTokens.CMD_CREATE);
		}
		//TODO: Parameter setzen, evtl. manche optional
		//setParameter(name + ", " + tagField.getName() + ", " + filterValue + "," + filterMask);
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.TagSelectorSerializer#getMaxNumberSupported()
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

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.TagSelectorSerializer#getName()
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
	 * @see org.accada.reader.testclient.command.TagSelectorSerializer#getTagField()
	 */
	public String getTagField() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GTF);
		} else {
			setCommandName(TextTokens.CMD_GET_TAG_FIELD);
		}
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.TagSelectorSerializer#getValue()
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

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.TagSelectorSerializer#getMask()
	 */
	public String getMask() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GMX);
		} else {
			setCommandName(TextTokens.CMD_GET_MASK);
		}
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.TagSelectorSerializer#getInclusiveFlag()
	 */
	public String getInclusiveFlag() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GIF);
		} else {
			setCommandName(TextTokens.CMD_GET_INCLUSIVE_FLAG);
		}
		return serializeCommand();
	}
	
	/**
	 * Serializes an TagSelector command.
	 */
	public String serializeCommand() {
		return super.serializeCommand();
	}

}
