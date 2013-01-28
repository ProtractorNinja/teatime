/** An editor for the MC map items */

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.border.*;
import java.awt.event.*;
import javax.swing.event.*;

public class TabMapEditor extends JPanel implements ActionListener {
	private TeaTime parent;
	private JPanel chooserPanel;
		private JComboBox mapList;
	private JPanel editorPanel;
		private JPanel scalePanel;
			private JFormattedTextField scaleField;
		private JPanel dimensionPanel;
			private JFormattedTextField dimensionField;
		private JPanel xPanel;
			private JFormattedTextField xField;
		private JPanel zPanel;
			private JFormattedTextField zField;
		private JPanel buttonPanel;
			private JButton centerPlayerButton;
			private JButton centerSpawnButton;
	private JPanel operationsPanel;
		private JButton saveButton;
		private JButton refreshButton;
		private JButton refreshListButton;

	private Tag currentMap;
	private File currentFile;
	private String currentWorldName;

	private MouseAdapter mapMouse;

	public TabMapEditor(TeaTime tt) {
		super();
		parent = tt;
		currentMap = null;
		mapMouse = new MouseAdapter() {
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

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		chooserPanel = buildChooserPanel();
		add(chooserPanel);

		editorPanel = buildEditorPanel();
		add(editorPanel);

		disableFields();
	}

	public JPanel buildChooserPanel() {
		JPanel jp = buildJPanel(null, BoxLayout.Y_AXIS, BorderFactory.createTitledBorder("Choose the map file to edit:"));
		mapList = new JComboBox();
		setAllSize(mapList, new Dimension(parent.getInvTab().getPreferredSize().width-10, 25));
		mapList.setFocusable(false);
		mapList.addActionListener(this);
		jp.add(mapList);
		jp.add(Box.createRigidArea(new Dimension(0,5)));
		return jp;
	}

	// HACK HACK COUGH
	public JPanel buildEditorPanel() {
		Dimension baseDim = new Dimension(parent.getInvTab().getPreferredSize().width-10, 22);
		JPanel ep = buildJPanel(null, BoxLayout.Y_AXIS, BorderFactory.createTitledBorder("Map Data"));

		scalePanel = buildJPanel(baseDim, BoxLayout.X_AXIS, null);
		scaleField = addLabeledField(scalePanel, "Zoom Scale", 80);

		dimensionPanel = buildJPanel(baseDim, BoxLayout.X_AXIS, null);
		dimensionField = addLabeledField(dimensionPanel, "Dimension", 80);

		xPanel = buildJPanel(baseDim, BoxLayout.X_AXIS, null);
		xField = addLabeledField(xPanel, "Center X", 80);

		zPanel = buildJPanel(baseDim, BoxLayout.X_AXIS, null);
		zField = addLabeledField(zPanel, "Center Z", 80);

		buttonPanel = buildJPanel(baseDim, BoxLayout.X_AXIS, null);
		buttonPanel.add(Box.createHorizontalGlue());
		centerPlayerButton = new JButton("Center to Player");
		centerPlayerButton.addActionListener(this);
		centerPlayerButton.setFocusable(false);
		buttonPanel.add(centerPlayerButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(25,0)));
		centerSpawnButton = new JButton("Center to Spawn");
		centerSpawnButton.addActionListener(this);
		centerSpawnButton.setFocusable(false);
		buttonPanel.add(centerSpawnButton);
		buttonPanel.add(Box.createHorizontalGlue());

		operationsPanel = buildOperationsPanel();

		ep.add(scalePanel);
		ep.add(Box.createRigidArea(new Dimension(0,5)));
		ep.add(dimensionPanel);
		ep.add(Box.createRigidArea(new Dimension(0,5)));
		ep.add(xPanel);
		ep.add(Box.createRigidArea(new Dimension(0,5)));
		ep.add(zPanel);
		ep.add(Box.createRigidArea(new Dimension(0,5)));
		ep.add(buttonPanel);
		ep.add(Box.createRigidArea(new Dimension(0,5)));
		ep.add(operationsPanel);

		return ep;
	}

	public JPanel buildOperationsPanel() {
		JPanel op = buildJPanel(null, BoxLayout.X_AXIS, null);

		saveButton = new JButton("Save Map");
		saveButton.addActionListener(this);
		saveButton.setFocusable(false);

		refreshButton = new JButton("Discard Changes");
		refreshButton.addActionListener(this);
		refreshButton.setFocusable(false);

		refreshListButton = new JButton("Refresh List");
		refreshListButton.addActionListener(this);
		refreshListButton.setFocusable(false);

		op.add(Box.createHorizontalGlue());
		op.add(saveButton);
		op.add(Box.createRigidArea(new Dimension(25,0)));
		op.add(refreshButton);
		op.add(Box.createRigidArea(new Dimension(25,0)));
		op.add(refreshListButton);
		op.add(Box.createHorizontalGlue());

		return op;
	}

	public void setAllSize(JComponent comp, Dimension size) {
		comp.setPreferredSize(size);
		comp.setMinimumSize(size);
		comp.setMaximumSize(size);
	}

	public void loadMapList(String p) {
		currentWorldName = p;
		mapList.removeAllItems();
		if(!TeaUtils.mapDirExists(p)) {
			disableFields();
			return;
		}
		enableFields();
		File[] maps = TeaUtils.getMapDataList(p);
		if(maps.length == 0) {
			disableFields();
			return;
		}
		for(File m : maps) {
			mapList.addItem(m);
		}

	}

