package org.fosstrak.reader.rprm.core.mgmt.agent.snmp.mib;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * All tests for package <code>org.fosstrak.reader.mgmt.agent.snmp.mib</code> and its subpackages.
 */
public class AllTests {
	
	/**
	 * Creates the test.
	 * @return The test
	 */
	public static Test suite() {
		
		String className = AllTests.class.getName();
		String packageName = className.substring(0, className.lastIndexOf('.'));
		
		TestSuite suite = new TestSuite("All Tests for package <" + packageName + "> and its subpackages");
		
		suite.addTest(MibTestSuite.suite());
		
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
		junit.swingui.TestRunner.run(AllTests.class);
	}
	
}
