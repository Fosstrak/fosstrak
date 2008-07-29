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

package org.fosstrak.reader.rprm.core.mgmt.agent.snmp.mib;

import org.fosstrak.reader.rprm.core.mgmt.util.SnmpUtil;
import org.snmp4j.PDU;
import org.snmp4j.agent.mo.snmp.smi.ValueConstraint;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;

/**
 * The <code>BitsEnumerationConstraint</code> class checks an
 * <code>OctetString</code> value to be a valid bit string that represents an
 * <code>OperationalStatus</code>.
 */
public class BitsEnumerationConstraint implements ValueConstraint {
	
	/**
	 * Checks a <code>Variable</code> objects accordingly to its validity.
	 * 
	 * @param variable
	 *            Variable
	 * @return Error code (see <code>PDU</code> class)
	 */
	public int validate(Variable variable) {
		if (variable instanceof OctetString) {
			if (SnmpUtil.bitsToOperState((OctetString)variable) == null)
				return PDU.badValue;
			return PDU.noError;
		}
		return PDU.wrongType;
	}
	
}
