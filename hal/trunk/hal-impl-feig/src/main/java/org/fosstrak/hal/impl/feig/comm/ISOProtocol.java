/*
 * Copyright (C) 2007 ETH Zurich
 *
 * This file is part of Accada (www.accada.org).
 *
 * Accada is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1, as published by the Free Software Foundation.
 *
 * Accada is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Accada; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA  02110-1301  USA
 */

package org.accada.hal.impl.feig.comm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import javax.comm.CommPortIdentifier;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import javax.comm.UnsupportedCommOperationException;

import org.accada.hal.util.ByteBlock;
import org.accada.hal.util.CRC16;
import org.apache.log4j.Logger;

/**
 * ISOProtocol is an interface to the ISO 15693 compliant RFID-Reader FEIG ID
 * ISC.MR100. It allows to send a request to the reader. The sendRequest method
 * then locks until a response from the reader has arrived or a timeout occured.
 * A request consists of a comAdr, which is the bus-address of the reader, a
 * control byte, which specifies the command that the reader should execute and
 * some data-bytes, which state more precisely, what's asked for. When
 * terminating the session with the reader one should close the serial-port with
 * the closePort method.
 *
 * @author Simon Keel, skeel@student.ethz.ch
 * @version 1.0
 */
public class ISOProtocol implements SerialPortEventListener {

	static Logger log = Logger.getLogger(ISOProtocol.class);

	// -------- Constants
	// ---------------------------------------------------------
	/** time in milliseconds to block waiting for port open */
	static final int openPortTimeout = 600;

	// -------- Fields
	// ------------------------------------------------------------
	/**
	 * time in milliseconds to block waiting for response after sending a
	 * request
	 */
	long timeout;

	/** instance of SerialPort used by this instance of ISOProtocol */
	SerialPort serialPort;

	/** stream for writing to the serial port */
	OutputStream out;

	/** stream for reading from the serial port */
	InputStream in;

	/** flag to indicate that a sendRequest method is proceeding */
	boolean sendRequestActive = false;

	/**
	 * flag to indicate that a serialEvent is expected and we are interested in
	 * an answer from the reader
	 */
	boolean serialEventExpected = false;

	/**
	 * contains the answer from the producer (serialEvent) to be consumed from
	 * sendRequst
	 */
	ResponseRecord responseRecord = null;

	/** indicates wheter a io-exception occured during serialEvent */
	String errorMsg = null;

	// -------- Constructor(s)
	// ----------------------------------------------------
	/**
	 * creates a new instance and opens the com-port.
	 *
	 * @param portNr
	 *            the n-th serial port in the system starting with 1.
	 * @param timeout
	 *            the maximal time in milliseconds that the sendRequest method
	 *            blocks, before throwing an exception.
	 * @param baud
	 *            the baud rate for the serial port communication.
	 * @param databits
	 *            the databits for the serial port communication.
	 * @param stopbits
	 *            the stopbits for the serial port communication.
	 * @param parity
	 *            then parity for the serial port communication.
	 * @throws UnsupportedCommOperationException
	 *             if setting port parameters fails
	 * @throws PortInUseException
	 *             if opening port fails
	 * @throws IOException
	 *             if geting in-/outputstream fails or if there are too many
	 *             listeners for this port
	 */
	public ISOProtocol(int portNr, long timeout, int baud, int databits,
			int stopbits, int parity) throws UnsupportedCommOperationException,
			PortInUseException, IOException {
		this.timeout = timeout;
		String portName = getSerialPortNameForOS(portNr);
		openPort(portName, baud, databits, stopbits, parity);
	}

