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
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.accada.reader.rprm.core.ReaderDevice;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

/**
 * Loads the configuration properties for the MessageLayer from the property
 * file.
 * 
 * @author Andreas Fürer, ETH Zurich Switzerland, Winter 2005/06
 * 
 */
public final class MessageLayerConfiguration {

	/** The logger. */
	private static Logger log = Logger
			.getLogger(MessageLayerConfiguration.class);

	/** The path of the property file. */
	private static final String msgLayerPropFile = ReaderDevice.PROPERTIES_FILE;

	/** Key for the thread pool size property. */
	private static final String THREAD_POOL_SIZE = "threadPoolSize";

	/** Default value for the thread pool size. */
	public static final String THREAD_POOL_SIZE_DEFAULT = "16";

	/** Key for the TCP server connection property. */
	private static final String TCP_SERVER_CONNECTION = "tcpServerConnection";

	/** Key for the TCP port property. */
	private static final String TCP_PORT = "tcpPort";

	/** Key for the HTTP server connection property. */
	private static final String HTTP_SERVER_CONNECTION = "httpServerConnection";

	/** Key for the HTTP port property. */
	private static final String HTTP_PORT = "httpPort";

	/** Key for the notification timeout property. */
	private static final String NOTIFICATION_LISTEN_TIMEOUT = "notificationListenTimeout";

	/** Default value for the notification timeout. */
	public static final String NOTIFICATION_LISTEN_TIMEOUT_DEFAULT = "0";

	/** The properties. */
	private static XMLConfiguration configuration;

	/** The singleton configuration instance. */
	private static MessageLayerConfiguration instance;

	/**
	 * Private constructor to construct the hide the default constructor.
	 */
	private MessageLayerConfiguration() {
		configuration = getProperties();
	}

	/**
	 * Gets the singleton instance.
	 * 
	 * @return the singleton instance
	 */
	public static MessageLayerConfiguration getInstance() {
		if (instance == null) {
			instance = new MessageLayerConfiguration();
		}
		return instance;
	}

   /**
    * Singleton implementation of properties file accessor.
    * 
    * @return properties instance
    */
   private static XMLConfiguration getProperties() {
      return getProperties(msgLayerPropFile);
   }

	/**
	 * Singleton implementation of properties file accessor.
	 * 
	 * @return properties instance
	 */
	private static XMLConfiguration getProperties(final String propFile) {
		if (configuration == null) {
	      // properties
		   configuration = new XMLConfiguration();
	      try {
            // load resource from where this class is located
            Exception ex = new Exception();
            StackTraceElement[] sTrace = ex.getStackTrace();
            String className = sTrace[0].getClassName();
            String codesourcelocation = Class.forName(className).getProtectionDomain()
               .getCodeSource().getLocation().toString();
            String urlstring;
            URL fileurl;
            if (codesourcelocation.endsWith("jar")) {
               String configoutside = codesourcelocation.substring(0, codesourcelocation
                  .lastIndexOf("/") + 1) + propFile;
               boolean exists;
               try {
                  exists = (new File((new URL(configoutside)).toURI())).exists();
               } catch (URISyntaxException use) {
                  exists = false;
               } catch (MalformedURLException mue) {
                  exists = false;
               }
               if (exists) {
                  urlstring = configoutside;
               } else {
                  urlstring = "jar:" + codesourcelocation + "!/" + propFile;
               }
            } else {
               urlstring = codesourcelocation + propFile;
            }
            fileurl = new URL(urlstring);
	         configuration.load(fileurl);
	      } catch (ConfigurationException e) {
	         log.error("Could not find properties file: " + propFile);
	      } catch (MalformedURLException mue) {
            log.error("Could not find properties file: " + propFile);
         } catch (ClassNotFoundException cnfe) {
            log.error("Could not find properties file: " + propFile);
         }
	   }
		return configuration;
	}

	/**
	 * Gets the number of workers for the thread pool.
	 * 
	 * @return The number of workers or <code>THREAD_POOL_SIZE_DEFAULT</code>
	 *         if the property isn't specified in the property file.
	 * @throws NumberFormatException
	 *             If the pool size couldn't be converted into an
	 *             <code>int</code>.
	 */
	public int getThreadPoolSize() throws NumberFormatException {
      return Integer.parseInt(configuration.getString(
            THREAD_POOL_SIZE, THREAD_POOL_SIZE_DEFAULT));
	}

	/**
	 * Should a TCP server be openend?
	 * 
	 * @return the value of <code>tcpServerConnection</code> or
	 *         <code>false</code> if the value is not specified. Also if the
	 *         TCP port is not specified it returns <code>false</code>.
	 */
	public boolean hasTcpServerConnection() {
		String tcpConn = configuration.getString(TCP_SERVER_CONNECTION);
		if (tcpConn != null && tcpConn.equalsIgnoreCase("true")
				&& getTcpPort() != -1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Gets the port on which the TCP server should listen.
	 * 
	 * @return the TCP port or <code>-1</code> if there is no TCP port
	 *         specified.
	 * @throws NumberFormatException
	 *             If the value couldn't be converted into an <code>int</code>.
	 */
	public int getTcpPort() throws NumberFormatException {
		String tcpPort = configuration.getString(TCP_PORT);
		if (tcpPort != null) {
			return Integer.parseInt(tcpPort);
		} else {
			return -1;
		}
	}

	/**
	 * Should a HTTP server be openend?
	 * 
	 * @return the value of <code>httpServerConnection</code> or
	 *         <code>false</code> if the value is not specified. Also if the
	 *         HTTP port is not specified it returns <code>false</code>.
	 */
	public boolean hasHttpServerConnection() {
		String httpConn = configuration.getString(HTTP_SERVER_CONNECTION);
		if (httpConn != null && httpConn.equalsIgnoreCase("true")
				&& getHttpPort() != -1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Gets the port on which the HTTP server should listen.
	 * 
	 * @return the HTTP port or <code>-1</code> if there is no HTTP port
	 *         specified.
	 * @throws NumberFormatException
	 *             If the value couldn't be converted into an <code>int</code>.
	 */
	public int getHttpPort() throws NumberFormatException {
		String httpPort = configuration.getString(HTTP_PORT);
		if (httpPort != null) {
			return Integer.parseInt(httpPort);
		} else {
			return -1;
		}
	}

	/**
	 * Returns the timeout in ms which a NotificationChannel in listen mode is
	 * waiting for a connection from the client. A value of 0 indicates an
	 * infinite waiting time.
	 * 
	 * @return the timeout time or 0 if the property isn't specified.
	 * @throws NumberFormatException
	 *             If the value couldn't be converted into an <code>int</code>.
	 */
	public int getNotificationListenTimeout() throws NumberFormatException {
		return Integer.parseInt(configuration.getString(
				NOTIFICATION_LISTEN_TIMEOUT,
				NOTIFICATION_LISTEN_TIMEOUT_DEFAULT));
	}

}
