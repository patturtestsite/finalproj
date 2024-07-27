package src;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * DrawPanel creates a panel where the drawing is done.
 * According to the data in Officer.
 *
 * @author: Celine Ha
 * @author: Pranay Tiru
 */
public class DrawPanel extends JPanel {
	private Officer officer;

	public DrawPanel(Officer officer) {
		this.officer = officer;
		setBackground(new Color(176, 250, 192));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		ArrayList<DrawAction> list = new ArrayList<>(officer.getUndoStack());

		for (DrawAction action : list) {
			if (action.isSelected()) {
				action.drawOutline(g);
			}
			action.draw(g);
		}

		if (officer.isDrawingOutline()) {
			g.setColor(Color.BLACK);
			switch (officer.getDrawAction().getShapeName()) {
				case "Rectangle":
					g.drawRect(officer.getOutlineX(), officer.getOutlineY(), officer.getOutlineWidth(), officer.getOutlineHeight());
					break;
				case "Circle":
					g.drawOval(officer.getOutlineX(), officer.getOutlineY(), officer.getOutlineWidth(), officer.getOutlineHeight());
					break;
				case "Arc":
					g.drawArc(officer.getOutlineX(), officer.getOutlineY(), officer.getOutlineWidth(), officer.getOutlineHeight(), 0, 180);
					break;
				case "Line":
					g.drawLine(officer.getOutlineX(), officer.getOutlineY(), officer.getOutlineX() + officer.getOutlineWidth(), officer.getOutlineY() + officer.getOutlineHeight());
					break;
			}
		}
	}
}
