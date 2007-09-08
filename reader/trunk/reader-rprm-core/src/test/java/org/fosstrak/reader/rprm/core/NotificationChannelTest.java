package org.accada.reader.rprm.core;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;

import junit.framework.TestCase;

import org.accada.reader.rprm.core.NotificationChannel;
import org.accada.reader.rprm.core.ReaderDevice;
import org.accada.reader.rprm.core.mgmt.OperationalStatus;
import org.accada.reader.rprm.core.mgmt.agent.snmp.SnmpAgent;
import org.accada.reader.rprm.core.msg.MessageLayer;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * Tests for the class <code>org.accada.reader.NotificationChannel</code>.
 */
public class NotificationChannelTest extends TestCase {

	private NotificationChannel notifChan;

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

		notifChan = NotificationChannel.create("NotificationChannelTestNotifChan", "addr", readerDevice);
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
	 * Tests the <code>getOperStatus()</code> method.
	 */
	public final void testGetOperStatus() {
		// try to reach the host

		int timeout = 200;
		OperationalStatus operStatus;

		URI addr = null;
		try {
			addr = new URI(notifChan.getAddress());
		} catch (URISyntaxException use) {
			assertEquals(OperationalStatus.UNKNOWN, notifChan.getOperStatus());
			return;
		}

		try {
			InetAddress host = InetAddress.getByName(addr.getHost());
			if (host.isReachable(timeout))
				operStatus = OperationalStatus.UP;
			else
				operStatus = OperationalStatus.DOWN;
		} catch (Exception e) {
			operStatus = OperationalStatus.UNKNOWN;
		}

		assertEquals(operStatus, notifChan.getOperStatus());
	}

	/**
	 * Tests the <code>increaseOperStateSuppressions()</code> method.
	 */
	public final void testIncreaseOperStateSuppressions() {
		int value = notifChan.getOperStateSuppressions();
		notifChan.increaseOperStateSuppressions();
		assertEquals(value + 1, notifChan.getOperStateSuppressions());
	}

	/**
	 * Tests the <code>resetOperStateSuppressions()</code> method.
	 */
	public final void testResetOperStateSuppressions() {
		notifChan.increaseOperStateSuppressions();
		assertTrue(notifChan.getOperStateSuppressions() > 0);
		notifChan.resetOperStateSuppressions();
		assertEquals(0, notifChan.getOperStateSuppressions());
	}

	/**
	 * Runs the test using the gui runner.
	 * @param args No arguments
	 */
	public static void main(String[] args) {
        junit.swingui.TestRunner.run(NotificationChannelTest.class);
    }

}
