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
 * This class represents ... of a Mach1 protocol frame.
 *
 * <p>Copyright: Copyright (c) 2007</p>
 * @author Jonas Haller
 */
public class FrameParameters {

   // -------- Constants --------------------------------------------------- //
   /**  */
   public static final boolean C = true;

   // -------- Fields ------------------------------------------------------ //
   /** hash map containing the parameters */
   private HashMap<Integer, byte[]> params = new HashMap<Integer, byte[]>();

   // -------- Public Methods ---------------------------------------------- //
   /**
    * Constructor.
    *
    * @param aData
    *          the data field of the frame
    * @param aSpec
    *          the specification of the parameters contained in the data field
    * @throws FrameException
    *          if data does not comply to the spec or other error occures
    */
   public FrameParameters(byte[] aData, FrameParametersSpec aSpec)
         throws FrameException {
      int index = 0;
      int datalen = aData.length;
      int[] paramspec;
      int id, len;
      byte[] param;

      // get mandantory parameters
      aSpec.resetMandantory();
      while (true) {
         try {
            paramspec = aSpec.getNextMandantory();
         } catch (FrameException fe) {
            break;
         }
         id = paramspec[0];
         len = paramspec[1];
         if (len == 0) {
            // get variable length
            checkEnough(index, datalen, 2);
            len = (((aData[index] & 0xFF) << 8) | (aData[index + 1] & 0xFF));
            index += 2;
         }
         checkEnough(index, datalen, len);
         param = new byte[len];
         System.arraycopy(aData, index, param, 0, len);
         params.put(id, param);
         index += len;
      }

      // get optional parameters
      while (index < datalen) {
         id = aData[index] & 0xFF;
         index++;
         len = aSpec.getOptionalLength(id);
         if (len == 0) {
            // get variable length
            checkEnough(index, datalen, 2);
            len = (((aData[index] & 0xFF) << 8) | (aData[index + 1] & 0xFF));
            index += 2;
         }
         checkEnough(index, datalen, len);
         param = new byte[len];
         System.arraycopy(aData, index, param, 0, len);
         params.put(id, param);
         index += len;
      }
   }

   /**
    * Get a parameter value.
    *
    * @param aId
    *          the ID of the parameter
    * @return
    *          the value of the parameter
    * @throws FrameException
    *          if parameter not available
    */
   public byte[] getParameter(int aId) throws FrameException {
      byte[] param = params.get(aId);
      if (param == null) {
         throw new FrameException("Parameter '" + aId + "' not available.");
      }
      return param;
   }

   // -------- Private Methods --------------------------------------------- //
   /**
    * Check if enough elements of data left
    *
    * @param aIndex
    *          the index (in the array)
    * @param aDataLen
    *          the total length of data
    * @param aLen
    *          the length to check if it is available
    * @throws FrameException
    *          if less than aLen elements available
    */
   private void checkEnough(int aIndex, int aDataLen, int aLen)
         throws FrameException {
      if ((aDataLen - aIndex) < aLen) {
         throw new FrameException("Not enough data available in array.");
      }
   }
}