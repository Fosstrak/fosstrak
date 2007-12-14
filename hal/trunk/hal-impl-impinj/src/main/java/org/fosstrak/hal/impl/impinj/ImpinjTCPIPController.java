package org.accada.hal.impl.impinj;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.accada.hal.AsynchronousIdentifyListener;
import org.accada.hal.HardwareAbstraction;
import org.accada.hal.HardwareException;
import org.accada.hal.MemoryBankDescriptor;
import org.accada.hal.MemoryDescriptor;
import org.accada.hal.Observation;
import org.accada.hal.ReadPointNotFoundException;
import org.accada.hal.TagDescriptor;
import org.accada.hal.Trigger;
import org.accada.hal.UnsignedByteArray;
import org.accada.hal.UnsupportedOperationException;
import org.accada.hal.impl.impinj.comm.Frame;
import org.accada.hal.impl.impinj.comm.FrameException;
import org.accada.hal.impl.impinj.comm.FrameParameters;
import org.accada.hal.impl.impinj.comm.FrameParametersSpec;
import org.accada.hal.impl.impinj.comm.TCPIPProtocol;
import org.accada.hal.transponder.IDType;
import org.accada.hal.transponder.InventoryItem;
import org.accada.hal.transponder.EPCTransponderModel;
import org.accada.hal.transponder.RFTechnology;
import org.accada.hal.transponder.TransponderType;
import org.accada.hal.util.ByteBlock;
import org.accada.hal.util.ResourceLocator;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

/**
 *
 * @author Jonas Haller
 *
 */
public class ImpinjTCPIPController implements HardwareAbstraction {

   /**
    * The maximum time in milliseconds to suppress notifications in
    * asynchronous identification to prevent overwhelming the listener(s).
    */
   private static final long MAX_NOTIFICATION_SUPPRESSION = 100;

	static Logger log = Logger.getLogger(ImpinjTCPIPController.class);

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
   private String defaultConfigFile = "/props/ImpinjTCPIPController_default.xml";
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
    * The current inventory (all seen ids but only at one antenna)
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
    * If reset is called from init (do not reinitialize HAL on error) or from
    * outside of this class (reinitiazile whole HAL on reset error)
    */
   private boolean resetfrominit = false;

   /**
    * AsynchronousIdentify object and thread
    */
   private AsynchronousIdentify asyncIdent;
   private Thread asyncIdentThread;

	/**
	 *
	 * @param halName
	 */
	public ImpinjTCPIPController(String halName, String configFile) {
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
         throw new HardwareException("Properties file not found.", e);
      }

      // Create AsynchronousIdentify object and thread
      asyncIdent = new AsynchronousIdentify();
      asyncIdentThread = new Thread(asyncIdent);

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
	 * @see org.accada.hal.HardwareAbstraction#identify(java.lang.String[])
	 */
	public Observation[] identify(String[] readPointNames)
			throws HardwareException {

      currentInventory.clear();

      Observation[] observations = new Observation[readPointNames.length];
      Vector<String>[] ids_vec_arr = new Vector[readPointNames.length];
      HashMap<String, Integer> name2index = new HashMap<String, Integer>();

      for (int i = 0; i < readPointNames.length; i++) {
   		observations[i] = new Observation();
   		observations[i].setHalName(getHALName());
   		observations[i].setReadPointName(readPointNames[i]);
         ids_vec_arr[i] = new Vector<String>();
         name2index.put(readPointNames[i], i);
      }

      selectReadPoints(readPointNames);

      Vector<InventoryItem> items = null;
      items = getInventory();

		for (InventoryItem item : items) {
			String id = item.id;

			ids_vec_arr[name2index.get(item.readPoint)].add(id);
			currentInventory.put(id, item);
		}

      for (int i = 0; i < readPointNames.length; i++) {
   		int len = ids_vec_arr[i].size();
   		String[] ids_arr = new String[len];
   		ids_arr = ids_vec_arr[i].toArray(ids_arr);
         TagDescriptor[] td_arr = new TagDescriptor[ids_arr.length];
         for (int j = 0; j < td_arr.length; j++) {
				IDType idType = IDType.getIdType("EPC",
						config.getString("idTypesConfig"));
            EPCTransponderModel tagModel = currentInventory.get(ids_arr[j]).
               epcTransponderModel;
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
            td_arr[j] = new TagDescriptor(idType, memoryDescriptor);
         }
   		observations[i].setTagDescriptors(td_arr);
         observations[i].setIds(ids_arr);
         observations[i].setTimestamp(System.currentTimeMillis());
      }

		return observations;
	}

   /**
    * Select a read point
    *
    * @param readpointName
    *          the name of the read point
    * @return
    *          true if read point selected
    * @throws HardwareException
    *          if read point not available or selecting antenna connectors failed
    */
   protected void selectReadPoint(String readpointName)
         throws HardwareException {

      String[] readpoints = new String[] { readpointName };
      selectReadPoints(readpoints);

   }

   /**
    * Select read points
    *
    * @param readpointNames
    *          the names of the read points
    * @return
    *          true if read point selected
    * @throws HardwareException
    *          if read point not available or selecting antenna connectors failed
    */
   protected void selectReadPoints(String[] readpointNames)
         throws HardwareException{

      // set antenna outputs according to readpointNames list
      int antennaConnectors = 0;
      int connector, leftshift;
      Integer connectorInt = null;
      for (int i = 0; i < readpointNames.length; i++) {
         connectorInt = (Integer) readPoints.get(readpointNames[i]);
         if (connectorInt == null) {
            throw new HardwareException("Read point '" + readpointNames[i] +
                  "' not available.");
         }
         connector = connectorInt.intValue();
         leftshift = connector - 1;
         antennaConnectors |= 0x01 << leftshift;
      }

      selectAntennaConnectors(antennaConnectors);
   }

   /**
    * Select read points
    *
    * @param readpointNames
    *          the names of the read points
    * @return
    *          true if read point selected
    * @throws HardwareException
    *          if selecting antenna connectors failed
    */
   protected void selectAllReadPoints() throws HardwareException{

      // set antenna outputs according to configfile
      Iterator connectorIter = readPoints.values().iterator();
      int antennaConnectors = 0;
      int connector, leftshift;
      while (connectorIter.hasNext()) {
         connector = (Integer) connectorIter.next();
         leftshift = connector - 1;
         antennaConnectors |= (0x01 << leftshift);
      }

      selectAntennaConnectors(antennaConnectors);
   }

