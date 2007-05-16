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

import java.util.Hashtable;

import org.accada.reader.rprm.core.ReaderProtocolException;
import org.accada.reader.rprm.core.readreport.ReadReport;

/**
 * This class represents the Source of the object model.
 * <br /><br />
 * This is the local client stub which can be used to access the remote object on
 * the reader.
 *
 * @author Andreas Fürer, ETH Zurich Switzerland, Winter 2005/06
 */
public final class Source {

   
   /**
    * The name of the source.
    */
   private String name;

 /**
    * Create a source object with a given name. If a source object with the same
    * name exists, an error is returned ("ERROR_OBJECT_EXISTS"). This is a
    * static method. The source shall implicitly be added to the list of all
    * sources kept by the ReaderDevice object.
    * @param name
    *           The name of the source
    * @param readerDevice
    *           The reader device it belongs to
    * @return The instance of the new source
    * @throws ReaderProtocolException
    *            "ERROR_UNKNOWN"
    */
   public static Source create(final String name) throws ReaderProtocolException {

      return new Source(name);
   }

   /**
    * The private constructor of the source.
    * @param name
    *           The name of the source
    * @param readerDevice
    *           The ReaderDevice it belongs to
    */
   private Source(final String name) {

      this.name = name;
   
   }

   /**
    * Returns the name of the source.
    * @return The name of the source
    */
   public String getName() {
      return name;
   }

   /**
    * Check if the source is fixed.
    * @return 'true' if the source is fixed, otherwise false
    */
   public boolean isFixed() {
      return false;
   }

   /**
    * Sets the fixed flag.
    * @param isFixed
    *           Indicate if the source is fixed
    */
   public void setFixed(final boolean isFixed) {
      
   }

   /**
    * Adds the specified ReadPoints to the list of readpoints currently
    * associated with this source. If some of the ReadPoints to be added are
    * already associated with this source, only the not yet associated
    * ReadPoints will be added.
    * @param readPointList
    *           The list of readpoints
    */
   public void addReadPoints(final ReadPoint[] readPointList) {

      
   }

   /**
    * Removes the specified ReadPoints from the list of ReadPoints currently
    * associated with this source.
    * @param readPointList
    *           The list of readpoints
    */
   public void removeReadPoints(final ReadPoint[] readPointList) {

     
   }

   /**
    * Remove all readpoints from source.
    */
   public void removeAllReadPoints() {
    
   }

   /**
    * Get a readpoint by name.
    * @param name
    *           The name of the readpoint
    * @return The instance of the readpoint
    * @throws ReaderProtocolException
    *            "ERROR_READPOINT_NOT_FOUND"
    */
   public ReadPoint getReadPoint(final String name)
         throws ReaderProtocolException {
	   return null;

   }

   /**
    * Get all readpoits of the source.
    * @return The list of readpoints
    */
   public ReadPoint[] getAllReadPoints() {
      return null;
   }

   /**
    * Add a list of read triggers to this source.
    * @param triggerList
    *           The list of read triggers
    * @throws ReaderProtocolException
    *            "ERROR_TOO_MANY_TRIGGERS"
    */
   public void addReadTriggers(final Trigger[] triggerList)
         throws ReaderProtocolException {

    
   }

   /**
    * Remove a list of read triggers.
    * @param triggerList
    *           The list of read triggers
    */
   public void removeReadTriggers(final Trigger[] triggerList) {

     

   }

   /**
    * Remove all read triggers from source.
    */
   public void removeAllReadTriggers() {
     
   }

   /**
    * Get a read trigger by name.
    * @param name
    *           The name of the read trigger
    * @return The instance of the read trigger
    * @throws ReaderProtocolException
    *            "ERROR_TRIGGER_NOT_FOUND"
    */
   public Trigger getReadTrigger(final String name)
         throws ReaderProtocolException {

     return null;

   }

   /**
    * Get all read triggers.
    * @return The list of read triggers
    */
   public Trigger[] getAllReadTriggers() {
      return null;
   }

   /**
    * Add a list of tag selectors.
    * @param selectorList
    *           The list of tag selectors
    * @throws ReaderProtocolException
    *            "ERROR_TOO_MANY_TAGSELECTORS"
    */
   public void addTagSelectors(final TagSelector[] selectorList)
         throws ReaderProtocolException {

   }

   /**
    * Remove a list of tag selectors.
    * @param tagSelectorList
    *           The list of tag selectors
    */
   public void removeTagSelectors(final TagSelector[] tagSelectorList) {

     
   }

   /**
    * Remove all tag selectors.
    */
   public void removeAllTagSelectors() {
     
   }

   /**
    * Get a tag selector by name.
    * @param name
    *           The name of the tag selector
    * @return The instance of the tag selector
    * @throws ReaderProtocolException
    *            "ERROR_TAGSELECTOR_NOT_FOUND"
    */
   public TagSelector getTagSelector(final String name)
         throws ReaderProtocolException {
	   
	   return null;

   }

   /**
    * Get all tag selectors.
    * @return The list of tag selectors
    */
   public TagSelector[] getAllTagSelectors() {
      return null;
   }

   /**
    * Get the glimpsed timeout value.
    * @return The glimpsed timeout in ms
    */
   public int getGlimpsedTimeout() {
      return -1;
   }

   /**
    * Set the glimpse timeout value (0..infinit ms).
    * @param timeout
    *           The glimpsed timeout in ms
    * @throws ReaderProtocolException
    *            "ERROR_PARAMETER_OUT_OF_RANGE"
    */
   public void setGlimpsedTimeout(final int timeout)
         throws ReaderProtocolException {
     
   }

