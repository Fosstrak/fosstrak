package org.fosstrak.reader.rprm.core;

import junit.framework.TestCase;

import org.fosstrak.reader.rprm.core.AntennaReadPoint;
import org.fosstrak.reader.rprm.core.ReadPoint;
import org.fosstrak.reader.rprm.core.ReaderDevice;
import org.fosstrak.reader.rprm.core.ReaderProtocolException;
import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.SnmpAgent;
import org.fosstrak.reader.rprm.core.msg.MessageLayer;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * Tests for the class <code>org.fosstrak.reader.AntennaReadPoint</code>.
 */
public class AntennaReadPointTest extends TestCase {

	private AntennaReadPoint antReadPoint;

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

		ReadPoint[] readPoints = readerDevice.getAllReadPoints();
		for (int i = 0; i < readPoints.length; i++) {
			if (readPoints[i] instanceof AntennaReadPoint)
			antReadPoint = (AntennaReadPoint) readPoints[i];
			return;
		}
		fail();
	}

	/**
	 * Does the cleanup.
	 * @exception Exception An error occurred
	 */
	protected final void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Tests the <code>create()</code> method.
	 */
	public final void testCreate() {
		String name = "AntennaReadPointTestTestCreateAntReadPoint";
		int readPointCount = readerDevice.getAllReadPoints().length;
		try {
			AntennaReadPoint antReadPoint = AntennaReadPoint.create(name, readerDevice, null);
			assertEquals(readPointCount + 1, readerDevice.getAllReadPoints().length);
			readerDevice.getReadPoints().remove(antReadPoint.getName());
		} catch (ReaderProtocolException rpe) {
			assertEquals(readPointCount, readerDevice.getAllReadPoints().length);
		}
	}

	/**
	 * Tests the <code>resetCounters()</code> method.
	 */
	public final void testResetCounters() {
		antReadPoint.increaseIdentificationCount();
		antReadPoint.increaseMemReadCount();
		antReadPoint.increaseWriteCount();
		antReadPoint.increaseKillCount();
		antReadPoint.increaseEraseCount();
		antReadPoint.increaseLockCount();
		antReadPoint.increaseFailedIdentificationCount();
		antReadPoint.memReadFailureOccurred();
		antReadPoint.writeFailureOccurred();
		antReadPoint.killFailureOccurred();
		antReadPoint.eraseFailureOccurred();
		antReadPoint.lockFailureOccurred();

		assertEquals(1, antReadPoint.getIdentificationCount());
		assertEquals(1, antReadPoint.getMemReadCount());
		assertEquals(1, antReadPoint.getWriteCount());
		assertEquals(1, antReadPoint.getKillCount());
		assertEquals(1, antReadPoint.getEraseCount());
		assertEquals(1, antReadPoint.getLockCount());
		assertEquals(1, antReadPoint.getFailedIdentificationCount());
		assertEquals(1, antReadPoint.getFailedMemReadCount());
		assertEquals(1, antReadPoint.getFailedWriteCount());
		assertEquals(1, antReadPoint.getFailedKillCount());
		assertEquals(1, antReadPoint.getFailedEraseCount());
		assertEquals(1, antReadPoint.getFailedLockCount());

		antReadPoint.resetCounters();

		assertEquals(0, antReadPoint.getIdentificationCount());
		assertEquals(0, antReadPoint.getMemReadCount());
		assertEquals(0, antReadPoint.getWriteCount());
		assertEquals(0, antReadPoint.getKillCount());
		assertEquals(0, antReadPoint.getEraseCount());
		assertEquals(0, antReadPoint.getLockCount());
		assertEquals(0, antReadPoint.getFailedIdentificationCount());
		assertEquals(0, antReadPoint.getFailedMemReadCount());
		assertEquals(0, antReadPoint.getFailedWriteCount());
		assertEquals(0, antReadPoint.getFailedKillCount());
		assertEquals(0, antReadPoint.getFailedEraseCount());
		assertEquals(0, antReadPoint.getFailedLockCount());
	}

	/**
	 * Tests the <code>memReadFailureOccurred()</code> method.
	 */
	public final void testMemReadFailureOccurred() {
		int value = antReadPoint.getFailedMemReadCount();
		antReadPoint.memReadFailureOccurred();
		assertEquals(value + 1, antReadPoint.getFailedMemReadCount());
	}

	/**
	 * Tests the <code>writeFailureOccurred()</code> method.
	 */
	public final void testWriteFailureOccurred() {
		int value = antReadPoint.getFailedWriteCount();
		antReadPoint.writeFailureOccurred();
		assertEquals(value + 1, antReadPoint.getFailedWriteCount());
	}

	/**
	 * Tests the <code>killFailureOccurred()</code> method.
	 */
	public final void testKillFailureOccurred() {
		int value = antReadPoint.getFailedKillCount();
		antReadPoint.killFailureOccurred();
		assertEquals(value + 1, antReadPoint.getFailedKillCount());
	}

	/**
	 * Tests the <code>eraseFailureOccurred()</code> method.
	 */
	public final void testEraseFailureOccurred() {
		int value = antReadPoint.getFailedEraseCount();
		antReadPoint.eraseFailureOccurred();
		assertEquals(value + 1, antReadPoint.getFailedEraseCount());
	}

	/**
	 * Tests the <code>lockFailureOccurred()</code> method.
	 */
	public final void testLockFailureOccurred() {
		int value = antReadPoint.getFailedLockCount();
		antReadPoint.lockFailureOccurred();
		assertEquals(value + 1, antReadPoint.getFailedLockCount());
	}

	/**
	 * Tests the <code>increaseIdentificationCount()</code> method.
	 */
	public final void testIncreaseIdentificationCount() {
		int value = antReadPoint.getIdentificationCount();
		antReadPoint.increaseIdentificationCount();
		assertEquals(value + 1, antReadPoint.getIdentificationCount());
	}

	/**
	 * Tests the <code>increaseFailedIdentificationCount()</code> method.
	 */
	public final void testIncreaseFailedIdentificationCount() {
		int value = antReadPoint.getFailedIdentificationCount();
		antReadPoint.increaseFailedIdentificationCount();
		assertEquals(value + 1, antReadPoint.getFailedIdentificationCount());
	}

	/**
	 * Tests the <code>increaseMemReadCount()</code> method.
	 */
	public final void testIncreaseMemReadCount() {
		int value = antReadPoint.getMemReadCount();
		antReadPoint.increaseMemReadCount();
		assertEquals(value + 1, antReadPoint.getMemReadCount());
	}

	/**
	 * Tests the <code>increaseWriteCount()</code> method.
	 */
	public final void testIncreaseWriteCount() {
		int value = antReadPoint.getWriteCount();
		antReadPoint.increaseWriteCount();
		assertEquals(value + 1, antReadPoint.getWriteCount());
	}

	/**
	 * Tests the <code>increaseKillCount()</code> method.
	 */
	public final void testIncreaseKillCount() {
		int value = antReadPoint.getKillCount();
		antReadPoint.increaseKillCount();
		assertEquals(value + 1, antReadPoint.getKillCount());
	}

	/**
	 * Tests the <code>increaseEraseCount()</code> method.
	 */
	public final void testIncreaseEraseCount() {
		int value = antReadPoint.getEraseCount();
		antReadPoint.increaseEraseCount();
		assertEquals(value + 1, antReadPoint.getEraseCount());
	}

	/**
	 * Tests the <code>increaseLockCount()</code> method.
	 */
	public final void testIncreaseLockCount() {
		int value = antReadPoint.getLockCount();
		antReadPoint.increaseLockCount();
		assertEquals(value + 1, antReadPoint.getLockCount());
	}

	/**
	 * Runs the test using the gui runner.
	 * @param args No arguments
	 */
	public static void main(String[] args) {
        junit.swingui.TestRunner.run(AntennaReadPointTest.class);
    }

}
