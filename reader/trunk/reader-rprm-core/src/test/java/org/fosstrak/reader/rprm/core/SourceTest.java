package org.accada.reader.rprm.core;

import java.util.Enumeration;
import java.util.Vector;

import junit.framework.TestCase;

import org.accada.reader.rprm.core.AntennaReadPoint;
import org.accada.reader.rprm.core.ReadPoint;
import org.accada.reader.rprm.core.ReaderDevice;
import org.accada.reader.rprm.core.ReaderProtocolException;
import org.accada.reader.rprm.core.Source;
import org.accada.reader.rprm.core.Source.ReaderAndReadPoints;
import org.accada.reader.hal.HardwareAbstraction;
import org.accada.reader.rprm.core.mgmt.AdministrativeStatus;
import org.accada.reader.rprm.core.mgmt.OperationalStatus;
import org.accada.reader.rprm.core.mgmt.agent.snmp.SnmpAgent;
import org.accada.reader.rprm.core.msg.MessageLayer;
import org.apache.log4j.PropertyConfigurator;

/**
 * Tests for the class <code>org.accada.reader.Source</code>.
 */
public class SourceTest extends TestCase {

	private Source source;
	HardwareAbstraction hal = null;
	private ReadPoint readPoint1;
	private ReadPoint readPoint2;
	private ReadPoint readPoint3;

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

		source = Source.create("SourceTestSource", readerDevice);
		ReadPoint[] readPoints = readerDevice.getAllReadPoints();
		if (readPoints.length > 0) {
			hal = readPoints[0].getReader();
		}
		if (hal != null) {
			readPoint1 = AntennaReadPoint.create("SourceTestReadPoint1", readerDevice, hal);
			readPoint2 = AntennaReadPoint.create("SourceTestReadPoint2", readerDevice, hal);
			readPoint3 = AntennaReadPoint.create("SourceTestReadPoint3", readerDevice, hal);
			source.addReadPoints(new ReadPoint[] { readPoint1, readPoint2, readPoint3 });
		}
	}

	/**
	 * Does the cleanup.
	 * @exception Exception An error occurred
	 */
	protected final void tearDown() throws Exception {
		super.tearDown();

		readerDevice.removeSources(new Source[] { source });
		if (hal != null) {
			readerDevice.getReadPoints().remove(readPoint1.getName());
			readerDevice.getReadPoints().remove(readPoint2.getName());
			readerDevice.getReadPoints().remove(readPoint3.getName());
		}
	}

	/**
	 * Tests the <code>getOperStatus()</code> method.
	 */
	public final void testGetOperStatus() {
		OperationalStatus operStatus;
		int up = 0;
		int down = 0;
		int other = 0;
		ReadPoint[] readPoints = source.getAllReadPoints();
		for (int i = 0; i < readPoints.length; i++) {
			switch (readPoints[i].getOperStatus()) {
				case UP:
					up++;
					break;
				case DOWN:
					down++;
					break;
				case OTHER:
					other++;
					break;
			}
		}
		if ((other > 0) || ((up > 0) && (down > 0)))
			operStatus = OperationalStatus.OTHER;
		else if ((up > 0) && (down == 0))
			operStatus = OperationalStatus.UP;
		else if ((up == 0) && (down > 0))
			operStatus = OperationalStatus.DOWN;
		else
			operStatus = OperationalStatus.UNKNOWN;
		assertEquals(operStatus, source.getOperStatus());
	}

	/**
	 * Tests the <code>setAdminStatus()</code> method.
	 */
	public final void testSetAdminStatus() {
		ReadPoint[] readPoints = source.getAllReadPoints();
		AdministrativeStatus adminStatus;

		adminStatus = AdministrativeStatus.UP;
		source.setAdminStatus(adminStatus);
		for (int i = 0; i < readPoints.length; i++) {
			assertEquals(adminStatus, readPoints[i].getAdminStatus());
		}

		adminStatus = AdministrativeStatus.DOWN;
		source.setAdminStatus(adminStatus);
		for (int i = 0; i < readPoints.length; i++) {
			assertEquals(adminStatus, readPoints[i].getAdminStatus());
		}
	}

	/**
	 * Tests the <code>supportsWriteOperations()</code> method.
	 */
	public final void testSupportsWriteOperations() {
		try {
			Vector closure = source.getReaderAndReadPoints();
			Enumeration readerIterator = closure.elements();
			HardwareAbstraction curHardwareAbstraction;
			while (readerIterator.hasMoreElements()) {
				curHardwareAbstraction = ((ReaderAndReadPoints) readerIterator.nextElement()).getReader();
				if (curHardwareAbstraction.supportsWriteBytes() || curHardwareAbstraction.supportsProgramId()) {
					assertTrue(source.supportsWriteOperations());
					return;
				}
			}
			assertFalse(source.supportsWriteOperations());
		} catch (ReaderProtocolException rpe) {
			fail();
		}
	}

	/**
	 * Tests the <code>resetCounters()</code> method.
	 */
	public final void testResetCounters() {
		source.resetCounters();

		assertEquals(0, source.getUnknownToGlimpsedCount());
		assertEquals(0, source.getGlimpsedToUnknownCount());
		assertEquals(0, source.getGlimpsedToObservedCount());
		assertEquals(0, source.getObservedToLostCount());
		assertEquals(0, source.getLostToGlimpsedCount());
		assertEquals(0, source.getLostToUnknownCount());

		assertEquals(0, source.getAntennaReadPointMemReadCount());
		assertEquals(0, source.getAntennaReadPointWriteCount());
		assertEquals(0, source.getAntennaReadPointKillCount());
	}

	/**
	 * Tests the <code>increaseOperStateSuppressions()</code> method.
	 */
	public final void testIncreaseOperStateSuppressions() {
		int value = source.getOperStateSuppressions();
		source.increaseOperStateSuppressions();
		assertEquals(value + 1, source.getOperStateSuppressions());
	}

	/**
	 * Tests the <code>resetOperStateSuppressions()</code> method.
	 */
	public final void testResetOperStateSuppressions() {
		source.increaseOperStateSuppressions();
		assertTrue(source.getOperStateSuppressions() > 0);
		source.resetOperStateSuppressions();
		assertEquals(0, source.getOperStateSuppressions());
	}

	/**
	 * Runs the test using the gui runner.
	 * @param args No arguments
	 */
	public static void main(String[] args) {
        junit.swingui.TestRunner.run(SourceTest.class);
    }

}
