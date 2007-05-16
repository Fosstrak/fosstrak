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

import org.accada.reader.rp.proxy.msg.stubs.serializers.DataSelectorSerializer;

/**
 * @author Andreas
 *
 */
public class DataSelectorSerializerImpl extends CommandSerializerImpl implements DataSelectorSerializer {

		
	public DataSelectorSerializerImpl(int id, String targetName) {
		super(id, targetName);
		init();
	}
	
	public DataSelectorSerializerImpl(String targetName) {
		super(targetName);
		init();
	}
	
	private void init() {
		if (shortMode) {
			setObjectTypeName(TextTokens.DS);
		} else {
			setObjectTypeName(TextTokens.DATASELECTOR);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.DataSelectorSerializer#create(java.lang.String)
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

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.DataSelectorSerializer#getName()
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
	 * @see org.accada.reader.testclient.command.DataSelectorSerializer#addFieldNames(java.lang.String[])
	 */
	public String addFieldNames(String[] fieldNameList) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_AFN);
		} else {
			setCommandName(TextTokens.CMD_ADD_FIELD_NAMES);
		}
		setParameterList(fieldNameList);
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.DataSelectorSerializer#removeFieldNames(java.lang.String[])
	 */
	public String removeFieldNames(String[] fieldNameList) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_RFN);
		} else {
			setCommandName(TextTokens.CMD_REMOVE_FIELD_NAMES);
		}
		setParameterList(fieldNameList);
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.DataSelectorSerializer#removeAllFieldNames()
	 */
	public String removeAllFieldNames() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_RAFN);
		} else {
			setCommandName(TextTokens.CMD_REMOVE_ALL_FIELD_NAMES);
		}
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.DataSelectorSerializer#getAllFieldNames()
	 */
	public String getAllFieldNames() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GAFN);
		} else {
			setCommandName(TextTokens.CMD_GET_ALL_FIELD_NAMES);
		}
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.DataSelectorSerializer#addEventFilters(java.lang.String[])
	 */
	public String addEventFilters(String[] eventList) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_AEF);
		} else {
			setCommandName(TextTokens.CMD_ADD_EVENT_FILTERS);
		}
		setParameterList(eventList);
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.DataSelectorSerializer#removeEventFilters(java.lang.String[])
	 */
	public String removeEventFilters(String[] eventList) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_REF);
		} else {
			setCommandName(TextTokens.CMD_REMOVE_EVENT_FILTERS);
		}
		setParameterList(eventList);
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.DataSelectorSerializer#removeAllEventFilters()
	 */
	public String removeAllEventFilters() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_RAEF);
		} else {
			setCommandName(TextTokens.CMD_REMOVE_ALL_EVENT_FILTERS);
		}
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.DataSelectorSerializer#getAllEventFilters()
	 */
	public String getAllEventFilters() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GAEF);
		} else {
			setCommandName(TextTokens.CMD_GET_ALL_EVENT_FILTERS);
		}
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.DataSelectorSerializer#addTagFieldNames(java.lang.String[])
	 */
	public String addTagFieldNames(String[] tagFieldNameList) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_ATFN);
		} else {
			setCommandName(TextTokens.CMD_ADD_TAG_FIELD_NAMES);
		}
		setParameterList(tagFieldNameList);
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.DataSelectorSerializer#removeTagFieldNames(java.lang.String[])
	 */
	public String removeTagFieldNames(String[] tagFieldNameList) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_RTFN);
		} else {
			setCommandName(TextTokens.CMD_REMOVE_TAG_FIELD_NAMES);
		}
		setParameterList(tagFieldNameList);
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.DataSelectorSerializer#removeAllTagFieldNames()
	 */
	public String removeAllTagFieldNames() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_RATFN);
		} else {
			setCommandName(TextTokens.CMD_REMOVE_ALL_TAG_FIELD_NAMES);
		}
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.DataSelectorSerializer#getAllTagFieldNames()
	 */
	public String getAllTagFieldNames() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GATFN);
		} else {
			setCommandName(TextTokens.CMD_GET_ALL_TAG_FIELD_NAMES);
		}
		return serializeCommand();
	}
	
	/**
	 * Serializes a DataSelector command.
	 */
	public String serializeCommand() {
		return super.serializeCommand();
	}

}
