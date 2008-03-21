package org.accada.reader.rprm.core.mgmt.agent.snmp.table ;

import junit.framework.TestCase;

import org.accada.reader.rprm.core.ReaderDevice;
import org.accada.reader.rprm.core.Trigger;
import org.accada.reader.rprm.core.TriggerType;
import org.accada.reader.rprm.core.mgmt.agent.snmp.SnmpAgent;
import org.accada.reader.rprm.core.mgmt.agent.snmp.mib.EpcglobalReaderMib;
import org.accada.reader.rprm.core.mgmt.agent.snmp.table.RowObjectContainer;
import org.accada.reader.rprm.core.mgmt.agent.snmp.table.SnmpTableRow;
import org.accada.reader.rprm.core.mgmt.agent.snmp.table.SnmpTable.TableTypeEnum;
import org.accada.reader.rprm.core.msg.MessageLayer;
import org.apache.log4j.xml.DOMConfigurator;
import org.snmp4j.smi.Gauge32;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;

/**
 * Tests for the class <code>org.accada.reader.mgmt.agent.snmp.table.EpcgTriggerTableRow</code>.
 */
public class EpcgTriggerTableRowTest extends TestCase {


	private SnmpTableRow row;

	private Trigger trigger;

	private ReaderDevice readerDevice;

	/**
	 * Sets up the test.
	 * @exception Exception An error occurred
	 */
	protected final void setUp() throws Exception {
		super.setUp();

		DOMConfigurator.configure("./target/classes/props/log4j.xml");

		if (SnmpAgent.getInstance() == null) {
			MessageLayer.main(new String[] { });
		}

		readerDevice = ReaderDevice.getInstance();

		trigger = Trigger.create("EpcgTriggerTableRowTestTrigger", TriggerType.CONTINUOUS, "", readerDevice);

		row = SnmpTableRow.getSnmpTableRow(new RowObjectContainer(TableTypeEnum.EPCG_TRIGGER_TABLE, new Object[] { trigger }));
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
	 * Tests the <code>getValue()</code> method.
	 */
	public final void testGetValue() {
		Variable value;

		value = row.getValue(EpcglobalReaderMib.idxEpcgTrigName);
		assertEquals(new OctetString(trigger.getName()), value);

		value = row.getValue(EpcglobalReaderMib.idxEpcgTrigType);
		int triggerTypeInt;
		String type = trigger.getType();
		if (type.equals(TriggerType.NONE)) triggerTypeInt = 1;
		else if (type.equals(TriggerType.TIMER)) triggerTypeInt = 2;
		else if (type.equals(TriggerType.CONTINUOUS)) triggerTypeInt = 3;
		else if (type.equals(TriggerType.IO_EDGE)) triggerTypeInt = 4;
		else if (type.equals(TriggerType.VENDOR_EXTENSION)) triggerTypeInt = 5;
		else triggerTypeInt = 0;
		assertEquals(new Integer32(triggerTypeInt), value);

		value = row.getValue(EpcglobalReaderMib.idxEpcgTrigParameters);
		assertEquals(new OctetString(trigger.getValue()), value);

		value = row.getValue(EpcglobalReaderMib.idxEpcgTriggerMatches);
		assertEquals(new Gauge32(trigger.getFireCount()), value);

		value = row.getValue(EpcglobalReaderMib.idxEpcgTrigIoPort);
		assertEquals(new OID("0.0"), value); // we don't associate triggers with IOPorts
	}

	/**
	 * Runs the test using the gui runner.
	 * @param args No arguments
	 */
	public static void main(String[] args) {
        junit.swingui.TestRunner.run(EpcgTriggerTableRowTest.class);
    }

}
