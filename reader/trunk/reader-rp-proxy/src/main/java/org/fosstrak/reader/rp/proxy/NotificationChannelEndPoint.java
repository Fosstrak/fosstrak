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

package org.accada.reader.rp.proxy;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.accada.reader.rprm.core.msg.notification.Notification;
import org.apache.log4j.Logger;

/**
 * The class provides the basic functionality to listen to notification messages from
 * a reader via TCP and notify registered listeners. It acts as a end point for a 
 * notification channel on a reader.
 * 
 * @author regli
 */
public class NotificationChannelEndPoint implements Runnable {

	/** the logger */
	private static Logger log = Logger.getLogger(NotificationChannelEndPoint.class);
	
	/** the thread */
	private final Thread thread;
	
	/** contains the subscribers of this NotificationChannelEndPoint */
	private final List<NotificationChannelListener> listeners = new Vector<NotificationChannelListener>();
	
	/** server socket to communicate with the reader's notification channel */
	private final ServerSocket ss;

	/**
	 * Constructor opens the server socket and starts the thread.
	 * 
	 * @param port on which the notification channel communicates
	 * @throws RPProxyException if server socket could not be created on specified port.
	 */
	public NotificationChannelEndPoint(int port) throws RPProxyException {
		
		try {
			ss = new ServerSocket(port);
		} catch (IOException e) {
			throw new RPProxyException(e.getMessage());
		}
		
		thread = new Thread(this);
		thread.start();
		
	}
	
	/**
	 * This mehtod adds a new subscriber to the list of listeners.
	 * 
	 * @param listener to add to this notification channel end point
	 */
	public void addListener(NotificationChannelListener listener) {
		
		listeners.add(listener);
		
	}
	
	/**
	 * This method removes a subscriber from the list of listeners.
	 * 
	 * @param listener to remove from this notification channel end point
	 */
	public void removeListener(NotificationChannelListener listener) {
		
		listeners.remove(listener);
		
	}
	
	/**
	 * This method contains the main loop of the thread, in which data is read from the socket
	 * and forwarded to the method notifyListeners().
	 */
	public void run() {
		
		try {
			while (true) {
				Socket s = ss.accept();
				BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
				while (s.isConnected()) {
					StringBuffer data = new StringBuffer();
					String line = null;
					while (!reader.ready()) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
						}
					}
					while (!"".equals(line) && reader.ready()) {
						line = reader.readLine();
						data.append(line);
					};
					log.debug("Incoming notification: " + data);
					notifyListeners(data);
				}
			}
		} catch (IOException e) {
		}
		
	}
	
	/**
	 * This method stops the thread and closes the socket
	 */
	public void stop() {
		
		// stop thread
		if (thread.isAlive()) {
			thread.interrupt();
		}
		
		// close socket
		try {
			ss.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * This method starts the notification channel end point for testing purposes.
	 * 
	 * @param args command line arguments, which can contain the port number. If the
	 * port number is not given port 9000 is used.
	 * @throws RPProxyException if something goes wrong while creating notification channel end point
	 */
	public static void main(String[] args) throws RPProxyException {
		
		int port = 9000;
		if (args.length == 1) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {}
		}
		new Thread(new NotificationChannelEndPoint(port)).start();
		
	}
	
	//
	// private
	//
	
	/**
	 * This method parses a received notification message and notifies all
	 * subscribers.
	 * 
	 * @param data string buffer with notification as string
	 */
	private void notifyListeners(StringBuffer data) {
		
		Notification notification = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance("org.accada.reader.rprm.core.msg.notification");
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			notification = (Notification)unmarshaller.unmarshal(new ByteArrayInputStream(data.substring(data.indexOf("<")).getBytes()));
			
			Iterator listenerIt = listeners.iterator();
			while (listenerIt.hasNext()) {
				((NotificationChannelListener)listenerIt.next()).dataReceived(notification);
			}
		
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
	}

}