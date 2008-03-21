package org.accada.reader.rprm.core.mgmt;

import org.accada.reader.rprm.core.mgmt.AdministrativeStatus;

import junit.framework.TestCase;

/**
 * Tests for the class <code>org.accada.reader.mgmt.AdministrativeStatus</code>.
 */
public class AdministrativeStatusTest extends TestCase {
	
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
		assertEquals(1, AdministrativeStatus.UP.toInt());
		assertEquals(2, AdministrativeStatus.DOWN.toInt());
	}
	
	/**
	 * Tests the <code>intToEnum()</code> method.
	 */
	public final void testIntToEnum() {
		assertEquals(AdministrativeStatus.UP, AdministrativeStatus.intToEnum(1));
		assertEquals(AdministrativeStatus.DOWN, AdministrativeStatus.intToEnum(2));
	}
	
	/**
	 * Runs the test using the gui runner.
	 * @param args No arguments
	 */
	public static void main(String[] args) {
        junit.swingui.TestRunner.run(AdministrativeStatusTest.class);
    }
	
}
