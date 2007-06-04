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

/*
 * Created on 2005
 */
package org.accada.reader.hal.impl.sim;

import java.util.Random;

import org.accada.reader.hal.util.ByteBlock;


/**
 * The class represents a simple tag that can be used a virtual tag within the Simulator Framework.
 * 
 * 
 * 
 * @author Roland Schneider
 *
 */
public class Tag implements Cloneable {
	
	/**
	 * The ID.
	 */
	private String snr;
	
	/**
	 * The memory banks.
	 */
	ByteBlock[] memory;

	/**
	 * Constructor without user memory.
	 * 
	 * @param snr
	 *            The ID
	 */
	public Tag(String snr){
		this.snr = snr;
		memory = new ByteBlock[4];
		
		memory[0] = new ByteBlock(new byte[] { }); // no passwords
		setTagID(snr); // EPC
		String tid = Integer.toHexString((new Random()).nextInt()); // random TID
		memory[2] = new ByteBlock((tid.length() % 2 != 0) ? (0 + tid) : tid);
		memory[3] = new ByteBlock(new byte[] { }); // no user memory
	}
	
	/**
	 * Constructor with user memory
	 * 
	 * @param snr
	 *            The ID
	 * @param userMemory
	 *            The user memory
	 */
	public Tag(String snr, byte[] userMemory){
		this.snr = snr;
		memory[0] = new ByteBlock(new byte[] { }); // no passwords
		setTagID(snr); // EPC
		String tid = Integer.toHexString((new Random()).nextInt()); // random TID
		memory[2] = new ByteBlock((tid.length() % 2 != 0) ? (0 + tid) : tid);
		memory[3] = new ByteBlock(userMemory); // user memory
	}
	
	/**
	 * Returns the ID of this tag.
	 * 
	 * @return The ID of this tag
	 */
	public String getTagID(){
		return snr;
	}
	
	/**
	 * Sets the tag's ID.
	 * 
	 * @param newID
	 *            The ID to be set
	 */
	public void setTagID(String newID){
		String id = ((newID.length() % 2 != 0) ? (0 + newID) : newID);
		memory[1] = new ByteBlock(id); // EPC
		this.snr = newID;
	}
	
	/**
	 * Reads some data.
	 * 
	 * @return The data
	 */
	public byte[] readData(int memoryBank, int offset, int length) {
		return ByteBlock.getRegion(memory[memoryBank].toByteArray(), offset, length);
	}
	
	/**
	 * Writes some data.
	 * 
	 * @param data
	 *            The data
	 */
	public void writeData(byte[] data, int memoryBank, int offset) {
		ByteBlock.replaceRegion(memory[memoryBank].getInternalByteArray(), offset, data);
	}
	
	/**
	 * Clones the tag.
	 *
	 * @return The deep copy of the RFID tag
	 *
	 */
	 public Object clone() {
	 	Tag tagCopy = new Tag( this.snr );
	 	for (int i = 0; i < memory.length; i++) {
	 		tagCopy.writeData(memory[i].toByteArray(), i, 0);
	 	}
	    return tagCopy;
	  }
	
	
	/**
	 * Compares SNR (the ID) of this tag and the argument.
	 * 
	 * @param snr
	 *            The ID
	 * @return <code>true</code> it the ID is the same, <code>false</code>
	 *         otherwise
	 */ 
	 private boolean compareSNR(String snr){
	 	return this.snr.equalsIgnoreCase(snr);
	 }
	 
	  /**
	   * Checks tags for equality.
	   * 
	   * @param tag
	   *            The tag to compare
	   * 
	   * @return If both IDs are equal <code>true</code> else
	   *         <code>false</code>
	   */
	  public boolean equals( Object tag ) {
	    if ( tag instanceof Tag ) {
	      return compareSNR(((Tag)tag).getTagID());
	    }
	    else {
	      return false;
	    }
	  }
	
	  /**
	   * Returns the hash code of the ID.
	   * 
	   * @return The hash code of the ID
	   */
	  public int hashCode() {	   
	    return snr.hashCode();
	  }
}