   /**
    * Selects antenna connectors according to 'Impinj Speedway Reference Design
    * / Mach1 API Specification, Operating Command Set version 2.8.0, chapter
    * 2.3.6 SetAntenna'. Bit 0 corresponds to antenna 1, bit 1 to antenna 2,
    * bit 2 to antenna 3 and bit 3 to antenna 4.
    *
    * @param connectors
    *          the connectors to select (0x00 - 0x0F)
    * @throws HardwareException
    *          if selecting antenna connectors failed
    */
   synchronized protected void selectAntennaConnectors(int connectors)
         throws HardwareException {

      // activate given antennas
      try {
         // declare variables for command and response
         Frame c, r;

         // set antenna outputs
         byte[] ant_data = new byte[1];
         ant_data[0] = (byte) (connectors & 0x0F);
         c = new Frame(Frame.Type.COMMAND, Frame.Category.OPERATING_MODEL,
               0x07, ant_data);
         r = tcpip.sendRequest(c);
         if ((r.getData().length == 0) || (r.getData()[0] != 0x00)) {
            throw new FrameException("Antenna port configuration failed.");
         }

      } catch (IOException e) {
         // input/output error
         String message = "Selecting antenna connectors failed, I/O error";
         log.error(message,e);
         throw new HardwareException(message, e);
      } catch (FrameException fe) {
         // frame error
         log.error(fe.getMessage());
         throw new HardwareException(fe.getMessage(), fe);
      }

   }

   /**
	 * (non-Javadoc)
	 *
	 * @see org.accada.hal.HardwareAbstraction#getReadPointNames()
	 */
	public String[] getReadPointNames() {
      Set<String> keyset = readPoints.keySet();
      return keyset.toArray(new String[keyset.size()]);
	}

	/**
	 * The Mach1 protocol uses word (2 bytes) for memory access
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
	 * @see org.accada.hal.HardwareAbstraction#readBytes(java.lang.String,
	 *      java.lang.String, int, int, java.lang.String[])
	 */
	public UnsignedByteArray readBytes(String readPointName, String id,
         int memoryBank, int offset, int length, String[] passwords)
			throws HardwareException {

      byte[] idbytes = ByteBlock.hexStringToByteArray(id);

      // calculate block addresses
		int blockSize = getBlockSize();
		if (blockSize == 0) {
			log.error("readBytes: Detected transponder blocksize is not valid");
			return new UnsignedByteArray(new byte[0]);
		}
		int blockStart = offset / blockSize;
		int blockEnd = (offset + length - 1) / blockSize;
		int blockCount = blockEnd - blockStart + 1;

      // select readPointName
      selectReadPoint(readPointName);

      // inventory with halt on EPC
      try {
         haltEPC(idbytes);
      } catch (Exception e) {
         throw new HardwareException(e.getMessage(), e);
      }

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

      try {
         stopModem();
      } catch (Exception e) {
         throw new HardwareException(e.getMessage(), e);
      }

      if (!success) {
         throw new HardwareException("Write failed.");
      }

      if (data.length < (blockSize * blockCount)) {
         throw new HardwareException("Could not read all bytes.");
      }
      byte[] dataTrim = new byte[length];
      System.arraycopy(data, offset % blockSize, dataTrim, 0, length);

		return new UnsignedByteArray(dataTrim);
	}

	private byte[] readMemoryBlocks(byte[] id, int memoryBank, int address,
         int length, byte[] password) throws FrameException, IOException {

		// log.debug("readMemoryBlocks: started");

      Frame c, n;
      int cmdlen;
      boolean ispw;

      // TagReadCmd
      if ((password == null) || (password.length < 4)) {
         cmdlen = 4;
         ispw = false;
      } else {
         cmdlen = 9;
         ispw = true;
      }
      byte[] cmddata = new byte[cmdlen];
      cmddata[0] = (byte) (memoryBank & 0xFF);
      cmddata[1] = (byte) ((address >> 8) & 0xFF);
      cmddata[2] = (byte) (address & 0xFF);
      cmddata[3] = (byte) (length & 0xFF);
      if (ispw) {
         cmddata[4] = 0x01;
         System.arraycopy(password, 0, cmddata, 5, 4);
      }
      c = new Frame(Frame.Type.COMMAND, Frame.Category.OPERATING_MODEL,
            0x17, cmddata);
      tcpip.sendRequest(c);

      // receive TagReadNtf
      n = tcpip.receiveNotification(Frame.Category.OPERATING_MODEL, 0x05);
      int nlen = n.getData().length;
      if ((nlen < 4) || (n.getData()[0] != 0x00)) {
         throw new IOException("Read failed, ReadResultCode: " +
               n.getData()[0] + ".");
      }
      byte[] data = new byte[nlen - 4];
      System.arraycopy(n.getData(), 4, data, 0, nlen - 4);

		// log.debug("readMemoryBlocks: finished successfuly");

		return data;
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

      byte[] idbytes = ByteBlock.hexStringToByteArray(id);
      byte[] databytes = data.toByteArray();
		int datalen = databytes.length;

      // calculate block addresses
		int blockSize = getBlockSize();
		if (blockSize == 0) {
			log.error("readBytes: Detected transponder blocksize is not valid");
			return;
		}
		int blockStart = offset / blockSize;
		int blockEnd = (offset + datalen - 1) / blockSize;
		int blockCount = blockEnd - blockStart + 1;

      // select readPointName
      selectReadPoint(readPointName);

      // inventory with halt on EPC
      try {
         haltEPC(idbytes);
      } catch (Exception e) {
         throw new HardwareException(e.getMessage(), e);
      }

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

      try {
         stopModem();
      } catch (Exception e) {
         throw new HardwareException(e.getMessage(), e);
      }

      if (!success) {
         throw new HardwareException("Write failed.");
      }
   }

