package org.accada.reader.rprm.core.mgmt.agent.snmp;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests for package <code>org.accada.reader.mgmt.agent.snmp</code>.
 */
public class SnmpTestSuite {
	
	/**
	 * Creates the test.
	 * @return The test
	 */
	public static Test suite() {
		
		String className = SnmpTestSuite.class.getName();
		String packageName = className.substring(0, className.lastIndexOf('.'));
		
		TestSuite suite = new TestSuite("Tests for package <" + packageName + ">");
		
		suite.addTestSuite(EpcgScalarTest.class);
		suite.addTestSuite(SnmpAgentTest.class);
		
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
		junit.swingui.TestRunner.run(SnmpTestSuite.class);
	}
	
}
