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

package org.accada.reader.rp.proxy.msg;

import org.accada.reader.rprm.core.msg.MessagingConstants;
import org.accada.reader.rprm.core.msg.TcpReceiverHandshakeMessage;
import org.apache.log4j.Logger;

public class TcpClientConnection extends ClientConnection {

	/** The logger. */
	private static final Logger LOG = Logger.getLogger(TcpClientConnection.class);
	
	public TcpClientConnection(Client target) {
		super(target);
	}
	
	public void sendHandshake() {
		try {
			out.write(handshake.serializeTcp());
			LOG.info("Handshake: " + handshake.serializeTcp());
			out.flush();

         // Receive answer (receiver handshake)
         boolean handshakeComplete = false;
         int offset = 0;
         byte[] buffer = new byte[TcpReceiverHandshakeMessage.LENGTH];
         while (!handshakeComplete) {
            int r = in.read(buffer, offset,
                  TcpReceiverHandshakeMessage.LENGTH - offset);
            offset += r;
            if (r == -1 && offset != TcpReceiverHandshakeMessage.LENGTH) {
               return;
            } else if (r != -1 && offset == TcpReceiverHandshakeMessage.LENGTH) {
               handshakeComplete = true;
            }
         }
         String receiverhandshake = new String(buffer);
         LOG.info("Received Handshake: " + receiverhandshake);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(String message) {
		try {
			//send message header
			out.write(message.length() + MessagingConstants.EOH);
			//send message
			out.write(message);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	

}
