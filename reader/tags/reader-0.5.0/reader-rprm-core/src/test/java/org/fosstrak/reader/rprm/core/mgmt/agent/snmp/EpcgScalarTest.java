package org.fosstrak.reader.rprm.core.mgmt.agent.snmp;

import junit.framework.TestCase;

import org.fosstrak.reader.rprm.core.AntennaReadPoint;
import org.fosstrak.reader.rprm.core.ReadPoint;
import org.fosstrak.reader.rprm.core.ReaderDevice;
import org.fosstrak.reader.rprm.core.mgmt.OperationalStatus;
import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.EpcgScalar;
import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.EpcgScalarType;
import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.SnmpAgent;
import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.mib.EpcglobalReaderMib;
import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.table.SnmpTable;
import org.fosstrak.reader.rprm.core.mgmt.agent.snmp.table.SnmpTable.TableTypeEnum;
import org.fosstrak.reader.rprm.core.mgmt.alarm.AlarmLevel;
import org.fosstrak.reader.rprm.core.mgmt.util.SnmpUtil;
import org.fosstrak.reader.rprm.core.msg.MessageLayer;
import org.apache.log4j.xml.DOMConfigurator;
import org.snmp4j.agent.mo.MOAccessImpl;
import org.snmp4j.smi.Counter32;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UnsignedInteger32;

/**
 * Tests for the class <code>org.fosstrak.reader.mgmt.agent.snmp.EpcgScalar</code>.
 */
public class EpcgScalarTest extends TestCase {

	private ReaderDevice readerDevice;

	private EpcgScalar epcgRdrDevDescription;
	private EpcgScalar epcgRdrDevRole;
	private EpcgScalar epcgRdrDevEpc;
	private EpcgScalar epcgRdrDevSerialNumber;
//	private EpcgScalar epcgRdrDevTimeUtc;
	private EpcgScalar epcgRdrDevCurrentSource;
	private EpcgScalar epcgRdrDevReboot;
	private EpcgScalar epcgRdrDevResetStatistics;
//	private EpcgScalar epcgRdrDevResetTimestamp;
	private EpcgScalar epcgRdrDevNormalizePowerLevel;
	private EpcgScalar epcgRdrDevNormalizeNoiseLevel;
	private EpcgScalar epcgRdrDevOperStatus;
//	private EpcgScalar epcgRdrDevOperStatusPrior;
	private EpcgScalar epcgRdrDevOperStateEnable;
	private EpcgScalar epcgRdrDevOperNotifFromState;
	private EpcgScalar epcgRdrDevOperNotifToState;
	private EpcgScalar epcgRdrDevOperNotifStateLevel;
	private EpcgScalar epcgRdrDevOperStateSuppressInterval;
	private EpcgScalar epcgRdrDevOperStateSuppressions;
//	private EpcgScalar epcgRdrDevFreeMemory;
	private EpcgScalar epcgRdrDevFreeMemoryNotifEnable;
	private EpcgScalar epcgRdrDevFreeMemoryNotifLevel;
	private EpcgScalar epcgRdrDevFreeMemoryOnsetThreshold;
	private EpcgScalar epcgRdrDevFreeMemoryAbateThreshold;
	private EpcgScalar epcgRdrDevFreeMemoryStatus;
	private EpcgScalar epcgRdrDevMemStateSuppressInterval;
	private EpcgScalar epcgRdrDevMemStateSuppressions;
//	private EpcgScalar epcgReadPointPriorOperStatus;
//	private EpcgScalar epcgReadPointOperStateSuppressInterval;
	private EpcgScalar epcgReadPointOperStateSuppressions;
//	private EpcgScalar epcgAntRdPntSuppressInterval;
//	private EpcgScalar epcgIoPortOperStatusPrior;
//	private EpcgScalar epcgIoPortOperStateSuppressInterval;
	private EpcgScalar epcgIoPortOperStateSuppressions;
//	private EpcgScalar epcgSrcOperPriorStatus;
//	private EpcgScalar epcgSrcOperStateSuppressInterval;
	private EpcgScalar epcgSrcOperStateSuppressions;
//	private EpcgScalar epcgNotifChanOperStatusPrior;
//	private EpcgScalar epcgNotifChanOperStateSuppressInterval;
	private EpcgScalar epcgNotifChanOperStateSuppressions;
	private EpcgScalar sysDescr;
	private EpcgScalar sysLocation;
	private EpcgScalar sysContact;
//	private EpcgScalar sysUpTime;
	private EpcgScalar sysName;

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