	private void writeMemoryBlocks(byte[] id, int memoryBank, int address,
         byte[] data, byte[] password) throws FrameException, IOException {

		// log.debug("writeMemoryBlocks: started");

      Frame c, n;
      int cmdlen;
      boolean ispw;

      // TagReadCmd
      if ((password == null) || (password.length < 4)) {
         cmdlen = 6 + data.length;
         ispw = false;
      } else {
         cmdlen = 6 + data.length + 5;
         ispw = true;
      }
      byte[] cmddata = new byte[cmdlen];
      cmddata[0] = (byte) (memoryBank & 0xFF);
      cmddata[1] = (byte) ((address >> 8) & 0xFF);
      cmddata[2] = (byte) (address & 0xFF);
      cmddata[3] = (byte) ((data.length >> 8) & 0xFF);
      cmddata[4] = (byte) (data.length & 0xFF);
      System.arraycopy(data, 0, cmddata, 5, data.length);
      cmddata[5+data.length] = 0x01; // use a Gen2 Write command (CoverCoding)
      if (ispw) {
         cmddata[6+data.length] = 0x01;
         System.arraycopy(password, 0, cmddata, 7+data.length, 4);
      }
      c = new Frame(Frame.Type.COMMAND, Frame.Category.OPERATING_MODEL,
            0x18, cmddata);
      tcpip.sendRequest(c);

      // receive TagWriteNtf
      n = tcpip.receiveNotification(Frame.Category.OPERATING_MODEL, 0x06);
      byte[] ndata = n.getData();
      int nlen = ndata.length;
      if ((nlen == 4) && (ndata[0] != 0x00)) {
         // write failed
         int erraddr = (((int) ndata[2]) << 8) | ((int) ndata[3]);
         throw new IOException("Write failed at word address " + erraddr + ".");
      }
      if ((nlen != 1) || (ndata[0] != 0x00)) {
         // parameter error
         throw new FrameException("Failed to receive TagWriteNtf.");
      }

		log.debug("writeMemoryBlocks: finished successfuly");
	}

   /**
	 * (non-Javadoc)
	 *
	 * @see org.accada.hal.HardwareAbstraction#programTagId(java.lang.String,
	 *      java.lang.String[])
	 */
	public void writeId(String readPointName, String id, String[] passwords)
			throws ReadPointNotFoundException, HardwareException,
         UnsupportedOperationException {

      // select readPointName
      selectReadPoint(readPointName);

      // assemble PC and EPC
      byte[] idbytes = ByteBlock.hexStringToByteArray(id);
      int idlen = idbytes.length;
      int blockSize = getBlockSize();
      if ((idlen % blockSize) != 0) {
         throw new HardwareException("ID length not multiple of word length.");
      }
      int blocks = idlen / blockSize;
      if (blocks > 31) {
         throw new HardwareException("ID length greater than 31 word maximum.");
      }
      int PCbits = ((blocks & 0x0000001F) << 11);
      byte[] iddata = new byte[idlen+blockSize];
      iddata[0] = (byte) ((PCbits >>8) & 0xFF);
      iddata[1] = (byte) (PCbits & 0xFF);
      System.arraycopy(idbytes, 0, iddata, blockSize, idlen);

      // inventory with halt on every tag
      byte[] epc;
      try {
         epc = haltNextTag();
      } catch (Exception e) {
         throw new HardwareException(e.getMessage(), e);
      }

      // write new id data
      final int epcBank = 1;
      final int pcEpcStart = 1;
      boolean success = false;
      int tries = (passwords == null) ? 2 : (passwords.length + 2);
      byte[] password;
      for (int i = 0; i < tries; i++) {
         // get password for this try
         if (i == (tries - 1)) {
            password = null;
         } else if (i == (tries - 2)) {
            password = new byte[] { 0x00, 0x00, 0x00, 0x00 };
         } else {
            password = ByteBlock.hexStringToByteArray(passwords[i]);
         }

         // write blocks
         try {
            writeMemoryBlocks(epc, epcBank, pcEpcStart, iddata,
                  password);
            success = true;
         } catch (Exception e) {}
      }

      // stop modem
      try {
         stopModem();
      } catch (Exception e) {
         throw new HardwareException(e.getMessage(), e);
      }

      // check success
      if (!success) {
         throw new HardwareException("Write ID failed.");
      }
	}

   /**
    * (non-Javadoc)
    *
    * @see org.accada.hal.HardwareAbstraction#supportsWriteId()
    */
   public boolean supportsWriteId() {
      return true;
   }

