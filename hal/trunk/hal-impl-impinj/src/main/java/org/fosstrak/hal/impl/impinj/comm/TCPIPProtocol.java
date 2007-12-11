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

package org.accada.hal.impl.impinj.comm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.accada.hal.impl.impinj.comm.Frame.Category;
import org.accada.hal.impl.impinj.comm.Frame.Type;
import org.accada.hal.impl.impinj.comm.CRC8;
import org.accada.hal.util.ByteBlock;
import org.apache.log4j.Logger;

/**
 * TCPIPProtocol is an interface to an RFID-Reader. It allows to send a request
 * to the reader. The sendRequest method then locks until a response from the
 * reader has arrived or a timeout occured.
 * When terminating the session with the reader one should close the socket with
 * the close method.
 *
 * @author Jonas Haller
 */
public class TCPIPProtocol {

	static Logger log = Logger.getLogger(TCPIPProtocol.class);

	// -------- Fields ----------------------------------------------------- //
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

   /**
    * flag to indicate that a sendRequest or receiveNotification method is
    * proceeding
    */
   boolean sendReceiveActive = false;

	// -------- Constructor ------------------------------------------------ //
	/**
	 * Creates a new instance and opens the socket.
	 *
    * @param host
    *            the host name to connect to
	 * @param port
	 *            the port to connect to
	 * @param timeout
	 *            the maximal time in milliseconds that the sendRequest method
	 *            blocks, before throwing an exception
	 * @throws IOException
	 *            if geting in-/outputstream fails or socket can not be opened
	 */
	public TCPIPProtocol(String host, int port, long timeout) throws IOException {
		this.timeout = timeout;
		socket = new Socket(host, port);
      out = socket.getOutputStream();
      in = socket.getInputStream();
	}

	// -------- Methods----------------------------------------------------- //

	/**
	 * Closes the socket associated with this instance.
	 *
	 * @throws IOException
	 *          if closing in-/outputstream or socket fails
	 */
	public void close() throws IOException {
		out.close();
		in.close();
		socket.close();
	}

   /**
    * Sends a request Frame to the reader and returns the response Frame.
    * This method method locks until a response from the reader has arrived or
    * a timeout occured.
    *
    * @param frame
    *          the Frame to be sent to the reader
    * @return
    *          the response from the reader as Frame
    * @throws IOException
    *          in case of a timeout or any other io exception.
    */
   public synchronized Frame sendRequest(Frame frame)
         throws IOException {

      // create byte array from Frame
      byte[] request = frame.getFrame();

      // send request and receive response
      byte[] response = sendRequestBytes(request);

      // create Frame from byte array
      Frame responseFrame;
      try {
         responseFrame = new Frame(response);
      } catch (FrameException e) {
         throw new IOException(e.getMessage(), e);
      }

      return responseFrame;
   }

   /**
    * Receives the next notification Frame from the reader with temporary
    * adjusted timeout and returns the Frame.
    * This method method locks until a notification from the reader has arrived
    * or a timeout occured.
    *
    * @param tempTimeout
    *          the timeout for this notification receive in milliseconds
    * @return
    *          the notification from the reader as Frame
    * @throws IOException
    *          in case of a timeout or any other io exception.
    */
   public synchronized Frame receiveNextNotification(long tempTimeout)
         throws IOException {

      // save timeout and set temporary timeout
      long oldTimeout = timeout;
      timeout = tempTimeout;

      // receive notification
      Frame notificationFrame = null;
      try {
         notificationFrame = receiveNextNotification();
      } catch (IOException ioe) {
         // restore old timeout
         timeout = oldTimeout;
         throw ioe;
      }

      // restore old timeout
      timeout = oldTimeout;

      return notificationFrame;
   }

   /**
    * Receives the next notification Frame from the reader and returns the
    * Frame.
    * This method method locks until a notification from the reader has arrived
    * or a timeout occured.
    *
    * @return
    *          the notification from the reader as Frame
    * @throws IOException
    *          in case of a timeout or any other io exception.
    */
   public synchronized Frame receiveNextNotification() throws IOException {

      // receive notification
      byte[] notification = receiveNotificationBytes();

      // create Frame from byte array
      Frame notificationFrame;
      try {
         notificationFrame = new Frame(notification);
      } catch (FrameException e) {
         throw new IOException(e.getMessage(), e);
      }

      return notificationFrame;
   }

