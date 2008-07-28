package org.fosstrak.reader.rprm.core.mgmt.agent.snmp.table ;

import junit.framework.TestCase;

import org.fosstrak.reader.rprm.core.AntennaReadPoint;
import org.fosstrak.reader.rprm.core.NotificationChannel;
import org.fosstrak.reader.rprm.core.ReaderDevice;
import org.fosstrak.reader.rprm.core.Source;
import org.fosstrak.reader.rprm.core.Trigger;
import org.fosstrak.reader.rprm.core.TriggerType;
import org.fosstrak.reader.rprm.core.mgmt.IOPort;
import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.SnmpAgent;
import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.mib.EpcglobalReaderMib;
import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.table.EpcgAntReadPointTableRow;
import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.table.EpcgGlobalCountersTableRow;
import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.table.EpcgIoPortTableRow;
import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.table.EpcgNotifChanSrcTableRow;
import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.table.EpcgNotifTrigTableRow;
import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.table.EpcgNotificationChannelTableRow;
import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.table.EpcgRdPntSrcTableRow;
import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.table.EpcgReadPointTableRow;
import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.table.EpcgReadTrigTableRow;
import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.table.EpcgReaderServerTableRow;
import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.table.EpcgSourceTableRow;
import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.table.EpcgTriggerTableRow;
import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.table.RowObjectContainer;
import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.table.SnmpTable;
import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.table.SnmpTableRow;
import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.table.EpcgGlobalCountersTableRow.CounterType;
import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.table.EpcgReaderServerTableRow.ServerTypeEnum;
import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.table.SnmpTable.TableTypeEnum;
import org.fosstrak.reader.rprm.core.mgmt.util.SnmpUtil;
import org.fosstrak.reader.rprm.core.msg.MessageLayer;
import org.apache.log4j.xml.DOMConfigurator;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;

/**
 * Tests for the class <code>org.fosstrak.reader.mgmt.agent.snmp.table.SnmpTableRow</code>.
 */
public class SnmpTableRowTest extends TestCase {

	private ReaderDevice readerDevice;

	private Trigger trigger;
	private NotificationChannel notifChan;
	private Source source;
	private AntennaReadPoint antReadPoint;
	private IOPort ioPort;

	private int triggerIndex;
	private int notifChanIndex;
	private int sourceIndex;
	private int antReadPointIndex;

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

		notifChan = NotificationChannel.create("SnmpTableRowTestNotifChan", "addr", readerDevice);
		trigger = Trigger.create("SnmpTableRowTestTrigger", TriggerType.CONTINUOUS, "", readerDevice);
		source = Source.create("SnmpTableRowTestSource", readerDevice);
		antReadPoint = AntennaReadPoint.create("SnmpTableRowTestAntReadPoint", readerDevice, null);
		ioPort = new IOPort("SnmpTableRowTestIOPort");

