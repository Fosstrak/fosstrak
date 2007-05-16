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

package org.accada.reader.hal.impl.sim;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import org.accada.reader.hal.HardwareException;
import org.accada.reader.hal.impl.sim.graphic.Antenna;
import org.accada.reader.hal.impl.sim.graphic.Cable;
import org.accada.reader.hal.impl.sim.graphic.IGraphicSimulator;
import org.accada.reader.hal.impl.sim.graphic.Reader;
import org.accada.reader.hal.impl.sim.graphic.SelectionComponent;
import org.accada.reader.hal.impl.sim.graphic.Tag;
import org.accada.reader.hal.impl.sim.graphic.TranslationListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author regli
 */
public class GraphicSimulator extends JFrame implements SimulatorEngine, IGraphicSimulator {

	/**	the serial version uid */
	private static final long serialVersionUID = 1L;
	/** system default language */
	private static final Locale SYSTEM_DEFAULT_LOCALE = Locale.getDefault();
	/** the default language (if language is not defined in property file and system default language does not exists) */
	private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
	/** the properties file location and name */
	private static final String PROPERTIES_FILE_LOCATION = "/props/GraphicSimulator.properties";
	/** the logger */
	private static final Log LOG = LogFactory.getLog(GraphicSimulator.class);
	
	/** the resource bundle */
	private static ResourceBundle guiText; 
	/** the properties */
	private static Properties props;

	/** a hash set containing all the tags */
	private final HashSet tags = new HashSet();
	/** a hash set containing all the antennas */
	private final HashSet antennas = new HashSet();
	/** the listener which listen to tag translations */
	private final TranslationListener translationListener = new TranslationListener();
	/** the component for selection */
	private final SelectionComponent selection = new SelectionComponent();

	/** the simulator controller */
	private SimulatorController controller;
	/** the menu bar */
	private JMenuBar jJMenuBar;
	/** the layered pane which contains the antennas and tags */
	private JLayeredPane jLayeredPane;
	/** the context menu on the layered pane */
	private JPopupMenu contextMenu;
	/** the dialog window to add a new antenna */
	private JDialog newAntennaDialog;
	/** the dialog window to add a new tag */
	private JDialog newTagDialog;
	/** the active context menu */
	private JPopupMenu activeContextMenu;
	/** the tag id label */
	private JTextField tagIdField;
	/** the management simulator dialogs */
	private Hashtable<String, MgmtSimDialog> mgmtSimDialogs;
	/** the active context menu including the management simulator */
	private JPopupMenu mgmtSimMenu;
	/** the currently selected antenna */
	private Antenna selectedAntenna;
	
	/**
	 * implements the initialize method of the SimulatorEngine
     * 
     * @param controller 
     * @param file is only required for BatchSimulator
	 * @throws IOException 
	 */
	public void initialize(SimulatorController controller) throws IOException {
		this.controller = controller;
		mgmtSimDialogs = new Hashtable<String, MgmtSimDialog>();

		// load propterties
		props = new Properties();
		props.load(this.getClass().getResourceAsStream(PROPERTIES_FILE_LOCATION));
		
		// load text
		InputStream languageStream = null;
		
		// try to get language form property file
		if (props.containsKey("Language")) {
			languageStream = this.getClass().getResourceAsStream("/props/GUIText_" + props.getProperty("Language") + ".properties");
		}
		
		// try system default language
		if (languageStream == null) {
			languageStream = this.getClass().getResourceAsStream("/props/GUIText_" + SYSTEM_DEFAULT_LOCALE.getLanguage() + ".properties");
		}
		
		// try default language
		if (languageStream == null) {
			languageStream = this.getClass().getResourceAsStream("/props/GUIText_" + DEFAULT_LOCALE.getLanguage() + ".properties");
		}
		guiText = new PropertyResourceBundle(languageStream);
		
		// initialize GUI
		initializeGUI();
		LOG.info("GraphicSimulator started");
	}

