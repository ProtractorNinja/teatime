import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.io.FileOutputStream;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.Insets;
import javax.swing.SwingConstants;
import java.util.regex.*;
import java.net.URI;
import java.net.URL;
import java.awt.Desktop;
import javax.swing.Box;
import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import java.io.BufferedOutputStream;

public class TeaMenuBar extends JMenuBar implements ActionListener, ItemListener {

	public TeaTime parent;
	public JFileChooser fc;

	public JMenu saveMenu;
		public JMenuItem saveWorld;
		public JMenuItem saveWorldAs;

	public JMenu openMenu;
		public JMenuItem browseWorld;

	public JMenu worldMenu;
		public JMenuItem refreshWorld;
		public JMenuItem clearWorld;

	public JMenu toolMenu;
		public JMenuItem editItemsList;
		public JMenuItem toggleAdvancedEditor;
		public JMenuItem gotoMCDirectory;

	public JMenu aboutMenu;
		public JMenuItem about;
		public JMenuItem visitNinja;
		public JMenuItem donate;
		public JMenuItem downNew;

	public JMenu schemeMenu;
		private File[] schemes;
		public JMenu loadSchemeMenu;
			public JMenuItem loadScheme;
		public JMenu saveSchemeMenu;
			public JMenuItem saveScheme;
			public JMenuItem saveNewScheme;
		public JMenu delSchemeMenu;
			public JMenuItem delScheme;

	private File schemesFile;

	//These are generic
	private JMenu menu;
	private JMenu subMenu;
	private JMenuItem menuItem;

	private Pattern p;
	private Matcher m;

	private Desktop desktop;

	public static final String PATHSEP = File.separator;

	public TeaMenuBar(TeaTime tt) {
		super();
		parent = tt;

		if(Desktop.isDesktopSupported()) {
			desktop = Desktop.getDesktop();
		}

		openMenu = buildOpenMenu();
		saveMenu = buildSaveMenu();
		worldMenu = buildWorldMenu();
		aboutMenu = buildAboutMenu();
		toolMenu = buildToolMenu();
		schemeMenu = buildSchemeMenu();

		add(openMenu);
		add(saveMenu);
		add(worldMenu);
		add(toolMenu);
		add(schemeMenu);
		add(Box.createHorizontalGlue());
		add(aboutMenu);

		saveWorld.setEnabled(false);
		saveWorldAs.setEnabled(false);
	}

	public JButton buildMenuButton(ImageIcon icon, String toolTip, boolean enabled) {
		JButton b = new JButton(icon);
		b.addActionListener(this);
		b.setToolTipText(toolTip);
		b.setBorderPainted(false);
		b.setFocusable(false);
		b.setEnabled(enabled);
		b.setMargin(new Insets(0,0,0,0));
		return b;
	}

	public JMenu buildOpenMenu() {
		openMenu = new JMenu();
		openMenu.setIcon(getImageIcon("folder.png"));
		int lio;
		String[] aw = TeaUtils.getMCWorldsList();
		String world;
		for(String i : aw) {
			lio = i.lastIndexOf(PATHSEP);
			world = i.substring(i.lastIndexOf(PATHSEP,lio-1)+1, lio);
			menuItem = new JMenuItem(world, getImageIcon("world.png"));
			menuItem.setActionCommand("load world " + world);
			menuItem.addActionListener(this);
			menuItem.setToolTipText("Import data from " + world);
			openMenu.add(menuItem);
		}
		browseWorld = buildMenuItem("Browse...", getImageIcon("folder_go.png"), "Browse for a custom level file", true);
		openMenu.add(browseWorld);

		return openMenu;
	}

	public JMenu buildSaveMenu() {
		JMenu saveMenu = new JMenu();
		saveMenu.setIcon(getImageIcon("disk.png"));

		saveWorld = buildMenuItem("Save the World", getImageIcon("world_go.png"), "Save data to the original world.", true);
		saveMenu.add(saveWorld);

		saveWorldAs = buildMenuItem("Save as...", getImageIcon("disk_download.png"), "Save data to a new location", true);
		saveMenu.add(saveWorldAs);

		return saveMenu;
	}

	public JMenu buildWorldMenu() {
		JMenu wm = new JMenu();
		wm.setIcon(getImageIcon("world.png"));

		refreshWorld = buildMenuItem("Refresh World", getImageIcon("arrow_rotate_clockwise.png"), "Reload the current world's data", false);
		wm.add(refreshWorld);

		clearWorld = buildMenuItem("Clear World", getImageIcon("page_white_world.png"), "Clear everything", true);
		wm.add(clearWorld);

		return wm;
	}

	public JMenu buildAboutMenu() {
		JMenu am = new JMenu();
		am.setIcon(getImageIcon("information.png"));

		about = buildMenuItem("About Tea Time", getImageIcon("coffee_cup_icon.png"), "About Tea Time!", true);
		am.add(about);

		visitNinja = buildMenuItem("Visit Protractor Ninja", getImageIcon("cup_go.png"), "Visit the Protractor Ninja's website!", true);
		am.add(visitNinja);

		downNew = buildMenuItem("Download Current Version", getImageIcon("drive-download.png"), "Download the newest version of Tea Time", true);
		am.add(downNew);

		return am;
	}

