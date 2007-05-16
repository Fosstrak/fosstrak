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

package org.accada.reader.hal;


import java.util.Date;
import java.util.Vector;




/**
 * The <code>Observation</code> class provides a set of methods and fields
 * that represent a result of an identification process. Every
 * <code>Observation</code> object is uniquely defined by its timestamp, which
 * specifies when a certain observation has been made.
 * </p>
 * Every Observation is associated to a read point. Thereby the term read point
 * refers to a reader's antenna.
 */
public class Observation {
	
	
	//-------------------fields---------------------------------------------------

	/**
	 * The name of the HAL
	 */	
	private String halName;
	
	/**
	 * <code>readPointName</code> is to distinguish between several
	 * antennas/channels of a reader.
	 */
	private String readPointName = null;
	
	/**
	 * The timestamp.
	 */
	private long timestamp;
	
	/**
	 * The IDs of identified tags.
	 */
	private String[] ids = null;
	
	/**
	 * Specifies whether the accordant operation has been performed successfully
	 * or not.
	 */
	public boolean successful = true;
		
	
	//	----------------constructor-----------------------------------------------
	
	/**
	 * Constructor.
	 */
	public Observation() { }

	//-------------------method declarations ------------------------------------------

	/**
	 * Gets the timestamp.
	 * 
	 * @return The timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * Sets The timestamp.
	 * 
	 * @param timestamp
	 *            The timestamp
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Returns the name of the HAL.
	 * 
	 * @return The name of the HAL
	 */
	public String getHalName() {
		return halName;
	}
	
	/**
	 * Sets the HAL name.
	 * 
	 * @param halName
	 *            The HAL name to set
	 */
	public void setHalName(String halName) {
		this.halName = halName;
	}

	/**
	 * Gets the name of the read point involved in the identification.
	 * 
	 * @return The name of the read point
	 */
	public String getReadPointName() {
		return readPointName;
	}


	/**
	 * Sets the name of the read point involved in the identification.
	 * 
	 * @param readPointName
	 *            The name of read point to set
	 */
	public void setReadPointName(String readPointName) {
		this.readPointName = readPointName;
	}


	/**
	 * Gets the identified tags. Depending on the underlaying physical reader
	 * and the identification mechanisms used an Observation contains one ore
	 * more tag IDs.
	 * 
	 * @return The tag IDs
	 */
	public String[] getTagIds() {
		return ids;
	}


	/**
	 * Sets the IDs of the identified tags. Depending on the underlaying
	 * physical reader and the identification mechanisms used an Observation
	 * contains one ore more tag IDs.
	 * 
	 * @param idVector
	 *            The tagIDs
	 */
	public void setTagIds(Vector idVector) {
		int length = idVector.size();
		ids = new String[length];
		for (int i = 0; i < length; i++){
			ids[i] = (String) (idVector.elementAt(i));
		}
	}
	
	
	/**
	 * Checks if the observation contains a specific ID.
	 * 
	 * @param id
	 *            The ID
	 * @return <code>true</code> it the ID has been found, <code>false</code>
	 *         otherwise
	 */
	public boolean containsId(String id){
		for (int i = 0; i < ids.length; i++){
			if (ids[i].equals(id)){
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Returns a <code>String</code> containing all the observation
	 * information.
	 * 
	 * @return The information <code>String</code>
	 */
	public String toString(){
		String string = null;
		Date date = new Date(this.timestamp);
		string = "\n************************************";
		string = string + "\n Observation: "
			+ "\n HalName: " + this.halName
			+ "\n ReadPointName: " + this.readPointName
			+ "\n Timestamp: " + date.toString()
			+ "\n Tags: ";
		if (this.ids.length > 0){	
			for (int i=0; i<ids.length; i++){
				string = string + "\n    " + ids[i]; 
			}
		}
		string = string + "\n Successful: " + Boolean.toString(successful);
		
		string = string + "\n************************************";
		return string;
	}


}
