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

package org.accada.reader.rprm.core.mgmt.agent.snmp.table;

import java.util.Enumeration;
import java.util.Vector;

import org.accada.reader.rprm.core.ReadPoint;
import org.accada.reader.rprm.core.ReaderProtocolException;
import org.accada.reader.rprm.core.mgmt.AdministrativeStatus;
import org.accada.reader.rprm.core.mgmt.agent.snmp.mib.EpcglobalReaderMib;
import org.accada.reader.rprm.core.mgmt.agent.snmp.table.SnmpTable.TableTypeEnum;
import org.accada.reader.rprm.core.mgmt.alarm.AlarmLevel;
import org.accada.reader.rprm.core.mgmt.util.SnmpUtil;
import org.apache.log4j.Logger;
import org.snmp4j.smi.Counter32;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UnsignedInteger32;
import org.snmp4j.smi.Variable;

/**
 * Forms a row of the epcgReadPointTable.
 */
public class EpcgReadPointTableRow extends SnmpTableRow {
	
	private static long refreshTimeInMs = 100; // default value of the refresh time
	
	public static final int numOfColumns = EpcglobalReaderMib.getInstance().getEpcgReadPointEntry().getColumnCount();
	private static Logger log = Logger.getLogger(EpcgReadPointTableRow.class);
	private long[] lastRefreshTimes = new long[EpcgReadPointTableRow.numOfColumns];
	private ReadPoint readPoint;
	
	/**
	 * Protected Constructor. Use <code>getSnmpTableRow()</code> to
	 * instantiate.
	 * 
	 * @param cont
	 *            Row object container which corresponds to this row
	 * @throws ReaderProtocolException
	 *             Thrown if the <code>ReaderDevice</code> instance cannot be
	 *             obtained.
	 */
	protected EpcgReadPointTableRow(RowObjectContainer cont)
			throws ReaderProtocolException {
		super(EpcgReadPointTableRow.computeIndex(cont), new Variable[numOfColumns], cont);
		readPoint = (ReadPoint)cont.getRowObjects()[0];
	}
	
	/**
	 * Computes the index for this row
	 * 
	 * @param cont
	 *            Row object container of this row
	 * @return Index
	 */
	private static OID computeIndex(RowObjectContainer cont) {
		SnmpTable table = (SnmpTable)SnmpUtil.getSnmpTable(TableTypeEnum.EPCG_READ_POINT_TABLE);
		Vector<OID> indices = table.getSortedIndices();
		
		Enumeration<OID> elements = indices.elements();
		while (elements.hasMoreElements()) {
			OID tempIndex = elements.nextElement().nextPeer();
			if (!indices.contains(tempIndex)) return tempIndex;
		}
		return new OID("1");
	}
	
