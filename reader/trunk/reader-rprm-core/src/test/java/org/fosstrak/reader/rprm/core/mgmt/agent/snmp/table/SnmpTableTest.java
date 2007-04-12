package org.accada.reader.rprm.core.mgmt.agent.snmp.table ;

import java.util.Iterator;
import java.util.Vector;

import junit.framework.TestCase;

import org.accada.reader.rprm.core.NotificationChannel;
import org.accada.reader.rprm.core.ReaderDevice;
import org.accada.reader.rprm.core.ReaderProtocolException;
import org.accada.reader.rprm.core.mgmt.agent.snmp.SnmpAgent;
import org.accada.reader.rprm.core.mgmt.agent.snmp.mib.EpcglobalReaderMib;
import org.accada.reader.rprm.core.mgmt.agent.snmp.table.RowObjectContainer;
import org.accada.reader.rprm.core.mgmt.agent.snmp.table.SnmpTable;
import org.accada.reader.rprm.core.mgmt.agent.snmp.table.SnmpTableRow;
import org.accada.reader.rprm.core.mgmt.agent.snmp.table.TableCreator;
import org.accada.reader.rprm.core.mgmt.agent.snmp.table.SnmpTable.TableTypeEnum;
import org.accada.reader.rprm.core.mgmt.util.SnmpUtil;
import org.accada.reader.rprm.core.msg.MessageLayer;
import org.apache.log4j.PropertyConfigurator;
import org.snmp4j.agent.DefaultMOContextScope;
import org.snmp4j.agent.DefaultMOServer;
import org.snmp4j.agent.mo.MOTableModel;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;

/**
 * Tests for the class <code>org.accada.reader.mgmt.agent.snmp.table.SnmpTable</code>.
 */
public class SnmpTableTest extends TestCase {
	
	/**
	 * SnmpTable instance.
	 */
	private SnmpTable snmpTable;
	
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
		
		readerDevice.removeAllNotificationChannels();
		