	// -------- Methods
	// -----------------------------------------------------------
	/**
	 * opens the com-port.
	 *
	 * @param portName
	 *            string representing the port name for the current os
	 * @param baud
	 *            the baud rate for the serial port communication.
	 * @param databits
	 *            the databits for the serial port communication.
	 * @param stopbits
	 *            the stopbits for the serial port communication.
	 * @param parity
	 *            then parity for the serial port communication.
	 * @throws UnsupportedCommOperationException
	 *             if setting port parameters fails
	 * @throws PortInUseException
	 *             if opening port fails
	 * @throws IOException
	 *             if geting in-/outputstream fails or if there are too many
	 *             listeners for this port
	 */
	private void openPort(String portName, int baud, int databits,
			int stopbits, int parity) throws UnsupportedCommOperationException,
			PortInUseException, IOException {
		Enumeration portList = CommPortIdentifier.getPortIdentifiers();
		CommPortIdentifier portId;

		while (portList.hasMoreElements()) {
			portId = (CommPortIdentifier) portList.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				if (portId.getName().equals(portName)) {

					// Port found. Open it.
					// (maybe throws PortInUseException)
					serialPort = (SerialPort) portId.open("RFID ISOProtocol",
							openPortTimeout);

					// setting default port parameters
					// (maybe throws UnsupportedCommOperationException)
					serialPort.setSerialPortParams(baud, databits, stopbits,
							parity);

					// add EventListener
					try {
						serialPort.addEventListener(this);
					} catch (TooManyListenersException e) {
						throw new IOException(e.getMessage());
					}

					serialPort.notifyOnDataAvailable(true);
					serialPort.notifyOnBreakInterrupt(true);
					serialPort.notifyOnFramingError(true);
					serialPort.notifyOnOverrunError(true);
					serialPort.notifyOnParityError(true);

					// assign Input- and OutputStream
					// (maybe throws IOException)
					out = serialPort.getOutputStream();
					in = serialPort.getInputStream();

					// return
					return;

				}
			}
		}