	/**
	 *	initialize the graphic user interface
	 */
	private void initializeGUI() {
		if(!props.containsKey("WindowWidth")) {
			props.setProperty("WindowWidth", new Integer(2 * getProperty("FramePadding") + getProperty("TagWidth") + getProperty("HorizontalPadding") + getProperty("AntennasPerRow") * (getProperty("AntennaWidth") + getProperty("HorizontalInterAntennaPadding")) - getProperty("HorizontalInterAntennaPadding")).toString());
		}
		if(!props.containsKey("WindowHeight")) {
			try {
				props.setProperty("WindowHeight", new Integer(2 * getProperty("FramePadding") + getProperty("ReaderHeight") + getProperty("VerticalPadding") + (controller.getReadPointNames().length / getProperty("AntennasPerRow") + (controller.getReadPointNames().length % getProperty("AntennasPerRow") > 0 ? 1 : 0)) * (getProperty("AntennaHeight") + getProperty("VerticalInterAntennaPadding")) - getProperty("VerticalInterAntennaPadding") + 50).toString());
			} catch (HardwareException ignored) { }
		}
		if(!props.containsKey("AntennaPaneX")) {
			props.setProperty("AntennaPaneX", new Integer(getProperty("FramePadding") + getProperty("TagWidth") + getProperty("HorizontalPadding")).toString());
		}
		if(!props.containsKey("AntennaPaneY")) {
			props.setProperty("AntennaPaneY", new Integer(getProperty("FramePadding") + getProperty("ReaderHeight") + getProperty("VerticalPadding")).toString());
		}
		if(!props.containsKey("AntennaPaneWidth")) {
			props.setProperty("AntennaPaneWidth", new Integer(getProperty("WindowWidth") - getProperty("AntennaPaneX") - getProperty("FramePadding")).toString());
		}
		if(!props.containsKey("AntennaPaneHeight")) {
			props.setProperty("AntennaPaneHeight", new Integer(getProperty("WindowHeight") - getProperty("AntennaPaneY") - getProperty("FramePadding")).toString());
		}
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(getProperty("WindowWidth"), getProperty("WindowHeight"));
		this.setLayeredPane(getJLayeredPane());
		this.setJMenuBar(getJJMenuBar());
		
		this.setTitle(guiText.getString("ApplicationTitle"));
		this.setVisible(true);
	}
	
	/**
	 * creates the menu bar if it does not already exists
	 * 
	 * @return the menu bar
	 */
	private JMenuBar getJJMenuBar() {
		if (jJMenuBar == null) {
			jJMenuBar = new JMenuBar();
			jJMenuBar.add(getFileMenu());
			//jJMenuBar.add(getAntennaMenu());
			jJMenuBar.add(getTagMenu());
			jJMenuBar.add(getHelpMenu());
		}
		return jJMenuBar;
	}
	
