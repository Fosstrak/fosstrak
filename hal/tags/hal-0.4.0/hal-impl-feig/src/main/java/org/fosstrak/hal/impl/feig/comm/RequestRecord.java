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

package org.accada.hal.impl.feig.comm;

/**
 * This class is used as a record to be a container for all the
 * parameters needed to launch a reader-request via ISOProtocol's sendRerquest
 *
 * <p>Copyright: Copyright (c) 2003</p>
 * @author Simon Keel, skeel@student.ethz.ch
 * @version 1.0
 */
public class RequestRecord {
	//	-------- Fields ------------------------------------------------------------
	/** the bus-address of the reader */
	public byte comAdr;
	/** the byte with the command for the reader to execute */
	public byte controlByte;
	/** additional information the reader needs to execute the command given by controlByte */
	public byte[] data;
}