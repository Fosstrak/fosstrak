package org.accada.hal.impl.feig;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.UnsupportedCommOperationException;

import org.accada.hal.AsynchronousIdentifyListener;
import org.accada.hal.ControllerProperties;
import org.accada.hal.HardwareException;
import org.accada.hal.Observation;
import org.accada.hal.Trigger;
import org.accada.hal.UnsignedByteArray;
import org.accada.hal.UnsupportedOperationException;
import org.accada.hal.impl.feig.comm.ISOProtocol;
import org.accada.hal.impl.feig.comm.RequestRecord;
import org.accada.hal.impl.feig.comm.ResponseRecord;
import org.accada.hal.impl.feig.util.ISOTransponderResponseErrorCode;
import org.accada.hal.impl.feig.util.StatusByte;
import org.accada.hal.transponder.InventoryItem;
import org.accada.hal.transponder.RFTechnology;
import org.accada.hal.transponder.TransponderModel;
import org.accada.hal.transponder.TransponderSystemInformationISO;
import org.accada.hal.transponder.TransponderType;
import org.accada.hal.util.ByteBlock;
import org.apache.log4j.Logger;

/**
 *
 * @author altery
 *
 */
public class FeigCOMController implements FeigController {

	static Logger log = Logger.getLogger(FeigCOMController.class);

	/**
	 * Controller properties encapsulation
	 */
	ControllerProperties props = null;

	/**
	 * Serial port number the reader is attached to
	 */
	private int comPort;

	/**
	 * Serial port communication speed
	 */
	private String baudRate;

	/**
	 * Serial port frame properties (databits, stopbits, parity)
	 */
	private String frame;

	/**
	 * Serial port communication timeout
	 */
	private int timeout = 3000;

   /**
    * HAL name
    */
   private String halName;

   /**
    * The properties file
    */
   private String defaultPropFile = "/props/FeigCOMController_default.xml";
   private String propFile;

	/**
	 * Transponder model used.
	 * If multiple transponders models are used the model is UNKOWN
	 */
	private TransponderModel transponderModel;

	/**
	 *
	 */
	private FeigMultiplexConfiguration multiplexConfig;

	/**
	 * ISOProtocol used for reader communication
	 */
	private ISOProtocol ip;

	/**
	 * The currently selected readpoint
	 */
	private String selectedReadPoint = "";

	private HashMap<String, InventoryItem> currentInventory = new HashMap<String, InventoryItem>();

	/**
	 *
	 * @param halName
	 */
	public FeigCOMController(String halName, String propFile) {
		this.halName = halName;
		this.propFile = propFile;
		try {
			log.debug("trying to initialize " + getHALName());
			this.initialize();
			log.debug("initialized");
		} catch (Exception e) {
			log.error("Reader initialization failed", e);
		}
	}

	/**
	 * Initialize the reader hardware.
	 *
	 * @throws HardwareException
	 */
	public void initialize() throws HardwareException {
		this.props = new ControllerProperties(propFile, defaultPropFile);

		try {
			// Read parameters from property file
			this.baudRate = this.props.getParameter("baudRate");
			this.comPort = Integer.parseInt(this.props.getParameter("comPort"));
			this.frame = this.props.getParameter("frame");
			this.timeout = Integer.parseInt(this.props.getParameter("timeout"));

			// Initialize multiplexer configuration
			String multiplexerConfig = this.props.getParameter("multiplexerConfig");
         if ((multiplexerConfig == null)) {
            multiplexConfig = new FeigMultiplexConfiguration(this,
                  propFile, defaultPropFile);
         } else {
            multiplexConfig = new FeigMultiplexConfiguration(this,
                  multiplexerConfig, null);
         }
			multiplexConfig.initialize();
			transponderModel = TransponderModel.getTransponderSpec(this.props.getParameter("transponderModel"));
		} catch (Exception e) {
			String message = "Error in reader property file";
			log.error("initialize: " + message, e);
			throw new HardwareException(message, e);
		}

		// Initialize the reader hardware
		try {
			log.info("  trying port: " + comPort + " baud: " + baudRate
					+ " frame: " + frame + " ...");
			initReader(comPort, baudRate, frame);
			log.info("reader initialized.");
			return;
		} catch (HardwareException e) {
			String message = "Error initializing reader";
			log.error("initialize: " + message, e);
			throw new HardwareException(message, e);
		}

	}

