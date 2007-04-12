package org.accada.reader.rprm.core.mgmt.agent.snmp.table ;

import junit.framework.TestCase;

import org.accada.reader.rprm.core.AntennaReadPoint;
import org.accada.reader.rprm.core.ReadPoint;
import org.accada.reader.rprm.core.ReaderDevice;
import org.accada.reader.rprm.core.mgmt.agent.snmp.SnmpAgent;
import org.accada.reader.rprm.core.mgmt.agent.snmp.mib.EpcglobalReaderMib;
import org.accada.reader.rprm.core.mgmt.agent.snmp.table.RowObjectContainer;
import org.accada.reader.rprm.core.mgmt.agent.snmp.table.SnmpTableRow;
import org.accada.reader.rprm.core.mgmt.agent.snmp.table.SnmpTable.TableTypeEnum;
import org.accada.reader.rprm.core.mgmt.alarm.AlarmLevel;
import org.accada.reader.rprm.core.msg.MessageLayer;
import org.apache.log4j.PropertyConfigurator;
import org.snmp4j.smi.Counter32;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.UnsignedInteger32;
import org.snmp4j.smi.Variable;

/**
 * Tests for the class <code>org.accada.reader.mgmt.agent.snmp.table.EpcgAntReadPointTableRow</code>.
 */
public class EpcgAntReadPointTableRowTest extends TestCase {
	
	
	private SnmpTableRow row;
	
	private AntennaReadPoint antReadPoint;
	
	private ReaderDevice readerDevice;
	
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
		
		readerDevice = ReaderDevice.getInstance();
		
		ReadPoint[] readPoints = readerDevice.getAllReadPoints();
		for (int i = 0; i < readPoints.length; i++) {
			if (readPoints[i] instanceof AntennaReadPoint)
			antReadPoint = (AntennaReadPoint) readPoints[i];
			row = SnmpTableRow.getSnmpTableRow(new RowObjectContainer(TableTypeEnum.EPCG_ANT_READ_POINT_TABLE, new Object[] { antReadPoint }));
			return;
		}
		fail();
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
		
