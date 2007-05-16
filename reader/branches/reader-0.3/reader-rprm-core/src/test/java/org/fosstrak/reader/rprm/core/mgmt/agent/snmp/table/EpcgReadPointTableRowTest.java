package org.accada.reader.rprm.core.mgmt.agent.snmp.table ;

import junit.framework.TestCase;

import org.accada.reader.rprm.core.ReadPoint;
import org.accada.reader.rprm.core.ReaderDevice;
import org.accada.reader.rprm.core.mgmt.AdministrativeStatus;
import org.accada.reader.rprm.core.mgmt.OperationalStatus;
import org.accada.reader.rprm.core.mgmt.agent.snmp.SnmpAgent;
import org.accada.reader.rprm.core.mgmt.agent.snmp.mib.EpcglobalReaderMib;
import org.accada.reader.rprm.core.mgmt.agent.snmp.table.RowObjectContainer;
import org.accada.reader.rprm.core.mgmt.agent.snmp.table.SnmpTableRow;
import org.accada.reader.rprm.core.mgmt.agent.snmp.table.SnmpTable.TableTypeEnum;
import org.accada.reader.rprm.core.mgmt.alarm.AlarmLevel;
import org.accada.reader.rprm.core.mgmt.util.SnmpUtil;
import org.accada.reader.rprm.core.msg.MessageLayer;
import org.apache.log4j.PropertyConfigurator;
import org.snmp4j.smi.Counter32;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UnsignedInteger32;
import org.snmp4j.smi.Variable;

/**
 * Tests for the class <code>org.accada.reader.mgmt.agent.snmp.table.EpcgReadPointTableRow</code>.
 */
public class EpcgReadPointTableRowTest extends TestCase {


	private SnmpTableRow row;

	private ReadPoint readPoint;

	private ReaderDevice readerDevice;

	/**
	 * Sets up the test.
	 * @exception Exception An error occurred
	 */
	protected final void setUp() throws Exception {
		super.setUp();

		PropertyConfigurator.configure("./target/classes/props/log4j.properties");

		if (SnmpAgent.getInstance() == null) {
			MessageLayer.main(new String[] { });
		}

		readerDevice = ReaderDevice.getInstance();

		readPoint = readerDevice.getAllReadPoints()[0];

		row = SnmpTableRow.getSnmpTableRow(new RowObjectContainer(TableTypeEnum.EPCG_READ_POINT_TABLE, new Object[] { readPoint }));
	}

	/**
	 * Does the cleanup.
	 * @exception Exception An error occurred
	 */
	protected final void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Tests the <code>getValue()</code> method.
	 */
	public final void testGetValue() {
		Variable value;

		value = row.getValue(EpcglobalReaderMib.idxEpcgReadPointName);
		assertEquals(new OctetString(readPoint.getName()), value);

		value = row.getValue(EpcglobalReaderMib.idxEpcgReadPointDescription);
		assertEquals(new OctetString(readPoint.getDescription()), value);

		value = row.getValue(EpcglobalReaderMib.idxEpcgReadPointAdminStatus);
		assertEquals(new Integer32(readPoint.getAdminStatus().toInt()), value);

		value = row.getValue(EpcglobalReaderMib.idxEpcgReadPointOperStatus);
		assertEquals(new Integer32(readPoint.getOperStatus().toInt()), value);

		value = row.getValue(EpcglobalReaderMib.idxEpcgReadPointOperStateNotifyEnable);
		assertEquals(new Integer32(readPoint.getOperStatusAlarmControl().getEnabled() ? 1 : 2), value);

		value = row.getValue(EpcglobalReaderMib.idxEpcgReadPointOperNotifyFromState);
		assertEquals(SnmpUtil.operStateToBITS(readPoint.getOperStatusAlarmControl().getTriggerFromState()), value);

		value = row.getValue(EpcglobalReaderMib.idxEpcgReadPointOperNotifyToState);
		assertEquals(SnmpUtil.operStateToBITS(readPoint.getOperStatusAlarmControl().getTriggerToState()), value);

		value = row.getValue(EpcglobalReaderMib.idxEpcgReadPointOperNotifyStateLevel);
		assertEquals(new Integer32(readPoint.getOperStatusAlarmControl().getLevel().toInt()), value);

		value = row.getValue(EpcglobalReaderMib.idxEpcgReadPointOperStateSuppressInterval);
		assertEquals(new UnsignedInteger32(readPoint.getOperStatusAlarmControl().getSuppressInterval()), value);

		value = row.getValue(EpcglobalReaderMib.idxEpcgReadPointOperStateSuppressions);
		assertEquals(new Counter32(readPoint.getOperStateSuppressions()), value);
	}

