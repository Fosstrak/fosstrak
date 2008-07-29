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
package org.fosstrak.reader.rp.proxy.msg.stubs.serializers.xml;

import java.util.Hashtable;
import java.util.Iterator;

import javax.xml.bind.JAXBException;

import org.fosstrak.reader.rprm.core.msg.command.HexStringListType;
import org.fosstrak.reader.rprm.core.msg.command.ReadPointListParamType;
import org.fosstrak.reader.rprm.core.msg.command.SourceCommand;
import org.fosstrak.reader.rprm.core.msg.command.TagFieldValueListParamType;
import org.fosstrak.reader.rprm.core.msg.command.TagSelectorListParamType;
import org.fosstrak.reader.rprm.core.msg.command.TriggerListParamType;
import org.fosstrak.reader.rprm.core.msg.command.SourceCommand.AddReadPoints;
import org.fosstrak.reader.rprm.core.msg.command.SourceCommand.RemoveReadPoints;
import org.fosstrak.reader.rprm.core.msg.util.HexUtil;
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

	private SourceCommand srcCommand = null;

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
		srcCommand = cmdFactory.createSourceCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#create(java.lang.String)
	 */
	public String create(String name) {
		resetCommand();
		SourceCommand.Create c = cmdFactory
				.createSourceCommandCreate();
		c.setName(name);
		srcCommand.setCreate(c);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#getName()
	 */
	public String getName() {
		resetCommand();
		srcCommand.setGetName(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#isFixed()
	 */
	public String isFixed() {
		resetCommand();
		srcCommand.setIsFixed(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#addReadPoints(org.fosstrak.reader.ReadPoint[])
	 */
	public String addReadPoints(ReadPoint[] readPoints) {
		resetCommand();
		AddReadPoints rps = cmdFactory
				.createSourceCommandAddReadPoints();
		ReadPointListParamType.List list = cmdFactory
				.createReadPointListParamTypeList();
		ReadPointListParamType listType = cmdFactory
				.createReadPointListParamType();
		list.getValue().addAll(toStringList(readPoints));
		listType.setList(list);
		rps.setReadPoints(listType);
		srcCommand.setAddReadPoints(rps);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#removeReadPoints(org.fosstrak.reader.ReadPoint[])
	 */
	public String removeReadPoints(ReadPoint[] readPoints) {
		resetCommand();
		RemoveReadPoints rps = cmdFactory
				.createSourceCommandRemoveReadPoints();
		ReadPointListParamType.List list = cmdFactory
				.createReadPointListParamTypeList();
		ReadPointListParamType listType = cmdFactory
				.createReadPointListParamType();
		list.getValue().addAll(toStringList(readPoints));
		listType.setList(list);
		rps.setReadPoints(listType);
		srcCommand.setRemoveReadPoints(rps);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#removeAllReadPoints()
	 */
	public String removeAllReadPoints() {
		resetCommand();
		srcCommand.setRemoveAllReadPoints(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#getReadPoint(java.lang.String)
	 */
	public String getReadPoint(String name) {
		resetCommand();
		SourceCommand.GetReadPoint rp = cmdFactory
				.createSourceCommandGetReadPoint();
		rp.setName(name);
		srcCommand.setGetReadPoint(rp);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#getAllReadPoints()
	 */
	public String getAllReadPoints() {
		resetCommand();
		srcCommand.setGetAllReadPoints(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#addReadTriggers(org.fosstrak.reader.Trigger[])
	 */
	public String addReadTriggers(Trigger[] triggers) {
		resetCommand();
		SourceCommand.AddReadTriggers rtrg = cmdFactory
				.createSourceCommandAddReadTriggers();
		TriggerListParamType.List list = cmdFactory
				.createTriggerListParamTypeList();
		TriggerListParamType listType = cmdFactory
				.createTriggerListParamType();
		list.getValue().addAll(toStringList(triggers));
		listType.setList(list);
		rtrg.setTriggers(listType);
		srcCommand.setAddReadTriggers(rtrg);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#removeReadTriggers(org.fosstrak.reader.Trigger[])
	 */
	public String removeReadTriggers(Trigger[] triggers) {
		resetCommand();
		SourceCommand.RemoveReadTriggers rtrgs = cmdFactory
				.createSourceCommandRemoveReadTriggers();
		TriggerListParamType.List list = cmdFactory
				.createTriggerListParamTypeList();
		TriggerListParamType listType = cmdFactory
				.createTriggerListParamType();
		list.getValue().addAll(toStringList(triggers));
		listType.setList(list);
		rtrgs.setTriggers(listType);
		srcCommand.setRemoveReadTriggers(rtrgs);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#removeAllReadTriggers()
	 */
	public String removeAllReadTriggers() {
		resetCommand();
		srcCommand.setRemoveAllReadTriggers(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#getReadTrigger(java.lang.String)
	 */
	public String getReadTrigger(String name) {
		resetCommand();
		SourceCommand.GetReadTrigger rtrg = cmdFactory
				.createSourceCommandGetReadTrigger();
		rtrg.setName(name);
		srcCommand.setGetReadTrigger(rtrg);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#getAllReadTriggers()
	 */
	public String getAllReadTriggers() {
		resetCommand();
		srcCommand.setGetAllReadTriggers(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#addTagSelectors(org.fosstrak.reader.TagSelector[])
	 */
	public String addTagSelectors(TagSelector[] selectors) {
		resetCommand();
		SourceCommand.AddTagSelectors tss = cmdFactory
				.createSourceCommandAddTagSelectors();
		TagSelectorListParamType.List list = cmdFactory
				.createTagSelectorListParamTypeList();
		TagSelectorListParamType listType = cmdFactory
				.createTagSelectorListParamType();
		list.getValue().addAll(toStringList(selectors));
		listType.setList(list);
		tss.setSelectors(listType);
		srcCommand.setAddTagSelectors(tss);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#removeTagSelectors(org.fosstrak.reader.TagSelector[])
	 */
	public String removeTagSelectors(TagSelector[] tagSelectors) {
		resetCommand();
		SourceCommand.RemoveTagSelectors tss = cmdFactory
				.createSourceCommandRemoveTagSelectors();
		TagSelectorListParamType.List list = cmdFactory
				.createTagSelectorListParamTypeList();
		TagSelectorListParamType listType = cmdFactory
				.createTagSelectorListParamType();
		list.getValue().addAll(toStringList(tagSelectors));
		listType.setList(list);
		tss.setSelectors(listType);
		srcCommand.setRemoveTagSelectors(tss);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#removeAllTagSelectors()
	 */
	public String removeAllTagSelectors() {
		resetCommand();
		srcCommand.setRemoveAllTagSelectors(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#getTagSelector(java.lang.String)
	 */
	public String getTagSelector(String name) {
		resetCommand();
		SourceCommand.GetTagSelector ts = cmdFactory
				.createSourceCommandGetTagSelector();
		ts.setName(name);
		srcCommand.setGetTagSelector(ts);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#getAllTagSelectors()
	 */
	public String getAllTagSelectors() {
		resetCommand();
		srcCommand.setGetAllTagSelectors(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#getGlimpsedTimeout()
	 */
	public String getGlimpsedTimeout() {
		resetCommand();
		srcCommand.setGetGlimpsedTimeout(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#setGlimpsedTimeout(int)
	 */
	public String setGlimpsedTimeout(int timeout) {
		resetCommand();
		SourceCommand.SetGlimpsedTimeout glmps = cmdFactory
				.createSourceCommandSetGlimpsedTimeout();
		glmps.setTimeout(timeout);
		srcCommand.setSetGlimpsedTimeout(glmps);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#getObservedThreshold()
	 */
	public String getObservedThreshold() {
		resetCommand();
		srcCommand.setGetObservedThreshold(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#setObservedThreshold(int)
	 */
	public String setObservedThreshold(int threshold) {
		resetCommand();
		SourceCommand.SetObservedThreshold t = cmdFactory
				.createSourceCommandSetObservedThreshold();
		t.setThreshold(threshold);
		srcCommand.setSetObservedThreshold(t);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#getObservedTimeout()
	 */
	public String getObservedTimeout() {
		resetCommand();
		srcCommand.setGetObservedTimeout(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#setObservedTimeout(int)
	 */
	public String setObservedTimeout(int timeout) {
		resetCommand();
		SourceCommand.SetObservedTimeout t = cmdFactory
				.createSourceCommandSetObservedTimeout();
		t.setTimeout(timeout);
		srcCommand.setSetObservedTimeout(t);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#getLostTimeout()
	 */
	public String getLostTimeout() {
		resetCommand();
		srcCommand.setGetLostTimeout(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#setLostTimeout(int)
	 */
	public String setLostTimeout(int timeout) {
		resetCommand();
		SourceCommand.SetLostTimeout t = cmdFactory
				.createSourceCommandSetLostTimeout();
		t.setTimeout(timeout);
		srcCommand.setSetLostTimeout(t);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#rawReadIDs(org.fosstrak.reader.testclient.command.DataSelectorSerializer)
	 */
	public String rawReadIDs(DataSelector dataSelector) {
		resetCommand();
		SourceCommand.RawReadIDs r = cmdFactory
				.createSourceCommandRawReadIDs();
		// Set DataSelector if optional parameter is available
		if (dataSelector != null) {
			r.setDataSelector(dataSelector.getName());
		}
		srcCommand.setRawReadIDs(r);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#readIDs(org.fosstrak.reader.testclient.command.DataSelectorSerializer)
	 */
	public String readIDs(DataSelector dataSelector) {
		resetCommand();
		SourceCommand.ReadIDs r = cmdFactory
				.createSourceCommandReadIDs();
		// Set DataSelector if optional parameter is available
		if (dataSelector != null) {
			r.setDataSelector(dataSelector.getName());
		}
		srcCommand.setReadIDs(r);
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
		SourceCommand.Read r = cmdFactory.createSourceCommandRead();

		// Set DataSelector if optional parameter is available
		if (dataSelector != null) {
			r.setDataSelector(dataSelector.getName());
		}

		// TODO: @Markus: passwords sollte keine Hashtable sein
		// Convert Hashtable into String[]
		String[] strPasswords;
		if (passwords != null) {
			strPasswords = new String[passwords.size()];
			Iterator it = passwords.values().iterator();
			int index = 0;
			while (it.hasNext()) {
				String s = (String) it.next();
				strPasswords[index] = s;
				index++;
			}
		} else {
			strPasswords = new String[0];
		}

		// Set passwords if optional parameter is available
		if (passwords != null) {
			HexStringListType.List list = cmdFactory
					.createHexStringListTypeList();
			HexStringListType listType = cmdFactory
					.createHexStringListType();
			list.getValue().addAll(toHexStringList(strPasswords));
			listType.setList(list);
			r.setPasswords(listType);
		}

		srcCommand.setRead(r);
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
		SourceCommand.WriteID w = cmdFactory
				.createSourceCommandWriteID();
		w.setId(HexUtil.hexToByteArray(newID));

		// Set selectors if optional parameter is available
		if (tagSelectors != null) {
			TagSelectorListParamType.List list = cmdFactory
					.createTagSelectorListParamTypeList();
			TagSelectorListParamType listType = cmdFactory
					.createTagSelectorListParamType();
			list.getValue().addAll(toStringList(tagSelectors));
			listType.setList(list);
			w.setSelectors(listType);
		}

		// Set passwords if optional parameter is available
		if (passwords != null) {
			HexStringListType.List list = cmdFactory
					.createHexStringListTypeList();
			HexStringListType listType = cmdFactory
					.createHexStringListType();
			list.getValue().addAll(toHexStringList(passwords));
			listType.setList(list);
			w.setPasswords(listType);
		}

		srcCommand.setWriteID(w);
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
		try {
			SourceCommand.Write w = cmdFactory
					.createSourceCommandWrite();
			// Set data
			TagFieldValueListParamType.List tfList = cmdFactory
					.createTagFieldValueListParamTypeList();
			TagFieldValueListParamType tfListType = cmdFactory
					.createTagFieldValueListParamType();
			tfList.getValue().addAll(toTagFieldValueList(tagFieldValues));
			tfListType.setList(tfList);
			w.setData(tfListType);

			// Set Selectors if optional parameter is available
			if (tagSelectors != null) {
				TagSelectorListParamType.List list = cmdFactory
						.createTagSelectorListParamTypeList();
				TagSelectorListParamType listType = cmdFactory
						.createTagSelectorListParamType();
				list.getValue().addAll(toStringList(tagSelectors));
				listType.setList(list);
				w.setSelectors(listType);
			}
			// Set passwords if optional parameter available
			if (passwords != null) {
				HexStringListType.List list = cmdFactory
						.createHexStringListTypeList();
				HexStringListType listType = cmdFactory
						.createHexStringListType();
				list.getValue().addAll(toHexStringList(passwords));
				listType.setList(list);
				w.setPasswords(listType);
			}

			srcCommand.setWrite(w);
			return serializeCommand();
		} catch (JAXBException e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#kill(java.lang.String[],
	 *      org.fosstrak.reader.TagSelector[])
	 */
	public String kill(String[] passwords, TagSelector[] tagSelectors) {
		resetCommand();
		SourceCommand.Kill k = cmdFactory.createSourceCommandKill();

		// Set Selectors if optional parameter is available
		if (tagSelectors != null) {
			TagSelectorListParamType.List list = cmdFactory
					.createTagSelectorListParamTypeList();
			TagSelectorListParamType listType = cmdFactory
					.createTagSelectorListParamType();
			list.getValue().addAll(toStringList(tagSelectors));
			listType.setList(list);
			k.setSelectors(listType);
		}
		// Set passwords if optional parameter available
		if (passwords != null) {
			HexStringListType.List list = cmdFactory
					.createHexStringListTypeList();
			HexStringListType listType = cmdFactory
					.createHexStringListType();
			list.getValue().addAll(toHexStringList(passwords));
			listType.setList(list);
			k.setPasswords(listType);
		}

		srcCommand.setKill(k);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#getReadCyclesPerTrigger()
	 */
	public String getReadCyclesPerTrigger() {
		resetCommand();
		srcCommand.setGetReadCyclesPerTrigger(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#setReadCyclesPerTrigger(int)
	 */
	public String setReadCyclesPerTrigger(int cycles) {
		resetCommand();
		SourceCommand.SetReadCyclesPerTrigger c = cmdFactory
				.createSourceCommandSetReadCyclesPerTrigger();
		c.setCycles(cycles);
		srcCommand.setSetReadCyclesPerTrigger(c);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#getMaxReadDutyCycles()
	 */
	public String getMaxReadDutyCycles() {
		resetCommand();
		srcCommand.setGetMaxReadDutyCycle(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#setMaxReadDutyCycles(int)
	 */
	public String setMaxReadDutyCycles(int cycles) {
		resetCommand();
		SourceCommand.SetMaxReadDutyCycle c = cmdFactory
				.createSourceCommandSetMaxReadDutyCycle();
		c.setDutyCycle(cycles);
		srcCommand.setSetMaxReadDutyCycle(c);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#getReadTimeout()
	 */
	public String getReadTimeout() {
		resetCommand();
		srcCommand.setGetReadTimeout(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#setReadTimeout(int)
	 */
	public String setReadTimeout(int timeout) {
		resetCommand();
		SourceCommand.SetReadTimeout t = cmdFactory
				.createSourceCommandSetReadTimeout();
		t.setTimeout(timeout);
		srcCommand.setSetReadTimeout(t);
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#getSession()
	 */
	public String getSession() {
		resetCommand();
		srcCommand.setGetSession(cmdFactory.createNoParamType());
		return serializeCommand();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.fosstrak.reader.testclient.command.SourceSerializer#setSession(int)
	 */
	public String setSession(int session) {
		resetCommand();
		SourceCommand.SetSession s = cmdFactory
				.createSourceCommandSetSession();
		s.setSession(session);
		srcCommand.setSetSession(s);
		return serializeCommand();
	}

	/**
	 * Serializes an Source command.
	 */
	public String serializeCommand() {
		command.setSource(srcCommand);
		return super.serializeCommand();
	}

}
