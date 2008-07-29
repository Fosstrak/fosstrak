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

import org.fosstrak.reader.rprm.core.ReaderProtocolException;
import org.fosstrak.reader.rprm.core.readreport.ReadReport;

/**
 * A NotificationChannel is used to report notifications to a single host. It
 * has a list of Sources associated with it. Only events coming from these
 * Sources shall be reported. It also has a DataSelector that determines what
 * data should be reported to the host. If no DataSelector is explicitly
 * associated, then the Readers current DataSelector shall be used.
 * Notification shall be sent whenever the associated Trigger fires.
 * Alternatively, a host can also query the NotificationChannel for its
 * contents, using the command NotificationChannel.readQueuedData().
 * <br /><br />
 * This is the local client stub which can be used to access the remote object on
 * the reader.
 *
 * @author Andreas Fürer, ETH Zurich Switzerland, Winter 2005/06
 */
public final class NotificationChannel {

   /**
    * The name of this notification channel.
    */
   private String name;

   /**
    * Create a NotificationChannel object with a given name. If a
    * NotificationChannel object with the same name exists already, an error is
    * returned. This is a static method. The NotificationChannel will implicitly
    * be added to the list of all NotificationChannels kept by the ReaderDevice
    * object.
    * @param name
    *           The name of the notification channel
    * @param addr
    *           The host address
    * @param readerDevice
    *           The reader device this channel belongs to
    * @return The instance of the new NotificationChannel
    * @throws ReaderProtocolException
    *            The ReaderProtocolException "ERROR_OBJECT_EXISTS" is thrown
    */
   public static NotificationChannel create(final String name,
         final String addr)
         throws ReaderProtocolException {
	   return new NotificationChannel(name, addr);
   }

   /**
    * The private constructor of the NotificationChannel.
    * @param name
    *           The name of the channel
    * @param addr
    *           The address of the host
    * @param readerDevice
    *           The reader device this channel belongs to
    */
   private NotificationChannel(final String name, final String addr) {
      this.name = name;
      
   }

   /**
    * Returns the name of the given NotificationChannel object.
    * @return The name of the NotificationChannel
    */
   public String getName() {
      return name;
   }

   /**
    * Returns the address to which this NotificationChannel object sends its
    * notifications as specified by either commands NotificationChannel.create
    * or NotificationChannel.setAddress.
    * @return The address of the host
    */
   public String getAddress() {
      return null;
   }

   /**
    * Returns the effective address to which this NotificationChannel object
    * sends its notifications.
    * @return The effective address of the host
    */
   public String getEffectiveAddress() {
      return null;
   }

   /**
    * Sets the address to which this NotificationChannel object sends its
    * notifications.
    * @param addr
    *           The address of the host
    * @return If connect mode was specified, then the return value does not
    *         apply and the Reader returns zero. Otherwise, if listen mode was
    *         specified, then the return returns the port number assigned by the
    *         Reader to listen for host NotificationChannel connections.
    */
   public int setAddress(final String addr) {
      return -1;
   }

   /**
    * Returns the DataSelector currently associated with this
    * NotificationChannel object. If there is no DataSelector object associated,
    * the error ERROR_DATASELECTOR_NOT_FOUND is raised.
    * @return The dataSelector associated to this notification channel
    * @throws ReaderProtocolException
    *            "ERROR_DATASELECTOR_NOT_FOUND"
    */
   public DataSelector getDataSelector() throws ReaderProtocolException {
      return null;
   }

   /**
    * Sets the DataSelector object to be used with this NotificationChannel.
    * @param dataSelector
    *           The dataSelector to use
    */
   public void setDataSelector(final DataSelector dataSelector) {
      
   }

   /**
    * Adds the specified Sources to the list of Sources currently associated
    * with this NotificationChannel. If some of the Sources to be added are
    * already associated with this NotificationChannel, only the not yet
    * associated Sources shall be added and the command completes successfully.
    * @param sourceList
    *           The sources to add
    */
   public void addSources(final Source[] sourceList) {

    
   }

   /**
    * Removes the specified Sources from the list of Sources currently
    * associated with this NotificationChannel.
    * @param sourceList
    *           The sources to remove
    */
   public void removeSources(final Source[] sourceList) {


   }

   /**
    * Removes all Sources currently associated with this NotificationChannel.
    */
   public void removeAllSources() {
      
   }

   /**
    * Returns the Source with the specified name currently associated with this
    * NotificationChannel object. If no Source object with the given name
    * exists, the error ERROR_SOURCE_NOT_FOUND is raised.
    * @param name
    *           The name of the source
    * @return The instance of the source
    * @throws ReaderProtocolException
    *            "ERROR_SOURCE_NOT_FOUND"
    */
   public Source getSource(final String name) throws ReaderProtocolException {
	   return null;
   }

   /**
    * Returns all Sources currently associated with this NotificationChannel
    * object. If no Sources are currently associated with this object, the
    * command completes successfully and an empty list will be returned.
    * @return A list of sources
    */
   public Source[] getAllSources() {
      return null;
   }

   /**
    * Adds the specified Triggers to the list of Notification Triggers currently
    * associated with this NotificationChannel. If some of the Triggers to be
    * added are already associated with this NotificationChannel, only the not
    * yet associated Triggers shall be added and the command completes
    * successfully. Once a Trigger has been added, it is immediately activated.
    * @param triggerList
    *           The triggers to add
    * @throws ReaderProtocolException
    *            "ERROR_TOO_MANY_TRIGGERS"
    */
   public void addNotificationTriggers(final Trigger[] triggerList)
         throws ReaderProtocolException {


   }

   /**
    * Removes the specified Triggers from the list of Notification Triggers
    * currently associated with this NotificationChannel. Once a Trigger isnt
    * associated to any object anymore, it is deactivated.
    * @param triggerList
    *           The triggers to remove
    */
   public void removeNotificationTriggers(final Trigger[] triggerList) {


   }

   /**
    * Removes all Triggers currently associated with this NotificationChannel.
    */
   public void removeAllNotificationTriggers() {
      
   }

   /**
    * Returns the Trigger with the specified name currently associated with this
    * NotificationChannel object. If no Trigger object with the given name
    * exists, the error ERROR_TRIGGER_NOT_FOUND is raised.
    * @param name
    *           The name of the notification trigger
    * @return The notification trigger
    * @throws ReaderProtocolException
    *            "ERROR_TRIGGER_NOT_FOUND"
    */
   public Trigger getNotificationTrigger(final String name) {
      return null;
   }

   /**
    * Returns all Triggers currently associated with this NotificationChannel
    * object. If no Triggers are currently associated with this object, the
    * command will complete successfully and an empty list will be returned.
    * @return A list of triggers
    */
   public Trigger[] getAllNotificationTriggers() {
      return null;
   }

   /**
    * This command returns the data currently queued for delivery in the report
    * buffer. What data is reported depends on the DataSelector associated with
    * the NotificationChannel.
    * @param clearBuffer
    *           An optional flag to indicate if the report buffer should be
    *           cleared after the ReadReport is returned
    * @return The resulting ReadReport
    */
   public ReadReport readQueuedData(final boolean clearBuffer) {

     return null;

   }

   /**
    * This command returns the data currently queued for delivery in the report
    * buffer. What data is reported depends on the DataSelector associated with
    * the NotificationChannel.
    * @param trigger
    *           The trigger that cause this readQueuedData request
    * @param clearBuffer
    *           An optional flag to indicate if the report buffer should be
    *           cleared after the ReadReport is returned
    * @return The resulting ReadReport
    */
   public ReadReport readQueuedData(final boolean clearBuffer,
         final Trigger trigger) {

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
