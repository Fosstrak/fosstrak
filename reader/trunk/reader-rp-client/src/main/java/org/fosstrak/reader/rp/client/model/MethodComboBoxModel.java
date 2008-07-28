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

package org.fosstrak.reader.rp.client.model;

import javax.swing.DefaultComboBoxModel;

public class MethodComboBoxModel {
	public static DefaultComboBoxModel rdModel;
	public static DefaultComboBoxModel srcModel;
	public static DefaultComboBoxModel rpModel;
	public static DefaultComboBoxModel tsModel;
	public static DefaultComboBoxModel dsModel;
	public static DefaultComboBoxModel ncModel;
	public static DefaultComboBoxModel trgModel;
	public static DefaultComboBoxModel ccModel;
	public static DefaultComboBoxModel etModel;
	public static DefaultComboBoxModel ttModel;
	public static DefaultComboBoxModel fnModel;
	public static DefaultComboBoxModel tfModel;
	
	private static MethodComboBoxModel instance;
	
	private MethodComboBoxModel() {
		rpModel = new DefaultComboBoxModel();
		tsModel = new DefaultComboBoxModel();
		dsModel = new DefaultComboBoxModel();
		ccModel = new DefaultComboBoxModel();
		etModel = new DefaultComboBoxModel();
		ttModel = new DefaultComboBoxModel();
		fnModel = new DefaultComboBoxModel();
		tfModel = new DefaultComboBoxModel();
		
		rdModel = new DefaultComboBoxModel();
		rdModel.addElement("getEPC");
		rdModel.addElement("getManufacturer");
		rdModel.addElement("getModel");
		rdModel.addElement("getHandle");
		rdModel.addElement("setHandle");
		rdModel.addElement("getName");
		rdModel.addElement("setName");
		rdModel.addElement("getRole");
		rdModel.addElement("setRole");
		rdModel.addElement("getTimeTicks");
		rdModel.addElement("getTimeUTC");
		rdModel.addElement("setTimeUTC");
		rdModel.addElement("getManufacturerDescription");
		rdModel.addElement("getCurrentSource");
		rdModel.addElement("setCurrentSource");
		rdModel.addElement("getCurrentDataSelector");
		rdModel.addElement("setCurrentDataSelector");
		rdModel.addElement("removeSources");
		rdModel.addElement("removeAllSources");
		rdModel.addElement("getSource");
		rdModel.addElement("getAllSources");
		rdModel.addElement("removeDataSelectors");
		rdModel.addElement("removeAllDataSelectors");
		rdModel.addElement("getDataSelector");
		rdModel.addElement("getAllDataSelectors");
		rdModel.addElement("removeNotificationChannels");
		rdModel.addElement("removeAllNotificationChannels");
		rdModel.addElement("getNotificationChannel");
		rdModel.addElement("getAllNotificationChannels");
		rdModel.addElement("removeTriggers");
		rdModel.addElement("removeAllTriggers");
		rdModel.addElement("getTrigger");
		rdModel.addElement("getAllTriggers");
		rdModel.addElement("removeTagSelectors");
		rdModel.addElement("removeAllTagSelectors");
		rdModel.addElement("getTagSelector");
		rdModel.addElement("getAllTagSelectors");
		rdModel.addElement("removeTagFields");
		rdModel.addElement("removeAllTagFields");
		rdModel.addElement("getTagField");
		rdModel.addElement("getAllTagFields");
		rdModel.addElement("resetToDefaultSettings");
		rdModel.addElement("reboot");
		rdModel.addElement("goodbye");
		rdModel.addElement("getReadPoint");
		rdModel.addElement("getAllReadPoints");
		
		srcModel = new DefaultComboBoxModel();
		srcModel.addElement("create");
		srcModel.addElement("getName");
		srcModel.addElement("isFixed");
		srcModel.addElement("addReadPoints");
		srcModel.addElement("removeReadPoints");
		srcModel.addElement("removeAllReadPoints");
		srcModel.addElement("getReadPoint");
		srcModel.addElement("getAllReadPoints");
		srcModel.addElement("addReadTriggers");
		srcModel.addElement("removeReadTriggers");
		srcModel.addElement("removeAllReadTriggers");
		srcModel.addElement("getReadTrigger");
		srcModel.addElement("getAllReadTriggers");
		srcModel.addElement("addTagSelectors");
		srcModel.addElement("removeTagSelectors");
		srcModel.addElement("removeAllTagSelectors");
		srcModel.addElement("getTagSelector");
		srcModel.addElement("getAllTagSelectors");
		srcModel.addElement("getGlimpsedTimeout");
		srcModel.addElement("setGlimpsedTimeout");
		srcModel.addElement("getObservedTimeout");
		srcModel.addElement("setObservedTimeout");
		srcModel.addElement("getObservedThreshold");
		srcModel.addElement("setObservedThreshold");
		srcModel.addElement("getObservedTimeout");
		srcModel.addElement("setObservedTimeout");
		srcModel.addElement("getLostTimeout");
		srcModel.addElement("setLostTimeout");
		srcModel.addElement("rawReadIDs");
		srcModel.addElement("readIDs");
		srcModel.addElement("read");
		srcModel.addElement("write");
		srcModel.addElement("writeID");
		srcModel.addElement("write");
		srcModel.addElement("kill");
		srcModel.addElement("getReadCyclesPerTrigger");
		srcModel.addElement("setReadCyclesPerTrigger");
		srcModel.addElement("getMaxReadDutyCycle");
		srcModel.addElement("setMaxReadDutyCycle");
		srcModel.addElement("getReadTimetout");
		srcModel.addElement("setReadTimeout");
		srcModel.addElement("getSession");
		srcModel.addElement("setSession");
		
		trgModel = new DefaultComboBoxModel();
		trgModel.addElement("create");
		trgModel.addElement("getMaxNumberSupported");
		trgModel.addElement("getName");
		trgModel.addElement("getType");
		trgModel.addElement("getValue");
		trgModel.addElement("fire");
		
		ncModel = new DefaultComboBoxModel();
		ncModel.addElement("create");
		ncModel.addElement("getName");
		ncModel.addElement("getAddress");
		ncModel.addElement("getEffectiveAddress");
		ncModel.addElement("setAddress");
		ncModel.addElement("getDataSelector");
		ncModel.addElement("setDataSelector");
		ncModel.addElement("addSources");
		ncModel.addElement("removeSources");
		ncModel.addElement("getSource");
		ncModel.addElement("getAllSources");
		ncModel.addElement("addNotificationTriggers");
		ncModel.addElement("removeNotificationTriggers");
		ncModel.addElement("removeAllNotificationTriggers");
		ncModel.addElement("getNotificationTrigger");
		ncModel.addElement("getAllNotificationTriggers");
		ncModel.addElement("readQueuedData");
		
		rpModel.addElement("getName");
		
		tsModel.addElement("create");
		tsModel.addElement("getMaxNumberSupported");
		tsModel.addElement("getName");
		tsModel.addElement("getTagField");
		tsModel.addElement("getValue");
		tsModel.addElement("getMask");
		tsModel.addElement("getInclusiveFlag");
		
		dsModel.addElement("create");
		dsModel.addElement("getName");
		dsModel.addElement("addFieldNames");
		dsModel.addElement("removeFieldNames");
		dsModel.addElement("removeAllFieldNames");
		dsModel.addElement("getAllFieldNames");
		dsModel.addElement("addEventFilters");
		dsModel.addElement("removeEventFilters");
		dsModel.addElement("removeAllEventFilters");
		dsModel.addElement("getAllEventFilters");
		dsModel.addElement("addTagFieldNames");
		dsModel.addElement("removeTagFieldNames");
		dsModel.addElement("removeAllTagFieldNames");
		dsModel.addElement("getAllTagFieldNames");
		
		etModel.addElement("getSupportedTypes");
		
		ttModel.addElement("getSupportedTypes");
		
		fnModel.addElement("getSupportedNames");
		
		tfModel.addElement("create");
		tfModel.addElement("getName");
		tfModel.addElement("getTagFieldName");
		tfModel.addElement("setTagFieldName");
		tfModel.addElement("getMemoryBank");
		tfModel.addElement("setMemoryBank");
		tfModel.addElement("getOffset");
		tfModel.addElement("setOffset");
		tfModel.addElement("getLength");
		tfModel.addElement("setLength");
		
		fnModel.addElement("create");
		fnModel.addElement("getName");
		fnModel.addElement("getTagFieldName");
		fnModel.addElement("setTagFieldName");
		fnModel.addElement("getMemoryBank");
		fnModel.addElement("setMemoryBank");
		fnModel.addElement("getOffset");
		fnModel.addElement("setOffset");
		fnModel.addElement("getLength");
		fnModel.addElement("setLength");
		
		
	}
	
	public static MethodComboBoxModel getInstance() {
		if (instance == null) {
			instance = new MethodComboBoxModel();
		}
		return instance;
	}
	

}
