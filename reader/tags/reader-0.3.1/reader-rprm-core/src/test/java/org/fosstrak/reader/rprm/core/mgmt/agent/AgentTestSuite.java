package org.accada.reader.rprm.core.mgmt.agent;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests for package <code>org.accada.reader.mgmt.agent</code>.
 */
public class AgentTestSuite {
	
	/**
	 * Creates the test.
	 * @return The test
	 */
	public static Test suite() {
		
		String className = AgentTestSuite.class.getName();
		String packageName = className.substring(0, className.lastIndexOf('.'));
		
		TestSuite suite = new TestSuite("Tests for package <" + packageName + ">");
		
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
		junit.swingui.TestRunner.run(AgentTestSuite.class);
	}
	
}
