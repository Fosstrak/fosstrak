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
 * A Trigger is a non-mutable object that can exist as a composite member of
 * either a Source or a NotificationChannel object, i.e., Trigger objects are
 * added and removed from Source and NotificationChannel objects. There is one
 * pool of Trigger objects. Source and NotificationChannel objects can both
 * reference the same Trigger object. Source and NotificationChannel objects can
 * contain zero or up to a predefined maximum number of total Trigger member
 * objects. A Trigger shall be activated as soon as it is added to another
 * object. It shall be deactivated once it has been removed from all objects it
 * had been added to, i.e. when it is not associated with any other object.
 * <br /><br />
 * This is the local client stub which can be used to access the remote object on
 * the reader.
 *
 * @author Andreas Fürer, ETH Zurich Switzerland, Winter 2005/06
 */
public final class Trigger {

   /**
    * The name of the trigger.
    */
   private String name;

   /**
    * Create a Triger object with a given name. If a Trigger object with the
    * same name exists, an error is returned. This is a static method. The
    * Trigger objects type and value establish the conditions whereby the
    * trigger shall fire. If the Reader doesnt support the requested
    * TriggerType, the error condition ERROR_PARAMETER_NOT_SUPPORTED shall be
    * raised. The Trigger shall implicitly be added to the list of all Triggers
    * kept by the ReaderDevice object. Possible values for triggers are:
    * Continuous: no value <br>
    * Timer : "ms=5000"
    * @param name
    *           The name of the trigger
    * @param type
    *           The type of the trigger ((Continuous, Timer, IOEdge, IOValue))
    * @param value
    *           The value of the trigger
    * @param readerDevice
    *           The reader device this trigger belongs to
    * @return The new instance of the trigger
    * @throws ReaderProtocolException
    *            "ERROR_TOO_MANY_TRIGGERS", "ERROR_PARAMETER_NOT_SUPPORTED",
    *            "ERROR_PARAMETER_MISSING"
    */
   public static Trigger create(final String name)
         throws ReaderProtocolException {

     return new Trigger(name, "", "", null);
   }

   /**
    * The private constructor of the trigger.
    * @param name
    *           The name of the trigger
    * @param value
    *           The value of the trigger
    * @param type
    *           The type of the trigger ((Continuous, Timer, IOEdge, IOValue))
    * @param readerDevice
    *           The reader device this trigger belongs to
    */
   private Trigger(final String name, final String value, final String type,
         final ReaderDevice readerDevice) {

      this.name = name;
      

   }

   /**
    * Returns the maximum number of Triggers this Reader supports.
    * @return The maximum number of Triggers this Reader supports
    */
   public int getMaxNumberSupported() {
      return -1;
   }

   /**
    * Returns the name of the given Trigger object.
    * @return The name of the trigger
    */
   public String getName() {
      return name;
   }

   /**
    * Returns the TriggerType of this Trigger.
    * @return The TriggerType
    */
   public String getType() {
      return null;
   }

   /**
    * Returns the triggervalue of this Trigger object.
    * @return The value of the trigger
    */
   public String getValue() {
      return null;
   }

   /**
    * This command causes this Trigger object to fire (i.e., activate the
    * trigger) immediately, regardless of the value settings of the Trigger.
    */
   public void fire() {

     
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
