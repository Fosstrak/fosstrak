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
 * Tagfield objects are used to define individual tag data fields. A tag field
 * specifies where the data field is located in tag memory. Further each tag
 * field has a name that makes it possible to associate data fields with the
 * same or similar data types. For example, two different tag types, using
 * different tag RF protocols, may need to store the same type of application
 * data. However, these associated data fields may exist at different locations
 * on the two different tag types.
 * <br /><br />
 * This is the local client stub which can be used to access the remote object on
 * the reader.
 *
 * @author Andreas Fürer, ETH Zurich Switzerland, Winter 2005/06
 */
public final class TagField {

   /**
    * The name of the tagfield.
    */
   private String name;

  
   /**
    * Create a TagField object with a given name. If a TagField object with the
    * same name exists, an exception ("ERROR_OBJECT_EXISTS") is thrown.
    * @param name
    *           The name of the tagfield
    * @param readerDevice
    *           The instance of the reader device this tagfield belongs to
    * @return The instance of the created tagfield
    * @throws ReaderProtocolException
    *            "ERROR_OBJECT_EXISTS"
    */
   public static TagField create(final String name) throws ReaderProtocolException {

      return new TagField(name, null);
   }

   /**
    * The private constructor of the TagField.
    * @param name
    *           The name of the TagField
    * @param readerDevice
    *           The instance of the reader device this TagField belongs to
    */
   private TagField(final String name, final ReaderDevice readerDevice) {

      this.name = name;
     

   }

   /**
    * Returns the name of the given tag field object. This name should not be
    * confused with the TagFieldName attribute of the object. A TagField
    * objects name shall be unique whereas a TagField objects TagFieldName
    * attribute can be shared
    * @return The name of the TagField.
    */
   public String getName() {
      return name;
   }

   /**
    * Returns the tag field name of this TagField object. This TagFieldName
    * attribute is not the same entity as the TagField objects name. Two or
    * more TagField objects can share the same TagFieldName attributes. This may
    * be necessary to associate tag data fields that carry the same type of
    * application data but exist in different locations on different tag types
    * @return The TagFieldName of the TagField
    */
   public String getTagFieldName() {
      return null;
   }

   /**
    * Sets the TagFieldName attribute for this TagField object.
    * @param tagFieldName
    *           The TagFieldName to set
    */
   public void setTagFieldName(final String tagFieldName) {
      
   }

   /**
    * Returns the value corresponding to the TagField objects memory bank
    * attribute. The application of this attribute is dependent upon a tags RF
    * protocol.
    * @return The memory bank
    */
   public int getMemoryBank() {
      return -1;
   }

   /**
    * Sets the memory bank.
    * @param memoryBank
    *           The memory bank to set
    */
   public void setMemoryBank(final int memoryBank) {
      
   }

   /**
    * Returns the offset of the data.
    * @return The offset of the data
    */
   public int getOffset() {
      return -1;
   }

   /**
    * Sets the offset of the data.
    * @param offset
    *           The offset of the data to set
    */
   public void setOffset(final int offset) {
      
   }

   /**
    * Returns the length of the data.
    * @return The lenght of the data
    */
   public int getLength() {
      return -1;
   }

   /**
    * Sets the length of the data.
    * @param length
    *           The lenght of the data
    */
   public void setLength(final int length) {
      
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
