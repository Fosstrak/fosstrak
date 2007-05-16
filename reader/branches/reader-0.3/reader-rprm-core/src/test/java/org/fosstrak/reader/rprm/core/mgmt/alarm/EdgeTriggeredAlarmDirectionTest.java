package org.accada.reader.rprm.core.mgmt.alarm;

import org.accada.reader.rprm.core.mgmt.alarm.EdgeTriggeredAlarmDirection;

import junit.framework.TestCase;

/**
 * Tests for the class <code>org.accada.reader.mgmt.alarm.EdgeTriggeredAlarmDirection</code>.
 */
public class EdgeTriggeredAlarmDirectionTest extends TestCase {
	
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
		assertEquals(0, EdgeTriggeredAlarmDirection.RISING.toInt());
		assertEquals(1, EdgeTriggeredAlarmDirection.FALLING.toInt());
	}
	
	/**
	 * Tests the <code>intToEnum()</code> method.
	 */
	public final void testIntToEnum() {
		assertEquals(EdgeTriggeredAlarmDirection.RISING, EdgeTriggeredAlarmDirection.intToEnum(0));
		assertEquals(EdgeTriggeredAlarmDirection.FALLING, EdgeTriggeredAlarmDirection.intToEnum(1));
	}
	
	/**
	 * Runs the test using the gui runner.
	 * @param args No arguments
	 */
	public static void main(String[] args) {
        junit.swingui.TestRunner.run(EdgeTriggeredAlarmDirectionTest.class);
    }
	
}