   /**
    * Receives a specific notification Frame from the reader with temporary
    * adjusted timeout and returns the Frame.
    * This method method locks until a notification from the reader has arrived
    * or a timeout occured.
    *
    * @param tempTimeout
    *          the timeout for this notification receive in milliseconds
    * @return
    *          the notification from the reader as Frame
    * @throws IOException
    *          in case of a timeout or any other io exception.
    */
   public synchronized Frame receiveNotification(Category aCategory,
         int aMessageID, long tempTimeout) throws IOException {

      // save timeout and set temporary timeout
      long oldTimeout = timeout;
      timeout = tempTimeout;

      // receive notification
      Frame notificationFrame;
      try {
         notificationFrame = receiveNotification(aCategory, aMessageID);
      } catch (IOException ioe) {
         // restore old timeout
         timeout = oldTimeout;
         throw ioe;
      }

      // restore old timeout
      timeout = oldTimeout;

      return notificationFrame;
   }

   /**
    * Receives a specific notification Frame from the reader and returns the
    * Frame.
    * This method method locks until a notification from the reader has arrived
    * or a timeout occured.
    *
    * @return
    *          the notification from the reader as Frame
    * @throws IOException
    *          in case of a timeout or any other io exception.
    */
   public synchronized Frame receiveNotification(Category aCategory,
         int aMessageID) throws IOException {

      // check message ID
      if ((aMessageID < 0x00) || (aMessageID > 0xFF)) {
         throw new IOException("Can not receive notification, MessageID out of"
               + " range [0x00, 0xFF]");
      }

      // total timeout and start time
      long totalTimeout = timeout;
      long t0 = System.currentTimeMillis();
      long time = 0;

      byte[] notification = null;
      try {
         do {
            // handle omitted notifications
            if (notification != null) {
               handleUnusedFrame(notification);
            }

            // set timeout to remaining time
            time = System.currentTimeMillis() - t0;
            timeout = totalTimeout - time;

            // receive notification
            notification = receiveNotificationBytes();
         } while (((notification[1] & 0x0F) != aCategory.toInt()) ||
               ((notification[2] & 0xFF) != aMessageID));
      } catch (IOException ioe) {
         timeout = totalTimeout;
         throw ioe;
      }
      timeout = totalTimeout;

      // create Frame from byte array
      Frame notificationFrame;
      try {
         notificationFrame = new Frame(notification);
      } catch (FrameException e) {
         throw new IOException(e.getMessage(), e);
      }

      return notificationFrame;
   }

   /**
    * Sends a request on the socket and returns the response.
    * This method method locks until a response from the reader has arrived or
    * a timeout occured.
    *
    * @param request
    *          the frame to send as byte array
    * @return
    *          the response from the reader as byte array
    * @throws IOException
    *            in case of a timeout or any other io exception
    */
	private synchronized byte[] sendRequestBytes(byte[] request)
         throws IOException {

		while (sendReceiveActive) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		sendReceiveActive = true;

		// send request
		out.write(request);

      // for debugging only...
		// log.debug(" sent frame: " +
		// ByteBlock.byteArrayToHexString(request));

      // receive response frame (omit notification frames)
      long totalTimeout = timeout;
      long t0 = System.currentTimeMillis();
      long time = 0;

      byte[] response = null;
      try {
         do {
            // handle omitted frames (notifications)
            if (response != null) {
               handleUnusedFrame(response);
            }

            // set timeout to remaining time
            time = System.currentTimeMillis() - t0;
            timeout = totalTimeout - time;

            // receive frame bytes
            response = receiveFrameBytes();
         } while ((response[1] & 0x20) != 0x00);
      } catch (IOException ioe) {
         timeout = totalTimeout;
         throw ioe;
      }
      timeout = totalTimeout;


      if (((response[1] & 0x2F) != (request[1] & 0x0F)) ||
            (response[2] != request[2])) {
         throw new IOException ("Can not receive response.");
      }

		//  for debugging only...
      // log.debug(" received frame: " +
      // ByteBlock.byteArrayToHexString(response));

		// allow another send
		sendReceiveActive = false;
		notifyAll();
		return response;
	}

