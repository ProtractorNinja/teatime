import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import java.awt.Container;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.*;
import javax.swing.JCheckBox;
import java.awt.Dimension;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Insets;
import java.io.FileInputStream;
import java.util.Random;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JSlider;

public class TabLevelData extends JPanel implements ActionListener, ChangeListener {

	private TeaTime parent;
	private Dimension panelDimension;
	private ActionListener dataActionListener;
	private Random rand;
	private MouseAdapter dataMouse;
	private static int TIME_MIN = 0;
	private static int TIME_MAX = 24000;
	private JSlider timeSlider;
	private Tag cTag;
	private Tag[] sTags;
	private Tag cw;

	private JPanel dataPanel;
		private JPanel namePanel;
			private JFormattedTextField nameField;
		private JPanel typePanel;
			private JFormattedTextField typeField;
		private JPanel featurePanel;
			private JCheckBox featureCheck;
		private JPanel seedPanel;
			private JFormattedTextField seedField;
			private JButton seedButtonRandom;
		private JPanel dimensionPanel;
			private JFormattedTextField dimensionField;
			private JButton dimensionButtonHell;
			private JButton dimensionButtonGaia;
		private JPanel timePanel;
			private JFormattedTextField timeField;
			private JButton timeButtonDay;
			private JButton timeButtonNight;
		private JPanel healthPanel;
			private JFormattedTextField healthField;
			private JButton healthButtonFull;
			private JButton healthButtonGod;
		private JPanel firePanel;
			private JFormattedTextField fireField;
			private JButton fireButtonHeal;
		private JPanel scorePanel;
			private JFormattedTextField scoreField;
			private JButton scoreButtonZero;
		private JPanel rainPanel;
			private JFormattedTextField rainField;
			private JCheckBox rainCheck;
		private JPanel stormPanel;
			private JFormattedTextField stormField;
			private JCheckBox stormCheck;
	private JPanel locationPanel;
		private JPanel playerLocation;
			private JPanel playerXPanel;
				private JFormattedTextField playerXField;
			private JPanel playerYPanel;
				private JFormattedTextField playerYField;
			private JPanel playerZPanel;
				private JFormattedTextField playerZField;
			private JPanel playerGotoPanel;
				private JButton playerButtonRefresh;
				private JButton playerButtonToSpawn;
		private JPanel spawnLocation;
			private JPanel spawnXPanel;
				private JFormattedTextField spawnXField;
			private JPanel spawnYPanel;
				private JFormattedTextField spawnYField;
			private JPanel spawnZPanel;
				private JFormattedTextField spawnZField;
			private JPanel spawnGotoPanel;
				private JButton spawnButtonRefresh;
				private JButton spawnButtonToPlayer;

	public TabLevelData(TeaTime tt) {
		super();
		parent = tt;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		rand = new Random(randInt(0,10000000));
		dataMouse = new MouseAdapter() {
			public void mousePressed(final MouseEvent e) {
		        SwingUtilities.invokeLater(new Runnable() {
		            public void run() {
		                JFormattedTextField tf = (JFormattedTextField)e.getSource();
		                int offset = tf.viewToModel(e.getPoint());
		                tf.setCaretPosition(offset);
		            }
		        });
		    }
		};

		dataPanel = buildDataPanel();
		add(dataPanel);

		locationPanel = buildLocationPanel();
		add(locationPanel);

		disableAllFields();

	}

