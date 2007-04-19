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

package org.accada.reader.rp.proxy.msg;

import java.io.StringWriter;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.accada.reader.rp.proxy.msg.stubs.DataSelector;
import org.accada.reader.rp.proxy.msg.stubs.NotificationChannel;
import org.accada.reader.rp.proxy.msg.stubs.ReadPoint;
import org.accada.reader.rp.proxy.msg.stubs.Source;
import org.accada.reader.rp.proxy.msg.stubs.TagField;
import org.accada.reader.rp.proxy.msg.stubs.TagFieldValue;
import org.accada.reader.rp.proxy.msg.stubs.TagSelector;
import org.accada.reader.rp.proxy.msg.stubs.Trigger;
import org.accada.reader.rp.proxy.msg.stubs.serializers.DataSelectorSerializer;
import org.accada.reader.rp.proxy.msg.stubs.serializers.EventTypeSerializer;
import org.accada.reader.rp.proxy.msg.stubs.serializers.FieldNameSerializer;
import org.accada.reader.rp.proxy.msg.stubs.serializers.NotificationChannelSerializer;
import org.accada.reader.rp.proxy.msg.stubs.serializers.ReadPointSerializer;
import org.accada.reader.rp.proxy.msg.stubs.serializers.ReaderDeviceSerializer;
import org.accada.reader.rp.proxy.msg.stubs.serializers.SourceSerializer;
import org.accada.reader.rp.proxy.msg.stubs.serializers.TagFieldSerializer;
import org.accada.reader.rp.proxy.msg.stubs.serializers.TagSelectorSerializer;
import org.accada.reader.rp.proxy.msg.stubs.serializers.TriggerSerializer;
import org.accada.reader.rp.proxy.msg.stubs.serializers.TriggerTypeSerializer;
import org.accada.reader.rp.proxy.msg.stubs.serializers.xml.DataSelectorSerializerImpl;
import org.accada.reader.rp.proxy.msg.stubs.serializers.xml.EventTypeSerializerImpl;
import org.accada.reader.rp.proxy.msg.stubs.serializers.xml.FieldNameSerializerImpl;
import org.accada.reader.rp.proxy.msg.stubs.serializers.xml.NotificationChannelSerializerImpl;
import org.accada.reader.rp.proxy.msg.stubs.serializers.xml.ReadPointSerializerImpl;
import org.accada.reader.rp.proxy.msg.stubs.serializers.xml.ReaderDeviceSerializerImpl;
import org.accada.reader.rp.proxy.msg.stubs.serializers.xml.SourceSerializerImpl;
import org.accada.reader.rp.proxy.msg.stubs.serializers.xml.TagFieldSerializerImpl;
import org.accada.reader.rp.proxy.msg.stubs.serializers.xml.TagSelectorSerializerImpl;
import org.accada.reader.rp.proxy.msg.stubs.serializers.xml.TriggerSerializerImpl;
import org.accada.reader.rp.proxy.msg.stubs.serializers.xml.TriggerTypeSerializerImpl;
import org.accada.reader.rprm.core.ReaderProtocolException;
import org.accada.reader.rprm.core.msg.MessageSerializingException;
import org.accada.reader.rprm.core.msg.command.Command;
import org.accada.reader.rprm.core.msg.command.NotificationChannelCommand;
import org.accada.reader.rprm.core.msg.command.ObjectFactory;
import org.accada.reader.rprm.core.msg.command.SourceCommand;

/**
 * This class generates xml and text commands to send to a reader device.
 * 
 * @author Andreas Fürer, Remo Egli
 */
public class CommandFactory {

	/** count message ids */
	private static int counter = 0;
	