		epcgRdrDevDescription = new EpcgScalar(EpcgScalarType.EPCG_RDR_DEV_DESCRIPTION, MOAccessImpl.ACCESS_READ_ONLY, null, readerDevice);
		epcgRdrDevRole = new EpcgScalar(EpcgScalarType.EPCG_RDR_DEV_ROLE, MOAccessImpl.ACCESS_READ_ONLY, null, readerDevice);
		epcgRdrDevEpc = new EpcgScalar(EpcgScalarType.EPCG_RDR_DEV_EPC, MOAccessImpl.ACCESS_READ_ONLY, null, readerDevice);
		epcgRdrDevSerialNumber = new EpcgScalar(EpcgScalarType.EPCG_RDR_DEV_SERIAL_NUMBER, MOAccessImpl.ACCESS_READ_ONLY, null, readerDevice);
//		epcgRdrDevTimeUtc = new EpcgScalar(EpcgScalarType.EPCG_RDR_DEV_TIME_UTC, MOAccessImpl.ACCESS_READ_ONLY, null, readerDevice);
		epcgRdrDevCurrentSource = new EpcgScalar(EpcgScalarType.EPCG_RDR_DEV_CURRENT_SOURCE, MOAccessImpl.ACCESS_READ_ONLY, null, readerDevice);
		epcgRdrDevReboot = new EpcgScalar(EpcgScalarType.EPCG_RDR_DEV_REBOOT, MOAccessImpl.ACCESS_READ_WRITE, null, readerDevice);
		epcgRdrDevResetStatistics = new EpcgScalar(EpcgScalarType.EPCG_RDR_DEV_RESET_STATISTICS, MOAccessImpl.ACCESS_READ_WRITE, null, readerDevice);
//		epcgRdrDevResetTimestamp = new EpcgScalar(EpcgScalarType.EPCG_RDR_DEV_RESET_TIMESTAMP, MOAccessImpl.ACCESS_READ_ONLY, new OctetString(""), readerDevice);
		epcgRdrDevNormalizePowerLevel = new EpcgScalar(EpcgScalarType.EPCG_RDR_DEV_NORMALIZE_POWER_LEVEL, MOAccessImpl.ACCESS_READ_ONLY, null, readerDevice);
		epcgRdrDevNormalizeNoiseLevel = new EpcgScalar(EpcgScalarType.EPCG_RDR_DEV_NORMALIZE_NOISE_LEVEL, MOAccessImpl.ACCESS_READ_ONLY, null, readerDevice);
		epcgRdrDevOperStatus = new EpcgScalar(EpcgScalarType.EPCG_RDR_DEV_OPER_STATUS, MOAccessImpl.ACCESS_READ_ONLY, null, readerDevice);
//		epcgRdrDevOperStatusPrior = new EpcgScalar(EpcgScalarType.EPCG_RDR_DEV_OPER_STATUS_PRIOR, MOAccessImpl.ACCESS_FOR_NOTIFY, new Integer32(OperationalStatus.UNKNOWN.toInt()), readerDevice);
		epcgRdrDevOperStateEnable = new EpcgScalar(EpcgScalarType.EPCG_RDR_DEV_OPER_STATE_ENABLE, MOAccessImpl.ACCESS_READ_WRITE, null, readerDevice);
		epcgRdrDevOperNotifFromState = new EpcgScalar(EpcgScalarType.EPCG_RDR_DEV_OPER_NOTIF_FROM_STATE, MOAccessImpl.ACCESS_READ_WRITE, null, readerDevice);
		epcgRdrDevOperNotifToState = new EpcgScalar(EpcgScalarType.EPCG_RDR_DEV_OPER_NOTIF_TO_STATE, MOAccessImpl.ACCESS_READ_WRITE, null, readerDevice);
		epcgRdrDevOperNotifStateLevel = new EpcgScalar(EpcgScalarType.EPCG_RDR_DEV_OPER_NOTIF_STATE_LEVEL, MOAccessImpl.ACCESS_READ_WRITE, null, readerDevice);
		epcgRdrDevOperStateSuppressInterval = new EpcgScalar(EpcgScalarType.EPCG_RDR_DEV_OPER_STATE_SUPPRESS_INTERVAL, MOAccessImpl.ACCESS_READ_WRITE, new UnsignedInteger32(0), readerDevice);
		epcgRdrDevOperStateSuppressions = new EpcgScalar(EpcgScalarType.EPCG_RDR_DEV_OPER_STATE_SUPPRESSIONS, MOAccessImpl.ACCESS_READ_ONLY, new Counter32(0), readerDevice);
//		epcgRdrDevFreeMemory = new EpcgScalar(EpcgScalarType.EPCG_RDR_DEV_FREE_MEMORY, MOAccessImpl.ACCESS_READ_ONLY, null, readerDevice);
		epcgRdrDevFreeMemoryNotifEnable = new EpcgScalar(EpcgScalarType.EPCG_RDR_DEV_FREE_MEMORY_NOTIF_ENABLE, MOAccessImpl.ACCESS_READ_WRITE, null, readerDevice);
		epcgRdrDevFreeMemoryNotifLevel = new EpcgScalar(EpcgScalarType.EPCG_RDR_DEV_FREE_MEMORY_NOTIF_LEVEL, MOAccessImpl.ACCESS_READ_WRITE, null, readerDevice);
		epcgRdrDevFreeMemoryOnsetThreshold = new EpcgScalar(EpcgScalarType.EPCG_RDR_DEV_FREE_MEMORY_ONSET_THRESHOLD, MOAccessImpl.ACCESS_READ_WRITE, null, readerDevice);
		epcgRdrDevFreeMemoryAbateThreshold = new EpcgScalar(EpcgScalarType.EPCG_RDR_DEV_FREE_MEMORY_ABATE_THRESHOLD, MOAccessImpl.ACCESS_READ_WRITE, null, readerDevice);
		epcgRdrDevFreeMemoryStatus = new EpcgScalar(EpcgScalarType.EPCG_RDR_DEV_FREE_MEMORY_STATUS, MOAccessImpl.ACCESS_READ_ONLY, null, readerDevice);
		epcgRdrDevMemStateSuppressInterval = new EpcgScalar(EpcgScalarType.EPCG_RDR_DEV_MEM_STATE_SUPPRESS_INTERVAL, MOAccessImpl.ACCESS_READ_WRITE, new UnsignedInteger32(0), readerDevice);
		epcgRdrDevMemStateSuppressions = new EpcgScalar(EpcgScalarType.EPCG_RDR_DEV_MEM_STATE_SUPPRESSIONS, MOAccessImpl.ACCESS_READ_ONLY, new Counter32(0), readerDevice);
//		epcgReadPointPriorOperStatus = new EpcgScalar(EpcgScalarType.EPCG_READ_POINT_PRIOR_OPER_STATUS, MOAccessImpl.ACCESS_FOR_NOTIFY, new Integer32(OperationalStatus.UNKNOWN.toInt()), readerDevice);
//		epcgReadPointOperStateSuppressInterval = new EpcgScalar(EpcgScalarType.EPCG_READ_POINT_OPER_STATE_SUPPRESS_INTERVAL, MOAccessImpl.ACCESS_READ_WRITE, new UnsignedInteger32(0), readerDevice);
//		epcgReadPointOperStateSuppressions = new EpcgScalar(EpcgScalarType.EPCG_READ_POINT_OPER_STATE_SUPPRESSIONS, MOAccessImpl.ACCESS_READ_ONLY, new Counter32(0), readerDevice);
//		epcgAntRdPntSuppressInterval = new EpcgScalar(EpcgScalarType.EPCG_ANT_RD_PNT_SUPPRESS_INTERVAL, MOAccessImpl.ACCESS_READ_WRITE, new UnsignedInteger32(0), readerDevice);
//		epcgIoPortOperStatusPrior = new EpcgScalar(EpcgScalarType.EPCG_IO_PORT_OPER_STATUS_PRIOR, MOAccessImpl.ACCESS_FOR_NOTIFY, new Integer32(OperationalStatus.UNKNOWN.toInt()), readerDevice);
//		epcgIoPortOperStateSuppressInterval = new EpcgScalar(EpcgScalarType.EPCG_IO_PORT_OPER_STATE_SUPPRESS_INTERVAL, MOAccessImpl.ACCESS_READ_WRITE, new UnsignedInteger32(0), readerDevice);
//		epcgIoPortOperStateSuppressions = new EpcgScalar(EpcgScalarType.EPCG_IO_PORT_OPER_STATE_SUPPRESSIONS, MOAccessImpl.ACCESS_READ_ONLY, new Counter32(0), readerDevice);
//		epcgSrcOperPriorStatus = new EpcgScalar(EpcgScalarType.EPCG_SRC_OPER_PRIOR_STATUS, MOAccessImpl.ACCESS_FOR_NOTIFY, new Integer32(OperationalStatus.UNKNOWN.toInt()), readerDevice);
//		epcgSrcOperStateSuppressInterval = new EpcgScalar(EpcgScalarType.EPCG_SRC_OPER_STATE_SUPPRESS_INTERVAL, MOAccessImpl.ACCESS_READ_WRITE, new UnsignedInteger32(0), readerDevice);
//		epcgSrcOperStateSuppressions = new EpcgScalar(EpcgScalarType.EPCG_SRC_OPER_STATE_SUPPRESSIONS, MOAccessImpl.ACCESS_READ_ONLY, new Counter32(0), readerDevice);
//		epcgNotifChanOperStatusPrior = new EpcgScalar(EpcgScalarType.EPCG_NOTIF_CHAN_OPER_STATUS_PRIOR, MOAccessImpl.ACCESS_FOR_NOTIFY, new Integer32(OperationalStatus.UNKNOWN.toInt()), readerDevice);
//		epcgNotifChanOperStateSuppressInterval = new EpcgScalar(EpcgScalarType.EPCG_NOTIF_CHAN_OPER_STATE_SUPPRESS_INTERVAL, MOAccessImpl.ACCESS_READ_WRITE, new UnsignedInteger32(0), readerDevice);
//		epcgNotifChanOperStateSuppressions = new EpcgScalar(EpcgScalarType.EPCG_NOTIF_CHAN_OPER_STATE_SUPPRESSIONS, MOAccessImpl.ACCESS_READ_ONLY, new Counter32(0), readerDevice);
		sysDescr = new EpcgScalar(EpcgScalarType.SYS_DESCR, MOAccessImpl.ACCESS_READ_ONLY, null, readerDevice);
		sysLocation = new EpcgScalar(EpcgScalarType.SYS_LOCATION, MOAccessImpl.ACCESS_READ_WRITE, null, readerDevice);
		sysContact = new EpcgScalar(EpcgScalarType.SYS_CONTACT, MOAccessImpl.ACCESS_READ_WRITE, null, readerDevice);
//		sysUpTime = new EpcgScalar(EpcgScalarType.SYS_UP_TIME, MOAccessImpl.ACCESS_READ_ONLY, null, readerDevice);
		sysName = new EpcgScalar(EpcgScalarType.SYS_NAME, MOAccessImpl.ACCESS_READ_WRITE, null, readerDevice);
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
		assertEquals(new OctetString(readerDevice.getDescription()), epcgRdrDevDescription.getValue());

