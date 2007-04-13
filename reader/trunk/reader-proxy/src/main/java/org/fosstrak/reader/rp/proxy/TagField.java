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

package org.accada.reader.rp.proxy;



public interface TagField {

	/**
	 * Returns the name of the given tag field object. This name should not be
	 * confused with the TagFieldName attribute of the object. A TagField
	 * objects name shall be unique whereas a TagField objects TagFieldName
	 * attribute can be shared
	 * @return The name of the TagField.
	 */
	String getName() throws RPProxyException;

	/**
	 * Returns the tag field name of this TagField object. This TagFieldName
	 * attribute is not the same entity as the TagField objects name. Two or
	 * more TagField objects can share the same TagFieldName attributes. This may
	 * be necessary to associate tag data fields that carry the same type of
	 * application data but exist in different locations on different tag types
	 * @return The TagFieldName of the TagField
	 */
	String getTagFieldName() throws RPProxyException;

	/**
	 * Sets the TagFieldName attribute for this TagField object.
	 * @param tagFieldName
	 *           The TagFieldName to set
	 */
	void setTagFieldName(final String tagFieldName) throws RPProxyException;

	/**
	 * Returns the value corresponding to the TagField objects memory bank
	 * attribute. The application of this attribute is dependent upon a tags RF
	 * protocol.
	 * @return The memory bank
	 */
	int getMemoryBank() throws RPProxyException;

	/**
	 * Sets the memory bank.
	 * @param memoryBank
	 *           The memory bank to set
	 */
	void setMemoryBank(final int memoryBank) throws RPProxyException;

	/**
	 * Returns the offset of the data.
	 * @return The offset of the data
	 */
	int getOffset() throws RPProxyException;

	/**
	 * Sets the offset of the data.
	 * @param offset
	 *           The offset of the data to set
	 */
	void setOffset(final int offset) throws RPProxyException;

	/**
	 * Returns the length of the data.
	 * @return The lenght of the data
	 */
	int getLength() throws RPProxyException;

	/**
	 * Sets the length of the data.
	 * @param length
	 *           The lenght of the data
	 */
	void setLength(final int length) throws RPProxyException;

}