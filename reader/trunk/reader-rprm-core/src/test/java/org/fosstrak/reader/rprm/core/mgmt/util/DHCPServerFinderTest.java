package org.fosstrak.reader.rprm.core.mgmt.util;

import org.fosstrak.reader.rprm.core.mgmt.util.DHCPServerFinder;

import junit.framework.TestCase;

/**
 * Tests for the class <code>org.fosstrak.reader.mgmt.util.DHCPServerFinder</code>.
 */
public class DHCPServerFinderTest extends TestCase {
	
	/**
	 * Sets up the test.
	 * @exception Exception An error occurred
	 */
	protected final void setUp() throws Exception {
		super.setUp();
	}
	
	/**
	 * Does the cleanup.
	 * @exception Exception An error occurred
	 */
	protected final void tearDown() throws Exception {
		super.tearDown();
	}
	
	/**
	 * Tests the <code>macAddressStringToByteArray()</code> method.
	 */
	public final void testMacAddressStringToByteArray() {
		String macAddr = "00-08-9F-1C-44-2D";
		byte[] ba = DHCPServerFinder.macAddressStringToByteArray(macAddr, "-");
		assertEquals((byte) 0, ba[0]);
		assertEquals((byte) 8, ba[1]);
		assertEquals((byte) 159, ba[2]);
		assertEquals((byte) 28, ba[3]);
		assertEquals((byte) 68, ba[4]);
		assertEquals((byte) 45, ba[5]);
	}
	
	/**
	 * Runs the test using the gui runner.
	 * @param args No arguments
	 */
	public static void main(String[] args) {
        junit.swingui.TestRunner.run(DHCPServerFinderTest.class);
    }
	
}
