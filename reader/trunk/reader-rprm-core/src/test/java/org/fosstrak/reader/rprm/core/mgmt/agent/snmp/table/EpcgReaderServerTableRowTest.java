package org.accada.reader.rprm.core.mgmt.agent.snmp.table ;

import junit.framework.TestCase;

import org.accada.reader.rprm.core.mgmt.agent.snmp.SnmpAgent;
import org.accada.reader.rprm.core.mgmt.agent.snmp.table.RowObjectContainer;
import org.accada.reader.rprm.core.mgmt.agent.snmp.table.SnmpTableRow;
import org.accada.reader.rprm.core.mgmt.agent.snmp.table.EpcgReaderServerTableRow.ServerTypeEnum;
import org.accada.reader.rprm.core.mgmt.agent.snmp.table.SnmpTable.TableTypeEnum;
import org.accada.reader.rprm.core.msg.MessageLayer;
import org.apache.log4j.PropertyConfigurator;

/**
 * Tests for the class <code>org.accada.reader.mgmt.agent.snmp.table.EpcgReaderServerTableRow</code>.
 */
public class EpcgReaderServerTableRowTest extends TestCase {


	private SnmpTableRow row;

	private String dhcp = "192.168.1.1/67";

	/**
	 * Sets up the test.
	 * @exception Exception An error occurred
	 */
	protected final void setUp() throws Exception {
		super.setUp();

		PropertyConfigurator.configure("./target/classes/props/log4j.properties");

		if (SnmpAgent.getInstance() == null) {
			MessageLayer.main(new String[] { });
		}

		row = SnmpTableRow.getSnmpTableRow(new RowObjectContainer(TableTypeEnum.EPCG_READER_SERVER_TABLE, new Object[] { ServerTypeEnum.DHCP, dhcp }));
	}

	/**
	 * Does the cleanup.
	 * @exception Exception An error occurred
	 */
	protected final void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Tests the <code>setValue()</code> method.
	 */
	public final void testSetValue() {
		// TODO: implement
//		row.setValue(EpcglobalReaderMib.idxEpcgReaderServerAddressType, ...);
//		assertEquals(..., ...);
//
//		row.setValue(EpcglobalReaderMib.idxEpcgReaderServerAddress, ...);
//		assertEquals(..., ...);
	}

	/**
	 * Runs the test using the gui runner.
	 * @param args No arguments
	 */
	public static void main(String[] args) {
        junit.swingui.TestRunner.run(EpcgReaderServerTableRowTest.class);
    }

}
