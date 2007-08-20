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

import org.accada.reader.hal.Observation;
import org.accada.reader.hal.UnsignedByteArray;
import org.accada.reader.hal.Trigger;
import org.accada.reader.hal.HardwareException;
import org.accada.reader.hal.UnsupportedOperationException;


/**
 * The standardized interface defines an abstraction of the underlaying reader hardware. The HardwareAbstraction interface defines 
 * a set of methods that can be implemented by any so called HAL controller class. A class that implements 
 * the interface agrees to implement all the methods defined in the interface, thereby agreeing to certain behavior.
 * <p>
 * The functionality provided by the interface is kept as generally as possible in order not to exclude certain
 * readers. The hardware itself has to comply with the following minimal requirements in order to be supported by the framework.
 * 
 * The minimal assumption about the proprietary RFID systems is that the reader allows the identification of tags by a unique serial number.
 * 
 * The tag's identifiers have to be specified as a String representing a hexadecimal number. The hexadecimal digits are the 
 * ordinary, base-10 digits '0' through '9' plus the letters 'A' through 'F'. In the hexadecimal system, these digits represent 
 * the values 0 through 15, respectively. A hexadecimal integer is a sequence of hexadecimal digits, such as 34A7, FF8, 174204. Depending
 * on the concrete hardware this String will be converted into a convenient format, for example a byte array.
 *
 * In the HAL model, the memory is devided into memory banks that can be addressed. In each memory bank the memory can be accessed
 * in block units, whereas the blocksize has to be specified in a corresponding properties file. Despite the block size, memory is
 * specified in bytes.
 *
 * @author Matthias Lampe, lampe@acm.org
 * @author Christian Floerkemeier, floerkem@mit.edu
 */
public interface HardwareAbstraction {
	
	//--------- Identify

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
	 * @param readPointNames Array that contains the names of all read points to be scanned
	 * 
	 * @return An array that contains for each read point an <code>Observation</code> object
	 * @throws HardwareException, if the operation can not be performed.
	 */
	Observation[] identify(String[] readPointNames) throws HardwareException;
	

	
	//--------- Asynchronous Identify

	/**
	 * Identifies the tags in a reader's field in a continuous way that operates asynchronously.
	 * 
	 * If the controller is a single-read-point-reader the array <code>readPointNames</code>
	 * should contain only one read point.
	 * 
	 * @param readPointNames Assay that contains the names of all read points to be scanned
	 * @param trigger The trigger that indicates the type of asynchronous identify
	 * 
	 * @throws HardwareException, if the operation can not be performed.
	 * @throws UnsupportedOperationException, if the operation is not supported by the controller implementation.
	 */
	void startAsynchronousIdentify(String[] readPointNames, Trigger trigger) throws HardwareException, UnsupportedOperationException;
	
	
	/**
	 * Stops the asynchronous identification for a specific listener.
	 * 
	 * @throws HardwareException, if the operation can not be performed.
	 * @throws UnsupportedOperationException, if the operation is not supported by the controller implementation.
	 */
	void stopAsynchronousIdentify() throws HardwareException, UnsupportedOperationException;
	
		
	/**
	 * Checks whether asynchronous identify is running.
	 * 
	 * @return true, if asynchronous identify is running, false otherwise
	 * 
	 * @throws HardwareException, if the operation can not be performed.
	 * @throws UnsupportedOperationException, if the operation is not supported by the controller implementation.
	 */
	boolean isAsynchronousIdentifyRunning() throws HardwareException, UnsupportedOperationException;

	
	/**
	 * Adds an asynchronous identify listener.
	 * 
	 * @throws HardwareException, if the operation can not be performed.
	 * @throws UnsupportedOperationException, if the operation is not supported by the controller implementation.
	 */
	public void addAsynchronousIdentifyListener(AsynchronousIdentifyListener listener) throws HardwareException, UnsupportedOperationException;
	
	
	/**
	 * removes an asynchronous identify listener.
	 * 
	 * @throws HardwareException, if the operation can not be performed.
	 * @throws UnsupportedOperationException, if the operation is not supported by the controller implementation.
	 */
	public void removeAsynchronousIdentifyListener(AsynchronousIdentifyListener listener) throws HardwareException, UnsupportedOperationException;
	
	
	/**
	 * Checks whether this HAL controller implementation supports the <code>startAsynchronousIdentify()</code>,
	 * <code>stopAsynchronousIdentify()</code>, <code>isAsynchronousIdentifyRunning()</code>,
	 * <code>addAsynchronousIdentifyListener()</code> and <code>removeAsynchronousIdentifyListener()</code>
	 * methods.
	 * 
	 * @return true, if methods are supported, false otherwise
	 */
	boolean supportsAsynchronousIdentify();
	


	//--------- Read and Write