	public void loadMap(File path) {
		try {
			enableFields();
			Tag t;
			currentMap = Tag.readFrom(new FileInputStream(path));

			t = currentMap.findTagByName("scale");
			scaleField.setValue(""+t.getValue());

			t = currentMap.findTagByName("dimension");
			dimensionField.setValue(""+t.getValue());

			t = currentMap.findTagByName("xCenter");
			xField.setValue(""+t.getValue());

			t = currentMap.findTagByName("zCenter");
			zField.setValue(""+t.getValue());

		} catch (Exception e) {
			TeaUtils.buildExceptionDialog(e);
		}
	}

	public void actionPerformed(ActionEvent e) {
		Tag t;
		Object src = e.getSource();
		if(src == mapList) {
			currentFile = (File)((JComboBox)e.getSource()).getSelectedItem();
			if(currentFile == null) return;
			loadMap(currentFile);
		}
		else if(src == centerPlayerButton) {
			try {
				t = Tag.readFrom(new FileInputStream(TeaUtils.getCurrentWorldPath()));
				Tag[] tt = (Tag[])t.findTagByName("Pos").getValue();
				xField.setValue(""+((Double)tt[0].getValue()).intValue());
				zField.setValue(""+((Double)tt[2].getValue()).intValue());
			} catch (Exception ee) {
				TeaUtils.buildExceptionDialog(ee);
			}
		}
		else if(src == centerSpawnButton) {
			try {
				t = Tag.readFrom(new FileInputStream(TeaUtils.getCurrentWorldPath()));
				xField.setValue(""+t.findTagByName("SpawnX").getValue());
				zField.setValue(""+t.findTagByName("SpawnZ").getValue());
			} catch (Exception ee) {
				TeaUtils.buildExceptionDialog(ee);
			}
		}
		else if(src == saveButton) {
			saveMap();
		}
		else if(src == refreshButton) {
			if(currentFile!=null) loadMap(currentFile);
		}
		else if(src == refreshListButton) {
			if(currentWorldName != null) loadMapList(currentWorldName);
		}
	}

	public JFormattedTextField addLabeledField(Container c, String label, int width) {
        JLabel l = new JLabel(label);
        setAllSize(l, new Dimension(width, c.getPreferredSize().height));
        c.add(l);

        JFormattedTextField field = new JFormattedTextField();
        field.addMouseListener((MouseListener)mapMouse);
        field.setText("");
        l.setLabelFor(field);
        c.add(field);

        return field;
    }

    public void saveMap() {
    	Byte scale, dimension;
    	Integer xCenter, zCenter;

    	if(!scaleField.getText().isEmpty()) scale = new Byte(scaleField.getText());
    	else scale = new Byte("3");

    	if(!dimensionField.getText().isEmpty()) dimension = new Byte(dimensionField.getText());
    	else dimension = new Byte("1");

    	if(!xField.getText().isEmpty()) xCenter = new Integer(xField.getText());
    	else xCenter = (Integer)currentMap.findTagByName("xCenter").getValue();

    	if(!zField.getText().isEmpty()) zCenter = new Integer(zField.getText());
    	else zCenter = (Integer)currentMap.findTagByName("zCenter").getValue();

    	try {
    		Tag ft = new Tag(Tag.Type.TAG_Compound, "", new Tag[0]);
    		Tag nm = new Tag(Tag.Type.TAG_Compound, "data", new Tag[0]);
    		nm.addTag(new Tag(Tag.Type.TAG_Byte, "scale", scale));
    		nm.addTag(new Tag(Tag.Type.TAG_Byte, "dimension", dimension));
    		nm.addTag(currentMap.findTagByName("height"));
    		nm.addTag(currentMap.findTagByName("width"));
    		nm.addTag(new Tag(Tag.Type.TAG_Int, "xCenter", xCenter));
    		nm.addTag(new Tag(Tag.Type.TAG_Int, "zCenter", zCenter));
    		nm.addTag(currentMap.findTagByName("colors"));
    		nm.addTag(new Tag(Tag.Type.TAG_End, null, null));
    		ft.addTag(nm);
    		ft.addTag(new Tag(Tag.Type.TAG_End, null, null));
    		ft.writeTo(new FileOutputStream(currentFile));
    		loadMap(currentFile);
    	} catch (Exception e) {
    		TeaUtils.buildExceptionDialog(e);
    	}
    }

    public JPanel buildJPanel(Dimension d, int boxType, Border bd) {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, boxType));
		setAllSize(p, d);
		p.setBorder(bd);
		return p;
	}

	public void disableFields() {
		mapList.setEnabled(false);

		scaleField.setEnabled(false);
		dimensionField.setEnabled(false);
		xField.setEnabled(false);
		zField.setEnabled(false);

		centerPlayerButton.setEnabled(false);
		centerSpawnButton.setEnabled(false);
		saveButton.setEnabled(false);
		refreshButton.setEnabled(false);
		refreshListButton.setEnabled(false);
	}

	public void enableFields() {
		mapList.setEnabled(true);

		scaleField.setEnabled(true);
		dimensionField.setEnabled(true);
		xField.setEnabled(true);
		zField.setEnabled(true);

		centerPlayerButton.setEnabled(true);
		centerSpawnButton.setEnabled(true);
		saveButton.setEnabled(true);
		refreshButton.setEnabled(true);
		refreshListButton.setEnabled(true);
	}

}