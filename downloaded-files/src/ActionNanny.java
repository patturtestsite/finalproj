package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ActionNanny listens for action events.
 * Sends information to Officer.
 * @version 3.0
 * @author: Tenzin Konchok
 */
public class ActionNanny implements ActionListener {
	private Officer officer;

	public ActionNanny(Officer officer) {
		this.officer = officer;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JCheckBoxMenuItem) {
			String command = e.getActionCommand();
			switch (command) {
				case "Black":
					colorActionCommand(Color.BLACK, e);
					break;
				case "Red":
					colorActionCommand(Color.RED, e);
					break;
				case "Blue":
					colorActionCommand(Color.BLUE, e);
					break;
				case "Green":
					colorActionCommand(Color.GREEN, e);
					break;
				case "Yellow":
					colorActionCommand(Color.YELLOW, e);
					break;
				case "Orange":
					colorActionCommand(Color.ORANGE, e);
					break;
				case "Pink":
					colorActionCommand(Color.PINK, e);
					break;
				default:
					officer.getDrawAction().setShapeName(command);
					MainHomework.clearCheckBoxes(MainHomework.shapeCheckBoxes, (JCheckBoxMenuItem) e.getSource());
					break;
			}
		}
	}

	public void colorActionCommand(Color color, ActionEvent e) {
		officer.getDrawAction().setColor(color);
		MainHomework.clearCheckBoxes(MainHomework.colorCheckBoxes, (JCheckBoxMenuItem) e.getSource());
		if (officer.getSelectedShape() != null) {
			for (DrawAction d : officer.getUndoStack()) {
				if (d.isSelected()) {
					d.setColor(color);
					officer.tellYourBoss();
				}
			}
		}
	}
}