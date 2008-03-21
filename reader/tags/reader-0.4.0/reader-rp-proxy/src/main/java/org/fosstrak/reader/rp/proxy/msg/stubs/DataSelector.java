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

package org.accada.reader.rp.proxy.msg.stubs;

import org.accada.reader.rprm.core.ReaderDevice;
import org.accada.reader.rprm.core.ReaderProtocolException;

/**
 * DataSelector objects are used to define what data shall be reported in
 * notification messages or by commands that take a DataSelector object as
 * parameter. Only events and data fields that are added to the respective lists
 * shall be reported.
 * <br /><br />
 * This is the local client stub which can be used to access the remote object on
 * the reader.
 
 * @author Andreas Fürer, ETH Zurich Switzerland, Winter 2005/06
 *
 */
public class DataSelector {

   /**
    * The name of the dataselector.
    */
   private String name;

   /**
    * The static method to create an instance of a dateselector. If this method
    * terminates successfully, the data selector is added to the list of
    * dataselectors in the reader device. If a dataselector with the same name
    * exists, an exception ("ERROR_OBJECT_EXISTS") is thrown.
    * @param name
    *           The name of the dataselector
    * @param readerDevice
    *           The reader device the dataselector belongs to
    * @return The instance of the dataselector
    * @throws ReaderProtocolException
    *            "ERROR_OBJECT_EXISTS"
    */
   public static DataSelector create(final String name) throws ReaderProtocolException {
	   return new DataSelector(name, null);
   }

   /**
    * The constructor of the dataselector.
    * @param name
    *           The name of the data selector
    * @param readerDevice
    *           The reader device the data selector belongs to
    */
   public DataSelector(final String name, final ReaderDevice readerDevice) {

      this.name = name;


   }

   /**
    * Returns the name of the given DataSelector object.
    * @return The name of the data selector
    */
   public final String getName() {
      return name;
   }

   /**
    * Adds the specified field names to the list of field names currently
    * associated with this dataselector.
    * @param fieldNameList
    *           A list of field names
    */
   public final void addFieldNames(final String[] fieldNameList) {

   }

   /**
    * Removes a fieldname of the list of fieldnames currently associated with
    * this dataselector.
    * @param fieldNameList
    *           The list of fieldnames to remove
    */
   public final void removeFieldNames(final String[] fieldNameList) {

     

   }

   /**
    * Removes all fieldnames of the list of fieldnames currently associated with
    * this dataselector.
    */
   public final void removeAllFieldNames() {
     
   }

   /**
    * Returns a list with all fieldnames currentliy associated with this
    * dataselector.
    * @return A list with all fieldnames currentliy associated with this
    *         dataselector
    */
   public final String[] getAllFieldNames() {
     return null;
   }

   /**
    * Adds the specified eventtypes to the list of eventtypes currently
    * associated with this DataSelector. Only events of the types that have been
    * added shall be reported.
    * @param eventList
    *           A list of event types to add
    */
   public final void addEventFilters(final String[] eventList) {

      

   }

   /**
    * Removes the specified eventtypes from the list of eventfilters currently
    * associated with this dataselector.
    * @param eventList
    *           A list of eventtypes to remove
    */
   public final void removeEventFilters(final String[] eventList) {

     
   }

   /**
    * Removes all eventtypes currently associated with this dataselector.
    */
   public final void removeAllEventFilters() {
      
   }

   /**
    * Returns tahe list of all eventtypes currently associated with this
    * dataselector.
    * @return Returns the list of event filters
    */
   public final String[] getAllEventFilters() {
      return null;
   }

   /**
    * Adds the specified tagfieldnames to the list of tagfieldnames currently
    * associated with this dataselector.
    * @param tagFieldNameList
    *           A list of tag field names to add (list of strings)
    */
   public final void addTagFieldNames(final String[] tagFieldNameList) {

      
   }

   /**
    * Removes certain tagfieldnames from the list of tagfieldnames currently
    * associated with this dataselector.
    * @param tagFieldNameList
    *           A list of tagfieldnames to remove
    */
   public final void removeTagFieldNames(final String[] tagFieldNameList) {

     
   }

   /**
    * Removes all tagfieldnames currently associated with this dataselector.
    */
   public final void removeAllTagFieldNames() {
     
   }

   /**
    * Returns a list of all tagfieldnames currently associated with this
    * dataselector. Note, this is a list of Strings.
    * @return A list of all tagfieldnames
    */
   public final String[] getAllTagFieldNames() {
      return null;
   }
   
   /**
    * Serializes object as String
    */
   public String toString() {
	   if (name == null) {
		   return super.toString();
	   } else {
		   return name;
	   }
   }

}
