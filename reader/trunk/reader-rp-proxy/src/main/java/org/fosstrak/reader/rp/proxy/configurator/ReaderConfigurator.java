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

package org.accada.reader.rp.proxy.configurator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.accada.reader.rp.proxy.DataSelector;
import org.accada.reader.rp.proxy.NotificationChannel;
import org.accada.reader.rp.proxy.RPProxyException;
import org.accada.reader.rp.proxy.ReaderDevice;
import org.accada.reader.rp.proxy.Source;
import org.accada.reader.rp.proxy.Trigger;
import org.accada.reader.rp.proxy.configurator.DataSelectorConfiguration.EventFilters;
import org.accada.reader.rp.proxy.configurator.DataSelectorConfiguration.FieldNames;
import org.accada.reader.rp.proxy.configurator.DataSelectorConfiguration.TagFields;
import org.accada.reader.rp.proxy.configurator.NotificationChannelConfiguration.NotificationTriggers;
import org.accada.reader.rp.proxy.factories.DataSelectorFactory;
import org.accada.reader.rp.proxy.factories.NotificationChannelFactory;
import org.accada.reader.rp.proxy.factories.ReaderDeviceFactory;
import org.accada.reader.rp.proxy.factories.TriggerFactory;
import org.accada.reader.rp.proxy.msg.Handshake;
import org.accada.reader.rprm.core.TriggerType;
import org.apache.log4j.Logger;

/**
 * This class configurate a reader on the basis of a configuration file.
 * 
 * @author regli
 */
public class ReaderConfigurator {

	/** the logger */
	private static final Logger LOG = Logger.getLogger(ReaderConfigurator.class);
	
	/** the jaxb context */
	private static final String JAXB_CONTEXT = "org.accada.reader.rp.proxy.configurator";

	/** the reader device to configurate */
	private static ReaderDevice readerDevice;
	
	/**
	 * This method configurates a reader on the basis of a configuration file and returns his proxy.
	 * 
	 * @param configFilePath filepath to the configuration file
	 * @return proxy of the reader device
	 * @throws RPProxyException if configuration fails
	 */
	public static ReaderDevice initReaderDeviceConfiguration(String configFilePath) throws RPProxyException {
	
		// read configuration file
		ReaderDeviceConfiguration config = readFile(configFilePath);
		
		// get reader device proxy
		readerDevice = getReaderDevice(config.getReaderConfig());
		
		// get elements
		List<Object> elements = config.getTriggersOrDataSelectorsOrNotificationChannels();
		if (elements != null) {
			
			// create triggers, data selectors
			for (Object element : elements) {
				if (element instanceof Triggers) {
					for (Object trigger : ((Triggers)element).getContinuousTriggerOrTimerTriggerOrIoEdgeTrigger()) {
						createTrigger(trigger);
					}
				} else if (element instanceof DataSelectors) {
					for (DataSelectorConfiguration dataSelector : ((DataSelectors)element).getDataSelector()) {
						createDataSelector(dataSelector);
					}
				}
			}
			
			// create notification channels
			for (Object element : elements) {
				if (element instanceof NotificationChannels) {
					for (NotificationChannelConfiguration channelConfig : ((NotificationChannels)element).getNotificationChannel()) {
						createNotificationChannel(channelConfig);
					}
				} else if (element instanceof Sources) {
					for (SourceConfiguration sourceConfig : ((Sources)element).getSource()) {
						addTriggersToSource(sourceConfig);
					}
				}
			}
		}
		
		LOG.info("Configuration of ReaderDevice terminated");
		return readerDevice;
		
	}
	
	//
	// private methods
	//
	
	/**
	 * This method reads and parses the configuration file.
	 * 
	 * @param configFilePath filepath to the configuration file
	 * @return the parsed configuration as object
	 * @throws RPProxyException if configuration file could not be read or parsed
	 */
	private static ReaderDeviceConfiguration readFile(String configFilePath) throws RPProxyException {
		
		// try to parse reader configuration file
		LOG.debug("Parse configuration file '" + configFilePath + "'");
		try {
			
			// check if config file exists
			File configFile = new File(configFilePath);
			if (!configFile.exists()) {
				throw new RPProxyException("Configuration File '" + configFilePath + "' not found.");
			}
			
			// initialize jaxb context and unmarshaller
			JAXBContext context = JAXBContext.newInstance(JAXB_CONTEXT);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
			// unmarshal logical reader configuration file
			return ((ConfigurationDocument)unmarshaller.unmarshal(configFile)).getReaderDeviceConfiguration();
			
		} catch (JAXBException e) {
			throw new RPProxyException("Unable to parse configuration file: " + e.getMessage());
		}
		
	}
	
