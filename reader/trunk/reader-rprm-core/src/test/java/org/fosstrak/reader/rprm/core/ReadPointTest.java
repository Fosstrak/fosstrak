package org.accada.reader.rprm.core;

import junit.framework.TestCase;

import org.accada.reader.hal.HardwareException;
import org.accada.reader.hal.ReadPointNotFoundException;
import org.accada.reader.hal.UnsupportedOperationException;
import org.accada.reader.rprm.core.ReadPoint;
import org.accada.reader.rprm.core.ReaderDevice;
import org.accada.reader.rprm.core.mgmt.OperationalStatus;
import org.accada.reader.rprm.core.mgmt.agent.snmp.SnmpAgent;
import org.accada.reader.rprm.core.msg.MessageLayer;
import org.apache.log4j.PropertyConfigurator;

/**
 * Tests for the class <code>org.accada.reader.ReadPoint</code>.
 */
public class ReadPointTest extends TestCase {

	private ReadPoint readPoint;

	private ReaderDevice readerDevice;

	/**
	 * Sets up the test.
	 * @exception Exception An error occurred
	 */
	protected final void setUp() throws Exception {
		super.setUp();

		PropertyConfigurator.configure("./target/classes/props/log4j.properties");

		if (SnmpAgent.getInstance() == null) {
			MessageLayer.main(new String[] { });
		}

		readerDevice = ReaderDevice.getInstance();
		readPoint = readerDevice.getAllReadPoints()[0];
	}

	/**
	 * Does the cleanup.
	 * @exception Exception An error occurred
	 */
	protected final void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Tests the <code>getOperStatus()</code> method.
	 */
	public final void testGetOperStatus() {
		boolean ready = false;
		try {
			readPoint.getReader().isReadPointReady(readPoint.name);
		} catch (HardwareException e) {
			// TODO Auto-generated catch block
		}
		if (ready) {
			assertEquals(OperationalStatus.UP, readPoint.getOperStatus());
		} else {
			assertEquals(OperationalStatus.DOWN, readPoint.getOperStatus());
		}
	}

	/**
	 * Tests the <code>increaseOperStateSuppressions()</code> method.
	 */
	public final void testIncreaseOperStateSuppressions() {
		int value = readPoint.getOperStateSuppressions();
		readPoint.increaseOperStateSuppressions();
		assertEquals(value + 1, readPoint.getOperStateSuppressions());
	}

	/**
	 * Tests the <code>resetOperStateSuppressions()</code> method.
	 */
	public final void testResetOperStateSuppressions() {
		readPoint.increaseOperStateSuppressions();
		assertTrue(readPoint.getOperStateSuppressions() > 0);
		readPoint.resetOperStateSuppressions();
		assertEquals(0, readPoint.getOperStateSuppressions());
	}

	/**
	 * Runs the test using the gui runner.
	 * @param args No arguments
	 */
	public static void main(String[] args) {
        junit.swingui.TestRunner.run(ReadPointTest.class);
    }

}
