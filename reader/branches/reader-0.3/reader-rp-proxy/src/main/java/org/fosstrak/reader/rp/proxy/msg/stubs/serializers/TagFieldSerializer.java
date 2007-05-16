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

package org.accada.reader.rp.proxy.msg.stubs.serializers;


/**
 * TagFieldSerializer objects serialize a command on a TagField into a String
 * representation.
 * 
 * @author Andreas Fürer, ETH Zurich Switzerland, Winter 2005/06
 * 
 */
public interface TagFieldSerializer {

	/**
	 * @param name
	 *            The name of the tagfield
	 */
	public String create(final String name);

	/**
	 */
	public String getName();

	/**
	 */
	public String getTagFieldName();

	/**
	 * @param tagFieldName
	 *            The TagFieldName to set
	 */
	public String setTagFieldName(final String tagFieldName);

	/**
	 */
	public String getMemoryBank();

	/**
	 * @param memoryBank
	 *            The memory bank to set
	 */
	public String setMemoryBank(final int memoryBank);

	/**
	 */
	public String getOffset();

	/**
	 * @param offset
	 *            The offset of the data to set
	 */
	public String setOffset(final int offset);

	/**
	 * @return The lenght of the data
	 */
	public String getLength();

	/**
	 * @param length
	 *            The lenght of the data
	 */
	public String setLength(final int length);

	/**
	 * Serializes a TagField command.
	 */
	public String serializeCommand();
}
