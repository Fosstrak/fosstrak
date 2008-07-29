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

package org.fosstrak.hal.impl.feig.comm;

/**
 * This class is used as a record containing the response from a reader-request.
 * Used by ISOProtocol's sendRerquest method.
 *
 * <p>Copyright: Copyright (c) 2003</p>
 * @author Simon Keel, skeel@student.ethz.ch
 * @version 1.0
 */
public class ResponseRecord {
	//	-------- Fields ------------------------------------------------------------
	/** the status byte of a reader-response */
	public byte status;
	/** data such as serial numbers of the transponders */
	public byte[] data;
}