	/**
	 * (non-Javadoc)
	 *
	 * @see org.accada.hal.HardwareAbstraction#reset()
	 */
	public void reset() throws HardwareException {
      try {
         // declare variables for command, response and notification
         Frame c, r, n;

         // StutdownModem
         c = new Frame(Frame.Type.COMMAND, Frame.Category.MODEM_MANAGEMENT,
               0x05, null);
         tcpip.sendRequest(c);

         // BootModem
         c.setMessageID(0x04); // recycle ShutdownModem command frame
         tcpip.sendRequest(c);
         do {
            n = tcpip.receiveNotification(Frame.Category.MODEM_MANAGEMENT,
                  0x02, 2000);
         } while (n.getData()[0] == 0x01);
         if (n.getData()[0] != 0x00) {
            throw new FrameException("BootModem failed, BootResultCode: " +
                  n.getData()[0] + ".");
         }

         // SetRegulatoryRegion
         int regulatoryRegion = config.getInt("regulatoryRegion");
         byte[] rr_data = new byte[2];
         rr_data[0] = (byte) ((regulatoryRegion >> 8) & 0xFF);
         rr_data[1] = (byte) (regulatoryRegion & 0xFF);
         c = new Frame(Frame.Type.COMMAND, Frame.Category.OPERATING_MODEL,
               0x09, rr_data);
         r = tcpip.sendRequest(c);
         if (r.getData()[0] != 0x00) {
            throw new FrameException("Regulatory region not accepted, " +
                  "ResultCode: " + r.getData()[0] + ".");
         }
         n = tcpip.receiveNotification(Frame.Category.OPERATING_MODEL, 0x0A);
         if (n.getData()[0] != 0x00) {
            throw new FrameException("Setting regulatory region failed, " +
                  "ResultCode: " + n.getData()[0] + ".");
         }

      } catch (Exception e) {
         // frame or I/O error
         if (resetfrominit) {
            resetfrominit = false;
            log.error(e.getMessage());
            throw new HardwareException(e.getMessage(), e);
         } else {
            initialize();
         }
      }
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
         if (param.startsWith("reader.")) {
            // reader parameter
            String key = param.substring(7);
            boolean success = false;
            Frame c, r;
            if (key.equals("TxPower")) {
               // SetTxPower
               byte[] data = ByteBlock.hexStringToByteArray(value);
               c = new Frame(Frame.Type.COMMAND, Frame.Category.OPERATING_MODEL,
                     0x05, data);
               r = tcpip.sendRequest(c);
               if (r.getData()[0] != 0x00) {
                  throw new HardwareException("Setting parameter failed.");
               }
               success = true;
            }
            if (key.equals("Gen2Params")) {
               // SetGen2Params
               byte[] data = ByteBlock.hexStringToByteArray(value);
               c = new Frame(Frame.Type.COMMAND, Frame.Category.OPERATING_MODEL,
                     0x0D, data);
               r = tcpip.sendRequest(c);
               if (r.getData()[0] != 0x00) {
                  throw new HardwareException("Setting parameter failed.");
               }
               success = true;
            }
            if (key.equals("LBTParams")) {
               // SetLBTParams
               byte[] data = ByteBlock.hexStringToByteArray(value);
               c = new Frame(Frame.Type.COMMAND, Frame.Category.OPERATING_MODEL,
                     0x1F, data);
               r = tcpip.sendRequest(c);
               if (r.getData()[0] != 0x00) {
                  throw new HardwareException("Setting parameter failed.");
               }
               success = true;
            }
            if (key.equals("RxConfig")) {
               // SetRxConfig
               byte[] data = ByteBlock.hexStringToByteArray(value);
               c = new Frame(Frame.Type.COMMAND, Frame.Category.OPERATING_MODEL,
                     0x22, data);
               r = tcpip.sendRequest(c);
               if (r.getData()[0] != 0x00) {
                  throw new HardwareException("Setting parameter failed.");
               }
               success = true;
            }
            if (!success) {
               throw new HardwareException("Parameter '" + param + "' not " +
                     "available for writing.");
            }
         } else {
            // config file parameter
            config.setProperty(param, value);
         }
      } catch (HardwareException he) {
         log.error(he.getMessage());
         throw he;
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

      String value = null;
      try {
         if (param.startsWith("reader.")) {
            // reader parameter
            String key = param.substring(7);
            boolean success = false;
            Frame c, r;
            if (key.equals("ReaderInfo")) {
               // GetReaderInfo
               c = new Frame(Frame.Type.COMMAND, Frame.Category.MODEM_MANAGEMENT,
                     0x01, null);
               r = tcpip.sendRequest(c);
               value = ByteBlock.byteArrayToHexString(r.getData());
               success = true;
            }
            if (key.equals("TxPower")) {
               // GetTxPower
               c = new Frame(Frame.Type.COMMAND, Frame.Category.OPERATING_MODEL,
                     0x06, null);
               r = tcpip.sendRequest(c);
               value = ByteBlock.byteArrayToHexString(r.getData());
               success = true;
            }
            if (key.equals("Gen2Params")) {
               // GetGen2Params
               c = new Frame(Frame.Type.COMMAND, Frame.Category.OPERATING_MODEL,
                     0x0E, new byte[] { 0x01, 0x01});
               r = tcpip.sendRequest(c);
               value = ByteBlock.byteArrayToHexString(r.getData());
               success = true;
            }
            if (key.equals("SupportedGen2Params")) {
               // GetSupportedGen2Params
               c = new Frame(Frame.Type.COMMAND, Frame.Category.OPERATING_MODEL,
                     0x1C, new byte[] { 0x01, 0x01, 0x02, 0x01});
               r = tcpip.sendRequest(c);
               value = ByteBlock.byteArrayToHexString(r.getData());
               success = true;
            }
            if (key.equals("LBTParams")) {
               // GetLBTParams
               c = new Frame(Frame.Type.COMMAND, Frame.Category.OPERATING_MODEL,
                     0x20, null);
               r = tcpip.sendRequest(c);
               value = ByteBlock.byteArrayToHexString(r.getData());
               success = true;
            }
            if (key.equals("RxConfig")) {
               // GetRxConfig
               c = new Frame(Frame.Type.COMMAND, Frame.Category.OPERATING_MODEL,
                     0x23, null);
               r = tcpip.sendRequest(c);
               value = ByteBlock.byteArrayToHexString(r.getData());
               success = true;
            }
            if (!success) {
               throw new HardwareException("Parameter '" + param + "' not " +
                     "available for reading.");
            }
         } else {
            // config file parameter
            value = config.getString(param);
         }
		} catch (HardwareException he) {
         log.error(he.getMessage());
         throw he;
      } catch (Exception e) {
			log.error("getParameter: Error getting parameter", e);
			throw new HardwareException("Error getting parameter", e);
		}
      return value;
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
	 * Throws HardwareException if error occurs.
	 *
	 * @return
    *          Vector of InventoryItems seen on activated antennas
    * @throws HardwareException
    *          if an error occures
	 */
	synchronized protected Vector<InventoryItem> getInventory()
			throws HardwareException {

	   final int MUX_ROUNDS = 2;
		Vector<InventoryItem> inventory = new Vector<InventoryItem>();

		Frame c, r, n;
      try {
         // enable inventory attempt count reporting
         byte[] iacr = new byte[] { 0x00, 0x03, 0x01 };
         c = new Frame(Frame.Type.COMMAND, Frame.Category.OPERATING_MODEL,
               0x1E, iacr);
         r = tcpip.sendRequest(c);
         if (r.getData()[0] != 0x00) {
            throw new HardwareException("Could not enable "
                  + "InventoryAttemptCount reporting.");
         }

         // inventory command (read TID during inventory)
         byte[] data = new byte[] { 0x0c, 0x02, 0x0d, 0x00, 0x00, 0x0e, 0x02 };
         c = new Frame(Frame.Type.COMMAND, Frame.Category.OPERATING_MODEL,
               0x13, data);
  			r = tcpip.sendRequest(c);
         if (r.getData()[0] != 0x00) {
            throw new HardwareException("Inventory command failed, " +
                  "ResultCode: " + r.getData()[0] + ".");
         }

         // storage structures for inventory status
         int catmid;
         FrameParameters params;
         // set transponder type to EPCclass1gen2
         byte trType = (byte) 0x84;
         String id, key;
         int antenna;
         Vector<String> seen = new Vector<String>();
         byte[] iac; // InventoryAttemptCount
         int roundcount = 0;

         // process notifications (InventoryNtf, InventoryStatusNtf and others)
			do {
            // receive next notification
			   n = tcpip.receiveNextNotification();
            catmid = ((n.getCategory().toInt() & 0x0F) << 8) |
               (n.getMessageID() & 0xFF);

            // switch for different notification types
            switch (catmid) {
            case 0x0101:
               // InventoryNtf
               params = new FrameParameters(n.getData(),
                  new FrameParametersSpec(FrameParametersSpec.InventoryNtf));
               id = ByteBlock.byteArrayToHexString(params.
                  getParameter(0x0100));
               key = id + ByteBlock.byteToHexString(params.
                  getParameter(0x04)[0]);

               if (!seen.contains(key)) {
                  InventoryItem item = new InventoryItem();

                  item.transponderType = TransponderType.getType(trType);
                  item.rfTechnology = RFTechnology.getType(trType);

                  int readresult = params.getParameter(0x01)[0];
                  if (readresult == 0) {
                     item.tid = params.getParameter(0x02);
                  } else {
                     item.tid = new byte[] { (byte) 0xE2, 0x00, 0x00, 0x00 };
                  }
                  item.epcTransponderModel = EPCTransponderModel.
                     getEpcTrasponderModel(item.tid, epcTransponderModelsConfig);

                  item.id = id;

                  antenna = params.getParameter(0x04)[0] & 0xFF;
                  for (int i = 0; i < 4; i++) {
                     if (((antenna >> i) & 0x01) > 0x00) {
                        antenna = i + 1;
                        break;
                     }
                  }
                  item.readPoint = antennaNames.get(antenna);

                  inventory.add(item);
                  seen.add(key);
               }
               break;
            case 0x0102:
               // InventoryStatusNtf
               params = new FrameParameters(n.getData(),
                  new FrameParametersSpec(FrameParametersSpec.
                  InventoryStatusNtf));
               if ((params.getParameter(0x0100)[0] & 0xFF) != 0x00) {
                  // transmitter enabled, more params available
                  iac = params.getParameter(0x03);
                  roundcount = ((iac[0] & 0xFF) << 8) | (iac[1] & 0xFF);
               }
               break;
            default:
               // omit other notifications
               break;
            }

			} while (roundcount < MUX_ROUNDS);

         // stop the inventory
         stopModem();

      } catch (FrameException fe) {
         try { stopModem(); } catch (Exception e) {}
         throw new HardwareException(fe.getMessage(), fe);
      } catch (IOException ioe) {
         try { stopModem(); } catch (Exception e) {}
         throw new HardwareException(ioe.getMessage(), ioe);
      }

		return inventory;
	}

   /**
    * Stop the modem.
    *
    * @throws FrameException
    *          if creating StopModemCmd fails
    * @throws IOException
    *          if communication error occurs
    */
   synchronized protected void stopModem() throws FrameException, IOException {

      Frame c, n;
      int catmid;

      // modem stop command
      c = new Frame(Frame.Type.COMMAND, Frame.Category.OPERATING_MODEL,
            0x16, null);
      tcpip.sendRequest(c);

      // process notification until ModemStoppedNtf arrives
      boolean modemstopped = false;
      while (!modemstopped) {
         // receive next notification
         n = tcpip.receiveNextNotification();
         catmid = ((n.getCategory().toInt() & 0x0F) << 8) |
            (n.getMessageID() & 0xFF);

         // switch for different notification types
         switch (catmid) {
         case 0x0104:
            // ModemStoppedNtf
            modemstopped = true;
            break;
         default:
            // omit other notifications
            break;
         }
      }
   }

   /**
    * Start an inventory and halt on given EPC.
    *
    * @param aEPC
    *          the EPC to halt for
    * @throws FrameException
    *          on error in frame and parameter handling
    * @throws IOException
    *          on communication error
    */
   synchronized protected void haltEPC(byte[] aEPC) throws FrameException,
         IOException {

      final int MUX_ROUNDS = 2;
      Frame c, r, n;
      boolean halted = false;

      // enable inventory attempt count reporting
      byte[] iacr = new byte[] { 0x00, 0x03, 0x01 };
      c = new Frame(Frame.Type.COMMAND, Frame.Category.OPERATING_MODEL,
            0x1E, iacr);
      r = tcpip.sendRequest(c);
      if (r.getData()[0] != 0x00) {
         throw new IOException("Could not enable InventoryAttemptCount "
               + "reporting.");
      }

      // inventory command with halt filter
      int epclen = aEPC.length;
      int datalen = 18 + 2 * epclen;
      byte[] data = new byte[datalen];
      // operation (halt filter A), bank (EPC bank 1) and offset (0x20 EPC start)
      byte[] obo = new byte[] { 0x0f, 0x00, 0x10, 0x01, 0x11, 0x00, 0x20 };
      System.arraycopy(obo, 0, data, 0, 7);
      data[7] = 0x12; // length
      data[8] = (byte) (((epclen * 8) >> 8) & 0xFF);
      data[9] = (byte) ((epclen * 8) & 0xFF);
      data[10] = 0x13; // mask
      data[11] = (byte) ((epclen >> 8) & 0xFF);
      data[12] = (byte) (epclen & 0xFF);
      for (int i = 0; i < epclen; i++) {
         data[13+i] = (byte) 0xFF;
      }
      data[13+epclen] = 0x14; // value
      data[14+epclen] = (byte) ((epclen >> 8) & 0xFF);
      data[15+epclen] = (byte) (epclen & 0xFF);
      System.arraycopy(aEPC, 0, data, 16+epclen, epclen);
      data[16+2*epclen] = 0x15; // compare
      data[17+2*epclen] = 0x00;
      c = new Frame(Frame.Type.COMMAND, Frame.Category.OPERATING_MODEL,
            0x13, data);
      r = tcpip.sendRequest(c);
      if (r.getData()[0] != 0x00) {
         throw new IOException("Inventory configuration failed.");
      }

      // storage structures for inventory status
      int catmid;
      FrameParameters params;
//      int antenna;
      byte[] iac; // InventoryAttemptCount
      int roundcount = 0;

      // process notifications (InventoryNtf, InventoryStatusNtf and others)
      do {
         // receive next notification
         n = tcpip.receiveNextNotification();
         catmid = ((n.getCategory().toInt() & 0x0F) << 8) |
            (n.getMessageID() & 0xFF);

         // switch for different notification types
         switch (catmid) {
         case 0x0101:
            // InventoryNtf
            params = new FrameParameters(n.getData(),
               new FrameParametersSpec(FrameParametersSpec.InventoryNtf));
            if (params.getParameter(0x0101)[0] == 0x01) {
               if(ByteBlock.byteArrayToHexString(params.getParameter(0x0100)).
                     equals(ByteBlock.byteArrayToHexString(aEPC))) {
                  halted = true;
               }
            }
            /*antenna = params.getParameter(0x04)[0] & 0xFF;
            for (int i = 0; i < 4; i++) {
               if (((antenna >> i) & 0x01) > 0x00) {
                  antenna = i + 1;
                  break;
               }
            }*/
            break;
         case 0x0102:
            // InventoryStatusNtf
            params = new FrameParameters(n.getData(),
               new FrameParametersSpec(FrameParametersSpec.
               InventoryStatusNtf));
            if ((params.getParameter(0x0100)[0] & 0xFF) != 0x00) {
               // transmitter enabled, more params available
               iac = params.getParameter(0x03);
               roundcount = ((iac[0] & 0xFF) << 8) | (iac[1] & 0xFF);
            }
            break;
         default:
            // omit other notifications
            break;
         }

      } while ((roundcount < MUX_ROUNDS) && !halted);

      if (!halted) {
         // stop the inventory
         stopModem();
         throw new IOException("EPC not found in inventory.");
      }
   }

   /**
    * Start an inventory and halt on the next tag.
    *
    * @return
    *          the epc of the tag halted on
    * @throws FrameException
    *          on error in frame and parameter handling
    * @throws IOException
    *          on communication error
    */
   synchronized protected byte[] haltNextTag() throws FrameException,
         IOException {

      final int MUX_ROUNDS = 2;
      Frame c, r, n;
      byte[] epc = null;
      boolean halted = false;

      // enable inventory attempt count reporting
      byte[] iacr = new byte[] { 0x00, 0x03, 0x01 };
      c = new Frame(Frame.Type.COMMAND, Frame.Category.OPERATING_MODEL,
            0x1E, iacr);
      r = tcpip.sendRequest(c);
      if (r.getData()[0] != 0x00) {
         throw new IOException("Could not enable InventoryAttemptCount "
               + "reporting.");
      }

      // inventory command with halt filter
      // operation (halt filter A) and length (0 to halt on every tag)
      byte[] data = new byte[] { 0x0f, 0x00, 0x12, 0x00, 0x00 };
      c = new Frame(Frame.Type.COMMAND, Frame.Category.OPERATING_MODEL,
            0x13, data);
      r = tcpip.sendRequest(c);
      if (r.getData()[0] != 0x00) {
         throw new IOException("Inventory command failed, " +
               "ResultCode: " + r.getData()[0] + ".");
      }

      // storage structures for inventory status
      int catmid;
      FrameParameters params;
//      int antenna;
      byte[] iac; // InventoryAttemptCount
      int roundcount = 0;

      // process notifications (InventoryNtf, InventoryStatusNtf and others)
      do {
         // receive next notification
         n = tcpip.receiveNextNotification();
         catmid = ((n.getCategory().toInt() & 0x0F) << 8) |
            (n.getMessageID() & 0xFF);

         // switch for different notification types
         switch (catmid) {
         case 0x0101:
            // InventoryNtf
            params = new FrameParameters(n.getData(),
               new FrameParametersSpec(FrameParametersSpec.InventoryNtf));
            if (params.getParameter(0x0101)[0] == 0x01) {
               epc = params.getParameter(0x0100); // get the epc
               halted = true;
            }
            /*antenna = params.getParameter(0x04)[0] & 0xFF;
            for (int i = 0; i < 4; i++) {
               if (((antenna >> i) & 0x01) > 0x00) {
                  antenna = i + 1;
                  break;
               }
            }*/
            break;
         case 0x0102:
            // InventoryStatusNtf
            params = new FrameParameters(n.getData(),
               new FrameParametersSpec(FrameParametersSpec.
               InventoryStatusNtf));
            if ((params.getParameter(0x0100)[0] & 0xFF) != 0x00) {
               // transmitter enabled, more params available
               iac = params.getParameter(0x03);
               roundcount = ((iac[0] & 0xFF) << 8) | (iac[1] & 0xFF);
            }
            break;
         default:
            // omit other notifications
            break;
         }

      } while ((roundcount < MUX_ROUNDS) && !halted);

      if (!halted) {
         // stop the inventory
         stopModem();
         throw new IOException("EPC not found in inventory.");
      }

      return epc;
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
         // close old connection if existing
			if (tcpip != null) {
				tcpip.close();
         }

         // open new connection
			tcpip = new TCPIPProtocol(address, port, timeout);

         // receive SocketConnectionStatus notification
         Frame n = tcpip.receiveNotification(Frame.Category.MODEM_MANAGEMENT, 0x00);
         if (n.getData()[0] != 0x00) {
            throw new HardwareException("Connection failed, perhaps an other "
                  + "socket is allready open.");
         }

		} catch (IOException e) {
			// input/output error
			String message = "initReader: Unknown I/O error";
			log.error(message,e);
			throw new HardwareException(message, e);
		}

      // reset reader
      resetfrominit = true;
		reset();

      // activate all configured antenna outputs
      selectAllReadPoints();
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
      if (asyncIdent.getRunFlag()) {
         throw new HardwareException("AsynchronousIdentify allready running.");
      }
      asyncIdent.setReadPointNames(readPointNames);
      switch (trigger.getType()) {
      case Trigger.CONTINUOUS:
         asyncIdent.setTimerTrigger(false);
         asyncIdent.setInterval(MAX_NOTIFICATION_SUPPRESSION);
         break;
      case Trigger.TIMER:
         asyncIdent.setTimerTrigger(true);
         asyncIdent.setInterval(trigger.getInteral());
         break;
      }
      asyncIdentThread.start();
	}