	/**
	 * This method creates xml commands.
	 * 
	 * @param object the type of the object on which the method should be executed
	 * @param command which should be executed
	 * @param params parameters which belongs to the command
	 * @param target the name of the object on which the method should be executed
	 * @return the command in xml format as string
	 * @throws ParameterTypeException if the parameters are illegal
	 */
	public static String getXMLCommand(String object, String command, Parameter[] params, String target) throws ParameterTypeException {
		
		ObjectFactory cmdFactory = new ObjectFactory();
		Command cmd = cmdFactory.createCommand();
		cmd.setId(Integer.toString(getNextId()));
		if (!target.equals("")) {
			cmd.setTargetName(target);
		}
		if (object.equals("ReaderDevice")) {
			ReaderDeviceSerializer rdCommand = new ReaderDeviceSerializerImpl(getNextId(), target);
			if (command.equals("getEPC")) {
				return rdCommand.getEPC();
			}
			else if (command.equals("getManufacturer")) {
				return rdCommand.getManufacturer();
			}
			else if (command.equals("getModel")) {
				return rdCommand.getModel();
			}
			else if (command.equals("getHandle")) {
				return rdCommand.getHandle();
			}
			else if (command.equals("setHandle")) {
				return rdCommand.setHandle(params[0].getInt());
			}
			
			else if (command.equals("getName")) {
				return rdCommand.getName();
			}
			else if (command.equals("setName")) {
				return rdCommand.setName(params[0].getString());
			}
			else if (command.equals("getRole")) {
				return rdCommand.getRole();
			}
			else if (command.equals("setRole")) {
				return rdCommand.setRole(params[0].getString());
			}
			else if (command.equals("getTimeTicks")) {
				return rdCommand.getTimeTicks();
			}
			else if (command.equals("getTimeUTC")) {
				return rdCommand.getTimeUTC();
			}
			else if (command.equals("setTimeUTC")) {
				return rdCommand.setTimeUTC(Calendar.getInstance().getTime()); 
			}
			else if (command.equals("getManufacturerDescription")) {
				return rdCommand.getManufacturerDescription();
			}
			else if (command.equals("getCurrentSource")) {
				return rdCommand.getCurrentSource();
			}
			else if (command.equals("setCurrentSource")) {
				Source src;
				try {
					src = Source.create(params[0].getString());
					return rdCommand.setCurrentSource(src);
				} catch (ReaderProtocolException e) {
					e.printStackTrace();
					return null;
				} 
				
			}
			else if (command.equals("getCurrentDataSelector")) {
				return rdCommand.getCurrentDataSelector();
			}
			else if (command.equals("setCurrentDataSelector")) {
				DataSelector ds;
				try {
					ds = DataSelector.create(params[0].getString());
					return rdCommand.setCurrentDataSelector(ds);
				} catch (ReaderProtocolException e) {
					e.printStackTrace();
					return null;
				}					
			}
			else if (command.equals("removeSources")) {
				Source[] sources = toSourceArray(params[0].getCollection());
				return rdCommand.removeSources(sources);
			}
			else if (command.equals("removeAllSources")) {
				return rdCommand.removeAllSources();
			}
			else if (command.equals("getSource")) {
				return rdCommand.getSource(params[0].getString());
			}
			else if (command.equals("getAllSources")) {
				return rdCommand.getAllSources();
			}
			else if (command.equals("removeDataSelectors")) {
				DataSelector[] ds = toDataSelectorArray(params[0].getCollection());
				return rdCommand.removeDataSelectors(ds);
			}
			else if (command.equals("removeAllDataSelectors")) {
				return rdCommand.removeAllDataSelectors();
			}
			else if (command.equals("getDataSelector")) {
				return rdCommand.getDataSelector(params[0].getString());
			}
			else if (command.equals("getAllDataSelectors")) {
				return rdCommand.getAllDataSelectors();
			}
			else if (command.equals("removeNotificationChannels")) {
				NotificationChannel[] ncs = toNotificationChannelArray(params[0].getCollection());
				return rdCommand.removeNotificationChannels(ncs);
			}
			else if (command.equals("removeAllNotificationChannels")) {
				return rdCommand.removeAllNotificationChannels();
			}
			else if (command.equals("getNotificationChannel")) {
				return rdCommand.getNotificationChannel(params[0].getString());
			}
			else if (command.equals("getAllNotificationChannels")) {
				return rdCommand.getAllNotificationChannels();
			}
			else if (command.equals("removeTriggers")) {
				Trigger[] trgs = toTriggerArray(params[0].getCollection());
				return rdCommand.removeTriggers(trgs);
			}
			else if (command.equals("removeAllTriggers")) {
				return rdCommand.removeAllTriggers();
			}
			else if (command.equals("getTrigger")) {
				return rdCommand.getTrigger(params[0].getString());
			}
			else if (command.equals("getAllTriggers")) {
				return rdCommand.getAllTriggers();
			}
			else if (command.equals("removeTagSelectors")) {
				TagSelector[] tss = toTagSelectorArray(params[0].getCollection());
				return rdCommand.removeTagSelectors(tss);
			}
			else if (command.equals("removeAllTagSelectors")) {
				return rdCommand.removeAllTagSelectors();
			}
			else if (command.equals("getTagSelector")) {
				return rdCommand.getTagSelector(params[0].getString());
			}
			else if (command.equals("getAllTagSelectors")) {
				return rdCommand.getAllTagSelectors();
			}
			else if (command.equals("removeTagFields")) {
				TagField[] tfs = toTagFieldArray(params[0].getCollection());
				return rdCommand.removeTagFields(tfs);
			}
			else if (command.equals("removeAllTagFields")) {
				return rdCommand.removeAllTagFields();
			}
			else if (command.equals("getTagField")) {
				return rdCommand.getTagField(params[0].getString());
			}
			else if (command.equals("getAllTagFields")) {
				return rdCommand.getAllTagFields();
			}
			else if (command.equals("resetToDefaultSettings")) {
				return rdCommand.resetToDefaultSettings();
			}
			else if (command.equals("reboot")) {
				return rdCommand.reboot();
			}
			else if (command.equals("goodbye")) {
				return rdCommand.goodbye();
			}
			else if (command.equals("getReadPoint")) {
				return rdCommand.getReadPoint(params[0].getString());
			}
			else if (command.equals("getAllReadPoints")) {
				return rdCommand.getAllReadPoints();
			}
		}
		else if (object.equals("Source")) {
			SourceSerializer srcCommand = new SourceSerializerImpl(getNextId(), target);
			if (command.equals("create")) {
				return srcCommand.create(params[0].getString());
			}
			else if (command.equals("getName")) {
				return srcCommand.getName();
			}
			else if (command.equals("isFixed")) {
				return srcCommand.isFixed();
			}
			else if (command.equals("addReadPoints")) {
				ReadPoint[] rps = toReadPointArray(params[0].getCollection());
				return srcCommand.addReadPoints(rps);
			}
			else if (command.equals("removeReadPoints")) {
				ReadPoint[] rps = toReadPointArray(params[0].getCollection());
				return srcCommand.removeReadPoints(rps);
			}
			else if (command.equals("removeAllReadPoints")) {
				return srcCommand.removeAllReadPoints();
			}
			else if (command.equals("getReadPoint")) {
				return srcCommand.getReadPoint(params[0].getString());
			}
			else if (command.equals("getAllReadPoints")) {
				return srcCommand.getAllReadPoints();
			}
			else if (command.equals("addReadTriggers")) {
				Trigger[] rtrg = toTriggerArray(params[0].getCollection());
				return srcCommand.addReadTriggers(rtrg);
			}
			else if (command.equals("removeReadTriggers")) {
				Trigger[] rtrgs = toTriggerArray(params[0].getCollection());
				return srcCommand.removeReadTriggers(rtrgs);
			}
			else if (command.equals("removeAllReadTriggers")) {
				return srcCommand.removeAllReadTriggers();
			}
			else if (command.equals("getReadTrigger")) {
				return srcCommand.getReadTrigger(params[0].getString());
			}
			else if (command.equals("getAllReadTriggers")) {
				return srcCommand.getAllReadTriggers();
			}
			else if (command.equals("addTagSelectors")) {
				TagSelector[] tss = toTagSelectorArray(params[0].getCollection());
				return srcCommand.addTagSelectors(tss);
			}
			else if (command.equals("removeTagSelectors")) {
				TagSelector[] tss = toTagSelectorArray(params[0].getCollection());
				return srcCommand.removeTagSelectors(tss);
			}
			else if (command.equals("removeAllTagSelectors")) {
				return srcCommand.removeAllTagSelectors();
			}
			else if (command.equals("getTagSelector")) {
				return srcCommand.getTagSelector(params[0].getString());
			}
			else if (command.equals("getAllTagSelectors")) {
				return srcCommand.getAllTagSelectors();
			}
			else if (command.equals("getGlimpsedTimeout")) {
				return srcCommand.getGlimpsedTimeout();
			}
			else if (command.equals("setGlimpsedTimeout")) {
				return srcCommand.setGlimpsedTimeout(params[0].getInt());
			}
			else if (command.equals("getObservedThreshold")) {
				return srcCommand.getObservedThreshold();
			}
			else if (command.equals("setObservedThreshold")) {
				return srcCommand.setObservedThreshold(params[0].getInt());
			}
			else if (command.equals("getObservedTimeout")) {
				return srcCommand.getObservedTimeout();
			}
			else if (command.equals("setObservedTimeout")) {
				return srcCommand.setObservedTimeout(params[0].getInt());
			}
			else if (command.equals("getLostTimeout")) {
				return srcCommand.getLostTimeout();
			}
			else if (command.equals("setLostTimeout")) {
				return srcCommand.setLostTimeout(params[0].getInt());
			}
			else if (command.equals("rawReadIDs")) {
				SourceCommand.RawReadIDs r = cmdFactory.createSourceCommandRawReadIDs();
				if (params.length > 0) { //optional parameter is set
					try{
						DataSelector ds = DataSelector.create(params[0].getString());
						return srcCommand.rawReadIDs(ds);
					} catch (Exception e) {}
				} else {
					return srcCommand.rawReadIDs(null);
				}
				
			}
			else if (command.equals("readIDs")) {
				SourceCommand.ReadIDs r = cmdFactory.createSourceCommandReadIDs();
				if (params.length > 0) { //optional parameter is set
					try	{
						DataSelector ds = DataSelector.create(params[0].getString());
						return srcCommand.readIDs(ds);
					} catch (Exception e) {}
				} else {
					return srcCommand.readIDs(null);
				}
				
			}
			else if (command.equals("read")) {
				SourceCommand.Read r = cmdFactory.createSourceCommandRead();
				if (params.length > 0) { //optional parameter is set
					try {
						DataSelector ds = DataSelector.create(params[0].getString());
						return srcCommand.read(ds, null); //TODO: set Passwords
					} catch (Exception e) {}
				} else {
					return srcCommand.read(null, null);
				}
			}
			else if (command.equals("writeID")) {
				TagSelector[] tagSelectors = null;
				String[] passwords = null;
				
				if (params.length > 1) {
					passwords = params[1].getArray();
				}
				if (params.length > 2) {
					tagSelectors = toTagSelectorArray(params[2].getCollection());
				}
				
				return srcCommand.writeID(params[0].getString(), passwords, tagSelectors);
			}
			else if (command.equals("write")) {
				TagFieldValue[] tagFieldValues = null;
				TagSelector[] tagSelectors = null;
				String[] passwords = null;
				
				tagFieldValues = toTagFieldValueArray(params[0].getCollection());
				if (params.length > 1) {
					passwords = params[1].getArray();
				}
				if (params.length > 2) {
					tagSelectors = toTagSelectorArray(params[2].getCollection());
				}
				return srcCommand.write(tagFieldValues, passwords, tagSelectors);
			}
			else if (command.equals("kill")) {
				String[] passwords = null;
				TagSelector[] selectors = null;
				if (params.length > 0) {
					passwords = params[0].getArray();
				}
				if (params.length > 1) {
					selectors = toTagSelectorArray(params[1].getCollection());
				}
				return srcCommand.kill(passwords, selectors);
			}
			else if (command.equals("getReadCyclesPerTrigger")) {
				return srcCommand.getReadCyclesPerTrigger();
			}
			else if (command.equals("getMaxReadDutyCycle")) {
				return srcCommand.getMaxReadDutyCycles();
			}
			else if (command.equals("setReadCyclesPerTrigger")) {
				return srcCommand.setReadCyclesPerTrigger(params[0].getInt());
			}
			else if (command.equals("setMaxReadDutyCycle")) {
				return srcCommand.setMaxReadDutyCycles(params[0].getInt());
			}
			else if (command.equals("setReadTimeout")) {
				return srcCommand.setReadTimeout(params[0].getInt());
			}
			else if (command.equals("getReadTimeout")) {
				return srcCommand.getReadTimeout();
			}
			else if (command.equals("getSession")) {
				return srcCommand.getSession();
			}
			else if (command.equals("setSession")) {
				return srcCommand.setSession(params[0].getInt());
			}			
		}
		else if (object.equals("ReadPoint")) {
			ReadPointSerializer rpCommand = new ReadPointSerializerImpl(getNextId(), target);
			if (command.equals("getName")) {
				return rpCommand.getName();
			}
			
		}
		else if (object.equals("TagSelector")) {
			TagSelectorSerializer tsCommand = new TagSelectorSerializerImpl(getNextId(), target);
			if (command.equals("create")) {
				try {
					TagField tf = TagField.create(params[1].getString());
					return tsCommand.create(params[0].getString(), tf, params[2].getString(), params[3].getString(), params[4].getBoolean());
				} catch (Exception e) {}
			}
			else if (command.equals("getName")) {
				return tsCommand.getName();
			}
			else if (command.equals("getMaxNumberSupported")) {
				return tsCommand.getMaxNumberSupported();
			}
			else if (command.equals("getTagField")) {
				return tsCommand.getTagField();
			}
			else if (command.equals("getValue")) {
				return tsCommand.getValue();
			}
			else if (command.equals("getMask")) {
				return tsCommand.getMask();
			}
			else if (command.equals("getInclusiveFlag")) {
				return tsCommand.getInclusiveFlag();
			}
		}
		else if (object.equals("DataSelector")) {
			DataSelectorSerializer dsCommand = new DataSelectorSerializerImpl(getNextId(), target);
			if (command.equals("create")) {
				return dsCommand.create(params[0].getString());
			}
			else if (command.equals("getName")) {
				return dsCommand.getName();
			}
			else if (command.equals("addFieldNames")) {
				return dsCommand.addFieldNames(params[0].getArray());
			}
			else if (command.equals("removeFieldNames")) {
				return dsCommand.removeFieldNames(params[0].getArray());
			}
			else if (command.equals("removeAllFieldNames")) {
				return dsCommand.removeAllFieldNames();
			}
			else if (command.equals("getAllFieldNames")) {
				return dsCommand.getAllFieldNames();
			}
			else if (command.equals("addEventFilters")) {
				return dsCommand.addEventFilters(params[0].getArray());
			}
			else if (command.equals("removeEventFilters")) {
				return dsCommand.removeEventFilters(params[0].getArray());
			}
			else if (command.equals("removeAllEventFilters")) {
				return dsCommand.removeAllEventFilters();
			}
			else if (command.equals("getAllEventFilters")) {
				return dsCommand.getAllEventFilters();
			}
			else if (command.equals("addTagFieldNames")) {
				return dsCommand.addTagFieldNames(params[0].getArray());
			}
			else if (command.equals("removeTagFieldNames")) {
				return dsCommand.removeTagFieldNames(params[0].getArray());
			}
			else if (command.equals("removeAllTagFieldNames")) {
				return dsCommand.removeAllTagFieldNames();
			}
			else if (command.equals("getAllTagFieldNames")) {
				return dsCommand.getAllTagFieldNames();
			}
		}
		else if (object.equals("NotificationChannel")) {
			NotificationChannelSerializer ncCommand = new NotificationChannelSerializerImpl(getNextId(), target);
			if (command.equals("create")) {
				return ncCommand.create(params[0].getString(), params[1].getString());
			}
			else if (command.equals("getName")) {
				return ncCommand.getName();
			}
			else if (command.equals("getAddress")) {
				return ncCommand.getAddress();
			}
			else if (command.equals("getEffectiveAddress")) {
				return ncCommand.getEffectiveAddress();
			}
			else if (command.equals("setAddress")) {
				return ncCommand.setAddress(params[0].getString());
			}
			else if (command.equals("getDataSelector")) {
				return ncCommand.getDataSelector();
			}
			else if (command.equals("setDataSelector")) {
				try {
					DataSelector ds = DataSelector.create(params[0].getString());
					return ncCommand.setDataSelector(ds);
				} catch (Exception e) {}
			}
			else if (command.equals("addSources")) {
				Source[] srcs = toSourceArray(params[0].getCollection());
				return ncCommand.addSources(srcs);
			}
			else if (command.equals("removeSources")) {
				Source[] srcs = toSourceArray(params[0].getCollection());
				return ncCommand.removeSources(srcs);
			}
			else if (command.equals("getAllSources")) {
				return ncCommand.getAllSources();
			}
			else if (command.equals("addNotificationTriggers")) {
				Trigger[] trgs = toTriggerArray(params[0].getCollection());
				return ncCommand.addNotificationTriggers(trgs);
			}
			else if (command.equals("removeNotificationTriggers")) {
				Trigger[] trgs = toTriggerArray(params[0].getCollection());
				return ncCommand.removeNotificationTriggers(trgs);
			}
			else if (command.equals("removeAllNotificationTriggers")) {
				return ncCommand.removeAllNotificationTriggers();
			}
			else if (command.equals("getNotificationTrigger")) {
				return ncCommand.getNotificationTrigger(params[0].getString());
			}
			else if (command.equals("getAllNotificationTriggers")) {
				return ncCommand.getAllNotificationTriggers();
			}
			else if (command.equals("readQueuedData")) {
				NotificationChannelCommand.ReadQueuedData rq = cmdFactory.createNotificationChannelCommandReadQueuedData();
				if (params.length > 0) {
					return ncCommand.readQueuedData(params[0].getBoolean());
				} else {
					return ncCommand.readQueuedData();
				}
			}
		}
		else if (object.equals("Trigger")) {
			TriggerSerializer trgCommand = new TriggerSerializerImpl(getNextId(),target);
			if (command.equals("create")) {
				return trgCommand.create(params[0].getString(),params[1].getString(),params[2].getString());
			}
			else if (command.equals("getMaxNumberSupported")) {
				return trgCommand.getMaxNumberSupported();
			}
			else if (command.equals("getName")) {
				return trgCommand.getName();
			}
			else if (command.equals("getType")) {
				return trgCommand.getType();
			}
			else if (command.equals("getValue")) {
				return trgCommand.getValue();
			}
			else if (command.equals("fire")) {
				return trgCommand.fire();
			}
		
		}
		else if (object.equals("EventType")) {
			EventTypeSerializer evCommand = new EventTypeSerializerImpl(getNextId(), target);
			if (command.equals("getSupportedTypes")) {
				return evCommand.getSupportedTypes();
			}
		}
		else if (object.equals("TriggerType")) {
			TriggerTypeSerializer ttCommand = new TriggerTypeSerializerImpl(getNextId(), target);
			if (command.equals("getSupportedTypes")) {
				return ttCommand.getSupportedTypes();
			}
			
			
		}
		else if (object.equals("FieldName")) {
			FieldNameSerializer fnCommand = new FieldNameSerializerImpl(getNextId(), target);
			if (command.equals("getSupportedNames")) {
				return fnCommand.getSupportedNames();
			}
			
		}
		else if (object.equals("TagField")) {
			TagFieldSerializer tfCommand = new TagFieldSerializerImpl(getNextId(), target);
			if (command.equals("create")) {
				return tfCommand.create(params[0].getString());
			}
			else if (command.equals("getName")) {
				return tfCommand.getName();
			}
			else if (command.equals("getTagFieldName")) {
				return tfCommand.getTagFieldName();
			}
			else if (command.equals("setTagFieldName")) {
				return tfCommand.setTagFieldName(params[0].getString());
			}
			else if (command.equals("getMemoryBank")) {
				return tfCommand.getMemoryBank();
			}
			else if (command.equals("setMemoryBank")) {
				return tfCommand.setMemoryBank(params[0].getInt());
			}
			else if (command.equals("getOffset")) {
				return tfCommand.getOffset();
			}
			else if (command.equals("setOffset")) {
				return tfCommand.setOffset(params[0].getInt());
			}
			else if (command.equals("getLength")) {
				return tfCommand.getLength();
			}
			else if (command.equals("setLength")) {
				return tfCommand.setLength(params[0].getInt());
			}
			
		}
		else if (object.equals("IOPorts")) {
			
		}
		
		String xmlCommand = serializeCommand(cmd);
		return xmlCommand;
		
	}
	
