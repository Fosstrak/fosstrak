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

import org.fosstrak.reader.rp.proxy.msg.stubs.serializers.DataSelectorSerializer;
import org.fosstrak.reader.rprm.core.msg.command.DataSelectorCommand;
import org.fosstrak.reader.rprm.core.msg.command.EventTypeListParamType;
import org.fosstrak.reader.rprm.core.msg.command.FieldNameListParamType;
import org.fosstrak.reader.rprm.core.msg.command.StringListParamType;

/**
 * @author Andreas
 *
 */
public class DataSelectorSerializerImpl extends CommandSerializerImpl implements DataSelectorSerializer {

	private DataSelectorCommand dsCommand = null;
	
	
	public DataSelectorSerializerImpl(int id, String targetName) {
		super(id, targetName);
		init();
	}
	
	public DataSelectorSerializerImpl(String targetName) {
		super(targetName);
		init();
	}
	
	private void init() {
		dsCommand  = cmdFactory.createDataSelectorCommand();
	}
	
	/* (non-Javadoc)
	 * @see org.fosstrak.reader.testclient.command.DataSelectorSerializer#create(java.lang.String)
	 */
	public String create(String name) {
		resetCommand();
		DataSelectorCommand.Create c;
		c = cmdFactory.createDataSelectorCommandCreate();
		c.setName(name);
		dsCommand.setCreate(c);
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.fosstrak.reader.testclient.command.DataSelectorSerializer#getName()
	 */
	public String getName() {
		resetCommand();
		dsCommand.setGetName(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.fosstrak.reader.testclient.command.DataSelectorSerializer#addFieldNames(java.lang.String[])
	 */
	public String addFieldNames(String[] fieldNameList) {
		resetCommand();
		DataSelectorCommand.AddFieldNames fns = cmdFactory.createDataSelectorCommandAddFieldNames();
		FieldNameListParamType.List list = cmdFactory.createFieldNameListParamTypeList();
		FieldNameListParamType listType = cmdFactory.createFieldNameListParamType();
		list.getValue().addAll(toStringList(fieldNameList));
		listType.setList(list);
		fns.setFieldNames(listType);
		dsCommand.setAddFieldNames(fns);
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.fosstrak.reader.testclient.command.DataSelectorSerializer#removeFieldNames(java.lang.String[])
	 */
	public String removeFieldNames(String[] fieldNameList) {
		resetCommand();
		DataSelectorCommand.RemoveFieldNames fns = cmdFactory.createDataSelectorCommandRemoveFieldNames();
		FieldNameListParamType.List list = cmdFactory.createFieldNameListParamTypeList();
		FieldNameListParamType listType = cmdFactory.createFieldNameListParamType();
		list.getValue().addAll(toStringList(fieldNameList));
		listType.setList(list);
		fns.setFieldNames(listType);
		dsCommand.setRemoveFieldNames(fns);
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.fosstrak.reader.testclient.command.DataSelectorSerializer#removeAllFieldNames()
	 */
	public String removeAllFieldNames() {
		resetCommand();
		dsCommand.setRemoveAllFieldNames(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.fosstrak.reader.testclient.command.DataSelectorSerializer#getAllFieldNames()
	 */
	public String getAllFieldNames() {
		resetCommand();
		dsCommand.setGetAllFieldNames(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.fosstrak.reader.testclient.command.DataSelectorSerializer#addEventFilters(java.lang.String[])
	 */
	public String addEventFilters(String[] eventList) {
		resetCommand();
		DataSelectorCommand.AddEventFilters efs = cmdFactory.createDataSelectorCommandAddEventFilters();
		EventTypeListParamType.List list = cmdFactory.createEventTypeListParamTypeList();
		EventTypeListParamType listType = cmdFactory.createEventTypeListParamType();
		list.getValue().addAll(toStringList(eventList));
		listType.setList(list);
		efs.setEventType(listType);
		dsCommand.setAddEventFilters(efs);
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.fosstrak.reader.testclient.command.DataSelectorSerializer#removeEventFilters(java.lang.String[])
	 */
	public String removeEventFilters(String[] eventList) {
		resetCommand();
		DataSelectorCommand.RemoveEventFilters efs = cmdFactory.createDataSelectorCommandRemoveEventFilters();
		EventTypeListParamType.List list = cmdFactory.createEventTypeListParamTypeList();
		EventTypeListParamType listType = cmdFactory.createEventTypeListParamType();
		list.getValue().addAll(toStringList(eventList));
		listType.setList(list);
		efs.setEventType(listType);
		dsCommand.setRemoveEventFilters(efs);
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.fosstrak.reader.testclient.command.DataSelectorSerializer#removeAllEventFilters()
	 */
	public String removeAllEventFilters() {
		resetCommand();
		dsCommand.setRemoveAllEventFilters(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.fosstrak.reader.testclient.command.DataSelectorSerializer#getAllEventFilters()
	 */
	public String getAllEventFilters() {
		resetCommand();
		dsCommand.setGetAllEventFilters(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.fosstrak.reader.testclient.command.DataSelectorSerializer#addTagFieldNames(java.lang.String[])
	 */
	public String addTagFieldNames(String[] tagFieldNameList) {
		resetCommand();
		DataSelectorCommand.AddTagFieldNames tfs = cmdFactory.createDataSelectorCommandAddTagFieldNames();
		StringListParamType.List list = cmdFactory.createStringListParamTypeList();
		StringListParamType listType = cmdFactory.createStringListParamType();
		list.getValue().addAll(toStringList(tagFieldNameList));
		listType.setList(list);
		tfs.setFieldNames(listType);
		dsCommand.setAddTagFieldNames(tfs);
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.fosstrak.reader.testclient.command.DataSelectorSerializer#removeTagFieldNames(java.lang.String[])
	 */
	public String removeTagFieldNames(String[] tagFieldNameList) {
		resetCommand();
		DataSelectorCommand.RemoveTagFieldNames tfs = cmdFactory.createDataSelectorCommandRemoveTagFieldNames();
		StringListParamType.List list = cmdFactory.createStringListParamTypeList();
		StringListParamType listType = cmdFactory.createStringListParamType();
		list.getValue().addAll(toStringList(tagFieldNameList));
		listType.setList(list);
		tfs.setFieldNames(listType);
		dsCommand.setRemoveTagFieldNames(tfs);
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.fosstrak.reader.testclient.command.DataSelectorSerializer#removeAllTagFieldNames()
	 */
	public String removeAllTagFieldNames() {
		resetCommand();
		dsCommand.setRemoveAllTagFieldNames(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.fosstrak.reader.testclient.command.DataSelectorSerializer#getAllTagFieldNames()
	 */
	public String getAllTagFieldNames() {
		resetCommand();
		dsCommand.setGetAllTagFieldNames(cmdFactory.createNoParamType());
		return serializeCommand();
	}
	
	/**
	 * Serializes a DataSelector command.
	 */
	public String serializeCommand() {
		command.setDataSelector(dsCommand);
		return super.serializeCommand();
	}

}
