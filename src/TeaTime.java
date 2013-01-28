/*
 * TeaTime
 * The extraordinary, amazing, terrific, incredible minecraft editor
 *
 * Made by A. Anderson
 * Version 1.5
 * 2011.05.13
 */

 import javax.swing.*;
 import java.awt.*;
 import java.awt.datatransfer.Transferable;
 import java.awt.event.*;
 import javax.swing.event.*;
 import javax.imageio.ImageIO;
 import java.io.File;

 public class TeaTime extends JFrame{
 	//Current version String
 	public static final String CURRENT_VERSION = "1.6";

 	//Current world under editing
 	private Tag currentWorld;

 	//Main Containers
 	//private JFrame window;
 	private Container container;
 	private JTabbedPane mainPane;

 	//Tabs
 	public JPanel invTab;
 	public JPanel dataTab;
 	public JPanel mapsTab;

 	//Menu Bar
 	public TeaMenuBar teaMenuBar;

 	public TeaTime() {
 		super("Tea Time");
 		System.out.print("Initializing window...");
 		try {
 			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
 			//UIManager.setLookAndFeel( "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
 			setIconImage(ImageIO.read(this.getClass().getResourceAsStream("/lib/icons/coffee_cup_icon.png")));
 		} catch (Exception e) {
 			TeaUtils.buildExceptionDialog(e);
 		}
 		setBounds(0,0,550,500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
		System.out.println("done");

		System.out.print("Creating menu bar... ");
		teaMenuBar = new TeaMenuBar(this);
		setJMenuBar(teaMenuBar);
		System.out.println("done.");


		mainPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
		mainPane.setFocusable(false);
 		container = getContentPane();
 		container.add(mainPane);

 		invTab = new TabInventory(this);
		mainPane.addTab("Inventory Editor", invTab);

		dataTab = new TabLevelData(this);
		mainPane.addTab("Level Data", dataTab);

		mapsTab = new TabMapEditor(this);
		mainPane.addTab("Maps Editor", mapsTab);

		mainPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JTabbedPane tp = (JTabbedPane)e.getSource();
				int s = tp.getSelectedIndex();
				JPanel p = (JPanel)tp.getComponentAt(s);
				if(p instanceof TabInventory) {
					teaMenuBar.toggleAdvancedEditor.setEnabled(true);
				}
				else{
					teaMenuBar.toggleAdvancedEditor.setEnabled(false);
				}
				((TabInventory)invTab).killAdvancedPanel();
				pack();
				p.requestFocusInWindow();
			}
		});

		pack();
 		setVisible(true);
 	}

 	public TabInventory getInvTab() {
 		return (TabInventory)invTab;
 	}

 	public static void main(String[] args) throws Exception {
    	System.out.println("Creating tea time instance...");
    	TeaTimer timer = new TeaTimer();
    	timer.start();
    	TeaTime tt = new TeaTime();
    	timer.stop();
    	System.out.println("Tea Time initialization complete: " + timer);
    }

    public JTabbedPane getMainPane() {
    	return mainPane;
    }

    public TabLevelData getDataTab() {
    	return (TabLevelData)dataTab;
    }

    public TabMapEditor getMapsTab() {
    	return (TabMapEditor)mapsTab;
    }

    public void loadWorld(String path) {
    	getInvTab().loadInventoryData(path);
    	getDataTab().loadLevelData(path);
    	String s = new File(path).getParentFile().getName();
    	getMapsTab().loadMapList(s);
    }

 }