		assertEquals(new OctetString(readerDevice.getRole()), epcgRdrDevRole.getValue());

		assertEquals(new OctetString(readerDevice.getEPC()), epcgRdrDevEpc.getValue());

		assertEquals(new OctetString(readerDevice.getSerialNumber()), epcgRdrDevSerialNumber.getValue());

//		assertEquals(SnmpUtil.dateToOctetString(readerDevice.getTimeUTC()), epcgRdrDevTimeUtc.getValue()); // cannot test

		SnmpTable sourceTable = (SnmpTable)SnmpUtil.getSnmpTable(TableTypeEnum.EPCG_SOURCE_TABLE);
		String sourceName = readerDevice.getCurrentSource().getName();
		OID index = sourceTable.getTableRowIndexByValue(new OctetString(sourceName), EpcglobalReaderMib.idxEpcgSrcName);
		OID oid = new OID(EpcglobalReaderMib.oidEpcgSourceEntry);
		oid.append(EpcglobalReaderMib.colEpcgSrcName);
		oid.append(index);
		assertEquals(oid, epcgRdrDevCurrentSource.getValue());

		assertEquals(new Integer32(2), epcgRdrDevReboot.getValue());

		assertEquals(new Integer32(2), epcgRdrDevResetStatistics.getValue());

