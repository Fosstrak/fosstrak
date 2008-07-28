package org.fosstrak.reader.rprm.core.mgmt.agent.snmp.table ;

import junit.framework.TestCase;

import org.fosstrak.reader.rprm.core.ReaderDevice;
import org.fosstrak.reader.rprm.core.ReaderProtocolException;
import org.fosstrak.reader.rprm.core.Source;
import org.fosstrak.reader.rprm.core.mgmt.AdministrativeStatus;
import org.fosstrak.reader.rprm.core.mgmt.OperationalStatus;
import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.SnmpAgent;
import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.mib.EpcglobalReaderMib;
import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.table.RowObjectContainer;
import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.table.SnmpTableRow;
import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.table.SnmpTable.TableTypeEnum;
import org.fosstrak.reader.rprm.core.mgmt.alarm.AlarmLevel;
import org.fosstrak.reader.rprm.core.mgmt.util.SnmpUtil;
import org.fosstrak.reader.rprm.core.msg.MessageLayer;
import org.apache.log4j.xml.DOMConfigurator;
import org.snmp4j.smi.Counter32;
import org.snmp4j.smi.Gauge32;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UnsignedInteger32;
import org.snmp4j.smi.Variable;

/**
 * Tests for the class <code>org.fosstrak.reader.mgmt.agent.snmp.table.EpcgSourceTableRow</code>.
 */
public class EpcgSourceTableRowTest extends TestCase {


	private SnmpTableRow row;

	private Source source;

	private ReaderDevice readerDevice;

	/**
	 * Sets up the test.
	 * @exception Exception An error occurred
	 */
	protected final void setUp() throws Exception {
		super.setUp();

		DOMConfigurator.configure("./target/classes/props/log4j.xml");

		if (SnmpAgent.getInstance() == null) {
			MessageLayer.main(new String[] { });
		}

		readerDevice = ReaderDevice.getInstance();

		source = Source.create("EpcgSourceTableRowTestSource", readerDevice);

		row = SnmpTableRow.getSnmpTableRow(new RowObjectContainer(TableTypeEnum.EPCG_SOURCE_TABLE, new Object[] { source }));
	}

	/**
	 * Does the cleanup.
	 * @exception Exception An error occurred
	 */
	protected final void tearDown() throws Exception {
		super.tearDown();

		readerDevice.removeSources(new Source[] { source });
	}

