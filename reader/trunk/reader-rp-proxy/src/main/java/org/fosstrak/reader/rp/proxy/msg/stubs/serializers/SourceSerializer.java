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

package org.accada.reader.rp.proxy.msg.stubs.serializers;

import java.util.Hashtable;

import org.accada.reader.rp.proxy.msg.stubs.DataSelector;
import org.accada.reader.rp.proxy.msg.stubs.ReadPoint;
import org.accada.reader.rp.proxy.msg.stubs.TagFieldValue;
import org.accada.reader.rp.proxy.msg.stubs.TagSelector;
import org.accada.reader.rp.proxy.msg.stubs.Trigger;

/**
 * SourceSerializer objects serialize a command on a Source into a String
 * representation.
 * 
 * @author Andreas Fürer, ETH Zurich Switzerland, Winter 2005/06
 */
public interface SourceSerializer {

	/**
	 * @param name
	 *            The name of the source
	 */
	public String create(final String name);

	/**
	 */
	public String getName();

	/**
	 */
	public String isFixed();

	/**
	 * @param readPoint
	 *            The list of readpoints
	 */
	public String addReadPoints(final ReadPoint[] readPoint);

	/**
	 * @param readPoint
	 *            The list of readpoints
	 */
	public String removeReadPoints(final ReadPoint[] readPoint);

	/**
	 */
	public String removeAllReadPoints();

	/**
	 * @param name
	 *            The name of the readpoint
	 */
	public String getReadPoint(final String name);

	/**
	 */
	public String getAllReadPoints();

	/**
	 * @param trigger
	 *            The list of read triggers
	 */
	public String addReadTriggers(final Trigger[] trigger);

	/**
	 * @param trigger
	 *            The list of read triggers
	 */
	public String removeReadTriggers(final Trigger[] trigger);

	/**
	 */
	public String removeAllReadTriggers();

	/**
	 * @param name
	 *            The name of the read trigger
	 */
	public String getReadTrigger(final String name);

	/**
	 */
	public String getAllReadTriggers();

	/**
	 * @param selector
	 *            The list of tag selectors
	 */
	public String addTagSelectors(final TagSelector[] selector);

	/**
	 * @param tagSelector
	 *            The list of tag selectors
	 */
	public String removeTagSelectors(final TagSelector[] tagSelector);

	/**
	 */
	public String removeAllTagSelectors();

	/**
	 * @param name
	 *            The name of the tag selector
	 */
	public String getTagSelector(final String name);

	/**
	 */
	public String getAllTagSelectors();

	/**
	 */
	public String getGlimpsedTimeout();

	/**
	 * @param timeout
	 *            The glimpsed timeout in ms
	 */
	public String setGlimpsedTimeout(final int timeout);

	/**
	 */
	public String getObservedThreshold();

	/**
	 * @param threshold
	 *            The observed threshold in ms
	 */
	public String setObservedThreshold(final int threshold);

	/**
	 */
	public String getObservedTimeout();

	/**
	 * @param timeout
	 *            The observed timeout value in ms
	 */
	public String setObservedTimeout(final int timeout);

	/**
	 */
	public String getLostTimeout();

	/**
	 * @param timeout
	 *            The lost timeout in ms
	 */
	public String setLostTimeout(final int timeout);

	/**
	 * @param dataselector
	 *            The data selector to use
	 */
	public String rawReadIDs(final DataSelector dataSelector);

	/**
	 * @param dataselector
	 *            The data selector to use
	 */
	public String readIDs(final DataSelector dataSelector);

	/**
	 * @param dataSelector
	 *            The data selector to use
	 * @param passwords
	 *            Not yet supported
	 */
	public String read(final DataSelector dataSelector,
			final Hashtable passwords);

	/**
	 * @param newID
	 *            The new id
	 * @param passwords
	 *            Not yet supported
	 * @param tagSelector
	 *            The tag selectors
	 */
	public String writeID(final String newID, final String[] passwords,
			final TagSelector[] tagSelector);

	/**
	 * @param tagFieldValue
	 *            The date to write on the tag
	 * @param passwords
	 *            Not yet supported
	 * @param tagSelector
	 *            The tag selectors
	 */
	public String write(final TagFieldValue[] tagFieldValue,
			final String[] passwords, final TagSelector[] tagSelector);

	/**
	 * @param passwords
	 *            Not yet supported
	 * @param tagSelector
	 *            The tag selectors
	 */
	public String kill(final String[] passwords,
			final TagSelector[] tagSelector);

	/**
	 */
	public String getReadCyclesPerTrigger();

	/**
	 * @param cycles
	 *            The read cycle per trigger value
	 */
	public String setReadCyclesPerTrigger(final int cycles);

	/**
	 */
	public String getMaxReadDutyCycles();

	/**
	 * @param cycles
	 *            The maximal read duty cycle value
	 */
	public String setMaxReadDutyCycles(final int cycles);

	/**
	 */
	public String getReadTimeout();

	/**
	 * @param timeout
	 *            The read timeout
	 */
	public String setReadTimeout(final int timeout);

	/**
	 */
	public String getSession();

	/**
	 * @param session
	 *            The session
	 */
	public String setSession(final int session);

	/**
	 * Serializes a SourceSerializer command.
	 */
	public String serializeCommand();
}
