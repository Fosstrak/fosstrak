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

package org.fosstrak.reader.rp.proxy.msg.stubs.serializers;

/**
 * DataSelectorSerializer objects serialize a command on a DataSelector
 * into a String representation.
 * 
 * @author Andreas Fürer, ETH Zurich Switzerland, Winter 2005/06
 */
public interface DataSelectorSerializer {
	
	/**
	 * Serializes the command. 
	 * @param name
	 *           The name of the dataselector
	 * @return The instance of the dataselector
	 */
	public String create(final String name);
	
	/**
	 * Serializes the command.
	 * @return The name of the data selector
	 */
	public String getName();
	
	/**
	 * Serializes the command.
	 * @param fieldNameList
	 *           A list of field names
	 */
	public String addFieldNames(final String[] fieldNameList);
	
	/**
	 * Serializes the command.
	 * @param fieldNameList
	 *           The list of fieldnames to remove
	 */
	public String removeFieldNames(final String[] fieldNameList);
	
	/**
	 * Serializes the command.
	 */
	public String removeAllFieldNames();
	
	/**
	 * Serializes the command.
	 * @return A list with all fieldnames currentliy associated with this
	 *         dataselector
	 */
	public String getAllFieldNames();
	
	/**
	 * Serializes the command.
	 * @param eventList
	 *           A list of event types to add
	 */
	public String addEventFilters(final String[] eventList);
	
	/**
	 * Serializes the command.
	 * @param eventList
	 *           A list of eventtypes to remove
	 */
	public String removeEventFilters(final String[] eventList);
	
	/**
	 * Serializes the command.
	 */
	public String removeAllEventFilters();
	
	/**
	 * Serializes the command.
	 * @return Returns the list of event filters
	 */
	public String getAllEventFilters();
	
	/**
	 * Serializes the command.
	 * @param tagFieldNameList
	 *           A list of tag field names to add (list of strings)
	 */
	public String addTagFieldNames(final String[] tagFieldNameList);
	
	/**
	 * Serializes the command.
	 * @param tagFieldNameList
	 *           A list of tagfieldnames to remove
	 */
	public String removeTagFieldNames(final String[] tagFieldNameList);
	
	/**
	 * Serializes the command.
	 */
	public String removeAllTagFieldNames();
	
	/**
	 * Serializes the command.
	 * @return A list of all tagfieldnames
	 */
	public String getAllTagFieldNames();
	
	/**
	 * Serializes a DataSelector command.
	 */
	public String serializeCommand();
}
