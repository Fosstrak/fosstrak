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

package org.accada.reader.rp.proxy;

/**
 * This exception is thrown if something goes wrong in a proxy operation.
 * 
 * @author regli
 */
public class RPProxyException extends Exception {

	/** default serial id */
	private static final long serialVersionUID = 1L;
	
	/** description of the exception */
	private String description = null;

	/**
	 * Contructor initializes the exception.
	 * 
	 * @param msg exception message
	 */
	public RPProxyException(String msg) {
		
		super(msg);
		
	}
	
	/**
	 * Contructor initializes the exception.
	 * 
	 * @param msg exception message
	 * @param description describes the exception in more details
	 */
	public RPProxyException(String msg, String description) {
		
		this(msg);
		this.description = description;
		
	}
	
	/**
	 * This method returns the description of the exception.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		
		return description;
		
	}
	
	/** 
	 * This method returns if this exception has a description or not.
	 * 
	 * @return true if this exception has a description and false otherwise
	 */
	public boolean hasDescription() {
		
		return description != null;
		
	}
	
}