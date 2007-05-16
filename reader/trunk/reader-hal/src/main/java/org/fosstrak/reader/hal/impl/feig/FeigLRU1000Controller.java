package org.accada.reader.hal.impl.feig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import org.accada.reader.hal.AsynchronousIdentifyListener;
import org.accada.reader.hal.ControllerProperties;
import org.accada.reader.hal.HardwareAbstraction;
import org.accada.reader.hal.HardwareException;
import org.accada.reader.hal.Observation;
import org.accada.reader.hal.UnsupportedOperationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Accada Reader Hardware Abstraction Controller for 
 * Feig UHF ID ISC.LRU1000 readers.
 * 
 * The controller is using a TCP connection to communicate with
 * the hardware reader. The address and port are specified in
 * the properties file.
 * 
 * Currently not all functionality of the reader has been implemented. 
 *
 * @author Matthias Lampe, lampe@acm.org
 *
 */
public class FeigLRU1000Controller implements HardwareAbstraction {
	//  ------------------- Constants -----------------------------------------------

	/** the logger */
	private static Log log = LogFactory.getLog(FeigLRU1000Controller.class); 

	//  ------------------- Fields --------------------------------------------------

	/** the name of the HAL */
	private String halName;	
	
	/** the names of all available read points connected to this reader */
	private String[] readPointNames;

	/** the maximum number of read points connected to this reader */
	private int nOfReadPoints = 0;
	
	/** props instance which encapulates all the methods responsible for parameter updates */
	private ControllerProperties props = null;

	/** tpc address */
	private String address;

	/** communication port */
	private int port;

	/** connection timeout in milli seconds */
	private long timeout;
	
	/** socket for connection */
	Socket socket;
	
	/** input stream of socket connection */
	BufferedReader in;

	/** output stream of socket connection */
	PrintWriter out;

	//	--------------- Constructor(s) --------------------------------------------

	  /**
	   * creates a new instance of the controller and initializes all fields.
	   */
	public FeigLRU1000Controller(String halName) {
		
		this.halName = halName;
		try{
			this.initialize();
		}
		catch (Exception e ){
			String message = "Constructor failed: " + e.getMessage();
			log.debug(message);
		}
	}

	
	//	--------------- Methods --------------------------------------------
	
	/**
	 * initialize the reader. 
	 * The method reads the properties from the properties file and 
	 * initializes all important fields.
	 */
	protected void initialize(){
		
		this.props = new ControllerProperties(halName);
		
		try{
			//--- Sets the parameters according to the properties file

			// get the number of read points of the reader
			nOfReadPoints = Integer.parseInt(props.getParameter("numberOfReadPoints"));
			
			// get all the read point names
			readPointNames = new String[nOfReadPoints];
			for (int i = 0; i < nOfReadPoints; i++){
				readPointNames[i] = props.getParameter("readPoint_" + (i + 1));
			}
			
			// get tcp address and port
			address = props.getParameter("address");
			port = Integer.parseInt(props.getParameter("port"));
			
			// get connection timeout
			timeout = Long.parseLong(props.getParameter("timeout"));

			//--- Establish TCP connection to reader
			
		
		}	
		catch (Exception e){
			String message="Couldn't initialize the reader:" + e.getMessage();
			log.debug(message);
		}
	}

	
	/**
	 * connects to the physical reader via TCP.
	 * 
	 * @param address the TCP address of the reader.
	 * @param port the TCP port of the reader.
	 * @throws HardwareException, if the connection fails.
	 */
	protected void connectReader(String address, int port) 
				throws HardwareException {
		// TODO: refine code for the Socket Connection. Use InetAddress instead of host.

        try {
            socket = new Socket(address, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(
            		socket.getInputStream()));
        } catch (UnknownHostException e) {
			String message="Unknown host:" + e.getMessage();
			log.debug(message);
        } catch (IOException e) {
			String message="Could not open socket connection:" + e.getMessage();
			log.debug(message);
        }
	}
	
	/**
	 * disconnects the physical reader.
	 */
	protected void disconnectReader() {
		// TODO: refine code for disconnection here
		out.close();
		try {
			in.close();
        } catch (IOException e) {
			String message="Exception closing input stream: " + e.getMessage();
			log.debug(message);
        }
		try {
			socket.close();
        } catch (IOException e) {
			String message="Exception closing input stream: " + e.getMessage();
			log.debug(message);
        }
		out = null;
		in = null;
		socket = null;
	}
	
