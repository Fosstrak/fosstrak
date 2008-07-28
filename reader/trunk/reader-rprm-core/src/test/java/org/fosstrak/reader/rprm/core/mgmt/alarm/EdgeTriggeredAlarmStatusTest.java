package org.fosstrak.reader.rprm.core.mgmt.alarm;

import org.fosstrak.reader.rprm.core.mgmt.alarm.EdgeTriggeredAlarmStatus;

import junit.framework.TestCase;

/**
 * Tests for the class <code>org.fosstrak.reader.mgmt.alarm.EdgeTriggeredAlarmStatus</code>.
 */
public class EdgeTriggeredAlarmStatusTest extends TestCase {
	
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
		assertEquals(0, EdgeTriggeredAlarmStatus.ARMED.toInt());
		assertEquals(1, EdgeTriggeredAlarmStatus.FIRED.toInt());
	}
	
	/**
	 * Tests the <code>intToEnum()</code> method.
	 */
	public final void testIntToEnum() {
		assertEquals(EdgeTriggeredAlarmStatus.ARMED, EdgeTriggeredAlarmStatus.intToEnum(0));
		assertEquals(EdgeTriggeredAlarmStatus.FIRED, EdgeTriggeredAlarmStatus.intToEnum(1));
	}
	
	/**
	 * Runs the test using the gui runner.
	 * @param args No arguments
	 */
	public static void main(String[] args) {
        junit.swingui.TestRunner.run(EdgeTriggeredAlarmStatusTest.class);
    }
	
}