	public JMenu buildToolMenu() {
		JMenu tm = new JMenu();
		tm.setIcon(getImageIcon("wand-hat.png"));

		toggleAdvancedEditor = buildMenuItem("Toggle Editor", getImageIcon("overlays.png"), "Toggle the advanced data editor view", true);
		tm.add(toggleAdvancedEditor);

		//editItemsList = buildMenuItem("Edit item list", getImageIcon("cross.png"), "Open the items.txt file in a text editor", true);
		//tm.add(editItemsList);
		// Removed because I doubt I'll ever implement this.

		gotoMCDirectory = buildMenuItem("Minecraft Directory", getImageIcon("drive-download.png"), "Go to your Minecraft data directory", true);
		tm.add(gotoMCDirectory);

		return tm;
	}

	public JMenu buildSchemeMenu() {
		JMenu sm = new JMenu();
		sm.setIcon(getImageIcon("leaf.png"));

		saveSchemeMenu = new JMenu("Save Scheme");
		saveSchemeMenu.setIcon(getImageIcon("leaf--plus.png"));
		sm.add(saveSchemeMenu);

		loadSchemeMenu = new JMenu("Load Scheme");
		loadSchemeMenu.setIcon(getImageIcon("leaf--arrow.png"));
		sm.add(loadSchemeMenu);

		delSchemeMenu = new JMenu("Delete Scheme");
		delSchemeMenu.setIcon(getImageIcon("leaf--minus.png"));
		sm.add(delSchemeMenu);

		resetSchemesMenu();

		return sm;
	}

	public void resetSchemesMenu() {
		loadSchemeMenu.removeAll();
		saveSchemeMenu.removeAll();
		delSchemeMenu.removeAll();
		schemes = TeaUtils.getSchemeFiles();
		String loadCommand;
		String name;
		for(int i=0; i<schemes.length; i++) {
			name = schemes[i].getName().replace(".dat", "");
			loadScheme = buildMenuItem(name, getImageIcon("leaf.png"), "Load the scheme " + name, true);
			loadScheme.setActionCommand("load scheme " + name);
			loadSchemeMenu.add(loadScheme);
			saveScheme = buildMenuItem(name, getImageIcon("leaf.png"), "Save the scheme " + name, true);
			saveScheme.setActionCommand("save scheme " + name);
			saveSchemeMenu.add(saveScheme);
			delScheme = buildMenuItem(name, getImageIcon("leaf-wormhole.png"), "Delete the scheme " + name, true);
			delScheme.setActionCommand("del scheme " + name);
			delSchemeMenu.add(delScheme);
		}

		saveNewScheme = buildMenuItem("Save New", getImageIcon("leaf--pencil.png"), "Save as new scheme", true);
		saveNewScheme.setActionCommand("save new scheme");
		saveSchemeMenu.add(saveNewScheme);
	}

