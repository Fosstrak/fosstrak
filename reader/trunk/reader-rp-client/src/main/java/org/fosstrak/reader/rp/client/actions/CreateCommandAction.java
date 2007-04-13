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

package org.accada.reader.rp.client.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.accada.reader.rp.client.TestClient;
import org.accada.reader.rp.proxy.msg.CommandFactory;
import org.accada.reader.rp.proxy.msg.Handshake;
import org.accada.reader.rp.proxy.msg.ParameterTypeException;

public class CreateCommandAction extends AbstractAction {

	private TestClient parent;
	private static int counter;
	
	public CreateCommandAction(TestClient parent) {
		super("Create Command");
		this.parent = parent;
	}
	public void actionPerformed(ActionEvent arg0) {
		try {
			String cmd = null;
			if (parent.getHandshake().getMessageFormat() == Handshake.FORMAT_XML) {
				cmd = CommandFactory.getXMLCommand(parent.getRpObject(), parent.getRpCommand(), parent.getParameters(), parent.getRpTarget());
			} else {
				cmd = CommandFactory.getTextCommand(parent.getRpObject(), parent.getRpCommand(), parent.getParameters(), parent.getRpTarget()) + "\r\n\r\n";
			}
			parent.setOutText(cmd);
		} catch(ArrayIndexOutOfBoundsException e) {
			JOptionPane.showMessageDialog(parent,
					"Wrong number of parameters. Please define all the required parameters.",
            	    "Wrong parameters",
            	    JOptionPane.ERROR_MESSAGE);			
		} catch(ParameterTypeException e) {
			JOptionPane.showMessageDialog(parent,
					e.getMessage(),
            	    "Falscher Parametertyp",
            	    JOptionPane.ERROR_MESSAGE);	
		}
		
	}
	
	public static int getNextId() {
		counter++;
		return counter;
	}

}
