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

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Signals that a command failed on the hardware abstraction layer. This
 * exception is thrown by objects within the hardware abstraction layer if the
 * attempt to execute a command failed. The idea is to propagate just a single
 * kind of error to the layers above. This simplifies the error handling for
 * other layers. <n> The error itself can be specified in more detail by using
 * the following codes: </n>
 * <ul>
 * <li><b><code>serviceCode</code>: </b> Indicates the kind of service,
 * which caused the exception
 * <li><b><code>readerProtocolErrorCode</code>: </b> The RP conform error
 * code..
 * </ul>
 */

public class HardwareException extends Exception {
	
	//----------------------constants--------------------------------
	
	private static final long serialVersionUID = 1L;
	
	public static final int SERVICECODE_IDENTIFY = 1; // error during identification
	public static final int SERVICECODE_WRITE = 2; // error during write
	public static final int SERVICECODE_READ = 3; // error during read
	public static final int SERVICECODE_KILL = 4; // error during kill
	public static final int SERVICECODE_PROGRAM_ID = 5; // error during program id
	public static final int SERVICECODE_INITIALIZE = 200; //error during initialization
	
	public static final String UNDEFINED_READ_POINT_NAME = "N/A";

	//----------------------fields-----------------------------------
	
	/**
	 * The name of the HAL.
	 */
	protected String halName;
	
	/**
	 * The name of the read point.
	 */
	protected String readPointName;
	
	/**
	 * The timestamp.
	 */
	protected Date timestamp;
	
	/**
	 * The RP conform error code.
	 * @see org.accada.reader.rprm.core.msg.MessagingConstants
	 */
	private int readerProtocolErrorCode;
	
	/**
	 * An integer which indicates the service that is involved.
	 */
	private int serviceCode;
	
	/**
	 * The logger.
	 */
	protected static Log log = LogFactory.getLog(HardwareException.class); 	
	
	//----------------------constructors----------------------------
	
	/**
	 * Constructor.
	 * 
	 * @param serviceCode
	 *            The service code
	 * @param readPointName
	 *            The name of the read point
	 * @param halName
	 *            The name of the HAL
	 */
	public HardwareException(int serviceCode, String readPointName, String halName) {
		super();
		this.serviceCode = serviceCode;
		this.readPointName = readPointName;
		this.halName = halName;
	}

	/**
	 * Constructor specifying a message.
	 * 
	 * @param serviceCode
	 *            The service code
	 * @param readPointName
	 *            The name of the read point
	 * @param halName
	 *            The name of the HAL
	 * @param message
	 *            The message
	 */
	public HardwareException(int serviceCode, String readPointName, String halName, String message) {
		super(message);
		this.serviceCode = serviceCode;
		this.readPointName = readPointName;
		this.halName = halName;
	}

	/**
	 * Constructor specifying a message and a cause.
	 * 
	 * @param serviceCode
	 *            The service code
	 * @param readPointName
	 *            The name of the read point
	 * @param halName
	 *            The name of the HAL
	 * @param message
	 *            The message
	 * @param cause
	 *            The cause
	 */
	public HardwareException(int serviceCode, String readPointName, String halName, String message, Throwable cause) {
		super(message, cause);
		this.serviceCode = serviceCode;
		this.readPointName = readPointName;
		this.halName = halName;
	}

	/**
	 * Constructor using a cause.
	 * 
	 * @param serviceCode
	 *            The service code
	 * @param readPointName
	 *            The name of the read point
	 * @param halName
	 *            The name of the HAL
	 * @param cause
	 *            The cause
	 */
	public HardwareException(int serviceCode, String readPointName, String halName, Throwable cause) {
		super(cause);
		this.serviceCode = serviceCode;
		this.readPointName = readPointName;
		this.halName = halName;
	}
	
	//--------------------------methods--------------------------------------------------------------------------
	
	/**
	 * Returns the name of the HAL.
	 * 
	 * @return The name of the HAL
	 */
	public String getHalName() {
		return halName;
	}

	/**
	 * Returns the name of the read point.
	 * 
	 * @return The name of the read point
	 */
	public String getReadPointName() {
		return readPointName;
	}

	/**
	 * Returns the timestamp.
	 * 
	 * @return The timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}
	
	/**
	 * Returns the RP conform error code.
	 * 
	 * @return The RP conform error code
	 */
	public int getReaderProtocolErrorCode() {
		return readerProtocolErrorCode;
	}
	
	/**
	 * Returns an integer which indicates the service that is involved.
	 * 
	 * @return An integer which indicates the service that is involved
	 */
	public int getServiceCode() {
		return serviceCode;
	}
	
}
