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

package org.fosstrak.capturingapp;

import org.apache.log4j.Logger;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;

/**
 * The default handler. This handler gets always loaded. if you invoke the 
 * handler with a change-set file, the handler will use this change-set to 
 * load the drools rule set.
 * @author sawielan
 *
 */
public class DefaultECReportHandler extends ECReportsHandler {

	// logger
	private static final Logger log = Logger.getLogger(DefaultECReportHandler.class);
	
	/**
	 * default constructor.
	 */
	public DefaultECReportHandler() {
		super();
	}
	
	/**
	 * create a new handler with a non default change set.
	 * @param changeSet
	 */
	public DefaultECReportHandler(String changeSet) {
		super(changeSet);
	}
	
	@Override
	public void loadRules() {
		// we only load the rules when the rule base is empty.
		if (null == kbase) {
			log.debug("loading rules from file.");
			kbuilder.add(
					ResourceFactory.newClassPathResource(
							changeSet,
							DefaultECReportHandler.class),
					ResourceType.CHANGE_SET);
		}

	}
}
