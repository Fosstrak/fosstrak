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

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

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
import org.accada.reader.rp.proxy.msg.stubs.serializers.CommandSerializer;
import org.accada.reader.rprm.core.msg.MessageSerializingException;
import org.accada.reader.rprm.core.msg.command.Command;
import org.accada.reader.rprm.core.msg.command.ObjectFactory;
import org.accada.reader.rprm.core.msg.command.TagFieldValueParamType;
import org.accada.reader.rprm.core.msg.command.TextCommandParserHelper;
import org.accada.reader.rprm.core.msg.util.HexUtil;

/**
 * @author Andreas
 * 
 */
public class CommandSerializerImpl implements CommandSerializer {

	/** the command id */
	private int id;

	/** the target name */
	private String targetName;

	/** the command factory */
	protected ObjectFactory cmdFactory = null;

	/** the command */
	protected Command command = null;

	private CommandSerializerImpl() {
		cmdFactory = new ObjectFactory();
		command = cmdFactory.createCommand();
	}

	public CommandSerializerImpl(String targetName) {
		this();
		setTargetName(targetName);
	}

	public CommandSerializerImpl(int id) {
		this();
		setId(id);
	}

	public CommandSerializerImpl(int id, String targetName) {
		this();
		setId(id);
		setTargetName(targetName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.CommandSerializer#setId(java.lang.String)
	 */
	public void setId(int id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.accada.reader.testclient.command.CommandSerializer#setTargetName(java.lang.String)
	 */
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	/**
	 * Serializes the command.
	 */
	public String serializeCommand() {
		try {
			command.setTargetName(targetName);
			command.setId(Integer.toString(id));

			JAXBContext ctx = JAXBContext
					.newInstance("org.accada.reader.rprm.core.msg.command");
			Marshaller marshaller = ctx.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
					Boolean.TRUE);
			StringWriter sw = new StringWriter();
			try {
				marshaller.marshal(command, sw);
			} catch (JAXBException e) {
				throw new MessageSerializingException(e);
			}
			return sw.getBuffer().toString();
		} catch (Exception e) {
			return "";
		}
	}

	public String toString() {
		return serializeCommand();
	}

	protected void resetCommand() {
		command.setDataSelector(null);
		command.setEventType(null);
		command.setFieldName(null);
		command.setNotificationChannel(null);
		command.setReaderDevice(null);
		command.setReadPoint(null);
		command.setSource(null);
		command.setTagField(null);
		command.setTagSelector(null);
		command.setTrigger(null);
		command.setTriggerType(null);
	}

	/**
	 * Converts a String array into a List of Strings
	 * 
	 * @param strArray
	 *            The String array
	 * @return the List of Strings
	 */
	public List toStringList(String[] strArray) {
		List list = new ArrayList(strArray.length);
		for (int i = 0; i < strArray.length; i++) {
			list.add(strArray[i]);
		}
		return list;
	}

	/**
	 * Converts a Source array into a List of Strings
	 * 
	 * @param srcArray
	 *            The Source array
	 * @return the List of Strings
	 */
	public List toStringList(Source[] srcArray) {
		List list = new ArrayList(srcArray.length);
		for (int i = 0; i < srcArray.length; i++) {
			list.add(srcArray[i].getName());
		}
		return list;
	}

	/**
	 * Converts a Trigger array into a List of Strings
	 * 
	 * @param trgArray
	 *            The Trigger array
	 * @return the List of Strings
	 */
	public List toStringList(Trigger[] trgArray) {
		List list = new ArrayList(trgArray.length);
		for (int i = 0; i < trgArray.length; i++) {
			list.add(trgArray[i].getName());
		}
		return list;
	}

	/**
	 * Converts a DataSelector array into a List of Strings
	 * 
	 * @param dsArray
	 *            The DataSelector array
	 * @return the List of Strings
	 */
	public List toStringList(DataSelector[] dsArray) {
		List list = new ArrayList(dsArray.length);
		for (int i = 0; i < dsArray.length; i++) {
			list.add(dsArray[i].getName());
		}
		return list;
	}

	/**
	 * Converts a NotificationChannel array into a List of Strings
	 * 
	 * @param ncArray
	 *            The NotificationChannel array
	 * @return the List of Strings
	 */
	public List toStringList(NotificationChannel[] ncArray) {
		List list = new ArrayList(ncArray.length);
		for (int i = 0; i < ncArray.length; i++) {
			list.add(ncArray[i].getName());
		}
		return list;
	}

	/**
	 * Converts a TagSelector array into a List of Strings
	 * 
	 * @param tsArray
	 *            The TagSelector array
	 * @return the List of Strings
	 */
	public List toStringList(TagSelector[] tsArray) {
		List list = new ArrayList(tsArray.length);
		for (int i = 0; i < tsArray.length; i++) {
			list.add(tsArray[i].getName());
		}
		return list;
	}

	/**
	 * Converts a TagField array into a List of Strings
	 * 
	 * @param tfArray
	 *            The TagField array
	 * @return the List of Strings
	 */
	public List toStringList(TagField[] tfArray) {
		List list = new ArrayList(tfArray.length);
		for (int i = 0; i < tfArray.length; i++) {
			list.add(tfArray[i].getName());
		}
		return list;
	}

	/**
	 * Converts a ReadPoint array into a List of Strings
	 * 
	 * @param rpArray
	 *            The ReadPoint array
	 * @return the List of Strings
	 */
	public List toStringList(ReadPoint[] rpArray) {
		List list = new ArrayList(rpArray.length);
		for (int i = 0; i < rpArray.length; i++) {
			list.add(rpArray[i].getName());
		}
		return list;
	}

	/**
	 * Creates a copy of the internal list of parameters. The list items are
	 * represented as <code>byte[]</code> used in <source>source.writeID(...)</code>.
	 * 
	 * @param strArray
	 *            Array of Strings with hex values (passwords, tag ids, ...)
	 * @return List of <code>byte[]</code>
	 */
	public List toHexStringList(String[] strArray) {

		List hexList = new ArrayList(strArray.length);
		for (int i = 0; i < strArray.length; i++) {
			byte[] hexArray = HexUtil.hexToByteArray(strArray[i]);
			hexList.add(hexArray);
		}
		return hexList;

	}

	/**
	 * Creates a copy of the internal list of parameters. The list items are
	 * represented as <code>TagFieldValueParamType</code> used in
	 * <source>source.write(...)</code>.
	 * 
	 * @param tfArray
	 *            TagFieldValue array
	 * @return List of <code>TagFieldValueParamType</code>
	 * @throws JAXBException
	 * @see org.accada.reader.msg.command.TagFieldValueParamType
	 */
	public List toTagFieldValueList(TagFieldValue[] tfArray)
			throws JAXBException {
		List tfvList = new ArrayList(tfArray.length);
		for (int i = 0; i < tfArray.length; i++) {
			TagFieldValueParamType tfvParam = TextCommandParserHelper.cmdFactory
					.createTagFieldValueParamType();
			tfvParam.setTagFieldName(tfArray[i].getTagFieldName());
			tfvParam.setTagFieldValue(HexUtil.hexToByteArray(tfArray[i]
					.getValue()));
			tfvList.add(tfvParam);
		}
		return tfvList;

	}

	public void setProperty(String name, Object value) {
		//No properties for XML implementation, ignore it.		
	}
	
	
}
