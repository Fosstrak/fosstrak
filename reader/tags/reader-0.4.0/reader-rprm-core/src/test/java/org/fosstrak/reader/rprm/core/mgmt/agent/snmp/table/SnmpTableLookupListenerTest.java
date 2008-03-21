package org.accada.reader.rprm.core.mgmt.agent.snmp.table ;

import java.util.Iterator;
import java.util.Vector;

import junit.framework.TestCase;

import org.accada.reader.rprm.core.NotificationChannel;
import org.accada.reader.rprm.core.ReaderDevice;
import org.accada.reader.rprm.core.ReaderProtocolException;
import org.accada.reader.rprm.core.mgmt.agent.snmp.SnmpAgent;
import org.accada.reader.rprm.core.mgmt.agent.snmp.table.SnmpTable;
import org.accada.reader.rprm.core.mgmt.agent.snmp.table.SnmpTableLookupListener;
import org.accada.reader.rprm.core.mgmt.agent.snmp.table.SnmpTableRow;
import org.accada.reader.rprm.core.mgmt.agent.snmp.table.TableCreator;
import org.accada.reader.rprm.core.mgmt.agent.snmp.table.SnmpTable.TableTypeEnum;
import org.accada.reader.rprm.core.msg.MessageLayer;
import org.apache.log4j.xml.DOMConfigurator;
import org.snmp4j.agent.DefaultMOServer;
import org.snmp4j.agent.mo.MOTableModel;

/**
 * Tests for the class <code>org.accada.reader.mgmt.agent.snmp.table.SnmpTableLookupListener</code>.
 */
public class SnmpTableLookupListenerTest extends TestCase {

	/**
	 * SnmpTable instance.
	 */
	private SnmpTable snmpTable;

	/**
	 * The lookup listener.
	 */
	private SnmpTableLookupListener lookupListener;

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

		readerDevice.removeAllNotificationChannels();

		snmpTable = (new TableCreator(new DefaultMOServer(), readerDevice)).createTable(TableTypeEnum.EPCG_NOTIFICATION_CHANNEL_TABLE);
		lookupListener = new SnmpTableLookupListener(snmpTable);
	}

	/**
	 * Does the cleanup.
	 * @exception Exception An error occurred
	 */
	protected final void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Tests the <code>lookupEvent()</code> method.
	 */
	public final void testLookupEvent() {
		MOTableModel model = snmpTable.getModel();

		String chan1Name = "notifChan1";
		String chan2Name = "notifChan2";
		String chan3Name = "notifChan3";
		String chan4Name = "notifChan4";

		NotificationChannel chan1 = null, chan2 = null, chan3 = null, chan4 = null;
		try {
			chan1 = NotificationChannel.create(chan1Name, "NotifChan1Addr", readerDevice);
			chan2 = NotificationChannel.create(chan2Name, "NotifChan2Addr", readerDevice);
			chan3 = NotificationChannel.create(chan3Name, "NotifChan3Addr", readerDevice);
			chan4 = NotificationChannel.create(chan4Name, "NotifChan4Addr", readerDevice);
		} catch (ReaderProtocolException rpe) {
			fail("Should not throw any exceptions.");
		}

		lookupListener.lookupEvent(null);

		Vector<NotificationChannel> notifChans = new Vector<NotificationChannel>();
		Iterator iter = model.iterator();
		while (iter.hasNext()) {
			SnmpTableRow curRow = (SnmpTableRow) iter.next();
			notifChans.add((NotificationChannel)curRow.getRowObjectContainer().getRowObjects()[0]);
		}
		assertTrue(notifChans.contains(chan1));
		assertTrue(notifChans.contains(chan2));
		assertTrue(notifChans.contains(chan3));
		assertTrue(notifChans.contains(chan4));
		assertEquals(4, notifChans.size());

		readerDevice.removeAllNotificationChannels();
	}

	/**
	 * Runs the test using the gui runner.
	 * @param args No arguments
	 */
	public static void main(String[] args) {
        junit.swingui.TestRunner.run(SnmpTableLookupListenerTest.class);
    }

}
