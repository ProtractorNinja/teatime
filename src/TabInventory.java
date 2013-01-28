/*
 * This class builds the inventory editor!
 */

import javax.swing.JPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.util.Iterator;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.jar.*;
import java.util.zip.*;
import java.util.Enumeration;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.io.ByteArrayOutputStream;
import java.awt.image.PixelGrabber;
import java.util.HashMap;
import java.util.Iterator;
import java.awt.event.KeyEvent;
import java.util.Map;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.MouseWheelEvent;

public class TabInventory extends JPanel {

	private TeaTime parent;
	private InvUtils tea;
	private JPanel managerPanel;
 	private JLabel materialName;
 	private JLabel materialNameTemp;
 	private JPanel invPanel;
 	  private SlotInv[] invSlots;
 	  private JPanel armorPanel;
 	  private JPanel toolsPanel;
 	  	private ToolButton fullButton;
 	  	private ToolButton trashButton;
 	  	private ToolButton repairButton;
 	  private JPanel slotsPanel;
 	    private JPanel itemsPanel;
 	  	private JPanel quickPanel;
 	  private AdvancedPanel advEditor;
 	private JTabbedPane materials;
 		private JPanel searchPanel;
 	private MouseAdapter listener;

 	// Transfers
 	public static Slot tempSlot;
	public static Slot sourceSlot;
	public static Slot destSlot;
	public static int tTransferMode;
	public static boolean dropSuccess;
	public static int tTransferButton;

	// Tool types
	public static int FULL   = 24;
	public static int REPAIR = 25;
	public static int TRASH  = 26;

	public TabInventory(TeaTime tt) {
 		super();
 		System.out.print("Initializing Tea Inventory Resources... ");
 		parent = tt;
 		setFocusable(true);
 		addKeyListener(new TeaKey());
 		add(Box.createRigidArea(new Dimension(0,5)));
 		InvUtils.init();
 		System.out.println("done.");

		System.out.print("Building inventory area... ");
		//Building the upper area
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		materialName = new JLabel("");

 		listener = new InvMouse(materialName, this);

 		managerPanel = new JPanel(new BorderLayout());
 		add(managerPanel);

 		invPanel = new JPanel();
 		invSlots = new SlotInv[105];
 		invPanel.setLayout(new BoxLayout(invPanel, BoxLayout.X_AXIS));

 		armorPanel = buildArmorSlots(listener);
 		slotsPanel = buildInventorySlots(listener);
 		toolsPanel = buildToolSlots(listener);

		invPanel.add(Box.createRigidArea(new Dimension(5,0)));
 		invPanel.add(armorPanel);
 		invPanel.add(Box.createRigidArea(new Dimension(6,0)));
 		invPanel.add(slotsPanel);
 		invPanel.add(Box.createRigidArea(new Dimension(6,0)));
 		invPanel.add(toolsPanel);
 		invPanel.add(Box.createRigidArea(new Dimension(5,0)));

 		managerPanel.add(invPanel, BorderLayout.CENTER);
 		materialName.setPreferredSize(new Dimension(invPanel.getPreferredSize().width, 60));
 		materialName.setFont(new Font("Default", Font.BOLD, 18));
 		materialName.setVerticalAlignment(JLabel.CENTER);
		materialName.setHorizontalAlignment(JLabel.CENTER);
 		managerPanel.add(materialName, BorderLayout.SOUTH);

 		advEditor = new AdvancedPanel();
 		advEditor.setVisible(false);

 		System.out.println("done.");

		System.out.print("Building material slots...");
		materials = buildMaterialSlots(listener);
		add(materials);
		add(Box.createRigidArea(new Dimension(0,30)));
		System.out.println("done.");
 	}

 	public void grabKeyFocus() {
 		requestFocusInWindow();
 	}

	/**
	 * Get a list of all the inventory slots
	 *
	 * @return inventory slot list
	 */
 	public SlotInv[] getinvSlots() {
 		return invSlots;
 	}

 	/**
 	 * Adds z inventory buttons to JPanel p, from n, with the adapter l, transfer mode m, and importable b.
 	 */
 	public void addinvSlots(JPanel p, int n, int z, MouseAdapter l) {
 		for(int i=n; i<n+z; i++) {
 			SlotInv slot = new SlotInv(i, l);
 			p.add(slot);
 			invSlots[i] = slot;
 		}
 	}

 	/**
 	 * Builds the armor panel.
 	 */
 	public JPanel buildArmorSlots(MouseAdapter l) {
 		JPanel ap = new JPanel();
 		ap.setLayout(new BoxLayout(ap, BoxLayout.Y_AXIS));
 		ap.add(Box.createVerticalGlue());

 		SlotInv helmetSlot = new SlotInv(103, l, InvUtils.grabItemIcon(15,0,2,"items.png"));
 		ap.add(helmetSlot);
 		invSlots[103] = helmetSlot;
 		ap.add(Box.createRigidArea(new Dimension(0,2)));

 		SlotInv armorSlot = new SlotInv(102, l, InvUtils.grabItemIcon(15,1,2,"items.png"));
 		ap.add(armorSlot);
 		invSlots[102] = armorSlot;
 		ap.add(Box.createRigidArea(new Dimension(0,2)));

 		SlotInv legSlot = new SlotInv(101, l,InvUtils.grabItemIcon(15,2,2,"items.png"));
 		ap.add(legSlot);
 		invSlots[101] = legSlot;
 		ap.add(Box.createRigidArea(new Dimension(0,2)));

 		SlotInv bootSlot = new SlotInv(100, l, InvUtils.grabItemIcon(15,3,2,"items.png"));
 		ap.add(bootSlot);
 		invSlots[100] = bootSlot;

 		ap.add(Box.createVerticalGlue());
 		return ap;
 	}

 	/**
 	 * Builds the inventory slots.
 	 */
 	public JPanel buildInventorySlots(MouseAdapter l) {
 		JPanel sp = new JPanel();
 		sp.setLayout(new BoxLayout(sp, BoxLayout.Y_AXIS));
 		//Normal slots
 		JPanel ip = new JPanel(new GridLayout(3, 9, 1, 1));
 		addinvSlots(ip, 9, 27, l);
 		sp.add(ip);
 		sp.add(Box.createRigidArea(new Dimension(0,7)));
 		//Quick slots
 		JPanel qp = new JPanel(new GridLayout(1, 9, 1, 1));
 		addinvSlots(qp, 0, 9, l);
 		sp.add(qp);
 		return sp;
 	}

 	/*
 	 * Builds the tool slots/buttons.
 	 */
 	public JPanel buildToolSlots(MouseAdapter l) {
 		JPanel tp = new JPanel();
 		tp.setLayout(new BoxLayout(tp, BoxLayout.Y_AXIS));
 		repairButton = new ToolButton("Repair", REPAIR, l);
 		fullButton = new ToolButton("Full", FULL, l);
 		trashButton = new ToolButton("Trash", TRASH, l);
 		tp.add(fullButton);
 		tp.add(Box.createVerticalGlue());
 		tp.add(repairButton);
 		tp.add(Box.createVerticalGlue());
 		tp.add(Box.createRigidArea(new Dimension(0,1)));
 		tp.add(trashButton);
 		return tp;
 	}

 	/**
 	 * Builds the material slots.
 	 */
 	public JTabbedPane buildMaterialSlots(MouseAdapter l) {
 		JPanel pTemp;
 		JPanel tempItemLine;
 		String[] aTemp;
 		int totalSlots;
 		int tempid;
 		int numIDs;
 		Item aItem = new Item();
 		JTabbedPane m = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
 		m.setFocusable(false);
 		Iterator<Map.Entry<Integer, String[]>> iList = InvUtils.getGroupsIter();
 		while(iList.hasNext()) {
 			aTemp = (String[])iList.next().getValue();
			pTemp = new JPanel();
 			m.addTab(aTemp[InvUtils.GROUP_NAME], InvUtils.grabItemIcon(aTemp[InvUtils.GROUP_ICON], 1), pTemp);
 			aTemp = Arrays.copyOfRange(aTemp, InvUtils.GROUP_ARRAY_START, aTemp.length);

			loadItems(pTemp, aTemp);
 		}

 		return m;
 	}

