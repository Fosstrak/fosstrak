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

package org.accada.reader.hal;



/**
 * The standardized interface defines an abstraction of the underlaying hardware. The HardwareAbstraction interface defines 
 * a set of methods that can be implemented by any class anywhere in the class hierarchy. A class that implements 
 * the interface agrees to implement all the methods defined in the interface, thereby agreeing to certain behavior.
 * <p>
 * The functionality provided by the interface is kept as generally as possible in order to not exclude certain
 * readers. The hardware itself has to comply with the following minimal requirements in order to be supported by the framework.
 * The minimal assumption about the proprietary RFID systems are:
 * <ul>
 * <li>The reader allows the identification of tags by a unique serial number.
 * </ul>
 * <p>
 * The tag's identifiers have to be specified as a String representing a hexadecimal number. The hexadecimal digits are the 
 * ordinary, base-10 digits '0' through '9' plus the letters 'A' through 'F'. In the hexadecimal system, these digits represent 
 * the values 0 through 15, respectively. A hexadecimal integer is a sequence of hexadecimal digits, such as 34A7, FF8, 174204. Depending
 * on the concrete hardware this String will be converted into a convenient format, for example a byte array.
 * <p>
 * A tag's memory can be accessed in block units, whereas the blocksize has to be specified in a corresponding properties file.
 */
public interface HardwareAbstraction {
	
	/**
	 * Returns the HAL name.
	 * 
	 * @return The HAL name
	 */
	String getHalName();
	
