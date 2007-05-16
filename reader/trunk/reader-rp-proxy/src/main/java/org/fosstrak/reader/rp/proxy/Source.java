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

package org.accada.reader.rp.proxy;

import java.util.Hashtable;



public interface Source {

	/**
	 * Returns the name of the source.
	 * @return The name of the source
	 */
	String getName() throws RPProxyException;

	/**
	 * Check if the source is fixed.
	 * @return 'true' if the source is fixed, otherwise false
	 */
	boolean isFixed() throws RPProxyException;

	/**
	 * Sets the fixed flag.
	 * @param isFixed
	 *           Indicate if the source is fixed
	 */
	void setFixed(final boolean isFixed) throws RPProxyException;

	/**
	 * Adds the specified ReadPoints to the list of readpoints currently
	 * associated with this source. If some of the ReadPoints to be added are
	 * already associated with this source, only the not yet associated
	 * ReadPoints will be added.
	 * @param readPointList
	 *           The list of readpoints
	 */
	void addReadPoints(final ReadPoint[] readPointList) throws RPProxyException;

	/**
	 * Removes the specified ReadPoints from the list of ReadPoints currently
	 * associated with this source.
	 * @param readPointList
	 *           The list of readpoints
	 */
	void removeReadPoints(final ReadPoint[] readPointList) throws RPProxyException;

	/**
	 * Remove all readpoints from source.
	 */
	void removeAllReadPoints() throws RPProxyException;

	/**
	 * Get a readpoint by name.
	 * @param name
	 *           The name of the readpoint
	 * @return The instance of the readpoint
	 * @throws RPProxyException
	 *            "ERROR_READPOINT_NOT_FOUND"
	 */
	ReadPoint getReadPoint(final String name) throws RPProxyException;

	/**
	 * Get all readpoits of the source.
	 * @return The list of readpoints
	 */
	ReadPoint[] getAllReadPoints() throws RPProxyException;

	/**
	 * Add a list of read triggers to this source.
	 * @param triggerList
	 *           The list of read triggers
	 * @throws RPProxyException
	 *            "ERROR_TOO_MANY_TRIGGERS"
	 */
	void addReadTriggers(final Trigger[] triggerList)
			throws RPProxyException;

	/**
	 * Remove a list of read triggers.
	 * @param triggerList
	 *           The list of read triggers
	 */
	void removeReadTriggers(final Trigger[] triggerList) throws RPProxyException;

	/**
	 * Remove all read triggers from source.
	 */
	void removeAllReadTriggers() throws RPProxyException;

	/**
	 * Get a read trigger by name.
	 * @param name
	 *           The name of the read trigger
	 * @return The instance of the read trigger
	 * @throws RPProxyException
	 *            "ERROR_TRIGGER_NOT_FOUND"
	 */
	Trigger getReadTrigger(final String name) throws RPProxyException;

	/**
	 * Get all read triggers.
	 * @return The list of read triggers
	 */
	Trigger[] getAllReadTriggers() throws RPProxyException;

	/**
	 * Add a list of tag selectors.
	 * @param selectorList
	 *           The list of tag selectors
	 * @throws RPProxyException
	 *            "ERROR_TOO_MANY_TAGSELECTORS"
	 */
	void addTagSelectors(final TagSelector[] selectorList)
			throws RPProxyException;

	/**
	 * Remove a list of tag selectors.
	 * @param tagSelectorList
	 *           The list of tag selectors
	 */
	void removeTagSelectors(final TagSelector[] tagSelectorList) throws RPProxyException;

	/**
	 * Remove all tag selectors.
	 */
	void removeAllTagSelectors() throws RPProxyException;

	/**
	 * Get a tag selector by name.
	 * @param name
	 *           The name of the tag selector
	 * @return The instance of the tag selector
	 * @throws RPProxyException
	 *            "ERROR_TAGSELECTOR_NOT_FOUND"
	 */
	TagSelector getTagSelector(final String name)
			throws RPProxyException;

	/**
	 * Get all tag selectors.
	 * @return The list of tag selectors
	 */
	TagSelector[] getAllTagSelectors() throws RPProxyException;

	/**
	 * Get the glimpsed timeout value.
	 * @return The glimpsed timeout in ms
	 */
	int getGlimpsedTimeout() throws RPProxyException;