	/**
	 * Tests the <code>setValue()</code> method.
	 */
	public final void testSetValue() {
		int value;
		AlarmLevel level;
		AdministrativeStatus adminStatus;
		OperationalStatus operStatus;

		adminStatus = AdministrativeStatus.UP;
		row.setValue(EpcglobalReaderMib.idxEpcgReadPointAdminStatus, new Integer32(adminStatus.toInt()));
		assertEquals(adminStatus, readPoint.getAdminStatus());
		adminStatus = AdministrativeStatus.DOWN;
		row.setValue(EpcglobalReaderMib.idxEpcgReadPointAdminStatus, new Integer32(adminStatus.toInt()));
		assertEquals(adminStatus, readPoint.getAdminStatus());

		value = 1;
		row.setValue(EpcglobalReaderMib.idxEpcgReadPointOperStateNotifyEnable, new Integer32(value));
		assertEquals(value, readPoint.getOperStatusAlarmControl().getEnabled() ? 1 : 2);
		value = 2;
		row.setValue(EpcglobalReaderMib.idxEpcgReadPointOperStateNotifyEnable, new Integer32(value));
		assertEquals(value, readPoint.getOperStatusAlarmControl().getEnabled() ? 1 : 2);

		operStatus = OperationalStatus.UP;
		row.setValue(EpcglobalReaderMib.idxEpcgReadPointOperNotifyFromState, SnmpUtil.operStateToBITS(operStatus));
		assertEquals(operStatus, readPoint.getOperStatusAlarmControl().getTriggerFromState());
		operStatus = OperationalStatus.DOWN;
		row.setValue(EpcglobalReaderMib.idxEpcgReadPointOperNotifyFromState, SnmpUtil.operStateToBITS(operStatus));
		assertEquals(operStatus, readPoint.getOperStatusAlarmControl().getTriggerFromState());

		operStatus = OperationalStatus.UP;
		row.setValue(EpcglobalReaderMib.idxEpcgReadPointOperNotifyToState, SnmpUtil.operStateToBITS(operStatus));
		assertEquals(operStatus, readPoint.getOperStatusAlarmControl().getTriggerToState());
		operStatus = OperationalStatus.DOWN;
		row.setValue(EpcglobalReaderMib.idxEpcgReadPointOperNotifyToState, SnmpUtil.operStateToBITS(operStatus));
		assertEquals(operStatus, readPoint.getOperStatusAlarmControl().getTriggerToState());

		level = AlarmLevel.CRITICAL;
		row.setValue(EpcglobalReaderMib.idxEpcgReadPointOperNotifyStateLevel, new Integer32(level.toInt()));
		assertEquals(level, readPoint.getOperStatusAlarmControl().getLevel());
		level = AlarmLevel.DEBUG;
		row.setValue(EpcglobalReaderMib.idxEpcgReadPointOperNotifyStateLevel, new Integer32(level.toInt()));
		assertEquals(level, readPoint.getOperStatusAlarmControl().getLevel());

		value = 1;
		row.setValue(EpcglobalReaderMib.idxEpcgReadPointOperStateSuppressInterval, new UnsignedInteger32(value));
		assertEquals(value, readPoint.getOperStatusAlarmControl().getSuppressInterval());
		value = 100;
		row.setValue(EpcglobalReaderMib.idxEpcgReadPointOperStateSuppressInterval, new UnsignedInteger32(value));
		assertEquals(value, readPoint.getOperStatusAlarmControl().getSuppressInterval());
	}

	/**
	 * Runs the test using the gui runner.
	 * @param args No arguments
	 */
	public static void main(String[] args) {
        junit.swingui.TestRunner.run(EpcgReadPointTableRowTest.class);
    }

}
