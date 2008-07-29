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

package org.fosstrak.reader.rprm.core.msg;

import javax.xml.bind.JAXBException;

/**
 * Exception which is thrown if the parsing could not be processed
 * successfully. This is a wrapper of different parsing exceptions, i.e.,
 * for the JAXBException and others generated by the different parser
 * implementations.
 * 
 * @author Andreas F�rer, ETH Zurich Switzerland, Winter 2005/06
 *
 */
public class MessageParsingException extends Exception {

	/**
	 * The serialVersionUID.
	 */
	private static final long serialVersionUID = 3520002005175713855L;

	/** The result code. */
	private int resultCode;

	/** The error name defined in RP 1.1 Specification, section 8.3. */
	private String errorName;

	/** The error description. */
	private String errorDescription;

	/**
	 * Constructor for a general MessageParsingExcption.
	 * 
	 * @param resultCode
	 *            The result code (e.g.,
	 *            <code>MessagingConstants.ERROR_UNKOWN</code>).
	 * @param errorName
	 *            The string representation of the error (e.g.,
	 *            <code>MessagingConstants.ERROR_UNKOWN_STR</code>).
	 * @param errorMessage
	 *            The error description
	 */
	public MessageParsingException(final int resultCode,
			final String errorName, final String errorMessage) {
		super(errorMessage);
		this.resultCode = resultCode;
		this.errorName = errorName;
		this.errorDescription = errorMessage;
	}

	/**
	 * Constructor for a general <code>MessageParsingException</code> as a
	 * wrapper of a <code>JAXBException</code>.
	 * 
	 * @param e
	 *            the <code>JAXBException</code> that caught this
	 *            <code>MessageParsingException</code>
	 */
	public MessageParsingException(final JAXBException e) {
		super(e.getMessage(), e.getCause());
		this.resultCode = MessagingConstants.ERROR_UNKNOWN;
		this.errorName = MessagingConstants.ERROR_UNKNOWN_STR;
		this.errorDescription = "[XML_PARSING_EXCEPTION]" + e.getMessage();
	}

	/**
	 * @return Returns the errorDescription.
	 */
	public String getErrorDescription() {
		return errorDescription;
	}

	/**
	 * @return Returns the errorName.
	 */
	public String getErrorName() {
		return errorName;
	}

	/**
	 * @return Returns the resultCode.
	 */
	public int getResultCode() {
		return resultCode;
	}

}
