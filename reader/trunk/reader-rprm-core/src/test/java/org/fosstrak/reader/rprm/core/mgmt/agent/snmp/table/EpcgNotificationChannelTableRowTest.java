package org.accada.reader.rprm.core.mgmt.agent.snmp.table ;

import java.util.Date;

import junit.framework.TestCase;

import org.accada.reader.rprm.core.NotificationChannel;
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
 * Tests for the class <code>org.accada.reader.mgmt.agent.snmp.table.EpcgNotificationChannelTableRow</code>.
 */
public class EpcgNotificationChannelTableRowTest extends TestCase {
	
	
	private SnmpTableRow row;
	
	private NotificationChannel notifChan;
	
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
		
		notifChan = NotificationChannel.create("EpcgNotificationChannelTableRowTestNotifChan", "addr", readerDevice);
		
		row = SnmpTableRow.getSnmpTableRow(new RowObjectContainer(TableTypeEnum.EPCG_NOTIFICATION_CHANNEL_TABLE, new Object[] { notifChan }));
	}
	
	/**
	 * Does the cleanup.
	 * @exception Exception An error occurred
	 */
	protected final void tearDown() throws Exception {
		super.tearDown();
		
		readerDevice.removeNotificationChannels(new NotificationChannel[] { notifChan });
	}
	
	/**
	 * Tests the <code>getValue()</code> method.
	 */
	public final void testGetValue() {
		Variable value;
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgNotifChanName);
		assertEquals(new OctetString(notifChan.getName()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgNotifChanAddressType);
//		assertEquals(new Integer32(1), value); // TODO: implement
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgNotifChanAddress);
		assertEquals(new OctetString(notifChan.getAddress()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgNotifChanLastAttempt);
		Date lastAttempt = notifChan.getLastNotificationAttempt();
		if (lastAttempt != null) {
			assertEquals(SnmpUtil.dateToOctetString(lastAttempt), value);
		} else {
			assertNull(value);
		}
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgNotifChanLastSuccess);
		Date lastSuccess = notifChan.getLastSuccessfulNotification();
		if (lastSuccess != null) {
			assertEquals(SnmpUtil.dateToOctetString(lastSuccess), value);
		} else {
			assertNull(value);
		}
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgNotifChanAdminStatus);
		assertEquals(new Integer32(notifChan.getAdminStatus().toInt()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgNotifChanOperStatus);
		assertEquals(new Integer32(notifChan.getOperStatus().toInt()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgNotifChanOperNotifEnable);
		assertEquals(new Integer32(notifChan.getOperStatusAlarmControl().getEnabled() ? 1 : 2), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgNotifChanOperNotifLevel);
		assertEquals(new Integer32(notifChan.getOperStatusAlarmControl().getLevel().toInt()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgNotifChanOperNotifFromState);
		assertEquals(SnmpUtil.operStateToBITS(notifChan.getOperStatusAlarmControl().getTriggerFromState()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgNotifChanOperNotifToState);
		assertEquals(SnmpUtil.operStateToBITS(notifChan.getOperStatusAlarmControl().getTriggerToState()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgNotifChanOperStateSuppressInterval);
		assertEquals(new UnsignedInteger32(notifChan.getOperStatusAlarmControl().getSuppressInterval()), value);
		
		value = row.getValue(EpcglobalReaderMib.idxEpcgNotifChanOperStateSuppressions);
		assertEquals(new Counter32(notifChan.getOperStateSuppressions()), value);
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
		row.setValue(EpcglobalReaderMib.idxEpcgNotifChanAdminStatus, new Integer32(adminStatus.toInt()));
		assertEquals(adminStatus, notifChan.getAdminStatus());
		adminStatus = AdministrativeStatus.DOWN;
		row.setValue(EpcglobalReaderMib.idxEpcgNotifChanAdminStatus, new Integer32(adminStatus.toInt()));
		assertEquals(adminStatus, notifChan.getAdminStatus());
		
		value = 1;
		row.setValue(EpcglobalReaderMib.idxEpcgNotifChanOperNotifEnable, new Integer32(value));
		assertEquals(value, notifChan.getOperStatusAlarmControl().getEnabled() ? 1 : 2);
		value = 2;
		row.setValue(EpcglobalReaderMib.idxEpcgNotifChanOperNotifEnable, new Integer32(value));
		assertEquals(value, notifChan.getOperStatusAlarmControl().getEnabled() ? 1 : 2);
		
		level = AlarmLevel.CRITICAL;
		row.setValue(EpcglobalReaderMib.idxEpcgNotifChanOperNotifLevel, new Integer32(level.toInt()));
		assertEquals(level, notifChan.getOperStatusAlarmControl().getLevel());
		level = AlarmLevel.DEBUG;
		row.setValue(EpcglobalReaderMib.idxEpcgNotifChanOperNotifLevel, new Integer32(level.toInt()));
		assertEquals(level, notifChan.getOperStatusAlarmControl().getLevel());
		
		operStatus = OperationalStatus.UP;
		row.setValue(EpcglobalReaderMib.idxEpcgNotifChanOperNotifFromState, SnmpUtil.operStateToBITS(operStatus));
		assertEquals(operStatus, notifChan.getOperStatusAlarmControl().getTriggerFromState());
		operStatus = OperationalStatus.DOWN;
		row.setValue(EpcglobalReaderMib.idxEpcgNotifChanOperNotifFromState, SnmpUtil.operStateToBITS(operStatus));
		assertEquals(operStatus, notifChan.getOperStatusAlarmControl().getTriggerFromState());
		
		operStatus = OperationalStatus.UP;
		row.setValue(EpcglobalReaderMib.idxEpcgNotifChanOperNotifToState, SnmpUtil.operStateToBITS(operStatus));
		assertEquals(operStatus, notifChan.getOperStatusAlarmControl().getTriggerToState());
		operStatus = OperationalStatus.DOWN;
		row.setValue(EpcglobalReaderMib.idxEpcgNotifChanOperNotifToState, SnmpUtil.operStateToBITS(operStatus));
		assertEquals(operStatus, notifChan.getOperStatusAlarmControl().getTriggerToState());
		
		value = 1;
		row.setValue(EpcglobalReaderMib.idxEpcgNotifChanOperStateSuppressInterval, new UnsignedInteger32(value));
		assertEquals(value, notifChan.getOperStatusAlarmControl().getSuppressInterval());
		value = 100;
		row.setValue(EpcglobalReaderMib.idxEpcgNotifChanOperStateSuppressInterval, new UnsignedInteger32(value));
		assertEquals(value, notifChan.getOperStatusAlarmControl().getSuppressInterval());
	}
	
	/**
	 * Runs the test using the gui runner.
	 * @param args No arguments
	 */
	public static void main(String[] args) {
        junit.swingui.TestRunner.run(EpcgNotificationChannelTableRowTest.class);
    }
	
}