	/**
	 * This method creates text commands
	 * 
	 * @param object the type of the object on which the method should be executed
	 * @param command which should be executed
	 * @param params parameters which belongs to the command
	 * @param target the name of the object on which the method should be executed
	 * @return the command in text format as string
	 * @throws ParameterTypeException if the parameters are illegal
	 */
	public static String getTextCommand(String object, String command, Parameter[] params, String target) throws ParameterTypeException {
		
		ObjectFactory cmdFactory = new ObjectFactory();
		Command cmd = cmdFactory.createCommand();
		cmd.setId(Integer.toString(getNextId()));
		if (!target.equals("")) {
			cmd.setTargetName(target);
		}
		if (object.equals("ReaderDevice")) {
			ReaderDeviceSerializer rdCommand = new org.accada.reader.rp.proxy.msg.stubs.serializers.text.ReaderDeviceSerializerImpl(getNextId(), target);
			if (command.equals("getEPC")) {
				return rdCommand.getEPC();
			}
			else if (command.equals("getManufacturer")) {
				return rdCommand.getManufacturer();
			}
			else if (command.equals("getModel")) {
				return rdCommand.getModel();
			}
			else if (command.equals("getHandle")) {
				return rdCommand.getHandle();
			}
			else if (command.equals("setHandle")) {
				return rdCommand.setHandle(params[0].getInt());
			}
			
			else if (command.equals("getName")) {
				return rdCommand.getName();
			}
			else if (command.equals("setName")) {
				return rdCommand.setName(params[0].getString());
			}
			else if (command.equals("getRole")) {
				return rdCommand.getRole();
			}
			else if (command.equals("setRole")) {
				return rdCommand.setRole(params[0].getString());
			}
			else if (command.equals("getTimeTicks")) {
				return rdCommand.getTimeTicks();
			}
			else if (command.equals("getTimeUTC")) {
				return rdCommand.getTimeUTC();
			}
			else if (command.equals("setTimeUTC")) {
				return rdCommand.setTimeUTC(Calendar.getInstance().getTime()); 
			}
			else if (command.equals("getManufacturerDescription")) {
				return rdCommand.getManufacturerDescription();
			}
			else if (command.equals("getCurrentSource")) {
				return rdCommand.getCurrentSource();
			}
			else if (command.equals("setCurrentSource")) {
				Source src;
				try {
					src = Source.create(params[0].getString());
					return rdCommand.setCurrentSource(src);
				} catch (ReaderProtocolException e) {
					e.printStackTrace();
					return null;
				} 
				
			}
			else if (command.equals("getCurrentDataSelector")) {
				return rdCommand.getCurrentDataSelector();
			}
			else if (command.equals("setCurrentDataSelector")) {
				DataSelector ds;
				try {
					ds = DataSelector.create(params[0].getString());
					return rdCommand.setCurrentDataSelector(ds);
				} catch (ReaderProtocolException e) {
					e.printStackTrace();
					return null;
				}					
			}
			else if (command.equals("removeSources")) {
				Source[] sources = toSourceArray(params[0].getCollection());
				return rdCommand.removeSources(sources);
			}
			else if (command.equals("removeAllSources")) {
				return rdCommand.removeAllSources();
			}
			else if (command.equals("getSource")) {
				return rdCommand.getSource(params[0].getString());
			}
			else if (command.equals("getAllSources")) {
				return rdCommand.getAllSources();
			}
			else if (command.equals("removeDataSelectors")) {
				DataSelector[] ds = toDataSelectorArray(params[0].getCollection());
				return rdCommand.removeDataSelectors(ds);
			}
			else if (command.equals("removeAllDataSelectors")) {
				return rdCommand.removeAllDataSelectors();
			}
			else if (command.equals("getDataSelector")) {
				return rdCommand.getDataSelector(params[0].getString());
			}
			else if (command.equals("getAllDataSelectors")) {
				return rdCommand.getAllDataSelectors();
			}
			else if (command.equals("removeNotificationChannels")) {
				NotificationChannel[] ncs = toNotificationChannelArray(params[0].getCollection());
				return rdCommand.removeNotificationChannels(ncs);
			}
			else if (command.equals("removeAllNotificationChannels")) {
				return rdCommand.removeAllNotificationChannels();
			}
			else if (command.equals("getNotificationChannel")) {
				return rdCommand.getNotificationChannel(params[0].getString());
			}
			else if (command.equals("getAllNotificationChannels")) {
				return rdCommand.getAllNotificationChannels();
			}
			else if (command.equals("removeTriggers")) {
				Trigger[] trgs = toTriggerArray(params[0].getCollection());
				return rdCommand.removeTriggers(trgs);
			}
			else if (command.equals("removeAllTriggers")) {
				return rdCommand.removeAllTriggers();
			}
			else if (command.equals("getTrigger")) {
				return rdCommand.getTrigger(params[0].getString());
			}
			else if (command.equals("getAllTriggers")) {
				return rdCommand.getAllTriggers();
			}
			else if (command.equals("removeTagSelectors")) {
				TagSelector[] tss = toTagSelectorArray(params[0].getCollection());
				return rdCommand.removeTagSelectors(tss);
			}
			else if (command.equals("removeAllTagSelectors")) {
				return rdCommand.removeAllTagSelectors();
			}
			else if (command.equals("getTagSelector")) {
				return rdCommand.getTagSelector(params[0].getString());
			}
			else if (command.equals("getAllTagSelectors")) {
				return rdCommand.getAllTagSelectors();
			}
			else if (command.equals("removeTagFields")) {
				TagField[] tfs = toTagFieldArray(params[0].getCollection());
				return rdCommand.removeTagFields(tfs);
			}
			else if (command.equals("removeAllTagFields")) {
				return rdCommand.removeAllTagFields();
			}
			else if (command.equals("getTagField")) {
				return rdCommand.getTagField(params[0].getString());
			}
			else if (command.equals("getAllTagFields")) {
				return rdCommand.getAllTagFields();
			}
			else if (command.equals("resetToDefaultSettings")) {
				return rdCommand.resetToDefaultSettings();
			}
			else if (command.equals("reboot")) {
				return rdCommand.reboot();
			}
			else if (command.equals("goodbye")) {
				return rdCommand.goodbye();
			}
			else if (command.equals("getReadPoint")) {
				return rdCommand.getReadPoint(params[0].getString());
			}
			else if (command.equals("getAllReadPoints")) {
				return rdCommand.getAllReadPoints();
			}
		}
		else if (object.equals("Source")) {
			SourceSerializer srcCommand = new org.accada.reader.rp.proxy.msg.stubs.serializers.text.SourceSerializerImpl(getNextId(), target);
			if (command.equals("create")) {
				return srcCommand.create(params[0].getString());
			}
			else if (command.equals("getName")) {
				return srcCommand.getName();
			}
			else if (command.equals("isFixed")) {
				return srcCommand.isFixed();
			}
			else if (command.equals("addReadPoints")) {
				ReadPoint[] rps = toReadPointArray(params[0].getCollection());
				return srcCommand.addReadPoints(rps);
			}
			else if (command.equals("removeReadPoints")) {
				ReadPoint[] rps = toReadPointArray(params[0].getCollection());
				return srcCommand.removeReadPoints(rps);
			}
			else if (command.equals("removeAllReadPoints")) {
				return srcCommand.removeAllReadPoints();
			}
			else if (command.equals("getReadPoint")) {
				return srcCommand.getReadPoint(params[0].getString());
			}
			else if (command.equals("getAllReadPoints")) {
				return srcCommand.getAllReadPoints();
			}
			else if (command.equals("addReadTriggers")) {
				Trigger[] rtrg = toTriggerArray(params[0].getCollection());
				return srcCommand.addReadTriggers(rtrg);
			}
			else if (command.equals("removeReadTriggers")) {
				Trigger[] rtrgs = toTriggerArray(params[0].getCollection());
				return srcCommand.removeReadTriggers(rtrgs);
			}
			else if (command.equals("removeAllReadTriggers")) {
				return srcCommand.removeAllReadTriggers();
			}
			else if (command.equals("getReadTrigger")) {
				return srcCommand.getReadTrigger(params[0].getString());
			}
			else if (command.equals("getAllReadTriggers")) {
				return srcCommand.getAllReadTriggers();
			}
			else if (command.equals("addTagSelectors")) {
				TagSelector[] tss = toTagSelectorArray(params[0].getCollection());
				return srcCommand.addTagSelectors(tss);
			}
			else if (command.equals("removeTagSelectors")) {
				TagSelector[] tss = toTagSelectorArray(params[0].getCollection());
				return srcCommand.removeTagSelectors(tss);
			}
			else if (command.equals("removeAllTagSelectors")) {
				return srcCommand.removeAllTagSelectors();
			}
			else if (command.equals("getTagSelector")) {
				return srcCommand.getTagSelector(params[0].getString());
			}
			else if (command.equals("getAllTagSelectors")) {
				return srcCommand.getAllTagSelectors();
			}
			else if (command.equals("getGlimpsedTimeout")) {
				return srcCommand.getGlimpsedTimeout();
			}
			else if (command.equals("setGlimpsedTimeout")) {
				return srcCommand.setGlimpsedTimeout(params[0].getInt());
			}
			else if (command.equals("getObservedThreshold")) {
				return srcCommand.getObservedThreshold();
			}
			else if (command.equals("setObservedThreshold")) {
				return srcCommand.setObservedThreshold(params[0].getInt());
			}
			else if (command.equals("getObservedTimeout")) {
				return srcCommand.getObservedTimeout();
			}
			else if (command.equals("setObservedTimeout")) {
				return srcCommand.setObservedTimeout(params[0].getInt());
			}
			else if (command.equals("getLostTimeout")) {
				return srcCommand.getLostTimeout();
			}
			else if (command.equals("setLostTimeout")) {
				return srcCommand.setLostTimeout(params[0].getInt());
			}
			else if (command.equals("rawReadIDs")) {
				SourceCommand.RawReadIDs r = cmdFactory.createSourceCommandRawReadIDs();
				if (params.length > 0) { //optional parameter is set
					try{
						DataSelector ds = DataSelector.create(params[0].getString());
						return srcCommand.rawReadIDs(ds);
					} catch (Exception e) {}
				} else {
					return srcCommand.rawReadIDs(null);
				}
				
			}
			else if (command.equals("readIDs")) {
				SourceCommand.ReadIDs r = cmdFactory.createSourceCommandReadIDs();
				if (params.length > 0) { //optional parameter is set
					try	{
						DataSelector ds = DataSelector.create(params[0].getString());
						return srcCommand.readIDs(ds);
					} catch (Exception e) {}
				} else {
					return srcCommand.readIDs(null);
				}
				
			}
			else if (command.equals("read")) {
				SourceCommand.Read r = cmdFactory.createSourceCommandRead();
				if (params.length > 0) { //optional parameter is set
					try {
						DataSelector ds = DataSelector.create(params[0].getString());
						return srcCommand.read(ds, null); //TODO: set Passwords
					} catch (Exception e) {}
				} else {
					return srcCommand.read(null, null);
				}
			}
			else if (command.equals("writeID")) {
				TagSelector[] tagSelectors = null;
				String[] passwords = null;
				
				if (params.length > 1) {
					passwords = params[1].getArray();
				}
				if (params.length > 2) {
					tagSelectors = toTagSelectorArray(params[2].getCollection());
				}
				
				return srcCommand.writeID(params[0].getString(), passwords, tagSelectors);
			}
			else if (command.equals("write")) {
				TagFieldValue[] tagFieldValues = null;
				TagSelector[] tagSelectors = null;
				String[] passwords = null;
				
				tagFieldValues = toTagFieldValueArray(params[0].getCollection());
				if (params.length > 1) {
					passwords = params[1].getArray();
				}
				if (params.length > 2) {
					tagSelectors = toTagSelectorArray(params[2].getCollection());
				}
				return srcCommand.write(tagFieldValues, passwords, tagSelectors);
			}
			else if (command.equals("kill")) {
				String[] passwords = null;
				TagSelector[] selectors = null;
				if (params.length > 0) {
					passwords = params[0].getArray();
				}
				if (params.length > 1) {
					selectors = toTagSelectorArray(params[1].getCollection());
				}
				return srcCommand.kill(passwords, selectors);
			}
			else if (command.equals("getReadCyclesPerTrigger")) {
				return srcCommand.getReadCyclesPerTrigger();
			}
			else if (command.equals("getMaxReadDutyCycle")) {
				return srcCommand.getMaxReadDutyCycles();
			}
			else if (command.equals("setReadCyclesPerTrigger")) {
				return srcCommand.setReadCyclesPerTrigger(params[0].getInt());
			}
			else if (command.equals("setMaxReadDutyCycle")) {
				return srcCommand.setMaxReadDutyCycles(params[0].getInt());
			}
			else if (command.equals("setReadTimeout")) {
				return srcCommand.setReadTimeout(params[0].getInt());
			}
			else if (command.equals("getReadTimeout")) {
				return srcCommand.getReadTimeout();
			}
			else if (command.equals("getSession")) {
				return srcCommand.getSession();
			}
			else if (command.equals("setSession")) {
				return srcCommand.setSession(params[0].getInt());
			}			
		}
		else if (object.equals("ReadPoint")) {
			ReadPointSerializer rpCommand = new org.accada.reader.rp.proxy.msg.stubs.serializers.text.ReadPointSerializerImpl(getNextId(), target);
			if (command.equals("getName")) {
				return rpCommand.getName();
			}
			
		}
		else if (object.equals("TagSelector")) {
			TagSelectorSerializer tsCommand = new org.accada.reader.rp.proxy.msg.stubs.serializers.text.TagSelectorSerializerImpl(getNextId(), target);
			if (command.equals("create")) {
				try {
					TagField tf = TagField.create(params[1].getString());
					return tsCommand.create(params[0].getString(),tf,params[2].getString(), params[3].getString(),params[4].getBoolean());
				} catch (Exception e) {}
			}
			else if (command.equals("getName")) {
				return tsCommand.getName();
			}
			else if (command.equals("getMaxNumberSupported")) {
				return tsCommand.getMaxNumberSupported();
			}
			else if (command.equals("getTagField")) {
				return tsCommand.getTagField();
			}
			else if (command.equals("getValue")) {
				return tsCommand.getValue();
			}
			else if (command.equals("getMask")) {
				return tsCommand.getMask();
			}
			else if (command.equals("getInclusiveFlag")) {
				return tsCommand.getInclusiveFlag();
			}
		}
		else if (object.equals("DataSelector")) {
			DataSelectorSerializer dsCommand = new org.accada.reader.rp.proxy.msg.stubs.serializers.text.DataSelectorSerializerImpl(getNextId(), target);
			if (command.equals("create")) {
				return dsCommand.create(params[0].getString());
			}
			else if (command.equals("getName")) {
				return dsCommand.getName();
			}
			else if (command.equals("addFieldNames")) {
				return dsCommand.addFieldNames(params[0].getArray());
			}
			else if (command.equals("removeFieldNames")) {
				return dsCommand.removeFieldNames(params[0].getArray());
			}
			else if (command.equals("removeAllFieldNames")) {
				return dsCommand.removeAllFieldNames();
			}
			else if (command.equals("getAllFieldNames")) {
				return dsCommand.getAllFieldNames();
			}
			else if (command.equals("addEventFilters")) {
				return dsCommand.addEventFilters(params[0].getArray());
			}
			else if (command.equals("removeEventFilters")) {
				return dsCommand.removeEventFilters(params[0].getArray());
			}
			else if (command.equals("removeAllEventFilters")) {
				return dsCommand.removeAllEventFilters();
			}
			else if (command.equals("getAllEventFilters")) {
				return dsCommand.getAllEventFilters();
			}
			else if (command.equals("addTagFieldNames")) {
				return dsCommand.addTagFieldNames(params[0].getArray());
			}
			else if (command.equals("removeTagFieldNames")) {
				return dsCommand.removeTagFieldNames(params[0].getArray());
			}
			else if (command.equals("removeAllTagFieldNames")) {
				return dsCommand.removeAllTagFieldNames();
			}
			else if (command.equals("getAllTagFieldNames")) {
				return dsCommand.getAllTagFieldNames();
			}
		}
		else if (object.equals("NotificationChannel")) {
			NotificationChannelSerializer ncCommand = new org.accada.reader.rp.proxy.msg.stubs.serializers.text.NotificationChannelSerializerImpl(getNextId(), target);
			if (command.equals("create")) {
				return ncCommand.create(params[0].getString(), params[1].getString());
			}
			else if (command.equals("getName")) {
				return ncCommand.getName();
			}
			else if (command.equals("getAddress")) {
				return ncCommand.getAddress();
			}
			else if (command.equals("getEffectiveAddress")) {
				return ncCommand.getEffectiveAddress();
			}
			else if (command.equals("setAddress")) {
				return ncCommand.setAddress(params[0].getString());
			}
			else if (command.equals("getDataSelector")) {
				return ncCommand.getDataSelector();
			}
			else if (command.equals("setDataSelector")) {
				try {
					DataSelector ds = DataSelector.create(params[0].getString());
					return ncCommand.setDataSelector(ds);
				} catch (Exception e) {}
			}
			else if (command.equals("addSources")) {
				Source[] srcs = toSourceArray(params[0].getCollection());
				return ncCommand.addSources(srcs);
			}
			else if (command.equals("removeSources")) {
				Source[] srcs = toSourceArray(params[0].getCollection());
				return ncCommand.removeSources(srcs);
			}
			else if (command.equals("getAllSources")) {
				return ncCommand.getAllSources();
			}
			else if (command.equals("addNotificationTriggers")) {
				Trigger[] trgs = toTriggerArray(params[0].getCollection());
				return ncCommand.addNotificationTriggers(trgs);
			}
			else if (command.equals("removeNotificationTriggers")) {
				Trigger[] trgs = toTriggerArray(params[0].getCollection());
				return ncCommand.removeNotificationTriggers(trgs);
			}
			else if (command.equals("removeAllNotificationTriggers")) {
				return ncCommand.removeAllNotificationTriggers();
			}
			else if (command.equals("getNotificationTrigger")) {
				return ncCommand.getNotificationTrigger(params[0].getString());
			}
			else if (command.equals("getAllNotificationTriggers")) {
				return ncCommand.getAllNotificationTriggers();
			}
			else if (command.equals("readQueuedData")) {
				NotificationChannelCommand.ReadQueuedData rq = cmdFactory.createNotificationChannelCommandReadQueuedData();
				if (params.length > 0) {
					return ncCommand.readQueuedData(params[0].getBoolean());
				} else {
					return ncCommand.readQueuedData();
				}
			}
		}
		else if (object.equals("Trigger")) {
			TriggerSerializer trgCommand = new org.accada.reader.rp.proxy.msg.stubs.serializers.text.TriggerSerializerImpl(getNextId(),target);
			if (command.equals("create")) {
				return trgCommand.create(params[0].getString(),params[1].getString(),params[2].getString());
			}
			else if (command.equals("getMaxNumberSupported")) {
				return trgCommand.getMaxNumberSupported();
			}
			else if (command.equals("getName")) {
				return trgCommand.getName();
			}
			else if (command.equals("getType")) {
				return trgCommand.getType();
			}
			else if (command.equals("getValue")) {
				return trgCommand.getValue();
			}
			else if (command.equals("fire")) {
				return trgCommand.fire();
			}
		
		}
		else if (object.equals("EventType")) {
			EventTypeSerializer evCommand = new org.accada.reader.rp.proxy.msg.stubs.serializers.text.EventTypeSerializerImpl(getNextId(), target);
			if (command.equals("getSupportedTypes")) {
				return evCommand.getSupportedTypes();
			}
		}
		else if (object.equals("TriggerType")) {
			TriggerTypeSerializer ttCommand = new org.accada.reader.rp.proxy.msg.stubs.serializers.text.TriggerTypeSerializerImpl(getNextId(), target);
			if (command.equals("getSupportedTypes")) {
				return ttCommand.getSupportedTypes();
			}
			
			
		}
		else if (object.equals("FieldName")) {
			FieldNameSerializer fnCommand = new org.accada.reader.rp.proxy.msg.stubs.serializers.text.FieldNameSerializerImpl(getNextId(), target);
			if (command.equals("getSupportedNames")) {
				return fnCommand.getSupportedNames();
			}
			
		}
		else if (object.equals("TagField")) {
			TagFieldSerializer tfCommand = new org.accada.reader.rp.proxy.msg.stubs.serializers.text.TagFieldSerializerImpl(getNextId(), target);
			if (command.equals("create")) {
				return tfCommand.create(params[0].getString());
			}
			else if (command.equals("getName")) {
				return tfCommand.getName();
			}
			else if (command.equals("getTagFieldName")) {
				return tfCommand.getTagFieldName();
			}
			else if (command.equals("setTagFieldName")) {
				return tfCommand.setTagFieldName(params[0].getString());
			}
			else if (command.equals("getMemoryBank")) {
				return tfCommand.getMemoryBank();
			}
			else if (command.equals("setMemoryBank")) {
				return tfCommand.setMemoryBank(params[0].getInt());
			}
			else if (command.equals("getOffset")) {
				return tfCommand.getOffset();
			}
			else if (command.equals("setOffset")) {
				return tfCommand.setOffset(params[0].getInt());
			}
			else if (command.equals("getLength")) {
				return tfCommand.getLength();
			}
			else if (command.equals("setLength")) {
				return tfCommand.setLength(params[0].getInt());
			}
			
		}
		else if (object.equals("IOPorts")) {
			
		}
		
		String xmlCommand = serializeCommand(cmd);
		return xmlCommand;
		
	}
	
