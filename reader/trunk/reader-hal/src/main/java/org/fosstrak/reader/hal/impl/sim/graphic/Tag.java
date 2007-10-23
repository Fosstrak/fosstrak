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

package org.accada.reader.hal.impl.sim.graphic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import org.accada.reader.hal.HardwareException;
import org.accada.reader.hal.util.ByteBlock;
import org.accada.reader.hal.util.ResourceLocator;

/**
 * @author regli
 */
public class Tag extends JPanel implements Comparable {
	
   /** the serial version uid for serialization */
   private static final long serialVersionUID = 1L;
   /** the sim tag implementation */
   private org.accada.reader.hal.impl.sim.Tag tag; 
	/** the image icon of the rfid tag */ 
	private final Icon icon;
	/** the drag listener */
	private final DragListener dragListener;
	/** graphic simulator to which this tag belongs to */
	private final IGraphicSimulator simulator;
	/** context menu for single selection */
	private final JPopupMenu singleTagContextMenu;
	/** context menu for multiple selection */
	private final JPopupMenu multiTagsContextMenu;
	/** context menu for grouped tags selection */
	private JPopupMenu groupedTagsContextMenu;
	/** this antenna */
	private final Tag thisTag = this;
   /** the user memory field */
   private JTextField userMemoryField;

