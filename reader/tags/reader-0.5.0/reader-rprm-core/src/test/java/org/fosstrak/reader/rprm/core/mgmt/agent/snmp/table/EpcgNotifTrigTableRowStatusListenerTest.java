package org.fosstrak.reader.rprm.core.mgmt.agent.snmp.table ;

import junit.framework.TestCase;

import org.fosstrak.reader.rprm.core.NotificationChannel;
import org.fosstrak.reader.rprm.core.ReaderDevice;
import org.fosstrak.reader.rprm.core.ReaderProtocolException;
import org.fosstrak.reader.rprm.core.Trigger;
import org.fosstrak.reader.rprm.core.TriggerType;
import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.SnmpAgent;
import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.table.EpcgNotifTrigTableRowStatusListener;
import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.table.RowObjectContainer;
import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.table.SnmpTableRow;
import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.table.SnmpTable.TableTypeEnum;
import org.fosstrak.reader.rprm.core.msg.MessageLayer;
import org.apache.log4j.xml.DOMConfigurator;
import org.snmp4j.agent.mo.snmp.RowStatus;
import org.snmp4j.agent.mo.snmp.RowStatusEvent;

/**
 * Tests for the class <code>org.fosstrak.reader.mgmt.agent.snmp.table.EpcgNotifTrigTableRowStatusListener</code>.
 */
public class EpcgNotifTrigTableRowStatusListenerTest extends TestCase {

	private EpcgNotifTrigTableRowStatusListener rowStatusListener;
	private ReaderDevice readerDevice;

	private NotificationChannel notifChan;
	private Trigger trigger;

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

		rowStatusListener = new EpcgNotifTrigTableRowStatusListener();

		notifChan = NotificationChannel.create("EpcgNotifTrigTableRowStatusListenerTestNotifChan", "addr", readerDevice);
		trigger = Trigger.create("EpcgNotifTrigTableRowStatusListenerTestTrigger", TriggerType.TIMER, "ms=2500", readerDevice);
	}

	/**
	 * Does the cleanup.
	 * @exception Exception An error occurred
	 */
	protected final void tearDown() throws Exception {
		super.tearDown();

		readerDevice.removeNotificationChannels(new NotificationChannel[] { notifChan });
		readerDevice.removeTriggers(new Trigger[] { trigger });
	}

	/**
	 * Tests the <code>rowStatusChanged()</code> method.
	 */
	public final void testRowStatusChanged() {
		SnmpTableRow row = SnmpTableRow.getSnmpTableRow(new RowObjectContainer(TableTypeEnum.EPCG_NOTIF_TRIG_TABLE, new Object[] { notifChan, trigger }));
		RowStatusEvent event;

		event = new RowStatusEvent(row, null, row, null, RowStatus.destroy, RowStatus.createAndGo);
		rowStatusListener.rowStatusChanged(event);
		try {
			notifChan.getNotificationTrigger(trigger.getName());
		} catch (ReaderProtocolException rpe) {
			fail();
		}

		event = new RowStatusEvent(row, null, row, null, RowStatus.active, RowStatus.notInService);
		rowStatusListener.rowStatusChanged(event);
		try {
			notifChan.getNotificationTrigger(trigger.getName());
			fail();
		} catch (ReaderProtocolException rpe) {
			// ok
		}

		event = new RowStatusEvent(row, null, row, null, RowStatus.notInService, RowStatus.active);
		rowStatusListener.rowStatusChanged(event);
		try {
			notifChan.getNotificationTrigger(trigger.getName());
		} catch (ReaderProtocolException rpe) {
			fail();
		}

		event = new RowStatusEvent(row, null, row, null, RowStatus.active, RowStatus.destroy);
		rowStatusListener.rowStatusChanged(event);
		try {
			notifChan.getNotificationTrigger(trigger.getName());
			fail();
		} catch (ReaderProtocolException rpe) {
			// ok
		}

		event = new RowStatusEvent(row, null, row, null, RowStatus.destroy, RowStatus.createAndWait);
		rowStatusListener.rowStatusChanged(event);
		try {
			notifChan.getNotificationTrigger(trigger.getName());
			fail();
		} catch (ReaderProtocolException rpe) {
			// ok
		}

		event = new RowStatusEvent(row, null, row, null, RowStatus.notInService, RowStatus.active);
		rowStatusListener.rowStatusChanged(event);
		try {
			notifChan.getNotificationTrigger(trigger.getName());
		} catch (ReaderProtocolException rpe) {
			fail();
		}

		event = new RowStatusEvent(row, null, row, null, RowStatus.active, RowStatus.destroy);
		rowStatusListener.rowStatusChanged(event);
		try {
			notifChan.getNotificationTrigger(trigger.getName());
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
        junit.swingui.TestRunner.run(EpcgNotifTrigTableRowStatusListenerTest.class);
    }

}
