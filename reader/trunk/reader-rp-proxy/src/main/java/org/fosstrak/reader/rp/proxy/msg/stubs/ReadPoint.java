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
import org.accada.hal.HardwareAbstraction;

/**
 * This class represents a readpoint. This means a single antenna, a barcode
 * reader, ...
 * <br /><br />
 * This is the local client stub which can be used to access the remote object on
 * the reader.
 *
 * @author Andreas Fürer, ETH Zurich Switzerland, Winter 2005/06
 */

public final class ReadPoint {

   /**
    * The name of the readpoint.
    */
   private String name;

   /**
    * The static method to create an instance of a read point. If this method
    * terminates successfully, the readpoint is added to the list of read points
    * in the reader device. If a read point with the same name exists, an
    * exception ("ERROR_OBJECT_EXISTS") is thrown.
    * @param name
    *           The name of the readpoint
    * @param readerDevice
    *           The reader device the source belongs to
    * @param reader
    *           The HardwareAbstraction which contains the read point
    * @return The instance of the created readpoint
    * @throws ReaderProtocolException
    *            "ERROR_OBJECT_EXISTS"
    */
   public static ReadPoint create(final String name,
         final ReaderDevice readerDevice, final HardwareAbstraction reader)
         throws ReaderProtocolException {
	   return null;
   }

   /**
    * The private constructor of a readpoint.
    * @param name
    *           The name of the readpoint
    * @param readerDevice
    *           The reader device the source belongs to
    * @param reader
    *           The HardwareAbstraction which contains the read point
    */
   private ReadPoint(final String name,
         final HardwareAbstraction reader) {

      this.name = name;
   }

   /**
    * Returns the name of the ReadPoint object.
    * @return The name of the readpoint
    */
   public String getName() {
      return name;
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
