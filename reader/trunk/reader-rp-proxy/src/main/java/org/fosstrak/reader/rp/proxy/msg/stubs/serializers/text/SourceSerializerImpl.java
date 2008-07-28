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

/**
 * 
 */
package org.fosstrak.reader.rp.proxy.msg.stubs.serializers.text;

import java.util.Hashtable;

import org.fosstrak.reader.rp.proxy.msg.stubs.DataSelector;
import org.fosstrak.reader.rp.proxy.msg.stubs.ReadPoint;
import org.fosstrak.reader.rp.proxy.msg.stubs.TagFieldValue;
import org.fosstrak.reader.rp.proxy.msg.stubs.TagSelector;
import org.fosstrak.reader.rp.proxy.msg.stubs.Trigger;
import org.fosstrak.reader.rp.proxy.msg.stubs.serializers.SourceSerializer;

/**
 * @author Andreas
 * 
 */
public class SourceSerializerImpl extends CommandSerializerImpl implements
		SourceSerializer {

	/**
	 * @param targetName
	 */
	public SourceSerializerImpl(String targetName) {
		super(targetName);
		init();
	}

	/**
	 * @param id
	 */
	public SourceSerializerImpl(int id) {
		super(id);
		init();
	}

	/**
	 * @param id
	 * @param targetName
	 */
	public SourceSerializerImpl(int id, String targetName) {
		super(id, targetName);
		init();
	}

	private void init() {
		if (shortMode) {
			setObjectTypeName(TextTokens.SRC);
		} else {
			setObjectTypeName(TextTokens.SOURCE);
		}
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#create(java.lang.String)
	 */
	public String create(String name) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_C);
		} else {
			setCommandName(TextTokens.CMD_CREATE);
		}
		setParameter(name);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#getName()
	 */
	public String getName() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GN);
		} else {
			setCommandName(TextTokens.CMD_GETNAME);
		}
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#isFixed()
	 */
	public String isFixed() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_ISFX);
		} else {
			setCommandName(TextTokens.CMD_IS_FIXED);
		}
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#addReadPoints(org.fosstrak.reader.ReadPoint[])
	 */
	public String addReadPoints(ReadPoint[] readPoints) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_ARP);
		} else {
			setCommandName(TextTokens.CMD_ADD_READ_POINTS);
		}
		setParameterList(readPoints);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#removeReadPoints(org.fosstrak.reader.ReadPoint[])
	 */
	public String removeReadPoints(ReadPoint[] readPoints) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_RRP);
		} else {
			setCommandName(TextTokens.CMD_REMOVE_READ_POINTS);
		}
		setParameterList(readPoints);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#removeAllReadPoints()
	 */
	public String removeAllReadPoints() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_RARP);
		} else {
			setCommandName(TextTokens.CMD_REMOVE_ALL_READ_POINTS);
		}
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#getReadPoint(java.lang.String)
	 */
	public String getReadPoint(String name) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GRP);
		} else {
			setCommandName(TextTokens.CMD_GET_READ_POINT);
		}
		setParameter(name);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#getAllReadPoints()
	 */
	public String getAllReadPoints() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GARP);
		} else {
			setCommandName(TextTokens.CMD_GET_ALL_READ_POINTS);
		}
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#addReadTriggers(org.fosstrak.reader.Trigger[])
	 */
	public String addReadTriggers(Trigger[] triggers) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_ART);
		} else {
			setCommandName(TextTokens.CMD_ADD_READ_TRIGGERS);
		}
		setParameterList(triggers);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#removeReadTriggers(org.fosstrak.reader.Trigger[])
	 */
	public String removeReadTriggers(Trigger[] triggers) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_RRT);
		} else {
			setCommandName(TextTokens.CMD_REMOVE_READ_TRIGGERS);
		}
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#removeAllReadTriggers()
	 */
	public String removeAllReadTriggers() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_RARP);
		} else {
			setCommandName(TextTokens.CMD_REMOVE_ALL_READ_TRIGGERS);
		}
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#getReadTrigger(java.lang.String)
	 */
	public String getReadTrigger(String name) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GRT);
		} else {
			setCommandName(TextTokens.CMD_GET_READ_TRIGGER);
		}
		setParameter(name);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#getAllReadTriggers()
	 */
	public String getAllReadTriggers() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GART);
		} else {
			setCommandName(TextTokens.CMD_GET_ALL_READ_TRIGGERS);
		}
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#addTagSelectors(org.fosstrak.reader.TagSelector[])
	 */
	public String addTagSelectors(TagSelector[] selectors) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_ATS);
		} else {
			setCommandName(TextTokens.CMD_ADD_TAG_SELECTORS);
		}
		setParameterList(selectors);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#removeTagSelectors(org.fosstrak.reader.TagSelector[])
	 */
	public String removeTagSelectors(TagSelector[] tagSelectors) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_RTS);
		} else {
			setCommandName(TextTokens.CMD_REMOVE_TAG_SELECTORS);
		}
		setParameterList(tagSelectors);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#removeAllTagSelectors()
	 */
	public String removeAllTagSelectors() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_RATS);
		} else {
			setCommandName(TextTokens.CMD_REMOVE_ALL_TAG_SELECTORS);
		}
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#getTagSelector(java.lang.String)
	 */
	public String getTagSelector(String name) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GTS);
		} else {
			setCommandName(TextTokens.CMD_GET_TAG_SELECTOR);
		}
		setParameter(name);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#getAllTagSelectors()
	 */
	public String getAllTagSelectors() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GATS);
		} else {
			setCommandName(TextTokens.CMD_GET_ALL_TAG_SELECTORS);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#getGlimpsedTimeout()
	 */
	public String getGlimpsedTimeout() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GGTO);
		} else {
			setCommandName(TextTokens.CMD_GET_GLIMPSED_TIMEOUT);
		}
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#setGlimpsedTimeout(int)
	 */
	public String setGlimpsedTimeout(int timeout) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_SGTO);
		} else {
			setCommandName(TextTokens.CMD_SET_GLIMPSED_TIMEOUT);
		}
		setParameter(timeout);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#getObservedThreshold()
	 */
	public String getObservedThreshold() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GOTH);
		} else {
			setCommandName(TextTokens.CMD_GET_OBSERVED_THRESHOLD);
		}
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#setObservedThreshold(int)
	 */
	public String setObservedThreshold(int threshold) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_SOTH);
		} else {
			setCommandName(TextTokens.CMD_SET_OBSERVED_THRESHOLD);
		}
		setParameter(threshold);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#getObservedTimeout()
	 */
	public String getObservedTimeout() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GOTO);
		} else {
			setCommandName(TextTokens.CMD_GET_OBSERVED_TIMEOUT);
		}
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#setObservedTimeout(int)
	 */
	public String setObservedTimeout(int timeout) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_SOTO);
		} else {
			setCommandName(TextTokens.CMD_SET_OBSERVED_TIMEOUT);
		}
		setParameter(timeout);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#getLostTimeout()
	 */
	public String getLostTimeout() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GLTO);
		} else {
			setCommandName(TextTokens.CMD_GET_LOST_TIMEOUT);
		}
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#setLostTimeout(int)
	 */
	public String setLostTimeout(int timeout) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_SLTO);
		} else {
			setCommandName(TextTokens.CMD_SET_LOST_TIMEOUT);
		}
		setParameter(timeout);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#rawReadIDs(org.fosstrak.reader.testclient.command.DataSelectorSerializer)
	 */
	public String rawReadIDs(DataSelector dataSelector) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_RRID);
		} else {
			setCommandName(TextTokens.CMD_RAW_READ_IDS);
		}
		setParameter(dataSelector.getName());
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#readIDs(org.fosstrak.reader.testclient.command.DataSelectorSerializer)
	 */
	public String readIDs(DataSelector dataSelector) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_RID);
		} else {
			setCommandName(TextTokens.CMD_READ_IDS);
		}
		setParameter(dataSelector.getName());
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#read(org.fosstrak.reader.testclient.command.DataSelectorSerializer,
	 *      java.util.Hashtable)
	 */
	public String read(DataSelector dataSelector, Hashtable passwords) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_R);
		} else {
			setCommandName(TextTokens.CMD_READ);
		}
		setParameter(dataSelector.getName());
		//TODO: how to serialize hashtable with passwords????
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#writeID(java.lang.String,
	 *      java.lang.String[], org.fosstrak.reader.TagSelector[])
	 */
	public String writeID(String newID, String[] passwords,
			TagSelector[] tagSelectors) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_WID);
		} else {
			setCommandName(TextTokens.CMD_WRITE_ID);
		}
		//TODO: Parameter setzen!!!!!!!!!
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#write(org.fosstrak.reader.TagFieldValue[],
	 *      java.lang.String[], org.fosstrak.reader.TagSelector[])
	 */
	public String write(TagFieldValue[] tagFieldValues, String[] passwords,
			TagSelector[] tagSelectors) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_W);
		} else {
			setCommandName(TextTokens.CMD_WRITE);
		}
		//TODO: Parameter setzen!!!!!!!!!!!!! ACHTUNG: Manche evtl. null
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#kill(java.lang.String[],
	 *      org.fosstrak.reader.TagSelector[])
	 */
	public String kill(String[] passwords, TagSelector[] tagSelectors) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_K);
		} else {
			setCommandName(TextTokens.CMD_KILL);
		}
		//TODO: Parameter setzen!!!!! ACHTUNG: Manche evtl. null
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#getReadCyclesPerTrigger()
	 */
	public String getReadCyclesPerTrigger() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GRCPT);
		} else {
			setCommandName(TextTokens.CMD_GET_READ_CYCLES_PER_TRIGGER);
		}
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#setReadCyclesPerTrigger(int)
	 */
	public String setReadCyclesPerTrigger(int cycles) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_SRCPT);
		} else {
			setCommandName(TextTokens.CMD_SET_READ_CYCLES_PER_TRIGGER);
		}
		setParameter(cycles);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#getMaxReadDutyCycles()
	 */
	public String getMaxReadDutyCycles() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GMRDC);
		} else {
			setCommandName(TextTokens.CMD_GET_MAX_READ_DUTY_CYCLE);
		}
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#setMaxReadDutyCycles(int)
	 */
	public String setMaxReadDutyCycles(int cycles) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_SMRDC);
		} else {
			setCommandName(TextTokens.CMD_SET_MAX_READ_DUTY_CYCLE);
		}
		setParameter(cycles);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#getReadTimeout()
	 */
	public String getReadTimeout() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GRTO);
		} else {
			setCommandName(TextTokens.CMD_GET_READ_TIMEOUT);
		}
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#setReadTimeout(int)
	 */
	public String setReadTimeout(int timeout) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_SRTO);
		} else {
			setCommandName(TextTokens.CMD_SET_READ_TIMEOUT);
		}
		setParameter(timeout);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#getSession()
	 */
	public String getSession() {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_GSS);
		} else {
			setCommandName(TextTokens.CMD_GET_SESSION);
		}
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#setSession(int)
	 */
	public String setSession(int session) {
		resetCommand();
		if (shortMode) {
			setCommandName(TextTokens.CMD_SSS);
		} else {
			setCommandName(TextTokens.CMD_SET_SESSION);
		}
		return serializeCommand();
	}

	/**
	 * Serializes an Source command.
	 */
	public String serializeCommand() {
		return super.serializeCommand();
	}

}
