package org.accada.reader.rprm.core.mgmt.agent.snmp.table ;

import java.util.Iterator;

import junit.framework.TestCase;

import org.accada.reader.rprm.core.AntennaReadPoint;
import org.accada.reader.rprm.core.ReadPoint;
import org.accada.reader.rprm.core.ReaderDevice;
import org.accada.reader.rprm.core.Source;
import org.accada.reader.rprm.core.Trigger;
import org.accada.reader.rprm.core.mgmt.agent.snmp.SnmpAgent;
import org.accada.reader.rprm.core.mgmt.agent.snmp.mib.EpcglobalReaderMib;
import org.accada.reader.rprm.core.mgmt.agent.snmp.table.EpcgGlobalCountersTableRow;
import org.accada.reader.rprm.core.mgmt.agent.snmp.table.SnmpTable;
import org.accada.reader.rprm.core.mgmt.agent.snmp.table.EpcgGlobalCountersTableRow.CounterType;
import org.accada.reader.rprm.core.mgmt.agent.snmp.table.SnmpTable.TableTypeEnum;
import org.accada.reader.rprm.core.mgmt.util.SnmpUtil;
import org.accada.reader.rprm.core.msg.MessageLayer;
import org.apache.log4j.xml.DOMConfigurator;
import org.snmp4j.smi.Integer32;

/**
 * Tests for the class <code>org.accada.reader.mgmt.agent.snmp.table.EpcgGlobalCountersTableRow</code>.
 */
public class EpcgGlobalCountersTableRowTest extends TestCase {


	private SnmpTable table;

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