 	public void loadItems(JPanel panel, String[] iArray) {
 		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
 		Item aItem = new Item();
 		JPanel tPanel;
 		int totalSlots = iArray.length;
 		totalSlots = totalSlots + (12 - totalSlots % 12);
 		if(totalSlots > 36) totalSlots = 36;
		int tempid = 0;
		JPanel tempItemLine;
		panel.add(Box.createRigidArea(new Dimension(0,6)));
		for(int j=0; j<totalSlots/12; j++) {
			if(j > 0) panel.add(Box.createRigidArea(new Dimension(0,2)));
			tempItemLine = new JPanel();
			tempItemLine.setLayout(new BoxLayout(tempItemLine, BoxLayout.X_AXIS));
			panel.add(tempItemLine);
			tempItemLine.add(Box.createHorizontalGlue());
			for(int k=0; k<12; k++) {
				if(k > 0) tempItemLine.add(Box.createRigidArea(new Dimension(3,0)));
				if(tempid < iArray.length){
					String iDat = iArray[tempid];
					if(iDat.contains("d0")) iDat = iDat.replace("d0", "");
					try {
						aItem = new Item(InvUtils.getItemData(iDat));
					} catch (Exception e) {
						/*JOptionPane.showMessageDialog(null, "The entry:\n" + iDat + "\nWas not found in the item list. Tea Time will now exit.", "Fatal Error!", JOptionPane.ERROR_MESSAGE);
						System.exit(-1);*/
						try {
							int tempID = Integer.parseInt(iDat);
							aItem = new Item();
							aItem.setID(tempID);
							aItem.setIconToNull();
						} catch (Exception ie) {
							System.out.println("Somehting isn't right here.");
						}
					}
					tempItemLine.add(new SlotItem(aItem, listener));
				}
				else tempItemLine.add(new SlotItem());
				tempid++;
			}
			tempItemLine.add(Box.createHorizontalGlue());
			panel.add(Box.createRigidArea(new Dimension(0,2)));
		}
		panel.add(Box.createRigidArea(new Dimension(0,2)));
 	}

 	/*
 	 * Does something to the WHOLE inventory!
 	 */
 	public void doInventory(int mode) {
 		for(int i=0; i<invSlots.length; i++) {
 			if(invSlots[i] != null && invSlots[i].hasItem()) {
 				invSlots[i].doAction(mode);
 			}
 		}
 		repaint();
    	revalidate();
 	}

 	/*
 	 * Apply an item to the whole inventory, sans armor slots.
 	 */
 	public void fillInventory(Item itm) {
 	 	fillInvRange(itm, 0, 35);
 	}

 	/*
 	 * Apply an item to a selection of inventory slots.
 	 */
 	public void fillInvRange(Item itm, int start, int stop) {
 		for(int i=start; i<=stop; i++) {
 			if(invSlots[i] != null && !invSlots[i].hasItem()) {
 				invSlots[i].setItem((Item)itm.clone());
 			}
 		}
 		repaint();
 		revalidate();
 	}

 	public void clearItem(Item itm) {
 		for(int i=0; i<invSlots.length; i++) {
 			if(invSlots[i] != null && invSlots[i].hasItem() && invSlots[i].getItem().getID() == itm.getID()) {
 				invSlots[i].removeItem();
 			}
 		}
 	}

 	public void clearInvSlots() {
 		clearInvRange(0,103);
 	}

 	public void clearInvRange(int start, int stop) {
 		for(int i=start; i<=stop; i++) {
 			if(invSlots[i].hasItem() && invSlots[i].hasItem()) {
 				invSlots[i].removeItem();
 			}
 		}
 	}

	/*
	 * Loads inventory data and adds it to the inventory slots from a filepath, which must be a .dat file.
	 */
 	public void loadInventoryData(String filepath) {
 		try {
 			Tag cw = Tag.readFrom(new FileInputStream(filepath));
 			TeaUtils.setCurrentWorldPath(filepath);
 			loadInventoryData(cw);
 		} catch (Exception e) {
 			TeaUtils.buildExceptionDialog(e);
 		}
 	}

 	public void loadInventoryData(Tag cw) {
		TeaUtils.setCurrentWorld(cw);
		Tag test = cw.findTagByName("Inventory");
		loadInvTag(test);
 	}

 	public void loadInvTag(Tag tInv) {
 		try {
 			clearInventory();
 			Item newItem;
 			Tag itemTag;
 			int slt;
			Tag[] subtags = (Tag[])tInv.getValue();
			for(int i=0; i<subtags.length; i++) {
				itemTag = subtags[i];
				newItem = InvUtils.getItemFromTag(itemTag);
				slt = ((Byte)itemTag.findTagByName("Slot").getValue()).intValue();
				invSlots[slt].setItem(newItem);
			}
			repaint();
 			revalidate();
		} catch (Exception e) {
			TeaUtils.buildExceptionDialog(e);
		}
 	}

	/**
	 * Builds inventory data into an Inventory tag for export.
	 */
 	public Tag buildInventoryData() {
 		int fullSlots = getFullinvSlots();
 		Tag invTag;
 		Tag[] tempInvTags;
 		Tag[] invtags = new Tag[fullSlots];
 		int tagSpot = 0;
		for(int i=0; i<invSlots.length; i++) {
			if(invSlots[i]!=null && invSlots[i].hasItem()) {
				int b = new Integer(""+invSlots[i].getItem().getCount()).intValue();
				if(b > 255) b = 255;
				if(b >= 128) b -= 256;
				Byte bb = new Byte(""+b);
				tempInvTags = new Tag[5];
				tempInvTags[0] = new Tag(Tag.Type.TAG_Short, "id", new Short(""+invSlots[i].getItem().getID()));
				tempInvTags[1] = new Tag(Tag.Type.TAG_Short, "Damage", new Short(""+invSlots[i].getItem().getDamage()));
				tempInvTags[2] = new Tag(Tag.Type.TAG_Byte, "Count", new Byte(bb));
				tempInvTags[3] = new Tag(Tag.Type.TAG_Byte, "Slot", new Byte(""+invSlots[i].getSlot()));
				tempInvTags[4] = new Tag(Tag.Type.TAG_End, null, null);
				invtags[tagSpot] = new Tag(Tag.Type.TAG_Compound, null, tempInvTags);
				tagSpot++;
			}
		}
		if(invtags.length > 0) {
			invTag = new Tag(Tag.Type.TAG_List, "Inventory", invtags);
		} else {
			invTag = new Tag(Tag.Type.TAG_List, "Inventory", Tag.Type.TAG_Byte);
		}
		return invTag;
 	}

	/*
	 * Returns the total number of in-use inventory slots.
	 */
	public int getFullinvSlots() {
		int fs = 0;
		for(int i=0; i<invSlots.length; i++) {
			if(invSlots[i]!=null && invSlots[i].hasItem()) fs++;
		}
		return fs;
	}

	/*
	 * Completely empties the inventory.
	 */
 	public void clearInventory() {
 		TeaUtils.setCurrentWorld(null);
 		TeaUtils.setCurrentWorldPath("");
 		for(int i=0; i<invSlots.length; i++) {
 			if(invSlots[i]!= null) {
 				invSlots[i].removeItem();
 			}
 		}
 		repaint();
 		revalidate();
 	}

