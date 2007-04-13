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



public interface TagSelector {

	/**
	 * Returns the maximum number of tagselectors the reader supports.
	 * @return Ehe maximum number of tagselectors the reader supports
	 */
	int getMaxNumberSupported() throws RPProxyException;

	/**
	 * Returns the name of the given tagselector object.
	 * @return Rhe name of the given tagselector object.
	 */
	String getName() throws RPProxyException;

	/**
	 * Returns the tagfield attribute of the tagselector.
	 * @return The tagfield attribute of the tagselector
	 */
	TagField getTagField() throws RPProxyException;

	/**
	 * Returns the value attribute of this tagselector object.
	 * @return The value attribute of this tagselector object
	 */
	String getValue() throws RPProxyException;

	/**
	 * Returns the mask attribute of this tagselector object.
	 * @return The mask attribute of this tagselector object
	 */
	String getMask() throws RPProxyException;

	/**
	 * Returns the inclusive flag attribute of this tagselector object.
	 * @return The inclusive flag attribute of this tagselector object
	 */
	boolean getInclusiveFlag() throws RPProxyException;

}