		snmpTable = (new TableCreator(new DefaultMOServer(), readerDevice)).createTable(TableTypeEnum.EPCG_NOTIFICATION_CHANNEL_TABLE);
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
		try {
			NotificationChannel notifChan = NotificationChannel.create("SnmpTableTestGetValueTestNotifChan", "addr", readerDevice);
			OID index = snmpTable.getTableRowIndexByValue(new OctetString(notifChan.getName()), EpcglobalReaderMib.idxEpcgNotifChanName);
			assertEquals(new OctetString(notifChan.getName()), snmpTable.getValue(new OID(snmpTable.getOID() + "." + EpcglobalReaderMib.colEpcgNotifChanName + "." + index)));
			assertEquals(new OctetString(notifChan.getName()), snmpTable.getValue(index, EpcglobalReaderMib.idxEpcgNotifChanName));
			readerDevice.removeAllNotificationChannels();
		} catch (ReaderProtocolException rpe) {
			fail();
		}
	}
	
	/**
	 * Tests the <code>getSortedIndices()</code> method.
	 */
	public final void testGetSortedIndices() {
		SnmpTable table = (SnmpTable) SnmpUtil.getSnmpTable(TableTypeEnum.EPCG_GLOBAL_COUNTERS_TABLE);
		Vector<OID> oids = table.getSortedIndices();
		for (int i = 0; i < oids.size() - 1; i++) {
			assertTrue(oids.elementAt(i).compareTo(oids.elementAt(i + 1)) < 0);
		}
	}
	
	/**
	 * Tests the <code>find()</code> method.
	 */
	public final void testFind() {
		OctetString context = SnmpAgent.getInstance().getServer().getContexts()[0];
		SnmpTable table = (SnmpTable) SnmpUtil.getSnmpTable(TableTypeEnum.EPCG_GLOBAL_COUNTERS_TABLE);
		OID oid = new OID(EpcglobalReaderMib.oidEpcgGlobalCountersEntry);
		OID foundOID = table.find(new DefaultMOContextScope(context, oid, true, oid.nextPeer(), false));
		oid.append("2.1");
		assertEquals(oid, foundOID);
	}
	
	/**
	 * Tests the <code>update()</code> method.
	 */
	public final void testUpdate() {
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
		
		snmpTable.update();
		
		Vector<NotificationChannel> notifChans = new Vector<NotificationChannel>();
		Iterator<SnmpTableRow> iter = model.iterator();
		while (iter.hasNext()) {
			SnmpTableRow curRow = iter.next();
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
	 * Tests the <code>updateStoredRows()</code> method.
	 */
	public final void testUpdateStoredRows() {
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
		
		Vector<RowObjectContainer> conts = new Vector<RowObjectContainer>();
		conts.add(new RowObjectContainer(TableTypeEnum.EPCG_NOTIFICATION_CHANNEL_TABLE, new Object[] {chan1}));
		conts.add(new RowObjectContainer(TableTypeEnum.EPCG_NOTIFICATION_CHANNEL_TABLE, new Object[] {chan2}));
		conts.add(new RowObjectContainer(TableTypeEnum.EPCG_NOTIFICATION_CHANNEL_TABLE, new Object[] {chan3}));
		
		snmpTable.updateStoredRows(conts);
		
		Iterator<SnmpTableRow> iter;
		SnmpTableRow curRow;
		
		iter = model.iterator();
		curRow = iter.next();
		assertEquals(1, Integer.parseInt(curRow.getIndex().toString()));
		assertEquals(chan1Name, ((NotificationChannel)curRow.getRowObjectContainer().getRowObjects()[0]).getName());
		curRow = iter.next();
		assertEquals(2, Integer.parseInt(curRow.getIndex().toString()));
		assertEquals(chan2Name, ((NotificationChannel)curRow.getRowObjectContainer().getRowObjects()[0]).getName());
		curRow = iter.next();
		assertEquals(3, Integer.parseInt(curRow.getIndex().toString()));
		assertEquals(chan3Name, ((NotificationChannel)curRow.getRowObjectContainer().getRowObjects()[0]).getName());
		
		
		conts.remove(new RowObjectContainer(TableTypeEnum.EPCG_NOTIFICATION_CHANNEL_TABLE, new Object[] {chan2}));
		snmpTable.updateStoredRows(conts); 
		
		iter = model.iterator();
		curRow = iter.next();
		assertEquals(1, Integer.parseInt(curRow.getIndex().toString()));
		assertEquals(chan1Name, ((NotificationChannel)curRow.getRowObjectContainer().getRowObjects()[0]).getName());
		curRow = iter.next();
		assertEquals(3, Integer.parseInt(curRow.getIndex().toString()));
		assertEquals(chan3Name, ((NotificationChannel)curRow.getRowObjectContainer().getRowObjects()[0]).getName());
		
		conts.add(new RowObjectContainer(TableTypeEnum.EPCG_NOTIFICATION_CHANNEL_TABLE, new Object[] {chan4}));
		snmpTable.updateStoredRows(conts);
		
		iter = model.iterator();
		curRow = iter.next();
		assertEquals(1, Integer.parseInt(curRow.getIndex().toString()));
		assertEquals(chan1Name, ((NotificationChannel)curRow.getRowObjectContainer().getRowObjects()[0]).getName());
		curRow = iter.next();
		assertEquals(2, Integer.parseInt(curRow.getIndex().toString()));
		assertEquals(chan4Name, ((NotificationChannel)curRow.getRowObjectContainer().getRowObjects()[0]).getName());
		curRow = iter.next();
		assertEquals(3, Integer.parseInt(curRow.getIndex().toString()));
		assertEquals(chan3Name, ((NotificationChannel)curRow.getRowObjectContainer().getRowObjects()[0]).getName());
		
		conts.add(new RowObjectContainer(TableTypeEnum.EPCG_NOTIFICATION_CHANNEL_TABLE, new Object[] {chan2}));
		snmpTable.updateStoredRows(conts);
		
		iter = model.iterator();
		curRow = iter.next();
		assertEquals(1, Integer.parseInt(curRow.getIndex().toString()));
		assertEquals(chan1Name, ((NotificationChannel)curRow.getRowObjectContainer().getRowObjects()[0]).getName());
		curRow = iter.next();
		assertEquals(2, Integer.parseInt(curRow.getIndex().toString()));
		assertEquals(chan4Name, ((NotificationChannel)curRow.getRowObjectContainer().getRowObjects()[0]).getName());
		curRow = iter.next();
		assertEquals(3, Integer.parseInt(curRow.getIndex().toString()));
		assertEquals(chan3Name, ((NotificationChannel)curRow.getRowObjectContainer().getRowObjects()[0]).getName());
		curRow = iter.next();
		assertEquals(4, Integer.parseInt(curRow.getIndex().toString()));
		assertEquals(chan2Name, ((NotificationChannel)curRow.getRowObjectContainer().getRowObjects()[0]).getName());
		
		readerDevice.removeAllNotificationChannels();
	}
	
	/**
	 * Tests the <code>getTableRowIndexByValue()</code> method.
	 */
	public final void testGetTableRowIndexByValue() {
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
		
		Vector<RowObjectContainer> conts = new Vector<RowObjectContainer>();
		conts.add(new RowObjectContainer(TableTypeEnum.EPCG_NOTIFICATION_CHANNEL_TABLE, new Object[] {chan1}));
		conts.add(new RowObjectContainer(TableTypeEnum.EPCG_NOTIFICATION_CHANNEL_TABLE, new Object[] {chan2}));
		conts.add(new RowObjectContainer(TableTypeEnum.EPCG_NOTIFICATION_CHANNEL_TABLE, new Object[] {chan3}));
		conts.add(new RowObjectContainer(TableTypeEnum.EPCG_NOTIFICATION_CHANNEL_TABLE, new Object[] {chan4}));
		
		snmpTable.updateStoredRows(conts);
		
		OID oid = snmpTable.getTableRowIndexByValue(new OctetString(chan3Name), EpcglobalReaderMib.idxEpcgNotifChanName);
		assertEquals(new OctetString(chan3Name), snmpTable.getValue(oid, EpcglobalReaderMib.idxEpcgNotifChanName));
		
		readerDevice.removeAllNotificationChannels();
	}
	
	/**
	 * Tests the <code>getRowObjContOfRow()</code> method.
	 */
	public final void testGetRowObjContOfRow() {
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
		
		Vector<RowObjectContainer> conts = new Vector<RowObjectContainer>();
		RowObjectContainer cont1 = new RowObjectContainer(TableTypeEnum.EPCG_NOTIFICATION_CHANNEL_TABLE, new Object[] {chan1});
		RowObjectContainer cont2 = new RowObjectContainer(TableTypeEnum.EPCG_NOTIFICATION_CHANNEL_TABLE, new Object[] {chan2});
		RowObjectContainer cont3 = new RowObjectContainer(TableTypeEnum.EPCG_NOTIFICATION_CHANNEL_TABLE, new Object[] {chan3});
		RowObjectContainer cont4 = new RowObjectContainer(TableTypeEnum.EPCG_NOTIFICATION_CHANNEL_TABLE, new Object[] {chan4});
		conts.add(cont1);
		conts.add(cont2);
		conts.add(cont3);
		conts.add(cont4);
		
		snmpTable.updateStoredRows(conts);
		
		OID oid = snmpTable.getTableRowIndexByValue(new OctetString(chan3Name), EpcglobalReaderMib.idxEpcgNotifChanName);
		assertEquals(cont3, snmpTable.getRowObjContOfRow(oid));
		
		readerDevice.removeAllNotificationChannels();
	}
	
	/**
	 * Runs the test using the gui runner.
	 * @param args No arguments
	 */
	public static void main(String[] args) {
        junit.swingui.TestRunner.run(SnmpTableTest.class);
    }
	
}
