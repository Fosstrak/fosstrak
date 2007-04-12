package org.accada.reader.rprm.core.mgmt.util;

import java.util.Date;

import junit.framework.TestCase;

import org.accada.reader.rprm.core.ReaderDevice;
import org.accada.reader.rprm.core.ReaderProtocolException;
import org.accada.reader.rprm.core.mgmt.OperationalStatus;
import org.accada.reader.rprm.core.mgmt.agent.snmp.SnmpAgent;
import org.accada.reader.rprm.core.mgmt.agent.snmp.mib.EpcglobalReaderMib;
import org.accada.reader.rprm.core.mgmt.agent.snmp.mib.IfMib;
import org.accada.reader.rprm.core.mgmt.agent.snmp.mib.IpMib;
import org.accada.reader.rprm.core.mgmt.agent.snmp.table.SnmpTable.TableTypeEnum;
import org.accada.reader.rprm.core.mgmt.util.SnmpUtil;
import org.accada.reader.rprm.core.msg.Address;
import org.accada.reader.rprm.core.msg.MessageLayer;
import org.apache.log4j.PropertyConfigurator;
import org.snmp4j.agent.mo.MOScalar;
import org.snmp4j.agent.mo.MOTable;
import org.snmp4j.agent.mo.snmp.SnmpTargetMIB;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TcpAddress;
import org.snmp4j.smi.UdpAddress;

/**
 * Tests for the class <code>org.accada.reader.mgmt.util.SnmpUtil</code>.
 */
public class SnmpUtilTest extends TestCase {
	
	private SnmpUtil snmpUtil;
	
	/**
	 * Sets up the test.
	 * @exception Exception An error occurred
	 */
	protected final void setUp() throws Exception {
		super.setUp();
		
		PropertyConfigurator.configure("./props/log4j.properties");
		
		snmpUtil = new SnmpUtil();
		
		if (SnmpAgent.getInstance() == null) {
			MessageLayer.main(new String[] { });
		}
	}
	
	/**
	 * Does the cleanup.
	 * @exception Exception An error occurred
	 */
	protected final void tearDown() throws Exception {
		super.tearDown();
	}
	
	/**
	 * Tests the <code>dateToOctetString()</code> method.
	 */
	public final void testDateToOctetString() {
		Date date = new Date();
		Date result = SnmpUtil.octetStringToDate(SnmpUtil
				.dateToOctetString(date));
		assertEquals(date.toString(), result.toString());
	}
	
	/**
	 * Tests the <code>octetStringToDate()</code> method.
	 */
	public final void testOctetStringToDate() {
		testDateToOctetString();
	}
	
	/**
	 * Tests the <code>findMOScalar()</code> method.
	 */
	public final void testFindMOScalar() {
		MOScalar descScalar = SnmpUtil.findMOScalar(EpcglobalReaderMib.oidEpcgRdrDevDescription);
		ReaderDevice readerDevice = null;
		try {
			readerDevice = ReaderDevice.getInstance();
		} catch (ReaderProtocolException rpe) {
			fail();
		}
		String desc = readerDevice.getDescription();
		assertEquals(desc, ((OctetString)descScalar.getValue()).toString());
	}
	