	/**
	 * Gets the value at the specified column index.
	 * 
	 * @param column
	 *            The (zero-based) column index
	 * @return The value at the specified index
	 */
	@Override
	public Variable getValue(int column) {
		boolean refresh = ((System.currentTimeMillis() - lastRefreshTimes[column]) > refreshTimeInMs);
		if (refresh) {
			log.debug("refreshing row " + index.toString());
			lastRefreshTimes[column] = System.currentTimeMillis();
		} else {
			log.debug("no need to refresh row " + index.toString());
		}
		
		switch (column) {
			case EpcglobalReaderMib.idxEpcgReadPointName:
				if (refresh) setValue(column, new OctetString(readPoint.getName()));
				break;
			case EpcglobalReaderMib.idxEpcgReadPointDescription:
				if (refresh) setValue(column, new OctetString(readPoint.getDescription()));
				break;
			case EpcglobalReaderMib.idxEpcgReadPointAdminStatus:
				if (refresh) setValue(column, new Integer32(readPoint.getAdminStatus().toInt()));
				break;
			case EpcglobalReaderMib.idxEpcgReadPointOperStatus:
				if (refresh) setValue(column, new Integer32(readPoint.getOperStatus().toInt()));
				break;
			case EpcglobalReaderMib.idxEpcgReadPointOperStateNotifyEnable:
				if (refresh) setValue(column, new Integer32(readPoint.getOperStatusAlarmControl().getEnabled() ? 1 : 2));
				break;
			case EpcglobalReaderMib.idxEpcgReadPointOperNotifyFromState:
				if (refresh) setValue(column, SnmpUtil.operStateToBITS(readPoint.getOperStatusAlarmControl().getTriggerFromState()));
				break;
			case EpcglobalReaderMib.idxEpcgReadPointOperNotifyToState:
				if (refresh) setValue(column, SnmpUtil.operStateToBITS(readPoint.getOperStatusAlarmControl().getTriggerToState()));
				break;
			case EpcglobalReaderMib.idxEpcgReadPointOperNotifyStateLevel:
				if (refresh) setValue(column, new Integer32(readPoint.getOperStatusAlarmControl().getLevel().toInt()));
				break;
			case EpcglobalReaderMib.idxEpcgReadPointOperStateSuppressInterval:
				if (refresh) setValue(column, new UnsignedInteger32(readPoint.getOperStatusAlarmControl().getSuppressInterval()));
				break;
			case EpcglobalReaderMib.idxEpcgReadPointOperStateSuppressions:
				if (refresh) setValue(column, new Counter32(readPoint.getOperStateSuppressions()));
				break;
		}
		return super.getValue(column);
	}
	
	/**
	 * Sets the value of a column of this row.
	 * 
	 * @param column
	 *            The (zero-based) column index
	 * @param value
	 *            The new value for the specified column
	 */
	@Override
	public void setValue(int column, Variable value) {
		switch (column) {
			case EpcglobalReaderMib.idxEpcgReadPointAdminStatus:
				readPoint.setAdminStatus(AdministrativeStatus
					.intToEnum(((Integer32) value).toInt()));
				break;
			case EpcglobalReaderMib.idxEpcgReadPointOperStateNotifyEnable:
				readPoint.getOperStatusAlarmControl().setEnabled(
					((Integer32) value).toInt() == 1 ? true : false);
				break;
			case EpcglobalReaderMib.idxEpcgReadPointOperNotifyFromState:
				readPoint.getOperStatusAlarmControl().setTriggerFromState(SnmpUtil.bitsToOperState((OctetString)value));
				break;
			case EpcglobalReaderMib.idxEpcgReadPointOperNotifyToState:
				readPoint.getOperStatusAlarmControl().setTriggerToState(SnmpUtil.bitsToOperState((OctetString)value));
				break;
			case EpcglobalReaderMib.idxEpcgReadPointOperNotifyStateLevel:
				readPoint.getOperStatusAlarmControl().setLevel(
					AlarmLevel.intToEnum(((Integer32) value).toInt()));
				break;
			case EpcglobalReaderMib.idxEpcgReadPointOperStateSuppressInterval:
				readPoint.getOperStatusAlarmControl().setSuppressInterval(((UnsignedInteger32) value).toInt());
				break;
		}
		super.setValue(column, value);
	}

	/**
	 * Forces a refresh of all values.
	 */
	public void forceRefresh() {
		for (int i = 0; i < lastRefreshTimes.length; i++) {
			lastRefreshTimes[i] = 0;
		}
	}
	
	/**
	 * Forces a refresh of the value of a given column.
	 * 
	 * @param column
	 *            Column
	 */
	public void forceRefresh(int column) {
		lastRefreshTimes[column] = 0;
	}
	
	/**
	 * Sets the refresh time in ms.
	 * 
	 * @param refreshTimeInMs
	 *            Refresh time in ms
	 */
	public void setRefreshTime(long refreshTimeInMs) {
		EpcgReadPointTableRow.refreshTimeInMs = refreshTimeInMs;
	}

}