	/**
	 * Identifies the tags in a reader's field. The concrete behaviour of this
	 * method depends on the concrete implementation of the underlaying
	 * hardware.
	 * 
	 * If the controller is a single-read-point-reader the array
	 * <code>readPointNames</code> should contain only one read point.
	 * 
	 * Tags that have been locked during the identification can be reactivated
	 * by calling the <code>reset()</code> method.
	 * 
	 * @param readPointNames
	 *            Contains the names of all read points to be scanned
	 * 
	 * @return For each read point an <code>Observation</code> object
	 * @throws HardwareException
	 */
	Observation[] identify(String[] readPointNames) throws HardwareException;
	
	
	/**
	 * Identifies the tags in a reader's field in a continuous way.
	 * 
	 * If the controller is a single-read-point-reader the array <code>readPointNames</code> should contain only one read point.
	 * 
	 * The following table is copied from Reader Protocol Standard v1.1 Chapter 6.5.
	 * It shows the legal values for <code>triggerValue</code> and associated <code>triggerType</code>:
	 * <table border="1">
	 *  <tr>
	 *   <th>
	 *    <code>triggerValue</code>
	 *   </th>
	 *   <th>
	 *    <code>triggerType</code>
	 *   </th>
	 *   <th>
	 *    Description
	 *   </th>
	 *  </tr>
	 *  <tr>
	 *   <td>
	 *    n/a
	 *   </td>
	 *   <td>
	 *    continuous
	 *   </td>
	 *   <td>
	 *    When associated with a <code>Source</code>, the trigger fires repeatedly,
	 *    as rapidly as possible. When associated with a <code>NotificationChannel</code>
	 *    the trigger fires whenever the the report queue transitions from empty to
	 *    non-empty.
	 *   </td>
	 *  </tr>
	 *  <tr>
	 *   <td>
	 *    ms=&ltn&gt
	 *   </td>
	 *   <td>
	 *    timer
	 *   </td>
	 *   <td>
	 *    Periodic interval trigger; &ltn&gt is the number of milliseconds for the interval.
	 *   </td>
	 *  </tr>
	 *  <tr>
	 *   <td>
	 *    type=&ltrising|falling&gt ; port=&ltn&gt ; pin=&ltn&gt
	 *   </td>
	 *   <td>
	 *    ioEdge
	 *   </td>
	 *   <td>
	 *    The trigger fires when the specified i/o pin on the specified port changes state.
	 *    The type shall be 'rising' or 'falling'.
	 *   </td>
	 *  </tr>
	 *  <tr>
	 *   <td>
	 *    port=&ltn&gt ; value=&lthex&gt [;mask=&lthex&gt]
	 *   </td>
	 *   <td>
	 *    ioValue
	 *   </td>
	 *   <td>
	 *    The trigger fires when the specified port input data has a specific value. If a
	 *    mask is specified than it is bitwise "anded" with the port input data before
	 *    comparing to the value. The value and mask are specified as hexadecimal (e.g.
	 *    "…;value=00F2").
	 *   </td>
	 *  </tr>
	 *  <tr>
	 *   <td>
	 *    vendor=&ltname&gt ; type=&lttype&gt [;…arbitrary key/value pairs… ]
	 *   </td>
	 *   <td>
	 *    vendorExtension
	 *   </td>
	 *   <td>
	 *    Non-standard vendor extensions. The &ltname&gt is required and shall contain
	 *    the vendor's name. The &lttype&gt indicates the type of trigger; the actual
	 *    value is vendordependent. Additional key/value pairs may be specified, and
	 *    are vendor-dependent.
	 *   </td>
	 *  </tr>
	 * </table>
	 * 
	 * @param readPointNames
	 *            Contains the names of all read points to be scanned
	 * @param listener
	 *            The listener which receives the resulting <code>Observation</code> objects
	 * @param triggerType
	 *            The type of the trigger (see table above)
	 * @param triggerValue
	 *            The value of the trigger (see table above)
	 * @throws HardwareException
	 * @throws UnsupportedOperationException
	 */
	void startAsynchronousIdentify(String[] readPointNames, AsynchronousIdentifyListener listener, String triggerType, String triggerValue) throws HardwareException, UnsupportedOperationException;
	
	
	/**
	 * Stops the asynchronous identification for a specific listener.
	 * 
	 * @param listener
	 *            The listener which receives the resulting
	 *            <code>Observation</code> objects
	 * @throws HardwareException
	 * @throws UnsupportedOperationException
	 */
	void stopAsynchronousIdentify(AsynchronousIdentifyListener listener) throws HardwareException, UnsupportedOperationException;
	
	
	/**
	 * Checks whether a specific listener is registered for asynchronous
	 * identification.
	 * 
	 * @param listener
	 *            The listener which receives the resulting
	 *            <code>Observation</code> objects
	 * @return <code>true</code> if it is registered, <code>false</code>
	 *         otherwise
	 * 
	 * @throws HardwareException
	 * @throws UnsupportedOperationException
	 */
	boolean isAsynchronousIdentifyRunning(AsynchronousIdentifyListener listener) throws HardwareException, UnsupportedOperationException;
	
	
	/**
	 * Reads data from a specified tag, if in range. The data on a tag's memory
	 * can be read in block units. Therefore the <code>length</code>
	 * parameter, that indicates the number of bytes that have to be read, has
	 * to contain a multiple of the blocksize. The transponder id should be a
	 * result of a previous <code>identify</code>.
	 * 
	 * @param readPointName
	 *            The name of the read point on which the read attempt will be
	 *            done
	 * @param id
	 *            Selects the tag to read from
	 * @param memoryBank
	 *            The memory bank of the data
	 * @param offset
	 *            The offset of the data
	 * @param length
	 *            The number of bytes to read
	 * @param passwords
	 *            An optional list of one or more passwords (or lock code). The
	 *            use of passwords is dependent upon the tag's RF protocol
	 * 
	 * @return The data
	 * @throws HardwareException
	 * @throws UnsupportedOperationException
	 */
	byte[] readBytes(String readPointName, String id, int memoryBank, int offset, int length, String[] passwords) throws HardwareException, UnsupportedOperationException;
	
	
	/**
	 * Writes data to a specific tag, if in range. The transponder id should be
	 * a result of a previous <code>identify</code>.
	 * 
	 * @param readPointName
	 *            The name of the read point on which the write attempt will be
	 *            done
	 * @param id
	 *            Selects the tag to write to
	 * @param memoryBank
	 *            The memory bank of the data
	 * @param offset
	 *            The offset of the data
	 * @param data
	 *            The data that has to be written to the tag's memory.
	 * @param passwords
	 *            An optional list of one or more passwords (or lock code). The
	 *            use of passwords is dependent upon the tag's RF protocol
	 * 
	 * @throws HardwareException
	 * @throws UnsupportedOperationException
	 */
	void writeBytes(String readPointName, String id, int memoryBank, int offset, byte[] data, String[] passwords) throws HardwareException, UnsupportedOperationException;
	
	
	/**
	 * 
	 * Returns a list of the names of all available read points.
	 * 
	 * @return A list of the names of all available read points
	 */
	String[] getReadPointNames() throws HardwareException;
	
