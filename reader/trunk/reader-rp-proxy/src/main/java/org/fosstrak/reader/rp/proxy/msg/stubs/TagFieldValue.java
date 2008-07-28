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

/**
 * This object is not created on the reader. It is only passed as a parameter to
 * some Source commands. This object encapsulates the information for a named
 * tag data field and its associated data value.
 * <br /><br />
 * This is the local client stub which can be used to access the remote object on
 * the reader.
 *
 * @author Andreas Fürer, ETH Zurich Switzerland, Winter 2005/06
 */
public class TagFieldValue {

   /**
    * The tag field.
    */
   private String tagFieldName;

   /**
    * The data (as String).
    */
   private String value;

   /**
    * The constructor of the tag field value.
    * @param tagField
    *           The tag field
    * @param value
    *           The data
    */
   public TagFieldValue(final String tagFieldName, final String value) {
      this.tagFieldName = tagFieldName;
      this.value = value;
   }

   /**
    * Returns the tagField.
    * @return Returns the tagField
    */
   public final String getTagFieldName() {
      return tagFieldName;
   }

   /**
    * Sets the tag field.
    * @param tagField
    *           The tagField to set
    */
   public final void setTagFieldName(final String tagFieldName) {
      this.tagFieldName = tagFieldName;
   }

   /**
    * Returns the data.
    * @return Returns the data
    */
   public final String getValue() {
      return value;
   }

   /**
    * Sets the data.
    * @param value
    *           The data to set
    */
   public final void setValue(final String value) {
      this.value = value;
   }

}
