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

package org.accada.reader.rprm.core.msg.util;

import java.math.BigInteger;

import org.apache.log4j.Logger;

/**
 * Utilities used for hexadecimal calculations.
 * 
 * @author Andreas Fürer, ETH Zurich Switzerland, Winter 2005/06
 */
public class HexUtil {
	
	private static final Logger LOG = Logger.getLogger(HexUtil.class);
	/**
	 * Converts a hexadecimal value represented as String into
	 * an array of bytes.
	 * @param hexString The hex string
	 * @return the representing byte array
	 */
	public static byte[] hexToByteArray(String hexString) {
		try {
			LOG.debug("Convert HexString: " + hexString);
			byte[] b = new BigInteger(hexString, 16).toByteArray();
			LOG.debug("Result: " + b);
			return b;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * This method converts a byte array into a hexadecimal string.
	 * 
	 * @param byteArray to convert
	 * @return hexadecimal string
	 */
	public static String byteArrayToHexString(byte[] byteArray) {
		
		return new BigInteger(byteArray).toString(16).toUpperCase();
				
	}
	
}
