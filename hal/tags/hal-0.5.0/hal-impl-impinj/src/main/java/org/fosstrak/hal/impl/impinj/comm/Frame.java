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

package org.fosstrak.hal.impl.impinj.comm;

import org.fosstrak.hal.impl.impinj.comm.CRC8;

/**
 * This class represents a communication frame of the Mach1 protocol.
 *
 * <p>Copyright: Copyright (c) 2007</p>
 * @author Jonas Haller
 */
public class Frame {

   // -------- Constants --------------------------------------------------- //
   /** type of frame (command, response or notification) */
   public enum Type { COMMAND, RESPONSE, NOTIFICATION };
   /** categories */
   public enum Category {
      MACH1_PROTOCOL_ERRORS (0x00),
      OPERATING_MODEL (0x01),
      MODEM_MANAGEMENT (0x02),
      HARDWARE_INTERFACE (0x03),
      PRODUCTION (0x04),
      LOGGING (0x05),
      TEST (0x06);

      private final int category;
      Category(int aCategory) {
         this.category = aCategory;
      }
      public int toInt() {
         return category;
      }
   }

   // -------- Fields ------------------------------------------------------ //
   /** the type of this frame */
   private Type type;
   /** if timestamp is included */
   private boolean isTimestamp = false;
   /** the timestamp seconds */
   private int timestampSeconds = 0;
   /** the timestamp microseconds */
   private int timestampMicroseconds = 0;
   /** the category */
   private Category category;
   /** the message ID */
   private int messageID;
   /** the data */
   private byte[] data;

   // -------- Public Methods ---------------------------------------------- //
   /** Constructor. */
   public Frame(Type aType, Category aCategory, int aMessageID, byte[] aData)
         throws FrameException {
      setType(aType);
      setCategory(aCategory);
      setMessageID(aMessageID);
      setData(aData);
   }
   public Frame(byte[] frame) throws FrameException {
      // check length
      if (frame.length < 6) {
         throw new FrameException("Frame length < 6 Bytes minimum.");
      }

      // get type
      if ((((int) frame[0]) & 0x01) == 0) {
         type = Type.COMMAND;
      } else{
         if ((((int) frame[1]) & 0x20) == 0) {
            type = Type.RESPONSE;
         } else {
            type = Type.NOTIFICATION;
         }
      }

      // get category
      category = null;
      int cat = ((int) frame[1]) & 0x0F;
      for (Category c : Category.values()) {
         if (c.category == cat) {
            category = c;
            break;
         }
      }
      if (category == null) {
         throw new FrameException("Category " + cat + " out of range.");
      }

      // get message id
      int mid = ((int) frame[2]) & 0xFF;
      setMessageID(mid);

      // get length
      int len = ((((int) frame[3]) & 0xFF) << 8) + (((int) frame[4]) & 0xFF);
      if (len >= 1024) {
         throw new FrameException("Length >= 1024 Bytes maximum.");
      }

      // get timestamp
      if ((((int) frame[1]) & 0x40) > 0) {
         if (frame.length < 14) {
            throw new FrameException("Frame length < 14 Bytes minimum with "
                  + "timestamp.");
         }
         timestampSeconds = ((((int) frame[5]) & 0xFF) << 24) +
            ((((int) frame[6]) & 0xFF) << 16) +
            ((((int) frame[7]) & 0xFF) << 8) + (((int) frame[8]) & 0xFF);
         timestampSeconds = ((((int) frame[9]) & 0xFF) << 24) +
            ((((int) frame[10]) & 0xFF) << 16) +
            ((((int) frame[11]) & 0xFF) << 8) + (((int) frame[12]) & 0xFF);
         isTimestamp = true;
      }

      // get data
      if (len > 0) {
         int offset = 5;
         if (isTimestamp) {
            offset += 8;
         }
         data = new byte[len];
         System.arraycopy(frame, offset, data, 0, len);
      } else {
         data = new byte[0];
      }

   }

   /** Get Frame. */
   public byte[] getFrame() {
      int length = 6 + data.length;
      if (isTimestamp) length += 8;

      byte[] frame = new byte[length];
      int index = 0;

      frame[index] = getStartField();
      index++;

      System.arraycopy(getHeaderField(), 0, frame, index, 2);
      index += 2;

      System.arraycopy(getLengthField(), 0, frame, index, 2);
      index += 2;

      if (isTimestamp) {
         System.arraycopy(getTimestampField(), 0, frame, index, 8);
         index += 8;
      }

      if (data.length != 0) {
         System.arraycopy(data, 0, frame, index, data.length);
         index += data.length;
      }

      byte[] crcpart = new byte[length - 1];
      System.arraycopy(frame, 0, crcpart, 0, crcpart.length);
      byte crc = CRC8.crc8(crcpart);
      frame[index] = crc;

      return frame;
   }

