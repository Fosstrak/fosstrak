package org.accada.reader.rprm.core;

import java.net.MalformedURLException;
import java.util.Enumeration;

import junit.framework.TestCase;

import org.accada.reader.rprm.core.AntennaReadPoint;
import org.accada.reader.rprm.core.NotificationChannel;
import org.accada.reader.rprm.core.ReadPoint;
import org.accada.reader.rprm.core.ReaderDevice;
import org.accada.reader.rprm.core.ReaderProtocolException;
import org.accada.reader.rprm.core.Source;
import org.accada.reader.rprm.core.Trigger;
import org.accada.reader.rprm.core.TriggerType;
import org.accada.reader.rprm.core.mgmt.IOPort;
import org.accada.reader.rprm.core.mgmt.agent.snmp.SnmpAgent;
import org.accada.reader.rprm.core.mgmt.alarm.AlarmChannel;
import org.accada.reader.rprm.core.msg.Address;
import org.accada.reader.rprm.core.msg.MessageLayer;
import org.apache.log4j.PropertyConfigurator;

/**
 * Tests for the class <code>org.accada.reader.ReaderDevice</code>.
 */
public class ReaderDeviceTest extends TestCase {

	private ReaderDevice readerDevice;

	private AntennaReadPoint antReadPoint;
	private Source source;
	private Trigger trigger;

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

		readerDevice = ReaderDevice.getInstance();