	/**
	 * Reads data from a specified tag, if in range. The transponder id should be a
	 * result of a previous <code>identify</code>.
	 * 
	 * The data on a tag's memory
	 * can be read in block units. Therefore the <code>length</code>
	 * parameter, that indicates the number of bytes that have to be read, has
	 * to contain a multiple of the blocksize. 
	 * 
	 * @param readPointName The name of the read point on which the read attempt will be done
	 * @param id ID of the tag from which the data will be read
	 * @param memoryBank The number of the memory bank of the data
	 * @param offset The offset of the data in bytes
	 * @param length The number of bytes to be read
	 * @param passwords An optional list of one or more passwords (or lock code)
	 * 
	 * @return The data that has been read from the tag
	 * 
	 * @throws HardwareException, if the operation can not be performed.
	 * @throws UnsupportedOperationException, if the operation is not supported by the controller implementation.
	 */
	UnsignedByteArray readBytes(String readPointName, String id, int memoryBank, int offset, int length, String[] passwords) throws HardwareException, UnsupportedOperationException;
	
	/**
	 * Checks whether this HAL controller implementation supports the <code>readBytes()</code> method.
	 * 
	 * @return true, if method is supported, false otherwise
	 */
	boolean supportsReadBytes();
	
		
	/**
	 * Writes data to a specific tag, if in range. The transponder id should be
	 * a result of a previous <code>identify</code>.
	 * 
	 * @param readPointName The name of the read point on which the write attempt will be done
	 * @param id ID of the tag to which the data will be written
	 * @param memoryBank The number of the memory bank of the data
	 * @param offset The offset of the data in bytes
	 * @param data The byte data to be written to the tag
	 * @param passwords An optional list of one or more passwords (or lock code)

	 * @throws HardwareException, if the operation can not be performed.
	 * @throws UnsupportedOperationException, if the operation is not supported by the controller implementation.
	 */
	void writeBytes(String readPointName, String id, int memoryBank, int offset, UnsignedByteArray data, String[] passwords) throws HardwareException, UnsupportedOperationException;
	
	/**
	 * Checks whether this HAL controller implementation supports the <code>writeBytes()</code> method.
	 * 
	 * @return true, if method is supported, false otherwise
	 */
	boolean supportsWriteBytes();
	

	
	//--------- Kill and ProgramID

	/**
	 * Kills the specified tag, if in range. A killed tag doesn't respond to
	 * requests any longer.
	 * 
	 * @param id id of the tag that will be killed
	 * @param passwords an optional list of one or more passwords (or lock code) 
	 * 
	 * @throws HardwareException, if the operation can not be performed.
	 * @throws UnsupportedOperationException, if the operation is not supported by the controller implementation.
	 */
	void kill(String id, String[] passwords) throws HardwareException, UnsupportedOperationException;	
	
	/**
	 * Checks whether this HAL controller implementation supports the <code>kill()</code> method.
	 * 
	 * @return true, if method is supported, false otherwise
	 */
	boolean supportsKill();

	
	/**
	 * Programs a tag with the given ID and the optionally specified passwords.
	 * If the physical environment supports the setting of a new tag ID this
	 * method can be used to access the appropriate blocks within the memory.
	 * 
	 * @param id the new ID for the tag
	 * @param passwords an optional list of one or more passwords (or lock code)
	 * 
	 * @throws HardwareException, if the operation can not be performed.
	 * @throws UnsupportedOperationException, if the operation is not supported by the controller implementation.
	 */
	void programId(String id, String[] passwords) throws HardwareException, UnsupportedOperationException;
	
	/**
	 * Checks whether this HAL controller implementation supports the <code>programId()</code> method.
	 * 
	 * @return true, if method is supported, false otherwise
	 */
	boolean supportsProgramId();

	

	//--------- HAL Controller Management
	
	/**
	 * Returns the HAL name. The HAL name identifies the HAL controller instance and should be
	 * specified in a properties file.
	 * 
	 * @return The HAL name
	 */
	String getHALName();
	

	/**
	 * Gets the names of all available read points.
	 * 
	 * @return An array containing the names of all available read points
	 */
	String[] getReadPointNames();

	
	/**
	 * Gets the names of the supported parameters. The parameters which are
	 * configurable should be listed in the associated properties file.
	 * 
	 * @return The parameter names of all supported paramters
	 * 
	 * @throws HardwareException, if the operation can not be performed.
	 * @throws UnsupportedOperationException, if the operation is not supported by the controller implementation.
	 */
	String[] getAllParameterNames() throws HardwareException, UnsupportedOperationException;
	
	
	/**
	 * Gets the value of a given parameter. A call of the <code>getAllParameterNames()</code> 
	 * should precede this method in order not to request undefined parameters.
	 * 
	 * @param param The parameter name to be read
	 * 
	 * @return The parameter value
	 * 
	 * @throws HardwareException, if the operation can not be performed.
	 * @throws UnsupportedOperationException, if the operation is not supported by the controller implementation.
	 */
	String getParameter(String param) throws HardwareException, UnsupportedOperationException;
	
	
	/**
	 * Sets a given parameter. A call of the <code>getAllParameterNames()</code>
	 * should precede this method in order not to set undefined parameters.
	 * 
	 * @param param The parameter name
	 * @param value The parameter value
	 * 
	 * @throws HardwareException, if the operation can not be performed.
	 * @throws UnsupportedOperationException, if the operation is not supported by the controller implementation.
	 */
	void setParameter(String param, String value) throws HardwareException, UnsupportedOperationException;
	
