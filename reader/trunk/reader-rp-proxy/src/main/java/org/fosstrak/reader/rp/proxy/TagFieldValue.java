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



public interface TagFieldValue {

	/**
	 * Returns the tagField.
	 * @return Returns the tagField
	 */
	TagField getTagField() throws RPProxyException;

	/**
	 * Sets the tag field.
	 * @param tagField
	 *           The tagField to set
	 */
	void setTagField(final TagField tagField) throws RPProxyException;

	/**
	 * Returns the data.
	 * @return Returns the data
	 */
	String getValue() throws RPProxyException;

	/**
	 * Sets the data.
	 * @param value
	 *           The data to set
	 */
	void setValue(final String value) throws RPProxyException;

}