   /**
    * Receives a notification on the socket.
    * This method method locks until a notification from the reader has arrived or
    * a timeout occured.
    *
    * @return
    *          the notification from the reader as byte array
    * @throws IOException
    *            in case of a timeout or any other io exception
    */
   private synchronized byte[] receiveNotificationBytes()
         throws IOException {

      while (sendReceiveActive) {
         try {
            wait();
         } catch (InterruptedException e) {
         }
      }
      sendReceiveActive = true;

      // receive frame
      byte[] notification = receiveFrameBytes();
      if ((notification[1] & 0x20) == 0x00) {
         throw new IOException ("Can not receive notification.");
      }

      //  for debugging only...
      // log.debug(" received frame: " +
      // ByteBlock.byteArrayToHexString(response));

      // allow another send
      sendReceiveActive = false;
      notifyAll();
      return notification;
   }

	/**
	 * This method receives a frame from the reader (specific to Impinj Frame
    * format).
    *
    * @return
    *          the frame from the reader as byte array
    * @throws IOException
    *          in case of a timeout or any other io exception
	 */
	private synchronized byte[] receiveFrameBytes() throws IOException {

		byte[] buf = null;
      final int InitBytes = 5; // (length from bytes 1,3 and 4)

		// receiving data...
      // wait for at least InitBytes bytes of response
      long t0 = System.currentTimeMillis();
      long time = 0;
      while (in.available() < InitBytes) {
         try { Thread.sleep(10); } catch (InterruptedException ie) {}
         time = System.currentTimeMillis() - t0;
         if (time >= timeout) {
            throw new IOException("Timeout occured, no response for "
                  + timeout + "ms.");
         }
      }

		// read first InitBytes bytes
		byte[] initBytesArray = new byte[InitBytes];
		int stat = in.read(initBytesArray, 0, InitBytes);
		if (stat != InitBytes) {
			throw new IOException("No data available.");
		}

		// get length of response frame
      int msb = ByteBlock.byteToNumber(initBytesArray[3]) & 0x03;
      int lsb = ByteBlock.byteToNumber(initBytesArray[4]);
      int headerlen = 6;
      int tslen = 0;
      if ((initBytesArray[1] & 0x40) > 0) {
         tslen = 8;
      }
		int len = (msb << 8) + lsb + headerlen + tslen;

      // wait for (len - InitBytes) bytes (rest of response)
      while (in.available() < (len - InitBytes)) {
         try { Thread.sleep(10); } catch (InterruptedException ie) {}
         time = System.currentTimeMillis() - t0;
         if (time >= timeout) {
            throw new IOException("Timeout occured, response not completely "
                  + "arrived for " + timeout + "ms.");
         }
      }

      // read (len - InitBytes) bytes
      buf = new byte[len - InitBytes];
      stat = in.read(buf, 0, len - InitBytes);
      if (stat != (len - InitBytes)) {
         throw new IOException("No data available.");
      }

      // compose init bytes and rest of frame
      byte[] frame = new byte[len];
      System.arraycopy(initBytesArray, 0, frame, 0, InitBytes);
      System.arraycopy(buf, 0, frame, InitBytes, len - InitBytes);

		// verify checksum
      byte crc = CRC8.crc8(frame);
      if (crc != 0x00) {
         throw new IOException("CRC error in response.");
      }

      return frame;
	}

   /**
    * Handles frames that are not used otherwise (logging).
    *
    * @param frame
    *          the frame to handle
    */
   public void handleUnusedFrame(byte[] framebytes) {
      String framestring = ByteBlock.byteArrayToHexString(framebytes);
      Frame frame;
      try {
         frame = new Frame(framebytes);
      } catch (FrameException e) {
         log.info("Unable to pack unused frame: " + framestring);
         return;
      }

      if (frame.getType() == Type.NOTIFICATION) {
         if (frame.getCategory() == Category.OPERATING_MODEL) {
            switch (frame.getMessageID()) {
            case 0x01:
            case 0x02:
               // InventoryNtf or InventoryStatusNtf get omitted on stopping
               // inventory, do not log these notifications.
               break;
            default:
               log.info("Received unused Operating Command Set notification: " +
                     framestring);
            }
         } else if (frame.getCategory() == Category.MODEM_MANAGEMENT) {
            switch (frame.getMessageID()) {
            case 0x00:
               log.info("Received SocketConnectionStatus notification, an " +
                     "other client tried to connect to the reader.");
               break;
            case 0x01:
               log.info("Received SystemError notification: " + framestring);
               break;
            default:
               log.info("Received unused Management Command Set notification: " +
                     framestring);
               break;
            }
         } else {
            log.info("Received unused notification: " + framestring);
         }
      }
      if (frame.getType() == Type.RESPONSE) {
         log.info("Received unused response: " + framestring);
      }
   }
}