		// don't need to test epcgRdrDevResetTimestamp

		// epcgRdrDevNormalizePowerLevel // TODO: implement

		// epcgRdrDevNormalizeNoiseLevel // TODO: implement

		assertEquals(new Integer32(readerDevice.getOperStatus().toInt()), epcgRdrDevOperStatus.getValue());

		// don't need to test epcgRdrDevOperStatusPrior

		assertEquals(new Integer32(readerDevice.getOperStatusAlarmControl().getEnabled() ? 1 : 2), epcgRdrDevOperStateEnable.getValue());

		assertEquals(SnmpUtil.operStateToBITS(readerDevice.getOperStatusAlarmControl().getTriggerFromState()), epcgRdrDevOperNotifFromState.getValue());

		assertEquals(SnmpUtil.operStateToBITS(readerDevice.getOperStatusAlarmControl().getTriggerToState()), epcgRdrDevOperNotifToState.getValue());

		assertEquals(new Integer32(readerDevice.getOperStatusAlarmControl().getLevel().toInt()), epcgRdrDevOperNotifStateLevel.getValue());

		assertEquals(new UnsignedInteger32(readerDevice.getOperStatusAlarmControl().getSuppressInterval()), epcgRdrDevOperStateSuppressInterval.getValue());

		assertEquals(new Counter32(ReaderDevice.getOperStateSuppressions()), epcgRdrDevOperStateSuppressions.getValue());

//		assertEquals(new Gauge32(readerDevice.getFreeMemory()), epcgRdrDevFreeMemory.getValue()); // cannot test

