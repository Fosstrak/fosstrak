package org.fosstrak.reader.rprm.core.mgmt.util;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests for package <code>org.fosstrak.reader.mgmt.util</code>.
 */
public class UtilTestSuite {
	
	/**
	 * Creates the test.
	 * @return The test
	 */
	public static Test suite() {
		
		String className = UtilTestSuite.class.getName();
		String packageName = className.substring(0, className.lastIndexOf('.'));
		
		TestSuite suite = new TestSuite("Tests for package <" + packageName + ">");
		
		suite.addTestSuite(SnmpUtilTest.class);
		suite.addTestSuite(DHCPServerFinderTest.class);
		
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
		junit.swingui.TestRunner.run(UtilTestSuite.class);
	}
	
}
