package org.fosstrak.reader.rprm.core.mgmt;

import junit.framework.TestCase;

import org.fosstrak.reader.rprm.core.ReaderDevice;
import org.fosstrak.reader.rprm.core.mgmt.IOPort;
import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.SnmpAgent;
import org.fosstrak.reader.rprm.core.msg.MessageLayer;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * Tests for the class <code>org.fosstrak.reader.mgmt.IOPort</code>.
 */
public class IOPortTest extends TestCase {

	private IOPort ioPort;

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

		ioPort = new IOPort("IOPortTestIOPort");
	}

	/**
	 * Does the cleanup.
	 * @exception Exception An error occurred
	 */
	protected final void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Tests the <code>increaseOperStateSuppressions()</code> method.
	 */
	public final void testIncreaseOperStateSuppressions() {
		int value = ioPort.getOperStateSuppressions();
		ioPort.increaseOperStateSuppressions();
		assertEquals(value + 1, ioPort.getOperStateSuppressions());
	}

	/**
	 * Tests the <code>resetOperStateSuppressions()</code> method.
	 */
	public final void testResetOperStateSuppressions() {
		ioPort.increaseOperStateSuppressions();
		assertTrue(ioPort.getOperStateSuppressions() > 0);
		ioPort.resetOperStateSuppressions();
		assertEquals(0, ioPort.getOperStateSuppressions());
	}

	/**
	 * Runs the test using the gui runner.
	 * @param args No arguments
	 */
	public static void main(String[] args) {
        junit.swingui.TestRunner.run(IOPortTest.class);
    }

}