		assertEquals(new Integer32(readerDevice.getFreeMemoryAlarmControl().getEnabled() ? 1 : 2), epcgRdrDevFreeMemoryNotifEnable.getValue());

		assertEquals(new Integer32(readerDevice.getFreeMemoryAlarmControl().getLevel().toInt()), epcgRdrDevFreeMemoryNotifLevel.getValue());

		assertEquals(new UnsignedInteger32(readerDevice.getFreeMemoryAlarmControl().getAlarmThreshold()), epcgRdrDevFreeMemoryOnsetThreshold.getValue());

		assertEquals(new UnsignedInteger32(readerDevice.getFreeMemoryAlarmControl().getRearmThreshold()), epcgRdrDevFreeMemoryAbateThreshold.getValue());

		assertEquals(new Integer32(readerDevice.getFreeMemory() < readerDevice.getFreeMemoryAlarmControl().getAlarmThreshold() ? 1 : 2), epcgRdrDevFreeMemoryStatus.getValue());

		assertEquals(new UnsignedInteger32(readerDevice.getFreeMemoryAlarmControl().getSuppressInterval()), epcgRdrDevMemStateSuppressInterval.getValue());

		assertEquals(new Counter32(ReaderDevice.getMemStateSuppressions()), epcgRdrDevMemStateSuppressions.getValue());

		// don't need to test epcgReadPointPriorOperStatus

		// epcgReadPointOperStateSuppressInterval // ...

//		assertEquals(new Counter32(ReadPoint.getOperStateSuppressions()), epcgReadPointOperStateSuppressions.getValue());

		// epcgAntRdPntSuppressInterval // ...

		// don't need to test epcgIoPortOperStatusPrior

		// epcgIoPortOperStateSuppressInterval // ...

//		assertEquals(new Counter32(IOPort.getOperStateSuppressions()), epcgIoPortOperStateSuppressions.getValue());

		// don't need to test epcgSrcOperPriorStatus

		// epcgSrcOperStateSuppressInterval // ...

//		assertEquals(new Counter32(Source.getOperStateSuppressions()), epcgSrcOperStateSuppressions.getValue());