	/**
	 * Sets a given parameter. A call of the <code>getAllParameterNames()</code>
	 * should precede this method in order not to set undefined parameters.
	 * 
	 * @param param
	 *            The parameter name
	 * @param value
	 *            The parameter value
	 * 
	 * @throws HardwareException
	 * @throws UnsupportedOperationException
	 */
	void setParameter(String param, String value) throws HardwareException, UnsupportedOperationException;
	
	
	/**
	 * Gets the names of the supported parameters. The parameters which are
	 * configurable are listed in the associated properties file.
	 * 
	 * @return The parameter names
	 * 
	 * @throws HardwareException
	 * @throws UnsupportedOperationException
	 */
	String[] getAllParameterNames() throws HardwareException, UnsupportedOperationException;
	
	
	/**
	 * Gets the value of a given parameter. A call of the
	 * <code>getAllParameterNames()</code> should precede this method in order
	 * not to request undefined parameters.
	 * 
	 * @param param
	 *            The parameter name
	 * 
	 * @return The parameter value
	 * 
	 * @throws HardwareException
	 * @throws UnsupportedOperationException
	 */
	String getParameter(String param) throws HardwareException, UnsupportedOperationException;
	
	
	/**
	 * Programs a tag with the given ID and the optionally specified passwords.
	 * If the physical environment supports the setting of a new tag ID this
	 * method can be used to access the appropriate blocks within the memory.
	 * 
	 * @param id
	 *            The new ID
	 * @param passwords
	 *            An optional list of one or more passwords (or lock code). The
	 *            use of passwords is dependent upon the tag's RF protocol
	 * 
	 * @throws HardwareException
	 * @throws UnsupportedOperationException
	 */
	void programId(String id, String[] passwords) throws HardwareException, UnsupportedOperationException;
	
	
	/**
	 * Resets the reader. This includes the unlocking of all previously locked
	 * (muted) tags. Furthermore, if anything should not work as desired due to
	 * any failures, this method reinitializes the hardware abstraction.
	 */
	void reset() throws HardwareException;
	
	
	/**
	 * Kills the specified tag, if in range. A killed tag doesn't respond to
	 * requests any longer.
	 * 
	 * @param id
	 *            Selects the tag to kill
	 * @param passwords
	 *            An optional list of one or more passwords (or lock code). The
	 *            use of passwords is dependent upon the tag's RF protocol
	 * 
	 * @throws HardwareException
	 * @throws UnsupportedOperationException
	 */
	void kill(String id, String[] passwords) throws HardwareException, UnsupportedOperationException;
	
	
	/**
	 * Checks whether this reader supports the <code>readBytes()</code>
	 * method.
	 * 
	 * @return <code>true</code> if it is supported, <code>false</otherwise>
	 */
	boolean supportsReadBytes();
	
	
	/**
	 * Checks whether this reader supports the <code>writeBytes()</code>
	 * method.
	 * 
	 * @return <code>true</code> if it is supported, <code>false</otherwise>
	 */
	boolean supportsWriteBytes();
	
	
	/**
	 * Checks whether this reader supports the <code>programId()</code>
	 * method.
	 * 
	 * @return <code>true</code> if it is supported, <code>false</otherwise>
	 */
	boolean supportsProgramId();
	
	
	/**
	 * Checks whether this reader supports the <code>kill()</code>
	 * method.
	 * 
	 * @return <code>true</code> if it is supported, <code>false</otherwise>
	 */
	boolean supportsKill();
	
	
	/**
	 * Returns the current transmit power level of an antenna.
	 * 
	 * @param readPointName
	 *            The name of the read point
	 * @param normalize
	 *            Specifies whether the power level should be returned in a
	 *            normalized form (i.e. in a range from 0 to 255)
	 * @return The current transmit power level of the antenna
	 */
	int getAntennaPowerLevel(String readPointName, boolean normalize);
	
	
	/**
	 * Returns the current noise level observed at an antenna.
	 * 
	 * @param readPointName
	 *            The name of the read point
	 * @param normalize
	 *            Specifies whether the noise level should be returned in a
	 *            normalized form (i.e. in a range from 0 to 255)
	 * @return The current noise level observed at the antenna
	 */
	int getAntennaNoiseLevel(String readPointName, boolean normalize);
	
	
	/**
	 * Starts up a read point.
	 * 
	 * @param readPointName
	 *            The name of the read point
	 */
	void startUpReadPoint(String readPointName);
	
	
	/**
	 * Shuts down a read point.
	 * 
	 * @param readPointName
	 *            The name of the read point
	 */
	void shutDownReadPoint(String readPointName);
	
	
	/**
	 * Checks whether a read point is ready (i.e. it has been started up).
	 * 
	 * @param readPointName
	 *            The name of the read point
	 * @return <code>true</code> it the antenna is ready,
	 *         <code>false</code> otherwise
	 */
	boolean isReadPointReady(String readPointName);
	
}