	/**
	 * Checks whether the HAL controller implementation supports the <code>getAllParameterNames()</code>,
	 * <code>getParameter()</code> and <code>setParameter()</code> methods.
	 * 
	 * @return true, if methods are supported, false otherwise
	 */
	boolean supportsParameters();

	
	/**
	 * Resets the reader. This includes the unlocking of all previously locked
	 * (muted) tags. Furthermore, if anything should not work as desired due to
	 * any failures, this method reinitializes the hardware abstraction.
	 * 
	 * @throws HardwareException, if the operation can not be performed.
	 */
	void reset() throws HardwareException;
	
	/**
	 * Checks whether this HAL controller implementation supports the <code>reset()</code> method.
	 * 
	 * @return true, if method is supported, false otherwise
	 */
	boolean supportsReset();

	
	/**
	 * Returns the current transmit power level of an antenna.
	 * 
	 * @param readPointName The name of the read point
	 * @param normalize Specifies whether the power level should be returned in a normalized form (i.e. in a range from 0 to 255)
	 * @return The current transmit power level of the antenna
	 * 
	 * @throws HardwareException, if the operation can not be performed.
	 * @throws UnsupportedOperationException, if the operation is not supported by the controller implementation.
	 */
	int getAntennaPowerLevel(String readPointName, boolean normalize) throws HardwareException, UnsupportedOperationException;
	
	/**
	 * Checks whether this HAL controller implementation supports the <code>getAntennaPowerLevel()</code> method.
	 * 
	 * @return true, if method is supported, false otherwise
	 */
	boolean supportsGetAntennaPowerLevel();

	
	/**
	 * Returns the current noise level observed at an antenna.
	 * 
	 * @param readPointName The name of the read point
	 * @param normalize Specifies whether the noise level should be returned in a
	 *                  normalized form (i.e. in a range from 0 to 255)
	 *                  
	 * @return The current noise level observed at the antenna
	 * 
	 * @throws HardwareException, if the operation can not be performed.
	 * @throws UnsupportedOperationException, if the operation is not supported by the controller implementation.
	 */
	int getAntennaNoiseLevel(String readPointName, boolean normalize) throws HardwareException, UnsupportedOperationException;
	
	/**
	 * Checks whether this HAL controller implementation supports the <code>getAntennaNoiseLevel()</code> method.
	 * 
	 * @return true, if method is supported, false otherwise
	 */
	boolean supportsGetAntennaNoiseLevel();

	
	/**
	 * Starts up a read point.
	 * 
	 * @param readPointName The name of the read point
	 * 
	 * @throws HardwareException, if the operation can not be performed.
	 * @throws UnsupportedOperationException, if the operation is not supported by the controller implementation.
	 */
	void startUpReadPoint(String readPointName) throws HardwareException, UnsupportedOperationException;
	
	/**
	 * Checks whether this HAL controller implementation supports the <code>startUpReadPoint()</code> method.
	 * 
	 * @return true, if method is supported, false otherwise
	 */
	boolean supportsStartUpReadPoint();

	
	/**
	 * Shuts down a read point.
	 * 
	 * @param readPointName The name of the read point
	 * 
	 * @throws HardwareException, if the operation can not be performed.
	 * @throws UnsupportedOperationException, if the operation is not supported by the controller implementation.
	 */
	void shutDownReadPoint(String readPointName) throws HardwareException, UnsupportedOperationException;
	
	/**
	 * Checks whether this HAL controller implementation supports the <code>shutDownReadPoint()</code> method.
	 * 
	 * @return true, if method is supported, false otherwise
	 */
	boolean supportsShutDownReadPoint();

	
	/**
	 * Checks whether a read point is ready (i.e. it has been started up).
	 * 
	 * @param readPointName
	 *            The name of the read point
	 * @return <code>true</code> it the antenna is ready,
	 *         <code>false</code> otherwise
	 * 
	 * @throws HardwareException, if the operation can not be performed.
	 * @throws UnsupportedOperationException, if the operation is not supported by the controller implementation.
	 */
	boolean isReadPointReady(String readPointName) throws HardwareException, UnsupportedOperationException;
	
	/**
	 * Checks whether this HAL controller implementation supports the <code>isReadPointReady()</code> method.
	 * 
	 * @return true, if method is supported, false otherwise
	 */
	boolean supportsIsReadPointReady();

}