		// don't need to test epcgNotifChanOperStatusPrior

		// epcgNotifChanOperStateSuppressInterval // ...

//		assertEquals(new Counter32(NotificationChannel.getOperStateSuppressions()), epcgNotifChanOperStateSuppressions.getValue());

		String descr =
			"Manufacturer: "
			+ readerDevice.getManufacturer()
			+ ", Model: "
			+ readerDevice.getModel()
			+ ", Manufacturer Description: "
			+ readerDevice.getManufacturerDescription();
		assertEquals(new OctetString(descr), sysDescr.getValue());

		assertEquals(new OctetString(readerDevice.getLocationDescription()), sysLocation.getValue());

		assertEquals(new OctetString(readerDevice.getContact()), sysContact.getValue());

//		assertEquals(new TimeTicks(readerDevice.getTimeTicks()), sysUpTime.getValue()); // cannot test

		assertEquals(new OctetString(readerDevice.getName()), sysName.getValue());
	}

	/**
	 * Tests the <code>setValue()</code> method.
	 */
	public final void testSetValue() {
		boolean oldBoolean;
		OperationalStatus oldOperStatus;
		AlarmLevel oldAlarmLevel;
		int oldInterval;
		int oldThreshold;

		String cont = readerDevice.getContact();
		String newCont = cont + "_new";
		readerDevice.setContact(newCont);
		assertEquals(newCont, readerDevice.getContact());
		epcgRdrDevReboot.setValue(new Integer32(1));
		assertEquals(cont, readerDevice.getContact());

		AntennaReadPoint antReadPoint = null;
		ReadPoint[] readPoints = readerDevice.getAllReadPoints();
		for (int i = 0; i < readPoints.length; i++) {
			if (readPoints[i] instanceof AntennaReadPoint) {
				antReadPoint = (AntennaReadPoint)readPoints[i];
			}
		}
		if (antReadPoint != null) {
			antReadPoint.increaseWriteCount();
			assertTrue(antReadPoint.getWriteCount() > 0);
			epcgRdrDevResetStatistics.setValue(new Integer32(1));
			assertEquals(0, antReadPoint.getWriteCount());
		}

		oldBoolean = readerDevice.getOperStatusAlarmControl().getEnabled();
		epcgRdrDevOperStateEnable.setValue(new Integer32(!oldBoolean ? 1 : 2));
		assertEquals(!oldBoolean, readerDevice.getOperStatusAlarmControl().getEnabled());
		readerDevice.getOperStatusAlarmControl().setEnabled(oldBoolean);

		oldOperStatus = readerDevice.getOperStatusAlarmControl().getTriggerFromState();
		epcgRdrDevOperNotifFromState.setValue(SnmpUtil.operStateToBITS(OperationalStatus.UP));
		assertEquals(OperationalStatus.UP, readerDevice.getOperStatusAlarmControl().getTriggerFromState());
		epcgRdrDevOperNotifFromState.setValue(SnmpUtil.operStateToBITS(OperationalStatus.DOWN));
		assertEquals(OperationalStatus.DOWN, readerDevice.getOperStatusAlarmControl().getTriggerFromState());
		readerDevice.getOperStatusAlarmControl().setTriggerFromState(oldOperStatus);

		oldOperStatus = readerDevice.getOperStatusAlarmControl().getTriggerToState();
		epcgRdrDevOperNotifToState.setValue(SnmpUtil.operStateToBITS(OperationalStatus.UP));
		assertEquals(OperationalStatus.UP, readerDevice.getOperStatusAlarmControl().getTriggerToState());
		epcgRdrDevOperNotifToState.setValue(SnmpUtil.operStateToBITS(OperationalStatus.DOWN));
		assertEquals(OperationalStatus.DOWN, readerDevice.getOperStatusAlarmControl().getTriggerToState());
		readerDevice.getOperStatusAlarmControl().setTriggerFromState(oldOperStatus);

		oldAlarmLevel = readerDevice.getOperStatusAlarmControl().getLevel();
		epcgRdrDevOperNotifStateLevel.setValue(new Integer32(AlarmLevel.CRITICAL.toInt()));
		assertEquals(AlarmLevel.CRITICAL, readerDevice.getOperStatusAlarmControl().getLevel());
		epcgRdrDevOperNotifStateLevel.setValue(new Integer32(AlarmLevel.DEBUG.toInt()));
		assertEquals(AlarmLevel.DEBUG, readerDevice.getOperStatusAlarmControl().getLevel());
		readerDevice.getOperStatusAlarmControl().setLevel(oldAlarmLevel);

		oldInterval = readerDevice.getOperStatusAlarmControl().getSuppressInterval();
		epcgRdrDevOperStateSuppressInterval.setValue(new UnsignedInteger32(oldInterval + 1));
		assertEquals(oldInterval + 1, readerDevice.getOperStatusAlarmControl().getSuppressInterval());
		readerDevice.getOperStatusAlarmControl().setSuppressInterval(oldInterval);

		oldBoolean = readerDevice.getFreeMemoryAlarmControl().getEnabled();
		epcgRdrDevFreeMemoryNotifEnable.setValue(new Integer32(!oldBoolean ? 1 : 2));
		assertEquals(!oldBoolean, readerDevice.getFreeMemoryAlarmControl().getEnabled());
		readerDevice.getFreeMemoryAlarmControl().setEnabled(oldBoolean);

		oldAlarmLevel = readerDevice.getFreeMemoryAlarmControl().getLevel();
		epcgRdrDevFreeMemoryNotifLevel.setValue(new Integer32(AlarmLevel.CRITICAL.toInt()));
		assertEquals(AlarmLevel.CRITICAL, readerDevice.getFreeMemoryAlarmControl().getLevel());
		epcgRdrDevFreeMemoryNotifLevel.setValue(new Integer32(AlarmLevel.DEBUG.toInt()));
		assertEquals(AlarmLevel.DEBUG, readerDevice.getFreeMemoryAlarmControl().getLevel());
		readerDevice.getFreeMemoryAlarmControl().setLevel(oldAlarmLevel);

		oldThreshold = readerDevice.getFreeMemoryAlarmControl().getAlarmThreshold();
		epcgRdrDevFreeMemoryOnsetThreshold.setValue(new UnsignedInteger32(oldThreshold + 1));
		assertEquals(oldThreshold + 1, readerDevice.getFreeMemoryAlarmControl().getAlarmThreshold());
		readerDevice.getFreeMemoryAlarmControl().setAlarmThreshold(oldThreshold);

		oldThreshold = readerDevice.getFreeMemoryAlarmControl().getRearmThreshold();
		epcgRdrDevFreeMemoryAbateThreshold.setValue(new UnsignedInteger32(oldThreshold + 1));
		assertEquals(oldThreshold + 1, readerDevice.getFreeMemoryAlarmControl().getRearmThreshold());
		readerDevice.getFreeMemoryAlarmControl().setRearmThreshold(oldThreshold);

		oldInterval = readerDevice.getFreeMemoryAlarmControl().getSuppressInterval();
		epcgRdrDevMemStateSuppressInterval.setValue(new UnsignedInteger32(oldInterval + 1));
		assertEquals(oldInterval + 1, readerDevice.getFreeMemoryAlarmControl().getSuppressInterval());
		readerDevice.getFreeMemoryAlarmControl().setSuppressInterval(oldInterval);

		String contact = readerDevice.getContact();
		String newContact = contact + "_new";
		sysContact.setValue(new OctetString(newContact));
		assertEquals(newContact, readerDevice.getContact());
		readerDevice.setContact(contact);

		String location = readerDevice.getLocationDescription();
		String newLocation = location + "_new";
		sysLocation.setValue(new OctetString(newLocation));
		assertEquals(newLocation, readerDevice.getLocationDescription());
		readerDevice.setLocationDescription(location);

		String name = readerDevice.getName();
		String newName = name + "_new";
		sysName.setValue(new OctetString(newName));
		assertEquals(newName, readerDevice.getName());
		readerDevice.setName(name);
	}

	/**
	 * Runs the test using the gui runner.
	 * @param args No arguments
	 */
	public static void main(String[] args) {
        junit.swingui.TestRunner.run(EpcgScalarTest.class);
    }

}
