package org.accada.reader.rprm.core.mgmt.agent.snmp.table ;

import junit.framework.TestCase;

import org.accada.reader.rprm.core.AntennaReadPoint;
import org.accada.reader.rprm.core.ReadPoint;
import org.accada.reader.rprm.core.ReaderDevice;
import org.accada.reader.rprm.core.ReaderProtocolException;
import org.accada.reader.rprm.core.Source;
import org.accada.reader.rprm.core.mgmt.agent.snmp.SnmpAgent;
import org.accada.reader.rprm.core.mgmt.agent.snmp.table.EpcgRdPntSrcTableRowStatusListener;
import org.accada.reader.rprm.core.mgmt.agent.snmp.table.RowObjectContainer;
import org.accada.reader.rprm.core.mgmt.agent.snmp.table.SnmpTableRow;
import org.accada.reader.rprm.core.mgmt.agent.snmp.table.SnmpTable.TableTypeEnum;
import org.accada.reader.rprm.core.msg.MessageLayer;
import org.apache.log4j.PropertyConfigurator;
import org.snmp4j.agent.mo.snmp.RowStatus;
import org.snmp4j.agent.mo.snmp.RowStatusEvent;

/**
 * Tests for the class <code>org.accada.reader.mgmt.agent.snmp.table.EpcgRdPntSrcTableRowStatusListener</code>.
 */
public class EpcgRdPntSrcTableRowStatusListenerTest extends TestCase {
	
	private EpcgRdPntSrcTableRowStatusListener rowStatusListener;
	private ReaderDevice readerDevice;
	
	private ReadPoint readPoint;
	private Source source;
	
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
		
		rowStatusListener = new EpcgRdPntSrcTableRowStatusListener();
		
		readPoint = AntennaReadPoint.create("EpcgRdPntSrcTableRowStatusListenerTestReadPoint", readerDevice, null);
		source = Source.create("EpcgRdPntSrcTableRowStatusListenerTestSource", readerDevice);
	}
	
	/**
	 * Does the cleanup.
	 * @exception Exception An error occurred
	 */
	protected final void tearDown() throws Exception {
		super.tearDown();
		
		readerDevice.getReadPoints().remove(readPoint.getName());
		readerDevice.removeSources(new Source[] { source });
	}
	
	/**
	 * Tests the <code>rowStatusChanged()</code> method.
	 */
	public final void testRowStatusChanged() {
		SnmpTableRow row = SnmpTableRow.getSnmpTableRow(new RowObjectContainer(TableTypeEnum.EPCG_RD_PNT_SRC_TABLE, new Object[] { source, readPoint }));
		RowStatusEvent event;
		
		event = new RowStatusEvent(row, null, row, null, RowStatus.destroy, RowStatus.createAndGo);
		rowStatusListener.rowStatusChanged(event);
		try {
			source.getReadPoint(readPoint.getName());
		} catch (ReaderProtocolException rpe) {
			fail();
		}
		
		event = new RowStatusEvent(row, null, row, null, RowStatus.active, RowStatus.notInService);
		rowStatusListener.rowStatusChanged(event);
		try {
			source.getReadPoint(readPoint.getName());
			fail();
		} catch (ReaderProtocolException rpe) {
			// ok
		}
		
		event = new RowStatusEvent(row, null, row, null, RowStatus.notInService, RowStatus.active);
		rowStatusListener.rowStatusChanged(event);
		try {
			source.getReadPoint(readPoint.getName());
		} catch (ReaderProtocolException rpe) {
			fail();
		}
		
		event = new RowStatusEvent(row, null, row, null, RowStatus.active, RowStatus.destroy);
		rowStatusListener.rowStatusChanged(event);
		try {
			source.getReadPoint(readPoint.getName());
			fail();
		} catch (ReaderProtocolException rpe) {
			// ok
		}
		
		event = new RowStatusEvent(row, null, row, null, RowStatus.destroy, RowStatus.createAndWait);
		rowStatusListener.rowStatusChanged(event);
		try {
			source.getReadPoint(readPoint.getName());
			fail();
		} catch (ReaderProtocolException rpe) {
			// ok
		}
		
		event = new RowStatusEvent(row, null, row, null, RowStatus.notInService, RowStatus.active);
		rowStatusListener.rowStatusChanged(event);
		try {
			source.getReadPoint(readPoint.getName());
		} catch (ReaderProtocolException rpe) {
			fail();
		}
		
		event = new RowStatusEvent(row, null, row, null, RowStatus.active, RowStatus.destroy);
		rowStatusListener.rowStatusChanged(event);
		try {
			source.getReadPoint(readPoint.getName());
			fail();
		} catch (ReaderProtocolException rpe) {
			// ok
		}
	}
	
	/**
	 * Runs the test using the gui runner.
	 * @param args No arguments
	 */
	public static void main(String[] args) {
        junit.swingui.TestRunner.run(EpcgRdPntSrcTableRowStatusListenerTest.class);
    }
	
}