		ReadPoint[] readPoints = readerDevice.getAllReadPoints();
		for (int i = 0; i < readPoints.length; i++) {
			if (readPoints[i] instanceof AntennaReadPoint)
			antReadPoint = (AntennaReadPoint) readPoints[i];
			break;
		}
		source = Source.create("ReaderDeviceTestSource", readerDevice);
		trigger = Trigger.create("ReaderDeviceTestTrigger", TriggerType.TIMER, "ms=2500", readerDevice);
	}

	/**
	 * Does the cleanup.
	 * @exception Exception An error occurred
	 */
	protected final void tearDown() throws Exception {
		super.tearDown();

		readerDevice.removeSources(new Source[] { source });
		readerDevice.removeTriggers(new Trigger[] { trigger });
	}

	/**
	 * Tests the <code>getIOPort()</code> method.
	 */
	public final void testGetIOPort() {
		try {
			readerDevice.getIOPort("ReaderDeviceTestIOPort");
			fail();
		} catch (ReaderProtocolException rpe) {
			IOPort[] ioPorts = readerDevice.getAllIOPorts();
			if (ioPorts.length > 0) {
				String name = ioPorts[0].getName();
				try {
					readerDevice.getIOPort(name);
				} catch (ReaderProtocolException rpe2) {
					fail();
				}
			}
		}
	}

	/**
	 * Tests the <code>resetStatistics()</code> method.
	 */
	public final void testResetStatistics() {
		Enumeration iter;

		readerDevice.resetStatistics();
		iter = readerDevice.getReadPoints().elements();
		while (iter.hasMoreElements()) {
			ReadPoint readPoint = (ReadPoint) iter.nextElement();
			if (readPoint instanceof AntennaReadPoint) {
				AntennaReadPoint curAntReadPoint = (AntennaReadPoint) readPoint;
				assertEquals(0, curAntReadPoint.getIdentificationCount());
				assertEquals(0, curAntReadPoint.getMemReadCount());
				assertEquals(0, curAntReadPoint.getWriteCount());
				assertEquals(0, curAntReadPoint.getKillCount());
				assertEquals(0, curAntReadPoint.getEraseCount());
				assertEquals(0, curAntReadPoint.getLockCount());
				assertEquals(0, curAntReadPoint.getFailedIdentificationCount());
				assertEquals(0, curAntReadPoint.getFailedMemReadCount());
				assertEquals(0, curAntReadPoint.getFailedWriteCount());
				assertEquals(0, curAntReadPoint.getFailedKillCount());
				assertEquals(0, curAntReadPoint.getFailedEraseCount());
				assertEquals(0, curAntReadPoint.getFailedLockCount());
			}
		}

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

		// the code below results in a java.util.NoSuchElementException since there are no notification channels defined
		// commented out by CF
		/*
		NotificationChannel notifChan = (NotificationChannel) readerDevice.getNotificationChannels().elements().nextElement();
		notifChan.increaseOperStateSuppressions();
		assertTrue(notifChan.getOperStateSuppressions() > 0);
		*/
		readerDevice.resetStatistics();

		iter = readerDevice.getReadPoints().elements();
		while (iter.hasMoreElements()) {
			ReadPoint readPoint = (ReadPoint) iter.nextElement();
			if (readPoint instanceof AntennaReadPoint) {
				AntennaReadPoint curAntReadPoint = (AntennaReadPoint) readPoint;
				assertEquals(0, curAntReadPoint.getIdentificationCount());
				assertEquals(0, curAntReadPoint.getMemReadCount());
				assertEquals(0, curAntReadPoint.getWriteCount());
				assertEquals(0, curAntReadPoint.getKillCount());
				assertEquals(0, curAntReadPoint.getEraseCount());
				assertEquals(0, curAntReadPoint.getLockCount());
				assertEquals(0, curAntReadPoint.getFailedIdentificationCount());
				assertEquals(0, curAntReadPoint.getFailedMemReadCount());
				assertEquals(0, curAntReadPoint.getFailedWriteCount());
				assertEquals(0, curAntReadPoint.getFailedKillCount());
				assertEquals(0, curAntReadPoint.getFailedEraseCount());
				assertEquals(0, curAntReadPoint.getFailedLockCount());
			}
		}

		iter = readerDevice.getSources().elements();
		while (iter.hasMoreElements()) {
			Source curSource = (Source) iter.nextElement();
			assertEquals(0, curSource.getUnknownToGlimpsedCount());
			assertEquals(0, curSource.getGlimpsedToUnknownCount());
			assertEquals(0, curSource.getGlimpsedToObservedCount());
			assertEquals(0, curSource.getObservedToLostCount());
			assertEquals(0, curSource.getLostToGlimpsedCount());
			assertEquals(0, curSource.getLostToUnknownCount());
			assertEquals(0, curSource.getAntennaReadPointMemReadCount());
			assertEquals(0, curSource.getAntennaReadPointWriteCount());
			assertEquals(0, curSource.getAntennaReadPointKillCount());
		}

		iter = readerDevice.getTriggers().elements();
		while (iter.hasMoreElements()) {
			Trigger curTrigger = (Trigger) iter.nextElement();
			assertEquals(0, curTrigger.getFireCount());
		}

		// there are no notification channels defined and the initialization of the variable notifChan generates a java.util.NoSuchElementException (see above)
		// commented out by CF
		//assertEquals(0, notifChan.getOperStateSuppressions());
	}

	/**
	 * Tests the <code>removeAlarmChannels()</code> method.
	 */
	public final void testRemoveAlarmChannels() {
		try {
			int alarmChanCount = readerDevice.getAllAlarmChannels().length;
			AlarmChannel alarmChannel = AlarmChannel.create("ReaderDeviceTestAlarmChannel", new Address("udp://127.0.0.1:162"), readerDevice);
			assertEquals(alarmChanCount + 1, readerDevice.getAllAlarmChannels().length);
			readerDevice.getAlarmChannel(alarmChannel.getName());
			readerDevice.removeAlarmChannels(new AlarmChannel[] { alarmChannel });
			assertEquals(alarmChanCount, readerDevice.getAllAlarmChannels().length);
			try {
				readerDevice.getAlarmChannel(alarmChannel.getName());
				fail();
			} catch (ReaderProtocolException rpe2) {
				// ok
			}
		} catch (ReaderProtocolException rpe) {
			fail();
		} catch (MalformedURLException mue) {
			fail();
		}
	}

	/**
	 * Tests the <code>removeAllAlarmChannels()</code> method.
	 */
	public final void testRemoveAllAlarmChannels() {
		try {
			int alarmChanCount = readerDevice.getAllAlarmChannels().length;
			AlarmChannel.create("ReaderDeviceTestAlarmChannel1", new Address("udp://127.0.0.1:162"), readerDevice);
			AlarmChannel.create("ReaderDeviceTestAlarmChannel2", new Address("udp://127.0.0.2:162"), readerDevice);
			assertEquals(alarmChanCount + 2, readerDevice.getAllAlarmChannels().length);
			readerDevice.removeAllAlarmChannels();
			assertEquals(0, readerDevice.getAllAlarmChannels().length);
		} catch (ReaderProtocolException rpe) {
			fail();
		} catch (MalformedURLException mue) {
			fail();
		}
	}

	/**
	 * Tests the <code>getAlarmChannel()</code> method.
	 */
	public final void testGetAlarmChannel() {
		String name = "ReaderDeviceTestAlarmChannel";
		try {
			try {
				readerDevice.getAlarmChannel(name);
				fail();
			} catch (ReaderProtocolException rpe2) {
				// ok
			}
			AlarmChannel.create(name, new Address("udp://127.0.0.1:162"), readerDevice);
			assertEquals(name, readerDevice.getAlarmChannel(name).getName());
			readerDevice.removeAllAlarmChannels();
		} catch (ReaderProtocolException rpe) {
			fail();
		} catch (MalformedURLException mue) {
			fail();
		}
	}

	/**
	 * Tests the <code>increaseOperStateSuppressions()</code> method.
	 */
	public final void testIncreaseOperStateSuppressions() {
		int value = ReaderDevice.getOperStateSuppressions();
		ReaderDevice.increaseOperStateSuppressions();
		assertEquals(value + 1, ReaderDevice.getOperStateSuppressions());
	}

	/**
	 * Tests the <code>resetOperStateSuppressions()</code> method.
	 */
	public final void testResetOperStateSuppressions() {
		ReaderDevice.increaseOperStateSuppressions();
		assertTrue(ReaderDevice.getOperStateSuppressions() > 0);
		ReaderDevice.resetOperStateSuppressions();
		assertEquals(0, ReaderDevice.getOperStateSuppressions());
	}

	/**
	 * Tests the <code>increaseMemStateSuppressions()</code> method.
	 */
	public final void testIncreaseMemStateSuppressions() {
		int value = ReaderDevice.getMemStateSuppressions();
		ReaderDevice.increaseMemStateSuppressions();
		assertEquals(value + 1, ReaderDevice.getMemStateSuppressions());
	}

	/**
	 * Tests the <code>resetMemStateSuppressions()</code> method.
	 */
	public final void testResetMemStateSuppressions() {
		ReaderDevice.increaseMemStateSuppressions();
		assertTrue(ReaderDevice.getMemStateSuppressions() > 0);
		ReaderDevice.resetMemStateSuppressions();
		assertEquals(0, ReaderDevice.getMemStateSuppressions());
	}

	/**
	 * Runs the test using the gui runner.
	 * @param args No arguments
	 */
	public static void main(String[] args) {
        junit.swingui.TestRunner.run(ReaderDeviceTest.class);
    }

}
