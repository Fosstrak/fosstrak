package org.accada.reader.rprm.core.mgmt.alarm;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests for package <code>org.accada.reader.mgmt.alarm</code>.
 */
public class AlarmTestSuite {
	
	/**
	 * Creates the test.
	 * @return The test
	 */
	public static Test suite() {
		
		String className = AlarmTestSuite.class.getName();
		String packageName = className.substring(0, className.lastIndexOf('.'));
		
		TestSuite suite = new TestSuite("Tests for package <" + packageName + ">");
		
		suite.addTestSuite(AlarmTest.class);
		suite.addTestSuite(AlarmChannelTest.class);
		suite.addTestSuite(AlarmLevelTest.class);
		suite.addTestSuite(EdgeTriggeredAlarmDirectionTest.class);
		suite.addTestSuite(EdgeTriggeredAlarmStatusTest.class);
		
		//
		// Add more tests here
		//
		
		return suite;
	}
	
	/**
	 * Runs the test suite using the gui runner.
	 * @param args No arguments
	 */
	public static void main(String[] args) {
		junit.swingui.TestRunner.run(AlarmTestSuite.class);
	}
	
}
