package org.accada.reader.rprm.core.mgmt.agent.snmp.table;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests for package <code>org.accada.reader.mgmt.agent.snmp.table</code>.
 */
public class TableTestSuite {
	
	/**
	 * Creates the test.
	 * @return The test
	 */
	public static Test suite() {
		
		String className = TableTestSuite.class.getName();
		String packageName = className.substring(0, className.lastIndexOf('.'));
		
		TestSuite suite = new TestSuite("Tests for package <" + packageName + ">");
		
		suite.addTestSuite(EpcgAntReadPointTableRowTest.class);
		suite.addTestSuite(EpcgGlobalCountersTableRowTest.class);
		suite.addTestSuite(EpcgIoPortTableRowTest.class);
		suite.addTestSuite(EpcgNotifChanSrcTableRowStatusListenerTest.class);
		suite.addTestSuite(EpcgNotificationChannelTableRowTest.class);
		suite.addTestSuite(EpcgNotifTrigTableRowStatusListenerTest.class);
		suite.addTestSuite(EpcgRdPntSrcTableRowStatusListenerTest.class);
		suite.addTestSuite(EpcgReaderServerTableRowStatusListenerTest.class);
		suite.addTestSuite(EpcgReaderServerTableRowTest.class);
		suite.addTestSuite(EpcgReadPointTableRowTest.class);
		suite.addTestSuite(EpcgReadTrigTableRowStatusListenerTest.class);
		suite.addTestSuite(EpcgSourceTableRowTest.class);
		suite.addTestSuite(EpcgTriggerTableRowTest.class);
		suite.addTestSuite(RowObjectContainerTest.class);
		suite.addTestSuite(SnmpTableLookupListenerTest.class);
		suite.addTestSuite(SnmpTableRowFactoryTest.class);
		suite.addTestSuite(SnmpTableRowTest.class);
		suite.addTestSuite(SnmpTableTest.class);
		suite.addTestSuite(SnmpTargetAddrRowStatusListenerTest.class);
		suite.addTestSuite(TableCreatorTest.class);
		
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
		junit.swingui.TestRunner.run(TableTestSuite.class);
	}
	
}