   private class AsynchronousIdentify implements Runnable {
      /** flag if thread is running */
      private volatile boolean runFlag = false;
      /** the read point names */
      private String[] readPointNames = new String[0];
      /** if it is a timer trigger (false means continuous trigger) */
      private boolean timerTrigger = false;
      /** the interval for timer trigger or maximum notification suppression
          for continuous trigger */
      private long interval = 0;
      /** the list of listeners */
      private Vector<AsynchronousIdentifyListener> listeners = new
         Vector<AsynchronousIdentifyListener>();
      /**
       * The run method that implements the Runnable Interface.
       */
      public void run() {
         // set the run flag
         runFlag=true;

         HashMap<String, Integer> name2index = new HashMap<String, Integer>();
         for (int i = 0; i < readPointNames.length; i++) {
            name2index.put(readPointNames[i], i);
         }
         startInventory();

         // storage structures for inventory status
         Frame n;
         int catmid;
         FrameParameters params;
         // set transponder type to EPCclass1gen2
         byte trType = (byte) 0x84;
         String id;
         int antenna;
         byte[] iac; // InventoryAttemptCount
         boolean failure = false;
         int lastroundcount = 0;
         Vector<String>[] last_ids_vec_arr = new Vector[readPointNames.length];
         for (int i = 0; i < readPointNames.length; i++) {
            last_ids_vec_arr[i] = new Vector<String>();
         }
         long lastNtfTime = 0;

         while (runFlag) {
            currentInventory.clear();

            Observation[] observations = new Observation[readPointNames.length];
            Vector<String>[] ids_vec_arr = new Vector[readPointNames.length];

            for (int i = 0; i < readPointNames.length; i++) {
               observations[i] = new Observation();
               observations[i].setHalName(getHALName());
               observations[i].setReadPointName(readPointNames[i]);
               ids_vec_arr[i] = new Vector<String>();
            }

            Vector<InventoryItem> inventory = new Vector<InventoryItem>();

            try {
               // process notifications (InventoryNtf, InventoryStatusNtf and others)
               int roundcount;
               boolean roundend = false;
               do {
                  // receive next notification
                  n = tcpip.receiveNextNotification();
                  catmid = ((n.getCategory().toInt() & 0x0F) << 8) |
                     (n.getMessageID() & 0xFF);

                  // switch for different notification types
                  switch (catmid) {
                  case 0x0101:
                     // InventoryNtf
                     params = new FrameParameters(n.getData(),
                        new FrameParametersSpec(FrameParametersSpec.InventoryNtf));
                     id = ByteBlock.byteArrayToHexString(params.
                        getParameter(0x0100));
                     InventoryItem item = new InventoryItem();
                     item.transponderType = TransponderType.getType(trType);
                     item.rfTechnology = RFTechnology.getType(trType);
                     item.id = id;
                     antenna = params.getParameter(0x04)[0] & 0xFF;
                     for (int i = 0; i < 4; i++) {
                        if (((antenna >> i) & 0x01) > 0x00) {
                           antenna = i + 1;
                           break;
                        }
                     }
                     item.readPoint = antennaNames.get(antenna);
                     inventory.add(item);
                     break;
                  case 0x0102:
                     // InventoryStatusNtf
                     params = new FrameParameters(n.getData(),
                        new FrameParametersSpec(FrameParametersSpec.
                        InventoryStatusNtf));
                     if ((params.getParameter(0x0100)[0] & 0xFF) != 0x00) {
                        // transmitter enabled, more params available
                        iac = params.getParameter(0x03);
                        roundcount = ((iac[0] & 0xFF) << 8) | (iac[1] & 0xFF);
                        if (roundcount > lastroundcount) {
                           roundend = true;
                           lastroundcount = roundcount;
                        }
                     }
                     break;
                  default:
                     // omit other notifications
                     break;
                  }
               } while (!roundend);
            } catch (Exception e) {
               runFlag = false;
               failure = true;
            }

            for (InventoryItem item : inventory) {
               ids_vec_arr[name2index.get(item.readPoint)].add(item.id);
               currentInventory.put(item.id, item);
            }

            int len;
            for (int i = 0; i < readPointNames.length; i++) {
               len = ids_vec_arr[i].size();
               String[] ids_arr = new String[len];
               ids_arr = ids_vec_arr[i].toArray(ids_arr);
               TagDescriptor[] td_arr = new TagDescriptor[ids_arr.length];
               for (int j = 0; j < td_arr.length; j++) {
   					IDType idType = IDType.getIdType("EPC",
   							config.getString("idTypesConfig"));
                  EPCTransponderModel tagModel = currentInventory.get(ids_arr[j]).
                     epcTransponderModel;
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
                  td_arr[j] = new TagDescriptor(idType, memoryDescriptor);
               }
               observations[i].setTagDescriptors(td_arr);
               observations[i].setIds(ids_arr);
               observations[i].setTimestamp(System.currentTimeMillis());
               if (failure) {
                  observations[i].setSuccessful(false);
               }
            }

            // check if notification necessary
            boolean notify = false;
            if (timerTrigger) {
               if ((System.currentTimeMillis() - lastNtfTime) > interval) {
                  notify = true;
               }
            } else {
               boolean changed = false;
               int currentlen, lastlen;
               checkchanged:
               for (int i = 0; i < readPointNames.length; i++) {
                  currentlen = ids_vec_arr[i].size();
                  lastlen = last_ids_vec_arr[i].size();
                  if (currentlen != lastlen) {
                     changed = true;
                     break checkchanged;
                  } else {
                     for (String checkid : ids_vec_arr[i]) {
                        if (!last_ids_vec_arr[i].contains(checkid)) {
                           changed = true;
                           break checkchanged;
                        }
                     }
                  }
               }
               last_ids_vec_arr = ids_vec_arr;
               if (((System.currentTimeMillis() - lastNtfTime) > interval) |
                     changed) {
                  notify = true;
               }
            }

            // notify listeners if necessary
            if (notify) {
               for (AsynchronousIdentifyListener listener : listeners) {
                  for (int i = 0; i < readPointNames.length; i++) {
                     listener.asynchronousIdentifyPerformed(observations[i]);
                  }
               }
               lastNtfTime = System.currentTimeMillis();
            }
         }
         // stop the inventory
         try {
            stopModem();
         } catch (Exception e) {}
      }
      /**
       * Activates all configured read points and starts the inventory.
       * Sets the runFlag to false on error.
       */
      private void startInventory() {
         Frame c, r;
         try {
            // activate all read points
            selectAllReadPoints();

            // enable inventory attempt count reporting
            byte[] iacr = new byte[] { 0x00, 0x03, 0x01 };
            c = new Frame(Frame.Type.COMMAND, Frame.Category.OPERATING_MODEL,
                  0x1E, iacr);
            r = tcpip.sendRequest(c);
            if (r.getData()[0] != 0x00) {
               throw new HardwareException("Could not enable "
                     + "InventoryAttemptCount reporting.");
            }

            // inventory command (read TID during inventory)
            byte[] data = new byte[] { 0x0c, 0x02, 0x0d, 0x00, 0x00, 0x0e, 0x02 };
            c = new Frame(Frame.Type.COMMAND, Frame.Category.OPERATING_MODEL,
                  0x13, data);
            r = tcpip.sendRequest(c);
            if (r.getData()[0] != 0x00) {
               throw new HardwareException("Inventory command failed, " +
                     "ResultCode: " + r.getData()[0] + ".");
            }
         } catch (Exception e) {
            runFlag = false;
         }
      }

