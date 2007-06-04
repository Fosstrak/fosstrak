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

import java.util.Date;

import org.accada.reader.rp.proxy.msg.stubs.DataSelector;
import org.accada.reader.rp.proxy.msg.stubs.NotificationChannel;
import org.accada.reader.rp.proxy.msg.stubs.Source;
import org.accada.reader.rp.proxy.msg.stubs.TagField;
import org.accada.reader.rp.proxy.msg.stubs.TagSelector;
import org.accada.reader.rp.proxy.msg.stubs.Trigger;
import org.accada.reader.rp.proxy.msg.stubs.serializers.ReaderDeviceSerializer;
import org.accada.reader.rprm.core.msg.command.DataSelectorListParamType;
import org.accada.reader.rprm.core.msg.command.NotificationChannelListParamType;
import org.accada.reader.rprm.core.msg.command.ReaderDeviceCommand;
import org.accada.reader.rprm.core.msg.command.SourceListParamType;
import org.accada.reader.rprm.core.msg.command.TagFieldListParamType;
import org.accada.reader.rprm.core.msg.command.TagSelectorListParamType;
import org.accada.reader.rprm.core.msg.command.TriggerListParamType;
import org.accada.reader.rprm.core.msg.command.ReaderDeviceCommand.GetDataSelector;
import org.accada.reader.rprm.core.msg.command.ReaderDeviceCommand.GetNotificationChannel;
import org.accada.reader.rprm.core.msg.command.ReaderDeviceCommand.GetReadPoint;
import org.accada.reader.rprm.core.msg.command.ReaderDeviceCommand.GetSource;
import org.accada.reader.rprm.core.msg.command.ReaderDeviceCommand.GetTagField;
import org.accada.reader.rprm.core.msg.command.ReaderDeviceCommand.GetTagSelector;
import org.accada.reader.rprm.core.msg.command.ReaderDeviceCommand.GetTrigger;
import org.accada.reader.rprm.core.msg.command.ReaderDeviceCommand.RemoveDataSelectors;
import org.accada.reader.rprm.core.msg.command.ReaderDeviceCommand.RemoveNotificationChannels;
import org.accada.reader.rprm.core.msg.command.ReaderDeviceCommand.RemoveSources;
import org.accada.reader.rprm.core.msg.command.ReaderDeviceCommand.RemoveTagFields;
import org.accada.reader.rprm.core.msg.command.ReaderDeviceCommand.RemoveTagSelectors;
import org.accada.reader.rprm.core.msg.command.ReaderDeviceCommand.RemoveTriggers;
import org.accada.reader.rprm.core.msg.command.ReaderDeviceCommand.SetCurrentDataSelector;
import org.accada.reader.rprm.core.msg.command.ReaderDeviceCommand.SetCurrentSource;
import org.accada.reader.rprm.core.msg.command.ReaderDeviceCommand.SetHandle;
import org.accada.reader.rprm.core.msg.command.ReaderDeviceCommand.SetName;
import org.accada.reader.rprm.core.msg.command.ReaderDeviceCommand.SetRole;
import org.accada.reader.rprm.core.msg.command.ReaderDeviceCommand.SetTimeUTC;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

/**
 * @author Andreas
 * 
 */
