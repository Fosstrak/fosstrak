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

package org.fosstrak.reader.rp.proxy.msg;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;

import org.apache.log4j.xml.DOMConfigurator;


public abstract class ClientConnection {
	protected Socket socket = null;
	protected OutputStreamWriter out = null;
	protected DataInputStream in = null;

	protected Handshake handshake = null;

	protected Client client;
	protected Thread listenerThread = null;

	protected String host;
	private int port;

	public ClientConnection(Client target) {
      // initialize log4j
      DOMConfigurator.configure("./props/log4j.xml");

		this.client = target;
	}

	/**
	 * @return Returns the host.
	 */
	public String getHost() {
		return host;
	}
	/**
	 * @param host The host to set.
	 */
	public void setHost(String host) {
		this.host = host;
	}
	/**
	 * @return Returns the port.
	 */
	public int getPort() {
		return port;
	}
	/**
	 * @param port The port to set.
	 */
	public void setPort(int port) {
		this.port = port;
	}

	public boolean connect() {
		try {
			socket = new Socket(host, port);

			out = new OutputStreamWriter(socket.getOutputStream());
			in = new DataInputStream( socket.getInputStream());
			sendHandshake();
			StreamListener listener = new StreamListener(in);
			listenerThread = new Thread(listener);
			listenerThread.start();
			return true;
		} catch (IOException e) {
			client.printInput(e.getMessage() + "\n");
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void disconnect() {
		try {
			socket.shutdownInput();
			socket.shutdownOutput();
			socket.close();
			listenerThread.interrupt();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public abstract void sendHandshake();
	public abstract void sendMessage(String message);
	/**
	 * @return Returns the handshake.
	 */
	public Handshake getHandshake() {
		return handshake;
	}
	/**
	 * @param handshake The handshake to set.
	 */
	public void setHandshake(Handshake handshake) {
		this.handshake = handshake;
	}


	private class StreamListener implements Runnable {
		private DataInputStream inStream;

		public StreamListener(DataInputStream in) {
			this.inStream = in;
		}

		public void run() {
			try {
				String line = inStream.readUTF();
				while(line != null) {
					client.printInput(line);
					line = inStream.readUTF();
				}
			} catch (EOFException e) {

			} catch (SocketException e) {
				client.printInput(e.getMessage());

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}