      /**
       * Set the runFlag.
       *
       * @param runFlag
       *          the new runFlag status
       */
      public void setRunFlag (boolean runFlag) {
        this.runFlag = runFlag;
      }
      /**
       * Get the runFlag.
       *
       * @return
       *          the current runFlag status
       */
      public boolean getRunFlag() {
        return this.runFlag;
      }

      /**
       * Set read point names.
       *
       * @param readPointNames
       *          the read point names
       */
      public void setReadPointNames(String[] readPointNames) {
         this.readPointNames = readPointNames;
      }

      /**
       * Set trigger type.
       *
       * @param timerTrigger
       *          true for timer, false for continuous trigger
       */
      public void setTimerTrigger(boolean timerTrigger) {
         this.timerTrigger = timerTrigger;
      }

      /**
       * Set the interval for timer trigger or maximum notification suppression
       * for continuous trigger.
       *
       * @param interval
       *          the interval
       */
      public void setInterval(long interval) {
         this.interval = interval;
      }

      /**
       * Add an AsynchronousIdentifyListener.
       *
       * @param listener
       *          the listener to add
       */
      public void addListener(AsynchronousIdentifyListener listener) {
         listeners.add(listener);
      }
      /**
       * Remove an AsynchronousIdentifyListener.
       *
       * @param listener
       *          the listener to remove
       * @return
       *          true if listener removed, false if listener was not registered
       */
      public boolean removeListener(AsynchronousIdentifyListener listener) {
         return listeners.remove(listener);
      }
   } // end of AsynchronousIdentify class