		table = (SnmpTable) SnmpUtil.getSnmpTable(TableTypeEnum.EPCG_GLOBAL_COUNTERS_TABLE);
	}

	/**
	 * Does the cleanup.
	 * @exception Exception An error occurred
	 */
	protected final void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Tests the <code>getValue()</code> method.
	 */
	public final void testGetValue() {
		ReadPoint[] readPoints;
		Source[] sources;

		Iterator iter = table.getModel().iterator();
		while (iter.hasNext()) {
			int value = 0;
			EpcgGlobalCountersTableRow row = (EpcgGlobalCountersTableRow) iter.next();
			CounterType type = CounterType.intToEnum(Integer.parseInt(row.getIndex().toString()));
			switch (type) {

				case ANTENNA_TAGS_IDENTIFIED:
					readPoints = readerDevice.getAllReadPoints();
					for (int i = 0; i < readPoints.length; i++) {
						if (readPoints[i] instanceof AntennaReadPoint) {
							value += ((AntennaReadPoint)readPoints[i])
									.getIdentificationCount();
						}
					}
					break;
				case ANTENNA_TAGS_NOT_IDENTIFIED:
					readPoints = readerDevice.getAllReadPoints();
					for (int i = 0; i < readPoints.length; i++) {
						if (readPoints[i] instanceof AntennaReadPoint) {
							value += ((AntennaReadPoint) readPoints[i])
									.getFailedIdentificationCount();
						}
					}
					break;
				case ANTENNA_MEMORY_READ_FAILURES:
					readPoints = readerDevice.getAllReadPoints();
					for (int i = 0; i < readPoints.length; i++) {
						if (readPoints[i] instanceof AntennaReadPoint) {
							value += ((AntennaReadPoint) readPoints[i])
									.getFailedMemReadCount();
						}
					}
					break;
				case ANTENNA_WRITE_OPERATIONS:
					// TODO: Remove the comment operators and the code
					// of this case statement after the region commented
					// out (except the break statement).
//					readPoints = readerDevice.getAllReadPoints();
//					for (int i = 0; i < readPoints.length; i++) {
//						if (readPoints[i] instanceof AntennaReadPoint) {
//							value += ((AntennaReadPoint) readPoints[i])
//									.getWriteCount();
//						}
//					}
					sources = readerDevice.getAllSources();
					for (int i = 0; i < sources.length; i++) {
						value += sources[i].getAntennaReadPointWriteCount();
					}
					break;
				case ANTENNA_WRITE_FAILURES:
					readPoints = readerDevice.getAllReadPoints();
					for (int i = 0; i < readPoints.length; i++) {
						if (readPoints[i] instanceof AntennaReadPoint) {
							value += ((AntennaReadPoint) readPoints[i])
									.getFailedWriteCount();
						}
					}
					break;
				case ANTENNA_KILL_OPERATIONS:
					// TODO: Remove the comment operators and the code
					// of this case statement after the region commented
					// out (except the break statement).
//					readPoints = readerDevice.getAllReadPoints();
//					for (int i = 0; i < readPoints.length; i++) {
//						if (readPoints[i] instanceof AntennaReadPoint) {
//							value += ((AntennaReadPoint) readPoints[i])
//									.getKillCount();
//						}
//					}
					sources = readerDevice.getAllSources();
					for (int i = 0; i < sources.length; i++) {
						value += sources[i].getAntennaReadPointKillCount();
					}
					break;
				case ANTENNA_KILL_FAILURES:
					readPoints = readerDevice.getAllReadPoints();
					for (int i = 0; i < readPoints.length; i++) {
						if (readPoints[i] instanceof AntennaReadPoint) {
							value += ((AntennaReadPoint) readPoints[i])
									.getFailedKillCount();
						}
					}
					break;
				case ANTENNA_ERASE_OPERATIONS:
					readPoints = readerDevice.getAllReadPoints();
					for (int i = 0; i < readPoints.length; i++) {
						if (readPoints[i] instanceof AntennaReadPoint) {
							value += ((AntennaReadPoint) readPoints[i])
									.getEraseCount();
						}
					}
					break;
				case ANTENNA_LOCK_OPERATIONS:
					readPoints = readerDevice.getAllReadPoints();
					for (int i = 0; i < readPoints.length; i++) {
						if (readPoints[i] instanceof AntennaReadPoint) {
							value += ((AntennaReadPoint) readPoints[i])
									.getLockCount();
						}
					}
					break;
				case ANTENNA_LOCK_FAILURES:
					readPoints = readerDevice.getAllReadPoints();
					for (int i = 0; i < readPoints.length; i++) {
						if (readPoints[i] instanceof AntennaReadPoint) {
							value += ((AntennaReadPoint) readPoints[i])
									.getFailedLockCount();
						}
					}
					break;
				case SOURCE_UNKNOWN_TO_GLIMPSED:
					sources = readerDevice.getAllSources();
					for (int i = 0; i < sources.length; i++) {
						value += sources[i].getUnknownToGlimpsedCount();
					}
					break;
				case SOURCE_GLIMPSED_TO_UNKNOWN:
					sources = readerDevice.getAllSources();
					for (int i = 0; i < sources.length; i++) {
						value += sources[i].getGlimpsedToUnknownCount();
					}
					break;
				case SOURCE_GLIMPSED_TO_OBSERVED:
					sources = readerDevice.getAllSources();
					for (int i = 0; i < sources.length; i++) {
						value += sources[i].getGlimpsedToObservedCount();
					}
					break;
				case SOURCE_OBSERVED_TO_LOST:
					sources = readerDevice.getAllSources();
					for (int i = 0; i < sources.length; i++) {
						value += sources[i].getObservedToLostCount();
					}
					break;
				case SOURCE_LOST_TO_GLIMPSED:
					sources = readerDevice.getAllSources();
					for (int i = 0; i < sources.length; i++) {
						value += sources[i].getLostToGlimpsedCount();
					}
					break;
				case SOURCE_LOST_TO_UNKNOWN:
					sources = readerDevice.getAllSources();
					for (int i = 0; i < sources.length; i++) {
						value += sources[i].getLostToUnknownCount();
					}
					break;
				case TRIGGER_MATCHES:
					Trigger[] triggers = readerDevice.getAllTriggers();
					for (int i = 0; i < triggers.length; i++) {
						value += triggers[i].getFireCount();
					}
					break;
				case ANTENNA_MEMORY_READ_OPERATIONS:
					// TODO: Remove the comment operators and the code
					// of this case statement after the region commented
					// out (except the break statement).
//					readPoints = readerDevice.getAllReadPoints();
//					for (int i = 0; i < readPoints.length; i++) {
//						if (readPoints[i] instanceof AntennaReadPoint) {
//							value += ((AntennaReadPoint) readPoints[i])
//									.getMemReadCount();
//						}
//					}
					sources = readerDevice.getAllSources();
					for (int i = 0; i < sources.length; i++) {
						value += sources[i].getAntennaReadPointMemReadCount();
					}
					break;
				case ANTENNA_ERASE_FAILURES:
					readPoints = readerDevice.getAllReadPoints();
					for (int i = 0; i < readPoints.length; i++) {
						if (readPoints[i] instanceof AntennaReadPoint) {
							value += ((AntennaReadPoint) readPoints[i])
									.getFailedEraseCount();
						}
					}
					break;

			}
			if (!new Integer32(value).equals(row.getValue(EpcglobalReaderMib.idxEpcgGlobalCountersData))) {
				System.out.println(type);
			}
			assertEquals(new Integer32(value), row.getValue(EpcglobalReaderMib.idxEpcgGlobalCountersData));

		}
	}

	/**
	 * Runs the test using the gui runner.
	 * @param args No arguments
	 */
	public static void main(String[] args) {
        junit.swingui.TestRunner.run(EpcgGlobalCountersTableRowTest.class);
    }

}
