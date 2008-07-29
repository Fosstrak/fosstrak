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

package org.fosstrak.reader.rp.proxy;


public class TagFieldValueImpl implements org.fosstrak.reader.rp.proxy.TagFieldValue {

	private TagField tagField;
	private String value;

	public TagField getTagField() throws RPProxyException {
		
		return tagField;
		
	}

	public String getValue() throws RPProxyException {

		return value;
		
	}

	public void setTagField(TagField tagField) throws RPProxyException {
		
		this.tagField = tagField;
		
	}

	public void setValue(String value) throws RPProxyException {
		
		this.value = value;
		
	}
	
	public String toString() {

		return "[" + tagField.toString() + ";" + value + "]";
		
	}
	
}