	/**
	 * creates the file menu item if it does not already exists
	 * 
	 * @return file menu
	 */
	private JMenu getFileMenu() {
		JMenu fileMenu = new JMenu(guiText.getString("FileMenuItem"));
		
		// exit
		JMenuItem exitMenuItem = new JMenuItem();
		exitMenuItem.setText(guiText.getString("QuitMenuItem"));
		exitMenuItem.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				System.exit(0);
			}
		});
		fileMenu.add(exitMenuItem);
		return fileMenu;
	}

	/**
	 * creates the antenna menu item if it does not already exists
	 * 
	 * @return antenna menu
	 */
	private JMenu getAntennaMenu() {
		JMenu antennaMenu = new JMenu(guiText.getString("AntennaMenuItem"));
		
		// new antenna
		JMenuItem newAntennaMenuItem = new JMenuItem();
		newAntennaMenuItem.setText(guiText.getString("AddNewAntennaMenuItem"));
		newAntennaMenuItem.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				showAddAntennaDialog();
			}
		});
		antennaMenu.add(newAntennaMenuItem);
		return antennaMenu;
	}
	
	/**
	 * creates the tag menu item if it does not already exists
	 * 
	 * @return tag menu
	 */
	private JMenu getTagMenu() {
		JMenu tagMenu = new JMenu(guiText.getString("TagMenuItem"));
		
		// new tag
		JMenuItem newTagMenuItem = new JMenuItem();
		newTagMenuItem.setText(guiText.getString("AddNewTagMenuItem"));
		newTagMenuItem.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				showAddTagDialog();
			}
		});
		tagMenu.add(newTagMenuItem);
		return tagMenu;
	}
	
	/**
	 * creates the help menu item if it does not already exists
	 * 
	 * @return help menu
	 */
	private JMenu getHelpMenu() {
		JMenu helpMenu = new JMenu(guiText.getString("HelpMenuItem"));

		// about
		JMenuItem aboutMenuItem = new JMenuItem();
		aboutMenuItem.setText(guiText.getString("AboutMenuItem"));
		aboutMenuItem.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				JDialog aboutDialog = new JDialog(GraphicSimulator.this, guiText.getString("AboutDialogTitle"), true);
				Point pos = new Point();
				pos.x = jLayeredPane.getLocationOnScreen().x + (jLayeredPane.getWidth() - getProperty("DialogWindowWidth")) / 2;
				pos.y = jLayeredPane.getLocationOnScreen().y + (jLayeredPane.getHeight() - getProperty("DialogWindowHeight")) / 2;
				aboutDialog.setLocation(pos);
				aboutDialog.setSize(getProperty("DialogWindowWidth"), getProperty("DialogWindowHeight"));
				aboutDialog.setTitle(guiText.getString("AboutDialogTitle"));
				JLabel text = new JLabel(guiText.getString("AboutDialogContent"));
				text.setHorizontalAlignment(JLabel.CENTER);
				aboutDialog.add(text);
				aboutDialog.setVisible(true);
			}
		});
		helpMenu.add(aboutMenuItem);
		return helpMenu;
	}
	
	/**
	 * creates the layered pane if it does not already exists
	 * 
	 * @return layered pane
	 */
	private JLayeredPane getJLayeredPane() {
		if (jLayeredPane == null) {
			jLayeredPane = new JLayeredPane();
			jLayeredPane.setLayout(null);
			jLayeredPane.setOpaque(true);
			jLayeredPane.setBackground(Color.WHITE);
			jLayeredPane.add(getReader(), new Integer(0));

			// add antennas
			String[] antennaIds = null;
			try {
				antennaIds = controller.getReadPointNames();
			} catch (HardwareException ignored) { }
			for (int i = 0; i < antennaIds.length; i++) {
				createNewAntenna(antennaIds[i]);
			}
			jLayeredPane.add(getContextMenu());
			jLayeredPane.add(getMgmtSimMenu());

			// add mouse listener
			jLayeredPane.addMouseListener(new MouseAdapter() {

				// context menu
				public void mouseReleased(MouseEvent e) {
					hideActiveContextMenuAndSelection();
					if (e.getButton() == MouseEvent.BUTTON3) {
						Point posOnScreen = getJLayeredPane().getLocationOnScreen();
						contextMenu.setLocation(posOnScreen.x + e.getX(), posOnScreen.y + e.getY());
						contextMenu.setVisible(true);
						mgmtSimMenu.setVisible(false);
						setActiveContextMenu(contextMenu);
					} else {
						contextMenu.setVisible(false);
						mgmtSimMenu.setVisible(false);
						if(e.getButton() == MouseEvent.BUTTON1) {
							selection.select(tags);
						}
					}
				}
				
				// selection
				public void mousePressed(final MouseEvent e) {
					hideActiveContextMenuAndSelection();
					if(e.getButton() == MouseEvent.BUTTON1) {
						selection.setBounds(0, 0, getProperty("WindowWidth"), getProperty("WindowHeight"));
						selection.setStartPoint(e.getPoint());
						jLayeredPane.add(selection, new Integer(2));
					}
				}
			});
			
			// add drag listener
			jLayeredPane.addMouseMotionListener(new MouseMotionAdapter() {
				public void mouseDragged(MouseEvent e) {
					if(selection.isActive()) {
						selection.setCurrentPoint(e.getPoint());
					}
				}
			});
		}
		return jLayeredPane;
	}

	/**
	 * creates a label with the reader
	 * 
	 * @return reader label
	 */
	private Component getReader() {
		JLabel  reader = new JLabel();
		String filename = props.getProperty("ReaderImage");
		reader.setIcon(new ImageIcon(this.getClass().getResource(filename)));
		reader.setBounds(getProperty("AntennaPaneX") + (getProperty("AntennaPaneWidth") - getProperty("ReaderWidth")) / 2, getProperty("FramePadding"), getProperty("ReaderWidth"), getProperty("ReaderHeight"));
		return reader;
	}

	/**
	 * creates the context menu if it does not already exists
	 * 
	 * @return context menu
	 */
	private JPopupMenu getContextMenu() {
		if(contextMenu == null) {
			contextMenu = new JPopupMenu();
			
			// add new antenna item
			JMenuItem newAntennaContextMenuItem = new JMenuItem(guiText.getString("AddNewAntennaMenuItem"));
			newAntennaContextMenuItem.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					showAddAntennaDialog();
					contextMenu.setVisible(false);
				}
			});
			//contextMenu.add(newAntennaContextMenuItem);
			
			// add new tag item 
			JMenuItem newTagContextMenuItem = new JMenuItem(guiText.getString("AddNewTagMenuItem"));
			newTagContextMenuItem.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					showAddTagDialog();
					contextMenu.setVisible(false);
				}
			});
			contextMenu.add(newTagContextMenuItem);
		}
		return contextMenu;
	}
	
	/**
	 * shows the dialog menu to add a new antenna
	 */
	private void showAddAntennaDialog() {
		Point pos = new Point();
		pos.x = jLayeredPane.getLocationOnScreen().x + (jLayeredPane.getWidth() - getProperty("DialogWindowWidth")) / 2;
		pos.y = jLayeredPane.getLocationOnScreen().y + (jLayeredPane.getHeight() - getProperty("DialogWindowHeight")) / 2;
		
		if(newAntennaDialog == null) {
			newAntennaDialog = new JDialog(this, guiText.getString("AddNewAntennaDialogTitle"), true);
			newAntennaDialog.setSize(getProperty("DialogWindowWidth"), getProperty("DialogWindowHeight"));
			newAntennaDialog.setLayout(new BorderLayout());
			
			// input fields
			JLabel idLabel = new JLabel(guiText.getString("AntennaIdLabel") + ": ");
			final JTextField idField = new JTextField();
			JPanel inputFields = new JPanel();
			inputFields.setLayout(new GridLayout(2, 2));
			inputFields.add(idLabel);
			inputFields.add(idField);
			
			// cancel button
			JButton cancelButton = new JButton(guiText.getString("CancelButton"));
			cancelButton.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					newAntennaDialog.setVisible(false);
					idField.setText("");
				}
			});
			
			// add button
			JButton addButton = new JButton(guiText.getString("AddButton"));
			addButton.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					newAntennaDialog.setVisible(false);
					createNewAntenna(idField.getText());
					idField.setText("");
				}
			});
			
			// buttons panel
			JPanel buttons = new JPanel();
			buttons.add(addButton);
			buttons.add(cancelButton);
			
			newAntennaDialog.add(inputFields, BorderLayout.CENTER);
			newAntennaDialog.add(buttons, BorderLayout.SOUTH);
			newAntennaDialog.getRootPane().setDefaultButton(addButton);
		}
		newAntennaDialog.setLocation(pos);
		newAntennaDialog.setVisible(true);
	}
	
	/**
	 * shows the dialog menu to add a new tag
	 */
	private void showAddTagDialog() {
		// compute position of the dialog
		final Point pos = new Point();
		pos.x = jLayeredPane.getLocationOnScreen().x + (jLayeredPane.getWidth() - getProperty("DialogWindowWidth")) / 2;
		pos.y = jLayeredPane.getLocationOnScreen().y + (jLayeredPane.getHeight() - getProperty("DialogWindowHeight")) / 2;
		
		// set default id
		String id = props.getProperty("TagPrefix");
		for(int i = 0; i < 4 - new Integer(tags.size()).toString().length(); i++) {
			id += "0";
		}
		id += tags.size();
		if(tagIdField == null) {
			tagIdField = new JTextField();
		}
		tagIdField.setText(id);
		
		// create tag dialog if it does not already exists
		if(newTagDialog == null) {
			newTagDialog = new JDialog(this, guiText.getString("AddNewTagDialogTitle"), true);
			newTagDialog.setSize(getProperty("DialogWindowWidth"), getProperty("DialogWindowHeight"));
			newTagDialog.setLayout(new BorderLayout());
			
			// input fields panel
			JLabel epcLabel = new JLabel(guiText.getString("TagIdLabel") + ": ");
			JPanel inputFields = new JPanel();
			inputFields.setLayout(new GridLayout(2, 2));
			inputFields.add(epcLabel);
			inputFields.add(tagIdField);
			
			// cancel button
			JButton cancelButton = new JButton(guiText.getString("CancelButton"));
			cancelButton.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					newTagDialog.setVisible(false);
				}
			});
			
			// add button
			JButton addButton = new JButton(guiText.getString("AddButton"));
			addButton.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					newTagDialog.setVisible(false);
					createNewTag(tagIdField.getText());
				}
			});
			
			// buttons panel
			JPanel buttons = new JPanel();
			buttons.add(addButton);
			buttons.add(cancelButton);
			
			// compose all together
			newTagDialog.add(inputFields, BorderLayout.CENTER);
			newTagDialog.add(buttons, BorderLayout.SOUTH);
			newTagDialog.getRootPane().setDefaultButton(addButton);
		}
		newTagDialog.setLocation(pos);
		newTagDialog.setVisible(true);
	}

	/**
	 * creates a new antenna and adds it to the layered pane
	 * 
	 * @param name of the new antenna
	 * @param id of the new antenna
	 */
	private void createNewAntenna(String id) {
		if("".equals(id)) {
			try {
				id = controller.getReadPointNames()[antennas.size()];
			} catch (HardwareException ignored) { }
		}
		int x = (antennas.size() % getProperty("AntennasPerRow")) * (getProperty("AntennaWidth") + getProperty("HorizontalInterAntennaPadding")) + getProperty("AntennaPaneX");
		int y = (antennas.size() / getProperty("AntennasPerRow")) * (getProperty("AntennaHeight") + getProperty("VerticalInterAntennaPadding")) + (getProperty("AntennaPaneY"));
		Point pos = new Point(x, y);
		
		// antenna
		Antenna newAntenna = new Antenna(id, this);
		newAntenna.setPosition(pos);
		antennas.add(newAntenna);
		jLayeredPane.add(newAntenna, new Integer(0));
		
		// listeners
		getTranslationListener().add(newAntenna);
		newAntenna.addMouseListener(new AntennaMouseAdapter());
		
		//cable
		Cable newCable = new Cable(pos, this);
		jLayeredPane.add(newCable, new Integer(-1));
		jLayeredPane.repaint();
	}
	
	/**
	 * removes the antenna from the layered pane
	 * 
	 * @param antenna which will be removed
	 */
	public void removeAntenna(Antenna antenna, Reader reader) {
		antennas.remove(antenna);
		jLayeredPane.remove(antenna);
		jLayeredPane.repaint();
		
		MouseListener[] listeners = antenna.getMouseListeners();
		for (int i = 0; i < listeners.length; i++) {
			antenna.removeMouseListener(listeners[i]);
		}
	}
	
	/**
	 * removes the tag from the layered pane
	 * 
	 * @param tag which will be removed
	 */
	public void removeTag(Tag tag) {
		tags.remove(tag);
		jLayeredPane.remove(tag);
		jLayeredPane.repaint();
	}
	
	/**
	 * creates a new tag and adds it to the layered pane
	 * 
	 * @param name of the new tag
	 * @param epc of the new tag
	 */
	private void createNewTag(String id) {
		Point pos = new Point(getProperty("FramePadding"), tags.size() * (getProperty("TagHeight") + getProperty("InterTagPadding")) + getProperty("FramePadding"));
		createNewTag(id, pos);
	}
	
	/**
	 * creates a new tag and adds it to the layered pane
	 * 
	 * @param name of the new tag
	 * @param epc of the new tag
	 * @param pos position on the pane of the new tag
	 */
	private void createNewTag(String id, Point pos) {
		if(!"".equals(id)) {
			Tag newTag = new Tag(id, pos, this);
			tags.add(newTag);
			jLayeredPane.add(newTag, new Integer(1));
			jLayeredPane.repaint();
		}
	}

	/**
	 * returns the translation listener
	 * 
	 * @return translation listener
	 */
	public TranslationListener getTranslationListener() {
		return translationListener;
	}
	
	/**
	 * returns the gui text resource bundle
	 *
	 * @return gui text resource bundle
	 */
	public ResourceBundle getGuiText() {
		return guiText;
	}

	/**
	 * returns the simulator properties
	 * 
	 * @return properties
	 */
	public Properties getProperties() {
		return props;
	}
	
	/**
	 * adds an enter event to the simulator controller
	 * 
	 * @param antennaId 
	 * @param epc of the tag
	 */
	public void enterEvent(String readerId, String antennaId, String epc) {
		controller.add(antennaId, epc);
	}

	/**
	 * adds an exit event to the simulator controller
	 * 
	 * @param antennaId
	 * @param epc of the tag
	 */
	public void exitEvent(String readerId, String antennaId, String epc) {
		controller.remove(antennaId, epc);
	}
	
	/**
	 * gets a property value as integer
	 * 
	 * @param key of the property
	 * @return property as integer value
	 */
	public int getProperty(String key) {
		String error;
		String value = props.getProperty(key);
		if(value == null) {
			error =  "Value '" + key + "' not found in properties !";
		} else {
			try {
				return Integer.parseInt(value.trim());
			} catch (Exception e) {
				error = "Value '" + key + "' is not an integer !";
			}
		}
		throw new NumberFormatException(error);
	}

	/**
	 * sets active context menu
	 * 
	 * @param contextMenu which is active
	 */
	public void setActiveContextMenu(JPopupMenu contextMenu) {
		activeContextMenu = contextMenu;
	}
	
	/**
	 * hide active context menu
	 */
	public void hideActiveContextMenu() {
		if(activeContextMenu != null) {
			activeContextMenu.setVisible(false);
			activeContextMenu = null;
		}
	}
	
	/**
	 * hide active context menu and hide selection
	 */
	public void hideActiveContextMenuAndSelection() {
		hideActiveContextMenu();
		selection.unselect(tags);
	}
	
	/**
	 * Returns the management simulator menu.
	 * 
	 * @return The management simulator menu
	 */
	private JPopupMenu getMgmtSimMenu() {
		if (mgmtSimMenu == null) {
			mgmtSimMenu = new JPopupMenu();
			JMenuItem newMgmtContextMenuItem = new JMenuItem("Simulate an error");
			newMgmtContextMenuItem.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					mgmtSimMenu.setVisible(false);
					showMgmtSimDialog();
				}
			});
			mgmtSimMenu.add(newMgmtContextMenuItem);
			
			// add new tag item
			JMenuItem newTagContextMenuItem = new JMenuItem(guiText.getString("AddNewTagMenuItem"));
			newTagContextMenuItem.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					showAddTagDialog();
					mgmtSimMenu.setVisible(false);
				}
			});
			mgmtSimMenu.add(newTagContextMenuItem);
			
