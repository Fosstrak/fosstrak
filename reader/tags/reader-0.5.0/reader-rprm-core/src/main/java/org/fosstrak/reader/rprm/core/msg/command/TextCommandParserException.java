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

package org.fosstrak.reader.rprm.core.msg.command;

/**
 * TextCommandParserException used together with <code>Parameter</code> and its 
 * child classes.
 * 
 * @see org.fosstrak.reader.rprm.core.msg.command.Parameter
 * @see org.fosstrak.reader.rprm.core.msg.command.ValueParameter
 * @see org.fosstrak.reader.rprm.core.msg.command.ListParameter
 * 
 * @author Andreas Fürer, ETH Zurich Switzerland, Winter 2005/06
 *
 */
public class TextCommandParserException extends Exception {

	public TextCommandParserException(String msg) {
		super(msg);
	}
}
