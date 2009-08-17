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

package org.fosstrak.capturingapp.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.fosstrak.ale.xsd.epcglobal.EPC;
import org.fosstrak.epcis.model.ActionType;
import org.fosstrak.epcis.model.BusinessLocationType;
import org.fosstrak.epcis.model.EPCISBodyType;
import org.fosstrak.epcis.model.EPCISDocumentType;
import org.fosstrak.epcis.model.EPCListType;
import org.fosstrak.epcis.model.EventListType;
import org.fosstrak.epcis.model.ObjectEventType;
import org.fosstrak.epcis.model.ReadPointType;

/**
 * Helper class to assemble an EPCIS document. you can add object events and 
 * when you have all the events together, you can compile the final 
 * EPCIS document for further processing.
 * @author sawielan
 *
 */
public class SimpleEPCISDocument {
	
	/** a list of all the object events in the EPCIS document. */
	protected LinkedList<ObjectEventType> objectEvents = 
		new LinkedList<ObjectEventType> ();	
	
	/**
	 * add a new object event to the EPCIS document.
	 * @param epcs a list of EPCs to put into the report.
	 * @param action the kind of action triggered by this object even.
	 * @param bizSteps the <code>bizsteps</code> to set in the event.
	 * @param disposition the disposition.
	 * @param readPointId the id of the read point.
	 * @param bizLocationId the id of the location.
	 */
	public void addObjectEvent(List<Object> epcs, 
			ActionType action, String bizSteps, String disposition,
			String readPointId, String bizLocationId) {
		
		EPCListType epcList = new EPCListType();
		// add the epcs
		for (Object o : epcs) {
			if (o instanceof EPC) {
				EPC epc = (EPC) o;
				org.fosstrak.epcis.model.EPC nepc = 
					new org.fosstrak.epcis.model.EPC(); 
				nepc.setValue(epc.getValue());
				epcList.getEpc().add(nepc);
			}
		}
		
		ObjectEventType objEvent = new ObjectEventType();		
		objEvent.setEpcList(epcList);
		
		objEvent.setEventTime(getNow());
		objEvent.setEventTimeZoneOffset(getTimeOffset(objEvent.getEventTime()));
	
		// set action
		objEvent.setAction(action);
	
		// set bizStep
		objEvent.setBizStep(bizSteps);
	
		// set disposition
		objEvent.setDisposition(disposition);
	
		// set readPoint
		ReadPointType readPoint = new ReadPointType();
		readPoint.setId(readPointId);
		objEvent.setReadPoint(readPoint);
	
		// set bizLocation
		BusinessLocationType bizLocation = new BusinessLocationType();
		bizLocation.setId(bizLocationId);
		objEvent.setBizLocation(bizLocation);
		
		objectEvents.add(objEvent);
	}
	
	/**
	 * returns a string that describes the time offset.
	 * @param eventTime a gregorian calendar holding a time.
	 * @return a time offset string.
	 */
	protected String getTimeOffset(XMLGregorianCalendar eventTime) {
		String offset = "";
		// get the current time zone and set the eventTimeZoneOffset
		if (null != eventTime) {
		    int timezone = eventTime.getTimezone();
		    int h = Math.abs(timezone / 60);
		    int m = Math.abs(timezone % 60);
		    DecimalFormat format = new DecimalFormat("00");
		    String sign = (timezone < 0) ? "-" : "+";
		    offset = sign + format.format(h) + ":" + format.format(m);
		}
		return offset;
	}

	/**
	 * @return a gregorian calendar describing the current time.
	 */
	protected XMLGregorianCalendar getNow() {
		// get the current time and set the eventTime
		XMLGregorianCalendar now = null;
		try {
		    DatatypeFactory dataFactory = DatatypeFactory.newInstance();
		    now = dataFactory.newXMLGregorianCalendar(new GregorianCalendar());
		} catch (DatatypeConfigurationException e) {
		    e.printStackTrace();
		}
		return now;
	}

	/**
	 * @return the assembled EPCIS document.
	 */
	public EPCISDocumentType getDocument() {
		// create the EPCISDocument
		EPCISDocumentType epcisDoc = new EPCISDocumentType();
		EPCISBodyType epcisBody = new EPCISBodyType();
		EventListType eventList = new EventListType();
		
		for (ObjectEventType objEvent : objectEvents) {
			eventList.getObjectEventOrAggregationEventOrQuantityEvent().
				add(objEvent);
		}
		epcisBody.setEventList(eventList);
		epcisDoc.setEPCISBody(epcisBody);
		epcisDoc.setSchemaVersion(new BigDecimal("1.0"));
		epcisDoc.setCreationDate(getNow());
		return epcisDoc;
	}
}
