package org.accada.reader.rprm.core.mgmt.alarm;

import junit.framework.TestCase;

import org.accada.reader.rprm.core.NotificationChannel;
import org.accada.reader.rprm.core.ReadPoint;
import org.accada.reader.rprm.core.ReaderDevice;
import org.accada.reader.rprm.core.Source;
import org.accada.reader.rprm.core.mgmt.IOPort;
import org.accada.reader.rprm.core.mgmt.OperationalStatus;
import org.accada.reader.rprm.core.mgmt.agent.snmp.SnmpAgent;
import org.accada.reader.rprm.core.mgmt.alarm.Alarm;
import org.accada.reader.rprm.core.mgmt.alarm.AlarmLevel;
import org.accada.reader.rprm.core.mgmt.alarm.FreeMemoryAlarm;
import org.accada.reader.rprm.core.mgmt.alarm.IOPortOperStatusAlarm;
import org.accada.reader.rprm.core.mgmt.alarm.NotificationChannelOperStatusAlarm;
import org.accada.reader.rprm.core.mgmt.alarm.ReadPointOperStatusAlarm;
import org.accada.reader.rprm.core.mgmt.alarm.ReaderDeviceOperStatusAlarm;
import org.accada.reader.rprm.core.mgmt.alarm.SourceOperStatusAlarm;
import org.accada.reader.rprm.core.msg.MessageLayer;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * Tests for the class <code>org.accada.reader.mgmt.alarm.Alarm</code>.
 */
public class AlarmTest extends TestCase {

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
		readerDevice.resetStatistics();
	}

	/**
	 * Does the cleanup.
	 * @exception Exception An error occurred
	 */
	protected final void tearDown() throws Exception {
		super.tearDown();

		readerDevice.resetStatistics();
	}

	/**
	 * Tests the <code>suppress()</code> method.
	 */
	public final void testSuppress() {
		ReadPoint readPoint = null;
		if (readerDevice.getAllReadPoints().length > 0) {
			readPoint = readerDevice.getAllReadPoints()[0];
		}
		Source source = readerDevice.getCurrentSource();
		NotificationChannel notifChan = null;
		if (readerDevice.getAllNotificationChannels().length > 0) {
			notifChan = readerDevice.getAllNotificationChannels()[0];
		}
		IOPort ioPort = null;
		if (readerDevice.getAllIOPorts().length > 0) {
			ioPort = readerDevice.getAllIOPorts()[0];
		}

		Alarm alarm = new Alarm("Alarm", AlarmLevel.ERROR, readerDevice);
		alarm.suppress();
		assertEquals(1, alarm.getSuppressCount());

		ReaderDeviceOperStatusAlarm readerDeviceOperStatusAlarm = new ReaderDeviceOperStatusAlarm("ReaderDeviceOperStatusAlarm", AlarmLevel.ERROR, readerDevice, OperationalStatus.UP, OperationalStatus.DOWN);
		readerDeviceOperStatusAlarm.suppress();
		assertEquals(1, readerDeviceOperStatusAlarm.getSuppressCount());
		assertEquals(1, ReaderDevice.getOperStateSuppressions());

		FreeMemoryAlarm freeMemoryAlarm = new FreeMemoryAlarm("FreeMemoryAlarm", AlarmLevel.ERROR, readerDevice);
		freeMemoryAlarm.suppress();
		assertEquals(1, freeMemoryAlarm.getSuppressCount());
		assertEquals(1, ReaderDevice.getMemStateSuppressions());

		if (readPoint != null) {
			ReadPointOperStatusAlarm readPointOperStatusAlarm = new ReadPointOperStatusAlarm("ReadPointOperStatusAlarm", AlarmLevel.ERROR, readerDevice, OperationalStatus.UP, OperationalStatus.DOWN, readPoint.getName());
			readPointOperStatusAlarm.suppress();
			assertEquals(1, readPointOperStatusAlarm.getSuppressCount());
			assertEquals(1, readPoint.getOperStateSuppressions());
		}

		if (ioPort != null) {
			IOPortOperStatusAlarm ioPortOperStatusAlarm = new IOPortOperStatusAlarm("IOPortOperStatusAlarm", AlarmLevel.ERROR, readerDevice, OperationalStatus.UP, OperationalStatus.DOWN, ioPort.getName());
			ioPortOperStatusAlarm.suppress();
			assertEquals(1, ioPortOperStatusAlarm.getSuppressCount());
			assertEquals(1, ioPort.getOperStateSuppressions());
		}

		SourceOperStatusAlarm sourceOperStatusAlarm = new SourceOperStatusAlarm("SourceOperStatusAlarm", AlarmLevel.ERROR, readerDevice, OperationalStatus.UP, OperationalStatus.DOWN, source.getName());
		sourceOperStatusAlarm.suppress();
		assertEquals(1, sourceOperStatusAlarm.getSuppressCount());
		assertEquals(1, source.getOperStateSuppressions());

		if (notifChan != null) {
			NotificationChannelOperStatusAlarm notificationChannelOperStatusAlarm = new NotificationChannelOperStatusAlarm("NotificationChannelOperStatusAlarm", AlarmLevel.ERROR, readerDevice, OperationalStatus.UP, OperationalStatus.DOWN, notifChan.getName());
			notificationChannelOperStatusAlarm.suppress();
			assertEquals(1, notificationChannelOperStatusAlarm.getSuppressCount());
			assertEquals(1, notifChan.getOperStateSuppressions());
		}
	}

	/**
	 * Runs the test using the gui runner.
	 * @param args No arguments
	 */
	public static void main(String[] args) {
        junit.swingui.TestRunner.run(AlarmTest.class);
    }

}