public class ReaderDeviceSerializerImpl extends CommandSerializerImpl implements
ReaderDeviceSerializer {
	
	private ReaderDeviceCommand rdCommand = null;
	
	/**
	 * @param targetName
	 */
	public ReaderDeviceSerializerImpl(String targetName) {
		super(targetName);
		init();
	}
	
	/**
	 * @param id
	 */
	public ReaderDeviceSerializerImpl(int id) {
		super(id);
		init();
	}
	
	/**
	 * @param id
	 * @param targetName
	 */
	public ReaderDeviceSerializerImpl(int id, String targetName) {
		super(id, targetName);
		init();
	}
	
	private void init() {
		rdCommand = cmdFactory.createReaderDeviceCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#getEPC()
	 */
	public String getEPC() {
		resetCommand();
		rdCommand.setGetEPC(cmdFactory.createNoParamType());
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#getManufacturer()
	 */
	public String getManufacturer() {
		resetCommand();
		rdCommand.setGetManufacturer(cmdFactory.createNoParamType());
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#getModel()
	 */
	public String getModel() {
		resetCommand();
		rdCommand.setGetModel(cmdFactory.createNoParamType());
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#getHandle()
	 */
	public String getHandle() {
		resetCommand();
		rdCommand.setGetHandle(cmdFactory.createNoParamType());
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#setHandle(int)
	 */
	public String setHandle(int pHandle) {
		resetCommand();
		SetHandle handle = cmdFactory
		.createReaderDeviceCommandSetHandle();
		handle.setHandle(pHandle);
		rdCommand.setSetHandle(handle);
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#getName()
	 */
	public String getName() {
		resetCommand();
		rdCommand.setGetName(cmdFactory.createNoParamType());
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#setName(java.lang.String)
	 */
	public String setName(String pName) {
		resetCommand();
		SetName name = cmdFactory
		.createReaderDeviceCommandSetName();
		name.setName(pName);
		rdCommand.setSetName(name);
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#getRole()
	 */
	public String getRole() {
		resetCommand();
		rdCommand.setGetRole(cmdFactory.createNoParamType());
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#setRole(java.lang.String)
	 */
	public String setRole(String pRole) {
		resetCommand();
		SetRole role = cmdFactory
		.createReaderDeviceCommandSetRole();
		role.setRole(pRole);
		rdCommand.setSetRole(role);
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#getTimeTicks()
	 */
	public String getTimeTicks() {
		resetCommand();
		rdCommand.setGetTimeTicks(cmdFactory.createNoParamType());
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#getTimeUTC()
	 */
	public String getTimeUTC() {
		resetCommand();
		rdCommand.setGetTimeUTC(cmdFactory.createNoParamType());
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#setTimeUTC(java.util.Date)
	 */
	public String setTimeUTC(Date utc) {
		resetCommand();
		SetTimeUTC time = cmdFactory
		.createReaderDeviceCommandSetTimeUTC();
		time.setUtc(new XMLGregorianCalendarImpl()); 
		rdCommand.setSetTimeUTC(time);
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#getManufacturerDescription()
	 */
	public String getManufacturerDescription() {
		resetCommand();
		rdCommand.setGetManufacturerDescription(cmdFactory.createNoParamType());
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#getCurrentSource()
	 */
	public String getCurrentSource() {
		resetCommand();
		rdCommand.setGetCurrentSource(cmdFactory.createNoParamType());
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#setCurrentSource(org.accada.reader.Source)
	 */
	public String setCurrentSource(Source currentSource) {
		resetCommand();
		SetCurrentSource source = cmdFactory
		.createReaderDeviceCommandSetCurrentSource();
		source.setCurrentSource(currentSource.getName());
		rdCommand.setSetCurrentSource(source);
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#getCurrentDataSelector()
	 */
	public String getCurrentDataSelector() {
		resetCommand();
		rdCommand.setGetCurrentDataSelector(cmdFactory.createNoParamType());
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#setCurrentDataSelector(org.accada.reader.testclient.command.DataSelectorSerializer)
	 */
	public String setCurrentDataSelector(DataSelector currentDataSelector) {
		resetCommand();
		SetCurrentDataSelector ds = cmdFactory
		.createReaderDeviceCommandSetCurrentDataSelector();
		ds.setCurrentDataSelector(currentDataSelector.getName());
		rdCommand.setSetCurrentDataSelector(ds);
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#removeSources(org.accada.reader.Source[])
	 */
	public String removeSources(Source[] pSources) {
		resetCommand();
		RemoveSources sources = cmdFactory
		.createReaderDeviceCommandRemoveSources();
		SourceListParamType.List list = cmdFactory
		.createSourceListParamTypeList();
		SourceListParamType listType = cmdFactory
		.createSourceListParamType();
		list.getValue().addAll(toStringList(pSources));
		listType.setList(list);
		sources.setSources(listType);
		rdCommand.setRemoveSources(sources);
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#removeAllSources()
	 */
	public String removeAllSources() {
		resetCommand();
		rdCommand.setRemoveAllSources(cmdFactory.createNoParamType());
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#getSource(java.lang.String)
	 */
	public String getSource(String name) {
		resetCommand();
		GetSource source = cmdFactory
		.createReaderDeviceCommandGetSource();
		source.setName(name);
		rdCommand.setGetSource(source);
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#getAllSources()
	 */
	public String getAllSources() {
		resetCommand();
		rdCommand.setGetAllSources(cmdFactory.createNoParamType());
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#removeDataSelectors(org.accada.reader.testclient.command.DataSelectorSerializer[])
	 */
	public String removeDataSelectors(DataSelector[] dataSelectors) {
		resetCommand();
		RemoveDataSelectors ds = cmdFactory
		.createReaderDeviceCommandRemoveDataSelectors();
		DataSelectorListParamType.List list = cmdFactory
		.createDataSelectorListParamTypeList();
		DataSelectorListParamType listType = cmdFactory
		.createDataSelectorListParamType();
		list.getValue().addAll(toStringList(dataSelectors));
		listType.setList(list);
		ds.setDataSelectors(listType);
		rdCommand.setRemoveDataSelectors(ds);
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#removeAllDataSelectors()
	 */
	public String removeAllDataSelectors() {
		resetCommand();
		rdCommand.setRemoveAllDataSelectors(cmdFactory.createNoParamType());
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#getDataSelector(java.lang.String)
	 */
	public String getDataSelector(String name) {
		resetCommand();
		GetDataSelector ds = cmdFactory
		.createReaderDeviceCommandGetDataSelector();
		ds.setName(name);
		rdCommand.setGetDataSelector(ds);
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#getAllDataSelectors()
	 */
	public String getAllDataSelectors() {
		resetCommand();
		rdCommand.setGetAllDataSelectors(cmdFactory.createNoParamType());
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#removeNotificationChannels(org.accada.reader.NotificationChannel[])
	 */
	public String removeNotificationChannels(
			NotificationChannel[] notificationChannels) {
		resetCommand();
		RemoveNotificationChannels ncs = cmdFactory
		.createReaderDeviceCommandRemoveNotificationChannels();
		NotificationChannelListParamType.List list = cmdFactory
		.createNotificationChannelListParamTypeList();
		NotificationChannelListParamType listType = cmdFactory
		.createNotificationChannelListParamType();
		list.getValue().addAll(toStringList(notificationChannels));
		listType.setList(list);
		ncs.setChannels(listType);
		rdCommand.setRemoveNotificationChannels(ncs);
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#removeAllNotificationChannels()
	 */
	public String removeAllNotificationChannels() {
		resetCommand();
		rdCommand.setRemoveAllNotificationChannels(cmdFactory
				.createNoParamType());
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#getNotificationChannel(java.lang.String)
	 */
	public String getNotificationChannel(String name) {
		resetCommand();
		GetNotificationChannel nc = cmdFactory
		.createReaderDeviceCommandGetNotificationChannel();
		nc.setName(name);
		rdCommand.setGetNotificationChannel(nc);
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#getAllNotificationChannels()
	 */
	public String getAllNotificationChannels() {
		resetCommand();
		rdCommand.setGetAllNotificationChannels(cmdFactory.createNoParamType());
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#removeTriggers(org.accada.reader.Trigger[])
	 */
	public String removeTriggers(Trigger[] triggerList) {
		resetCommand();
		RemoveTriggers trgs = cmdFactory
		.createReaderDeviceCommandRemoveTriggers();
		TriggerListParamType.List list = cmdFactory
		.createTriggerListParamTypeList();
		TriggerListParamType listType = cmdFactory
		.createTriggerListParamType();
		list.getValue().addAll(toStringList(triggerList));
		listType.setList(list);
		trgs.setTriggers(listType);
		rdCommand.setRemoveTriggers(trgs);
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#removeAllTriggers()
	 */
	public String removeAllTriggers() {
		resetCommand();
		rdCommand.setRemoveAllTriggers(cmdFactory.createNoParamType());
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#getTrigger(java.lang.String)
	 */
	public String getTrigger(String name) {
		resetCommand();
		GetTrigger trg = cmdFactory
		.createReaderDeviceCommandGetTrigger();
		trg.setName(name);
		rdCommand.setGetTrigger(trg);
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#getAllTriggers()
	 */
	public String getAllTriggers() {
		resetCommand();
		rdCommand.setGetAllTriggers(cmdFactory.createNoParamType());
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#removeTagSelectors(org.accada.reader.TagSelector[])
	 */
	public String removeTagSelectors(TagSelector[] tagSelectors) {
		resetCommand();
		RemoveTagSelectors tss = cmdFactory
		.createReaderDeviceCommandRemoveTagSelectors();
		TagSelectorListParamType.List list = cmdFactory
		.createTagSelectorListParamTypeList();
		TagSelectorListParamType listType = cmdFactory
		.createTagSelectorListParamType();
		list.getValue().addAll(toStringList(tagSelectors));
		listType.setList(list);
		tss.setSelectors(listType);
		rdCommand.setRemoveTagSelectors(tss);
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#removeAllTagSelectors()
	 */
	public String removeAllTagSelectors() {
		resetCommand();
		rdCommand.setRemoveAllTagSelectors(cmdFactory.createNoParamType());
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#getTagSelector(java.lang.String)
	 */
	public String getTagSelector(String name) {
		resetCommand();
		GetTagSelector ts = cmdFactory
		.createReaderDeviceCommandGetTagSelector();
		ts.setName(name);
		rdCommand.setGetTagSelector(ts);
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#getAllTagSelectors()
	 */
	public String getAllTagSelectors() {
		resetCommand();
		rdCommand.setGetAllTagSelectors(cmdFactory.createNoParamType());
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#removeTagFields(org.accada.reader.TagField[])
	 */
	public String removeTagFields(TagField[] tagFields) {
		resetCommand();
		RemoveTagFields tfs = cmdFactory
		.createReaderDeviceCommandRemoveTagFields();
		TagFieldListParamType.List list = cmdFactory
		.createTagFieldListParamTypeList();
		TagFieldListParamType listType = cmdFactory
		.createTagFieldListParamType();
		list.getValue().addAll(toStringList(tagFields));
		listType.setList(list);
		tfs.setFields(listType);
		rdCommand.setRemoveTagFields(tfs);
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#removeAllTagFields()
	 */
	public String removeAllTagFields() {
		resetCommand();
		rdCommand.setRemoveAllTagFields(cmdFactory.createNoParamType());
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#getTagField(java.lang.String)
	 */
	public String getTagField(String name) {
		resetCommand();
		GetTagField tf = cmdFactory
		.createReaderDeviceCommandGetTagField();
		tf.setName(name);
		rdCommand.setGetTagField(tf);
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#getAllTagFields()
	 */
	public String getAllTagFields() {
		resetCommand();
		rdCommand.setGetAllTagFields(cmdFactory.createNoParamType());
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#resetToDefaultSettings()
	 */
	public String resetToDefaultSettings() {
		resetCommand();
		rdCommand.setResetToDefaultSettings(cmdFactory.createNoParamType());
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#reboot()
	 */
	public String reboot() {
		resetCommand();
		rdCommand.setReboot(cmdFactory.createNoParamType());
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#goodbye()
	 */
	public String goodbye() {
		resetCommand();
		rdCommand.setGoodbye(cmdFactory.createNoParamType());
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#getReadPoint(java.lang.String)
	 */
	public String getReadPoint(String name) {
		resetCommand();
		GetReadPoint rp = cmdFactory
		.createReaderDeviceCommandGetReadPoint();
		rp.setName(name);
		rdCommand.setGetReadPoint(rp);
		return serializeCommand();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.ReaderDeviceSerializer#getAllReadPoints()
	 */
	public String getAllReadPoints() {
		resetCommand();
		rdCommand.setGetAllReadPoints(cmdFactory.createNoParamType());
		return serializeCommand();
	}
	
	/**
	 * Serializes an ReaderDevice command.
	 */
	public String serializeCommand() {
		command.setReaderDevice(rdCommand);
		return super.serializeCommand();
	}
	
}
