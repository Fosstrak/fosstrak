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

import java.util.Collection;
import java.util.StringTokenizer;
import java.util.Vector;


public class Parameter {
	private Object value;
	private ParameterType type;
	
	public Parameter(String strValue, ParameterType type) throws ClassCastException {
		setParameter(strValue, type);
	}
	
	public void setParameter(String strValue, ParameterType type) {
		try {
			setParameterType(type);
			if (type.getType() == String.class) {
				value = strValue;
			}
			else if (type.getType() == Integer.class) {
				value = new Integer(Integer.parseInt(strValue));
			}
			else if (type.getType() == Boolean.class) {
				value = new Boolean(Boolean.getBoolean(strValue));
			}
			else if (type.getType() == Collection.class) {
				Vector v = new Vector();
				if (strValue != null) {
					StringTokenizer tokenizer = new StringTokenizer(strValue, ",");
					while(tokenizer.hasMoreTokens()) {
						String token = tokenizer.nextToken();
						v.add(token.trim());
					}
				}
				value = v;
			}
		} catch(NumberFormatException e) {
			throw new ClassCastException("Wrong parameter format. Could not convert " + strValue + " into " + type.getName());
		} 
	}
	/**
	 * @return Returns the value.
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * @return Returns the ParameterType.
	 */
	public ParameterType getParameterType() {
		return type;
	}
	/**
	 * @param type The ParameterType to set.
	 */
	public void setParameterType(ParameterType type) {
		this.type = type;
	}
	
	
	public String getString() throws ParameterTypeException {
		try {
			return (String)value;
		} catch (ClassCastException e) {
			throw new ParameterTypeException("A string was expected instead of a " + type.getName() + ".");
		}
	}
	
	public boolean getBoolean() throws ParameterTypeException {
		try {
			return ((Boolean)value).booleanValue();
		} catch (ClassCastException e) {
			throw new ParameterTypeException("A boolean was expected instead of a " + type.getName() + ".");
		}
	}
	
	public int getInt() throws ParameterTypeException {
		try {
			return ((Integer)value).intValue();
		} catch (ClassCastException e) {
			throw new ParameterTypeException("An integer was expected instead of a " + type.getName() + ".");
		}
	}
	
	public Collection getCollection() throws ParameterTypeException {
		try {
			return (Collection)value;
		} catch (ClassCastException e) {
			throw new ParameterTypeException("An array was expected instead of a " + type.getName() + ".");
		}
	}
	
	public String[] getArray() throws ParameterTypeException {
		Collection coll = getCollection();
		String[] strArray = new String[coll.size()]; 
		coll.toArray(strArray);
		return strArray;
	}
	
	public String toString() {
		return value + " [" + type.getName() + "]";
	}
}

