package org.fosstrak.reader.rprm.core.mgmt.alarm;

import org.fosstrak.reader.rprm.core.mgmt.alarm.AlarmLevel;

import junit.framework.TestCase;

/**
 * Tests for the class <code>org.fosstrak.reader.mgmt.alarm.AlarmLevel</code>.
 */
public class AlarmLevelTest extends TestCase {
	
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
		assertEquals(0, AlarmLevel.EMERGENCY.toInt());
		assertEquals(1, AlarmLevel.ALERT.toInt());
		assertEquals(2, AlarmLevel.CRITICAL.toInt());
		assertEquals(3, AlarmLevel.ERROR.toInt());
		assertEquals(4, AlarmLevel.WARNING.toInt());
		assertEquals(5, AlarmLevel.NOTICE.toInt());
		assertEquals(6, AlarmLevel.INFORMATIONAL.toInt());
		assertEquals(7, AlarmLevel.DEBUG.toInt());
	}
	
	/**
	 * Tests the <code>intToEnum()</code> method.
	 */
	public final void testIntToEnum() {
		assertEquals(AlarmLevel.EMERGENCY, AlarmLevel.intToEnum(0));
		assertEquals(AlarmLevel.ALERT, AlarmLevel.intToEnum(1));
		assertEquals(AlarmLevel.CRITICAL, AlarmLevel.intToEnum(2));
		assertEquals(AlarmLevel.ERROR, AlarmLevel.intToEnum(3));
		assertEquals(AlarmLevel.WARNING, AlarmLevel.intToEnum(4));
		assertEquals(AlarmLevel.NOTICE, AlarmLevel.intToEnum(5));
		assertEquals(AlarmLevel.INFORMATIONAL, AlarmLevel.intToEnum(6));
		assertEquals(AlarmLevel.DEBUG, AlarmLevel.intToEnum(7));
	}
	
	/**
	 * Runs the test using the gui runner.
	 * @param args No arguments
	 */
	public static void main(String[] args) {
        junit.swingui.TestRunner.run(AlarmLevelTest.class);
    }
	
}
