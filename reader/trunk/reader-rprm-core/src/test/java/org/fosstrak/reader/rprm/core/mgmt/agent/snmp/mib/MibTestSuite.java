package org.fosstrak.reader.rprm.core.mgmt.agent.snmp.mib;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests for package <code>org.fosstrak.reader.mgmt.agent.snmp.mib</code>.
 */
public class MibTestSuite {
	
	/**
	 * Creates the test.
	 * @return The test
	 */
	public static Test suite() {
		
		String className = MibTestSuite.class.getName();
		String packageName = className.substring(0, className.lastIndexOf('.'));
		
		TestSuite suite = new TestSuite("Tests for package <" + packageName + ">");
		
		suite.addTestSuite(BitsEnumerationConstraintTest.class);
		
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
		junit.swingui.TestRunner.run(MibTestSuite.class);
	}
	
}