	/**
	 * sets the channel that will be used for the next 'identify' or 'read' command
	 * of the physical reader.
	 * 
	 * @param readPointName the name of the readpoint that is set as active.
	 */
	public boolean selectChannel(String readPointName) {
		// TODO: implement code.
		// use ID ISC LRU1000 command 0x6A - RF Output On/Off to set an antenna active for the 
		// next Host Command (0xB0): identify (0x01) or read (0x23) or write (0x24)

		return false;
	}
	
	
	/**
	 * reads all tag IDs from the physical reader using the read point that
	 * has been set.
	 * 
	 * @return a Vector containing all the tagIDs
	 */
	public Vector readSerialNumbers() {
		// TODO: implement code.
		// use the Host Command (0xB0) identify (0x01) to implement this method.
		// should be just sending '02 00 09 FF B0 01 00 18 43' to out
		Vector tagIDs = new Vector();
		
		return tagIDs;
	}
	
	
	//	--------------- Methods implementing HAL Interface ---------------------
	

	
	//--------- Identify, Read and Write

	/* 
	 * identifies the tags in a reader's field. 
	 * 
	 * For readers with multiple readpoints, the readpoints used for the identify
	 * can be specified. If the reader is a single-read-point-reader the array
	 * <code>readPointNames</code> should contain only one read point.
	 * 
	 * @param readPointNames contains the names of all read points to be scanned
	 * 
	 * @return for each read point an <code>Observation</code> object
	 * @throws HardwareException, if an error occured while performing the identify.
	 * @see org.accada.reader.hal.HardwareAbstraction#identify(java.lang.String[])
	 */
	public Observation[] identify(String[] readPointNames)
			throws HardwareException {
		// TODO refine code. the following code is taken from the FeigMultiplexController using serial connection.


		//---- create an observation object for each readpoint involved in the identify
		Observation[] observations = new Observation[readPointNames.length];
		
		for (int i = 0; i < readPointNames.length; i++){			
			//-- set hal name and readpoint name
			observations[i] = new Observation();
			observations[i].setHalName(this.halName);
			observations[i].setReadPointName(readPointNames[i]);
	
			if(! this.selectChannel(readPointNames[i])){
				String message = "Readpoint not valid.";
				throw new HardwareException(HardwareException.SERVICECODE_IDENTIFY, readPointNames[i], this.halName, message);
			}
			
			observations[i].setTagIds(this.readSerialNumbers());
			observations[i].setTimestamp(System.currentTimeMillis());
		}
		return observations;
	}

	
	/* (non-Javadoc)
	 * @see org.accada.reader.hal.HardwareAbstraction#supportsReadBytes()
	 */
	public boolean supportsReadBytes() {
		return false;
	}

	
	/* (non-Javadoc)
	 * @see org.accada.reader.hal.HardwareAbstraction#readBytes(java.lang.String, java.lang.String, int, int, int, java.lang.String[])
	 */
	public byte[] readBytes(String readPointName, String id, int memoryBank,
			int offset, int length, String[] passwords)
			throws HardwareException, UnsupportedOperationException {

		throw new UnsupportedOperationException("Method currently not implemented.");
		//return null;
	}

	
	/* (non-Javadoc)
	 * @see org.accada.reader.hal.HardwareAbstraction#supportsWriteBytes()
	 */
	public boolean supportsWriteBytes() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.accada.reader.hal.HardwareAbstraction#writeBytes(java.lang.String, java.lang.String, int, int, byte[], java.lang.String[])
	 */
	public void writeBytes(String readPointName, String id, int memoryBank,
			int offset, byte[] data, String[] passwords)
			throws HardwareException, UnsupportedOperationException {

		throw new UnsupportedOperationException("Method currently not implemented.");
	}


	//--------- Kill and ProgramID

	/* (non-Javadoc)
	 * @see org.accada.reader.hal.HardwareAbstraction#supportsKill()
	 */
	public boolean supportsKill() {
		return false;
	}

	
	/* (non-Javadoc)
	 * @see org.accada.reader.hal.HardwareAbstraction#kill(java.lang.String, java.lang.String[])
	 */
	public void kill(String id, String[] passwords) throws HardwareException,
			UnsupportedOperationException {

		throw new UnsupportedOperationException("Method currently not implemented.");
	}

	
	/* (non-Javadoc)
	 * @see org.accada.reader.hal.HardwareAbstraction#supportsProgramId()
	 */
	public boolean supportsProgramId() {
		return false;
	}

	
	/* (non-Javadoc)
	 * @see org.accada.reader.hal.HardwareAbstraction#programId(java.lang.String, java.lang.String[])
	 */
	public void programId(String id, String[] passwords)
			throws HardwareException, UnsupportedOperationException {

		throw new UnsupportedOperationException("Method currently not implemented.");
	}

	