	/**
	 * Set the glimpse timeout value (0..infinit ms).
	 * @param timeout
	 *           The glimpsed timeout in ms
	 * @throws RPProxyException
	 *            "ERROR_PARAMETER_OUT_OF_RANGE"
	 */
	void setGlimpsedTimeout(final int timeout) throws RPProxyException;

	/**
	 * Get the observed threshold value.
	 * @return The observed threshold in ms
	 */
	int getObservedThreshold() throws RPProxyException;

	/**
	 * Set the observed threshold value (0..infinit).
	 * @param threshold
	 *           The observed threshold in ms
	 * @throws RPProxyException
	 *            "ERROR_PARAMETER_OUT_OF_RANGE"
	 */
	void setObservedThreshold(final int threshold)
			throws RPProxyException;

	/**
	 * Get the observed timeout value.
	 * @return The observed timeout in ms
	 */
	int getObservedTimeout() throws RPProxyException;

	/**
	 * Set the observed timeout (0..infinit ms).
	 * @param timeout
	 *           The observed timeout value in ms
	 * @throws RPProxyException
	 *            "ERROR_PARAMETER_OUT_OF_RANGE"
	 */
	void setObservedTimeout(final int timeout) throws RPProxyException;

	/**
	 * Get the lost timeout value.
	 * @return The lost timeout in ms
	 */
	int getLostTimeout() throws RPProxyException;

	/**
	 * Set the lost timeout value (0..infinit ms).
	 * @param timeout
	 *           The lost timeout in ms
	 * @throws RPProxyException
	 *            "ERROR_PARAMETER_OUT_OF_RANGE"
	 */
	void setLostTimeout(final int timeout) throws RPProxyException;

	/**
	 * Performs a single read cycle without using the corresponding Source
	 * objects list of TagSelectors. The resulting ReadReport shall be formatted
	 * according to the given DataSelector.
	 * @param dataselector
	 *           The data selector to use
	 * @return The read report
	 */
	ReadReport rawReadIDs(final DataSelector dataselector) throws RPProxyException;

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
	ReadReport readIDs(final DataSelector dataselector) throws RPProxyException;

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
	ReadReport read(final DataSelector dataSelector, final Hashtable passwords, final TagSelector[] selectors) throws RPProxyException;

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
	 * @throws RPProxyException
	 *            "ERROR_MULTIPLE_TAGS"
	 */
	void writeID(final String newID, final String[] passwords,
			final TagSelector[] tagSelectorList) throws RPProxyException;

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
	 * @throws RPProxyException
	 *            "ERROR_UNKNOWN"
	 */
	void write(final TagFieldValue[] tagFieldValueList,
			final String[] passwords, final TagSelector[] tagSelectorList)
			throws RPProxyException;

	/**
	 * Kills the specified tag or group of tags. An list of TagSelector objects
	 * can be specified with this command.
	 * @param passwords
	 *           Not yet supported
	 * @param tagSelectorList
	 *           The tag selectors
	 * @throws RPProxyException
	 *            "ERROR_MULTIPLE_TAGS", "ERROR_NO_TAG", "ERROR_UNKNOWN"
	 */
	void kill(final String[] passwords, final TagSelector[] tagSelectorList)
			throws RPProxyException;

	/**
	 * Get the read cycle per trigger value.
	 * @return The read cycle per trigger
	 */
	int getReadCyclesPerTrigger() throws RPProxyException;

	/**
	 * Set the read cycle per trigger value.
	 * @param cycles
	 *           The read cycle per trigger value
	 */
	void setReadCyclesPerTrigger(final int cycles) throws RPProxyException;

	/**
	 * Get the maximal read duty cycle value.
	 * @return The maximal read duty cycle value
	 */
	int getMaxReadDutyCycles() throws RPProxyException;

	/**
	 * Set the maximal read duty cycle value.
	 * @param cycles
	 *           The maximal read duty cycle value
	 */
	void setMaxReadDutyCycles(final int cycles) throws RPProxyException;

	/**
	 * Get the read timeout value (msec).
	 * @return The read timeout
	 */
	int getReadTimeout() throws RPProxyException;

	/**
	 * Set the read timeout value (msec).
	 * @param timeout
	 *           The read timeout
	 */
	void setReadTimeout(final int timeout) throws RPProxyException;

	/**
	 * Get the session.
	 * @return The session
	 */
	int getSession() throws RPProxyException;

	/**
	 * Set the session.
	 * @param session
	 *           The session
	 */
	void setSession(final int session) throws RPProxyException;

	int getMaxNumberSupported() throws RPProxyException;

}