	public JMenuItem buildMenuItem(String title, ImageIcon icon, String tooltip, boolean enabled) {
		JMenuItem mi = new JMenuItem(title, icon);
		mi.addActionListener(this);
		mi.setEnabled(enabled);
		mi.setToolTipText(tooltip);
		return mi;
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		Object src = e.getSource();
		try {
			if(cmd.contains("load world")) {
				String path = TeaUtils.getMCWorldsPath() + PATHSEP + cmd.replace("load world ", "") + PATHSEP + "level.dat";
				parent.loadWorld(path);
	 			String world = cmd.replace("load world ", "");
				parent.setTitle("Tea Time - " + world);
				saveWorld.setEnabled(true);
				refreshWorld.setEnabled(true);
				saveWorldAs.setEnabled(true);
			}
			else if(cmd.contains("load scheme")) {
				Tag tTag = TeaUtils.loadInvScheme(cmd.replace("load scheme ", ""));
				parent.getInvTab().loadInvTag(tTag);
			}
			else if(cmd.contains("save new scheme")) {
				Tag tTag = parent.getInvTab().buildInventoryData();
				String dest = (String)JOptionPane.showInputDialog(null, "Enter a name for your new scheme:" , "New Scheme", JOptionPane.PLAIN_MESSAGE);
				if(dest != null && dest.length() > 0) {
					TeaUtils.saveInvScheme(tTag, dest);
					File f = new File(schemesFile, dest);
				}
				resetSchemesMenu();
			}
			else if(cmd.contains("save scheme")) {
					Tag tTag = parent.getInvTab().buildInventoryData();
					TeaUtils.saveInvScheme(tTag, cmd.replace("save scheme ", ""));
			}
			else if(cmd.contains("del scheme")) {
					TeaUtils.delInvScheme(cmd.replace("del scheme ", ""));
					resetSchemesMenu();
			}
			else if(src == browseWorld) {
				if(fc == null){
					fc = new JFileChooser(TeaUtils.getMCWorldsPath());
					fc.setFileFilter(new TeaFileFilter());
				}
				browseForWorld();
			}
			else if(src == saveWorld) {
				saveWorld();
			}
			else if(src == saveWorldAs) {
				if(fc == null){
					fc = new JFileChooser(TeaUtils.getMCWorldsPath());
					fc.setFileFilter(new TeaFileFilter());
				}
				saveWorldAs();
			}
			else if(src == refreshWorld) {
				parent.loadWorld(TeaUtils.getCurrentWorldPath());
			}
			else if(src == toggleAdvancedEditor) {
				parent.getInvTab().toggleAdvancedPanel();
			}
			else if(src == clearWorld) {
				saveWorld.setEnabled(false);
				refreshWorld.setEnabled(false);
				saveWorldAs.setEnabled(false);
				parent.getInvTab().clearInventory();
				parent.getDataTab().clearAllFields();
				parent.getDataTab().disableAllFields();
				TeaUtils.removeCurrentWorld();
				parent.setTitle("Tea Time");
			}
			else if(src == editItemsList) {
				//desktop.edit(new File(this.getClass().getResource("/lib/items.txt")));
				//desktop.browse(this.getClass().getResource("lib/items.txt").toURI());
				JOptionPane.showMessageDialog(null, "Sorry, this doesn't work right now.\n\nUnzip the .jar with an archive program and edit the \nlib/items.txt file to add items or change groups.");
			}
			else if(src == about) {
				JOptionPane.showMessageDialog(null,
					"\nThe Extraordinary, Amazing, Terrific, Incredible Minecraft Editor is a" +
					"\nworld data editor for Minecraft, a game created by Markus \"Notch\"" +
					"\nPersson. Tea Time was designed and programmed by Protractor" +
					"\nNinja in the Java programming language." +
					"\n\nYour current version is " + TeaTime.CURRENT_VERSION + "." +
					"\n\nMany thanks to:" +
					"\nNotch, for creating Minecraft," +
					"\nMalvaviscos, for support, testing, encouragement, and being a great friend," +
					"\nSun, for creating Java (But not Oracle. I dislike Oracle.)," +
					"\nMinecraftwiki.net for the NBT IO class and block data reference," +
					"\nStackoverflow.com and the Java API for technical support," +
					"\nCopyboy, for creating INVedit and for his items.txt layout and features," +
					"\nand Dawidds + Shockah, creators of Loleditor, for feature inspiration." +
					"\n\nInterface icons courtesy of:" +
					"\nThe Fugue pack - http://p.yusukekamiyamane.com/," +
					"\nThe FamFamFam Silk pack - http://www.famfamfam.com/" +
					"\nThe FamFamFam Silk Companion I pack - http://damieng.com/" +
					"\nThe Breakfast icon pack - http://www.cutelittlefactory.com/" +
					"\n\nThanks for using Tea Time!",
					"It's time for tea!", JOptionPane.PLAIN_MESSAGE);
			}
			else if(src == visitNinja) {
				desktop.browse(new URI("http://protractorninja.blogspot.com/"));
			}
			else if(src == downNew) {
				desktop.browse(new URI("http://protractorninja.blogspot.com/p/downloads.html"));
			}
			else if(src == gotoMCDirectory) {
				desktop.open(new File(TeaUtils.getMCPath()));
			}
		} catch (Exception ie) {
			TeaUtils.buildExceptionDialog(ie);
		}
	}

	public void browseForWorld() {
		int returnVal = fc.showOpenDialog(parent);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			parent.loadWorld(fc.getSelectedFile().getPath());
			saveWorld.setEnabled(true);
			saveWorldAs.setEnabled(true);
			refreshWorld.setEnabled(true);
			String world = TeaUtils.getCurrentWorldPath();
			world = world.substring(world.lastIndexOf(PATHSEP) + 1);
			parent.setTitle("Tea Time - " + world);
		}
		else {
		}
	}

	public void saveWorld() {
		try {
			//Tag newInv = parent.getInvTab().buildInventoryData();
			Tag world = TeaUtils.buildLevelData(parent.getDataTab());
			FileOutputStream fos = new FileOutputStream(TeaUtils.getCurrentWorldPath());
			world.writeTo(fos);
			fos.close();
			parent.loadWorld(TeaUtils.getCurrentWorldPath());
		} catch (Exception e) {
			TeaUtils.buildExceptionDialog(e);
		}
	}

	public void saveWorldAs() {
		int returnVal = fc.showSaveDialog(parent);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				//Tag newInv = parent.getInvTab().buildInventoryData();
				Tag world = TeaUtils.buildLevelData(parent.getDataTab());
				String filePath = fc.getSelectedFile().getPath();
				if(!filePath.matches(".*(?i)\\.dat$")) filePath += ".dat";
				FileOutputStream fos = new FileOutputStream(filePath);
				world.writeTo(fos);
				fos.close();
			} catch (Exception e) {
				TeaUtils.buildExceptionDialog(e);
			}
		}
	}

	public void itemStateChanged(ItemEvent e) {

	}

	public ImageIcon getImageIcon(String name) {
		return new ImageIcon(this.getClass().getResource("/lib/icons/" + name));
	}
}
