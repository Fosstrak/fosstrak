package org.accada.reader.rprm.core.mgmt;

import org.accada.reader.rprm.core.mgmt.OperationalStatus;

import junit.framework.TestCase;

/**
 * Tests for the class <code>org.accada.reader.mgmt.OperationalStatus</code>.
 */
public class OperationalStatusTest extends TestCase {
	
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
	 * Tests the <code>toInt()</code> method.
	 */
	public final void testToInt() {
		assertEquals(1, OperationalStatus.UNKNOWN.toInt());
		assertEquals(2, OperationalStatus.OTHER.toInt());
		assertEquals(3, OperationalStatus.UP.toInt());
		assertEquals(4, OperationalStatus.DOWN.toInt());
	}
	
	/**
	 * Tests the <code>intToEnum()</code> method.
	 */
	public final void testIntToEnum() {
		assertEquals(OperationalStatus.UNKNOWN, OperationalStatus.intToEnum(1));
		assertEquals(OperationalStatus.OTHER, OperationalStatus.intToEnum(2));
		assertEquals(OperationalStatus.UP, OperationalStatus.intToEnum(3));
		assertEquals(OperationalStatus.DOWN, OperationalStatus.intToEnum(4));
	}
	
	/**
	 * Runs the test using the gui runner.
	 * @param args No arguments
	 */
	public static void main(String[] args) {
        junit.swingui.TestRunner.run(OperationalStatusTest.class);
    }
	
}