	/**
	 * Tests the <code>getSnmpTable()</code> method.
	 */
	public final void testGetSnmpTable() {
		EpcglobalReaderMib epcglobalReaderMib = EpcglobalReaderMib.getInstance();
		IfMib ifMib = IfMib.getInstance();
		IpMib ipMib = IpMib.getInstance();
		SnmpTargetMIB snmpTargetMIB = SnmpAgent.getInstance().getSnmpTargetMIB();
		MOTable table;
		
		table = SnmpUtil.getSnmpTable(TableTypeEnum.EPCG_GLOBAL_COUNTERS_TABLE);
		assertEquals(epcglobalReaderMib.getEpcgGlobalCountersEntry(), table);
		
		table = SnmpUtil.getSnmpTable(TableTypeEnum.EPCG_READER_SERVER_TABLE);
		assertEquals(epcglobalReaderMib.getEpcgReaderServerEntry(), table);
		
		table = SnmpUtil.getSnmpTable(TableTypeEnum.EPCG_READ_POINT_TABLE);
		assertEquals(epcglobalReaderMib.getEpcgReadPointEntry(), table);
		
		table = SnmpUtil.getSnmpTable(TableTypeEnum.EPCG_ANT_READ_POINT_TABLE);
		assertEquals(epcglobalReaderMib.getEpcgAntReadPointEntry(), table);
		
		table = SnmpUtil.getSnmpTable(TableTypeEnum.EPCG_IO_PORT_TABLE);
		assertEquals(epcglobalReaderMib.getEpcgIoPortEntry(), table);
		
		table = SnmpUtil.getSnmpTable(TableTypeEnum.EPCG_SOURCE_TABLE);
		assertEquals(epcglobalReaderMib.getEpcgSourceEntry(), table);
		
		table = SnmpUtil.getSnmpTable(TableTypeEnum.EPCG_NOTIFICATION_CHANNEL_TABLE);
		assertEquals(epcglobalReaderMib.getEpcgNotificationChannelEntry(), table);
		
		table = SnmpUtil.getSnmpTable(TableTypeEnum.EPCG_TRIGGER_TABLE);
		assertEquals(epcglobalReaderMib.getEpcgTriggerEntry(), table);
		
		table = SnmpUtil.getSnmpTable(TableTypeEnum.EPCG_NOTIF_TRIG_TABLE);
		assertEquals(epcglobalReaderMib.getEpcgNotifTrigEntry(), table);
		
		table = SnmpUtil.getSnmpTable(TableTypeEnum.EPCG_READ_TRIG_TABLE);
		assertEquals(epcglobalReaderMib.getEpcgReadTrigEntry(), table);
		
		table = SnmpUtil.getSnmpTable(TableTypeEnum.EPCG_RD_PNT_SRC_TABLE);
		assertEquals(epcglobalReaderMib.getEpcgRdPntSrcEntry(), table);
		
		table = SnmpUtil.getSnmpTable(TableTypeEnum.EPCG_NOTIF_CHAN_SRC_TABLE);
		assertEquals(epcglobalReaderMib.getEpcgNotifChanSrcEntry(), table);
		
		table = SnmpUtil.getSnmpTable(TableTypeEnum.IF_TABLE);
		assertEquals(ifMib.getIfEntry(), table);
		
		table = SnmpUtil.getSnmpTable(TableTypeEnum.IP_ADDR_TABLE);
		assertEquals(ipMib.getIpAddrEntry(), table);
		
		table = SnmpUtil.getSnmpTable(TableTypeEnum.IP_NET_TO_MEDIA_TABLE);
		assertEquals(ipMib.getIpNetToMediaEntry(), table);
		
		table = SnmpUtil.getSnmpTable(TableTypeEnum.SNMP_TARGET_ADDR_TABLE);
		assertEquals(snmpTargetMIB.getSnmpTargetAddrEntry(), table);
	}
	
	/**
	 * Tests the <code>operStateToBITS()</code> method.
	 */
	public final void testOperStateToBITS() {
		OperationalStatus[] values = OperationalStatus.values();
		for (int i = 0; i < values.length; i++) {
			OperationalStatus operState = values[i];
			OperationalStatus result = SnmpUtil.bitsToOperState(SnmpUtil.operStateToBITS(operState));
			assertEquals(operState, result);
		}
	}
	
	/**
	 * Tests the <code>bitsToOperState()</code> method.
	 */
	public final void testBitsToOperState() {
		testOperStateToBITS();
	}
	
	/**
	 * Tests the <code>octetStringToAddress()</code> method.
	 */
	public final void testOctetStringToAddress() {
		OID transportType;
		OctetString address = OctetString.fromByteArray(new byte[] { (byte) 127, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 162 });
		String addrString;
		
		addrString = "";
		transportType = new OID("1.3.6.1.2.1.100.1.1");
		UdpAddress udpAddress = new UdpAddress();
		try {
			udpAddress.setTransportAddress(address);
		} catch (Exception e) {
			fail();
		}
		addrString += "udp://" + udpAddress.getInetAddress().getHostAddress() + ":" + udpAddress.getPort();
		try {
			assertEquals(new Address(addrString).toString(), SnmpUtil.octetStringToAddress(address, transportType).toString());
		} catch (Exception e) {
			fail();
		}
		
		addrString = "";
		transportType = new OID("1.3.6.1.2.1.100.1.5");
		TcpAddress tcpAddress = new TcpAddress();
		try {
			tcpAddress.setTransportAddress(address);
		} catch (Exception e) {
			fail();
		}
		addrString += "tcp://" + tcpAddress.getInetAddress().getHostAddress() + ":" + tcpAddress.getPort();
		try {
			assertEquals(new Address(addrString).toString(), SnmpUtil.octetStringToAddress(address, transportType).toString());
		} catch (Exception e) {
			fail();
		}
		
		transportType = new OID("1.3.6.1.5.1.1.1.1");
		try {
			assertNull(SnmpUtil.octetStringToAddress(address, transportType));
		} catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * Runs the test using the gui runner.
	 * @param args No arguments
	 */
	public static void main(String[] args) {
        junit.swingui.TestRunner.run(SnmpUtilTest.class);
    }
	
}
