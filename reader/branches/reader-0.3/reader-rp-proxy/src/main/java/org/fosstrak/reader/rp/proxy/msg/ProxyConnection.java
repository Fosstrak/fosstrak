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

import java.io.IOException;

import org.accada.reader.rp.proxy.RPProxyException;
import org.accada.reader.rp.proxy.Result;
import org.accada.reader.rprm.core.msg.reply.Reply;
import org.apache.log4j.Logger;

/**
 * This class represents a proxy connection. Each reader device proxy owns such a connection.
 * Over the method executeCommand() commands can be executed over this connection.
 * 
 * @author regli
 */
public class ProxyConnection implements Client {

	/** the logger */
	private static final Logger LOG = Logger.getLogger(ProxyConnection.class);
	
	/** the host of the reader device */
	private final String host;
	
	/** the port of the reader device */
	private final int port;
	
	/** stores the message format and the transport protocol */
	private final Handshake handshake;
	
	/** the result of the command execution */
	private final Result result;
	
	/** the client connection over which the communication happens */
	private ClientConnection conn;
	
	/** indicates if the connection is connected or not */
	private boolean connected = false;
	
	/**
	 * Constructor sets the parameters.
	 * 
	 * @param host of the reader device
	 * @param port of the reader device
	 * @param handshake indicates the message format and the transport protocol
	 */
	public ProxyConnection(String host, int port, Handshake handshake) {
		
		this.host = host;
		this.port = port;
		this.handshake = handshake;
		result = new Result(handshake);
		
	}
	
	/**
	 * This method creates a new client connection and connects it to a reader device.
	 * 
	 * @return true if the client connection is established
	 * @throws RPProxyException if the connection could not be created
	 */
	public boolean connect() throws RPProxyException {
		
		if(isConnected()) disconnect();
		if (handshake.getTransportProtocol() == Handshake.HTTP) {
			conn = new HttpClientConnection(this);
		} else if (handshake.getTransportProtocol() == Handshake.TCP) {
			conn = new TcpClientConnection(this);
		} else {
			throw new RPProxyException("Unknown Protocol.");
		}
		conn.setHost(host);
		conn.setPort(port);
		if(conn.connect()) {;
			conn.setHandshake(handshake);
			conn.sendHandshake();
			connected = true;
			return true;
		}
		throw new RPProxyException("Unable to connect");
		
	}

	/**
	 * This method disconnects the client connection.
	 * 
	 * @return true if the client connection is disconnected
	 */
	public boolean disconnect() {
		
		conn.disconnect();
		connected = false;
		return true;
		
	}

	/**
	 * This method indicates if the connection is connected to a reader device.
	 * 
	 * @return true if the connection is connected and false otherwise
	 */
	public boolean isConnected() {
		
		return connected;
		
	}
	
	/**
	 * This message is invoked if the client connection receives data. 
	 * 
	 * @param data from the client connection
	 */
	public void printInput(String data) {
		
		LOG.debug("Read from proxy connection: " + data.replace('\n', ' '));
		try {
			result.addMsgFragment(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * This method executes a command on the reader device over the proxy connection.
	 * 
	 * @param object the type of the object on which the command should be executed
	 * @param command the command which should be executed
	 * @param parameterTypes the types of the parameters which belong to the command
	 * @param args the arguments which belong to the command
	 * @param target the name of the object on which the command shoud be executed
	 * @param ignoreNoReply indicates if it should be ignored if there is no reply or not
	 * @return the reply from the reader device
	 * @throws ParameterTypeException if the parameters are illegal
	 * @throws RPProxyException if there is an error while executing the command
	 */
	public Reply executeCommand(String object, String command, Class[] parameterTypes, Object[] args, String target, boolean ignoreNoReply) throws ParameterTypeException, RPProxyException {
		
		Parameter[] params = new Parameter[parameterTypes.length];
		ParameterType type;
		for (int i = 0; i < parameterTypes.length; i++) {
			type = new ParameterType(parameterTypes[i], parameterTypes[i].getName());
			params[i] = new Parameter(args[i] == null ? null : args[i].toString(), type);
		}
		result.init();
		if (handshake.getMessageFormat() == Handshake.FORMAT_XML) {
			conn.sendMessage(CommandFactory.getXMLCommand(object, command, params, target));
		} else if (handshake.getMessageFormat() == Handshake.FORMAT_TEXT) {
			conn.sendMessage(CommandFactory.getTextCommand(object, command, params, target) + "\r\n\r\n");
		} else {
			throw new RPProxyException("Unkown Format.");
		}
		if (result.getErrorCode() > 0) {
			if (ignoreNoReply) {
				return null;
			} else {
				throw new RPProxyException(result.getErrorName(), result.getErrorDescription());
			}
		}
		return result.get();
		
	}

}