		for (int i = 0; i < 1; i++) {
			antReadPoint.increaseIdentificationCount();
		}
		for (int i = 0; i < 2; i++) {
			antReadPoint.increaseWriteCount();
		}
		for (int i = 0; i < 3; i++) {
			antReadPoint.increaseKillCount();
		}
		for (int i = 0; i < 4; i++) {
			antReadPoint.increaseEraseCount();
		}
		for (int i = 0; i < 5; i++) {
			antReadPoint.increaseLockCount();
		}
		for (int i = 0; i < 6; i++) {
			antReadPoint.memReadFailureOccurred();
		}
		for (int i = 0; i < 7; i++) {
			antReadPoint.writeFailureOccurred();
		}
		for (int i = 0; i < 8; i++) {
			antReadPoint.killFailureOccurred();
		}
		for (int i = 0; i < 9; i++) {
			antReadPoint.eraseFailureOccurred();
		}
		for (int i = 0; i < 10; i++) {
			antReadPoint.lockFailureOccurred();
		}
		antReadPoint.getFailedMemReadAlarmControl().setEnabled(false);
		antReadPoint.getFailedMemReadAlarmControl().setLevel(AlarmLevel.ALERT);
		antReadPoint.getFailedWriteAlarmControl().setEnabled(true);
		antReadPoint.getFailedWriteAlarmControl().setLevel(AlarmLevel.CRITICAL);
		antReadPoint.getFailedKillAlarmControl().setEnabled(false);
		antReadPoint.getFailedKillAlarmControl().setLevel(AlarmLevel.DEBUG);
		antReadPoint.getFailedEraseAlarmControl().setEnabled(true);
		antReadPoint.getFailedEraseAlarmControl().setLevel(AlarmLevel.EMERGENCY);
		antReadPoint.getFailedLockAlarmControl().setEnabled(false);
		antReadPoint.getFailedLockAlarmControl().setLevel(AlarmLevel.ERROR);
		
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgAntRdPntTagsIdentified);
		assertEquals(new Integer32(antReadPoint.getIdentificationCount()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgAntRdPntTagsNotIdentified);
		assertEquals(new Integer32(antReadPoint.getFailedIdentificationCount()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgAntRdPntMemoryReadFailures);
		assertEquals(new Integer32(antReadPoint.getFailedMemReadCount()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgAntRdPntReadFailureNotifEnable);
		assertEquals(new Integer32(antReadPoint.getFailedMemReadAlarmControl().getEnabled() ? 1 : 2), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgAntRdPntReadFailureNotifLevel);
		assertEquals(new Integer32(antReadPoint.getFailedMemReadAlarmControl().getLevel().toInt()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgAntRdPntWriteOperations);
		assertEquals(new Integer32(antReadPoint.getWriteCount()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgAntRdPntWriteFailures);
		assertEquals(new Integer32(antReadPoint.getFailedWriteCount()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgAntRdPntWriteFailuresNotifEnable);
		assertEquals(new Integer32(antReadPoint.getFailedWriteAlarmControl().getEnabled() ? 1 : 2), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgAntRdPntWriteFailuresNotifLevel);
		assertEquals(new Integer32(antReadPoint.getFailedWriteAlarmControl().getLevel().toInt()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgAntRdPntKillOperations);
		assertEquals(new Integer32(antReadPoint.getKillCount()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgAntRdPntKillFailures);
		assertEquals(new Integer32(antReadPoint.getFailedKillCount()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgAntRdPntKillFailuresNotifEnable);
		assertEquals(new Integer32(antReadPoint.getFailedKillAlarmControl().getEnabled() ? 1 : 2), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgAntRdPntKillFailuresNotifLevel);
		assertEquals(new Integer32(antReadPoint.getFailedKillAlarmControl().getLevel().toInt()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgAntRdPntEraseOperations);
		assertEquals(new Integer32(antReadPoint.getEraseCount()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgAntRdPntEraseFailures);
		assertEquals(new Integer32(antReadPoint.getFailedEraseCount()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgAntRdPntEraseFailuresNotifEnable);
		assertEquals(new Integer32(antReadPoint.getFailedEraseAlarmControl().getEnabled() ? 1 : 2), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgAntRdPntEraseFailuresNotifLevel);
		assertEquals(new Integer32(antReadPoint.getFailedEraseAlarmControl().getLevel().toInt()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgAntRdPntLockOperations);
		assertEquals(new Integer32(antReadPoint.getLockCount()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgAntRdPntLockFailures);
		assertEquals(new Integer32(antReadPoint.getFailedLockCount()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgAntRdPntLockFailuresNotifEnable);
		assertEquals(new Integer32(antReadPoint.getFailedLockAlarmControl().getEnabled() ? 1 : 2), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgAntRdPntLockFailuresNotifLevel);
		assertEquals(new Integer32(antReadPoint.getFailedLockAlarmControl().getLevel().toInt()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgAntRdPntPowerLevel);
		assertEquals(new Integer32(antReadPoint.getPowerLevel()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgAntRdPntNoiseLevel);
		assertEquals(new Integer32(antReadPoint.getNoiseLevel()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgAntRdPntTimeEnergized);
//		assertEquals(new Gauge32(antReadPoint.getTimeEnergized()), value); // cannot test
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgAntRdPntMemoryReadOperations);
		assertEquals(new Integer32(antReadPoint.getMemReadCount()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgAntRdPntReadFailureSuppressInterval);
		assertEquals(new UnsignedInteger32(antReadPoint.getFailedMemReadAlarmControl().getSuppressInterval()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgAntRdPntReadFailureSuppressions);
		assertEquals(new Counter32(antReadPoint.getReadFailureSuppressions()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgAntRdPntWriteFailureSuppressInterval);
		assertEquals(new UnsignedInteger32(antReadPoint.getFailedWriteAlarmControl().getSuppressInterval()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgAntRdPntWriteFailureSuppressions);
		assertEquals(new Counter32(antReadPoint.getWriteFailureSuppressions()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgAntRdPntKillFailureSuppressInterval);
		assertEquals(new UnsignedInteger32(antReadPoint.getFailedKillAlarmControl().getSuppressInterval()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgAntRdPntKillFailureSuppressions);
		assertEquals(new Counter32(antReadPoint.getKillFailureSuppressions()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgAntRdPntEraseFailureSuppressInterval);
		assertEquals(new UnsignedInteger32(antReadPoint.getFailedEraseAlarmControl().getSuppressInterval()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgAntRdPntEraseFailureSuppressions);
		assertEquals(new Counter32(antReadPoint.getEraseFailureSuppressions()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgAntRdPntLockFailureSuppressInterval);
		assertEquals(new UnsignedInteger32(antReadPoint.getFailedLockAlarmControl().getSuppressInterval()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgAntRdPntLockFailureSuppressions);
		assertEquals(new Counter32(antReadPoint.getLockFailureSuppressions()), value);
	}
	
	/**
	 * Tests the <code>setValue()</code> method.
	 */
	public final void testSetValue() {
		int value;
		AlarmLevel level;
		
		value = 1;
		row.setValue(EpcglobalReaderMib.idxEpcgAntRdPntReadFailureNotifEnable, new Integer32(value));
		assertEquals(value, antReadPoint.getFailedMemReadAlarmControl().getEnabled() ? 1 : 2);
		value = 2;
		row.setValue(EpcglobalReaderMib.idxEpcgAntRdPntReadFailureNotifEnable, new Integer32(value));
		assertEquals(value, antReadPoint.getFailedMemReadAlarmControl().getEnabled() ? 1 : 2);
		
		level = AlarmLevel.CRITICAL;
		row.setValue(EpcglobalReaderMib.idxEpcgAntRdPntReadFailureNotifLevel, new Integer32(level.toInt()));
		assertEquals(level, antReadPoint.getFailedMemReadAlarmControl().getLevel());
		level = AlarmLevel.DEBUG;
		row.setValue(EpcglobalReaderMib.idxEpcgAntRdPntReadFailureNotifLevel, new Integer32(level.toInt()));
		assertEquals(level, antReadPoint.getFailedMemReadAlarmControl().getLevel());
		
		value = 1;
		row.setValue(EpcglobalReaderMib.idxEpcgAntRdPntWriteFailuresNotifEnable, new Integer32(value));
		assertEquals(value, antReadPoint.getFailedWriteAlarmControl().getEnabled() ? 1 : 2);
		value = 2;
		row.setValue(EpcglobalReaderMib.idxEpcgAntRdPntWriteFailuresNotifEnable, new Integer32(value));
		assertEquals(value, antReadPoint.getFailedWriteAlarmControl().getEnabled() ? 1 : 2);
		
		level = AlarmLevel.CRITICAL;
		row.setValue(EpcglobalReaderMib.idxEpcgAntRdPntWriteFailuresNotifLevel, new Integer32(level.toInt()));
		assertEquals(level, antReadPoint.getFailedWriteAlarmControl().getLevel());
		level = AlarmLevel.DEBUG;
		row.setValue(EpcglobalReaderMib.idxEpcgAntRdPntWriteFailuresNotifLevel, new Integer32(level.toInt()));
		assertEquals(level, antReadPoint.getFailedWriteAlarmControl().getLevel());
		
		value = 1;
		row.setValue(EpcglobalReaderMib.idxEpcgAntRdPntKillFailuresNotifEnable, new Integer32(value));
		assertEquals(value, antReadPoint.getFailedKillAlarmControl().getEnabled() ? 1 : 2);
		value = 2;
		row.setValue(EpcglobalReaderMib.idxEpcgAntRdPntKillFailuresNotifEnable, new Integer32(value));
		assertEquals(value, antReadPoint.getFailedKillAlarmControl().getEnabled() ? 1 : 2);
		
		level = AlarmLevel.CRITICAL;
		row.setValue(EpcglobalReaderMib.idxEpcgAntRdPntKillFailuresNotifLevel, new Integer32(level.toInt()));
		assertEquals(level, antReadPoint.getFailedKillAlarmControl().getLevel());
		level = AlarmLevel.DEBUG;
		row.setValue(EpcglobalReaderMib.idxEpcgAntRdPntKillFailuresNotifLevel, new Integer32(level.toInt()));
		assertEquals(level, antReadPoint.getFailedKillAlarmControl().getLevel());
		
		value = 1;
		row.setValue(EpcglobalReaderMib.idxEpcgAntRdPntEraseFailuresNotifEnable, new Integer32(value));
		assertEquals(value, antReadPoint.getFailedEraseAlarmControl().getEnabled() ? 1 : 2);
		value = 2;
		row.setValue(EpcglobalReaderMib.idxEpcgAntRdPntEraseFailuresNotifEnable, new Integer32(value));
		assertEquals(value, antReadPoint.getFailedEraseAlarmControl().getEnabled() ? 1 : 2);
		
		level = AlarmLevel.CRITICAL;
		row.setValue(EpcglobalReaderMib.idxEpcgAntRdPntEraseFailuresNotifLevel, new Integer32(level.toInt()));
		assertEquals(level, antReadPoint.getFailedEraseAlarmControl().getLevel());
		level = AlarmLevel.DEBUG;
		row.setValue(EpcglobalReaderMib.idxEpcgAntRdPntEraseFailuresNotifLevel, new Integer32(level.toInt()));
		assertEquals(level, antReadPoint.getFailedEraseAlarmControl().getLevel());
		
		value = 1;
		row.setValue(EpcglobalReaderMib.idxEpcgAntRdPntLockFailuresNotifEnable, new Integer32(value));
		assertEquals(value, antReadPoint.getFailedLockAlarmControl().getEnabled() ? 1 : 2);
		value = 2;
		row.setValue(EpcglobalReaderMib.idxEpcgAntRdPntLockFailuresNotifEnable, new Integer32(value));
		assertEquals(value, antReadPoint.getFailedLockAlarmControl().getEnabled() ? 1 : 2);
		
		level = AlarmLevel.CRITICAL;
		row.setValue(EpcglobalReaderMib.idxEpcgAntRdPntLockFailuresNotifLevel, new Integer32(level.toInt()));
		assertEquals(level, antReadPoint.getFailedLockAlarmControl().getLevel());
		level = AlarmLevel.DEBUG;
		row.setValue(EpcglobalReaderMib.idxEpcgAntRdPntLockFailuresNotifLevel, new Integer32(level.toInt()));
		assertEquals(level, antReadPoint.getFailedLockAlarmControl().getLevel());
		
		value = 1;
		row.setValue(EpcglobalReaderMib.idxEpcgAntRdPntReadFailureSuppressInterval, new UnsignedInteger32(value));
		assertEquals(value, antReadPoint.getFailedMemReadAlarmControl().getSuppressInterval());
		value = 100;
		row.setValue(EpcglobalReaderMib.idxEpcgAntRdPntReadFailureSuppressInterval, new UnsignedInteger32(value));
		assertEquals(value, antReadPoint.getFailedMemReadAlarmControl().getSuppressInterval());
		
		value = 1;
		row.setValue(EpcglobalReaderMib.idxEpcgAntRdPntWriteFailureSuppressInterval, new UnsignedInteger32(value));
		assertEquals(value, antReadPoint.getFailedWriteAlarmControl().getSuppressInterval());
		value = 100;
		row.setValue(EpcglobalReaderMib.idxEpcgAntRdPntWriteFailureSuppressInterval, new UnsignedInteger32(value));
		assertEquals(value, antReadPoint.getFailedWriteAlarmControl().getSuppressInterval());
		
		value = 1;
		row.setValue(EpcglobalReaderMib.idxEpcgAntRdPntKillFailureSuppressInterval, new UnsignedInteger32(value));
		assertEquals(value, antReadPoint.getFailedKillAlarmControl().getSuppressInterval());
		value = 100;
		row.setValue(EpcglobalReaderMib.idxEpcgAntRdPntKillFailureSuppressInterval, new UnsignedInteger32(value));
		assertEquals(value, antReadPoint.getFailedKillAlarmControl().getSuppressInterval());
		
		value = 1;
		row.setValue(EpcglobalReaderMib.idxEpcgAntRdPntEraseFailureSuppressInterval, new UnsignedInteger32(value));
		assertEquals(value, antReadPoint.getFailedEraseAlarmControl().getSuppressInterval());
		value = 100;
		row.setValue(EpcglobalReaderMib.idxEpcgAntRdPntEraseFailureSuppressInterval, new UnsignedInteger32(value));
		assertEquals(value, antReadPoint.getFailedEraseAlarmControl().getSuppressInterval());
		
		value = 1;
		row.setValue(EpcglobalReaderMib.idxEpcgAntRdPntLockFailureSuppressInterval, new UnsignedInteger32(value));
		assertEquals(value, antReadPoint.getFailedLockAlarmControl().getSuppressInterval());
		value = 100;
		row.setValue(EpcglobalReaderMib.idxEpcgAntRdPntLockFailureSuppressInterval, new UnsignedInteger32(value));
		assertEquals(value, antReadPoint.getFailedLockAlarmControl().getSuppressInterval());
	}
	
	/**
	 * Runs the test using the gui runner.
	 * @param args No arguments
	 */
	public static void main(String[] args) {
        junit.swingui.TestRunner.run(EpcgAntReadPointTableRowTest.class);
    }
	
}
