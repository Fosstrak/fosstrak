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

package org.accada.reader.rp.client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.accada.reader.rp.client.actions.ConnectAction;
import org.accada.reader.rp.client.actions.CreateCommandAction;
import org.accada.reader.rp.client.actions.DisconnectAction;
import org.accada.reader.rp.client.actions.ObjectChangeAction;
import org.accada.reader.rp.client.actions.SendAction;
import org.accada.reader.rp.client.actions.SetMessageFormatText;
import org.accada.reader.rp.client.actions.SetMessageFormatXML;
import org.accada.reader.rp.client.actions.SetTransportProtocolHttp;
import org.accada.reader.rp.client.actions.SetTransportProtocolTcp;
import org.accada.reader.rp.client.model.MethodComboBoxModel;
import org.accada.reader.rp.client.model.ObjectComboBoxModel;
import org.accada.reader.rp.proxy.msg.Client;
import org.accada.reader.rp.proxy.msg.ClientConnection;
import org.accada.reader.rp.proxy.msg.Handshake;
import org.accada.reader.rp.proxy.msg.Parameter;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * This is a test client which can be used to test an implementation
 * of the reader protocol. Command can be created by using an 
 * interactive user interface, the command is generated into the appropriate
 * message format. In this version only the XML format is supported.
 * 
 * Because I didn't focus on this client in my semester thesis the code
 * quality of this client might be not optimal :-) 
 * 
 * 
 * @author Andreas Fürer, ETH Zurich Switzerland, Winter 2005/06
 *
 */

public class TestClient extends JFrame implements Client {

	private JTextArea outText;
	private JTextArea inText;
	private JTextField hostField;
	private JTextField portField;
	private JButton sendButton;
	private JButton closeButton;
	private JButton connectButton;
	private JLabel transportLabel;
	private JRadioButton tcpConnectionButton;
	private JRadioButton httpConnectionButton;
	private JLabel formatLabel;
	private JRadioButton xmlFormatButton;
	private JRadioButton textFormatButton;
	private JLabel hostLabel;
	private JLabel portLabel;
	private Font monospaceFont;
	private JComboBox objComboBox;
	private JComboBox cmdComboBox;
	private JLabel targetLabel;
	private JLabel objLabel;
	private JLabel cmdLabel;
	private JTextField targetField;
	private JButton createCmdButton;
	private ParameterPanel parameterPanel;
	
	/* the model */
	private Handshake handshake;
	private ClientConnection conn;
	
	public void initialize() {
		handshake = new Handshake();
		
		this.setTitle("Reader Protocol Test Client");
		monospaceFont = new Font( "Monospaced", Font.PLAIN, 12 );
		
		Container content = this.getContentPane();
		content.setLayout(new BorderLayout());
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                getOutputPanel(),
                getInputPanel());
		splitPane.setOneTouchExpandable(true);
		splitPane.setResizeWeight(0.5);
		
		JPanel northPane = new JPanel();
		northPane.setLayout(new BoxLayout(northPane, BoxLayout.PAGE_AXIS));
		northPane.add(getConnectPanel());
		northPane.add(getCommandCreatorPanel());
		
