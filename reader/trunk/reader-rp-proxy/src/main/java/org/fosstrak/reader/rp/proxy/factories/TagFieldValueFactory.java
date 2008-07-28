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

package org.fosstrak.reader.rp.proxy.factories;

import org.fosstrak.reader.rp.proxy.TagFieldValue;
import org.fosstrak.reader.rp.proxy.TagFieldValueImpl;

/**
 * This class creates new tag field values and new tag field values proxies.
 * 
 * @author regli
 */
public class TagFieldValueFactory {
	
	public static TagFieldValue createTagFieldValue() {
		
		return new TagFieldValueImpl();
		
	}

}