	/**
	 * (non-Javadoc)
	 *
	 * @see org.accada.hal.HardwareAbstraction#identify(java.lang.String[])
	 */
	public Observation[] identify(String[] readPointNames)
			throws HardwareException {
		Observation[] observations = new Observation[readPointNames.length];

		currentInventory.clear();
		for (int i = 0; i < readPointNames.length; i++) {
			observations[i] = new Observation();
			observations[i].setHalName(getHALName());
			observations[i].setReadPointName(readPointNames[i]);

			if (!this.selectReadpoint(readPointNames[i])) {
				String message = "Channel not valid!";
				throw new HardwareException(message);
			}

			Vector<String> ids = new Vector<String>();
			Vector<InventoryItem> items = this.getInventory();

			for (InventoryItem item : items) {
				String id;
				if (item.transponderType == TransponderType.ICodeUID) {
					id = item.id.substring(item.id.length() - 5, item.id.length());
				} else {
					id = item.id;
				}

				item.readPoint = readPointNames[i];
				ids.add(id);
				currentInventory.put(id, item);
			}

			int len = ids.size();
			String ids_arr[] = new String[len];
			ids_arr = ids.toArray(ids_arr);
			observations[i].setIds(ids_arr);
			observations[i].setTimestamp(System.currentTimeMillis());
		}
		return observations;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see org.accada.hal.HardwareAbstraction#getReadPointNames()
	 */
	public String[] getReadPointNames() {
		return (String[]) this.multiplexConfig.getReadPoints();
	}

	/**
	 *
	 * @param id
	 * @return
	 * @throws HardwareException
	 */
	private int detectTransponderBlockSize(String id) throws HardwareException {
		if (transponderModel == TransponderModel.UNKNOWN) {
			byte[] data;

			try {
				data = readMemoryBlocks(id, 0, 1);
			} catch (IOException e) {
				String message = "Communication error with reader";
				log.error("readBytes: Communication error with reader", e);
				throw new HardwareException(message);
			}

			// Handle response
			int blockSize = data[1];

			if (blockSize == 0) {
				log.warn("detectTransponderBlockSize: Blocksize is 0");
			}

			return blockSize;
		} else {
			return transponderModel.getBlocksize();
		}
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see org.accada.hal.HardwareAbstraction#readBytes(java.lang.String,
	 *      java.lang.String, int, int, java.lang.String[])
	 */
	public UnsignedByteArray readBytes(String readPointName, String id, int memoryBank,
			int offset, int length, String[] passwords)
			throws HardwareException, UnsupportedOperationException {

		// Select channel
		if (!selectReadpoint(readPointName)) {
			log.error("readBytes: Read point not available");
			throw new HardwareException("Read point not available");
		}

		int blockSize = detectTransponderBlockSize(id);

		if (blockSize == 0) {
			log.error("readBytes: Detected transponder blocksize is not valid");
			return new UnsignedByteArray(new byte[0]);
		}

		int blockStart = offset / blockSize;
		int blockEnd = (offset + length - 1) / blockSize;
		int blockCount = blockEnd - blockStart + 1;

		if (blockCount > transponderModel.getBlocks()) {
			// TODO Correct error semantics?
			log.error("readBytes: Data length exceeds transponder capacity");
			return new UnsignedByteArray(new byte[0]);
		}

		byte[] data;
		try {
			data = readMemoryBlocks(id, blockStart, blockCount);
		} catch (IOException e) {
			String message = "Communication error with reader";
			log.error("readBytes: " + message , e);
			throw new HardwareException(message);
		}

		byte[] dataTrim = new byte[length];
		System.arraycopy(data, offset % blockSize, dataTrim, 0, length);

		return new UnsignedByteArray(dataTrim);
	}

	private byte[] readMemoryBlocks(String id, int address, int number)
			throws IOException, HardwareException {

		log.debug("readMemoryBlocks: started");
		RequestRecord req = new RequestRecord();
		req.comAdr = (byte) 0xff;
		// Set ISO15693 host command
		req.controlByte = (byte) 0xb0;

		byte[] readCommand = new byte[12];
		// Set read command (0x23)
		readCommand[0] = (byte) 0x23;
		// Set adressed mode (MODE)
		readCommand[1] = (byte) 0x1;
		// Set tag address (UID)
		byte[] idb = ByteBlock.hexStringToByteArray(id, 8);
		System.arraycopy(idb, 0, readCommand, 2, 8);
		// Set first block number to be read (DB-ADDR)
		if (address < 0 || address > 255) {
			log.warn("readMemoryBlocks: Offset exceeds range");
		}
		readCommand[10] = (byte) address;
		// Set no of data blocks to be read (DB-N)
		if (number < 0 || number > 255) {
			log.warn("readMemoryBlocks: Read length exceeds range.");
		}
		readCommand[11] = (byte) number;
		req.data = readCommand;

		ResponseRecord resp = ip.sendRequest(req);

		StatusByte status = StatusByte.getStatus(resp.status);
		String statusMessage = getStatusMessage(status);
		if (status == StatusByte.ISO_15693_ERROR) {
			ISOTransponderResponseErrorCode errorcode = ISOTransponderResponseErrorCode
					.getResponseError(resp.data[0]);
			String errorMessage = getTransponderErrorMessage(errorcode);

			log.error(statusMessage);
			log.error(errorMessage);
			throw new HardwareException("Reading data blocks failed\n" + " "
			      + statusMessage + "/n" + " " + errorMessage + "" + " at block "
					+ Integer.toHexString(resp.data[1]));
		} else if (status != StatusByte.OK) {
			log.error(statusMessage);
			throw new HardwareException("Reading data blocks failed\n" + " "
			      + statusMessage + "/n");
		}
		log.debug("readMemoryBlocks: finished successfuly");

		return resp.data;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see org.accada.hal.HardwareAbstraction#writeBytes(java.lang.String,
	 *      java.lang.String, int, int, byte[], java.lang.String[])
	 */
	public void writeBytes(String readPointName, String id, int memoryBank,
			int offset, UnsignedByteArray data, String[] passwords)
			throws HardwareException, UnsupportedOperationException {

		int length = data.toByteArray().length;

		// Select channel
		if (!selectReadpoint(readPointName)) {
			throw new HardwareException("Read point not available");
		}

		int blockSize = detectTransponderBlockSize(id);

		if (blockSize == 0) {
			// TODO How to notify failure? Throw exception?
			log.error("readBytes: Detected transponder blocksize is not valid");
			return;
		}

		int blockStart = offset / blockSize;
		int blockEnd = (offset + length - 1) / blockSize;
		int blockCount = blockEnd - blockStart + 1;

		if (blockCount > transponderModel.getBlocks()) {
			// TODO How to notify failure? Throw exception?
			log.error("writeBytes: Data length exceeds transponder capacity");
			return;
		}

		byte[] backupStart = null;
		int offsetStart = offset % blockSize;
		if (offsetStart != 0) {
			try {
				backupStart = readMemoryBlocks(id, blockStart, 1);
			} catch (IOException e) {
				String message = "Communication error with reader";
				log.error("writeBytes: " + message , e);
				throw new HardwareException(message);
			}
		}

		byte[] backupEnd = null;
		int offsetEnd = (offset + length -1) / blockSize;
		if (blockCount > 1 && offsetEnd != 0) {
			try {
				backupEnd = readMemoryBlocks(id, blockEnd, 1);
			} catch (IOException e) {
				String message = "Communication error with reader";
				log.error("writeBytes: " + message , e);
				throw new HardwareException(message);
			}
		}

		byte[] blockData = new byte[blockSize * blockCount];
		if (backupStart != null) {
			System.arraycopy(backupStart, 0, blockData, 0, blockSize);
		}

		if (backupEnd != null) {
			System.arraycopy(backupEnd, 0, blockData, blockData.length - blockSize, blockSize);
		}

		System.arraycopy(data.toByteArray(), 0, blockData, offsetStart, length);

		try {
			writeMemoryBlocks(id, blockStart, blockSize, blockData);
		} catch (IOException e) {
			log.error("writeBytes: Communication error with reader", e);
			throw new HardwareException("Communication error with reader");
		}
	}

	private void writeMemoryBlocks(String id, int address, int dbsize,
			byte[] data) throws HardwareException,
			UnsupportedOperationException, IOException {

		log.debug("writeMemoryBlocks: started");

		RequestRecord req = new RequestRecord();
		byte[] writeCommand = new byte[13 + data.length];
		// Set write command
		writeCommand[0] = (byte) 0x24;
		// Set adressed mode (MODE)
		writeCommand[1] = (byte) 0x1;
		// Set tag address (UID)
		byte[] idb = ByteBlock.hexStringToByteArray(id, 8);
		System.arraycopy(idb, 0, writeCommand, 2, 8);
		// Set first block number to be read (DB-ADDR)
		if (address < 0 || address > 255) {
			log.warn("writeMemoryBlocks: Address exeeds range");
		}
		writeCommand[10] = (byte) address;
		// Set no of data blocks to be read (DB-N)
		int dbn = data.length;
		if (dbn < 0 || dbn > 255) {
			log.warn("writeMemoryBlocks: Data size exceeds exceeds range");
		}
		writeCommand[11] = (byte) dbn;
		writeCommand[12] = (byte) dbsize;
		System.arraycopy(data, 0, writeCommand, 13, dbn);
		req.data = writeCommand;

		ResponseRecord resp = ip.sendRequest(req);

		// Handle response
		StatusByte status = StatusByte.getStatus(resp.status);
		String statusMessage = getStatusMessage(status);
		if (status == StatusByte.ISO_15693_ERROR) {
			ISOTransponderResponseErrorCode errorcode = ISOTransponderResponseErrorCode
					.getResponseError(resp.data[0]);
			String errorMessage = getTransponderErrorMessage(errorcode);

			log.error(statusMessage);
			log.error(errorMessage);
			throw new HardwareException("Writing data blocks failed\n" + " "
			      + statusMessage + "/n" + " " + errorMessage + "" + " at block "
					+ Integer.toHexString(resp.data[1]));
		} else if (status == StatusByte.WRITE_ERROR) {
			log.error(statusMessage);
			throw new HardwareException("Writing data blocks failed\n" + " "
			      + statusMessage + " at block " + Integer.toHexString(resp.data[1]));
		} else if (status != StatusByte.OK) {
			log.error(statusMessage);
			throw new HardwareException("Writing data blocks failed\n" + " "
			      + statusMessage + "/n");
		}

		log.debug("writeMemoryBlocks: finished successfuly");
	}

	private String getStatusMessage(StatusByte status) {
		return "Status: " + status.description() + " (0x" + status.code() + ")";
	}

	private String getTransponderErrorMessage(
			ISOTransponderResponseErrorCode error) {
		return "Transponder: " + error.description() + "(0x" + error.code()
				+ ")";
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see org.accada.hal.HardwareAbstraction#programTagId(java.lang.String,
	 *      java.lang.String[])
	 */
	public void writeId(String readPointName, String id, String[] passwords)
			throws HardwareException, UnsupportedOperationException {

      throw new HardwareException("HAL not ready.");

	}

   /**
    * (non-Javadoc)
    *
    * @see org.accada.hal.HardwareAbstraction#supportsWriteId()
    */
   public boolean supportsWriteId() {
      return false;
   }

	/**
	 * (non-Javadoc)
	 *
	 * @see org.accada.hal.HardwareAbstraction#reset()
	 */
	public void reset() throws HardwareException {
		// construct request record...
		RequestRecord req = new RequestRecord();
		req.comAdr = (byte) 0xff;
		// Set reader RF reset command
		req.controlByte = (byte) 0x69;
		req.data = null;

		// send record...
		ResponseRecord resp = null;

		// may throw IOException
		try {
			resp = ip.sendRequest(req);
		} catch (IOException e) {
			log.error("readBytes: Communication error with reader", e);
			throw new HardwareException("Communication error with reader");
		}
		// Handle response
		StatusByte status = StatusByte.getStatus(resp.status);
		String statusMessage = getStatusMessage(status);
		if (status != StatusByte.OK) {
			log.error(statusMessage);
			throw new HardwareException("Reset of reader reader failed\n"
							+ " " + statusMessage);
		}
		log.info(statusMessage);
	}

   /**
    * (non-Javadoc)
    *
    * @see org.accada.hal.HardwareAbstraction#supportsReset()
    */
	public boolean supportsReset() {
	   return true;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see org.accada.hal.HardwareAbstraction#setParameter(java.lang.String,
	 *      java.lang.String)
	 */
	public void setParameter(String param, String value)
			throws HardwareException {
		try {
			this.props.setParameter(param, value);
			this.initialize();
		} catch (Exception e) {
			log.error("setParameter: Error setting parameter", e);
			throw new HardwareException("Error setting parameter", e);
		}
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see org.accada.hal.HardwareAbstraction#getParameter(java.lang.String)
	 */
	public String getParameter(String param) throws HardwareException {

		try {
			return this.props.getParameter(param);
		} catch (Exception e) {
			log.error("getParameter: Error getting parameter", e);
			throw new HardwareException("Error getting parameter", e);
		}
	}

   /**
    * (non-Javadoc)
    *
    * @see org.accada.hal.HardwareAbstraction#supportParameters()
    */
   public boolean supportsParameters() {
      return true;
   }

	/**
	 * (non-Javadoc)
	 *
	 * @see org.accada.hal.HardwareAbstraction#getAllParameterNames()
	 */
	public String[] getAllParameterNames() throws HardwareException,
			UnsupportedOperationException {
		try {
			return this.props.getParameterNames();
		} catch (Exception e) {
			log.error("getAllParameterNames: Error gettings parameter names", e);
			throw new HardwareException("Error getting parameter names", e);
		}
	}

	synchronized protected void populateSystemInformation(
			Vector<InventoryItem> inventory) throws HardwareException {

		for (InventoryItem item : inventory) {
			if (item.transponderType == TransponderType.ISO15693) {
				RequestRecord req = new RequestRecord();
				req.comAdr = (byte) 0xff;
				// Set ISO15693 host command
				req.controlByte = (byte) 0xb0;

				byte[] sysInfCommand = new byte[10];
				// Set system information command
				sysInfCommand[0] = (byte) 0x2b;
				// Set mode to addressed (MODE)
				sysInfCommand[1] = (byte) 0x1;
				// Set UID
				byte[] idb = ByteBlock.hexStringToByteArray(item.id, 8);
				System.arraycopy(idb, 0, sysInfCommand, 2, 8);

				// Send request
				ResponseRecord resp = null;

				try {
					resp = ip.sendRequest(req);
				} catch (IOException e) {
					log.error("populateSystemInformation: Communication error with reader", e);
					throw new HardwareException("Communication error with reader");
				}

				StatusByte status = StatusByte.getStatus(resp.status);
				String statusMessage = getStatusMessage(status);
				if (status == StatusByte.ISO_15693_ERROR) {
					ISOTransponderResponseErrorCode errorcode = ISOTransponderResponseErrorCode
							.getResponseError(resp.data[0]);
					String errorMessage = getTransponderErrorMessage(errorcode);
					log.error(statusMessage);
					log.error(errorMessage);
					throw new HardwareException(
					      "Getting transponder system information failed\n"
							+ " " + statusMessage + "/n" + " " + errorMessage);
				}
				log.info(statusMessage);

				TransponderSystemInformationISO sysInf = new TransponderSystemInformationISO(
						resp.data);
				item.systemInformation = sysInf;
			}

		}
	}

	/**
	 * Throws RFIDException if error occurs. (returns empty Vector if error
	 * occurs!)
	 *
	 * @return
	 */
	synchronized protected Vector<InventoryItem> getInventory()
			throws HardwareException {

		Vector<InventoryItem> inventory = new Vector<InventoryItem>();
		boolean moreTags = false;

		do {
			// construct request record...
			RequestRecord req = new RequestRecord();
			req.comAdr = (byte) 0xff;
			// control byte = 0xb0 (ISO host command)
			req.controlByte = (byte) 0xb0;

			if (moreTags) {
				// 1. byte: inventory request (01)
				// 2. byte: mode: more data request (80)
				req.data = ByteBlock.hexStringToByteArray("0180");
			} else {
				// 1. byte: inventory request (01)
				// 2. byte: mode: new inventory request (00)
				req.data = ByteBlock.hexStringToByteArray("0100");
			}

			ResponseRecord resp = null;

			try {
				resp = ip.sendRequest(req);
			} catch (IOException e) {
				log.error("readBytes: Communication error with reader", e);
				throw new HardwareException("Communication error with reader");
			}

			StatusByte status = StatusByte.getStatus(resp.status);
			String statusMessage = getStatusMessage(status);
			if (status == StatusByte.NO_TRANSPONDER) {
				log.info(statusMessage);
				return inventory;
			} else if (status == StatusByte.MORE_DATA) {
				moreTags = true;
			} else if (resp.status == (byte) 0x00) {
				moreTags = false;
			} else {
				log.error(statusMessage);
				throw new HardwareException();
			}
			log.info(statusMessage);

			int datasets = ByteBlock.byteToNumber(resp.data[0]);
			TransponderType tType = TransponderType.getType(resp.data[1]);

			int dataSize = tType.dataSize();

			// Process each transponder
			for (int i = 0; i < datasets; i++) {
				InventoryItem item = new InventoryItem();

				byte[] data = new byte[dataSize];
				System.arraycopy(resp.data, 1 + i * dataSize, data, 0, dataSize);

				byte trType = data[0];
				item.transponderType = TransponderType.getType(trType);
				item.rfTechnology = RFTechnology.getType(trType);

				if (item.transponderType == TransponderType.ICodeEPC) {
					item.id = ByteBlock.byteArrayToHexString(Arrays
							.copyOfRange(data, 1, item.transponderType
									.dataSize()));
				} else if (item.transponderType == TransponderType.ICodeUID) {
					item.id =
						ByteBlock.byteArrayToHexString(Arrays.copyOfRange(data,
						1, item.transponderType.dataSize()));
				} else {
					item.dsfid = data[1];
					item.id = ByteBlock.byteArrayToHexString(Arrays
							.copyOfRange(data, 2, item.transponderType
									.dataSize()));
				}
				inventory.add(item);
			}
		} while (moreTags);

		return inventory;
	}

	int resolveSourceId(String readPointName) {
		int index = 0;

		while (!this.multiplexConfig.getReadPoints()[index].equalsIgnoreCase(readPointName)) {
			index++;
		}
		// +1 since the Feig Multiplexer enumerates its channels from 1 to 8
		return index + 1;
	}

	/**
	 * initializes the reader.
	 *
	 * @param comPort
	 *            the port number the reader is connected to.
	 * @param baudRate
	 *            the baud rate used for communication with the reader
	 * @param frame
	 *            a frame constisting of databit, parity and stopbit
	 * @throws RFIDException
	 *             if the port is not found, the frame is not supported, an
	 *             io-error occurs...
	 */
	protected void initReader(int comPort, String baudRate, String frame)
			throws HardwareException {

		// create new ISOProtocol that opens the serial port

		int baud = Integer.parseInt(baudRate);
		int databits = Integer.parseInt(frame.substring(0, 1));
		int parity;
		String p = frame.substring(1, 2);
		if (p.equalsIgnoreCase("e")) {
			parity = SerialPort.PARITY_EVEN;
		} else if (p.equalsIgnoreCase("o")) {
			parity = SerialPort.PARITY_ODD;
		} else {
			parity = SerialPort.PARITY_NONE;
		}
		int stopbits = Integer.parseInt(frame.substring(2, 3));
		try {
			if (ip != null)
				ip.closePort();
			ip = new ISOProtocol(comPort, timeout, baud, databits, stopbits,
					parity);
		} catch (UnsupportedCommOperationException e) {
			// serial port parameters (baud, databits, stopbits, parity) not
			// supported
			// or port not found (maybe locked)
			String message = "initReader: Port configuration error";
			log.error(message, e);
			throw new HardwareException(message, e);
		} catch (PortInUseException e) {
			// port is already open
			String message = "initReader: Port already in use: ";
			log.error(message, e);
			throw new HardwareException(message, e);
		} catch (IOException e) {
			// input/output error or too many event listeners for this
			// serial
			// port
			String message = "initReader: Unknown port error";
			log.error(message,e);
			throw new HardwareException(message, e);
		}

		reset();
	}

	public String getHALName() {
		return halName;
	}

	public boolean supportsReadBytes() {
		return true;
	}

	public boolean supportsWriteBytes() {
		return true;
	}

	public void startAsynchronousIdentify(String[] readPointNames, Trigger trigger)
	      throws HardwareException, UnsupportedOperationException {
      throw new HardwareException("HAL not ready.");
	}

	public void stopAsynchronousIdentify()
			throws HardwareException, UnsupportedOperationException {
      throw new HardwareException("HAL not ready.");
	}

	public boolean isAsynchronousIdentifyRunning() throws HardwareException,
			UnsupportedOperationException {
      throw new HardwareException("HAL not ready.");
	}

   public void addAsynchronousIdentifyListener(AsynchronousIdentifyListener listener)
         throws HardwareException, UnsupportedOperationException {
      throw new HardwareException("HAL not ready.");
   }

   public void removeAsynchronousIdentifyListener(AsynchronousIdentifyListener listener)
         throws HardwareException, UnsupportedOperationException {
      throw new HardwareException("HAL not ready.");
   }

	public boolean supportsAsynchronousIdentify() {
	   return false;
	}

	public boolean isReadPointReady(String readPointName) {
		for (String rp : multiplexConfig.getReadPoints()) {
			if (rp.equalsIgnoreCase(readPointName))
				return true;
		}
		return false;
	}

	public boolean supportsIsReadPointReady() {
	   return true;
	}

	public void kill(String readPointName, String id, String[] passwords) throws HardwareException,
			UnsupportedOperationException {

		if (!currentInventory.containsKey(id)) {
			log.error("kill: ID not identified.");
			// TODO Reason about error semantics (throw exception?)
			return;
		}

		InventoryItem item = currentInventory.get(id);
		// Get whole IDD for UID tags (same for other transponder types)
		String idd = item.id;

		if (!selectReadpoint(item.readPoint)) {
			throw new HardwareException("Read point not available");
		}

		if (!item.readPoint.equals(readPointName)) {
		   throw new HardwareException("Read point not available");
		}

		String password;
		if (passwords.length == 0) {
			password = "00000000";
		} else {
			password = passwords[0];
		}

		if (item.transponderType == TransponderType.TagItHF) {
			killTagIt(idd, password);
		} else if (item.transponderType == TransponderType.ICodeUID) {
			destroyIcodeUID(idd, password);
		} else if (item.transponderType == TransponderType.ICodeEPC) {
			destroyIcodeEPC(idd, password);
			throw new UnsupportedOperationException("TODO");
		} else {
			throw new UnsupportedOperationException("Transponder "
			      + item.transponderType.toString() + " does not support kill");
		}
	}

	private void killTagIt(String id, String password) throws HardwareException {
		// construct request record...
		RequestRecord req = new RequestRecord();
		req.comAdr = (byte) 0xff;
		// Set ISO15693 host command
		req.controlByte = (byte) 0xb0;

		byte[] killCommand = new byte[14];
		// Set kill command
		killCommand[0] = (byte) 0xa1;
		// Set mode
		killCommand[1] = (byte) 0x1;
		// Set tag address (UID)
		byte[] idb = ByteBlock.hexStringToByteArray(id, 19);
		System.arraycopy(idb, 0, killCommand, 2, 8);
		if (password.length() != 4)
			log.warn("killTagIt: Password is " + password.length()
					+ " bytes but should be 4");
		byte[] pswb = ByteBlock.hexStringToByteArray(password.substring(0, 3));
		System.arraycopy(pswb, 0, killCommand, 10, 4);
		req.data = killCommand;

		// send record...
		ResponseRecord resp = null;

		try {
			resp = ip.sendRequest(req);
		} catch (IOException e) {
			log.error("killTagIt: Communication error with reader", e);
			throw new HardwareException("Communication error with reader");
		}

		// Handle response
		StatusByte status = StatusByte.getStatus(resp.status);
		String statusMessage = getStatusMessage(status);
		if (status == StatusByte.ISO_15693_ERROR) {
			ISOTransponderResponseErrorCode errorcode = ISOTransponderResponseErrorCode
					.getResponseError(resp.data[0]);
			String errorMessage = getTransponderErrorMessage(errorcode);

			log.error(statusMessage);
			log.error(errorMessage);
			log.error("killTagIt: Killing tag failed");
			throw new HardwareException("Killing tag failed\n"
							+ " " + statusMessage + "/n");
		}
	}

	private void destroyIcodeEPC(String id, String password) {

	}

	private void destroyIcodeUID(String id, String password)
			throws HardwareException {
		// construct request record...
		RequestRecord req = new RequestRecord();
		req.comAdr = (byte) 0xff;
		// Set special host command (I-Code UID)
		req.controlByte = (byte) 0x1c;

		byte[] destroyCommand = new byte[24];
		// Set destroy command
		destroyCommand[0] = (byte) 0x18;
		// Set UID mode
		destroyCommand[1] = (byte) 0x1;
		// Set tag address (IDD)
		byte[] idb = ByteBlock.hexStringToByteArray(id, 19);
		System.arraycopy(idb, 0, destroyCommand, 2, 19);
		if (password.length() != 4) {
			log.warn("destroyIcodeUID: Password is " + password.length()
					+ " bytes but should be 4");
		}
		byte[] pswb = ByteBlock.hexStringToByteArray(password.substring(0, 2));
		System.arraycopy(pswb, 0, destroyCommand, 21, 3);

		req.data = destroyCommand;

		ResponseRecord resp = null;

		try {
			resp = ip.sendRequest(req);
		} catch (IOException e) {
			String message = "Communication error with reader";
			log.error("destroyIcodeUID: " + message, e);
			throw new HardwareException(message);
		}

		// Handle response
		StatusByte status = StatusByte.getStatus(resp.status);
		String statusMessage = getStatusMessage(status);
		if (status == StatusByte.RF_COMMUNICATION_ERROR) {
			log.error(statusMessage);
			log
					.error("destroyIcodeUID: Destroying tag failed. More than one transponder in range or ID not matching.");
			throw new HardwareException("Destroying tag failed. More than one transponder in range or ID not matching.");
		} else if (status == StatusByte.WRITE_ERROR) {
			log.error(statusMessage);
			log
					.error("destroyIcodeUID: Destroying tag not successful. Tag may still be read.");
			throw new HardwareException("Destroying tag not successful. Tag may still be read.");
		}
	}

	public void shutDownReadPoint(String readPointName) {
	}

	public boolean supportsShutDownReadPoint() {
	   return false;
	}

	public void startUpReadPoint(String readPointName) {
	}

	public boolean supportsStartUpReadPoint() {
	   return false;
	}

	public boolean supportsKill() {
		return true;
	}

	public int getReadPointNoiseLevel(String readPointName, boolean normalize) {
		return -1;
	}

	public boolean supportsGetReadPointNoiseLevel() {
	   return false;
	}

	public int getReadPointPowerLevel(String readPointName, boolean normalize) {
		return -1;
	}

	public boolean supportsGetReadPointPowerLevel() {
	   return false;
	}

	public boolean selectReadpoint(String readPointName) {
		boolean selected = multiplexConfig.selectReadPoint(readPointName);
		if (selected) {
			selectedReadPoint = readPointName;
		} else {
			selectedReadPoint = "";
		}

		return selected;
	}

	/**
	 *
	 */
	public boolean selectChannel(int address, int channel) throws HardwareException {
		RequestRecord req = new RequestRecord();

		// Select select channel command (0xDD)
		req.controlByte = (byte) 0xDD;

		byte[] data = new byte[3];
		// Set multiplexer address
		data[0] = (byte) address;
		// Set output channel 1
		data[1] = (byte) channel;
		// Ignore output channel 2
		data[2] = (byte) 0x00;

		req.data = data;

		ResponseRecord resp = null;

		try {
			resp = ip.sendFunctionUnitRequest(req);
		} catch (IOException e) {
			String message = "Communication error with reader";
			log.error("readBytes: Communication error with reader", e);
			throw new HardwareException(message);
		}

		StatusByte status = StatusByte.getStatus(resp.status);
		if (status == StatusByte.OK) {
			log.info("Status: " + status.description());
			log.info("Multiplexer: Channel " + channel + " selected");
			return true;
		} else if (status == StatusByte.PARAMETER_RANGE_ERROR) {
			log.error("Status: " + status.description());
			log.error("Multiplexer: Invalid channel");
			return false;
		} else {
			log.error("Status: " + status.description());
			log.error("Multiplexer: Channel " + channel + " not selected.");
			return false;
		}
	}

	public String[] getSupportedTransponderTypes() {
		// TODO query reader and determine supported transponder types
		// (use command 0x56, get software version, field TR-TYPE)
		// Then return a (hardcoded?) list for each transponder type
		// as we probably want to use TransponderModel enums for other
		// readers as well.
		return new String[1];
	}

}