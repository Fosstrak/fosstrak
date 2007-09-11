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

package org.accada.reader.rp.client.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.accada.reader.rp.client.TestClient;
import org.accada.reader.rp.proxy.msg.ClientConnection;
import org.accada.reader.rp.proxy.msg.Handshake;
import org.accada.reader.rp.proxy.msg.HttpClientConnection;
import org.accada.reader.rp.proxy.msg.TcpClientConnection;

public class ConnectAction extends AbstractAction {

	private TestClient parent;
	
	public ConnectAction(TestClient parent) {
		super("Connect");
		this.parent = parent;
	}
	public void actionPerformed(ActionEvent arg0) {
		ClientConnection conn = null;
		if (parent.getHandshake().getTransportProtocol() == Handshake.HTTP) {
			conn = new HttpClientConnection(parent);
		} else {
			conn = new TcpClientConnection(parent);
		}
		conn.setPort(parent.getPort());
		conn.setHost(parent.getHost());
		parent.setConn(conn);
		conn.setHandshake(parent.getHandshake());
		boolean connected = conn.connect();
		if (connected) {
			parent.setMainPanelEnabled(true);
			parent.setConnectPanelEnabled(false);
		}
	}

}
