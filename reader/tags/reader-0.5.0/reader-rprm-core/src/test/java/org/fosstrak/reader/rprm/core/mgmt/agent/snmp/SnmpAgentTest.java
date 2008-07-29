package org.fosstrak.reader.rprm.core.mgmt.agent.snmp;

import java.net.MalformedURLException;

import junit.framework.TestCase;

import org.fosstrak.reader.rprm.core.ReaderDevice;
import org.fosstrak.reader.rprm.core.ReaderProtocolException;
import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.SnmpAgent;
import org.fosstrak.reader.rprm.core.mgmt.alarm.AlarmChannel;
import org.fosstrak.reader.rprm.core.msg.Address;
import org.fosstrak.reader.rprm.core.msg.MessageLayer;
import org.apache.log4j.xml.DOMConfigurator;
import org.snmp4j.agent.mo.snmp.SnmpTargetMIB;
import org.snmp4j.smi.OctetString;

/**
 * Tests for the class <code>org.fosstrak.reader.mgmt.agent.snmp.SnmpAgent</code>.
 */
public class SnmpAgentTest extends TestCase {

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
	}

	/**
	 * Does the cleanup.
	 * @exception Exception An error occurred
	 */
	protected final void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Tests the <code>isInitialized()</code> method.
	 */
	public final void testIsInitialized() {
		assertEquals(true, snmpAgent.isInitialized());
	}

	/**
	 * Tests the <code>addAlarmChannels()</code> method.
	 */
	public final void testAddAlarmChannels() {
		String name = "TestAlarmChannel";
		AlarmChannel alarmChannel = null;
		try {
			alarmChannel = AlarmChannel.create(name, new Address("udp://127.0.0.1:162"), readerDevice);
		} catch (ReaderProtocolException rpe) {
			try {
				alarmChannel = readerDevice.getAlarmChannel(name);
			} catch (ReaderProtocolException rpe2) {
				fail();
			}
		} catch (MalformedURLException mue) {
			fail();
		}
		readerDevice.removeAlarmChannels(new AlarmChannel[] { alarmChannel });

		SnmpTargetMIB targetMib = snmpAgent.getSnmpTargetMIB();
		assertNull(targetMib.getTargetAddress(new OctetString(name)));
		snmpAgent.addAlarmChannels(new AlarmChannel[] { alarmChannel });
		assertNotNull(targetMib.getTargetAddress(new OctetString(name)));
		snmpAgent.removeAlarmChannel(alarmChannel);
		assertNull(targetMib.getTargetAddress(new OctetString(name)));
	}

	/**
	 * Tests the <code>removeAlarmChannel()</code> method.
	 */
	public final void testRemoveAlarmChannel() {
		testAddAlarmChannels();
	}


	/**
	 * Runs the test using the gui runner.
	 * @param args No arguments
	 */
	public static void main(String[] args) {
        junit.swingui.TestRunner.run(SnmpAgentTest.class);
    }

}
