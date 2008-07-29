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

package org.fosstrak.reader.rp.client;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.fosstrak.reader.rp.client.model.ParameterListModel;
import org.fosstrak.reader.rp.client.model.TypeComboBoxModel;
import org.fosstrak.reader.rp.proxy.msg.Parameter;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class ParameterPanel extends JPanel {

	private Component parent;
	private JLabel paramLabel;
	private JComboBox typeComboBox;
	private JTextField valField;
	private JButton addButton;
	private JButton changeButton;
	private JList paramList;
	private JButton removeParamButton;
	private JButton removeAllParamButton;
	
	private ParameterListModel paramListModel;
	private TypeComboBoxModel typeComboBoxModel;
	
	public ParameterPanel(Component parent) {
		this.parent = parent;
		buildUI();
	}
	
	private void buildUI() {
		initComponents();
		layoutComponents();
	}
	
	private void initComponents() {
		paramLabel = new JLabel("Parameter");
		typeComboBox = new JComboBox();
		typeComboBoxModel = new TypeComboBoxModel();
		typeComboBox.setModel(typeComboBoxModel);
		valField = new JTextField();
		addButton = new JButton("Add");
		addButton.addActionListener(new AddButtonAction());
		changeButton = new JButton("Modify");
		changeButton.addActionListener(new ChangeButtonAction());
		changeButton.setEnabled(false);
		paramList = new JList();
		paramListModel = new ParameterListModel();
		paramList.setModel(paramListModel);
		paramList.addMouseListener(new ParameterChangeAction());
		removeParamButton = new JButton("Remove");
		removeParamButton.addActionListener(new RemoveParameterButtonAction());
		removeAllParamButton = new JButton("Remove all");
		removeAllParamButton.addActionListener(new RemoveAllParametersButtonAction());
	}
	
	private void layoutComponents() {
		FormLayout layout = new FormLayout(
			"65dlu, 4dlu, 50dlu, left:pref:grow,4dlu,pref,4dlu,pref",    // columns
			"pref, 2dlu, pref, 4dlu, pref, 2dlu, top:35dlu");   // rows
		setLayout(layout);
		
		CellConstraints cc = new CellConstraints();
        add(paramLabel, cc.xy(1,1));
        add(typeComboBox,cc.xy(1,3));
        add(valField, cc.xywh(3,3,2,1));
        add(addButton, cc.xy(6,3));
        add(changeButton, cc.xy(8,3));
        add(new JScrollPane(paramList), cc.xywh(1,5,4,3));
        add(removeParamButton, cc.xy(6,5));
        add(removeAllParamButton, cc.xy(6,7));
       
	}
	public ParameterListModel getParameterListModel() {
		return paramListModel;
	}
	private class AddButtonAction implements ActionListener {
        public void actionPerformed( ActionEvent actionEvent ) {
            try {
            	paramListModel.addElement(valField.getText(),typeComboBoxModel.getType());
            	valField.setText("");
            	changeButton.setEnabled(false);
            } catch (ClassCastException e) {
            	JOptionPane.showMessageDialog(parent,
            	    e.getMessage(),
            	    "Wrong parameter",
            	    JOptionPane.ERROR_MESSAGE);
            }
            
        }
    }
	
	private class ParameterChangeAction implements MouseListener {
		public void mouseClicked(MouseEvent actionEvent) {
			if (actionEvent.getClickCount() >= 2 && paramList.getSelectedIndex() >= 0) {
				Parameter p = (Parameter)paramListModel.getElementAt(paramList.getSelectedIndex());
				String val = p.getValue().toString();
				if (p.getParameterType().getType() == Collection.class) {
					//Replace the [ and ] if we have an array
					val = val.replaceFirst("\\[","");
					val = val.replaceAll("\\]","");
				}
				valField.setText(val);
				typeComboBoxModel.setSelectedItem(p.getParameterType());
				changeButton.setEnabled(true);
			}
			
			
		}
		public void mousePressed(MouseEvent actionEvent) {};
		public void mouseReleased(MouseEvent actionEvent) {};
		public void mouseEntered(MouseEvent actionEvent) {};
		public void mouseExited(MouseEvent actionEvent) {};
	}
	
	private class ChangeButtonAction implements ActionListener {
        public void actionPerformed( ActionEvent actionEvent ) {
        	try {
        		Parameter p = (Parameter)paramListModel.getElementAt(paramList.getSelectedIndex());
        		p.setParameter(valField.getText(),typeComboBoxModel.getType());
        		paramList.repaint();
        		changeButton.setEnabled(false);
        		valField.setText("");
        	} catch (ClassCastException e) {
            	JOptionPane.showMessageDialog(parent,
            	    e.getMessage(),
            	    "Wrong parameter",
            	    JOptionPane.ERROR_MESSAGE);
            }
			
        }
    }
	
	private class RemoveAllParametersButtonAction implements ActionListener {
        public void actionPerformed( ActionEvent actionEvent ) {
            paramListModel.removeAllElements();
            changeButton.setEnabled(false);
        }
    }
	private class RemoveParameterButtonAction implements ActionListener {
        public void actionPerformed( ActionEvent actionEvent ) {
        	int index = paramList.getSelectedIndex();
        	if (index >= 0) {
        		paramListModel.removeElementAt(index);
        		changeButton.setEnabled(false);
        	}
        }
    }
	
	
}
