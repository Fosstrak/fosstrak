package org.accada.reader.rprm.core.mgmt.agent.snmp.table ;

import junit.framework.TestCase;

import org.accada.reader.rprm.core.mgmt.agent.snmp.table.EpcgReaderServerTableRow;
import org.accada.reader.rprm.core.mgmt.agent.snmp.table.SnmpTableRowFactory;
import org.accada.reader.rprm.core.mgmt.agent.snmp.table.SnmpTable.TableTypeEnum;
import org.apache.log4j.PropertyConfigurator;
import org.snmp4j.agent.mo.MOTableRow;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.Variable;

/**
 * Tests for the class <code>org.accada.reader.mgmt.agent.snmp.table.SnmpTableRowFactory</code>.
 */
public class SnmpTableRowFactoryTest extends TestCase {
	
	/**
	 * SnmpTableRowFactory instance.
	 */
	private SnmpTableRowFactory factory;
	
	private TableTypeEnum type;
	
	/**
	 * Sets up the test.
	 * @exception Exception An error occurred
	 */
	protected final void setUp() throws Exception {
		super.setUp();
		
		PropertyConfigurator.configure("./props/log4j.properties");
		
		type = TableTypeEnum.EPCG_READER_SERVER_TABLE;
		
		factory = new SnmpTableRowFactory(type);
	}
	
	/**
	 * Does the cleanup.
	 * @exception Exception An error occurred
	 */
	protected final void tearDown() throws Exception {
		super.tearDown();
	}
	
	/**
	 * Tests the <code>createRow()</code> method.
	 */
	public final void testCreateRow() {
		OID index = new OID("1.2");
		Variable var = new Integer32(5);
		Variable[] values = new Variable[] {null, null, var};
		MOTableRow row = factory.createRow(index, values);
		assertEquals(index, row.getIndex());
		assertEquals(var, row.getValue(2));
		assertEquals(EpcgReaderServerTableRow.class, row.getClass());
	}
	
	/**
	 * Runs the test using the gui runner.
	 * @param args No arguments
	 */
	public static void main(String[] args) {
        junit.swingui.TestRunner.run(SnmpTableRowFactoryTest.class);
    }
	
}
