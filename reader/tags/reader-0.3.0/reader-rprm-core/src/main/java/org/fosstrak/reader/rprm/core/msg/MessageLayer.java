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

package org.accada.reader.rprm.core.msg;


import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import org.accada.reader.rprm.core.ReaderDevice;
import org.accada.reader.rprm.core.ReaderProtocolException;
import org.accada.reader.rprm.core.mgmt.agent.snmp.SnmpAgent;
import org.accada.reader.rprm.core.mgmt.alarm.AlarmChannel;
import org.accada.reader.rprm.core.mgmt.simulator.MgmtSimulator;
import org.accada.reader.rprm.core.msg.transport.ConnectionThreadPool;
import org.accada.reader.rprm.core.msg.transport.ServerConnection;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.snmp4j.agent.io.ImportModes;

/**
 * MessageLayer is the main class. It instantiates the whole infrastructure.
 * After that, class <code>MessageDispatcher</code> and
 * <code>CommandDispatcher</code> are ready to execute commands on the reader.
 *
 * @author Patrice Oehen (poehen@student.ethz.ch)
 * @author Anna Wojtas, Marcel Bihr, Lukas Blunschi
 * @author Andreas Fürer, ETH Zurich, anfuerer@student.ethz.ch
 *
 */
public class MessageLayer {

	// ====================================================================
	// ---------------------------- Fields ------------------------------//
	// ====================================================================

	/** The logger. */
	private static Logger log = Logger.getLogger(MessageLayer.class);

	/** All connected <code>clients</code>. */
	private Clients clients = null;


	/**
	 * The time in ms that the reader waits for a notification connection in
	 * listen mode.
	 */
	private static int notificationListenTimeout;

	/** The single instance of the dispatcher. */
	private MessageDispatcher sDispatcher;

	/** The <code>IncomingMessageBuffer</code>. */
	private IncomingMessageBuffer mbuffer;

	/** The outgoing message dispatcher. */
	private OutgoingMessageDispatcher outDispatcher;

	/** The SNMP agent. */
	private SnmpAgent snmpAgent;

	/** The management agent's address */
	private String mgmtAgentAddress;

	/** The management agent's port */
	private int mgmtAgentPort;

	/** The management agent type (SNMP per default) */
	public static AgentType mgmtAgentType = AgentType.SNMP;

	/** Specifies whether the management simulator will be started */
	private boolean mgmtSimulatorStart;

	/** The management agent's properties file */
	private static final String mgmtAgentPropFile = "props/ReaderDevice.properties";

	/** The agent type enum */
	public enum AgentType {
		SNMP
	}

	// ====================================================================
	// ------------------------- Constructor ----------------------------//
	// ====================================================================

	/**
	 * Creates a new Message Layer
	 */
	public MessageLayer() {
		PropertyConfigurator.configure("./props/log4j.properties");
		//BasicConfigurator.configure();

		this.initialize();
	}

	// ====================================================================
	// ------------------------- Methods --------------------------------//
	// ====================================================================

	/**
	 * The main method.
	 *
	 * @param args
	 *            Not used
	 */
	public static void main(String[] args) {
		MessageLayer m = new MessageLayer();
	}