	/**
	 * Tests the <code>getValue()</code> method.
	 */
	public final void testGetValue() {
		Variable value;

		value = row.getValue(EpcglobalReaderMib.idxEpcgSrcName);
		assertEquals(new OctetString(source.getName()), value);

		value = row.getValue(EpcglobalReaderMib.idxEpcgSrcReadCyclesPerTrigger);
		assertEquals(new UnsignedInteger32(source.getReadCyclesPerTrigger()), value);

		value = row.getValue(EpcglobalReaderMib.idxEpcgSrcReadDutyCycle);
		assertEquals(new UnsignedInteger32(source.getMaxReadDutyCycles()), value);

		value = row.getValue(EpcglobalReaderMib.idxEpcgSrcReadTimeout);
		assertEquals(new UnsignedInteger32(source.getReadTimeout()), value);

		value = row.getValue(EpcglobalReaderMib.idxEpcgSrcGlimpsedTimeout);
		assertEquals(new UnsignedInteger32(source.getGlimpsedTimeout()), value);

		value = row.getValue(EpcglobalReaderMib.idxEpcgSrcObservedThreshold);
		assertEquals(new UnsignedInteger32(source.getObservedThreshold()), value);

		value = row.getValue(EpcglobalReaderMib.idxEpcgSrcObservedTimeout);
		assertEquals(new UnsignedInteger32(source.getObservedTimeout()), value);

		value = row.getValue(EpcglobalReaderMib.idxEpcgSrcLostTimeout);
		assertEquals(new UnsignedInteger32(source.getLostTimeout()), value);

		value = row.getValue(EpcglobalReaderMib.idxEpcgSrcUnknownToGlimpsedTrans);
		assertEquals(new Gauge32(source.getUnknownToGlimpsedCount()), value);

		value = row.getValue(EpcglobalReaderMib.idxEpcgSrcGlimpsedToUnknownTrans);
		assertEquals(new Gauge32(source.getGlimpsedToUnknownCount()), value);

		value = row.getValue(EpcglobalReaderMib.idxEpcgSrcGlimpsedToObservedTrans);
		assertEquals(new Gauge32(source.getGlimpsedToObservedCount()), value);

		value = row.getValue(EpcglobalReaderMib.idxEpcgSrcObservedToLostTrans);
		assertEquals(new Gauge32(source.getObservedToLostCount()), value);

		value = row.getValue(EpcglobalReaderMib.idxEpcgSrcLostToGlimpsedTrans);
		assertEquals(new Gauge32(source.getLostToGlimpsedCount()), value);

		value = row.getValue(EpcglobalReaderMib.idxEpcgSrcLostToUnknownTrans);
		assertEquals(new Gauge32(source.getLostToUnknownCount()), value);

		value = row.getValue(EpcglobalReaderMib.idxEpcgSrcAdminStatus);
		assertEquals(new Integer32(source.getAdminStatus().toInt()), value);

		value = row.getValue(EpcglobalReaderMib.idxEpcgSrcOperStatus);
		assertEquals(new Integer32(source.getOperStatus().toInt()), value);

		value = row.getValue(EpcglobalReaderMib.idxEpcgSrcOperStatusNotifEnable);
		assertEquals(new Integer32(source.getOperStatusAlarmControl().getEnabled() ? 1 : 2), value);

		value = row.getValue(EpcglobalReaderMib.idxEpcgSrcOperStatusNotifFromState);
		assertEquals(SnmpUtil.operStateToBITS(source.getOperStatusAlarmControl().getTriggerFromState()), value);

		value = row.getValue(EpcglobalReaderMib.idxEpcgSrcOperStatusNotifToState);
		assertEquals(SnmpUtil.operStateToBITS(source.getOperStatusAlarmControl().getTriggerToState()), value);

		value = row.getValue(EpcglobalReaderMib.idxEpcgSrcOperStatusNotifyLevel);
		assertEquals(new Integer32(source.getOperStatusAlarmControl().getLevel().toInt()), value);

		value = row.getValue(EpcglobalReaderMib.idxEpcgSrcSupportsWriteOperations);
		try {
			assertEquals(new Integer32(source.supportsWriteOperations() ? 1 : 2), value);
		} catch (ReaderProtocolException rpe) {
			fail();
		}

		value = row.getValue(EpcglobalReaderMib.idxEpcgSrcOperStateSuppressInterval);
		assertEquals(new UnsignedInteger32(source.getOperStatusAlarmControl().getSuppressInterval()), value);

		value = row.getValue(EpcglobalReaderMib.idxEpcgSrcOperStateSuppressions);
		assertEquals(new Counter32(source.getOperStateSuppressions()), value);
	}