 	/*
 	 * Hides or shows the advanced panel.
 	 */
 	public void toggleAdvancedPanel() {
 		if(advEditor.isVisible()) {
 			killAdvancedPanel();
 			grabKeyFocus();
 		}
 		else {
 			managerPanel.add(advEditor, BorderLayout.EAST);
 			advEditor.setVisible(true);
 			parent.pack();
 		}
 	}

 	public void killAdvancedPanel() {
 		managerPanel.remove(advEditor);
 		if(advEditor.hasFullSlot()) advEditor.removeSlot();
 		advEditor.setVisible(false);
 		parent.pack();
 	}

 	private static class InvUtils {
		public static final int ITEM_ID = 0;
		public static final int ITEM_NAME = 1;
		public static final int ITEM_LOCATION = 2;
		public static final int ITEM_COORDINATES = 3;
		public static final int ITEM_SPECIAL_FIELD = 4;
		public static final int GROUP_NAME = 0;
		public static final int GROUP_ICON = 1;
		public static final int GROUP_ARRAY_START = 2;
		public static final int LANG_SHORT_NAME = 0;
		public static final int LANG_LONG_NAME = 1;

		//public static ArrayList<ArrayList<String>> groupsList;
		private static HashMap<String, String[]> hItems;
		private static HashMap<Integer, String[]> hGroups;
		private static HashMap<String, String[]> hLang;
		private static String langItems;
		private static String listItems;
		private static String newline = System.getProperty("line.separator");
		public static int totalItems;

		public static TileGrabber tGrabber;
		public static TileGrabber iGrabber;
		public static TileGrabber sGrabber;
		public static TileGrabber nGrabber;

		public static ImageIcon nullIcon = new ImageIcon(TeaUtils.class.getResource("/lib/null.png"));
		private static JarFile jarFile;
		private static JarEntry entry;

		public static void init() {
			try {
				jarFile = new JarFile(TeaUtils.getMCJarPath());
				entry = new JarEntry("terrain.png");
				tGrabber = new TileGrabber(ImageIO.read(jarFile.getInputStream(entry)), 16, 16);
				entry = new JarEntry("gui/items.png");
				iGrabber = new TileGrabber(ImageIO.read(jarFile.getInputStream(entry)), 16, 16);
				sGrabber = new TileGrabber("/lib/special.png", 16, 16);
				nGrabber = new TileGrabber("/lib/nullicon.png", 15, 15);
			} catch (Exception e) {
				try {
					tGrabber = new TileGrabber("/lib/terrain.png", 16, 16);
					iGrabber = new TileGrabber("/lib/items.png", 16, 16);
					sGrabber = new TileGrabber("/lib/special.png", 16, 16);
					nGrabber = new TileGrabber("/lib/nullicon.png", 15, 15);
				} catch (Exception ie) {
					TeaUtils.buildExceptionDialog(ie);
				}
			}
			getItemNames();
			readItemsList();
		}

		public static HashMap<String, String[]> getItemsTable() {
			return hItems;
		}

		public static HashMap<Integer, String[]> getGroupsTable() {
			return hGroups;
		}

		public static int getTotalItems() {
			return totalItems;
		}

		public static Iterator<Map.Entry<String, String[]>> getItemsIter() {
			return hItems.entrySet().iterator();
		}

		public static Iterator<Map.Entry<Integer, String[]>> getGroupsIter() {
			return hGroups.entrySet().iterator();
		}

		public static String[] getItemData(String id) {
			return (String[])hItems.get(id);
		}

		public static ImageIcon getNullIcon(int s) {
			return nGrabber.grabTile(0,0,s);
		}

		/*
		 * Gets the name of an item, from the id.
		 * Nice and speedy!
		 */
		public static String getItemName(int tID) {
			String id = "" + tID;
			String fName = "Unknown ID: " + tID;

			if(!hItems.containsKey(id)) {
				return fName;
			}
			String sName = ((String[])hItems.get(id))[ITEM_NAME];
			return getItemName(sName);
		}

		/*
		 * Gets the name of an item, from the given string.
		 * Nice and speedy!
		 */
		public static String getItemName(String tName) {
			String fName = tName;

			if(tName.indexOf("\"") >= 0)
				fName = tName.replace("\"", "").replace("_", " ");
			else if(hLang.containsKey(tName))
				fName = ((String[])hLang.get(tName))[LANG_LONG_NAME];
			return fName;
		}

		/*
		 * Loads the item names from en_us.lang into a hash table.
		 */
	 	public static void getItemNames() {
		 	String[] aItems = new String[0];
		 	String[] aName;
		 	String lList = "";
		 	BufferedReader reader = null;
		 	try {
		 		jarFile = new JarFile(TeaUtils.getMCJarPath());
		 		entry = new JarEntry("lang/en_US.lang");
		 		reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(jarFile.getInputStream(entry))));
		 	} catch (Exception e) {
		 		reader = new BufferedReader(new InputStreamReader(InvUtils.class.getResourceAsStream("/lib/en_us.lang")));
		 	}

		 	if(reader != null) {
		 		try {
			 		String toRead;
			 		while((toRead = reader.readLine()) != null) {
			 			if((toRead.startsWith("item") || toRead.startsWith("tile")) && !toRead.contains("desc"))
							lList += toRead.replace(".name", "") + "\n";
			 		}
				} catch (Exception rce) {
			 		TeaUtils.buildExceptionDialog(rce);
			 	}
			 	aItems = lList.split("\n");
		 	}

