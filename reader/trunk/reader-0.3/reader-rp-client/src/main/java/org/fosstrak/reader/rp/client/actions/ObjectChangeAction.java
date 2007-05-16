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
import javax.swing.JComboBox;

import org.accada.reader.rp.client.TestClient;
import org.accada.reader.rp.client.model.MethodComboBoxModel;

public class ObjectChangeAction extends AbstractAction {

	private TestClient parent;
	
	public ObjectChangeAction(TestClient parent) {
		this.parent = parent;
	}
	public void actionPerformed(ActionEvent arg0) {
		JComboBox box = (JComboBox)arg0.getSource();
		if (box.getSelectedItem().equals("ReaderDevice")) {
			parent.setCommandModel(MethodComboBoxModel.getInstance().rdModel);
		}
		else if (box.getSelectedItem().equals("Source")) {
			parent.setCommandModel(MethodComboBoxModel.getInstance().srcModel);
		}
		else if (box.getSelectedItem().equals("ReadPoint")) {
			parent.setCommandModel(MethodComboBoxModel.getInstance().rpModel);
		}
		else if (box.getSelectedItem().equals("TagSelector")) {
			parent.setCommandModel(MethodComboBoxModel.getInstance().tsModel);
		}
		else if (box.getSelectedItem().equals("DataSelector")) {
			parent.setCommandModel(MethodComboBoxModel.getInstance().dsModel);
		}
		else if (box.getSelectedItem().equals("NotificationChannel")) {
			parent.setCommandModel(MethodComboBoxModel.getInstance().ncModel);
		}
		else if (box.getSelectedItem().equals("Trigger")) {
			parent.setCommandModel(MethodComboBoxModel.getInstance().trgModel);
		}
		else if (box.getSelectedItem().equals("CommandChannel")) {
			parent.setCommandModel(MethodComboBoxModel.getInstance().ccModel);
		}
		else if (box.getSelectedItem().equals("EventType")) {
			parent.setCommandModel(MethodComboBoxModel.getInstance().etModel);
		}
		else if (box.getSelectedItem().equals("TriggerType")) {
			parent.setCommandModel(MethodComboBoxModel.getInstance().ttModel);
		}
		else if (box.getSelectedItem().equals("FieldName")) {
			parent.setCommandModel(MethodComboBoxModel.getInstance().fnModel);
		}
		else if (box.getSelectedItem().equals("TagField")) {
			parent.setCommandModel(MethodComboBoxModel.getInstance().tfModel);
		}		
	}

}