	/**
	 * Initializes the messaging layer and starts the reader device.
	 *
	 * @param reader reference to the ReaderDevice
	 */
	private void initialize() {
		log.debug("**************************************");
		log.debug("* MessageLayer is beeing initialized *");
		log.debug("**************************************");

		readMgmtAgentProperties(MessageLayer.mgmtAgentPropFile);

		switch (MessageLayer.mgmtAgentType) {

			case SNMP:

				/*************************
				 *   create SNMP agent   *
				 *************************/

				String bootCounterFileName = "SnmpAgentBC.cfg";
				String configFileName = "SnmpAgentConfig.cfg";
				File bootCounterFile = new File(bootCounterFileName);
				File configFile = new File(configFileName);

				// delete the old files
				bootCounterFile.delete();
				configFile.delete();

				// create agent
				snmpAgent = SnmpAgent.create(bootCounterFile, configFile,
						mgmtAgentAddress + "/" + mgmtAgentPort);
				break;

			// case ...:

		}


		/*************************
		 *     MessageLayer      *
		 *************************/

		String connType;

		// sets the parameters according to the properties file
		try {
			Map serverConns = new HashMap();
			MessageLayerConfiguration conf = MessageLayerConfiguration.getInstance();

			try {
				ConnectionThreadPool.create(conf.getThreadPoolSize());

			} catch (NumberFormatException e) {
				log.error("Could not interpret the threadPoolSize from the property file. Please ensure to use a correct integer format.");
			}

			if (conf.hasTcpServerConnection()) {
				connType = ServerConnection.TCP_SERVER;
				try {
					int tcpPort = conf.getTcpPort();
					serverConns.put(connType, new Integer(tcpPort));
				} catch (NumberFormatException e) {
					log.error("Could not interpret the tcpPort from the property file. Please ensure to use a correct integer format.");
				}
			}

			if (conf.hasHttpServerConnection()) {
				connType = ServerConnection.HTTP_SERVER;
				try {
					int httpPort = conf.getHttpPort();
					serverConns.put(connType, new Integer(httpPort));
				} catch (NumberFormatException e) {
					log.error("Could not interpret the httpPort from the property file. Please ensure to use a correct integer format.");
				}
			}

			try {
				notificationListenTimeout = conf.getNotificationListenTimeout();
			} catch (NumberFormatException e) {
				log.error("Could not interpret the notificationListenTimeout from the property file. Please ensure to use a correct integer format.");
			}

			// create the message buffer
			log.debug("creating an IncomingMessageBuffer");
			mbuffer = IncomingMessageBuffer.getInstance();

			// create clients
			log.debug("creating Clients");
			clients = Clients.getInstance();

			// create the outgoing message dispatcher
			log.debug("creating an OutgoingMessageDispatcher");
			outDispatcher = OutgoingMessageDispatcher.getInstance();
			outDispatcher.initialize(clients);

			// create and init the service dispatcher
			log.debug("creating a ServiceDispatcher");
			sDispatcher = MessageDispatcher.getInstance(this);
			sDispatcher.initialize(mbuffer, clients, outDispatcher);
			sDispatcher.start();

			log.debug("creating ServiceConnection");
			Iterator iter = serverConns.keySet().iterator();

			while (iter.hasNext()) {
				String cType = (String) iter.next();
				ServerConnection.createServerConnection(cType,
						((Integer) serverConns.get(cType)).intValue(), mbuffer);
			}

			ReaderDevice readerDevice = ReaderDevice.getInstance();

			switch (MessageLayer.mgmtAgentType) {

				case SNMP:

					/*************************
					 * initialize SNMP agent *
					 *************************/

					snmpAgent.init();
					snmpAgent.loadConfig(ImportModes.UPDATE_CREATE);

					// We need to add the alarm channels again because at their
					// creation the SNMP agent was not initialized yet.
					Enumeration<AlarmChannel> alarmChanIter = readerDevice.getAlarmChannels().elements();
					while (alarmChanIter.hasMoreElements()) {
						snmpAgent.addAlarmChannels(new AlarmChannel[] { alarmChanIter.nextElement() });
					}

					snmpAgent.run();
					break;

				// case ...:

			}

			if (mgmtSimulatorStart) {
				MgmtSimulator inst = new MgmtSimulator(readerDevice);
				inst.setVisible(true);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Resets the MessageLayer to the default values and restarts dispatching.
	 *
	 */
	public void reset() {
		/* TODO: Was ist genauer Semantik von reset?? Connections auch zumachen? */

		// Close all server connections
		List servers = ServerConnection.getServerConnections();
		for (Iterator it = servers.iterator(); it.hasNext();) {
			ServerConnection server = (ServerConnection) it.next();
			server.close();
		}

		// Reset all clients
		Clients clients = Clients.getInstance();
		clients.reset();

		// Reset all buffers and dispatchers
		sDispatcher.suspendThread();
		sDispatcher = null;

		mbuffer.clean();

		// Re-Initialize
		initialize();
	}

	/**
	 * The time in ms a server waits for a host to connect a notification
	 * connection in listen mode.
	 *
	 * @return The time in milliseconds.
	 */
	public static int getNotificationListenTimeout() {
		return notificationListenTimeout;
	}

	/**
	 * Gets the name of this class (e.g.,
	 * org.accada.reader.msg.MessageLayer).
	 *
	 * @return The class name of this class
	 */
	public static String getClassname() {
		Class clazz = MessageLayer.class;
		return clazz.getName();
	}

	/**
	 * Gets the current CLASSPATH
	 *
	 * @return The CLASSPATH
	 */
	public static String getClasspath() {
		return System.getProperty("java.class.path");
	}

	/**
	 * Reads the management agent properties from a file.
	 *
	 * @param propFile
	 *            The properties file
	 * @throws ReaderProtocolException
	 */
	private void readMgmtAgentProperties(String propFile) {
		XMLConfiguration conf;
		try {
			conf = new XMLConfiguration(propFile);
			MessageLayer.mgmtAgentType = AgentType.valueOf(conf.getString(
					"mgmtAgentType").toUpperCase());
			mgmtAgentAddress = conf.getString("mgmtAgentAddress");
			mgmtAgentPort = conf.getInt("mgmtAgentPort");
			mgmtSimulatorStart = conf.getBoolean("mgmtSimulatorStart");
		} catch (ConfigurationException e) {
			log.error("Failed to read the management agent information from "
					+ propFile + "\n -> Start default SNMP agent.");
			MessageLayer.mgmtAgentType = AgentType.SNMP;
		}
	}

}
