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

import org.accada.reader.rp.proxy.msg.stubs.DataSelector;
import org.accada.reader.rp.proxy.msg.stubs.Source;
import org.accada.reader.rp.proxy.msg.stubs.Trigger;
import org.accada.reader.rp.proxy.msg.stubs.serializers.NotificationChannelSerializer;
import org.accada.reader.rprm.core.msg.command.NotificationChannelCommand;
import org.accada.reader.rprm.core.msg.command.SourceListParamType;
import org.accada.reader.rprm.core.msg.command.TriggerListParamType;

/**
 * @author Andreas
 *
 */
public class NotificationChannelSerializerImpl extends CommandSerializerImpl
		implements NotificationChannelSerializer {

	private NotificationChannelCommand ncCommand = null;
	
	/**
	 * @param targetName
	 */
	public NotificationChannelSerializerImpl(String targetName) {
		super(targetName);
		init();
	}

	/**
	 * @param id
	 */
	public NotificationChannelSerializerImpl(int id) {
		super(id);
		init();
	}

	/**
	 * @param id
	 * @param targetName
	 */
	public NotificationChannelSerializerImpl(int id, String targetName) {
		super(id, targetName);
		init();
	}
	
	private void init() {
		ncCommand = cmdFactory.createNotificationChannelCommand();
	}
	

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.NotificationChannelSerializer#create(java.lang.String, java.lang.String)
	 */
	public String create(String name, String addr) {
		resetCommand();
		NotificationChannelCommand.Create c = cmdFactory.createNotificationChannelCommandCreate();
		c.setName(name);
		c.setAddress(addr);
		ncCommand.setCreate(c);
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.NotificationChannelSerializer#getName()
	 */
	public String getName() {
		resetCommand();
		ncCommand.setGetName(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.NotificationChannelSerializer#getAddress()
	 */
	public String getAddress() {
		resetCommand();
		ncCommand.setGetAddress(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.NotificationChannelSerializer#getEffectiveAddress()
	 */
	public String getEffectiveAddress() {
		resetCommand();
		ncCommand.setGetEffectiveAddress(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.NotificationChannelSerializer#setAddress(java.lang.String)
	 */
	public String setAddress(String addr) {
		resetCommand();
		NotificationChannelCommand.SetAddress a = cmdFactory.createNotificationChannelCommandSetAddress();
		a.setAddress(addr);
		ncCommand.setSetAddress(a);
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.NotificationChannelSerializer#getDataSelector()
	 */
	public String getDataSelector() {
		resetCommand();
		ncCommand.setGetDataSelector(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.NotificationChannelSerializer#setDataSelector(org.accada.reader.testclient.command.DataSelectorSerializer)
	 */
	public String setDataSelector(DataSelector dataSelector) {
		resetCommand();
		NotificationChannelCommand.SetDataSelector ds = cmdFactory.createNotificationChannelCommandSetDataSelector();
		ds.setDataSelector(dataSelector.getName());
		ncCommand.setSetDataSelector(ds);
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.NotificationChannelSerializer#addSources(org.accada.reader.Source[])
	 */
	public String addSources(Source[] sourceList) {
		resetCommand();
		NotificationChannelCommand.AddSources srcs = cmdFactory.createNotificationChannelCommandAddSources();
		SourceListParamType.List list = cmdFactory.createSourceListParamTypeList();
		SourceListParamType listType = cmdFactory.createSourceListParamType();
		list.getValue().addAll(toStringList(sourceList));
		listType.setList(list);
		srcs.setSources(listType);
		ncCommand.setAddSources(srcs);
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.NotificationChannelSerializer#removeSources(org.accada.reader.Source[])
	 */
	public String removeSources(Source[] sourceList) {
		resetCommand();
		NotificationChannelCommand.RemoveSources srcs = cmdFactory.createNotificationChannelCommandRemoveSources();
		SourceListParamType.List list = cmdFactory.createSourceListParamTypeList();
		SourceListParamType listType = cmdFactory.createSourceListParamType();
		list.getValue().addAll(toStringList(sourceList));
		listType.setList(list);
		srcs.setSources(listType);
		ncCommand.setRemoveSources(srcs);
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.NotificationChannelSerializer#removeAllSources()
	 */
	public String removeAllSources() {
		resetCommand();
		ncCommand.setRemoveAllSources(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.NotificationChannelSerializer#getSource(java.lang.String)
	 */
	public String getSource(String name) {
		resetCommand();
		NotificationChannelCommand.GetSource src = cmdFactory.createNotificationChannelCommandGetSource();
		src.setName(name);
		ncCommand.setGetSource(src);
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.NotificationChannelSerializer#getAllSources()
	 */
	public String getAllSources() {
		resetCommand();
		ncCommand.setGetAllSources(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.NotificationChannelSerializer#addNotificationTriggers(org.accada.reader.Trigger[])
	 */
	public String addNotificationTriggers(Trigger[] triggers) {
		resetCommand();
		NotificationChannelCommand.AddNotificationTriggers trgs = cmdFactory.createNotificationChannelCommandAddNotificationTriggers();
		TriggerListParamType.List list = cmdFactory.createTriggerListParamTypeList();
		TriggerListParamType listType = cmdFactory.createTriggerListParamType();
		list.getValue().addAll(toStringList(triggers));
		listType.setList(list);
		trgs.setTriggers(listType);
		ncCommand.setAddNotificationTriggers(trgs);
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.NotificationChannelSerializer#removeNotificationTriggers(org.accada.reader.Trigger[])
	 */
	public String removeNotificationTriggers(Trigger[] triggers) {
		resetCommand();
		NotificationChannelCommand.RemoveNotificationTriggers trgs = cmdFactory.createNotificationChannelCommandRemoveNotificationTriggers();
		TriggerListParamType.List list = cmdFactory.createTriggerListParamTypeList();
		TriggerListParamType listType = cmdFactory.createTriggerListParamType();
		list.getValue().addAll(toStringList(triggers));
		listType.setList(list);
		trgs.setTriggers(listType);
		ncCommand.setRemoveNotificationTriggers(trgs);
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.NotificationChannelSerializer#removeAllNotificationTriggers()
	 */
	public String removeAllNotificationTriggers() {
		resetCommand();
		ncCommand.setRemoveAllNotificationTriggers(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.NotificationChannelSerializer#getNotificationTrigger(java.lang.String)
	 */
	public String getNotificationTrigger(String name) {
		resetCommand();
		NotificationChannelCommand.GetNotificationTrigger trg = cmdFactory.createNotificationChannelCommandGetNotificationTrigger();
		trg.setName(name);				
		ncCommand.setGetNotificationTrigger(trg);
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.NotificationChannelSerializer#getAllNotificationTriggers()
	 */
	public String getAllNotificationTriggers() {
		resetCommand();
		ncCommand.setGetAllNotificationTriggers(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.NotificationChannelSerializer#readQueuedData(boolean)
	 */
	public String readQueuedData(boolean clearBuffer) {
		resetCommand();
		NotificationChannelCommand.ReadQueuedData rq = cmdFactory.createNotificationChannelCommandReadQueuedData();
		rq.setClearBuffer(clearBuffer);
		ncCommand.setReadQueuedData(rq);
		return serializeCommand();
	}
	
	/* (non-Javadoc)
	 * @see org.accada.reader.testclient.command.NotificationChannelSerializer#readQueuedData(boolean)
	 */
	public String readQueuedData() {
		resetCommand();
		NotificationChannelCommand.ReadQueuedData rq = cmdFactory.createNotificationChannelCommandReadQueuedData();
		rq.setClearBuffer(true);
		ncCommand.setReadQueuedData(rq);
		return serializeCommand();
	}

	/**
	 * Serializes an NotificationChannel command.
	 */
	public String serializeCommand() {
		command.setNotificationChannel(ncCommand);
		return super.serializeCommand();
	}

}
