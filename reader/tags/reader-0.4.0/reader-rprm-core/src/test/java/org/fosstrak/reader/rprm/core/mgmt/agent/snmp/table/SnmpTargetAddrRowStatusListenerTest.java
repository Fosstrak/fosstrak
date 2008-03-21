package org.accada.reader.rprm.core.mgmt.agent.snmp.table ;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Iterator;

import junit.framework.TestCase;

import org.accada.reader.rprm.core.ReaderDevice;
import org.accada.reader.rprm.core.ReaderProtocolException;
import org.accada.reader.rprm.core.mgmt.agent.snmp.SnmpAgent;
import org.accada.reader.rprm.core.mgmt.agent.snmp.table.SnmpTargetAddrRowStatusListener;
import org.accada.reader.rprm.core.mgmt.alarm.AlarmChannel;
import org.accada.reader.rprm.core.msg.Address;
import org.accada.reader.rprm.core.msg.MessageLayer;
import org.apache.log4j.xml.DOMConfigurator;
import org.snmp4j.agent.mo.DefaultMOTable;
import org.snmp4j.agent.mo.MOTableRow;
import org.snmp4j.agent.mo.snmp.RowStatus;
import org.snmp4j.agent.mo.snmp.RowStatusEvent;
import org.snmp4j.agent.mo.snmp.SnmpTargetMIB;

/**
 * Tests for the class <code>org.accada.reader.mgmt.agent.snmp.table.SnmpTargetAddrRowStatusListener</code>.
 */
public class SnmpTargetAddrRowStatusListenerTest extends TestCase {

	private SnmpTargetAddrRowStatusListener rowStatusListener;
	private ReaderDevice readerDevice;
	private SnmpAgent snmpAgent;

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

		snmpAgent = SnmpAgent.getInstance();

		rowStatusListener = new SnmpTargetAddrRowStatusListener(readerDevice);
	}

	/**
	 * Does the cleanup.
	 * @exception Exception An error occurred
	 */
	protected final void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Tests the <code>rowStatusChanged()</code> method.
	 */
	public final void testRowStatusChanged() {
		AlarmChannel alarmChan = null;
		try {
			alarmChan = AlarmChannel.create("SnmpTargetAddrRowStatusListenerTestAlarmChan", new Address("udp://127.0.0.1:162"), readerDevice);
		} catch (ReaderProtocolException rpe) {
			fail();
		} catch (MalformedURLException mue) {
			fail();
		}
		SnmpTargetMIB targetMib = snmpAgent.getSnmpTargetMIB();
		DefaultMOTable table = targetMib.getSnmpTargetAddrEntry();
		MOTableRow row = null;
		Iterator iter = table.getModel().iterator();
		while (iter.hasNext()) {
			MOTableRow curRow = (MOTableRow) iter.next();
			try {
				if ((new String(curRow.getIndex().toByteArray(), "UTF8")).equals(alarmChan.getName())) {
					row = curRow;
					break;
				}
			} catch (UnsupportedEncodingException uee) {
				fail();
			}
		}
		assertNotNull(row);

		RowStatusEvent event;

		try {
			readerDevice.getAlarmChannel(alarmChan.getName());
		} catch (ReaderProtocolException rpe) {
			fail();
		}
		event = new RowStatusEvent(row, null, row, null, RowStatus.active, RowStatus.destroy);
		rowStatusListener.rowStatusChanged(event);
		try {
			readerDevice.getAlarmChannel(alarmChan.getName());
			fail();
		} catch (ReaderProtocolException rpe) {
			// ok
		}

		event = new RowStatusEvent(row, null, row, null, RowStatus.destroy, RowStatus.active);
		rowStatusListener.rowStatusChanged(event);
		try {
			readerDevice.getAlarmChannel(alarmChan.getName());
		} catch (ReaderProtocolException rpe) {
			fail();
		}

		event = new RowStatusEvent(row, null, row, null, RowStatus.active, RowStatus.destroy);
		rowStatusListener.rowStatusChanged(event);
		try {
			readerDevice.getAlarmChannel(alarmChan.getName());
			fail();
		} catch (ReaderProtocolException rpe) {
			// ok
		}

		event = new RowStatusEvent(row, null, row, null, RowStatus.destroy, RowStatus.createAndGo);
		rowStatusListener.rowStatusChanged(event);
		try {
			readerDevice.getAlarmChannel(alarmChan.getName());
		} catch (ReaderProtocolException rpe) {
			fail();
		}

		event = new RowStatusEvent(row, null, row, null, RowStatus.active, RowStatus.destroy);
		rowStatusListener.rowStatusChanged(event);
		try {
			readerDevice.getAlarmChannel(alarmChan.getName());
			fail();
		} catch (ReaderProtocolException rpe) {
			// ok
		}
	}

	/**
	 * Runs the test using the gui runner.
	 * @param args No arguments
	 */
	public static void main(String[] args) {
        junit.swingui.TestRunner.run(SnmpTargetAddrRowStatusListenerTest.class);
    }

}
