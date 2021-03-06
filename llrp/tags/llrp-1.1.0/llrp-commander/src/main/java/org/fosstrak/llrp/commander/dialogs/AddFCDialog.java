/*
 *  
 *  Fosstrak LLRP Commander (www.fosstrak.org)
 * 
 *  Copyright (C) 2008 ETH Zurich
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/> 
 *
 */

package org.fosstrak.llrp.commander.dialogs;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * dialog to add a new adaptor to the reader explorer.
 * @author sawielan
 *
 */
public class AddFCDialog extends ConnectDialog {
	
	/** the index for the adaptor name in the values array. */
	private static final int VALUE_NAME = 0;
	
	/** the index for the adaptor ip in the values array. */
	private static final int VALUE_IP = 1;
	
	/** handle to the local/remote button. */
	protected Button localAdapter;
	
	/** flag, whether local or remote adapter. */
	private boolean isLocalAdapter = false;

	/**
	 * create a new add adaptor dialog.
	 * @param aShell the parent shell.
	 */
	public AddFCDialog(Shell aShell) {
		super(aShell, "Add Adapter");
		FIELDS = new String[] { "Adapter Name", "IP" };
		DEFAULTS = new String [] { "AdapterName", "127.0.0.1" };
	}

	/**
	 * @return Logical Name of connection resource
	 */
	public String getName() {
		return values[VALUE_NAME];
	}
	
	/**
	 * if set to true, the adapter management will create a local adapter and 
	 * therefore ignoring the IP-address provided.
	 * @return true if user requests a local adapter, false otherwise. 
	 */
	public boolean isLocalAdapter() {
		return isLocalAdapter;
	}
	
	/**
	 * @return IP Address of connection resource
	 */
	public String getIP() {
		return values[VALUE_IP];
	}
	
	@Override
	protected Control createContents(Composite parent) {
		setLayout(parent);
		
		addTextFields(parent);
		
		// we need to create a special grid data object for the check-box 
		// without width-hint as otherwise the check-box will not be displayed 
		// in *nix ...
		GridData gridNoWidthHint = new GridData();
		gridNoWidthHint.horizontalSpan = 3;
		
		localAdapter = new Button(parent, SWT.CHECK);
		localAdapter.setText("local Adapter");
		localAdapter.setLayoutData(gridNoWidthHint);
		localAdapter.setSelection(false);
		
		addInvisibleButton(parent);
		addOKButton(parent);
		addCancelButton(parent);

		parent.pack();
		return parent;
	}
	
	@Override
	protected void addOKButton(Composite parent) {
		final Button btnOK = new Button(parent, SWT.PUSH);
		btnOK.setText("OK");
		btnOK.setLayoutData(gridLabel);
		
		btnOK.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent e) {
		    	  for (int i=0; i<DEFAULTS.length; i++) {
		    		  values[i] = txts[i].getText();
		    	  }
		    	  
		    	  isLocalAdapter = localAdapter.getSelection();
		    	  
		    	  setReturnCode(Window.OK);
		    	  close();
		      }
		    });
		
		
		registerTextFieldListeners(btnOK);
	}

	@Override
	public Listener getListener(final Text txt, int offset, final Button ok) {
		Listener listener = null;
		switch (offset) {
		case VALUE_NAME:
			listener = new Listener() {
				public void handleEvent(Event event) {
					try {
						if ((txt.getText() == null) || (txt.getText().length() < 3)) {
							ok.setEnabled(false);
						} else {
							ok.setEnabled(true);
						}
					} catch (Exception e) {
						ok.setEnabled(false);
					}
				}
			};
			break;
		case VALUE_IP:
			// we don't care about the IP format (hope that user does it right).
			break;
		}
		return listener;
	}
}

