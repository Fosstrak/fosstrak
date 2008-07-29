package org.fosstrak.hal.impl.feig;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.fosstrak.hal.AsynchronousIdentifyListener;
import org.fosstrak.hal.HardwareAbstraction;
import org.fosstrak.hal.HardwareException;
import org.fosstrak.hal.MemoryBankDescriptor;
import org.fosstrak.hal.MemoryDescriptor;
import org.fosstrak.hal.Observation;
import org.fosstrak.hal.TagDescriptor;
import org.fosstrak.hal.Trigger;
import org.fosstrak.hal.UnsignedByteArray;
import org.fosstrak.hal.UnsupportedOperationException;
import org.fosstrak.hal.impl.feig.comm.RequestRecord;
import org.fosstrak.hal.impl.feig.comm.ResponseRecord;
import org.fosstrak.hal.impl.feig.comm.TCPIPProtocol;
import org.fosstrak.hal.impl.feig.util.StatusByte;
import org.fosstrak.hal.transponder.EPCTransponderModel;
import org.fosstrak.hal.transponder.IDType;
import org.fosstrak.hal.transponder.InventoryItem;
import org.fosstrak.hal.transponder.RFTechnology;
import org.fosstrak.hal.transponder.TransponderType;
import org.fosstrak.hal.util.ByteBlock;
import org.fosstrak.hal.util.ResourceLocator;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

/**
 *
 * @author hallerj
 *
 */
public class FeigTCPIPController implements HardwareAbstraction {

	static Logger log = Logger.getLogger(FeigCOMController.class);

	/**
	 * The port number the reader is listening
	 */
	private int port;

	/**
	 * The address of the reader
	 */
	private String address;

	/**
	 * Communication timeout
	 */
	private int timeout = 3000;

   /**
    * The configuration files
    */
   private String defaultConfigFile = "/props/FeigTCPIPController_default.xml";
   private String configFile;
   private String epcTransponderModelsConfig;

   /**
    * The configuration
    */
   private XMLConfiguration config = null;

   /**
    * HAL name
    */
   private String halName;

	/**
	 * TCPIPProtocol used for reader communication
	 */
	private TCPIPProtocol tcpip;

	/**
    * The current inventory
	 */
   private HashMap<String, InventoryItem> currentInventory = new HashMap<String, InventoryItem>();

   /**
    * Number of read points
    */
   private int numberOfReadPoints = 0;

   /**
    * Logical read points
    */
   private HashMap<String, Integer> readPoints;
   private HashMap<Integer, String> antennaNames;

   /**
    * The currently selected readpoint
    */
   private String selectedReadPoint = "";