//			MenuElement[] elements = contextMenu.getSubElements();
//			for (int i = 0; i < elements.length; i++) {
//				mgmtSimMenu.add((JMenuItem) elements[i]);
//			}
		}
		return mgmtSimMenu;
	}
	
	/**
	 * Shows the management simulator dialog for the selected antenna.
	 */
	private void showMgmtSimDialog() {
		// compute position of the dialog
		final Point pos = new Point();
		pos.x = jLayeredPane.getLocationOnScreen().x + (jLayeredPane.getWidth() - getProperty("DialogWindowWidth")) / 2;
		pos.y = jLayeredPane.getLocationOnScreen().y + (jLayeredPane.getHeight() - getProperty("DialogWindowHeight")) / 2;
		
		String readPointName = selectedAntenna.getId();
		// create dialog if it does not already exists
		MgmtSimDialog mgmtSimDialog = mgmtSimDialogs.get(readPointName);
		if(mgmtSimDialog == null) {
			mgmtSimDialog = new MgmtSimDialog(readPointName, controller);
			mgmtSimDialogs.put(readPointName, mgmtSimDialog);
		}
		mgmtSimDialog.setLocation(pos);
		mgmtSimDialog.setModal(true);
		mgmtSimDialog.setVisible(true);
	}
	
	/**
	 * Mouse adapter for the antenna components.
	 */
	private class AntennaMouseAdapter extends MouseAdapter {
		
		/* (non-Javadoc)
		 * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseReleased(MouseEvent event) {
			if (event.getButton() == MouseEvent.BUTTON3) {
				selectedAntenna = (Antenna) event.getSource();
				Point posOnScreen = selectedAntenna.getLocationOnScreen();
				mgmtSimMenu.setLocation(posOnScreen.x + event.getX(), posOnScreen.y + event.getY());
				mgmtSimMenu.setVisible(true);
				contextMenu.setVisible(false);
			} else {
				contextMenu.setVisible(false);
				mgmtSimMenu.setVisible(false);
				if(event.getButton() == MouseEvent.BUTTON1) {
					selection.select(tags);
				}
			}
		}
	}
}