	/**
	 * This method creates a reader device proxy for the reader specified in the configuration.
	 * 
	 * @param config configuration read from file
	 * @return reader device proxy
	 * @throws RPProxyException if reader device proxy could not be created
	 */
	private static ReaderDevice getReaderDevice(ReaderConfiguration config) throws RPProxyException {
		
		// read config parameters
		String host = config.getCommandChannelHost();
		int port = config.getCommandChannelPort();
		int format;
		if (config.getTransportFormat() == TransportFormat.XML) {
			format = Handshake.FORMAT_XML;
		} else {
			format = Handshake.FORMAT_TEXT;
		}
		int protocol;
		if (config.getTransportProtocol() == TransportProtocol.HTTP) {
			protocol = Handshake.HTTP;
		} else {
			protocol = Handshake.TCP;
		}
		
		LOG.debug("Get Reader Device (host = '" + host + "' | port = '" + port + "' | format = '"
				+ format + "' | protocol = '" + protocol + "')");

		// create handshake
		Handshake handshake = new Handshake();
		handshake.setMessageFormat(format);
		handshake.setTransportProtocol(protocol);
		
		// create and return reader device proxy
		return ReaderDeviceFactory.getReaderDevice(host, port, handshake);
		
	}
	
	/**
	 * This method creates and configurates a notification channel on the basis of the notification channel configuration.
	 * 
	 * @param channelConfig notification channel configuration
	 * @throws RPProxyException if notification channel could not be created or configurated
	 */
	private static void createNotificationChannel(NotificationChannelConfiguration channelConfig) throws RPProxyException {
		
		// read parameters
		String name = channelConfig.getName();
		String protocol = channelConfig.getTransportProtocol().name().toLowerCase();
		String host = channelConfig.getNotificationChannelHost();
		int port = channelConfig.getNotificationChannelPort();
		String mode = channelConfig.getNotificationChannelMode().name().toLowerCase();
		
		String address = protocol + "://" + host + ":" + port + "?mode=" + mode;
		
		// remove notification channel if it already exists
		NotificationChannel notificationChannel = null;
		try {
			notificationChannel = readerDevice.getNotificationChannel(name);
		} catch (RPProxyException e) {}
		if (notificationChannel != null) {
			LOG.debug("Remove old NotificationChannel '" + name + "'");
			readerDevice.removeNotificationChannels(new NotificationChannel[] {notificationChannel});
		}
		
		// create notification channel
		LOG.debug("Create NotificationChannel '" + name + "'");
		notificationChannel = NotificationChannelFactory.createNotificationChannel(name, address, readerDevice);
		
		// add sources
		org.accada.reader.rp.proxy.configurator.NotificationChannelConfiguration.Sources sources = channelConfig.getSources();
		if (sources != null) {
			for (String sourceName : sources.getSource()) {
				LOG.debug("Add Source '" + sourceName + "' to NotificationChannel '" + name + "'");
				Source source = readerDevice.getSource(sourceName);
				notificationChannel.addSources(new Source[] {source});
			}
		}
		
		// add triggers
		NotificationTriggers triggers = channelConfig.getNotificationTriggers();
		if (triggers != null) {
			for (String triggerName : triggers.getTriggerName()) {
				LOG.debug("Add NotificationTrigger '" + triggerName + "' to NotificationChannel '" + name + "'");
				Trigger trigger = readerDevice.getTrigger(triggerName);
				notificationChannel.addNotificationTriggers(new Trigger[] {trigger});
			}
		}
		
		// add data selector
		String dataSelectorName = channelConfig.getDataSelector();
		if (dataSelectorName != null) {
			LOG.debug("Set DataSelector '" + dataSelectorName + "' of NotificationChannel '" + name + "'");
			DataSelector dataSelector = readerDevice.getDataSelector(dataSelectorName);
			notificationChannel.setDataSelector(dataSelector);
		}
		
	}