	//--------- Asynchronous Identify

	/* (non-Javadoc)
	 * @see org.accada.reader.hal.HardwareAbstraction#startAsynchronousIdentify(java.lang.String[], org.accada.reader.hal.AsynchronousIdentifyListener, java.lang.String, java.lang.String)
	 */
	public void startAsynchronousIdentify(String[] readPointNames,
			AsynchronousIdentifyListener listener, String triggerType,
			String triggerValue) throws HardwareException,
			UnsupportedOperationException {

		throw new UnsupportedOperationException("Method currently not implemented.");
	}
	
	
	/* (non-Javadoc)
	 * @see org.accada.reader.hal.HardwareAbstraction#stopAsynchronousIdentify(org.accada.reader.hal.AsynchronousIdentifyListener)
	 */
	public void stopAsynchronousIdentify(AsynchronousIdentifyListener listener)
			throws HardwareException, UnsupportedOperationException {

		throw new UnsupportedOperationException("Method currently not implemented.");
	}

	/* (non-Javadoc)
	 * @see org.accada.reader.hal.HardwareAbstraction#isAsynchronousIdentifyRunning(org.accada.reader.hal.AsynchronousIdentifyListener)
	 */
	public boolean isAsynchronousIdentifyRunning(
			AsynchronousIdentifyListener listener) throws HardwareException,
			UnsupportedOperationException {

		throw new UnsupportedOperationException("Method currently not implemented.");
		//return false;
	}

	

	//--------- Get and Set Parameters
	
	/* (non-Javadoc)
	 * @see org.accada.reader.hal.HardwareAbstraction#getAllParameterNames()
	 */
	public String[] getAllParameterNames() throws HardwareException,
			UnsupportedOperationException {

		try{
			return this.props.getParameterNames();
		}
		catch (Exception e){
			throw new HardwareException(HardwareException.SERVICECODE_IDENTIFY, null, halName, e.getMessage());
		}
	}

	
	/* (non-Javadoc)
	 * @see org.accada.reader.hal.HardwareAbstraction#getParameter(java.lang.String)
	 */
	public String getParameter(String param) throws HardwareException,
			UnsupportedOperationException {

		try{
			return this.props.getParameter(param);
		}
		catch (Exception e){
			e.printStackTrace();
			throw new HardwareException(HardwareException.SERVICECODE_IDENTIFY, null, halName, e.getMessage());
		}
	}
	
	
	/* (non-Javadoc)
	 * @see org.accada.reader.hal.HardwareAbstraction#setParameter(java.lang.String, java.lang.String)
	 */
	public void setParameter(String param, String value)
			throws HardwareException, UnsupportedOperationException {

		throw new UnsupportedOperationException("Method currently not implemented.");
	}

	
	
	//--------- Reader Management
	
	/* (non-Javadoc)
	 * @see org.accada.reader.hal.HardwareAbstraction#getHalName()
	 */
	public String getHalName() {
		return halName;
	}

	
	/* (non-Javadoc)
	 * @see org.accada.reader.hal.HardwareAbstraction#getReadPointNames()
	 */
	public String[] getReadPointNames() throws HardwareException {
		return readPointNames;
	}

	
	/* (non-Javadoc)
	 * @see org.accada.reader.hal.HardwareAbstraction#getAntennaNoiseLevel(java.lang.String, boolean)
	 */
	public int getAntennaNoiseLevel(String readPointName, boolean normalize) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	/* (non-Javadoc)
	 * @see org.accada.reader.hal.HardwareAbstraction#getAntennaPowerLevel(java.lang.String, boolean)
	 */
	public int getAntennaPowerLevel(String readPointName, boolean normalize) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	/* (non-Javadoc)
	 * @see org.accada.reader.hal.HardwareAbstraction#startUpReadPoint(java.lang.String)
	 */
	public void startUpReadPoint(String readPointName) {
		// TODO Auto-generated method stub

	}

	
	/* (non-Javadoc)
	 * @see org.accada.reader.hal.HardwareAbstraction#isReadPointReady(java.lang.String)
	 */
	public boolean isReadPointReady(String readPointName) {
		// TODO Auto-generated method stub
		return false;
	}

	
	/* (non-Javadoc)
	 * @see org.accada.reader.hal.HardwareAbstraction#shutDownReadPoint(java.lang.String)
	 */
	public void shutDownReadPoint(String readPointName) {
		// TODO Auto-generated method stub

	}

	
	/* (non-Javadoc)
	 * @see org.accada.reader.hal.HardwareAbstraction#reset()
	 */
	public void reset() throws HardwareException {
		// TODO Auto-generated method stub

	}
}