	public void stopAsynchronousIdentify()
			throws HardwareException, UnsupportedOperationException {
      asyncIdent.setRunFlag(false);
	}

	public boolean isAsynchronousIdentifyRunning() throws HardwareException,
			UnsupportedOperationException {
      return asyncIdent.getRunFlag();
	}

   public void addAsynchronousIdentifyListener(AsynchronousIdentifyListener listener)
         throws HardwareException, UnsupportedOperationException {
      asyncIdent.addListener(listener);
   }

   public void removeAsynchronousIdentifyListener(AsynchronousIdentifyListener listener)
         throws HardwareException, UnsupportedOperationException {
      asyncIdent.removeListener(listener);
   }

	public boolean supportsAsynchronousIdentify() {
	   return true;
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

	public void kill(String readPointName, String id, String[] passwords)
         throws HardwareException, UnsupportedOperationException {

      // select readPointName only
      selectReadPoint(readPointName);

      // inventory with halt on EPC
      byte[] idbytes = ByteBlock.hexStringToByteArray(id);
      try {
         haltEPC(idbytes);
      } catch (Exception e) {
         throw new HardwareException(e.getMessage(), e);
      }

      // kill
      int tries = (passwords == null) ? 1 : (passwords.length + 1);
      byte[] password;
      boolean success = false;
      for (int i = 0; i < tries; i++) {
         // get password for this try
         if (i == (tries - 1)) {
            password = new byte[] { 0x00, 0x00, 0x00, 0x00 };
         } else {
            password = ByteBlock.hexStringToByteArray(passwords[i]);
         }

         // read data
         try {
            killEPC(idbytes, password);
            success = true;
            break;
         } catch (Exception e) {}
      }

      // stop modem
      try {
         stopModem();
      } catch (Exception e) {
         throw new HardwareException(e.getMessage(), e);
      }

      // check success
      if (!success) {
         throw new HardwareException("Write failed.");
      }
 	}

   public boolean supportsKill() {
      return true;
   }

	private void killEPC(byte[] id, byte[] password)
			throws HardwareException , FrameException , IOException {

      Frame c, n;

      // TagKillCmd
      if ((password == null) || (password.length < 4)) {
         throw new HardwareException("Illegal password format.");
      }
      byte[] cmddata = new byte[4];
      System.arraycopy(password, 0, cmddata, 0, 4);
      c = new Frame(Frame.Type.COMMAND, Frame.Category.OPERATING_MODEL,
            0x1A, cmddata);
      tcpip.sendRequest(c);

      // receive TagKillNtf
      n = tcpip.receiveNotification(Frame.Category.OPERATING_MODEL, 0x08);
      if (n.getData()[0] != 0x00) {
         throw new IOException("Kill failed, KillResultCode: " +
               n.getData()[0] + ".");
      }
	}

	public void shutDownReadPoint(String readPointName)
         throws UnsupportedOperationException {

      throw new UnsupportedOperationException("Not implemented.");
	}

	public boolean supportsShutDownReadPoint() {
	   return false;
	}

	public void startUpReadPoint(String readPointName)
         throws UnsupportedOperationException {

      throw new UnsupportedOperationException("Not implemented.");
	}

	public boolean supportsStartUpReadPoint() {
	   return false;
	}

	public int getReadPointNoiseLevel(String readPointName, boolean normalize)
			throws UnsupportedOperationException {

		throw new UnsupportedOperationException("Not implemented.");
	}

	public boolean supportsGetReadPointNoiseLevel() {
	   return false;
	}

	public int getReadPointPowerLevel(String readPointName, boolean normalize)
			throws UnsupportedOperationException {

		throw new UnsupportedOperationException("Not implemented.");
	}

	public boolean supportsGetReadPointPowerLevel() {
	   return false;
	}

	public String[] getSupportedTransponderTypes() {
		return new String[0];
	}

}