   /** Get type. */
   public Type getType() {
      return type;
   }
   /**
    * Set type.
    *
    * @throws FrameException
    *          if Type is null
    */
   public void setType(Type aType) throws FrameException {
      if (aType != null) {
         type = aType;
      } else {
         throw new FrameException("Type can not be null.");
      }
   }

   /** Get if timestamp is set. */
   public boolean getIsTimestamp() {
      return isTimestamp;
   }
   /** Get timestamp seconds. */
   public int getTimestampSeconds() {
      return timestampSeconds;
   }
   /** Get if timestamp is set. */
   public int getTimestampMicroseconds() {
      return timestampMicroseconds;
   }
   /** Set timestamp to now. */
   public void setTimestampNow() {
      long millis = System.currentTimeMillis();
      setTimestamp((int) (millis / 1000), (int) ((millis % 1000) * 1000));
   }
   /** Set timestamp. */
   public void setTimestamp(int aSeconds, int aMicroseconds) {
      timestampSeconds = aSeconds;
      timestampMicroseconds = aMicroseconds;
      isTimestamp = true;
   }
   /** Reset timestamp. */
   public void resetTimestamp() {
      timestampSeconds = 0;
      timestampMicroseconds = 0;
      isTimestamp = false;
   }

   /** Get category. */
   public Category getCategory() {
      return category;
   }
   /**
    * Set category.
    *
    * @throws FrameException
    *          if Category is null
    */
   public void setCategory(Category aCategory) throws FrameException {
      if (aCategory != null) {
         category = aCategory;
      } else {
         throw new FrameException("Category can not be null.");
      }
   }

   /** Get messageID. */
   public int getMessageID() {
      return messageID;
   }
   /**
    * Set messageID.
    *
    * @throws FrameException
    *          if MessageID out of range [0x00,0xFF]
    */
   public void setMessageID(int aMessageID) throws FrameException {
      if ((aMessageID >= 0x00) && (aMessageID <= 0xFF)) {
         messageID = aMessageID;
      } else {
         throw new FrameException("MessageID out of range [0x00, 0xFF]");
      }
   }

   /** Get data. */
   public byte[] getData() {
      return data;
   }
   /**
    * Set data.
    *
    * @throws FrameException
    *          if data length >= 1024
    */
   public void setData(byte[] aData) throws FrameException {
      if (aData == null) {
         aData = new byte[0];
      }
      if (aData.length < 1024) {
         data = aData;
      } else {
         throw new FrameException("Data length >= 1024");
      }
   }

   // -------- Private Methods --------------------------------------------- //
   /**
    * Get the Start Field.
    *
    * @return
    *          Start byte
    */
   private byte getStartField() {
      byte start;
      if (type == Type.COMMAND) {
         start = (byte) 0xEE;
      } else {
         start = (byte) 0xEF;
      }
      return start;
   }

   /**
    * Get the Header Field.
    *
    * @return
    *          Header bytes (2)
    */
   private byte[] getHeaderField() {
      byte[] header = new byte[2];

      int ts = 0x00;
      if (isTimestamp) {
         ts = 0x40;
      }

      int rn = 0x00;
      if (type == Type.NOTIFICATION) {
         rn = 0x20;
      }

      int cat = category.toInt();

      header[0] = (byte) ((ts | rn | cat) & 0xFF);
      header[1] = (byte) (messageID & 0xFF);

      return header;
   }

   /**
    * Get the Length Field.
    *
    * @return
    *          Length bytes (2)
    */
   private byte[] getLengthField() {
      byte[] length = new byte[2];

      if (data != null) {
         length[0] = (byte) ((data.length >> 8) & 0x03);
         length[1] = (byte) (data.length & 0xFF);
      } else {
         length[0] = (byte) 0x00;
         length[1] = (byte) 0x00;
      }

      return length;
   }

   /**
    * Get the Time Stamp Field.
    *
    * @return
    *          Time Stamp bytes (null or 8)
    */
   private byte[] getTimestampField() {
      byte[] ts = null;

      if (isTimestamp) {
         ts = new byte[8];
         ts[0] = (byte) ((timestampSeconds >> 24) & 0xFF);
         ts[1] = (byte) ((timestampSeconds >> 16) & 0xFF);
         ts[2] = (byte) ((timestampSeconds >> 8) & 0xFF);
         ts[3] = (byte) (timestampSeconds & 0xFF);
         ts[4] = (byte) ((timestampMicroseconds >> 24) & 0xFF);
         ts[5] = (byte) ((timestampMicroseconds >> 16) & 0xFF);
         ts[6] = (byte) ((timestampMicroseconds >> 8) & 0xFF);
         ts[7] = (byte) (timestampMicroseconds & 0xFF);
      }

      return ts;
   }

}