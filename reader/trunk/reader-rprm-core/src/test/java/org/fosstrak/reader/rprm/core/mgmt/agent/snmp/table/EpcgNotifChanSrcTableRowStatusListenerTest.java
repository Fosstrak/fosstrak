package org.accada.reader.rprm.core.mgmt.agent.snmp.table ;

import junit.framework.TestCase;

import org.accada.reader.rprm.core.NotificationChannel;
import org.accada.reader.rprm.core.ReaderDevice;
import org.accada.reader.rprm.core.Source;
import org.accada.reader.rprm.core.mgmt.agent.snmp.SnmpAgent;
import org.accada.reader.rprm.core.mgmt.agent.snmp.table.EpcgNotifChanSrcTableRowStatusListener;
import org.accada.reader.rprm.core.mgmt.agent.snmp.table.RowObjectContainer;
import org.accada.reader.rprm.core.mgmt.agent.snmp.table.SnmpTableRow;
import org.accada.reader.rprm.core.mgmt.agent.snmp.table.SnmpTable.TableTypeEnum;
import org.accada.reader.rprm.core.msg.MessageLayer;
import org.apache.log4j.xml.DOMConfigurator;
import org.snmp4j.agent.mo.snmp.RowStatus;
import org.snmp4j.agent.mo.snmp.RowStatusEvent;

/**
 * Tests for the class <code>org.accada.reader.mgmt.agent.snmp.table.EpcgNotifChanSrcTableRowStatusListener</code>.
 */
public class EpcgNotifChanSrcTableRowStatusListenerTest extends TestCase {

	private EpcgNotifChanSrcTableRowStatusListener rowStatusListener;
	private ReaderDevice readerDevice;

	private NotificationChannel notifChan;
	private Source source;

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

		rowStatusListener = new EpcgNotifChanSrcTableRowStatusListener();

		notifChan = NotificationChannel.create("EpcgNotifChanSrcTableRowStatusListenerTestNotifChan", "addr", readerDevice);
		source = Source.create("EpcgNotifChanSrcTableRowStatusListenerTestSource", readerDevice);
	}

	/**
	 * Does the cleanup.
	 * @exception Exception An error occurred
	 */
	protected final void tearDown() throws Exception {
		super.tearDown();

		readerDevice.removeNotificationChannels(new NotificationChannel[] { notifChan });
		readerDevice.removeSources(new Source[] { source });
	}

	/**
	 * Tests the <code>rowStatusChanged()</code> method.
	 */
	public final void testRowStatusChanged() {
		SnmpTableRow row = SnmpTableRow.getSnmpTableRow(new RowObjectContainer(TableTypeEnum.EPCG_NOTIF_CHAN_SRC_TABLE, new Object[] { notifChan, source }));
		RowStatusEvent event;

		event = new RowStatusEvent(row, null, row, null, RowStatus.destroy, RowStatus.createAndGo);
		rowStatusListener.rowStatusChanged(event);
		assertNotNull(source.getAllNotificationChannels().get(notifChan.getName()));

		event = new RowStatusEvent(row, null, row, null, RowStatus.active, RowStatus.notInService);
		rowStatusListener.rowStatusChanged(event);
		assertNull(source.getAllNotificationChannels().get(notifChan.getName()));

		event = new RowStatusEvent(row, null, row, null, RowStatus.notInService, RowStatus.active);
		rowStatusListener.rowStatusChanged(event);
		assertNotNull(source.getAllNotificationChannels().get(notifChan.getName()));

		event = new RowStatusEvent(row, null, row, null, RowStatus.active, RowStatus.destroy);
		rowStatusListener.rowStatusChanged(event);
		assertNull(source.getAllNotificationChannels().get(notifChan.getName()));

		event = new RowStatusEvent(row, null, row, null, RowStatus.destroy, RowStatus.createAndWait);
		rowStatusListener.rowStatusChanged(event);
		assertNull(source.getAllNotificationChannels().get(notifChan.getName()));

		event = new RowStatusEvent(row, null, row, null, RowStatus.notInService, RowStatus.active);
		rowStatusListener.rowStatusChanged(event);
		assertNotNull(source.getAllNotificationChannels().get(notifChan.getName()));

		event = new RowStatusEvent(row, null, row, null, RowStatus.active, RowStatus.destroy);
		rowStatusListener.rowStatusChanged(event);
		assertNull(source.getAllNotificationChannels().get(notifChan.getName()));
	}

	/**
	 * Runs the test using the gui runner.
	 * @param args No arguments
	 */
	public static void main(String[] args) {
        junit.swingui.TestRunner.run(EpcgNotifChanSrcTableRowStatusListenerTest.class);
    }

}