	/*
	 * Creates a generic container JPanel.
	 */
	public JPanel buildJPanel(Dimension d, int boxType, Border bd) {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, boxType));
		setAllSize(p, d);
		p.setBorder(bd);
		return p;
	}

	public JButton buildJButton(Dimension d, String label, ActionListener al) {
		JButton b = new JButton(label);
		setAllSize(b, d);
		b.addActionListener(al);
		return b;
	}

	public JComponent setAllSize(JComponent comp, Dimension sz) {
		comp.setPreferredSize(sz);
		comp.setMaximumSize(sz);
		comp.setMinimumSize(sz);
		return comp;
	}

	public JFormattedTextField addLabeledField(Container c, String label, int width) {
        JLabel l = new JLabel(label);
        setAllSize(l, new Dimension(width, c.getPreferredSize().height));
        c.add(l);

        JFormattedTextField field = new JFormattedTextField();
        field.addMouseListener((MouseListener)dataMouse);
        field.setText("");
        l.setLabelFor(field);
        c.add(field);

        return field;
    }

	/*
	 * Builds the generic data panel
	 */
	public JPanel buildDataPanel() {
		panelDimension = new Dimension(parent.getInvTab().getPreferredSize().width - 10, 22);

		JPanel dp = buildJPanel(null, BoxLayout.Y_AXIS,	BorderFactory.createTitledBorder("Data"));

		namePanel = buildJPanel(panelDimension, BoxLayout.X_AXIS, null);
		nameField = addLabeledField(namePanel, "World Name", 80);
		dp.add(namePanel);
		dp.add(Box.createRigidArea(new Dimension(0,4)));

		typePanel = buildJPanel(panelDimension, BoxLayout.X_AXIS, null);
		typeField = addLabeledField(typePanel, "Game Mode", 80);
		dp.add(typePanel);
		dp.add(Box.createRigidArea(new Dimension(0,4)));

		seedPanel = buildJPanel(panelDimension, BoxLayout.X_AXIS, null);
		seedField = addLabeledField(seedPanel, "World Seed", 80);
		seedPanel.add(Box.createRigidArea(new Dimension(3,0)));
		seedButtonRandom = addButton(seedPanel, "Random", 100);
		dp.add(seedPanel);
		dp.add(Box.createRigidArea(new Dimension(0,4)));

		dimensionPanel = buildJPanel(panelDimension, BoxLayout.X_AXIS, null);
		dimensionField = addLabeledField(dimensionPanel, "Dimension", 80);
		dimensionPanel.add(Box.createRigidArea(new Dimension(3,0)));
		dimensionButtonHell = addButton(dimensionPanel, "Hell", 50);
		dimensionButtonGaia = addButton(dimensionPanel, "Gaia", 50);
		dp.add(dimensionPanel);
		dp.add(Box.createRigidArea(new Dimension(0,4)));

		timePanel = buildJPanel(panelDimension, BoxLayout.X_AXIS, null);
		timeField = addLabeledField(timePanel, "Current Time", 80);
		timePanel.add(Box.createRigidArea(new Dimension(3,0)));
		timeSlider = new JSlider(JSlider.HORIZONTAL, TIME_MIN, TIME_MAX, TIME_MIN);
		timeSlider.addChangeListener(this);
		timeSlider.setFocusable(false);
		timeSlider.setMajorTickSpacing(12000);
		timeSlider.setMinorTickSpacing(6000);
		timePanel.add(timeSlider);
		timePanel.add(Box.createRigidArea(new Dimension(3,0)));
		timeButtonDay = addButton(timePanel, "Day", 50);
		timeButtonNight = addButton(timePanel, "Night", 50);
		dp.add(timePanel);
		dp.add(Box.createRigidArea(new Dimension(0,4)));

		rainPanel = buildJPanel(panelDimension, BoxLayout.X_AXIS, null);
		rainField = addLabeledField(rainPanel, "Rain Time", 80);
		rainPanel.add(Box.createRigidArea(new Dimension(3,0)));
		rainCheck = new JCheckBox("Raining Now?");
		rainCheck.setFocusable(false);
		setAllSize(rainCheck, new Dimension(100, panelDimension.height));
		rainPanel.add(rainCheck);
		dp.add(rainPanel);
		dp.add(Box.createRigidArea(new Dimension(0,4)));

		stormPanel = buildJPanel(panelDimension, BoxLayout.X_AXIS, null);
		stormField = addLabeledField(stormPanel, "Storm Time", 80);
		stormPanel.add(Box.createRigidArea(new Dimension(3,0)));
		stormCheck = new JCheckBox("Storming Now?");
		stormCheck.setFocusable(false);
		setAllSize(stormCheck, new Dimension(100, panelDimension.height));
		stormPanel.add(stormCheck);
		dp.add(stormPanel);
		dp.add(Box.createRigidArea(new Dimension(0,4)));

		healthPanel = buildJPanel(panelDimension, BoxLayout.X_AXIS, null);
		healthField = addLabeledField(healthPanel, "Player Health", 80);
		healthPanel.add(Box.createRigidArea(new Dimension(3,0)));
		healthButtonFull = addButton(healthPanel, "Full", 50);
		healthButtonGod = addButton(healthPanel, "Godly", 50);
		dp.add(healthPanel);
		dp.add(Box.createRigidArea(new Dimension(0,4)));

		firePanel = buildJPanel(panelDimension, BoxLayout.X_AXIS, null);
		fireField = addLabeledField(firePanel, "Fire Data", 80);
		firePanel.add(Box.createRigidArea(new Dimension(3,0)));
		fireButtonHeal = addButton(firePanel, "Heal", 100);
		dp.add(firePanel);
		dp.add(Box.createRigidArea(new Dimension(0,4)));

		scorePanel = buildJPanel(panelDimension, BoxLayout.X_AXIS, null);
		scoreField = addLabeledField(scorePanel, "Score", 80);
		scorePanel.add(Box.createRigidArea(new Dimension(3,0)));
		scoreButtonZero = addButton(scorePanel, "Reset", 100);
		dp.add(scorePanel);
		dp.add(Box.createRigidArea(new Dimension(0,4)));

		featurePanel = buildJPanel(panelDimension, BoxLayout.X_AXIS, null);
		featureCheck = new JCheckBox("Generate Map Features?");
		featureCheck.setFocusable(false);
		featurePanel.add(Box.createHorizontalGlue());
		featurePanel.add(featureCheck);
		dp.add(featurePanel);
		dp.add(Box.createRigidArea(new Dimension(0,4)));

		return dp;
	}

	public JPanel buildLocationPanel() {
		JPanel lp = buildJPanel(null, BoxLayout.X_AXIS, null);

		playerLocation = buildPlayerLocationPanel();
		lp.add(playerLocation);

		spawnLocation = buildSpawnLocationPanel();
		lp.add(spawnLocation);

		return lp;
	}

	public JPanel buildPlayerLocationPanel() {
		panelDimension = new Dimension(parent.getInvTab().getPreferredSize().width/2-11, 22);

		JPanel pl = buildJPanel(null, BoxLayout.Y_AXIS, BorderFactory.createTitledBorder("Player Location"));

		playerXPanel = buildJPanel(panelDimension, BoxLayout.X_AXIS, null);
		playerXField = addLabeledField(playerXPanel, "X Position", 50);
		pl.add(playerXPanel);
		pl.add(Box.createRigidArea(new Dimension(0,5)));

		playerYPanel = buildJPanel(panelDimension, BoxLayout.X_AXIS, null);
		playerYField = addLabeledField(playerYPanel, "Y Position", 50);
		pl.add(playerYPanel);
		pl.add(Box.createRigidArea(new Dimension(0,5)));

		playerZPanel = buildJPanel(panelDimension, BoxLayout.X_AXIS, null);
		playerZField = addLabeledField(playerZPanel, "Z Position", 50);
		pl.add(playerZPanel);
		pl.add(Box.createRigidArea(new Dimension(0,5)));

		playerGotoPanel = buildJPanel(panelDimension, BoxLayout.X_AXIS, null);
		playerButtonRefresh = addButton(playerGotoPanel, "Refresh", pl.getPreferredSize().width/2-5);
		playerButtonRefresh.setEnabled(false);
		playerButtonToSpawn = addButton(playerGotoPanel, "To Spawn", pl.getPreferredSize().width/2-5);
		pl.add(playerGotoPanel);

		return pl;
	}

	public JPanel buildSpawnLocationPanel() {
		panelDimension = new Dimension(parent.getInvTab().getPreferredSize().width/2-11, 22);

		JPanel pl = buildJPanel(null, BoxLayout.Y_AXIS, BorderFactory.createTitledBorder("Spawn Location"));

		spawnXPanel = buildJPanel(panelDimension, BoxLayout.X_AXIS, null);
		spawnXField = addLabeledField(spawnXPanel, "X Position", 50);
		pl.add(spawnXPanel);
		pl.add(Box.createRigidArea(new Dimension(0,5)));

		spawnYPanel = buildJPanel(panelDimension, BoxLayout.X_AXIS, null);
		spawnYField = addLabeledField(spawnYPanel, "Y Position", 50);
		pl.add(spawnYPanel);
		pl.add(Box.createRigidArea(new Dimension(0,5)));

		spawnZPanel = buildJPanel(panelDimension, BoxLayout.X_AXIS, null);
		spawnZField = addLabeledField(spawnZPanel, "Z Position", 50);
		pl.add(spawnZPanel);
		pl.add(Box.createRigidArea(new Dimension(0,5)));

		spawnGotoPanel = buildJPanel(panelDimension, BoxLayout.X_AXIS, null);
		spawnButtonRefresh = addButton(spawnGotoPanel, "Refresh", pl.getPreferredSize().width/2-5);
		spawnButtonRefresh.setEnabled(false);
		spawnButtonToPlayer = addButton(spawnGotoPanel, "To Player", pl.getPreferredSize().width/2-5);
		pl.add(spawnGotoPanel);

		return pl;
	}

	public JButton addButton(Container c, String name, int d) {
		JButton b = new JButton(name);
		b.addActionListener(this);
		b.setFocusable(false);
		b.setMargin(new Insets(0,0,0,0));
		setAllSize(b, new Dimension(d, c.getPreferredSize().height));
		c.add(b);

		return b;
	}

	public void loadLevelData(String filepath) {
		TeaUtils.setCurrentWorldPath(filepath);
		try {
			Tag read = Tag.readFrom(new FileInputStream(filepath));
			loadLevelData(read);
		} catch (Exception e) {
			TeaUtils.buildExceptionDialog(e);
		}
	}

	public void loadLevelData(Tag tag) {
		try {
 			cw = tag;
	 		TeaUtils.setCurrentWorld(cw);

	 		getTagData("Time", timeField, new JButton[]{ timeButtonDay, timeButtonNight});
	 		timeSlider.setValue(new Integer((String)timeField.getValue()));

	 		getTagData("rainTime", rainField);

	 		getTagData("thunderTime", stormField);

	 		loadCheckBox("raining", rainCheck);

	 		loadCheckBox("thundering", stormCheck);

	 		getTagData("Health", healthField, new JButton[]{ healthButtonFull, healthButtonGod});

	 		getTagData("Dimension", dimensionField, new JButton[]{ dimensionButtonHell, dimensionButtonGaia});

	 		sTags = (Tag[])cw.findTagByName("Pos").getValue();
	 		playerXField.setValue(""+sTags[0].getValue());
	 		playerXField.setEnabled(true);
	 		playerYField.setValue(""+sTags[1].getValue());
	 		playerYField.setEnabled(true);
	 		playerZField.setValue(""+sTags[2].getValue());
	 		playerZField.setEnabled(true);
	 		playerButtonRefresh.setEnabled(true);
	 		playerButtonToSpawn.setEnabled(true);

	 		getTagData("Fire", fireField, new JButton[]{ fireButtonHeal });

	 		getTagData("Score", scoreField, new JButton[]{ scoreButtonZero });

	 		getTagData("SpawnX", spawnXField);

	 		getTagData("SpawnY", spawnYField);

	 		getTagData("SpawnZ", spawnZField);
	 		spawnButtonRefresh.setEnabled(true);
	 		spawnButtonToPlayer.setEnabled(true);

			getTagData("RandomSeed", seedField, new JButton[]{ seedButtonRandom });

	 		getTagData("LevelName", nameField);

	 		getTagData("GameType", typeField);

	 		loadCheckBox("MapFeatures", featureCheck);
	 	} catch (Exception e) {
			TeaUtils.buildExceptionDialog(e);
		}
	}

	public boolean getTagData(String tName, JFormattedTextField dest)  {
		try {
			cTag = cw.findTagByName(tName);
			dest.setValue(""+cTag.getValue());
			dest.setEnabled(true);
			return true;
		} catch (Exception e) {
			dest.setValue("");
			dest.setEnabled(false);
			return false;
		}
	}

	public boolean getTagData(String tName, JFormattedTextField dest, JButton[] buttons)  {
		try {
			cTag = cw.findTagByName(tName);
			dest.setValue(""+cTag.getValue());
			dest.setEnabled(true);
			for(int i=0; i<buttons.length; i++) {
				buttons[i].setEnabled(true);
			}
			return true;
		} catch (Exception e) {
			dest.setValue("");
			dest.setEnabled(false);
			for(int i=0; i<buttons.length; i++) {
				buttons[i].setEnabled(false);
			}
			return false;
		}
	}

	public boolean loadCheckBox(String name, JCheckBox dest) {
		try {
			cTag = cw.findTagByName(name);
			boolean b = ((Byte)cTag.getValue()).intValue() == 1;
			dest.setSelected(b);
			dest.setEnabled(true);
			return true;
		} catch (Exception e) {
			dest.setSelected(false);
			dest.setEnabled(false);
			return false;
		}
	}

	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();
		timeField.setValue("" + source.getValue());
	}

	public void actionPerformed(ActionEvent e) {
		Tag tTag;
		Object src = e.getSource();
		if(src == seedButtonRandom) {
			seedField.setValue(""+rand.nextLong());
		}
		else if(src == dimensionButtonHell) {
			dimensionField.setValue("-1");
		}
		else if(src == dimensionButtonGaia) {
			dimensionField.setValue("0");
		}
		else if(src == timeButtonDay) {
			timeField.setValue("0");
			timeSlider.setValue(new Integer(0));
		}
		else if(src == timeButtonNight) {
			timeField.setValue("12000");
			timeSlider.setValue(new Integer(12000));
		}
		else if(src == healthButtonFull) {
			healthField.setValue("20");
		}
		else if(src == healthButtonGod) {
			healthField.setValue("32767");
		}
		else if(src == fireButtonHeal) {
			fireField.setValue("-20");
		}
		else if(src == scoreButtonZero) {
			scoreField.setValue("0");
		}
		else if(src == playerButtonRefresh) {
			try {
				tTag = Tag.readFrom(new FileInputStream(TeaUtils.getCurrentWorldPath()));
				Tag[] t = (Tag[])tTag.findTagByName("Pos").getValue();
				playerXField.setValue(""+t[0].getValue());
				playerYField.setValue(""+t[1].getValue());
				playerZField.setValue(""+t[2].getValue());
			} catch (Exception ee) {
				TeaUtils.buildExceptionDialog(ee);
				tTag = new Tag(Tag.Type.TAG_Compound, "", new Tag[0]);
			}
		}
		else if(src == playerButtonToSpawn) {
			if(spawnXField.getValue() != null) playerXField.setValue(""+spawnXField.getValue());
			if(spawnYField.getValue() != null) playerYField.setValue(""+spawnYField.getValue());
			if(spawnZField.getValue() != null) playerZField.setValue(""+spawnZField.getValue());
		}
		else if(src == spawnButtonRefresh) {
			try {
				tTag = Tag.readFrom(new FileInputStream(TeaUtils.getCurrentWorldPath()));
				spawnXField.setValue(""+tTag.findTagByName("SpawnX").getValue());
				spawnYField.setValue(""+tTag.findTagByName("SpawnY").getValue());
				spawnZField.setValue(""+tTag.findTagByName("SpawnZ").getValue());
			} catch (Exception ee) {
				TeaUtils.buildExceptionDialog(ee);
				tTag = new Tag(Tag.Type.TAG_Compound, "", new Tag[0]);
			}
		}
		else if(src == spawnButtonToPlayer) {
			if(playerXField.getValue() != null) spawnXField.setValue(""+playerXField.getValue());
			if(playerYField.getValue() != null) spawnYField.setValue(""+playerYField.getValue());
			if(playerZField.getValue() != null) spawnZField.setValue(""+playerZField.getValue());
		}
	}

	public int randInt(int min, int max)
	{
		if (min <= max)
			return ((int) ((max-min+1)*Math.random()+min));
		else
			return ((int) ((min-max+1)*Math.random()+max));
	}

	public Tag buildLevelData() {
		String name = "Some World";
		if(nameField.isEnabled() && nameField.getText() != "") name = (String)nameField.getValue();

		Integer gametype = new Integer("0");
		if(typeField.isEnabled() && !typeField.getText().isEmpty()) {
			gametype = new Integer(typeField.getText());
		}

		Byte mapFeatures;
		if(featureCheck.isSelected()) mapFeatures = new Byte("1");
		else mapFeatures = new Byte("0");

		Long seed = rand.nextLong();
		if(!seedField.getText().isEmpty()) {
			try {
				seed = new Long(seedField.getText());
			} catch (Exception e) {
				seed = new Long((seedField.getText()).hashCode());
			}
		}

		Integer dimension = new Integer("0");
		if(dimensionField.isEnabled() && !dimensionField.getText().isEmpty()) {
			dimension = new Integer(dimensionField.getText());
		}

		int tTime = 0;
		if(!timeField.getText().isEmpty()) tTime = new Integer(timeField.getText()).intValue() % 24000;
		Long time = new Long(tTime);

		Short health = new Short("20");
		if(!healthField.getText().isEmpty()) health = new Short(healthField.getText());

		Short fire = new Short("-20");
		if(!fireField.getText().isEmpty()) fire = new Short(fireField.getText());

		Integer score = new Integer("0");
		if(!scoreField.getText().isEmpty()) {
			score = new Integer(scoreField.getText());
		}

		Byte storming;
		if(stormCheck.isSelected()) storming = new Byte("1");
		else storming = new Byte("0");

		Byte raining;
		if(rainCheck.isSelected()) raining = new Byte("1");
		else raining = new Byte("0");

		Integer rainTime;
		if(!rainField.getText().isEmpty()) {
			rainTime = new Integer(rainField.getText());
		} else rainTime = new Integer("0");

		Integer stormTime;
		if(!stormField.getText().isEmpty()) {
			stormTime = new Integer(stormField.getText());
		} else stormTime = new Integer("0");

		Integer spawnX = new Integer("0");
		Integer spawnY = new Integer("64");
		Integer spawnZ = new Integer("0");
		if(!spawnXField.getText().isEmpty() && !spawnYField.getText().isEmpty() && !spawnZField.getText().isEmpty()) {
			spawnX = new Integer((int)(Double.parseDouble((String)spawnXField.getValue())));
			spawnY = new Integer((int)(Double.parseDouble((String)spawnYField.getValue())));
			spawnZ = new Integer((int)(Double.parseDouble((String)spawnZField.getValue())));
			if(spawnX.intValue() > 0) spawnX = new Integer((int)(spawnX.intValue() + 0.5));
			else spawnX = new Integer((int)(spawnX.intValue() - 0.5));
			if(spawnY.intValue() > 0) spawnY = new Integer((int)(spawnY.intValue() + 0.5));
			else spawnY = new Integer((int)(spawnY.intValue() - 0.5));
			if(spawnZ.intValue() > 0) spawnX = new Integer((int)(spawnX.intValue() + 0.5));
			else spawnZ = new Integer((int)(spawnZ.intValue() - 0.5));
		}

		Double playerX = new Double(spawnX.intValue());
		Double playerY = new Double(spawnY.intValue());
		Double playerZ = new Double(spawnZ.intValue());
		if(!playerXField.getText().isEmpty() && !playerYField.getText().isEmpty() && !playerZField.getText().isEmpty()) {
			playerX = new Double((String)playerXField.getValue());
			playerY = new Double((String)playerYField.getValue());
			playerZ = new Double((String)playerZField.getValue());
		}

		try {
			Tag ncw = Tag.readFrom(new FileInputStream(TeaUtils.getCurrentWorldPath()));

			Tag nCraft = new Tag(Tag.Type.TAG_Compound, "", new Tag[0]);
			Tag nData = new Tag(Tag.Type.TAG_Compound, "Data", new Tag[0]);
			nData.addTag(new Tag(Tag.Type.TAG_Long, "Time", time));
			nData.addTag(cw.findTagByName("LastPlayed"));
			Tag nPlayer = new Tag(Tag.Type.TAG_Compound, "Player", new Tag[0]);
				if(ncw.findTagByName("SleepTimer") != null) nPlayer.addTag(ncw.findTagByName("SleepTimer"));
				nPlayer.addTag(ncw.findTagByName("Motion"));
				nPlayer.addTag(ncw.findTagByName("OnGround"));
				nPlayer.addTag(ncw.findTagByName("HurtTime"));
				nPlayer.addTag(new Tag(Tag.Type.TAG_Short, "Health", health));
				nPlayer.addTag(new Tag(Tag.Type.TAG_Int, "Dimension", dimension));
				nPlayer.addTag(ncw.findTagByName("Air"));
				Tag newInv = parent.getInvTab().buildInventoryData();
				nPlayer.addTag(newInv);
				Tag newPos = new Tag(Tag.Type.TAG_List, "Pos", new Tag[] {
					new Tag(Tag.Type.TAG_Double, null, playerX),
					new Tag(Tag.Type.TAG_Double, null, playerY),
					new Tag(Tag.Type.TAG_Double, null, playerZ)});
				nPlayer.addTag(newPos);
				nPlayer.addTag(ncw.findTagByName("AttackTime"));
				if(ncw.findTagByName("Sleeping") != null) nPlayer.addTag(ncw.findTagByName("Sleeping"));
				nPlayer.addTag(ncw.findTagByName("Fire"));
				nPlayer.addTag(ncw.findTagByName("FallDistance"));
				nPlayer.addTag(ncw.findTagByName("Rotation"));
				nPlayer.addTag(new Tag(Tag.Type.TAG_Int, "Score", score));
				nPlayer.addTag(ncw.findTagByName("DeathTime"));
				nPlayer.addTag(new Tag(Tag.Type.TAG_End, null, null));
			nData.addTag(nPlayer);
			nData.addTag(new Tag(Tag.Type.TAG_Int, "SpawnX", spawnX));
			nData.addTag(new Tag(Tag.Type.TAG_Int, "SpawnY", spawnY));
			if(ncw.findTagByName("raining") != null) nData.addTag(new Tag(Tag.Type.TAG_Byte, "raining", raining));
			if(ncw.findTagByName("rainTime") != null) nData.addTag(new Tag(Tag.Type.TAG_Int, "rainTime", rainTime));
			if(ncw.findTagByName("thundering") != null) nData.addTag(new Tag(Tag.Type.TAG_Byte, "thundering", storming));
			if(ncw.findTagByName("thunderTime") != null) nData.addTag(new Tag(Tag.Type.TAG_Int, "thunderTime", stormTime));
			if(ncw.findTagByName("LevelName") != null) nData.addTag(new Tag(Tag.Type.TAG_String, "LevelName", name));
			nData.addTag(new Tag(Tag.Type.TAG_Int, "SpawnZ", spawnZ));
			nData.addTag(ncw.findTagByName("SizeOnDisk"));
			if(ncw.findTagByName("RandomSeed") != null) nData.addTag(new Tag(Tag.Type.TAG_Long, "RandomSeed", seed));
			if(ncw.findTagByName("version") != null) nData.addTag(ncw.findTagByName("version"));
			if(ncw.findTagByName("GameType") != null) nData.addTag(new Tag(Tag.Type.TAG_Int, "GameType", gametype));
			if(ncw.findTagByName("MapFeatures") != null) nData.addTag(new Tag(Tag.Type.TAG_Byte, "MapFeatures", mapFeatures));
			nData.addTag(new Tag(Tag.Type.TAG_End, null, null));
			nCraft.addTag(nData);
			nCraft.addTag(new Tag(Tag.Type.TAG_End, null, null));
			//parent.loadWorld(TeaUtils.getCurrentWorldPath());
			return nCraft;
		} catch(Exception e) {
			TeaUtils.buildExceptionDialog(e);
			try {
				return Tag.readFrom(new FileInputStream(TeaUtils.getCurrentWorldPath()));
			} catch (Exception ie) {
				return TeaUtils.getCurrentWorld();
			}
		}
	}

	public boolean arrayTagsAreEqual(Tag aPos, Tag bPos) {
		if(aPos == null || bPos == null) return false;
		Tag[] aArray = (Tag[])aPos.getValue();
		Tag[] bArray = (Tag[])bPos.getValue();
		if(aArray.length != bArray.length) return false;
		for(int i=0; i<aArray.length; i++) {
			if(aArray[i].getValue() != bArray[i].getValue()) return false;
		}
		return true;
	}

	public void clearAllFields() {
		nameField.setValue("");
		nameField.setEnabled(true);
		typeField.setValue("");
		typeField.setEnabled(true);
		featureCheck.setEnabled(true);
		seedField.setValue("");
		dimensionField.setValue("");
		dimensionField.setEnabled(true);
		timeField.setValue("");
		timeSlider.setValue(new Integer(0));
		rainField.setValue("");
		stormField.setValue("");
		healthField.setValue("");
		fireField.setValue("");
		scoreField.setValue("");

		playerXField.setValue("");
		playerYField.setValue("");
		playerZField.setValue("");

		spawnXField.setValue("");
		spawnYField.setValue("");
		spawnZField.setValue("");
	}

	public void disableAllFields() {
		nameField.setEnabled(false);
		typeField.setEnabled(false);
		featureCheck.setEnabled(false);
		seedField.setEnabled(false);
		dimensionField.setEnabled(false);
		timeField.setEnabled(false);
		timeSlider.setEnabled(false);
		rainField.setEnabled(false);
		stormField.setEnabled(false);
		healthField.setEnabled(false);
		fireField.setEnabled(false);
		scoreField.setEnabled(false);

		seedButtonRandom.setEnabled(false);
		dimensionButtonHell.setEnabled(false);
		dimensionButtonGaia.setEnabled(false);
		timeButtonDay.setEnabled(false);
		timeButtonNight.setEnabled(false);
		healthButtonFull.setEnabled(false);
		healthButtonGod.setEnabled(false);
		fireButtonHeal.setEnabled(false);
		scoreButtonZero.setEnabled(false);
		stormCheck.setEnabled(false);
		rainCheck.setEnabled(false);
		playerButtonToSpawn.setEnabled(false);
		playerButtonRefresh.setEnabled(false);
		spawnButtonToPlayer.setEnabled(false);
		spawnButtonRefresh.setEnabled(false);

		playerXField.setEnabled(false);
		playerYField.setEnabled(false);
		playerZField.setEnabled(false);

		spawnXField.setEnabled(false);
		spawnYField.setEnabled(false);
		spawnZField.setEnabled(false);
	}

	public void enableAllFields() {
		nameField.setEnabled(true);
		seedField.setEnabled(true);
		dimensionField.setEnabled(true);
		timeField.setEnabled(true);
		timeSlider.setEnabled(true);
		rainField.setEnabled(true);
		stormField.setEnabled(true);
		healthField.setEnabled(true);
		fireField.setEnabled(true);
		scoreField.setEnabled(true);

		seedButtonRandom.setEnabled(true);
		dimensionButtonHell.setEnabled(true);
		dimensionButtonGaia.setEnabled(true);
		timeButtonDay.setEnabled(true);
		timeButtonNight.setEnabled(true);
		healthButtonFull.setEnabled(true);
		healthButtonGod.setEnabled(true);
		fireButtonHeal.setEnabled(true);
		scoreButtonZero.setEnabled(true);
		stormCheck.setEnabled(true);
		rainCheck.setEnabled(true);
		playerButtonToSpawn.setEnabled(true);
		playerButtonRefresh.setEnabled(true);
		spawnButtonToPlayer.setEnabled(true);
		spawnButtonRefresh.setEnabled(true);

		playerXField.setEnabled(true);
		playerYField.setEnabled(true);
		playerZField.setEnabled(true);

		spawnXField.setEnabled(true);
		spawnYField.setEnabled(true);
		spawnZField.setEnabled(true);
	}
}
