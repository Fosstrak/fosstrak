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

package org.fosstrak.hal;

/**
 * The class is a data structure to store information that describes the memory 
 * structure of a tag, i.e. the memory banks.
 * 
 * @author 	Matthias Lampe, lampe@acm.org
 */
public class MemoryDescriptor {
	
	//---- Fields -----------------------------------------------------------

	/** The descriptions of the memory banks */
	protected MemoryBankDescriptor[] memoryBankDescriptors;

	
	//---- Constructor(s) ---------------------------------------------------

	/**
	 * creates a memory descriptor with the given paramter.
	 *
	 * @param memoryBankDescriptors the descriptions of the memory banks
	 */
	public MemoryDescriptor(MemoryBankDescriptor[] memoryBankDescriptors) {
		this.memoryBankDescriptors = memoryBankDescriptors;
	}


	//---- Methods ----------------------------------------------------------

	/**
	 * gets the descriptions of the memory banks.
	 * 
	 * @return the memory bank descriptors
	 */
	public MemoryBankDescriptor[] getMemoryBankDescriptors() {
		return memoryBankDescriptors;
	}

	/**
	 * Gets the number of memory banks.
	 * 
	 * @return the number of memory banks.
	 */
	int getNumberOfMemoryBanks() {
		return memoryBankDescriptors.length;
	}
}