	/** the hash set with the selected tags */
	private TreeSet selectedTags = new TreeSet();
	/** the hash set with the grouped tags */
	private TreeSet groupedTags = new TreeSet();

	
	/**
	 * the constructor creates a new graphical representation of a rfid tag  
	 * 
	 * @param name of the new rfid tag
	 * @param epc of the new rfid tag
	 * @param pos on the pane of the new rfid tag
	 * @param translationListener which listen to the tranlation of the rfid tag
	 */
	public Tag(String epc, Point pos, final IGraphicSimulator simulator) {
		super();
		
      this.tag = new org.accada.reader.hal.impl.sim.Tag(epc);
		this.simulator = simulator;
		
		setBounds(new Rectangle(pos, new Dimension(simulator.getProperty("TagWidth"), simulator.getProperty("TagHeight"))));
		
		dragListener = new DragListener(this);
		addMouseListener(dragListener);
		addMouseMotionListener(dragListener);
		addComponentListener(simulator.getTranslationListener());
		
		String filename = simulator.getProperties().getString("TagImage");
      String defaultfilename = simulator.getProperties().getString("TagDefaultImage");
      URL fileurl = ResourceLocator.getURL(filename, defaultfilename, this.getClass());
      icon = new ImageIcon(fileurl);
		
		// initialize context menus
		singleTagContextMenu = new JPopupMenu();
		multiTagsContextMenu = new JPopupMenu();
		groupedTagsContextMenu = new JPopupMenu();

		// remove tag menu item
		JMenuItem removeTagContextMenuItem = new JMenuItem(simulator.getGuiText().getString("RemoveTagMenuItem"));
		removeTagContextMenuItem.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				showRemoveTagDialog(getLocationOnScreen().x + e.getPoint().x, getLocationOnScreen().y + e.getPoint().y);
				simulator.hideActiveContextMenu();
			}
		});
		singleTagContextMenu.add(removeTagContextMenuItem);
		
      // change user memory menu item
      JMenuItem changeMemoryContextMenuItem = new JMenuItem(simulator.getGuiText().getString("ChangeMemoryMenuItem"));
      changeMemoryContextMenuItem.addMouseListener(new MouseAdapter() {
         public void mouseReleased(MouseEvent e) {
            showChangeMemoryDialog(getLocationOnScreen().x + e.getPoint().x, getLocationOnScreen().y + e.getPoint().y);
            simulator.hideActiveContextMenu();
         }
      });
      singleTagContextMenu.add(changeMemoryContextMenuItem);
      
		// remove tags menu item
		JMenuItem removeTagsContextMenuItem = new JMenuItem(simulator.getGuiText().getString("RemoveTagsMenuItem"));
		removeTagsContextMenuItem.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				showRemoveTagsDialog(getLocationOnScreen().x + e.getPoint().x, getLocationOnScreen().y + e.getPoint().y);
				simulator.hideActiveContextMenu();
			}
		});
		multiTagsContextMenu.add(removeTagsContextMenuItem);
		
		// remove grouped tags menu item
		JMenuItem removeGroupedTagsContextMenuItem = new JMenuItem(simulator.getGuiText().getString("RemoveTagsMenuItem"));
		removeGroupedTagsContextMenuItem.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				showRemoveTagsDialog(getLocationOnScreen().x + e.getPoint().x, getLocationOnScreen().y + e.getPoint().y);
				simulator.hideActiveContextMenu();
			}
		});
		groupedTagsContextMenu.add(removeGroupedTagsContextMenuItem);

		// group tags menu item
		JMenuItem groupTagsContextMenuItem = new JMenuItem(simulator.getGuiText().getString("GroupTagsMenuItem"));
		groupTagsContextMenuItem.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				Iterator tagsIt = selectedTags.iterator();
				TreeSet tagsToGroup = (TreeSet)selectedTags.clone();
				while(tagsIt.hasNext()) {
					((Tag)tagsIt.next()).group(tagsToGroup);
				}
				simulator.hideActiveContextMenu();
			}
		});
		multiTagsContextMenu.add(groupTagsContextMenuItem);
		
		// ungroup tags menu item
		JMenuItem ungroupTagsContextMenuItem = new JMenuItem(simulator.getGuiText().getString("UngroupTagsMenuItem"));
		ungroupTagsContextMenuItem.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				Iterator tagsIt = ((TreeSet)selectedTags.clone()).iterator();
				while(tagsIt.hasNext()) {
					((Tag)tagsIt.next()).ungroup();
				}
				simulator.hideActiveContextMenu();
			}
		});
		groupedTagsContextMenu.add(ungroupTagsContextMenuItem);
		
		// rearrange tags menu item
		JMenuItem arrangeTagsContextMenuItem = new JMenuItem(simulator.getGuiText().getString("ArrangeTagsMenuItem"));
		arrangeTagsContextMenuItem.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				arrangeTags();
			}
		});
		multiTagsContextMenu.add(arrangeTagsContextMenuItem);
		
		// rearrange grouped tags menu item
		JMenuItem arrangeGroupedTagsContextMenuItem = new JMenuItem(simulator.getGuiText().getString("ArrangeTagsMenuItem"));
		arrangeGroupedTagsContextMenuItem.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				arrangeTags();
			}
		});
		groupedTagsContextMenu.add(arrangeGroupedTagsContextMenuItem);
		
		// add context menu listener
		addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					Point posOnScreen = getLocationOnScreen();
					if(selectedTags.size() == 1) {
						singleTagContextMenu.setLocation(posOnScreen.x + e.getX(), posOnScreen.y + e.getY());
						singleTagContextMenu.setVisible(true);
						simulator.setActiveContextMenu(singleTagContextMenu);
					} else {
						if(isGrouped() && getGroupMembers().size() == getSelectionMembers().size()) {
							groupedTagsContextMenu.setLocation(posOnScreen.x + e.getX(), posOnScreen.y + e.getY());
							groupedTagsContextMenu.setVisible(true);
							simulator.setActiveContextMenu(groupedTagsContextMenu);
						} else {
							multiTagsContextMenu.setLocation(posOnScreen.x + e.getX(), posOnScreen.y + e.getY());
							multiTagsContextMenu.setVisible(true);
							simulator.setActiveContextMenu(multiTagsContextMenu);
						}
					}
				}
			}

			public void mousePressed(MouseEvent e) {
				simulator.hideActiveContextMenu();
			}
		});
	}

	/**
	 * rearranges the selected tags
	 */
	private void arrangeTags() {
		// calculate position 
		Tag curTag;
		int x = 0;
		int y = 0;
		Iterator tagsIt = ((TreeSet)selectedTags.clone()).iterator();
		while(tagsIt.hasNext()) {
			curTag = (Tag)tagsIt.next();
			x += curTag.getX();
			y += curTag.getY();
		}
		x /= selectedTags.size();
		y = y / selectedTags.size() - (((selectedTags.size() - 1) * (simulator.getProperty("TagHeight") + simulator.getProperty("InterTagPadding"))) / 2);
		
		// arrange tags
		int counter = 0;
		tagsIt = ((TreeSet)selectedTags.clone()).iterator();
		while(tagsIt.hasNext()) {
			curTag = (Tag)tagsIt.next();
			curTag.setLocation(x, y + counter++ * (simulator.getProperty("TagHeight") + simulator.getProperty("InterTagPadding")));
		}
		simulator.hideActiveContextMenu();
	}
	
	/**
	 * groups the tag to the tags in the grouped tags hash map
	 * 
	 * @param groupedTags tags in the same group
	 */
	public void group(TreeSet groupedTags) {
		if(isGrouped()) this.groupedTags.remove(this);
		this.groupedTags = groupedTags;
	}
	
	/**
	 * ungroups the tag
	 */
	public void ungroup() {
		groupedTags.clear();
	}

	/**
	 * returns if this tag belongs to a group
	 * 
	 * @return true if the the tag is grouped otherwise returns false
	 */
	public boolean isGrouped() {
		return groupedTags.size() > 1;
	}
	
	/**
	 * shows the remove tag dialog window
	 * 
	 * @param x the horizontal position of the dialog window
	 * @param y the vertical position of the dialog window
	 */
	private void showRemoveTagDialog(int x, int y) {
		final JDialog removeTagDialog = new JDialog();
      removeTagDialog.setName(simulator.getGuiText().getString("RemoveTagDialogTitle"));
      removeTagDialog.setTitle(simulator.getGuiText().getString("RemoveTagDialogTitle"));
		removeTagDialog.setSize(simulator.getProperty("DialogWindowWidth"), simulator.getProperty("DialogWindowHeight"));
		removeTagDialog.setLayout(new BorderLayout());
		
		// label
		JLabel label = new JLabel(simulator.getGuiText().getString("RemoveTagLabel")
		   + " '" + tag.getTagID() + "' ?");
		label.setHorizontalAlignment(JLabel.CENTER);

		// cancel button
		JButton noButton = new JButton(simulator.getGuiText().getString("NoButton"));
		noButton.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				removeTagDialog.setVisible(false);
			}
		});
		
		// ok button
		JButton yesButton = new JButton(simulator.getGuiText().getString("YesButton"));
		yesButton.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				removeTagDialog.setVisible(false);
				simulator.removeTag(thisTag);
			}
		});
		
		// buttons panel
		JPanel buttons = new JPanel();
		buttons.add(yesButton);
		buttons.add(noButton);
		
		// add elements
		removeTagDialog.add(label, BorderLayout.CENTER);
		removeTagDialog.add(buttons, BorderLayout.SOUTH);
		removeTagDialog.getRootPane().setDefaultButton(yesButton);
		removeTagDialog.setLocation(x, y);
		removeTagDialog.setVisible(true);
	}
	
   /**
    * shows the change memory dialog window
    * 
    * @param x the horizontal position of the dialog window
    * @param y the vertical position of the dialog window
    */
   private void showChangeMemoryDialog(int x, int y) {
      final JDialog changeMemoryDialog = new JDialog();
      changeMemoryDialog.setName(simulator.getGuiText().getString("ChangeMemoryDialogTitle"));
      changeMemoryDialog.setTitle(simulator.getGuiText().getString("ChangeMemoryDialogTitle"));
      changeMemoryDialog.setSize(simulator.getProperty("DialogWindowWidth"), simulator.getProperty("DialogWindowHeight"));
      changeMemoryDialog.setLayout(new BorderLayout());
      
      if(userMemoryField == null) {
         userMemoryField = new JTextField();
      }
      byte[] data;
      try {
         data = tag.readData(3, 0, 0);
      } catch (HardwareException he) {
         data = new byte[] {};
      }
      userMemoryField.setText(ByteBlock.byteArrayToHexString(data));

      // user memory input field
      JLabel userMemoryLabel = new JLabel(simulator.getGuiText().getString(
         "ChangeUserMemoryLabel") + " :");
      JPanel inputFields = new JPanel();
      inputFields.setLayout(new GridLayout(2, 2));
      inputFields.add(userMemoryLabel);
      inputFields.add(userMemoryField);

      // cancel button
      JButton cancelButton = new JButton(simulator.getGuiText().getString("CancelButton"));
      cancelButton.addMouseListener(new MouseAdapter() {
         public void mouseReleased(MouseEvent e) {
            changeMemoryDialog.setVisible(false);
         }
      });
      
      // ok button
      JButton okButton = new JButton(simulator.getGuiText().getString("OkButton"));
      okButton.addMouseListener(new MouseAdapter() {
         public void mouseReleased(MouseEvent e) {
            changeMemoryDialog.setVisible(false);
            if (userMemoryField.getText().length() % 2 != 0) {
               userMemoryField.setText("0" + userMemoryField.getText());
            }
            try {
               tag.setData(ByteBlock.hexStringToByteArray(userMemoryField.getText()), 3);
            } catch (NumberFormatException nfe) {}
         }
      });
      
      // buttons panel
      JPanel buttons = new JPanel();
      buttons.add(okButton);
      buttons.add(cancelButton);
      
      // add elements
      changeMemoryDialog.add(inputFields, BorderLayout.CENTER);
      changeMemoryDialog.add(buttons, BorderLayout.SOUTH);
      changeMemoryDialog.getRootPane().setDefaultButton(okButton);
      changeMemoryDialog.setLocation(x, y);
      changeMemoryDialog.setVisible(true);
   }
   
	/**
	 * shows the remove tags dialog window
	 * 
	 * @param x the horizontal position of the dialog window
	 * @param y the vertical position of the dialog window
	 */
	private void showRemoveTagsDialog(int x, int y) {
		final JDialog removeTagsDialog = new JDialog();
      removeTagsDialog.setName(simulator.getGuiText().getString("RemoveTagsDialogTitle"));
      removeTagsDialog.setTitle(simulator.getGuiText().getString("RemoveTagsDialogTitle"));
		removeTagsDialog.setSize(simulator.getProperty("DialogWindowWidth"), simulator.getProperty("DialogWindowHeight"));
		removeTagsDialog.setLayout(new BorderLayout());
		
		// label
		JLabel label = new JLabel(simulator.getGuiText().getString("RemoveTagsLabel"));
		label.setHorizontalAlignment(JLabel.CENTER);

		// cancel button
		JButton noButton = new JButton(simulator.getGuiText().getString("NoButton"));
		noButton.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				removeTagsDialog.setVisible(false);
			}
		});
		
		// ok button
		JButton yesButton = new JButton(simulator.getGuiText().getString("YesButton"));
		yesButton.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				removeTagsDialog.setVisible(false);
				Iterator tagsIt = selectedTags.iterator();
				while(tagsIt.hasNext()) {
					simulator.removeTag((Tag)tagsIt.next());
				}
			}
		});
		
		// buttons panel
		JPanel buttons = new JPanel();
		buttons.add(yesButton);
		buttons.add(noButton);
		
		// add elements
		removeTagsDialog.add(label, BorderLayout.CENTER);
		removeTagsDialog.add(buttons, BorderLayout.SOUTH);
		removeTagsDialog.getRootPane().setDefaultButton(yesButton);
		removeTagsDialog.setLocation(x, y);
		removeTagsDialog.setVisible(true);
	}
	
	/**
	 * paints the rfid tag
	 * 
	 * @param the graphic representation of the component
	 */
	protected void paintComponent(Graphics g) {
		icon.paintIcon(this, g, (simulator.getProperty("TagWidth") - icon.getIconWidth()) / 2, 0);
		g.setColor(isSelected() ? Color.RED : Color.BLACK);
		g.setFont(new Font(simulator.getProperties().getString("TagLabelFont"), 0, simulator.getProperty("TagLabelSize")));
		g.drawString(tag.getTagID(), (getWidth() / 2) - (tag.getTagID().length() / 2) * 7 - (tag.getTagID().length() % 2 == 1 ? 3 : 0), simulator.getProperty("TagHeight"));
	}
	
	/**
	 * set this tag as selected and sets the selected tags
	 */
	public void select(TreeSet selectedTags) {
		this.selectedTags = selectedTags;
		paintComponent(getGraphics());
	}
	
	/**
	 * set this tag as selected
	 */
	public void select() {
		this.selectedTags.add(this);
		paintComponent(getGraphics());
	}
	
	/**
	 * set this tag as unselected
	 */
	public void unselect() {
		selectedTags = new TreeSet();
		paintComponent(getGraphics());
	}
	
	/**
	 * returns if this tag is selected or not
	 * 
	 *  @return isSelected
	 */
	public boolean isSelected() {
		return !selectedTags.isEmpty();
	}
	
	/**
	 * returns a hash set with all tags in the current selection
	 */
	public TreeSet getSelectionMembers() {
		return selectedTags;
	}
	
	/**
	 * returns a hash set with all tags which are in the same group like this tag
	 */
	public TreeSet getGroupMembers() {
		return groupedTags;
	}
	
   /**
    * returns the electronic product code of the rfid tag
    * 
    * @return epc
    */
   public String getId() {
      return tag.getTagID();
   }
   
   /**
    * returns the sim.Tag
    * 
    * @return sim.Tag
    */
   public org.accada.reader.hal.impl.sim.Tag getSimTag() {
      return tag;
   }
   
	/**
	 * returns the drag listener
	 * 
	 * @return drag listener
	 */
	public DragListener getDragListener() {
		return dragListener;
	}
	
	/**
	 * returns a string representation of the rfid tag
	 * 
	 * @return string representation
	 */
	public String toString() {
		return tag.getTagID();
	}
	
	/**
	 * returns the graphic simulator this tag belongs to
	 * 
	 * @ return graphic simulator
	 */
	public IGraphicSimulator getSimulator() {
		return simulator;
	}

	public int compareTo(Object obj) {
		if(obj instanceof Tag) {
			return tag.getTagID().compareTo(((Tag)obj).getId());
		} else {
			throw new ClassCastException("Can not compare Tag with " + obj.getClass());
		}
	}
}