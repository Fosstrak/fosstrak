/*
 *  
 *  Fosstrak LLRP Commander (www.fosstrak.org)
 * 
 *  Copyright (C) 2008 ETH Zurich
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/> 
 *
 */

package org.fosstrak.llrp.adaptor;

import java.io.FileReader;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;
import java.util.List;

import org.fosstrak.llrp.adaptor.Adaptor;
import org.fosstrak.llrp.adaptor.AdaptorImpl;
import org.fosstrak.llrp.adaptor.Constants;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.llrp.ltk.generated.LLRPMessageFactory;
import org.llrp.ltk.types.LLRPMessage;

import util.AsyncNotif;

import junit.framework.TestCase;

/**
 * this class runs basic tests on the adaptor.
 * it covers both local and remote adaptors.
 * <ul>
 * <li>test getAdaptorName()</li>
 * <li>test containsReader(String readerName)</li>
 * <li>test getReaderNames()</li>
 * <li>test asynchronous registration/deregistration and callback</li>
 * <li>test </li>
 * </ul>
 * @author sawielan
 *
 */
public class AdaptorTest extends TestCase {

	/** 
	 * helper class to test the remote rmi adaptor.
	 * @author sawielan
	 *
	 */
	protected class MyRunner implements Runnable {

		public MyRunner(Adaptor adaptor) {
			try {
				try {
					LocateRegistry.createRegistry(Constants.registryPort);
				} catch (Exception e) {
					// the registry already exists...
				}
				
				Registry r = LocateRegistry.getRegistry(Constants.registryPort);
				r.bind(Constants.adaptorNameInRegistry, adaptor);					
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (AlreadyBoundException e) {
				e.printStackTrace();
			}				
		}
		
		public void run() {
			while (true) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					break;
				}
			}
			// deregister the adaptor.
			Registry r;
			try {
				r = LocateRegistry.getRegistry(Constants.registryPort);
				r.unbind(Constants.adaptorNameInRegistry);
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (NotBoundException e) {
				e.printStackTrace();
			}

		}
		
	}

	protected List<String> readerList = new LinkedList<String> ();
	protected String adaptorName = "myAdaptor";
	protected String readerName = "myReader";

	
	protected void setUp() throws Exception {
		super.setUp();
		
		readerList.add(readerName);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	/**
	 * run tests on the local adaptor without rmi.
	 * @throws Exception whenever there is any type of error.
	 */
	public void testLocalAdaptor() throws Exception {		
		Adaptor adaptor = new AdaptorImpl(adaptorName);
		// test if getAdaptorName works.
		assertAdaptorName(adaptorName, adaptor);
		
		// test if containsReader(String) works.
		assertContainsReader(false, readerName, adaptor);
		
		// test if getReaderNames() works
		// here we run it against an empty list.
		assertGetReaderNames(new LinkedList<String>(), adaptor);
		
		// test if notification mechanism works.
		assertAsyncNotificationCallback(adaptor, false);
	}
	
	/**
	 * run test on a remote adaptor with rmi.
	 * @throws Exception whenever there is any type of error.
	 */
	public void testRemoteAdaptor() throws Exception {
		// create an adaptor an register it through rmi
		AdaptorImpl serverAdaptor = new AdaptorImpl(Constants.adaptorNameInRegistry);
		Thread thread = new Thread(new MyRunner(serverAdaptor));
		thread.start();

		// lookup the server adaptor in the registry
		Registry registry = LocateRegistry.getRegistry("localhost", Constants.registryPort);
		Adaptor adaptor = (Adaptor) registry.lookup(Constants.adaptorNameInRegistry);
		
		// test if getAdaptorName works.
		assertAdaptorName(Constants.adaptorNameInRegistry, adaptor);
		
		// test if containsReader(String) works.
		assertContainsReader(false, readerName, adaptor);
		
		// test if getReaderNames() works
		// here we run it against an empty list.
		assertGetReaderNames(new LinkedList<String>(), adaptor);
		
		// test if notification mechanism works.
		assertAsyncNotificationCallback(adaptor, true);
		
		// kill the thread.
		thread.interrupt();
	}
	
	
	
	// --------------------- HELPER METHODS ---------------------------
	
	/**
	 * tests if the adaptor name equals a given string.
	 * @param adaptorName the name that is expected.
	 * @param adaptor the adaptor that shall be tested.
	 * @throws Exception whenever there is any type of error.
	 */
	protected void assertAdaptorName(String adaptorName, Adaptor adaptor) throws Exception {
		assertNotNull(adaptor);
		assertNotNull(adaptor.getAdaptorName());
		assertEquals(adaptorName, adaptor.getAdaptorName());
	}
	
	/**
	 * tests if the adaptor contains a given reader. the outcome is compared to the expected value.
	 * @param expected the result that is expected in this test.
	 * @param readerName the name of the reader to test.
	 * @param adaptor the adaptor to test.
	 * @throws Exception whenever there is any type of error.
	 */
	protected void assertContainsReader(boolean expected, String readerName, Adaptor adaptor) throws Exception {
		assertEquals(expected, adaptor.containsReader(readerName));
	}
	
	/**
	 * tests if the adaptor returns the correct list of readers.
	 * @param expected the list containing Strings with reader names that we expect.
	 * @param adaptor the adaptor to test.
	 * @throws Exception whenever there is any type of error.
	 */
	protected void assertGetReaderNames(List<String> expected, Adaptor adaptor) throws Exception {
		assertNotNull(adaptor.getReaderNames());
		assertEquals(expected, adaptor.getReaderNames());
	}

	/**
	 * create a asynchronous notification listener and test the message callback.
	 * @param adaptor the adaptor to test.
	 * @param remote if set to true the notification listener exports itself as a rmi stub (needed when testing remote adaptor).
	 * @throws Exception whenever there is any type of error.
	 */
	protected void assertAsyncNotificationCallback(Adaptor adaptor, boolean remote) throws Exception {
		AsyncNotif notif = new AsyncNotif(remote);
		
		// register
		adaptor.registerForAsynchronous(notif);

		// just use some dummy method for test of the callback.
		Document document = new SAXBuilder().build(new FileReader("src/test/config/getCapabilities.xml"));
		
		LLRPMessage message = LLRPMessageFactory.createLLRPMessage(document);
		String readerName = "callbackTestReader";

		byte[] binaryEncodedMessage = message.encodeBinary();
		
		// run the callback test.
		notif.asyncNotifMessage = null;
		notif.asyncNotifReaderName = null;
		adaptor.messageReceivedCallback(binaryEncodedMessage, readerName);
		assertNotNull(notif.asyncNotifMessage);
		assertTrue(notif.asyncNotifMessage instanceof LLRPMessage);
		
		assertNotNull(notif.asyncNotifReaderName);
		assertEquals(readerName, notif.asyncNotifReaderName);
		// deregister
 		adaptor.deregisterFromAsynchronous(notif);
		
		notif.asyncNotifMessage = null;
		notif.asyncNotifReaderName = null;
		adaptor.messageReceivedCallback(binaryEncodedMessage, readerName);
		assertNull(notif.asyncNotifMessage);
		assertNull(notif.asyncNotifReaderName);
	}
}
