package org.accada.reader.rprm.core;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests for package <code>org.accada.reader</code>.
 */
public class ReaderTestSuite {
	
	/**
	 * Creates the test.
	 * @return The test
	 */
	public static Test suite() {
		
		String className = ReaderTestSuite.class.getName();
		String packageName = className.substring(0, className.lastIndexOf('.'));
		
		TestSuite suite = new TestSuite("Tests for package <" + packageName + ">");
		
		suite.addTestSuite(AntennaReadPointTest.class);
		suite.addTestSuite(NotificationChannelTest.class);
		suite.addTestSuite(ReaderDeviceTest.class);
		suite.addTestSuite(ReadPointTest.class);
		suite.addTestSuite(SourceTest.class);
		suite.addTestSuite(TriggerTest.class);
		
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
		junit.swingui.TestRunner.run(ReaderTestSuite.class);
	}
	
}
