/*
 * Copyright (C) 2007 ETH Zurich
 *
 * This file is part of Fosstrak (www.fosstrak.org).
 *
 * Fosstrak is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1, as published by the Free Software Foundation.
 *
 * Fosstrak is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Fosstrak; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA  02110-1301  USA
 */

package org.fosstrak.hal.impl.feig.comm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.fosstrak.hal.util.ByteBlock;
import org.fosstrak.hal.util.CRC16;
import org.apache.log4j.Logger;

/**
 * TCPIPProtocol is an interface to the RFID-Reader FEIG ID ISC.LRU1000. It
 * allows to send a request to the reader. The sendRequest method
 * then locks until a response from the reader has arrived or a timeout occured.
 * A request consists of a comAdr, which is the bus-address of the reader, a
 * control byte, which specifies the command that the reader should execute and
 * some data-bytes, which state more precisely, what's asked for. When
 * terminating the session with the reader one should close the connection with
 * the close method.
 *
 * @author hallerj
 * @version 1.0
 */
public class TCPIPProtocol {

	static Logger log = Logger.getLogger(TCPIPProtocol.class);

	// -------- Constants
	// ---------------------------------------------------------
	/**  */
	static final int STX = 0x02;

	// -------- Fields
	// ------------------------------------------------------------
	/**
	 * time in milliseconds to block waiting for response after sending a
	 * request
	 */
	private long timeout;

	/** instance of SerialPort used by this instance of ISOProtocol */
	private Socket socket;

	/** stream for writing to the serial port */
	private OutputStream out;

	/** stream for reading from the serial port */
	private InputStream in;

   /** flag to indicate that a sendRequest method is proceeding */
   boolean sendRequestActive = false;

	// -------- Constructor(s)
	// ----------------------------------------------------
	/**
	 * creates a new instance and opens the com-port.
	 *
    * @param host
    *            the host name to connect to.
	 * @param port
	 *            the port to connect to.
	 * @param timeout
	 *            the maximal time in milliseconds that the sendRequest method
	 *            blocks, before throwing an exception.
	 * @throws IOException
	 *            if geting in-/outputstream fails or socket can not be opened
	 */
	public TCPIPProtocol(String host, int port, long timeout) throws IOException {
		this.timeout = timeout;
		socket = new Socket(host, port);
      out = socket.getOutputStream();
      in = socket.getInputStream();
	}

	// -------- Methods
	// -----------------------------------------------------------

