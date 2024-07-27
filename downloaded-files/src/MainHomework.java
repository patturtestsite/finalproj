package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * MainHomework creates a frame and adds three panels to it.
 * This version adds MouseListener to the DrawPanel.
 * @author javiergs

 * @author: Celine Ha
 * @author: Tenzin Konchok
 * @author: Pranay Tiru
 * @author: Akshaj Srirambhatla
 * @version 3.0
 */
public class MainHomework extends JFrame {


	public static void main(String[] args) {
		MainHomework app = new MainHomework();
		app.setSize(800, 600);
		app.setTitle("Shape Drawing");
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setResizable(false);
		app.setVisible(true);
	}
	private Officer officer;
	public MainHomework() {
		officer = new Officer();

		JPanel drawPanel = new DrawPanel(officer);
		Observer infoPanel = new InfoPanel();

		officer.addObserver(infoPanel);

		MouseNanny mouseNanny = new MouseNanny(officer);
		KeyboardNanny keyNanny = new KeyboardNanny(officer);
		drawPanel.addMouseListener(mouseNanny);
		drawPanel.addMouseMotionListener(mouseNanny);
		drawPanel.addKeyListener(keyNanny);

		officer.setDrawPanel(drawPanel);
		setLayout(new BorderLayout());
		add(drawPanel, BorderLayout.CENTER);
		add((JPanel) infoPanel, BorderLayout.EAST);

		setJMenuBar(initializeMenus());
	}

	public static ArrayList<JCheckBoxMenuItem> colorCheckBoxes = new ArrayList<>();
	public static ArrayList<JCheckBoxMenuItem> shapeCheckBoxes = new ArrayList<>();

	private JMenuBar initializeMenus() {
		JMenuBar menuBar = new JMenuBar();
		ActionNanny actionNanny = new ActionNanny(officer);

		JMenu FileMenu = getFilejMenu();

		JMenu shapeMenu = getShapejMenu(actionNanny);

		JMenu editMenu = getEditjMenu();

		JMenu colorMenu = getColorjMenu(actionNanny);

		JMenuItem AboutItem = new JMenuItem("About");

		AboutItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showAboutDialog();
			}
		});

		menuBar.add(FileMenu);
		menuBar.add(shapeMenu);
		menuBar.add(colorMenu);
		menuBar.add(editMenu);
		menuBar.add(AboutItem);

		return menuBar;
	}

	private JMenu getFilejMenu() {
		JMenu FileMenu = new JMenu("File");
		JMenuItem NewItem = new JMenuItem("New");
		JMenuItem SaveItem = new JMenuItem("Save");
		JMenuItem LoadItem = new JMenuItem("Load");
		FileMenu.add(NewItem);
		FileMenu.add(SaveItem);
		FileMenu.add(LoadItem);

		NewItem.addActionListener(e -> {
			officer.clearDrawings();
		});

		SaveItem.addActionListener(e -> {
			officer.saveDrawings();
		});

		LoadItem.addActionListener(e -> {
            officer.loadDrawings();
		});

		return FileMenu;
	}

	private static JMenu getShapejMenu(ActionNanny a) {
		JMenu shapeMenu = new JMenu("Shape");
		String[] shapes = {"Line", "Rectangle", "Circle", "Arc"};


		for (String shape : shapes) {
			JCheckBoxMenuItem shapeItem = new JCheckBoxMenuItem(shape);
			if (shape.equals("Rectangle")) {
				shapeItem.setSelected(true);
			}
			shapeItem.addActionListener(a);
			shapeMenu.add(shapeItem);
			shapeCheckBoxes.add(shapeItem);
		}
		return shapeMenu;
	}

	private static JMenu getColorjMenu(ActionNanny a) {
		JMenu colorMenu = new JMenu("Color");
		String[] colors = {"Black", "Red", "Blue", "Green", "Yellow", "Orange", "Pink"};
		for (String color : colors) {
			JCheckBoxMenuItem colorItem = new JCheckBoxMenuItem(color);
			if (color.equals("Black")) {
				colorItem.setSelected(true);
			}
			colorItem.addActionListener(a);
			colorMenu.add(colorItem);
			colorCheckBoxes.add(colorItem);
		}

		return colorMenu;
	}

	private JMenu getEditjMenu() {
		JMenu editMenu = new JMenu("Edit");
		JMenuItem copyItem = new JMenuItem("Copy");
		JMenuItem pasteItem = new JMenuItem("Paste");
		JMenuItem undoItem = new JMenuItem("Undo");
		JMenuItem redoItem = new JMenuItem("Redo");
		JMenuItem eraseItem = new JMenuItem("Erase");

		undoItem.addActionListener(e -> {
			officer.undoDrawAction();
		});

		redoItem.addActionListener(e -> {
			officer.redoDrawAction();
		});

		eraseItem.addActionListener(e -> {
			officer.eraseDrawAction();
		});

		copyItem.addActionListener(e -> {
			officer.copyDrawAction();
		});

		pasteItem.addActionListener(e -> {
			officer.pasteDrawAction();
		});


		editMenu.add(undoItem);
		editMenu.add(redoItem);
		editMenu.add(copyItem);
		editMenu.add(pasteItem);
		editMenu.add(eraseItem);

		return editMenu;
	}

	private void showAboutDialog() {
		    JDialog aboutDialog = new JDialog();
			aboutDialog = new JDialog(this, "About", true);
			aboutDialog.setLayout(new BorderLayout());
			aboutDialog.setResizable(false);

			ImageIcon logoIcon = new ImageIcon("logo.png");
			Image scaledLogo = logoIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
			JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
			logoLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			aboutDialog.add(logoLabel, BorderLayout.WEST);

			JPanel infoPanel = new JPanel(new BorderLayout());

			Font playfulFont = new Font("Comic Sans MS", Font.BOLD, 14);

			JLabel membersLabel = new JLabel("Team Members:");
			membersLabel.setFont(playfulFont);

			JPanel membersPanel = new JPanel(new BorderLayout());
			membersPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		    JPanel namesPanel = new JPanel(new GridLayout(4, 1, 5, 5));
			namesPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

			namesPanel.add(new JLabel("Celine Ha"));
			namesPanel.add(new JLabel("Tenzin Konchok"));
			namesPanel.add(new JLabel("Pranay Tiru"));
			namesPanel.add(new JLabel("Akshaj Srirambhatla"));

		    membersPanel.add(membersLabel, BorderLayout.NORTH);
			membersPanel.add(namesPanel, BorderLayout.CENTER);

			infoPanel.add(membersPanel, BorderLayout.NORTH);

			JLabel versionLabel = new JLabel("Version: 3.0");
			versionLabel.setFont(playfulFont);
			versionLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			infoPanel.add(versionLabel, BorderLayout.SOUTH);

			aboutDialog.add(infoPanel, BorderLayout.CENTER);
			aboutDialog.setSize(400, 300);
			aboutDialog.setLocationRelativeTo(this);
			aboutDialog.setVisible(true);
	}

	public static void clearCheckBoxes(ArrayList<JCheckBoxMenuItem> arrayCB, JCheckBoxMenuItem cb){
		for (JCheckBoxMenuItem arrayCB1 : arrayCB) {
            arrayCB1.setSelected(arrayCB1 == cb);
		}
	}

}
