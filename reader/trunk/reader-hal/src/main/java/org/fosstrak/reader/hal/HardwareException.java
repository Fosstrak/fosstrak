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
 * This exception signals a reader hardware problem and is
 * thrown by several of the HAL controller methods.
 * 
 * @author Matthias Lampe, lampe@acm.org
 */
public class HardwareException extends HardwareAbstractionException {
	
	private static final long serialVersionUID = 1L;


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
		super(serviceCode, readPointName, halName);
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
		super(serviceCode, readPointName, halName, message);
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
		super(serviceCode, readPointName, halName, message, cause);
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
		super(serviceCode, readPointName, halName, cause);
	}

}