		triggerIndex = Integer.parseInt(((SnmpTable) SnmpUtil.getSnmpTable(TableTypeEnum.EPCG_TRIGGER_TABLE)).getTableRowIndexByValue(new OctetString(trigger.getName()), EpcglobalReaderMib.idxEpcgTrigName).toString());
		notifChanIndex = Integer.parseInt(((SnmpTable) SnmpUtil.getSnmpTable(TableTypeEnum.EPCG_NOTIFICATION_CHANNEL_TABLE)).getTableRowIndexByValue(new OctetString(notifChan.getName()), EpcglobalReaderMib.idxEpcgNotifChanName).toString());
		sourceIndex = Integer.parseInt(((SnmpTable) SnmpUtil.getSnmpTable(TableTypeEnum.EPCG_SOURCE_TABLE)).getTableRowIndexByValue(new OctetString(source.getName()), EpcglobalReaderMib.idxEpcgSrcName).toString());
		antReadPointIndex = Integer.parseInt(((SnmpTable) SnmpUtil.getSnmpTable(TableTypeEnum.EPCG_READ_POINT_TABLE)).getTableRowIndexByValue(new OctetString(antReadPoint.getName()), EpcglobalReaderMib.idxEpcgReadPointName).toString());
	}

	/**
	 * Does the cleanup.
	 * @exception Exception An error occurred
	 */
	protected final void tearDown() throws Exception {
		super.tearDown();
		readerDevice.removeNotificationChannels(new NotificationChannel[] { notifChan });
		readerDevice.removeTriggers(new Trigger[] { trigger });
		readerDevice.removeSources(new Source[] { source });
		readerDevice.getReadPoints().remove(antReadPoint.getName());
	}

	/**
	 * Tests the <code>getSnmpTableRow()</code> method.
	 */
	public final void testGetSnmpTableRow() {
		SnmpTableRow row;

		// getSnmpTableRow(RowObjectContainer)
		row = SnmpTableRow.getSnmpTableRow(new RowObjectContainer(TableTypeEnum.EPCG_GLOBAL_COUNTERS_TABLE, new Object[] { CounterType.ANTENNA_TAGS_IDENTIFIED }));
		assertTrue(row instanceof EpcgGlobalCountersTableRow);
		row = SnmpTableRow.getSnmpTableRow(new RowObjectContainer(TableTypeEnum.EPCG_READER_SERVER_TABLE, new Object[] { ServerTypeEnum.DHCP, "127.0.0.1/67" }));
		assertTrue(row instanceof EpcgReaderServerTableRow);
		row = SnmpTableRow.getSnmpTableRow(new RowObjectContainer(TableTypeEnum.EPCG_READ_POINT_TABLE, new Object[] { antReadPoint }));
		assertTrue(row instanceof EpcgReadPointTableRow);
		row = SnmpTableRow.getSnmpTableRow(new RowObjectContainer(TableTypeEnum.EPCG_ANT_READ_POINT_TABLE, new Object[] { antReadPoint }));
		assertTrue(row instanceof EpcgAntReadPointTableRow);
		row = SnmpTableRow.getSnmpTableRow(new RowObjectContainer(TableTypeEnum.EPCG_IO_PORT_TABLE, new Object[] { ioPort }));
		assertTrue(row instanceof EpcgIoPortTableRow);
		row = SnmpTableRow.getSnmpTableRow(new RowObjectContainer(TableTypeEnum.EPCG_SOURCE_TABLE, new Object[] { source }));
		assertTrue(row instanceof EpcgSourceTableRow);
		row = SnmpTableRow.getSnmpTableRow(new RowObjectContainer(TableTypeEnum.EPCG_NOTIFICATION_CHANNEL_TABLE, new Object[] { notifChan }));
		assertTrue(row instanceof EpcgNotificationChannelTableRow);
		row = SnmpTableRow.getSnmpTableRow(new RowObjectContainer(TableTypeEnum.EPCG_TRIGGER_TABLE, new Object[] { trigger }));
		assertTrue(row instanceof EpcgTriggerTableRow);
		row = SnmpTableRow.getSnmpTableRow(new RowObjectContainer(TableTypeEnum.EPCG_NOTIF_TRIG_TABLE, new Object[] { notifChan, trigger }));
		assertTrue(row instanceof EpcgNotifTrigTableRow);
		assertEquals(new OID(notifChanIndex + "." + triggerIndex), row.getIndex());
		row = SnmpTableRow.getSnmpTableRow(new RowObjectContainer(TableTypeEnum.EPCG_READ_TRIG_TABLE, new Object[] { source, trigger }));
		assertTrue(row instanceof EpcgReadTrigTableRow);
		assertEquals(new OID(sourceIndex + "." + triggerIndex), row.getIndex());
		row = SnmpTableRow.getSnmpTableRow(new RowObjectContainer(TableTypeEnum.EPCG_RD_PNT_SRC_TABLE, new Object[] { source, antReadPoint }));
		assertTrue(row instanceof EpcgRdPntSrcTableRow);
		assertEquals(new OID(antReadPointIndex + "." + sourceIndex), row.getIndex());
		row = SnmpTableRow.getSnmpTableRow(new RowObjectContainer(TableTypeEnum.EPCG_NOTIF_CHAN_SRC_TABLE, new Object[] { notifChan, source }));
		assertTrue(row instanceof EpcgNotifChanSrcTableRow);
		assertEquals(new OID(notifChanIndex + "." + sourceIndex), row.getIndex());

		// getSnmpTableRow(TableTypeEnum, OID, Variable[])
		row = SnmpTableRow.getSnmpTableRow(TableTypeEnum.EPCG_READER_SERVER_TABLE, new OID("1.1"), null);
		assertTrue(row instanceof EpcgReaderServerTableRow);
		row = SnmpTableRow.getSnmpTableRow(TableTypeEnum.EPCG_NOTIF_TRIG_TABLE, new OID(notifChanIndex + "." + triggerIndex), null);
		assertTrue(row instanceof EpcgNotifTrigTableRow);
		row = SnmpTableRow.getSnmpTableRow(TableTypeEnum.EPCG_READ_TRIG_TABLE, new OID(sourceIndex + "." + triggerIndex), null);
		assertTrue(row instanceof EpcgReadTrigTableRow);
		row = SnmpTableRow.getSnmpTableRow(TableTypeEnum.EPCG_RD_PNT_SRC_TABLE, new OID(antReadPointIndex + "." + sourceIndex), null);
		assertTrue(row instanceof EpcgRdPntSrcTableRow);
		row = SnmpTableRow.getSnmpTableRow(TableTypeEnum.EPCG_NOTIF_CHAN_SRC_TABLE, new OID(notifChanIndex + "." + sourceIndex), null);
		assertTrue(row instanceof EpcgNotifChanSrcTableRow);
	}

	/**
	 * Runs the test using the gui runner.
	 * @param args No arguments
	 */
	public static void main(String[] args) {
        junit.swingui.TestRunner.run(SnmpTableRowTest.class);
    }

}
