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

package org.fosstrak.reader.rp.proxy.msg.stubs;

import org.fosstrak.reader.rprm.core.ReaderDevice;
import org.fosstrak.reader.rprm.core.ReaderProtocolException;

/**
 * A TagSelector is a non-mutable object that can exist as a member of a Source
 * object (i.e., TagSelector objects are added and removed from a Source
 * object). A Source object can contain zero or more TagSelector objects. It
 * contains all information used for tag filtering.
 * <br /><br />
 * This is the local client stub which can be used to access the remote object on
 * the reader.
 *
 * @author Andreas Fürer, ETH Zurich Switzerland, Winter 2005/06
 */
public final class TagSelector {

   /**
    * The name of the tag selector.
    */
   private String name;

   
   /**
    * A static method to create a tag selector object with the given name. If a
    * tag selector object with the same name exists, an exception
    * ("ERROR_OBJECT_EXISTS") is thrown.
    * @param name
    *           The name of the tagselector
    * @param tagField
    *           The tag field of the dataselector
    * @param filterValue
    *           The filter value
    * @param filterMask
    *           The filter mask
    * @param inclusive
    *           The inclusive flag
    * @param readerDevice
    *           The instance of the reader device this tagselector belongs to
    * @return The instance of the created tagselector
    * @throws ReaderProtocolException
    *            "ERROR_OBJECT_EXISTS"
    */
   public static TagSelector create(final String name,
         final TagField tagField, final String filterValue,
         final String filterMask, final boolean inclusive) throws ReaderProtocolException {

      return new TagSelector(name, tagField, filterValue, filterMask, inclusive, null);

   }

   /**
    * The private constructor of the tagselector.
    * @param name
    *           The name of the tagselector
    * @param tagField
    *           The tag field of the dataselector
    * @param filterValue
    *           The filter value
    * @param filterMask
    *           The filter mask
    * @param inclusive
    *           The inclusive flag
    * @param readerDevice
    *           The instance of the reader device this tagselector belongs to
    */
   private TagSelector(final String name, final TagField tagField,
         final String filterValue, final String filterMask,
         final boolean inclusive, final ReaderDevice readerDevice) {

      this.name = name;

   }

   /**
    * Returns the maximum number of tagselectors the reader supports.
    * @return Ehe maximum number of tagselectors the reader supports
    */
   public int getMaxNumberSupported() {
      return -1;
   }

   /**
    * Returns the name of the given tagselector object.
    * @return Rhe name of the given tagselector object.
    */
   public String getName() {
      return name;
   }

   /**
    * Returns the tagfield attribute of the tagselector.
    * @return The tagfield attribute of the tagselector
    */
   public TagField getTagField() {
      return null;
   }

   /**
    * Returns the value attribute of this tagselector object.
    * @return The value attribute of this tagselector object
    */
   public String getValue() {
      return null;
   }

   /**
    * Returns the mask attribute of this tagselector object.
    * @return The mask attribute of this tagselector object
    */
   public String getMask() {
      return null;
   }

   /**
    * Returns the inclusive flag attribute of this tagselector object.
    * @return The inclusive flag attribute of this tagselector object
    */
   public boolean getInclusiveFlag() {
      return false;
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
