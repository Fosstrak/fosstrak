package org.accada.reader.rprm.core.mgmt.agent.snmp.table ;

import junit.framework.TestCase;

import org.accada.reader.rprm.core.mgmt.AdministrativeStatus;
import org.accada.reader.rprm.core.mgmt.IOPort;
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
 * Tests for the class <code>org.accada.reader.mgmt.agent.snmp.table.EpcgIoPortTableRow</code>.
 */
public class EpcgIoPortTableRowTest extends TestCase {
	
	
	private SnmpTableRow row;
	
	private IOPort ioPort;
	
	/**
	 * Sets up the test.
	 * @exception Exception An error occurred
	 */
	protected final void setUp() throws Exception {
		super.setUp();
		
		PropertyConfigurator.configure("./props/log4j.properties");
		
		if (SnmpAgent.getInstance() == null) {
			MessageLayer.main(new String[] { });
		}
		
		ioPort = new IOPort("EpcgIoPortTableRowTestIOPort");
		
		row = SnmpTableRow.getSnmpTableRow(new RowObjectContainer(TableTypeEnum.EPCG_IO_PORT_TABLE, new Object[] { ioPort }));
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
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgIoPortName);
		assertEquals(new OctetString(ioPort.getName()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgIoPortAdminStatus);
		assertEquals(new Integer32(ioPort.getAdminStatus().toInt()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgIoPortOperStatus);
		assertEquals(new Integer32(ioPort.getOperStatus().toInt()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgIoPortOperStatusNotifEnable);
		assertEquals(new Integer32(ioPort.getOperStatusAlarmControl().getEnabled() ? 1 : 2), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgIoPortOperStatusNotifLevel);
		assertEquals(new Integer32(ioPort.getOperStatusAlarmControl().getLevel().toInt()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgIoPortOperStatusNotifFromState);
		assertEquals(SnmpUtil.operStateToBITS(ioPort.getOperStatusAlarmControl().getTriggerFromState()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgIoPortOperStatusNotifToState);
		assertEquals(SnmpUtil.operStateToBITS(ioPort.getOperStatusAlarmControl().getTriggerToState()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgIoPortDescription);
		assertEquals(new OctetString(ioPort.getDescription()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgIoPortOperStateSuppressInterval);
		assertEquals(new UnsignedInteger32(ioPort.getOperStatusAlarmControl().getSuppressInterval()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgIoPortOperStateSuppressions);
		assertEquals(new Counter32(ioPort.getOperStateSuppressions()), value);
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
		row.setValue(EpcglobalReaderMib.idxEpcgIoPortAdminStatus, new Integer32(adminStatus.toInt()));
		assertEquals(adminStatus, ioPort.getAdminStatus());
		adminStatus = AdministrativeStatus.DOWN;
		row.setValue(EpcglobalReaderMib.idxEpcgIoPortAdminStatus, new Integer32(adminStatus.toInt()));
		assertEquals(adminStatus, ioPort.getAdminStatus());
		
		value = 1;
		row.setValue(EpcglobalReaderMib.idxEpcgIoPortOperStatusNotifEnable, new Integer32(value));
		assertEquals(value, ioPort.getOperStatusAlarmControl().getEnabled() ? 1 : 2);
		value = 2;
		row.setValue(EpcglobalReaderMib.idxEpcgIoPortOperStatusNotifEnable, new Integer32(value));
		assertEquals(value, ioPort.getOperStatusAlarmControl().getEnabled() ? 1 : 2);
		
		level = AlarmLevel.CRITICAL;
		row.setValue(EpcglobalReaderMib.idxEpcgIoPortOperStatusNotifLevel, new Integer32(level.toInt()));
		assertEquals(level, ioPort.getOperStatusAlarmControl().getLevel());
		level = AlarmLevel.DEBUG;
		row.setValue(EpcglobalReaderMib.idxEpcgIoPortOperStatusNotifLevel, new Integer32(level.toInt()));
		assertEquals(level, ioPort.getOperStatusAlarmControl().getLevel());
		
		operStatus = OperationalStatus.UP;
		row.setValue(EpcglobalReaderMib.idxEpcgIoPortOperStatusNotifFromState, SnmpUtil.operStateToBITS(operStatus));
		assertEquals(operStatus, ioPort.getOperStatusAlarmControl().getTriggerFromState());
		operStatus = OperationalStatus.DOWN;
		row.setValue(EpcglobalReaderMib.idxEpcgIoPortOperStatusNotifFromState, SnmpUtil.operStateToBITS(operStatus));
		assertEquals(operStatus, ioPort.getOperStatusAlarmControl().getTriggerFromState());
		
		operStatus = OperationalStatus.UP;
		row.setValue(EpcglobalReaderMib.idxEpcgIoPortOperStatusNotifToState, SnmpUtil.operStateToBITS(operStatus));
		assertEquals(operStatus, ioPort.getOperStatusAlarmControl().getTriggerToState());
		operStatus = OperationalStatus.DOWN;
		row.setValue(EpcglobalReaderMib.idxEpcgIoPortOperStatusNotifToState, SnmpUtil.operStateToBITS(operStatus));
		assertEquals(operStatus, ioPort.getOperStatusAlarmControl().getTriggerToState());
		
		value = 1;
		row.setValue(EpcglobalReaderMib.idxEpcgIoPortOperStateSuppressInterval, new UnsignedInteger32(value));
		assertEquals(value, ioPort.getOperStatusAlarmControl().getSuppressInterval());
		value = 100;
		row.setValue(EpcglobalReaderMib.idxEpcgIoPortOperStateSuppressInterval, new UnsignedInteger32(value));
		assertEquals(value, ioPort.getOperStatusAlarmControl().getSuppressInterval());
	}
	
	/**
	 * Runs the test using the gui runner.
	 * @param args No arguments
	 */
	public static void main(String[] args) {
        junit.swingui.TestRunner.run(EpcgIoPortTableRowTest.class);
    }
	
}