		// port not found
		throw new IOException("Serial port " + portName
				+ " not found (maybe it is locked).");
		// NoSuchPortException not yet implemented???

	}

	/**
	 * closes the com-port associated with this instance.
	 *
	 * @throws IOException
	 *             if closing in/output-streams fails
	 */
	public void closePort() throws IOException {
		serialPort.removeEventListener();
		// closing in/output-streams may throw IOException
		out.close();
		in.close();
		serialPort.close();
	}

	/**
	 * sends the request (COM-ADR, CONTROLBYTE and some DATA) to the reader.
	 * This method method locks until a response from the reader has arrived or
	 * a timeout occured.
	 *
	 * @param requestRecord
	 *            a record containing of COM-ADR, CONTROLBYTE and some DATA to
	 *            be sent to the reader
	 * @return the response from the reader. STATUSBYTE and DATA encapsulated in
	 *         a ResponseRecord.
	 * @throws IOException
	 *             in case of a serial port timeout or any other io exception.
	 */
	public synchronized ResponseRecord sendRequest(RequestRecord requestRecord)
			throws IOException {

		// create new send-frame
		byte[] sendFrame = composeReaderSendFrame(requestRecord);

		return sendFrame(sendFrame);
	}

	/**
	 * Sends a request to a function unit (multiplexer, antenna tuner)
	 * attached to the reader.
	 *
	 * @param requestRecord
	 * @return
	 * @throws IOException
	 */
	public synchronized ResponseRecord sendFunctionUnitRequest(
			RequestRecord requestRecord) throws IOException {

	   // create request data
		byte[] fuSendFrame = composeFUSendFrame(requestRecord);
      byte[] transparent = new byte[] { 0x01, 0x00, 0x20, 0x02, 0x1F};
      byte[] data = new byte[transparent.length + fuSendFrame.length];
      System.arraycopy(transparent, 0, data, 0, 5);
      System.arraycopy(fuSendFrame, 0, data, 5, fuSendFrame.length);

      RequestRecord req = new RequestRecord();
      req.comAdr = (byte) 0xFF;
      req.controlByte = (byte) 0xBF; // (ISO transparent command)
      req.data = data;

		return sendRequest(req);
	}

	private synchronized ResponseRecord sendFrame(byte[] frame) throws IOException {

		while (sendRequestActive) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		sendRequestActive = true;
		// send request (may throw IOException)
		out.write(frame);
		serialEventExpected = true;
		// for debugging only...
		// System.out.println(" sendFrame: " +
		// ByteBlock.byteArrayToHexString(sendFrame));

		errorMsg = null;

		// wait for serialEvent to return a response record
		long t0 = System.currentTimeMillis();
		long time = 0;
		while ((responseRecord == null) && (time < timeout)) {
			try {
				wait(timeout - time);
			} catch (InterruptedException e) {
			}
			time = System.currentTimeMillis() - t0;
		}
		serialEventExpected = false;
		if (responseRecord == null) {
			throw new IOException("Serial port timeout.");
		}

		// Did an IOException occur?
		if (errorMsg != null) {
			responseRecord = null;
			throw new IOException(errorMsg);
		}

		// transfer responseRecord
		ResponseRecord resp = new ResponseRecord();
		resp.status = responseRecord.status;
		resp.data = responseRecord.data;
		responseRecord = null;

		// reader needs ~50 ms before it is ready for the next command
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
		}

		// allow another sendRequest
		sendRequestActive = false;
		notifyAll();
		return resp;
	}

	/**
	 * creates a new send-frame.
	 *
	 * @param requestRecord,
	 *            the data to be sent to the reader.
	 * @return the composed frame ready for sending to the reader.
	 * @throws IOException
	 *             if the send frame is longer then 255 bytes.
	 */
	static byte[] composeReaderSendFrame(RequestRecord requestRecord)
			throws IOException {

		byte[] frame;

		// calculating LENGTH
		int dataLen;
		if (requestRecord.data != null)
			dataLen = requestRecord.data.length;
		else
			dataLen = 0;

		int length = 1 + 1 + 1 + dataLen + 2; // Wahrscheinlich: Header 3Bytes
												// + Daten + Checksumme 2Bytes
		if (length > 255) {
			throw new IOException("ISOProtocol send frame too long. (>255)");
		}
		byte[] len = ByteBlock.numberToByteArray(length);

		// constructing send-frame
		frame = new byte[length];
		frame[0] = len[3];
		frame[1] = requestRecord.comAdr;
		frame[2] = requestRecord.controlByte;
		for (int i = 0; i < dataLen; i++) {
			frame[3 + i] = requestRecord.data[i];
		}
		// calculating crc16-checksum
		byte[] temp = new byte[length - 2];
		for (int i = 0; i < length - 2; i++)
			temp[i] = frame[i];
		byte[] cs = CRC16.crc16(temp);
		frame[length - 2] = cs[0];
		frame[length - 1] = cs[1];

		return frame;
	}

	/**
	 * creates a new send-frame.
	 *
	 * @param requestRecord,
	 *            the data to be sent to the reader.
	 * @return the composed frame ready for sending to the reader.
	 * @throws IOException
	 *             if the send frame is longer then 255 bytes.
	 */
	static byte[] composeFUSendFrame(RequestRecord requestRecord)
			throws IOException {

		byte[] frame;

		// calculating LENGTH
		int dataLen;
		if (requestRecord.data != null)
			dataLen = requestRecord.data.length;
		else
			dataLen = 0;

		int length = 3 + dataLen;

		if (length > 255) {
			throw new IOException("ISOProtocol send frame too long. (>255)");
		}

		// constructing send-frame
		frame = new byte[length];
		frame[0] = 0x02;
		frame[1] = requestRecord.controlByte;
		frame[2] = (byte) 0xfe;

		for (int i = 0; i < dataLen; i++) {
			frame[3 + i] = requestRecord.data[i];
		}

		return frame;
	}

	/**
	 * is called by the underlying driver when an event occurs. This methods
	 * gets the answer from the reader and creates a responseRecord which is
	 * delivered to the sendRequest method
	 *
	 * @param event
	 *            an instance representing the serial event, containing data
	 *            about the type of the event.
	 */
	public synchronized void serialEvent(SerialPortEvent event) {

		boolean functionUnitResponse = false;

		// Ignore "spontaneous" events
		if (!serialEventExpected) {
			return;
		}

		byte[] buf = null;
		try {

			// receiving data...
			switch (event.getEventType()) {
			case SerialPortEvent.BI:
				throw new IOException("Serial port break interrupt.");
			case SerialPortEvent.OE:
				throw new IOException("Serial port overrun error.");
			case SerialPortEvent.FE:
				throw new IOException("Serial port framing error.");
			case SerialPortEvent.PE:
				throw new IOException("Serial port parity error.");
			case SerialPortEvent.DATA_AVAILABLE:

				// read first byte
				byte[] lenByte = new byte[1];
				int stat = in.read(lenByte, 0, 1);
				if (stat != 1) {
					throw new IOException("No data available at serial port.");
				}

				// get length of response frame
				int len = Integer.parseInt(ByteBlock
						.byteToHexString(lenByte[0]), 16);


				if (len == 0) {
					// Handle events from function units (read until eof)
					functionUnitResponse = true;

					// TODO Determine buffer size (8 would probably be enough)
					buf = new byte[16];
					stat = in.read(buf);

					if (stat <= 0) {
						throw new IOException(
						"Unexpected \"stream at end of file\" while reading ");
					}

					if (in.available() > 0) {
						throw new IOException(
								"ISOProtocol response frame too long.");
					}
					byte[] newBuf = new byte[stat];
					System.arraycopy(buf, 0, newBuf, 0, stat);
					buf = newBuf;

				} else {
					buf = new byte[len - 1];
					// read len- 1 bytes
					stat = 0;
					int temp;
					while (stat != len - 1) {
						temp = in.read(buf, stat, len - 1 - stat);
						if (temp == -1) {
							throw new IOException(
									"Unexpected \"stream at end of file\" while reading ");
						}
						stat += temp;
					}
					if (in.available() > 0) {
						throw new IOException(
								"ISOProtocol response frame too long.");
					}

					// verify checksum
					byte[] frame = new byte[len];
					frame[0] = lenByte[0];
					for (int i = 1; i < len; i++)
						frame[i] = buf[i - 1];
					byte[] cs = CRC16.crc16(frame);
					if ((cs[0] != 0) || (cs[1] != 0)) {
						throw new IOException(
								"ISOProtocol response frame checksum error.");
					}
				}

				break;
			}

		} catch (IOException e) {
			errorMsg = e.getMessage();
		}

		// create responseRecord
		responseRecord = new ResponseRecord();
		if (errorMsg == null) {
			if (!functionUnitResponse) {
				// Reader ISO responses
				responseRecord.status = buf[2];
				byte[] data = new byte[buf.length - 5];
				for (int i = 3, j = 0; i < buf.length - 2; i++) {
					data[j++] = buf[i];
				}
				responseRecord.data = data;
			} else {
				// Function unit responses
				responseRecord.status = buf[0];
				byte[] data = new byte[buf.length - 1];
				for (int i = 1, j = 0; i < buf.length; i++) {
					data[j++] = buf[i];
				}
				responseRecord.data = data;
			}

		}

		// for debugging only...
		// System.out.println(" buf: (without length) " +
		// ByteBlock.byteArrayToHexString(buf));
		// System.out.println();

		// allow sendRequest-method
		notifyAll();

	}

	/**
	 * returns the port-name for a given serial-port-number for the running OS.
	 *
	 * @param portNr
	 *            the port nnumber for which the portname should be generated
	 * @return a string representing the port name
	 * @throws UnsupportedCommOperationException
	 *             if the Operating System is not known by the method
	 */
	static public String getSerialPortNameForOS(int portNr)
			throws UnsupportedCommOperationException {
		char letter[] = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
				'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
				'w', 'x', 'y', 'z' };
		String os = System.getProperty("os.name");

		if ((portNr > 26) || (portNr < 1)) {
			throw new UnsupportedCommOperationException(
					"Serial port number must be between 1 and 26.");
		}

		if (os.equals("Linux")) {
			return "/dev/ttyS" + (portNr - 1);
		} else if (os.equals("SunOS")) {
			return "/dev/term/" + letter[portNr - 1];
		} else if (os.startsWith("Windows")) {
			return "COM" + portNr;
		} else {
			throw new UnsupportedCommOperationException(
					"ISOProtocol: Unknown Operating System.");
		}
	}

}