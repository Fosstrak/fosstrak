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

package org.accada.reader.rp.proxy.msg.stubs;

/**
 * The Exception Class of the Reader Protocol.
 * <br /><br />
 * This is the local client stub which can be used to access the remote object on
 * the reader.
 *
 * @author Andreas Fürer, ETH Zurich Switzerland, Winter 2005/06
 */
public class ReaderProtocolException extends Exception {

   /**
    * Id of the thread.
    */
   private static final long serialVersionUID = 8487811547447004938L;

   /**
    * The error name as string.
    */
   private String errorName;

   /**
    * The error code as int.
    */
   private int errorCode;

   /**
    * The error description as string.
    */
   private String errorDescription;

   /**
    * The constructor of the ReaderProtocolException.
    * @param errorName
    *           The error message as string
    * @param errorCode
    *           The error code as int
    * @param errorDescription
    *           A description of the error
    */
   public ReaderProtocolException(final String errorName,
         final int errorCode, final String errorDescription) {
      super(errorName);
      this.errorName = errorName;
      this.errorCode = errorCode;
      this.errorDescription = errorDescription;
   }

   /**
    * The constructor of the ReaderProtocolException.
    * @param errorName
    *           The error message as string
    * @param errorCode
    *           The error code as int
    */
   public ReaderProtocolException(final String errorName, final int errorCode) {
      this(errorName, errorCode, null);
   }

   /**
    * @return Returns the errorCode
    */
   public final int getErrorCode() {
      return errorCode;
   }

   /**
    * @return Returns the errorMessage.
    */
   public final String getErrorName() {
      return errorName;
   }

   /**
    * @return Returns the errorDescription.
    */
   public final String getErrorDescription() {
      return errorDescription;
   }

}