		content.add(northPane, BorderLayout.NORTH);
		content.add(splitPane, BorderLayout.CENTER);
		content.add(getCommandPanel(), BorderLayout.SOUTH);
		setMainPanelEnabled(false);
	}
	
	private JPanel getOutputPanel() {
		JPanel outPane = new JPanel();
		outPane.setLayout(new GridBagLayout());

        outText = new JTextArea(5, 20);
        outText.setLineWrap(true);
        outText.setFont(monospaceFont);
        JScrollPane scrollPane = new JScrollPane(outText,
                                       JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                       JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        //Add Components to this panel.
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        outPane.add(scrollPane, c);
        outPane.setBorder(
                BorderFactory.createCompoundBorder(
                    BorderFactory.createCompoundBorder(
                                    BorderFactory.createTitledBorder("Request"),
                                    BorderFactory.createEmptyBorder(5,5,5,5)),
                    outPane.getBorder()));
        
        return outPane;
	}
	
	
	private JPanel getInputPanel() {
		JPanel inPane = new JPanel();
		inPane.setLayout(new GridBagLayout());

        inText = new JTextArea(5, 20);
        inText.setLineWrap(true);
        inText.setFont(monospaceFont);
        JScrollPane scrollPane = new JScrollPane(inText,
                                       JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                       JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        //Add Components to this panel.
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        inPane.add(scrollPane, c);
        inPane.setBorder(
                BorderFactory.createCompoundBorder(
                    BorderFactory.createCompoundBorder(
                                    BorderFactory.createTitledBorder("Response"),
                                    BorderFactory.createEmptyBorder(5,5,5,5)),
                    inPane.getBorder()));
        
        return inPane;
	}
	
	/**
	 * Creates the panel with the UI to create a command.
	 * @return Panel with the command UI.
	 */
	private JPanel getCommandCreatorPanel() {
		JPanel cmdCreatorPanel = new JPanel();
		cmdCreatorPanel.setBorder(
                BorderFactory.createCompoundBorder(
                    BorderFactory.createCompoundBorder(
                                    BorderFactory.createTitledBorder("Command"),
                                    BorderFactory.createEmptyBorder(5,5,5,5)),
                    cmdCreatorPanel.getBorder()));
        
		FormLayout layout = new FormLayout(
				"65dlu, 4dlu, left:pref:grow , 4dlu ,4dlu,pref",    // columns
				"pref, 2dlu, pref, 2dlu, pref, 8dlu, pref");   // rows
		cmdCreatorPanel.setLayout(layout);
		objLabel = new JLabel("Object");
		cmdLabel = new JLabel("Command");
		objComboBox = new JComboBox();
		objComboBox.setModel(new ObjectComboBoxModel());
		objComboBox.addActionListener(new ObjectChangeAction(this));
		targetLabel = new JLabel("Target:");
		targetField = new JTextField();
		createCmdButton = new JButton();
		createCmdButton.setAction(new CreateCommandAction(this));
		cmdComboBox = new JComboBox();
		cmdComboBox.setModel(MethodComboBoxModel.getInstance().rdModel);
		parameterPanel = new ParameterPanel(this);


		CellConstraints cc = new CellConstraints();
		cmdCreatorPanel.add(objLabel, cc.xy(1,1));
		cmdCreatorPanel.add(cmdLabel, cc.xy(3,1));
		cmdCreatorPanel.add(objComboBox, cc.xy(1,3));
		cmdCreatorPanel.add(cmdComboBox, cc.xywh(3,3,2,1));
		cmdCreatorPanel.add(createCmdButton, cc.xy(6,3));
		cmdCreatorPanel.add(targetLabel, cc.xy(1,5));
		cmdCreatorPanel.add(targetField, cc.xywh(3,5,2,1));
		cmdCreatorPanel.add(parameterPanel, cc.xywh(1,7,6,1));
		return cmdCreatorPanel;
	}
	
	/**
	 * Creates the panel with the "Send" and "Close" button
	 * @return
	 */
	private JPanel getCommandPanel() {
		JPanel cmdPane = new JPanel();
		sendButton = new JButton();
		sendButton.setAction(new SendAction(this));
		closeButton = new JButton();
		closeButton.setAction(new DisconnectAction(this));
		cmdPane.add(sendButton);
		cmdPane.add(closeButton);
		return cmdPane;
	}
	
	public void setMainPanelEnabled(boolean status) {
		inText.setEnabled(status);
		outText.setEnabled(status);
		sendButton.setEnabled(status);
		closeButton.setEnabled(status);
	}
	
	/**
	 * Sets the enabled status of all UI components within the
	 * "connect panel".
	 * 
	 * @param status Status wheter to enable the components or not.
	 */
	public void setConnectPanelEnabled(boolean status) {
		connectButton.setEnabled(status);
		transportLabel.setEnabled(status);
		tcpConnectionButton.setEnabled(status);
		httpConnectionButton.setEnabled(status);
		formatLabel.setEnabled(status);
		xmlFormatButton.setEnabled(status);
		textFormatButton.setEnabled(status);
		hostField.setEnabled(status);
		portField.setEnabled(status);
		hostLabel.setEnabled(status);
		portLabel.setEnabled(status);
	}

	/**
	 * Creates the top-most panel with all the connection related
	 * suff.
	 * 
	 * @return
	 */
	private JPanel getConnectPanel() {
		JPanel connectPanel = new JPanel();
		connectPanel.setBorder(
                BorderFactory.createCompoundBorder(
                    BorderFactory.createCompoundBorder(
                                    BorderFactory.createTitledBorder("Connection"),
                                    BorderFactory.createEmptyBorder(5,5,5,5)),
                    connectPanel.getBorder()));
        
		FormLayout layout = new FormLayout(
				"pref, 4dlu, pref , left:pref:grow, 4dlu,pref,4dlu, pref, 4dlu, pref",    // columns
				"pref, 2dlu, pref, 1dlu, pref");   // rows
		connectPanel.setLayout(layout);
		
		hostField = new JTextField("localhost");
		portField = new JTextField("8000");
		hostLabel = new JLabel("Host: ");
		portLabel = new JLabel("Port: ");
		formatLabel = new JLabel("Format:");
		transportLabel = new JLabel("Transport:");
		connectButton = new JButton();
		connectButton.setAction(new ConnectAction(this));
		
		ButtonGroup connectionGroup = new ButtonGroup();
		tcpConnectionButton = new JRadioButton();
		tcpConnectionButton.setAction(new SetTransportProtocolTcp(this));
		httpConnectionButton = new JRadioButton();
		httpConnectionButton.setAction(new SetTransportProtocolHttp(this));
		
		httpConnectionButton.setSelected(true);
		handshake.setTransportProtocol(Handshake.HTTP);
		ButtonGroup formatGroup = new ButtonGroup();
		xmlFormatButton = new JRadioButton();;
		xmlFormatButton.setAction(new SetMessageFormatXML(this));
		textFormatButton = new JRadioButton();
		textFormatButton.setAction(new SetMessageFormatText(this));
		
		xmlFormatButton.setSelected(true);
		handshake.setMessageFormat(Handshake.FORMAT_XML);
		
		/*//Shows the EPC logo
		ImageIcon epcLogo = createImageIcon("resources/epc_logo.gif");
		JLabel logoLabel = new JLabel();
		logoLabel.setIcon(epcLogo);
		*/
	
		CellConstraints cc = new CellConstraints();
		connectionGroup.add(tcpConnectionButton);
		connectionGroup.add(httpConnectionButton);
		formatGroup.add(xmlFormatButton);
		formatGroup.add(textFormatButton);
		connectPanel.add(hostLabel,cc.xy(1,1));
		connectPanel.add(hostField,cc.xywh(3,1,3,1));
		connectPanel.add(portLabel,cc.xy(6,1));
		connectPanel.add(portField,cc.xy(8,1));
		connectPanel.add(transportLabel,cc.xy(1,3));
		connectPanel.add(tcpConnectionButton,cc.xy(3,3));
		connectPanel.add(httpConnectionButton,cc.xy(4,3));
		connectPanel.add(formatLabel,cc.xy(1,5));
		connectPanel.add(xmlFormatButton,cc.xy(3,5));
		connectPanel.add(textFormatButton,cc.xy(4,5));
		connectPanel.add(connectButton,cc.xy(10,1));
		// connectPanel.add(logoLabel, cc.xywh(5,3,6,3));
		
		return connectPanel;
	}
	 /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        TestClient client = new TestClient();
        client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.initialize();

        //Display the window.
        client.pack();
        client.setVisible(true);
    }
	
	public static void main(String[] args) {
		  //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
	}
	
	public Handshake getHandshake() {
		return handshake;
	}
	
	/**
	 * Prints the input into the client and shows it in
	 * the input text field.
	 * @param line
	 */
	public void printInput(String line) {
		inText.append(line);
		inText.setCaretPosition(inText.getText().length());
	}

	/**
	 * @return Returns the conn.
	 */
	public ClientConnection getConn() {
		return conn;
	}

	/**
	 * @param conn The conn to set.
	 */
	public void setConn(ClientConnection conn) {
		this.conn = conn;
	}
	
	public String getMessage() {
		return outText.getText();
	}
	
	public int getPort() {
		return Integer.parseInt(portField.getText());
	}
	
	public String getHost() {
		return hostField.getText();
	}
	
	public void setCommandModel(DefaultComboBoxModel model) {
		cmdComboBox.setModel(model);
	}
	
	public Parameter[] getParameters() {
		return parameterPanel.getParameterListModel().getParameters();		
	}
	
	public String getRpObject() {
		return (String)objComboBox.getSelectedItem();
	}
	
	public String getRpTarget() {
		return targetField.getText();
	}
	
	public String getRpCommand() {
		return (String)cmdComboBox.getSelectedItem();
	}
	
	public void setOutText(String txt) {
		outText.setText(txt);
	}
	
	/**
	 * Returns an ImageIcon, or null if the path was invalid.
	 * @param path
	 * @return
	 */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = TestClient.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