	/**
	 * closes the com-port associated with this instance.
	 *
	 * @throws IOException
	 *            if closing in-/outputstream or socket fails
	 */
	public void close() throws IOException {
		out.close();
		in.close();
		socket.close();
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
	 *            a ResponseRecord.
	 * @throws IOException
	 *            in case of a timeout or any other io exception.
	 */
	public synchronized ResponseRecord sendRequest(RequestRecord requestRecord)
			throws IOException {

		// create new send-frame
		byte[] sendFrame = composeReaderSendFrame(requestRecord);

		return sendFrame(sendFrame);
	}

   /**
    * Sends a frame on the socket and returns the response
    *
    * @param frame
    *            the frame to send as byte array
    * @return the response from the reader. STATUSBYTE and DATA encapsulated in
    *            a ResponseRecord.
    * @throws IOException
    *            in case of a timeout or any other io exception.
    */
	private synchronized ResponseRecord sendFrame(byte[] frame) throws IOException {

		while (sendRequestActive) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		sendRequestActive = true;
		// send request
		out.write(frame);

      // for debugging only...
		// log.debug(" sendFrame: " +
		// ByteBlock.byteArrayToHexString(sendFrame));

		// transfer responseRecord
		ResponseRecord responseRecord = getResponse();

		// reader needs ~50 ms before it is ready for the next command
      // NOTE: was true on Feig OBID i-scan ID ISC.MR100/101 and ID ISC.PR100/101
      // NOTE: no restrictions found in manual for ID ISC.LRU1000 (TCP/IP)
		/*try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
		}*/

		// allow another sendRequest
		sendRequestActive = false;
		notifyAll();
		return responseRecord;
	}

	/**
	 * creates a new send-frame.
	 *
	 * @param requestRecord,
	 *            the data to be sent to the reader.
	 * @return the composed frame ready for sending to the reader.
	 * @throws IOException
	 *             if the send frame is longer then 65535 bytes.
	 */
	static byte[] composeReaderSendFrame(RequestRecord requestRecord)
			throws IOException {

		byte[] frame;

		// calculating LENGTH (Advanced Protocol-Length)
		int dataLen;
		if (requestRecord.data != null)
			dataLen = requestRecord.data.length;
		else
			dataLen = 0;
		// STX + ALENGTH + COM-ADR + CONTROLBYTE + DATA + CRC16
		int length = 1 + 2 + 1 + 1 + dataLen + 2;
		if (length > 65535) {
			throw new IOException("Protocol send frame too long. (>65535)");
		}
		byte[] len = ByteBlock.numberToByteArray(length);

		// constructing send-frame
		frame = new byte[length];
		frame[0] = STX;
      frame[1] = len[2];
      frame[2] = len[3];
		frame[3] = requestRecord.comAdr;
		frame[4] = requestRecord.controlByte;
		for (int i = 0; i < dataLen; i++) {
			frame[5 + i] = requestRecord.data[i];
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
	 * This method gets the answer from the reader and creates a responseRecord
    * which is returned to the caller
	 */
	private synchronized ResponseRecord getResponse() throws IOException {

		byte[] buf = null;

		// receiving data...
      // wait for at least 3 bytes of response
      long t0 = System.currentTimeMillis();
      long time = 0;
      while (in.available() < 3) {
         try { Thread.sleep(10); } catch (InterruptedException ie) {}
         time = System.currentTimeMillis() - t0;
         if (time >= timeout) {
            throw new IOException("Timeout occured, no response for "
                  + timeout + "ms.");
         }
      }

		// read first three byte
		byte[] firstthree = new byte[3];
		int stat = in.read(firstthree, 0, 3);
		if (stat != 3) {
			throw new IOException("No data available.");
		}

      // check STX
      if (firstthree[0] != STX) {
         throw new IOException("Received Data not of type 'Protocol Frame: "
               + "Advanced Protocol-Length'.");
      }

		// get length of response frame
      int msb = ByteBlock.byteToNumber(firstthree[1]);
      int lsb = ByteBlock.byteToNumber(firstthree[2]);
		int len = (msb << 8) + lsb;

      // wait for len - 3 bytes (rest of response)
      while (in.available() < (len - 3)) {
         try { Thread.sleep(10); } catch (InterruptedException ie) {}
         time = System.currentTimeMillis() - t0;
         if (time >= timeout) {
            throw new IOException("Timeout occured, response not completely "
                  + "arrived for " + timeout + "ms.");
         }
      }

      // read len - 3 bytes
      buf = new byte[len - 3];
      stat = in.read(buf, 0, len - 3);
      if (stat != (len - 3)) {
         throw new IOException("No data available.");
      }

      // if more available, frame is too long
		if (in.available() > 0) {
			throw new IOException(
					"Protocol response frame too long.");
		}

		// verify checksum
		byte[] frame = new byte[len];
      for (int i = 0; i < 3; i++)
         frame[i] = firstthree[i];
		for (int i = 3; i < len; i++)
			frame[i] = buf[i - 3];
		byte[] cs = CRC16.crc16(frame);
		if ((cs[0] != 0) || (cs[1] != 0)) {
			throw new IOException(
					"Protocol response frame checksum error.");
		}

		// create responseRecord
		ResponseRecord responseRecord = new ResponseRecord();
		// Reader ISO responses
		responseRecord.status = buf[2];
		byte[] data = new byte[buf.length - 5];
		for (int i = 3, j = 0; i < buf.length - 2; i++) {
			data[j++] = buf[i];
		}
		responseRecord.data = data;

		// for debugging only...
		// System.out.println(" buf: (without length) " +
		// ByteBlock.byteArrayToHexString(buf));
		// System.out.println();

      return responseRecord;
	}

}