   /**
    * Get the observed threshold value.
    * @return The observed threshold in ms
    */
   public int getObservedThreshold() {
      return -1;
   }

   /**
    * Set the observed threshold value (0..infinit).
    * @param threshold
    *           The observed threshold in ms
    * @throws ReaderProtocolException
    *            "ERROR_PARAMETER_OUT_OF_RANGE"
    */
   public void setObservedThreshold(final int threshold)
         throws ReaderProtocolException {
     
   }

   /**
    * Get the observed timeout value.
    * @return The observed timeout in ms
    */
   public int getObservedTimeout() {
      return -1;
   }

   /**
    * Set the observed timeout (0..infinit ms).
    * @param timeout
    *           The observed timeout value in ms
    * @throws ReaderProtocolException
    *            "ERROR_PARAMETER_OUT_OF_RANGE"
    */
   public void setObservedTimeout(final int timeout)
         throws ReaderProtocolException {
     
   }

   /**
    * Get the lost timeout value.
    * @return The lost timeout in ms
    */
   public int getLostTimeout() {
      return -1;
   }

   /**
    * Set the lost timeout value (0..infinit ms).
    * @param timeout
    *           The lost timeout in ms
    * @throws ReaderProtocolException
    *            "ERROR_PARAMETER_OUT_OF_RANGE"
    */
   public void setLostTimeout(final int timeout)
         throws ReaderProtocolException {
      
   }

   /**
    * Performs a single read cycle without using the corresponding Source
    * objects list of TagSelectors. The resulting ReadReport shall be formatted
    * according to the given DataSelector.
    * @param dataselector
    *           The data selector to use
    * @return The read report
    */
   public ReadReport rawReadIDs(final DataSelector dataselector) {

      return null;

   }

   /**
    * Performs multiple read cycles using all TagSelectors currently associated
    * with this Source. The number of read cycles performed shall be determined
    * by the value of the Source attribute ReadCyclesPerTrigger.The resulting
    * ReadReport is formatted according to the given DataSelector. If a tag is
    * seen in several read cycles, it shall only be reported once. Note that
    * this command does not use event generation.
    * @param dataselector
    *           The data selector to use
    * @return The read report
    */
   public ReadReport readIDs(final DataSelector dataselector) {

      
      return null;

   }

   /**
    * Performs multiple read cycles on the selected group of tags. The number of
    * read cycles performed shall be determined by the value of the Source
    * attribute ReadCyclesPerTrigger. The resulting ReadReport is formatted
    * according to the given DataSelector. If a tag is seen in several read
    * cycles, it shall only be reported once. Note that this command does not
    * apply tag smoothing
    * @param dataSelector
    *           The data selector to use
    * @param passwords
    *           Not yet supported
    * @return The read report
    */
   public ReadReport read(final DataSelector dataSelector,
         final Hashtable passwords) {
	   return null;

   }

  

   /**
    * Programs a tag with the given ID and the optionally specified passwords. A
    * list of TagSelector objects can be used to select a single tag, in the
    * readers field of view, for writing.
    * @param newID
    *           The new id
    * @param passwords
    *           Not yet supported
    * @param tagSelectorList
    *           The tag selectors
    * @throws ReaderProtocolException
    *            "ERROR_MULTIPLE_TAGS"
    */
   public void writeID(final String newID, final String[] passwords,
         final TagSelector[] tagSelectorList) throws ReaderProtocolException {

      

   }

   /**
    * Writes the specified TagFieldValues to one or more tags. A list of
    * TagSelector objects can be used to select a set of tags, in the readers
    * field of view, for writing.
    * @param tagFieldValueList
    *           The date to write on the tag
    * @param passwords
    *           Not yet supported
    * @param tagSelectorList
    *           The tag selectors
    * @throws ReaderProtocolException
    *            "ERROR_UNKNOWN"
    */
   public void write(final TagFieldValue[] tagFieldValueList,
         final String[] passwords, final TagSelector[] tagSelectorList)
         throws ReaderProtocolException {
     

   }

   /**
    * Kills the specified tag or group of tags. An list of TagSelector objects
    * can be specified with this command.
    * @param passwords
    *           Not yet supported
    * @param tagSelectorList
    *           The tag selectors
    * @throws ReaderProtocolException
    *            "ERROR_MULTIPLE_TAGS", "ERROR_NO_TAG", "ERROR_UNKNOWN"
    */
   public void kill(final String[] passwords,
         final TagSelector[] tagSelectorList) throws ReaderProtocolException {

   }

   /**
    * Get the read cycle per trigger value.
    * @return The read cycle per trigger
    */
   public int getReadCyclesPerTrigger() {
      return -1;
   }

   /**
    * Set the read cycle per trigger value.
    * @param cycles
    *           The read cycle per trigger value
    */
   public void setReadCyclesPerTrigger(final int cycles) {
      
   }

   /**
    * Get the maximal read duty cycle value.
    * @return The maximal read duty cycle value
    */
   public int getMaxReadDutyCycles() {
      return -1;
   }

   /**
    * Set the maximal read duty cycle value.
    * @param cycles
    *           The maximal read duty cycle value
    */
   public void setMaxReadDutyCycles(final int cycles) {
      
   }

   /**
    * Get the read timeout value (msec).
    * @return The read timeout
    */
   public int getReadTimeout() {
      return -1;
   }

   /**
    * Set the read timeout value (msec).
    * @param timeout
    *           The read timeout
    */
   public void setReadTimeout(final int timeout) {
      
   }

   /**
    * Get the session.
    * @return The session
    */
   public int getSession() {
      return -1;
   }

   /**
    * Set the session.
    * @param session
    *           The session
    */
   public void setSession(final int session) {
    
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