	/**
	 *
	 * @param halName
	 */
	public FeigTCPIPController(String halName, String configFile) {
		this.halName = halName;
		this.configFile = configFile;
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

      // read parameters from configuration file
      config = new XMLConfiguration();
      config.setListDelimiter(',');
      URL fileurl = ResourceLocator.getURL(configFile, defaultConfigFile,
         this.getClass());
      try {
         config.load(fileurl);
         // get parameters
         this.address = config.getString("address");
         this.port = config.getInt("port");
         this.timeout = config.getInt("timeout");
         this.epcTransponderModelsConfig = config.getString(
            "epcTransponderModelsConfig");
         readPoints = new HashMap<String, Integer>();
         antennaNames = new HashMap<Integer, String>();
         numberOfReadPoints = config.getMaxIndex("readpoint") + 1;
         if (numberOfReadPoints > 4) {
            numberOfReadPoints = 4;
         }
         for (int i = 0; i < numberOfReadPoints; i++) {
            // key to current read point
            String key = "readpoint(" + i + ")";
            // read point name
            String readpointName = config.getString(key + ".name");
            log.debug("Property found: " + key + ".name = " + readpointName);
            // read point connector
            int readpointConnector = config.getInt(key + ".connector");
            log.debug("Property found: " + key + ".connector = " + readpointConnector);
            if ((readpointConnector < 1) || (readpointConnector > 4)) {
               log.error("Readpoint connector value out of range, readpoint '" +
                  readpointName + "' not added to read points.");
            } else {
               readPoints.put(readpointName, readpointConnector);
               antennaNames.put(readpointConnector, readpointName);
            }
         }
      } catch (ConfigurationException e) {
         log.error("Properties file not found.");
         throw new HardwareException("Properties file not found.");
      }

		// Initialize the reader hardware
		try {
			log.info("  trying address: " + address + " port: " + port + " ...");
			initReader(address, port);
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
	 * @see org.fosstrak.hal.HardwareAbstraction#identify(java.lang.String[])
	 */
	public Observation[] identify(String[] readPointNames)
			throws HardwareException {

		currentInventory.clear();

      Observation[] observations = new Observation[readPointNames.length];
      for (int i = 0; i < readPointNames.length; i++) {
   		observations[i] = new Observation();
   		observations[i].setHalName(getHALName());
   		observations[i].setReadPointName(readPointNames[i]);

         // Select channel
         if (!selectReadPoint(readPointNames[i])) {
            log.error("readBytes: Read point not available");
            throw new HardwareException("Read point not available");
         }

   		Vector<InventoryItem> items = this.getInventory();
         Vector<String> ids = new Vector<String>();
         Vector<TagDescriptor> tds = new Vector<TagDescriptor>();

   		for (InventoryItem item : items) {
   			String id = item.id;

            if (item.transponderType == TransponderType.EPCclass1Gen2) {
					IDType idType = IDType.getIdType("EPC",
							config.getString("idTypesConfig"));
               EPCTransponderModel tagModel = item.epcTransponderModel;
               MemoryBankDescriptor[] memoryBankDescriptors =
                  new MemoryBankDescriptor[4];
               memoryBankDescriptors[0] = new MemoryBankDescriptor(
                  tagModel.getReservedSize(), tagModel.getReservedReadable(),
                  tagModel.getReservedWriteable());
               memoryBankDescriptors[1] = new MemoryBankDescriptor(
                     tagModel.getEpcSize(), tagModel.getEpcReadable(),
                     tagModel.getEpcWriteable());
               memoryBankDescriptors[2] = new MemoryBankDescriptor(
                     tagModel.getTidSize(), tagModel.getTidReadable(),
                     tagModel.getTidWriteable());
               memoryBankDescriptors[3] = new MemoryBankDescriptor(
                     tagModel.getUserSize(), tagModel.getUserReadable(),
                     tagModel.getUserWriteable());
               MemoryDescriptor memoryDescriptor = new MemoryDescriptor(
                     memoryBankDescriptors);
               TagDescriptor td = new TagDescriptor(idType, memoryDescriptor);
               tds.add(td);
            }

   			item.readPoint = readPointNames[i];
   			ids.add(id);
   			currentInventory.put(id, item);
   		}

   		int len = ids.size();
   		String[] ids_arr = new String[len];
   		ids_arr = ids.toArray(ids_arr);
         if (tds.size() == len) {
            TagDescriptor[] tds_arr = new TagDescriptor[len];
            tds_arr = tds.toArray(tds_arr);
            observations[i].setTagDescriptors(tds_arr);
         }
         observations[i].setIds(ids_arr);
   		observations[i].setTimestamp(System.currentTimeMillis());
      }
      // turn readpoints off after inventories
      selectAntennaConnector(0);

		return observations;
	}

   /**
    * Select a read point
    *
    * @param readpointName
    *          the name of the read point
    * @return
    *          true if read point selected
    */
   protected boolean selectReadPoint(String readpointName) {

      boolean selected = false;

      if (readpointName.equals(selectedReadPoint)) {
         selected = true;
      }

      if (!selected) {
         Integer connector = readPoints.get(readpointName);
         if (connector != null) {
            try {
               selectAntennaConnector(connector);
               selected = true;
            } catch (Exception e) {}
         }
      }

      return selected;
   }

   /**
    * Selects an antenna connector
    *
    * @param connector
    *          the number of the connector to select (1-4)
    * @throws HardwareException
    */
   synchronized protected void selectAntennaConnector(int connector)
         throws HardwareException {

      // construct request record...
      RequestRecord req = new RequestRecord();
      req.comAdr = (byte) 0xff;
      req.controlByte = (byte) 0x6a; // (RF Output ON/OFF)
      switch (connector) {
         case 0:
            req.data = ByteBlock.hexStringToByteArray("80");
            break;
         case 1:
            req.data = ByteBlock.hexStringToByteArray("81");
            break;
         case 2:
            req.data = ByteBlock.hexStringToByteArray("82");
            break;
         case 3:
            req.data = ByteBlock.hexStringToByteArray("83");
            break;
         case 4:
            req.data = ByteBlock.hexStringToByteArray("84");
            break;
         default:
            log.error("Can not select connector " + connector + ", switching "
               + "all antennas off.");
            req.data = ByteBlock.hexStringToByteArray("80");
      }

      ResponseRecord resp = null;

      try {
         resp = tcpip.sendRequest(req);
      } catch (IOException e) {
         log.error("selectAntennaConnector: Communication error with reader", e);
         throw new HardwareException("Communication error with reader");
      }

      StatusByte status = StatusByte.getStatus(resp.status);
      String statusMessage = getStatusMessage(status);
      if (resp.status != (byte) 0x00) {
         log.error(statusMessage);
         throw new HardwareException();
      }
   }

   /**
	 * (non-Javadoc)
	 *
	 * @see org.fosstrak.hal.HardwareAbstraction#getReadPointNames()
	 */
	public String[] getReadPointNames() {
      Set<String> keyset = readPoints.keySet();
      return keyset.toArray(new String[keyset.size()]);
	}

	/**
	 * Block size are words (2 bytes).
    *
	 * @return
    *          the block size
	 */
	private int getBlockSize() {
      return 2;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see org.fosstrak.hal.HardwareAbstraction#readBytes(java.lang.String,
	 *      java.lang.String, int, int, int, java.lang.String[])
	 */
	public UnsignedByteArray readBytes(String readPointName, String id,
         int memoryBank, int offset, int length, String[] passwords)
			throws HardwareException {

		// Select channel
		if (!selectReadPoint(readPointName)) {
			log.error("readBytes: Read point not available");
			throw new HardwareException("Read point not available");
		}

		int blockSize = getBlockSize();

		if (blockSize == 0) {
			log.error("readBytes: Detected transponder blocksize is not valid");
			return new UnsignedByteArray(new byte[0]);
		}

		int blockStart = offset / blockSize;
		int blockEnd = (offset + length - 1) / blockSize;
		int blockCount = blockEnd - blockStart + 1;

      byte[] idbytes = ByteBlock.hexStringToByteArray(id);

      // read words and extract requested bytes
      int tries = (passwords == null) ? 2 : (passwords.length + 2);
      byte[] password;
      byte[] data = null;
      boolean success = false;
      for (int i = 0; i < tries; i++) {
         // get password for this try
         if (i == (tries - 1)) {
            password = new byte[] { 0x00, 0x00, 0x00, 0x00 };
         } else if (i == (tries - 2)) {
            password = null;
         } else {
            password = ByteBlock.hexStringToByteArray(passwords[i]);
         }

         // read data
         try {
            data = readMemoryBlocks(idbytes, memoryBank, blockStart,
               blockCount, password);
            success = true;
            break;
         } catch (Exception e) {}
      }
		if (!success) {
			log.error("readBytes: Communication error with reader");
			throw new HardwareException("Communication error with reader");
		}

		byte[] dataTrim = new byte[length];
		System.arraycopy(data, offset % blockSize, dataTrim, 0, length);

		return new UnsignedByteArray(dataTrim);
	}

	private byte[] readMemoryBlocks(byte[] id, int bank, int address,
         int length, byte[] password) throws IOException, HardwareException {

		// log.debug("readMemoryBlocks: started");

		RequestRecord req = new RequestRecord();
		req.comAdr = (byte) 0xff;
		// Set ISO15693 host command
		req.controlByte = (byte) 0xb0;

      int datalen = 7 + id.length;
      if (password != null) {
         datalen = datalen + 1 + password.length;
      }
		byte[] readCommand = new byte[datalen];
		// Set read command (0x23)
		readCommand[0] = (byte) 0x23;
		// Set mode (addressed, UID_LF set, EXT_ADDR set)
		readCommand[1] = (byte) 0x31;
      // Set UID length
      readCommand[2] = (byte) id.length;
		// Set tag address (UID)
		System.arraycopy(id, 0, readCommand, 3, id.length);
      // Set bank
      readCommand[3 + id.length] = (byte) bank;
      if (password != null) {
         // Set A_FLAG
         readCommand[3 + id.length] |= (byte) 0x80;
         // Set password length
         readCommand[4 + id.length] = (byte) password.length;
         // Set password
         System.arraycopy(password, 0, readCommand, 5 + id.length,
               password.length);
         readCommand[5 + id.length + password.length] =
            (byte) ((address >> 8) & 0xFF);
         readCommand[6 + id.length + password.length] =
            (byte) (address & 0xFF);
         readCommand[7 + id.length + password.length] = (byte) (length & 0xFF);
      } else {
         readCommand[4 + id.length] = (byte) ((address >> 8) & 0xFF);
         readCommand[5 + id.length] = (byte) (address & 0xFF);
         readCommand[6 + id.length] = (byte) (length & 0xFF);
      }
		req.data = readCommand;

		ResponseRecord resp = tcpip.sendRequest(req);

		StatusByte status = StatusByte.getStatus(resp.status);
		String statusMessage = getStatusMessage(status);
		if (status != StatusByte.OK) {
			log.error(statusMessage);
			throw new HardwareException("Reading data blocks failed\n" + " "
			      + statusMessage + "/n");
		}

      byte[] data = resp.data;
      int dbn = data[0];
      int dbsize = data[1];

      byte[] readBytes = new byte[dbn * dbsize];
      for (int i = 0; i < dbn; i++) {
         for (int j = 0; j < dbsize; j++) {
            readBytes[i * dbsize + j] = data[3 + i * (dbsize + 1) + j];
         }
      }

		// log.debug("readMemoryBlocks: finished successfuly");

		return readBytes;
	}

	/** TODO: test
	 * (non-Javadoc)
	 *
	 * @see org.fosstrak.hal.HardwareAbstraction#writeBytes(java.lang.String,
	 *      java.lang.String, int, int, UnsignedByteArray, java.lang.String[])
	 */
	public void writeBytes(String readPointName, String id, int memoryBank,
			int offset, UnsignedByteArray data, String[] passwords)
			throws HardwareException, UnsupportedOperationException {

      byte[] idbytes = ByteBlock.hexStringToByteArray(id);
      byte[] databytes = data.toByteArray();
      int datalen = databytes.length;

		// Select channel
		if (!selectReadPoint(readPointName)) {
			throw new HardwareException("Read point not available");
		}

		int blockSize = getBlockSize();

		int blockStart = offset / blockSize;
		int blockEnd = (offset + datalen - 1) / blockSize;
		int blockCount = blockEnd - blockStart + 1;

      // write
      int tries = (passwords == null) ? 2 : (passwords.length + 2);
      byte[] password;
      int offsetStart = offset % blockSize;
      int offsetEnd = (offset + datalen -1) / blockSize;
      boolean success = false;
      for (int i = 0; i < tries; i++) {
         // get password for this try
         if (i == (tries - 1)) {
            password = new byte[] { 0x00, 0x00, 0x00, 0x00 };
         } else if (i == (tries - 2)) {
            password = null;
         } else {
            password = ByteBlock.hexStringToByteArray(passwords[i]);
         }

         // read startblock if write does not start at block address
         byte[] backupStart = null;
         if (offsetStart != 0) {
            try {
               backupStart = readMemoryBlocks(idbytes, memoryBank, blockStart,
                     1, password);
            } catch (Exception e) {
               continue;
            }
         }

         // read endblock if write does not end at block end
         byte[] backupEnd = null;
         if (blockCount > 1 && offsetEnd != 0) {
            try {
               backupEnd = readMemoryBlocks(idbytes, memoryBank, blockEnd, 1,
                     password);
            } catch (Exception e) {
               continue;
            }
         }

         // assemble block data to write
         byte[] blockData = new byte[blockSize * blockCount];
         if (backupStart != null) {
            System.arraycopy(backupStart, 0, blockData, 0, blockSize);
         }
         if (backupEnd != null) {
            System.arraycopy(backupEnd, 0, blockData,
                  blockData.length - blockSize, blockSize);
         }
         System.arraycopy(data.toByteArray(), 0, blockData, offsetStart, datalen);

         // write blocks
         try {
            writeMemoryBlocks(idbytes, memoryBank, blockStart, blockData,
                  password);
            success = true;
         } catch (Exception e) {
            continue;
         }
      }

      if (!success) {
         throw new HardwareException("Write failed.");
      }

	}

	private void writeMemoryBlocks(byte[] id, int memoryBank, int address,
         byte[] data, byte[] password) throws HardwareException,
			UnsupportedOperationException, IOException {

		// log.debug("writeMemoryBlocks: started");

      RequestRecord req = new RequestRecord();
      req.comAdr = (byte) 0xff;
      // Set ISO15693 host command
      req.controlByte = (byte) 0xb0;

      int datalen = 8 + id.length + data.length;
      if (password != null) {
         datalen = datalen + 1 + password.length;
      }
      byte[] writeCommand = new byte[datalen];
      // Set write command (0x24)
      writeCommand[0] = (byte) 0x24;
      // Set mode (addressed, UID_LF set, EXT_ADDR set)
      writeCommand[1] = (byte) 0x31;
      // Set UID length
      writeCommand[2] = (byte) id.length;
      // Set tag address (UID)
      System.arraycopy(id, 0, writeCommand, 3, id.length);
      // Set bank
      writeCommand[3 + id.length] = (byte) memoryBank;
      int dbAddrIndex;
      if (password != null) {
         // Set A_FLAG
         writeCommand[3 + id.length] |= (byte) 0x80;
         // Set password length
         writeCommand[4 + id.length] = (byte) password.length;
         // Set password
         System.arraycopy(password, 0, writeCommand, 5 + id.length,
               password.length);
         dbAddrIndex = 5 + id.length + password.length;
      } else {
         dbAddrIndex = 4 + id.length;
      }
      writeCommand[dbAddrIndex] = (byte) ((address >> 8) & 0xFF);
      writeCommand[dbAddrIndex + 1] = (byte) (address & 0xFF);
      int dbsize = getBlockSize();
      int dbn = data.length / dbsize;
      writeCommand[dbAddrIndex + 2] = (byte) (dbn & 0xFF);
      writeCommand[dbAddrIndex + 3] = (byte) (dbsize & 0xFF);
      System.arraycopy(data, 0, writeCommand, dbAddrIndex + 4, data.length);
      req.data = writeCommand;

		ResponseRecord resp = tcpip.sendRequest(req);

		// Handle response
		StatusByte status = StatusByte.getStatus(resp.status);
		String statusMessage = getStatusMessage(status);
		if (status == StatusByte.WRITE_ERROR) {
			log.error(statusMessage);
			throw new HardwareException("Writing data blocks failed\n" + " "
			      + statusMessage + " at block " + Integer.toHexString(resp.data[0]));
		} else if (status != StatusByte.OK) {
			log.error(statusMessage);
			throw new HardwareException("Writing data blocks failed\n" + " "
			      + statusMessage + "/n");
		}

		// log.debug("writeMemoryBlocks: finished successfuly");
	}

	private String getStatusMessage(StatusByte status) {
		return "Status: " + status.description() + " (0x" + status.code() + ")";
	}

	/** TODO: implement
	 * (non-Javadoc)
	 *
	 * @see org.fosstrak.hal.HardwareAbstraction#writeId(java.lang.String,
	 *      java.lang.String, java.lang.String[])
	 */
	public void writeId(String readPointName, String id, String[] passwords)
			throws HardwareException, UnsupportedOperationException {

      throw new UnsupportedOperationException("writeId not implemented.");

	}

   /**
    * (non-Javadoc)
    *
    * @see org.fosstrak.hal.HardwareAbstraction#supportsWriteId()
    */
   public boolean supportsWriteId() {
      return false;
   }

	/**
	 * (non-Javadoc)
	 *
	 * @see org.fosstrak.hal.HardwareAbstraction#reset()
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
			resp = tcpip.sendRequest(req);
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
    * @see org.fosstrak.hal.HardwareAbstraction#supportsReset()
    */
	public boolean supportsReset() {
	   return true;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see org.fosstrak.hal.HardwareAbstraction#setParameter(java.lang.String,
	 *      java.lang.String)
	 */
	public void setParameter(String param, String value)
			throws HardwareException {
		try {
			config.setProperty(param, value);
			this.initialize();
		} catch (Exception e) {
			log.error("setParameter: Error setting parameter", e);
			throw new HardwareException("Error setting parameter", e);
		}
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see org.fosstrak.hal.HardwareAbstraction#getParameter(java.lang.String)
	 */
	public String getParameter(String param) throws HardwareException {

		try {
			return config.getString(param);
		} catch (Exception e) {
			log.error("getParameter: Error getting parameter", e);
			throw new HardwareException("Error getting parameter", e);
		}
	}

   /**
    * (non-Javadoc)
    *
    * @see org.fosstrak.hal.HardwareAbstraction#supportsParameters()
    */
   public boolean supportsParameters() {
      return true;
   }

	/**
	 * (non-Javadoc)
	 *
	 * @see org.fosstrak.hal.HardwareAbstraction#getAllParameterNames()
	 */
	public String[] getAllParameterNames() throws HardwareException,
			UnsupportedOperationException {
		try {
         Iterator it = config.getKeys();
         Vector<String> names = new Vector<String>();
         Object item;
         while (it.hasNext()) {
            item = it.next();
            if (String.class.isInstance(item)) {
               names.add((String) item);
            }
         }
         String[] namesarray = new String[names.size()];
         namesarray = names.toArray(namesarray);
			return namesarray;
		} catch (Exception e) {
			log.error("getAllParameterNames: Error gettings parameter names", e);
			throw new HardwareException("Error getting parameter names", e);
		}
	}

	/**
	 * Throws RFIDException if error occurs. (returns empty Vector if error
	 * occurs!)
	 *
	 * @return a vector with all the inventory items
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
				resp = tcpip.sendRequest(req);
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
         int index = 1;
         int datasetlen;

			// Process each transponder
			for (int i = 0; i < datasets; i++) {
				InventoryItem item = new InventoryItem();

            datasetlen = ByteBlock.byteToNumber(resp.data[index + 2]) + 3;

				byte trType = resp.data[index];
            item.transponderType = TransponderType.getType(trType);
				item.rfTechnology = RFTechnology.getType(trType);

            if (item.transponderType == TransponderType.EPCclass1Gen2) {
               // TODO: read TID
               item.tid = new byte[] { (byte) 0xE2, 0x00, 0x00, 0x00 };
               item.epcTransponderModel = EPCTransponderModel.
                  getEpcTrasponderModel(item.tid, epcTransponderModelsConfig);
            }

            byte[] idbytes;
            if (item.transponderType == TransponderType.EPCclass1Gen2) {
               // PC bits are sent with the ID of EPCclass1Gen2
               idbytes = new byte[datasetlen - 5];
               System.arraycopy(resp.data, index + 5, idbytes, 0,
                  idbytes.length);
            } else {
               idbytes = new byte[datasetlen - 3];
               System.arraycopy(resp.data, index + 3, idbytes, 0,
                  idbytes.length);
            }
            item.id = ByteBlock.byteArrayToHexString(idbytes);
				inventory.add(item);

            index += datasetlen;
			}
		} while (moreTags);

		return inventory;
	}

	/**
	 * initializes the reader.
	 *
	 * @param address
	 *            the address of the reader
	 * @param port
	 *            the port the reader is listening to
	 * @throws HardwareException
	 *             if an io-error occurs
	 */
	protected void initReader(String address, int port)
			throws HardwareException {

		// create new TCPIPProtocol that opens the socket

		try {
			if (tcpip != null) {
				tcpip.close();
         }
			tcpip = new TCPIPProtocol(address, port, timeout);
		} catch (IOException e) {
			// input/output error
			String message = "initReader: Unknown I/O error";
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
      throw new UnsupportedOperationException("AsynchronousIdentify not implemented.");
	}

	public void stopAsynchronousIdentify()
			throws HardwareException, UnsupportedOperationException {
      throw new UnsupportedOperationException("AsynchronousIdentify not implemented.");
	}

	public boolean isAsynchronousIdentifyRunning() throws HardwareException,
			UnsupportedOperationException {
      throw new UnsupportedOperationException("AsynchronousIdentify not implemented.");
	}

   public void addAsynchronousIdentifyListener(AsynchronousIdentifyListener listener)
         throws HardwareException, UnsupportedOperationException {
      throw new UnsupportedOperationException("AsynchronousIdentify not implemented.");
   }

   public void removeAsynchronousIdentifyListener(AsynchronousIdentifyListener listener)
         throws HardwareException, UnsupportedOperationException {
      throw new UnsupportedOperationException("AsynchronousIdentify not implemented.");
   }

	public boolean supportsAsynchronousIdentify() {
	   return false;
	}

	public boolean isReadPointReady(String readPointName) {
		for (String rp : getReadPointNames()) {
			if (rp.equalsIgnoreCase(readPointName))
				return true;
		}
		return false;
	}

	public boolean supportsIsReadPointReady() {
	   return true;
	}

   // TODO: check, reimplement?
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

		if (!selectReadPoint(item.readPoint)) {
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

      // TODO: adjust kill for TransponderTypes supported by Feig UHF Reader
		if (item.transponderType == TransponderType.TagItHF) {
			killEPC(idd, password);
		} else if (item.transponderType == TransponderType.ICodeEPC) {
			throw new UnsupportedOperationException("TODO");
		} else {
			throw new UnsupportedOperationException("Transponder "
			      + item.transponderType.toString() + " does not support kill");
		}
	}

	private void killEPC(String id, String password)
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
			resp = tcpip.sendRequest(req);
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
		return false;
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

	public String[] getSupportedTransponderTypes() {
		return new String[1];
	}

}