		 	hLang = new HashMap<String, String[]>(aItems.length);
		 	for(String i : aItems) {
		 		aName = i.split("=");
		 		hLang.put(aName[0], aName);
		 	}
	 	}

		/*
		 * Reads the items.txt file and makes a hash table of data.
		 */
		public static void readItemsList() {
			String itemslist = "";
			String groupslist = "";
			String[] gDat;
			String gName;
			String[] aItem;
			int tempid;
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(InvUtils.class.getResourceAsStream("/lib/items.txt")));
				String toAdd;
				String temp;
				int itemsnum = 0;
				int groups = 0;
				while((toAdd = reader.readLine()) != null) {
					if(!toAdd.startsWith("#") && !toAdd.startsWith(":") && toAdd.trim().length() != 0) {
						toAdd = toAdd.replaceAll("[\\s]+", " ").trim();
						if(toAdd.matches("^[\\d].*")) {
							itemsnum++;
							itemslist += toAdd.trim() + "\n";
						}
						else if(toAdd.matches("~.*")) {
							groups++;
							groupslist += toAdd.substring(2) + "\n";
						}
					}
				}
			} catch (Exception e) {
				TeaUtils.buildExceptionDialog(e);
			}

			// Right, so now we've got a (string) list of items and a (string) list of groups. Let's turn the groups list into
			// a nice, organized hashtable.
			String[] grps = groupslist.split("\n");
			String g;
			hGroups = new HashMap<Integer, String[]>(grps.length);
			for(int i=0; i<grps.length; i++) {
				g = grps[i];
				g = g.replace(" ", ",");
				gDat = g.split(",");
				gDat = testRange(gDat);
				hGroups.put(new Integer((i+1)*1), gDat);
			}

			// Now we have a hash table! (name) { icon, ids... }
			// We need to put the item list into a table, now.
			String[] aItems = itemslist.split("\n");
			hItems = new HashMap<String, String[]>(aItems.length);
			Item tItem;
			for(String i : aItems) {
				aItem = i.split(" ");
				if(aItem.length < 4) {
					JOptionPane.showMessageDialog(null, "The line:\n" + i + "\nContains too few arguments. Tea Time will now exit.", "Fatal Error", JOptionPane.ERROR_MESSAGE);
					System.exit(-1);
				}
				try {
					tItem = new Item(aItem);
				} catch (Exception ie) {
					JOptionPane.showMessageDialog(null, "The line:\n" + i + "\nContains incorrect item data syntax. Tea Time will now exit.", "Fatal Error", JOptionPane.ERROR_MESSAGE);
					System.exit(-1);
				}
				if(!hItems.containsKey(aItem[0])) {
					hItems.put(aItem[0], aItem);
				}
				else {
					hItems.put(aItem[0] + "d" + aItem[InvUtils.ITEM_SPECIAL_FIELD], aItem);
				}
			}
		}

		/*
		 * Tests an array for a range of item values, in the syntax 34d5-8 or something, and splits it into multiple items.
		 */
		public static String[] testRange(String[] array) {
			int loc = -1;
			for(int i=0; i<array.length; i++) {
				if(array[i].contains("-")) {
					loc = i;
					break;
				}
			}
			if(loc == -1) return array;
			String[] dat = array[loc].replace("d", "-").split("-");
			int start = Integer.parseInt(dat[1]); //0
			int end = Integer.parseInt(dat[2]); //7
			String id = dat[0];
			//System.out.println(end-start + array.length);
			String[] fArray = new String[end - start + array.length]; //11; 2, 17, 35.0, 35.1, 2,3,4,5,6,7,7
			//String[] nArray = new String(end - start + 1);
			//for(int j=0; j<nArray.length; j++)
			int fix = 0;
			for(int j=0; j<array.length; j++) {
				if(j!=loc) fArray[fix] = array[j];
				else {
					for(int k=start; k<=end; k++) {
						fArray[fix] = id + "d" + k;
						if(k<end)fix++;
					}
				}
				fix++;
			}
			return testRange(fArray);
		}

		/*
		 * Gets a new Item object from an array of item info tags.
		 */
	 	public static Item getItemFromTag(Tag itemTag) {
	 		Item item;
	 		int id, dmg, cnt;
	 		Object idVal = itemTag.findTagByName("id").getValue();
	 		Object dmgVal = itemTag.findTagByName("Damage").getValue();
	 		Object cntVal = itemTag.findTagByName("Count").getValue();
	 		id = ((Short)idVal).intValue();
	 		dmg = ((Short)dmgVal).intValue();
	 		cnt = ((Byte)cntVal).intValue();
	 		if(cnt < 0) cnt += 256;
	 		item = buildItem(id, dmg, cnt, false);
	 		if(item.hasSpecialDamage() && dmg > 0) {
	 			item = buildItem(id, dmg, cnt, true);
	 		}
	 		return item;
	 	}

	 	public static Item buildItem(int i, int d, int c, boolean s) {
	 		if(c > 255) c = 255;
	 		if(c >= 128) c -= 256;
	 		if(c < 0) c += 256;
	 		Item nItem;
	 		try {
				if(!s || d == 0) nItem = new Item(getItemData(""+i)); // To account for the thing that happens when loading the list
		 		else nItem = new Item(getItemData(""+i+"d"+d));
	 		} catch (Exception e) {
	 			nItem = new Item(new String[]{""+i, "Unknown ID: " + i, "null", "0,0"});
	 		}
		 	nItem.setCount(c);
		 	nItem.setDamage(d);
		 	return nItem;
		}

	 	/*
	 	 * Returns an imageIcon from a file, with coords, and resized.
	 	 */
		public static ImageIcon grabItemIcon(String id, int scale) {
			try {
				String[] data = InvUtils.getItemData(id);
		 		String[] coords = data[InvUtils.ITEM_COORDINATES].split(",");
		 		int xItem = Integer.parseInt(coords[0]);
		 		int yItem = Integer.parseInt(coords[1]);
		 		String location = data[InvUtils.ITEM_LOCATION];
		 		return grabItemIcon(xItem, yItem, scale, location);
			} catch (Exception e) {
				return nGrabber.grabTile(0,0,scale);
			}
		}

		/*
		 * Grabs an item icon off an integer id, using the below method.
		 */
		public static ImageIcon grabItemIcon(int id, int scale) {
			return grabItemIcon("" + id, scale);
		}

		/*
		 * Grabs an item icon from either the main tile grabbers or another if the location isn't one of the main 3.
		 */
		public static ImageIcon grabItemIcon(int x, int y, int scale, String location) {
			try {
				if(location.equals("terrain.png"))      return tGrabber.grabTile(x,y,scale);
		 		else if(location.equals("items.png"))   return iGrabber.grabTile(x,y,scale);
		 		else if(location.equals("special.png")) return sGrabber.grabTile(x,y,scale);
		 		else {
		 			if(location == "null") return nGrabber.grabTile(0,0,scale);
		 			else {
		 				TileGrabber grabber = new TileGrabber("/lib/" + location, 16, 16);
		 				return grabber.grabTile(x,y,scale);
		 			}
		 		}
			} catch (Exception e) {
				return nGrabber.grabTile(0,0,scale);
			}
		}

	}

	/*
	 * Special mouseadapter for the inventory panel.
	 */
	private class InvMouse extends MouseAdapter {

		private JLabel nameLabel;
		private Slot tempSlot;
		private TabInventory parent;

		public InvMouse(JLabel l, TabInventory pt) {
			super();
			nameLabel = l;
			parent = pt;
		}

		MouseEvent firstMouseEvent = null;

		/*
		 * On a press, record the event to test for dragging.
		 */
		public void mousePressed(MouseEvent e) {
		    firstMouseEvent = e;
		    e.consume();
		}

		/*
		 * On click, check for a tool button being clicked or the advanced editor highlighting being toggled.
		 */
		public void mouseClicked(MouseEvent e) {
			JComponent comp = (JComponent)e.getSource();
			if(comp instanceof ToolButton) {
		    	parent.doInventory(((ToolButton)e.getComponent()).getTransferMode());
		    }
		    if(comp instanceof SlotInv && ((Slot)comp).hasItem()) {
		    	if(advEditor.isVisible()) advEditor.toggleSlot((SlotInv)comp);
		    }
		}

		/*
		 * Export as DnD if the drag is more than 5 pixels.
		 */
		public void mouseDragged(MouseEvent e) {
		    if (firstMouseEvent != null) {
		        e.consume();
		        int dx = Math.abs(e.getX() - firstMouseEvent.getX());
		        int dy = Math.abs(e.getY() - firstMouseEvent.getY());
		        if (dx > 5 || dy > 5) {
		            JComponent c = (JComponent)e.getSource();
		            Slot slt  = (Slot)c;
		            TeaTransfer handler = (TeaTransfer)c.getTransferHandler();
		            handler.teaExport(c, firstMouseEvent, slt.getTransferMode(), firstMouseEvent.getButton(), parent);
		            firstMouseEvent = null;
		            if(advEditor.hasFullSlot()) advEditor.removeSlot();
		        }
		    }
		}

		/*
		 * Kill the first mouse event.
		 */
		public void mouseReleased(MouseEvent e) {
		    firstMouseEvent = null;
		}

		/*
		 * Set the name label to display an item's name if the mouse hovers over it.
		 */
		public void mouseEntered(MouseEvent e) {
			Slot s = (Slot)e.getSource();
			if(s.hasItem()) {
				nameLabel.setText(s.getItem().getName());
			}
		}

		/*
		 * Clear the effects of above.
		 */
		public void mouseExited(MouseEvent e) {
			nameLabel.setText("");
		}

		public void mouseWheelMoved(MouseWheelEvent e) {
			int change = e.getWheelRotation() * -1;
			Slot si = (Slot)e.getSource();
			if(si instanceof SlotInv && si.hasItem()) {
				Item cItem = si.getItem();
				if(TeaKey.testKey(KeyEvent.VK_CONTROL)) {
					int newDamage = cItem.getDamage() + change;
					si.setItem(InvUtils.buildItem(cItem.getID(), newDamage, cItem.getCount(), cItem.hasSpecialDamage()));
				} else {
					int newCount = cItem.getCount() + change;
					if(newCount >= 1 && newCount <= 255) cItem.setCount(newCount);
				}
				si.repaint();
			}
		}

	}

	/*
	 * A special transfer handler for the inventory editor. It handles item movement between slots and the like.
	 */
	private class TeaTransfer extends TransferHandler {

		public TeaTransfer(String text) {
			super(text);
		}

		/*
		 * A custom data import method, instead of going with whatever the TransferHandler dictates.
		 */
		public boolean importData(TransferHandler.TransferSupport support) {
			Component j = support.getComponent();
			destSlot = (Slot)support.getComponent();         // The actual destination slot
			tempSlot = ((Slot)support.getComponent()).copy(); // A copy of the destination slot
			Item sItemCopy = ((Item)sourceSlot.getItem().clone());
			boolean bool;
			//If the source slot and the destination slot are both Inventory Slots
			if(sourceSlot instanceof SlotInv && j instanceof SlotInv) {
				if(tempSlot.canImport() && sourceSlot != destSlot) {
					dropSuccess = true;
					if(tTransferMode == TransferHandler.MOVE && sourceSlot.canExport()) {
						if(sourceSlot.getItem().getID() == tempSlot.getItem().getID() &&
							(!sourceSlot.getItem().hasSpecialDamage() ||
							  sourceSlot.getItem().getDamage() == tempSlot.getItem().getDamage())) {
							int src = sourceSlot.getItem().getCount();
							int dst = destSlot.getItem().getCount();
							int max = sourceSlot.getItem().getMaxCount();
							if((dst < max && src < max) || dst < max && src == max) {
								dst += src;
								if(dst > max) {
									src = dst - max;
									dst = max;
							}
							else src = 0;
							destSlot.setItem((Item)sourceSlot.getItem().clone());
							destSlot.getItem().setCount(dst);
							sourceSlot.getItem().setCount(src);
							if(sourceSlot.getItem().getCount() == 0) sourceSlot.removeItem();
							}
						}
						else if (tTransferButton == MouseEvent.BUTTON3 && !destSlot.hasItem() && sourceSlot.getItem().getCount() > 1) {
							int num = sourceSlot.getItem().getCount();
							destSlot.setItem((Item)sourceSlot.getItem().clone());
							destSlot.getItem().setCount(num/2);
							sourceSlot.getItem().setCount(5);
							Integer newCount = (num - (num/2));
							sourceSlot.getItem().setCount(newCount.intValue());
						}
						else {
							tempSlot.setItem(sourceSlot.getItem());
							sourceSlot.setItem(destSlot.getItem());
							destSlot.setItem(tempSlot.getItem());
						}
						if(!sourceSlot.hasItem() || sourceSlot.getItem().getCount() == 0) sourceSlot.removeItem();
					}
					else if(tTransferMode == TransferHandler.LINK && sourceSlot.canExport()) {
						destSlot.setItem(sourceSlot.getItem());
						sourceSlot.removeItem();
					}
					else if(tTransferMode == TransferHandler.COPY) {
						destSlot.setItem((Item)sourceSlot.getItem().clone());
					}
					else {
						dropSuccess = false;
						bool = false;
					}
					bool = true;
				}
				else {
					bool = false;
				}
			}
			// If the source slot is an item list slot and the destination is an inventory slot
			else if(sourceSlot instanceof SlotItem && j instanceof SlotInv) {
				if(tempSlot.canImport()) {
					dropSuccess = true;
					if(tTransferMode == TransferHandler.MOVE) {
						if(sourceSlot.getItem().getID() == tempSlot.getItem().getID() &&
					   	    tempSlot.getItem().getCount() < tempSlot.getItem().getMaxCount()) {
							destSlot.getItem().setCount(destSlot.getItem().getCount() + 1);
						}
						else {
							if(tTransferButton == MouseEvent.BUTTON3) destSlot.setItem(((Item)sourceSlot.getItem().clone()).setCount(sourceSlot.getItem().getMaxCount()));
							else destSlot.setItem((Item)sourceSlot.getItem().clone());
						}
					}
					else if(tTransferMode == TransferHandler.COPY) {
						int destID = ((SlotInv)destSlot).getSlot();
						if(destID < 36) {
							destID = destID % 9;
							for(int i=destID; i<=destID+27; i+=9) {
								if(!invSlots[i].hasItem()) invSlots[i].setItem(sItemCopy.setCount(sItemCopy.getMaxCount()));
							}
						}
						else destSlot.setItem((Item)sourceSlot.getItem().clone());
					}
					else if(tTransferMode == TransferHandler.LINK) {
						int destID = ((SlotInv)destSlot).getSlot();
						if(destID < 36)
							fillInvRange(sItemCopy.setCount(sItemCopy.getMaxCount()), (destID/9)*9, ((destID/9)*9)+8);
						else destSlot.setItem(sItemCopy);
					}
					bool = true;
				}
				else {
					bool = false;
				}
			}
			// If the destination slot is that if a tool button
			else if(j instanceof ToolButton) {
				int md = ((ToolButton)j).getTransferMode();
				if(sourceSlot instanceof SlotInv) {
					int destID = ((SlotInv)sourceSlot).getSlot();
					int rowStart = (destID/9)*9;
					int rowEnd = rowStart + 8;
					if(tTransferMode == TransferHandler.COPY) {
						if(md == FULL) {
							if(destID < 36) {
								destID = destID % 9;
								for(int i=destID; i<=destID+27; i+=9) {
									if(!invSlots[i].hasItem()) invSlots[i].setItem(sItemCopy);
								}
							}
						}
						else if(md == TRASH) {
							if(destID < 36) {
								destID = destID % 9;
								for(int i=destID; i<=destID+27; i+=9) {
									if(invSlots[i].hasItem()) invSlots[i].removeItem();
								}
							}
						}
						else if(md == REPAIR) {
							sourceSlot.doAction(md);
						}
					}
					else if(tTransferMode == TransferHandler.LINK) {
						if(md == FULL) {
							if(destID < 36) fillInvRange(sItemCopy, rowStart, rowEnd);
						}
						else if(md == TRASH) {
							if(destID < 36) clearInvRange(rowStart, rowEnd);
							else clearInvRange(100,103);
						}
						else if(md == REPAIR) {
							sourceSlot.doAction(md);
						}
					}
					else if(tTransferButton == MouseEvent.BUTTON3) {
						if(md == FULL) {
							fillInventory(sItemCopy);
						}
						else if(md == TRASH) {
							clearItem(sItemCopy);
						}
					}
					else sourceSlot.doAction(md);
				}
				else if(sourceSlot instanceof SlotItem) {
					if(md == FULL) fillInventory(sItemCopy.setCount(sItemCopy.getMaxCount()));
				}
				bool = false;
			}
			//Otherwise, don't do anything.
			else bool = false;

			//This is important. Without these two calls, the repaint of the window when something changed wouldn't work correctly.
			repaint();
			revalidate();

			return bool;
		}

	 	public void exportDone(JComponent source, Transferable data, int action) {

	 	}

	 	public void teaExport(JComponent comp, InputEvent e, int action, int button, TabInventory parent) {
	 		tTransferButton = button;
	 		dropSuccess = false;
	 		sourceSlot = (Slot)comp;
	 		if(comp instanceof SlotItem) action = TransferHandler.MOVE;
	 		if     (TeaKey.testKey(KeyEvent.VK_CONTROL) && !TeaKey.testKey(KeyEvent.VK_ALT)) tTransferMode = TransferHandler.COPY;
	 		else if(TeaKey.testKey(KeyEvent.VK_ALT) && !TeaKey.testKey(KeyEvent.VK_CONTROL)) tTransferMode = TransferHandler.LINK;
	 		else tTransferMode = action;
	 		if(sourceSlot.hasItem()) exportAsDrag(comp, e, TransferHandler.COPY);
	 	}

	}

	/*
	 * The all-important item class. Every one represents an item in your inventory.
	 */
	private static class Item implements Cloneable, Comparable
	{
	 	private int id;
	 	private int damage;
	 	private int count;
	 	private String name;
	 	private ImageIcon icon;
	 	private boolean specialDamage;
	 	private int maxCount;
	 	private int maxDamage;

		public Item() {
			id = -1;
			damage = 0;
			count = 1;
			maxCount = 64;
			name = "";
		}

	 	public Item(int i) {
	 		this(i, 0, 1);
	 	}

		/*
		 * Creates an item with the specified id, damage , and count. Takes
		 */
	 	public Item(int i, int d, int c) {
	 		this(InvUtils.getItemData(""+i));
	 		damage = d;
	 		count = c;
	 	}

		/*
		 * Creates an item based on an array of data, whose format is that of the arrays loaded when the InvUtils loads the items.txt file.
		 */
	 	public Item(String[] data) {
	 		id = Integer.parseInt(data[InvUtils.ITEM_ID]);
	 		name = InvUtils.getItemName(data[InvUtils.ITEM_NAME]);
	 		damage = 0;
	 		count = 1;
	 		maxCount = 64;
	 		maxDamage = 0;
	 		specialDamage = false;

			if(data.length == InvUtils.ITEM_SPECIAL_FIELD + 1) {
		 		String[] sDat = data[InvUtils.ITEM_SPECIAL_FIELD].split(";");
		 		for(String s : sDat) {
		 			if(s.matches("\\+[\\d]*")) {
		 				maxDamage = Integer.parseInt(s.replace("+", ""));
		 				maxCount = 1;
		 			}
		 			else if(s.matches("x[\\d]*")) {
		 				maxCount = Integer.parseInt(s.replace("x", ""));
		 			}
		 			else if(s.matches("[\\d]*")) {
		 				damage = Integer.parseInt(s);
		 				specialDamage = true;
		 			}
		 		}
			}

			String[] coords = data[InvUtils.ITEM_COORDINATES].split(",");
			icon = InvUtils.grabItemIcon(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), 2,data[InvUtils.ITEM_LOCATION]);
			if(icon.getIconHeight() == 0) icon = InvUtils.getNullIcon(2);
	 	}

		//Predefined methods
		public int compareTo(Object that) {
			final int BEFORE = -1;
	    	final int EQUAL = 0;
	    	final int AFTER = 1;

			if(this == that) return EQUAL;

			int thisID = id;
			int thatID = ((Item)that).getID();

			if(thisID > thatID) return AFTER;
			else if(thisID < thatID) return BEFORE;
			else return EQUAL;
		}

	    public boolean equals(Object obj) {
	    	if(obj == null) return false;
	    	if(obj == this) return true;
	    	if(obj instanceof Item) {
	    		Item titem = (Item)obj;
	    		return (id == titem.getID() && count == titem.getCount() && damage == titem.getDamage());
	    	}
	    	else return false;
	    }

	    public int hashCode() {
	    	int result = HashCodeUtil.SEED;
	    	result = HashCodeUtil.hash(result, id);
	    	result = HashCodeUtil.hash(result, damage);
	    	result = HashCodeUtil.hash(result, count);
	    	result = HashCodeUtil.hash(result, name);
	    	result = HashCodeUtil.hash(result, specialDamage);
	    	result = HashCodeUtil.hash(result, maxCount);
	    	result = HashCodeUtil.hash(result, icon);
			return result;
	    }

	 	public String toString() {
	 		return "" + id + ":" + name + ",x" + count + "," + damage + "dmg";
	 	}

	 	//Generic Getter/Setters
		public boolean hasSpecialDamage() {
			return specialDamage;
		}

	 	public int getID() {
	 		return id;
	 	}

	 	public void setID(int nID) {
	 		id = nID;
	 	}

	 	public String getName() {
	 		if(id > 0) return name;
	 		else return "";
	 	}

		public Icon getIcon() {
	 		return icon;
	 	}

	 	public int getCount() {
	 		return count;
	 	}

	 	public int getMaxCount() {
	 		return maxCount;
	 	}

	 	public Item setCount(int c) {
	 		count = c;
	 		return this;
	 	}

	 	public int getDamage() {
	 		return damage;
	 	}

	 	public int getMaxDamage() {
	 		return maxDamage;
	 	}

	 	public void setIconToNull() {
	 		icon = InvUtils.getNullIcon(2);
	 	}

	 	public Item setDamage(int d) {
	 		damage = d;
	 		if(specialDamage && damage > 0) icon = InvUtils.grabItemIcon(id + "d" + damage, 2);
	 		return this;
	 	}

	 	public Object clone() {
	 		Item i;
	 		if(specialDamage) i = InvUtils.buildItem(this.getID(), this.getDamage(), this.getCount(), true);
	 		else i = InvUtils.buildItem(this.getID(), this.getDamage(), this.getCount(), false);
	 		return i;
	 	}

	}

	public class Slot extends JLabel {
		protected Item item;
		protected boolean canImport;
		protected boolean canExport;
		protected boolean hasItem;
		protected int transferMode;
		protected int size;
		protected MouseAdapter mouseAdapter;
		protected ImageIcon backgroundIcon;
		protected String itemDescription;

		//Constructors
	    public Slot(int sze, MouseAdapter msAdptr, int mode, boolean brdr, boolean cnImprt, boolean cnExprt) {
			this(sze, msAdptr, mode, brdr, cnImprt, cnExprt, new Item());
			itemDescription = "";
			hasItem = false;
	    }

	    public Slot(int sze, MouseAdapter msAdptr, int mode, boolean brdr, boolean cnImprt, boolean cnExprt, Item itm) {
	    	super();
	    	size = sze;
	    	setPreferredSize(new Dimension(size,size));
			setMinimumSize(getPreferredSize());
			setMaximumSize(getPreferredSize());
			setHorizontalAlignment(SwingConstants.CENTER);
			if(brdr) setBorder(BorderFactory.createEtchedBorder());

			addMouseMotionListener((MouseMotionListener)msAdptr);
			addMouseListener((MouseListener)msAdptr);
			addMouseWheelListener((MouseWheelListener)msAdptr);
			setTransferHandler(new TeaTransfer("item"));

			canImport = cnImprt;
			canExport = cnExprt;

			item = itm;
	    	setIcon(item.getIcon());
	    	hasItem = true;
	    	transferMode = mode;
	    	itemDescription = item.getName();
	    }

	   		//For an empty slot.
	    public Slot(int sze) {
	    	super();
	    	size = sze;
	    	setPreferredSize(new Dimension(size,size));
			setMinimumSize(getPreferredSize());
			setMaximumSize(getPreferredSize());
			setHorizontalAlignment(SwingConstants.CENTER);
			transferMode = TransferHandler.NONE;

			canImport = false;
			canExport = false;
			hasItem = false;
			itemDescription = "";
	    }

	    //Specially defined methods
	    public void paintComponent(Graphics g) {
	    	super.paintComponent(g);
	    	Color in = Color.WHITE;
	    	Color out = Color.BLACK;
	    	int aDamage;
	    	int aCount;

	    	if(hasItem && item.getCount() > 1) {
	    		aCount = item.getCount();
	    		if(aCount > item.getMaxCount()) {
	    			in = Color.RED;
	    			out = Color.BLACK;
	    		}
	    		int length = ("" + aCount).length();
	    		if(length == 1) drawOutlinedText(g, "" + aCount, 30, 36, out, in);
	    		else if(length == 2) drawOutlinedText(g, "" + aCount, 23, 36, out, in);
	    		else if(length == 3) drawOutlinedText(g, "" + aCount, 16, 36, out, in);
	    		else drawOutlinedText(g, "" + aCount, 9, 36, out, in);
	    	}
	    	if(hasItem && item.getDamage() != 0 && !item.hasSpecialDamage()) {
	    		out = Color.RED;
	    		aDamage = item.getDamage();
	    		if(aDamage > item.getMaxDamage()) {
	    			in = Color.RED;
	    			out = Color.BLACK;
	    		}
	    		if(aDamage < 0) {
	    			aDamage *= -1;
	    			in = Color.WHITE;
	    			out = Color.GREEN;
	    		}
		  		drawOutlinedText(g, "" + aDamage, 3, 12, out, in);
	    	}
	    }

	    public void drawOutlinedText(Graphics g, String t, int x, int y, Color oColor, Color tColor)
	    {
	    	g.setFont(new Font("Monospaced", Font.BOLD, 12));
	    	g.setColor(oColor);
			g.drawString(t, ShiftWest(x, 1), ShiftNorth(y, 1));
			g.drawString(t, ShiftWest(x, 1), y);
			g.drawString(t, ShiftWest(x, 1), ShiftSouth(y, 1));
			g.drawString(t, ShiftEast(x, 1), ShiftNorth(y, 1));
			g.drawString(t, ShiftEast(x, 1), y);
			g.drawString(t, ShiftEast(x, 1), ShiftSouth(y, 1));
			g.drawString(t, x,				 ShiftSouth(y, 1));
			g.drawString(t, x,				 ShiftNorth(y, 1));
			g.setColor(tColor);
			g.drawString(t, x, y);
	    }

	    //For de painting!
	    public int ShiftNorth(int p, int distance) {
		   return (p - distance);
		}
		public int ShiftSouth(int p, int distance) {
		   return (p + distance);
		}
		public int ShiftEast(int p, int distance) {
		   return (p + distance);
		}
		public int ShiftWest(int p, int distance) {
		   return (p - distance);
		}


	    public String toString() {
	    	if(hasItem) return "Slot full:" + item;
	    	else return "Slot empty.";
	    }

	    public boolean equals(Object obj) {
	    	if(obj == null) return false;
	    	if(obj == this) return true;
	    	if(obj instanceof Slot) {
	    		if(hasItem) return item.equals(((Slot)obj).getItem());
	    		else return false;
	    	}
	    	else return false;

	    }

		//Item operations
	    public Item getItem() {
	    	return item;
	    }

	    public void setItem(Item i) {
	    	item = i;
	    	setIcon(item.getIcon());
	    	if(item.getID() > 0) {
	    		 hasItem = true;
	    		 itemDescription = item.getName();
	    	}
	    	else {
	    		hasItem = false;
	    		itemDescription = "";
	    	}
	    }

	    public void setItemDescription(String id) {
	    	itemDescription = id;
	    }

	    public String getItemDescription() {
	    	return itemDescription;
	    }

	    public void removeItem() {
	    	item = new Item();
	    	itemDescription = "";
		    if(backgroundIcon == null) setIcon(null);
		    else setIcon(backgroundIcon);
	    	hasItem = false;
	    }

	    public boolean hasItem() {
	    	return hasItem;
	    }

		//Transfer operations
	    public int getTransferMode() {
	    	return transferMode;
	    }

	    //Does something to the contained item
	   	public void doAction(int mode) {
	    	if(mode == REPAIR && !item.hasSpecialDamage()) {
				item.setDamage(0);
	    	}
	    	else if(mode == FULL) {
				item.setCount(item.getMaxCount());
	    	}
	    	else if(mode == TRASH) {
	    		removeItem();
	    	}
	    	repaint();
	    }

	    public Slot copy() {
	    	return new Slot(size, mouseAdapter, transferMode, true, canImport, canExport, item);
	    }

	    //Generic getter/setter methods
	    public void setBackgroundIcon(ImageIcon i) {
	    	setIcon(i);
	    	backgroundIcon = i;
	    	repaint();
	    	revalidate();
	    }

	    public boolean canImport() {
	    	return canImport;
	    }

	    public boolean canExport() {
	    	return canExport;
	    }

	}

	public class SlotInv extends Slot
	{
		private int slot;
		private boolean hasBG;

		//Slot Number, Associated Mouse Adapter, Transfer Mode
		public SlotInv(int slt, MouseAdapter msAdptr) {
			super(41, msAdptr, TransferHandler.MOVE, true, true, true);
			hasBG = false;
			slot = slt;
		}

		public SlotInv(int slt, MouseAdapter msAdptr, ImageIcon bgi) {
			super(41, msAdptr, TransferHandler.MOVE, true, true, true);
			setBackgroundIcon(bgi);
			hasBG = true;
			slot = slt;
		}

		public int getSlot() {
			return slot;
		}

		public String toString() {
		    if(hasItem) return "Slot " + slot + " full:" + item;
	    	else return "Slot " + slot + " empty.";
		}

	}

	public class SlotItem extends Slot {
		public SlotItem(Item i, MouseAdapter msAdptr) {
			super(37, msAdptr, TransferHandler.COPY, true, false, false, i);
			setToolTipText(item.getName() + " [" + item.getID() + "]");
		}

		//Empty!
		public SlotItem() {
			super(37);
		}
	}

	public class ToolButton extends Slot {
		public ToolButton(String s, int m, MouseAdapter ms) {
			super(52, ms, m, true, false, false);
			setText(s);
		}
	}

	public class AdvancedPanel extends JPanel implements ActionListener, ChangeListener {
		private AdvMouse amouse;
		private JPanel editPanel;

		private JPanel namePanel;
			private JLabel name;
		private JPanel idSpinPanel;
			private JSpinner idSpinner;
		private JPanel countSpinPanel;
			private JSpinner countSpinner;
		private JPanel countButtons;
			private JLabel oneButton;
			private JLabel fullButton;
			private JLabel manyButton;
		private JPanel damageSpinPanel;
			private JSpinner damageSpinner;
		private JPanel damageButtons;
			private JLabel invinciButton;
			private JLabel healButton;
		private JTextField searchField;

		private SlotInv currentSlot;
		private Item currentItem;
		private boolean hasSlot;
		private AdvMouse m;

		public AdvancedPanel() {
			super();
			hasSlot = false;
			m = new AdvMouse();

			addMouseListener((MouseListener)m);

			setPreferredSize(new Dimension(125,invPanel.getPreferredSize().height));
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

			editPanel = new JPanel();
			editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.Y_AXIS));
			setAllSize(editPanel, new Dimension(this.getPreferredSize().width-7,invPanel.getPreferredSize().height));
			add(editPanel);
			add(Box.createRigidArea(new Dimension(7,0)));

			//editPanel.add(Box.createVerticalGlue());

			//Name panel
			namePanel = buildGenericPanel(20);
			name = new JLabel("None");
			//name.setFont(new Font("Monospaced", Font.PLAIN, 12));
			setAllSize(name, namePanel.getPreferredSize());
			name.setHorizontalAlignment(SwingConstants.CENTER);
			name.setVerticalAlignment(SwingConstants.CENTER);
			namePanel.add(name);
			editPanel.add(namePanel);
			editPanel.add(Box.createRigidArea(new Dimension(0,5)));

			//ID Spinner
			idSpinPanel = buildGenericPanel(20);
			idSpinner = addLabeledSpinner(idSpinPanel, "ID: ", new SpinnerNumberModel(0,0,null,1));
			idSpinner.setEnabled(false);
			editPanel.add(idSpinPanel);
			editPanel.add(Box.createRigidArea(new Dimension(0,5)));

			//Count Spinner
			countSpinPanel = buildGenericPanel(20);
			countSpinner = addLabeledSpinner(countSpinPanel, "CNT:", new SpinnerNumberModel(1,1,null,1));
			countSpinner.setEnabled(false);
			editPanel.add(countSpinPanel);
			editPanel.add(Box.createRigidArea(new Dimension(0,5)));

			//Count Buttons
			countButtons = buildGenericPanel(16);

			oneButton = buildButtonLabel("1", 35, 16, m);
			countButtons.add(oneButton);
			countButtons.add(Box.createHorizontalGlue());

			fullButton = buildButtonLabel("64", 35, 16, m);
			countButtons.add(fullButton);
			countButtons.add(Box.createHorizontalGlue());

			manyButton = buildButtonLabel("255", 35, 16, m);
			countButtons.add(manyButton);

			editPanel.add(countButtons);
			editPanel.add(Box.createRigidArea(new Dimension(0,5)));

			//Damage Spinner
			damageSpinPanel = buildGenericPanel(20);
			damageSpinner = addLabeledSpinner(damageSpinPanel, "DAM:", new SpinnerNumberModel());
			damageSpinner.setEnabled(false);
			editPanel.add(damageSpinPanel);
			editPanel.add(Box.createRigidArea(new Dimension(0,5)));

			//Damage Buttons
			damageButtons = buildGenericPanel(16);
			damageButtons.add(Box.createHorizontalGlue());

			invinciButton = buildButtonLabel("-32000", 55, 16, m);
			damageButtons.add(invinciButton);

			damageButtons.add(Box.createHorizontalGlue());

			healButton = buildButtonLabel("0", 35, 16, m);
			damageButtons.add(healButton);

			damageButtons.add(Box.createHorizontalGlue());
			editPanel.add(damageButtons);
			editPanel.add(Box.createRigidArea(new Dimension(0,5)));
			editPanel.add(Box.createVerticalGlue());

			//Search Box
			searchField = new JTextField();
			setAllSize(searchField, new Dimension(editPanel.getPreferredSize().width, 22));
			searchField.addActionListener(this);
			editPanel.add(searchField);
			editPanel.add(Box.createRigidArea(new Dimension(0,1)));
		}

		public void applyChanges() {
			//System.out.println("Applying changes...");
			int newID =  ((Integer)idSpinner.getValue()).intValue();
			int newCnt = ((Integer)countSpinner.getValue()).intValue();
			int newDam = ((Integer)damageSpinner.getValue()).intValue();

			if(newCnt > 255) {
				newCnt = 255;
				countSpinner.setValue(new Integer(255));
			}

			Item nItem = InvUtils.buildItem(newID, newDam, newCnt, currentItem.hasSpecialDamage());
			currentSlot.setItem(nItem);
			name.setText(nItem.getName());

			grabKeyFocus();

			currentSlot.repaint();
			repaint();
			revalidate();
		}

		public JComponent setAllSize(JComponent comp, Dimension sz) {
			comp.setPreferredSize(sz);
			comp.setMaximumSize(sz);
			comp.setMinimumSize(sz);
			return comp;
		}

		public JLabel buildButtonLabel(String str, int width, int height, AdvMouse ml) {
			JLabel l = new JLabel(str);
			l.setHorizontalAlignment(SwingConstants.CENTER);
			l.addMouseListener((MouseListener)ml);
			l.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
			setAllSize(l, new Dimension(width, height));
			return l;

		}

		public JPanel buildGenericPanel(int height) {
			JPanel p = new JPanel();
			p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
			setAllSize(p, new Dimension(editPanel.getPreferredSize().width, height));
			return p;
		}

		public JSpinner addLabeledSpinner(Container c, String label, SpinnerModel model) {
	        JLabel l = new JLabel(label);
	        l.setFont(new Font("Monospaced", Font.PLAIN, 12));
	        c.add(l);

	        JSpinner spinner = new JSpinner(model);
	        spinner.addChangeListener(this);
	        spinner.addMouseListener((MouseListener)m);
	        l.setLabelFor(spinner);
	        c.add(spinner);

	        return spinner;
    	}

    	public boolean hasFullSlot() {
    		return hasSlot;
    	}

		public void enablePanel() {
			currentSlot.setBorder(BorderFactory.createEtchedBorder(Color.WHITE, Color.RED));
		}

		public void disablePanel() {
			currentSlot.setBorder(BorderFactory.createEtchedBorder());
		}

		public void setCurrentSlot(SlotInv i) {
			currentSlot = i;
			currentItem = i.getItem();
			name.setText(currentItem.getName());
			fullButton.setText(""+currentItem.getMaxCount());

			hasSlot = true;
			idSpinner.setEnabled(true);
			countSpinner.setEnabled(true);
			damageSpinner.setEnabled(true);
			idSpinner.setValue(new Integer(currentItem.getID()));
			countSpinner.setValue(new Integer(currentItem.getCount()));
			damageSpinner.setValue(new Integer(currentItem.getDamage()));

			enablePanel();
		}

		public void toggleSlot(SlotInv i) {
			if(hasSlot && currentSlot == i) {
				removeSlot();
			}
			else if(hasSlot) {
				removeSlot();
				setCurrentSlot(i);
			}
			else setCurrentSlot(i);
		}

		public void removeSlot() {
			grabKeyFocus();
			disablePanel();
			name.setText("None");
			currentSlot = null;
			currentItem = null;
			hasSlot = false;
			idSpinner.setEnabled(false);
			countSpinner.setEnabled(false);
			damageSpinner.setEnabled(false);
			idSpinner.setValue(new Integer(0));
			countSpinner.setValue(new Integer(1));
			damageSpinner.setValue(new Integer(0));
		}

		public void stateChanged(ChangeEvent e) {
			if(hasSlot && e.getSource() instanceof JSpinner) applyChanges();
		}

		public void actionPerformed(ActionEvent e) {
			grabKeyFocus();
			searchPanel = new JPanel();
			if(materials.getTitleAt(materials.getTabCount()-1) == "") materials.remove(materials.getTabCount()-1);
			materials.addTab(null, searchPanel);
			materials.setSelectedIndex(materials.getTabCount()-1);
			String searchText = ""+searchField.getText();
			String[] itemsSearched = getSearchArray(searchText);
			if(itemsSearched.length > 0)loadItems(searchPanel, itemsSearched);
		}

		public String[] getSearchArray(String search) {
			Iterator<Map.Entry<String, String[]>> itemIterator = InvUtils.getItemsIter();
			ArrayList<String> list = new ArrayList<String>();
			String[] temp = new String[0];
			Item tItem;
			search = search.toLowerCase();
			while(itemIterator.hasNext()) {
				temp = itemIterator.next().getValue();
				if(temp[0].toLowerCase().contains(search) || temp[1].toLowerCase().contains(search) || InvUtils.getItemName(temp[1]).toLowerCase().contains(search)) {
					try {
						tItem = new Item(temp);
						if(new Item(temp).hasSpecialDamage()) list.add(temp[0] + "d" + temp[4]);
						else list.add(temp[0]);
					} catch (Exception e) {

					}
				}
			}
			if(list.size() == 0) {
				try {
					int newInt = Integer.parseInt(search);
					list.add(search);
				} catch (Exception e) {

				}
			}
			return list.toArray(new String[0]);
		}

		private class AdvMouse extends MouseAdapter {
			private BitSet buttons;

			public AdvMouse() {
				super();
				buttons = new BitSet(3);
			}

			public void mouseClicked(MouseEvent e) {
				if(hasSlot && e.getSource() instanceof JLabel) {
					int aInt = Integer.parseInt(((JLabel)e.getSource()).getText());
					//Count
					if(aInt == 1 || aInt == currentItem.getMaxCount() || aInt == 255) {
						countSpinner.setValue(new Integer(aInt));
					}
					//Damage
					else if(aInt == -32000 || aInt == 0) {
						damageSpinner.setValue(new Integer(aInt));
					}
					applyChanges();
				}
			}

			public void mousePressed(MouseEvent e) {
				buttons.set(e.getButton(), true);
			}

			public void mouseReleased(MouseEvent e) {
				buttons.set(e.getButton(), false);
			}

			public boolean testMouseButton(int testNum) {
				return buttons.get(testNum);
			}
		}
	}

}
