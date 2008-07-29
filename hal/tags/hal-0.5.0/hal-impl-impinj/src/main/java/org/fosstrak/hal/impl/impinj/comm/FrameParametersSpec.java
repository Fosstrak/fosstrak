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

import java.util.HashMap;

/**
 * This class represents a communication frame parameter specification of the
 * Mach1 protocol.
 * - ID format for mandantory parameters:   0x0100 | (parameter number)
 * - ID format for optional parameters:     0x0000 | PID
 * - Length for fixed-length parameters:    number of bytes
 * - Length for variable-length parameters: 0
 * Where (parameter number) out of range [0x00, 0xFF], first parameter = 0x00,
 * second parameter = 0x01, etc.
 * Range of ID = [0x0000, 0x01FF]
 * Range of Length = [0x0000, 0x03FF] = [0, 1023]
 *
 * <p>Copyright: Copyright (c) 2007</p>
 * @author Jonas Haller
 */
public class FrameParametersSpec {

   // -------- Constants --------------------------------------------------- //
   /** Some parameter spec arrays */
   public static final int[] InventoryNtf = new int[] { 0x0100, 0, 0x0101, 1,
      0x0102, 1, 0x0103, 2, 0x0104, 2, 0x01, 1, 0x02, 0, 0x03, 2, 0x04, 1,
      0x05, 2, 0x06, 4, 0x07, 4, 0x08, 1 };
   public static final int[] InventoryStatusNtf = new int[] { 0x0100, 1,
      0x01, 1, 0x02, 2, 0x03, 2 };

   /** mandantory bit mask */
   private static final int MANDANTORY = 0x0100;
   /** minimum ID */
   private static final int MIN_ID = 0x0000;
   /** maximum ID */
   private static final int MAX_ID = 0x01FF;
   /** minimum length */
   private static final int MIN_LEN = 0;
   /** maximum length */
   private static final int MAX_LEN = 1023;


   // -------- Fields ------------------------------------------------------ //
   /** hash map containing the parameter specifications */
   private HashMap<Integer, Integer> params = new HashMap<Integer, Integer>();
   /** current mandantory parameter */
   private int current;

   // -------- Public Methods ---------------------------------------------- //
   /**
    * Constructor.
    *
    * @param aIdLen
    *          the interleaved parameter ID and length array
    * @throws FrameException
    *          if odd number of elements or wrong format
    */
   public FrameParametersSpec(int[] aIdLen)
         throws FrameException {
      // check array length
      if ((aIdLen.length % 2) != 0) {
         throw new FrameException("Odd number of elements in interleaved ID / "
               + "length array.");
      }

      // add parameters to this parameter specification
      for (int i = 0; i < (aIdLen.length / 2); i++) {
         add(aIdLen[2*i], aIdLen[2*i+1]);
      }

      // reset current mandantory parameter to the first mandantory parameter
      resetMandantory();
   }
   /**
    * Constructor.
    *
    * @param aId
    *          the parameter ID array
    * @param aLen
    *          the parameter length array
    * @throws FrameException
    *          if different number of elements or wrong format
    */
   public FrameParametersSpec(int[] aId, int[] aLen)
         throws FrameException {
      // check array length
      if (aId.length != aLen.length) {
         throw new FrameException("Different number of elements in ID and "
               + "length array.");
      }

      // add parameters to this parameter specification
      for (int i = 0; i < aId.length; i++) {
         add(aId[i], aLen[i]);
      }

      // reset current mandantory parameter to the first mandantory parameter
      resetMandantory();
   }

   /**
    * Add a new parameter to the parameter specification.
    *
    * @param aId
    *          the ID of the parameter
    * @param aLen
    *          the length of the parameter
    * @throws FrameException
    *          if wrong format
    */
   public void add(int aId, int aLen) throws FrameException {
      if ((aId < MIN_ID) || (aId > MAX_ID)) {
         throw new FrameException("ID '" + aId + "' out of range.");
      }
      if ((aLen < MIN_LEN) || (aLen > MAX_LEN)) {
         throw new FrameException("Length '" + aLen + "' of ID '" + aId +
               "' out of range.");
      }
      params.put(aId, aLen);
   }

   /**
    * Get length of next mandantory parameter.
    *
    * @return
    *          the ID and length of the parameter (int[] of length 2)
    * @throws FrameException
    *          if no more mandantory parameters available
    */
   public int[] getNextMandantory() throws FrameException {
      Integer len = params.get(current);
      int[] value = null;
      if (len == null) {
         throw new FrameException("No more mandantory parameters available.");
      } else {
         value = new int[2];
         value[0] = current;
         value[1] = len;
         current++;
      }
      return value;
   }

   /**
    * Reset current mandantory parameter to the first mandantory parameter.
    */
   public void resetMandantory() {
      current = MANDANTORY; // mask equals first element
   }

   /**
    * Get length of optional parameter.
    *
    * @param aId
    *          the PID of the optional parameter
    * @return
    *          the length of the parameter
    * @throws FrameException
    *          if PID out of range or optional parameter not available
    */
   public int getOptionalLength(int aId) throws FrameException {
      if ((aId < 0x00) || (aId > 0xFF)) {
         throw new FrameException("PID '" + aId + "' out of range.");
      }
      Integer len = params.get(aId);
      if (len == null) {
         throw new FrameException("Optional parameter '" + aId + "' not "
               + "specified.");
      } else {
         current++;
      }
      return len;
   }

}