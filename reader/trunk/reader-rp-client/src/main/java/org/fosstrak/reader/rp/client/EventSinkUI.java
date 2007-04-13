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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class EventSinkUI extends JFrame implements ActionListener {
	
	private static int DEFAULT_PORT = 9000;
	
	private JTextArea inText;
	
	private Font monospaceFont;
	
	private int fontSize;
	
	
	public EventSinkUI() {
		super("Event Sink");
		fontSize = 12;
		buildUI();
	}
	
	private void buildUI() {
		this.setContentPane(getInputPanel());
		
		//Popup menu für TextArea erstellen
        final JPopupMenu popmen = new JPopupMenu();

        JMenuItem menu1 = new JMenuItem( "Clear content");
        menu1.setActionCommand("Clear content");
        menu1.addActionListener(this);
        JMenuItem menu2 = new JMenuItem("Increase font");
        menu2.setActionCommand("Increase font");
        menu2.addActionListener(this);
        JMenuItem menu3 = new JMenuItem("Decrease font");
        menu3.setActionCommand("Decrease font");
        menu3.addActionListener(this);
        popmen.add(menu1);
        popmen.addSeparator();
        popmen.add(menu2);
        popmen.add(menu3);
        
        inText.add(popmen);
        inText.addMouseListener( new MouseAdapter() {
            public void mouseReleased( MouseEvent me ) {
                if ( me.isPopupTrigger() )
                  popmen.show( me.getComponent(), me.getX(), me.getY() );
              }
            } );
        
	}
	
	private JPanel getInputPanel() {
		JPanel inPane = new JPanel();
		inPane.setLayout(new GridBagLayout());

        inText = new JTextArea(5, 20);
        inText.setLineWrap(true);
        setFontSize(fontSize);
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
                                    BorderFactory.createTitledBorder("Waiting for notifications on port " + DEFAULT_PORT + "..."),
                                    BorderFactory.createEmptyBorder(5,5,5,5)),
                    inPane.getBorder()));
        
        
        return inPane;
	}
	
	private void setFontSize(int size) {
		monospaceFont = new Font( "Monospaced", Font.PLAIN, size );
		inText.setFont(monospaceFont);
	}
	
	public void run() {
		try {
			ServerSocket ss = new ServerSocket(DEFAULT_PORT);
			while(true) {
				Socket s = ss.accept();
				DataInputStream in = new DataInputStream( s.getInputStream());
				
				String data = in.readLine();
				while(data != null) {
					inText.append(data + "\n");
					data = in.readLine();
					inText.setCaretPosition(inText.getText().length());
				}
			}
		} catch (Exception e) {
			inText.append("\nERROR: " + e.getMessage());
			System.out.println(e.getMessage());
		}
	}
	
	//---------- EVENT HANDLERS -------------
	public void actionPerformed( ActionEvent e )
	{
		if (e.getActionCommand().equals("Clear content")) {
			inText.setText("");
		} else if (e.getActionCommand().equals("Increase font")) {
			if (fontSize < 50) {
				fontSize += 5;
				setFontSize(fontSize);
			}
		} else if (e.getActionCommand().equals("Decrease font")) {
			if (fontSize > 7) {
				fontSize -= 5;
				setFontSize(fontSize);
			}
			
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        EventSinkUI client = new EventSinkUI();
        client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Display the window.
        client.pack();
        client.setSize(new Dimension(500,730));
        client.setVisible(true);
        client.run();

	}

}
