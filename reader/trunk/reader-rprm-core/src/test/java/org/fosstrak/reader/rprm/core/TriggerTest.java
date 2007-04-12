package org.accada.reader.rprm.core;

import junit.framework.TestCase;

import org.accada.reader.rprm.core.ReaderDevice;
import org.accada.reader.rprm.core.Trigger;
import org.accada.reader.rprm.core.TriggerType;
import org.accada.reader.rprm.core.mgmt.agent.snmp.SnmpAgent;
import org.accada.reader.rprm.core.msg.MessageLayer;
import org.apache.log4j.PropertyConfigurator;

/**
 * Tests for the class <code>org.accada.reader.Trigger</code>.
 */
public class TriggerTest extends TestCase {
	
	private Trigger trigger;
	
	private ReaderDevice readerDevice;
	
	/**
	 * Sets up the test.
	 * @exception Exception An error occurred
	 */
	protected final void setUp() throws Exception {
		super.setUp();
		
		PropertyConfigurator.configure("./props/log4j.properties");
		
		if (SnmpAgent.getInstance() == null) {
			MessageLayer.main(new String[] { });
		}
		
		readerDevice = ReaderDevice.getInstance();
		
		trigger = Trigger.create("TriggerTestTrigger", TriggerType.TIMER, "ms=2500", readerDevice);
	}
	
	/**
	 * Does the cleanup.
	 * @exception Exception An error occurred
	 */
	protected final void tearDown() throws Exception {
		super.tearDown();
		
		readerDevice.removeTriggers(new Trigger[] { trigger });
	}
	
	/**
	 * Tests the <code>resetFireCount()</code> method.
	 */
	public final void testResetFireCount() {
		trigger.fire();
		assertEquals(1, trigger.getFireCount());
		trigger.resetFireCount();
		assertEquals(0, trigger.getFireCount());
	}
	
	/**
	 * Runs the test using the gui runner.
	 * @param args No arguments
	 */
	public static void main(String[] args) {
        junit.swingui.TestRunner.run(TriggerTest.class);
    }
	
}