	//
	// private methods
	//
	
	/**
	 * This method serialize a command.
	 * 
	 * @param cmd command to serialize
	 * @return the serialized command as string
	 */
	private static String serializeCommand(Command cmd) {
		
		try {
			JAXBContext ctx = JAXBContext.newInstance("org.accada.reader.rprm.core.msg.command");
			Marshaller marshaller = ctx.createMarshaller();
	        marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
	        StringWriter sw = new StringWriter();
			try {
				marshaller.marshal(cmd,sw);
			} catch (JAXBException e) {
				throw new MessageSerializingException(e);
			}
			return sw.getBuffer().toString();
		} catch (Exception e) {
			return "";
		}
		
	}
	
	/**
	 * This method returns the next message id.
	 * 
	 * @return next message id
	 */
	private static int getNextId() {
		
		counter++;
		return counter;
		
	}
	
	/**
	 * This method transforms a collection of sources to a source array.
	 * 
	 * @param collection of sources
	 * @return source array
	 */
	private static Source[] toSourceArray(Collection collection) {
		
		try {
			Source[] sources = new Source[collection.size()];
			Iterator it = collection.iterator();
			int index = 0;
			while(it.hasNext()) {
				Source src = Source.create((String)it.next());
				sources[index] = src;
				index++;
			}
			return sources;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * This method transforms a collection of data selectors to a data selector array.
	 * 
	 * @param collection of data selectors
	 * @return data selector array
	 */
	private static DataSelector[] toDataSelectorArray(Collection collection) {
		
		try {
			DataSelector[] dataSelectors = new DataSelector[collection.size()];
			Iterator it = collection.iterator();
			int index = 0;
			while(it.hasNext()) {
				DataSelector ds = DataSelector.create((String)it.next());
				dataSelectors[index] = ds;
				index++;
			}
			return dataSelectors;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * This method transforms a collection of notification channels to a notification channel array.
	 * 
	 * @param collection of notification channels
	 * @return notifiction channel array
	 */
	private static NotificationChannel[] toNotificationChannelArray(Collection collection) {
		
		try {
			NotificationChannel[] notificationChannels = new NotificationChannel[collection.size()];
			Iterator it = collection.iterator();
			int index = 0;
			while(it.hasNext()) {
				NotificationChannel nc = NotificationChannel.create((String)it.next(), null);
				notificationChannels[index] = nc;
				index++;
			}
			return notificationChannels;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * This method transforms a collection of triggers to a trigger array.
	 * 
	 * @param collection of triggers
	 * @return trigger array
	 */
	private static Trigger[] toTriggerArray(Collection collection) {
		
		try {
			Trigger[] triggers = new Trigger[collection.size()];
			Iterator it = collection.iterator();
			int index = 0;
			while(it.hasNext()) {
				Trigger trg = Trigger.create((String)it.next());
				triggers[index] = trg;
				index++;
			}
			return triggers;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * This method transforms a collection of tag selectors to a tag selector array.
	 * 
	 * @param collection of tag selectors
	 * @return tag selector array
	 */
	private static TagSelector[] toTagSelectorArray(Collection collection) {
		
		try {
			TagSelector[] tagSelectors = new TagSelector[collection.size()];
			Iterator it = collection.iterator();
			int index = 0;
			while(it.hasNext()) {
				TagSelector ts = TagSelector.create((String)it.next(),null,null,null,false);
				tagSelectors[index] = ts;
				index++;
			}
			return tagSelectors;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * This method transforms a collection of tag fields to a tag field array.
	 * 
	 * @param collection of tag fields
	 * @return tag field array
	 */
	private static TagField[] toTagFieldArray(Collection collection) {
		
		try {
			TagField[] tagFields = new TagField[collection.size()];
			Iterator it = collection.iterator();
			int index = 0;
			while(it.hasNext()) {
				TagField tf = TagField.create((String)it.next());
				tagFields[index] = tf;
				index++;
			}
			return tagFields;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * This method transforms a collection of read point to a read point array.
	 * 
	 * @param collection of read points
	 * @return read point array
	 */
	private static ReadPoint[] toReadPointArray(Collection collection) {
		
		try {
			ReadPoint[] readPoints = new ReadPoint[collection.size()];
			Iterator it = collection.iterator();
			int index = 0;
			while(it.hasNext()) {
				ReadPoint rp = ReadPoint.create((String)it.next(),null,null);
				readPoints[index] = rp;
				index++;
			}
			return readPoints;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * This method transforms a collection of tag field values to a tag field value array.
	 * 
	 * @param collection of tag field values
	 * @return tag field value array
	 */
	private static TagFieldValue[] toTagFieldValueArray(Collection collection) {
		
		try {
			TagFieldValue[] readPoints = new TagFieldValue[collection.size()];
			Iterator it = collection.iterator();
			int index = 0;
			while(it.hasNext()) {
				//TODO: implementation not finished
				String tagFieldValue = (String)it.next();
				int commaPosition = tagFieldValue.indexOf(';');
				TagFieldValue rp = new TagFieldValue(tagFieldValue.substring(1, commaPosition), tagFieldValue.substring(commaPosition + 1, tagFieldValue.length() - 1));
				readPoints[index] = rp;
				index++;
			}
			return readPoints;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

}