	/**
	 * This method creates and configurates a data selector on the basis of the data selector configuration.
	 * 
	 * @param dataSelectorConfig data selector configuration
	 * @throws RPProxyException if the data selector could not be created or configurated
	 */
	private static void createDataSelector(DataSelectorConfiguration dataSelectorConfig) throws RPProxyException {
		
		// read parameters
		String name = dataSelectorConfig.getName();
		List<String> events = new ArrayList<String>();
		List<String> fieldNames = new ArrayList<String>();
		List<String> tagFields = new ArrayList<String>();
		List<Object> elements = dataSelectorConfig.getFieldNamesOrEventFiltersOrTagFields();
		for (Object element : elements) {
			if (element instanceof EventFilters) {
				for (EventFilter eventFilter : ((EventFilters)element).getEventFilter()) {
					events.add(eventFilter.name());
				}
			} else if (element instanceof FieldNames) {
				for (FieldName fieldName : ((FieldNames)element).getFieldName()) {
					fieldNames.add(fieldName.name());
				} 
			} else if (element instanceof TagFields) {
				for (TagField tagField : ((TagFields)element).getTagField()) {
					tagFields.add(tagField.name());
				}
			}
		}

		// remove data selector if it already exists
		DataSelector dataSelector = null;
		try {
			dataSelector = readerDevice.getDataSelector(name);
		} catch (RPProxyException e) {}
		if (dataSelector != null) {
			LOG.debug("Remove old DataSelector '" + name + "'");
			readerDevice.removeDataSelectors(new DataSelector[] {dataSelector});
		}
		
		// create data selector
		LOG.debug("Create DataSelector '" + name + "'");
		dataSelector = DataSelectorFactory.createDataSelector(name, readerDevice);
		dataSelector.addEventFilters(events.toArray(new String[0]));
		dataSelector.addFieldNames(fieldNames.toArray(new String[0]));
		dataSelector.addTagFieldNames(tagFields.toArray(new String[0]));
		
	}

	/**
	 * This method creates and configurates a trigger on the basis of the trigger configuration.
	 * 
	 * @param triggerConfig trigger configuration
	 * @throws RPProxyException if the trigger could not be created or configurated
	 */
	private static void createTrigger(Object triggerConfig) throws RPProxyException {
		
		// read parameters
		String name;
		String type;
		String triggerValue;
		if (triggerConfig instanceof ContinuousTriggerConfiguration) {
			
			// continuous trigger
			ContinuousTriggerConfiguration continuousTrigger = (ContinuousTriggerConfiguration)triggerConfig;
			name = continuousTrigger.getName();
			type = TriggerType.CONTINUOUS;
			triggerValue = null;			
		} else if (triggerConfig instanceof TimerTriggerConfiguration) {
			
			// timer trigger
			TimerTriggerConfiguration timerTrigger = (TimerTriggerConfiguration)triggerConfig;
			name = timerTrigger.getName();
			type = TriggerType.TIMER;
			int interval = timerTrigger.getValue();
			triggerValue = "ms=" + interval;
		} else if (triggerConfig instanceof IOEdgeTriggerConfiguration) {
			
			// io edge trigger
			IOEdgeTriggerConfiguration ioEdgeTrigger = (IOEdgeTriggerConfiguration)triggerConfig;
			name = ioEdgeTrigger.getName();
			type = TriggerType.IO_EDGE;
			IOEdgeType edgeType = ioEdgeTrigger.getType();
			int port = ioEdgeTrigger.getPort();
			int pin = ioEdgeTrigger.getPin();
			triggerValue = "type=" + edgeType + ";port=" + port + ";pin=" + pin;
		} else if (triggerConfig instanceof IOValueTriggerConfiguration) {
			
			// io value trigger
			IOValueTriggerConfiguration ioValueTrigger = (IOValueTriggerConfiguration)triggerConfig;
			name = ioValueTrigger.getName();
			type = TriggerType.IO_VALUE;
			int port = ioValueTrigger.getPort();
			String value = ioValueTrigger.getValue();
			String mask = ioValueTrigger.getMask();
			triggerValue = "port=" + port + ";value=" + value + (mask != null ? ";mask" + mask : "");
		} else {
			throw new RPProxyException("Unkown TriggerType");
		}
		
		// remove trigger if it already exists
		Trigger trigger = null;
		try {
			trigger = readerDevice.getTrigger(name);
		} catch (RPProxyException e) {}
		if (trigger != null) {
			LOG.debug("Remove old Trigger '" + name + "'");
			readerDevice.removeTriggers(new Trigger[] {trigger});
		}
		
		// create trigger
		LOG.debug("Create Trigger '" + name + "'");
		trigger = TriggerFactory.createTrigger(name, type, triggerValue, readerDevice);
		
	}
	
	/**
	 * This method adds read triggers to sources on the basis of the source configuration.
	 * 
	 * @param sourceConfig source configuration
	 * @throws RPProxyException if a read trigger could not be added
	 */
	private static void addTriggersToSource(SourceConfiguration sourceConfig) throws RPProxyException {
		
		// read parameters
		String sourceName = sourceConfig.getName();
		Source source = readerDevice.getSource(sourceName);
		
		// add triggers to source
		for (String triggerName : sourceConfig.getTriggerName()) {
			LOG.debug("Add ReadTrigger '" + triggerName + "' to Source '" + sourceName + "'");
			Trigger trigger = readerDevice.getTrigger(triggerName);
			source.addReadTriggers(new Trigger[] {trigger});
		}
		
	}
	
}