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
package org.fosstrak.reader.rp.proxy.msg.stubs.serializers.text;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.fosstrak.reader.rprm.core.msg.command.TagFieldValueParamType;
import org.fosstrak.reader.rprm.core.msg.command.TextCommandParserHelper;
import org.fosstrak.reader.rprm.core.msg.util.HexUtil;
import org.fosstrak.reader.rp.proxy.msg.stubs.DataSelector;
import org.fosstrak.reader.rp.proxy.msg.stubs.NotificationChannel;
import org.fosstrak.reader.rp.proxy.msg.stubs.ReadPoint;
import org.fosstrak.reader.rp.proxy.msg.stubs.Source;
import org.fosstrak.reader.rp.proxy.msg.stubs.TagField;
import org.fosstrak.reader.rp.proxy.msg.stubs.TagFieldValue;
import org.fosstrak.reader.rp.proxy.msg.stubs.TagSelector;
import org.fosstrak.reader.rp.proxy.msg.stubs.Trigger;
import org.fosstrak.reader.rp.proxy.msg.stubs.serializers.CommandSerializer;

/**
 * @author Andreas
 * 
 */

//TODO: Javadoc vervollständigen
public class CommandSerializerImpl implements CommandSerializer {

	/* String tokens */
	//TODO unbenutzte konstanten entfernen
	//TODO Token konstanten in interface TextTokens auslagern
	//TODO: unbenutzte methoden entfernen
	public static final String EXCLAMATION = "!";

	public static final String TERMINATOR = ">";

	public static final String COMMA = ",";

	public static final String DOUBLEQUOTE = "\"";
	
	public static final String SHARP = "#";
	
	public static final String DOT = ".";

	public static final String LF = "\n";

	public static final String ERR = "ERR";

	public static final String OK = "OK";

	public static final String GOODBYE = "GOODBYE";

	public static final String ISO_8601_MILLIS_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSz"; //$NON-NLS-1$
	
	public static final String LIST_BEGIN = "{";
	
	public static final String LIST_END = "}";
	
	public static final String PROPERTY_TEXT_MODE = "MODE";
	
	public static final String PROPERTY_TEXT_MODE_SHORT = "SHORT";
	
	public static final String PROPERTY_TEXT_MODE_LONG = "LONG";
	
	/** the command id */
	private int id;

	/** the target name */
	private String targetName = null;
	
	/** the object type */
	private String objectTypeName = null;

	/** the command */
	protected String commandName = null;
	
	/** the property for the mode of the text message (short form, long form) */
	protected boolean shortMode;
	
	/** the string with the parameter values */
	protected String parameterString;

	private CommandSerializerImpl() {
		//default property setting: short text mode
		shortMode = true;
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
	 * @see org.fosstrak.reader.testclient.command.CommandSerializer#setId(java.lang.String)
	 */
	public void setId(int id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.CommandSerializer#setTargetName(java.lang.String)
	 */
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
	
	/**
	 * Sets the object type name.
	 * @param objectName
	 */
	public void setObjectTypeName(String objectName) {
		this.objectTypeName = objectName;
	}
	
	/**
	 * Sets the command.
	 * @param command
	 */
	public void setCommandName(String command){
		this.commandName = command;
	}
	

	/**
	 * Serializes the command.
	 */
	public String serializeCommand() {
		try {
			StringWriter sw = new StringWriter();
			sw.write(EXCLAMATION);
			sw.write(id + " ");
			if(objectTypeName != null) {
				sw.write(objectTypeName);
				if(targetName != null && !targetName.equals("")) {
					sw.write(SHARP);
					sw.write(targetName);
				}
				sw.write(DOT);
			}
			sw.write(commandName);
			if(parameterString != null) {
				sw.write(" ");
				sw.write(parameterString);
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
	 * @see org.fosstrak.reader.msg.command.TagFieldValueParamType
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
		if(name.equalsIgnoreCase(PROPERTY_TEXT_MODE)) {
			if(value instanceof String && ((String)value).equalsIgnoreCase(PROPERTY_TEXT_MODE_SHORT)) {
				shortMode = true;
			} else {
				shortMode = false;
			}
		}
		
	}
	
	public void setParameter(int i) {
		parameterString = Integer.toString(i);
	}
	
	public void setParameter(String s) {
		parameterString = s;
	}
	
	public void setParameter(Date d) {
		//TODO impl of date siehe iso8601 schon gebraucht
	}
	
	public void setParameter(boolean b) {
		//TODO wie soll boolean serialisiert werden??
		parameterString = (b?"true":"false");
	}
	
	public void setParameterList(Object[] objArray) {
		StringBuffer buf = new StringBuffer();
		buf.append(LIST_BEGIN);
		for(int i=0; i < objArray.length; i++) {
			buf.append(objArray.toString());
			if(i < objArray.length) {
				buf.append(COMMA);
			}
		}
		buf.append(LIST_END);
		parameterString = buf.toString();
	}
	
	
}
