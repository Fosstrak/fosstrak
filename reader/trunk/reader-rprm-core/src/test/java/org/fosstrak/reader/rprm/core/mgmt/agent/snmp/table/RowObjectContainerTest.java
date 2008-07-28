package org.fosstrak.reader.rprm.core.mgmt.agent.snmp.table ;

import junit.framework.TestCase;

import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.table.RowObjectContainer;
import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.table.SnmpTable.TableTypeEnum;

/**
 * Tests for the class <code>org.fosstrak.reader.mgmt.agent.snmp.table.RowObjectContainer</code>.
 */
public class RowObjectContainerTest extends TestCase {
	
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
	 * Tests the <code>getTableType()</code> method.
	 */
	public final void testGetTableType() {
		Object obj = new Object();
		RowObjectContainer cont = new RowObjectContainer(TableTypeEnum.EPCG_TRIGGER_TABLE, new Object[] {obj});
		assertEquals(TableTypeEnum.EPCG_TRIGGER_TABLE, cont.getTableType());
	}
	
	/**
	 * Tests the <code>getRowObjects()</code> method.
	 */
	public final void testGetRowObjects() {
		Object[] objs = new Object[] {new Object()};
		RowObjectContainer cont = new RowObjectContainer(TableTypeEnum.EPCG_TRIGGER_TABLE, objs);
		assertEquals(objs, cont.getRowObjects());
	}
	
	/**
	 * Tests the <code>equals()</code> method.
	 */
	public final void testEquals() {
		Object obj = new Object();
		RowObjectContainer cont1 = new RowObjectContainer(TableTypeEnum.EPCG_TRIGGER_TABLE, new Object[] {obj});
		RowObjectContainer cont2 = new RowObjectContainer(TableTypeEnum.EPCG_TRIGGER_TABLE, new Object[] {obj});
		assertEquals(true, cont1.equals(cont2));
	}
	
	/**
	 * Runs the test using the gui runner.
	 * @param args No arguments
	 */
	public static void main(String[] args) {
        junit.swingui.TestRunner.run(RowObjectContainerTest.class);
    }
	
}