	/**
	 * Tests the <code>setValue()</code> method.
	 */
	public final void testSetValue() {
		int value;
		AlarmLevel level;
		AdministrativeStatus adminStatus;
		OperationalStatus operStatus;

		value = 1;
		row.setValue(EpcglobalReaderMib.idxEpcgSrcReadCyclesPerTrigger, new UnsignedInteger32(value));
		assertEquals(value, source.getReadCyclesPerTrigger());
		value = 2;
		row.setValue(EpcglobalReaderMib.idxEpcgSrcReadCyclesPerTrigger, new UnsignedInteger32(value));
		assertEquals(value, source.getReadCyclesPerTrigger());

		value = 10;
		row.setValue(EpcglobalReaderMib.idxEpcgSrcReadDutyCycle, new Gauge32(value));
		assertEquals(value, source.getMaxReadDutyCycles());
		value = 20;
		row.setValue(EpcglobalReaderMib.idxEpcgSrcReadDutyCycle, new Gauge32(value));
		assertEquals(value, source.getMaxReadDutyCycles());

		value = 10000;
		row.setValue(EpcglobalReaderMib.idxEpcgSrcReadTimeout, new UnsignedInteger32(value));
		assertEquals(value, source.getReadTimeout());
		value = 20000;
		row.setValue(EpcglobalReaderMib.idxEpcgSrcReadTimeout, new UnsignedInteger32(value));
		assertEquals(value, source.getReadTimeout());

		value = 10000;
		row.setValue(EpcglobalReaderMib.idxEpcgSrcGlimpsedTimeout, new UnsignedInteger32(value));
		assertEquals(value, source.getGlimpsedTimeout());
		value = 20000;
		row.setValue(EpcglobalReaderMib.idxEpcgSrcGlimpsedTimeout, new UnsignedInteger32(value));
		assertEquals(value, source.getGlimpsedTimeout());

		value = 1000;
		row.setValue(EpcglobalReaderMib.idxEpcgSrcObservedThreshold, new UnsignedInteger32(value));
		assertEquals(value, source.getObservedThreshold());
		value = 2000;
		row.setValue(EpcglobalReaderMib.idxEpcgSrcObservedThreshold, new UnsignedInteger32(value));
		assertEquals(value, source.getObservedThreshold());

		value = 10000;
		row.setValue(EpcglobalReaderMib.idxEpcgSrcObservedTimeout, new UnsignedInteger32(value));
		assertEquals(value, source.getObservedTimeout());
		value = 20000;
		row.setValue(EpcglobalReaderMib.idxEpcgSrcObservedTimeout, new UnsignedInteger32(value));
		assertEquals(value, source.getObservedTimeout());

		value = 10000;
		row.setValue(EpcglobalReaderMib.idxEpcgSrcLostTimeout, new UnsignedInteger32(value));
		assertEquals(value, source.getLostTimeout());
		value = 20000;
		row.setValue(EpcglobalReaderMib.idxEpcgSrcLostTimeout, new UnsignedInteger32(value));
		assertEquals(value, source.getLostTimeout());

		adminStatus = AdministrativeStatus.UP;
		row.setValue(EpcglobalReaderMib.idxEpcgSrcAdminStatus, new Integer32(adminStatus.toInt()));
		assertEquals(adminStatus, source.getAdminStatus());
		adminStatus = AdministrativeStatus.DOWN;
		row.setValue(EpcglobalReaderMib.idxEpcgSrcAdminStatus, new Integer32(adminStatus.toInt()));
		assertEquals(adminStatus, source.getAdminStatus());

		value = 1;
		row.setValue(EpcglobalReaderMib.idxEpcgSrcOperStatusNotifEnable, new Integer32(value));
		assertEquals(value, source.getOperStatusAlarmControl().getEnabled() ? 1 : 2);
		value = 2;
		row.setValue(EpcglobalReaderMib.idxEpcgSrcOperStatusNotifEnable, new Integer32(value));
		assertEquals(value, source.getOperStatusAlarmControl().getEnabled() ? 1 : 2);

		operStatus = OperationalStatus.UP;
		row.setValue(EpcglobalReaderMib.idxEpcgSrcOperStatusNotifFromState, SnmpUtil.operStateToBITS(operStatus));
		assertEquals(operStatus, source.getOperStatusAlarmControl().getTriggerFromState());
		operStatus = OperationalStatus.DOWN;
		row.setValue(EpcglobalReaderMib.idxEpcgSrcOperStatusNotifFromState, SnmpUtil.operStateToBITS(operStatus));
		assertEquals(operStatus, source.getOperStatusAlarmControl().getTriggerFromState());

		operStatus = OperationalStatus.UP;
		row.setValue(EpcglobalReaderMib.idxEpcgSrcOperStatusNotifToState, SnmpUtil.operStateToBITS(operStatus));
		assertEquals(operStatus, source.getOperStatusAlarmControl().getTriggerToState());
		operStatus = OperationalStatus.DOWN;
		row.setValue(EpcglobalReaderMib.idxEpcgSrcOperStatusNotifToState, SnmpUtil.operStateToBITS(operStatus));
		assertEquals(operStatus, source.getOperStatusAlarmControl().getTriggerToState());

		level = AlarmLevel.CRITICAL;
		row.setValue(EpcglobalReaderMib.idxEpcgSrcOperStatusNotifyLevel, new Integer32(level.toInt()));
		assertEquals(level, source.getOperStatusAlarmControl().getLevel());
		level = AlarmLevel.DEBUG;
		row.setValue(EpcglobalReaderMib.idxEpcgSrcOperStatusNotifyLevel, new Integer32(level.toInt()));
		assertEquals(level, source.getOperStatusAlarmControl().getLevel());

		value = 1;
		row.setValue(EpcglobalReaderMib.idxEpcgSrcOperStateSuppressInterval, new UnsignedInteger32(value));
		assertEquals(value, source.getOperStatusAlarmControl().getSuppressInterval());
		value = 100;
		row.setValue(EpcglobalReaderMib.idxEpcgSrcOperStateSuppressInterval, new UnsignedInteger32(value));
		assertEquals(value, source.getOperStatusAlarmControl().getSuppressInterval());
	}

	/**
	 * Runs the test using the gui runner.
	 * @param args No arguments
	 */
	public static void main(String[] args) {
        junit.swingui.TestRunner.run(EpcgSourceTableRowTest.class);
    }

}
