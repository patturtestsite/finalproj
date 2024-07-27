package src;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Stack;

/**
 * Officer is a class that manages the drawing application's state and actions.
 * It handles drawing shapes, setting colors, and manages files.
 *
 * @author: Celine Ha
 * @author: Pranay Tiru
 */
public class Officer implements Subject {
	private DrawAction drawAction;
	private Stack<Observer> observers;
	private JPanel drawPanel;
	private Stack<DrawAction> undoStack;
	private Stack<DrawAction> redoStack;
	private boolean isDrawingOutline;
	private int outlineX, outlineY, outlineWidth, outlineHeight;
	private DrawAction selectedShape;
	private DrawAction copyShape;

	public Officer() {
		this.drawAction = new Rectangle(0, 0, 0, 0, Color.BLACK);
		this.observers = new Stack<>();
		this.undoStack = new Stack<>();
		this.redoStack = new Stack<>();
		this.isDrawingOutline = false;
		this.selectedShape = null;
		this.copyShape = null;
	}

	public void setDrawPanel(JPanel panel) {
		this.drawPanel = panel;
		panel.setFocusable(true);
	}

	public void drawOutline(int x, int y, int width, int height) {
		this.outlineX = x;
		this.outlineY = y;
		this.outlineWidth = width;
		this.outlineHeight = height;
		this.isDrawingOutline = true;
		tellYourBoss();
	}

	public void clearOutline() {
		this.isDrawingOutline = false;
		tellYourBoss();
	}

	private DrawAction createDrawAction(DrawAction currAction) {
		return switch (currAction.getShapeName()) {
			case "Rectangle" -> new Rectangle(currAction.getX(), currAction.getY(),
					currAction.getWidth(), currAction.getHeight(), currAction.getColor());
			case "Circle" -> new Circle(currAction.getX(), currAction.getY(),
					currAction.getWidth(), currAction.getHeight(), currAction.getColor());
			case "Arc" -> new Arc(currAction.getX(), currAction.getY(),
					currAction.getWidth(), currAction.getHeight(), currAction.getColor());
			case "Line" -> new Line(currAction.getX(), currAction.getY(),
					currAction.getWidth(), currAction.getHeight(), currAction.getColor());
			default -> null;
		};
	}

	public void performDrawAction() {
		DrawAction action = createDrawAction(drawAction);
		undoStack.push(action);
		redoStack.clear();
		clearOutline();
		tellYourBoss();
	}

	public void clearDrawings() {
		undoStack.clear();
		redoStack.clear();
		tellYourBoss();
	}

	public void saveDrawings() {
		JFileChooser fileChooser = new JFileChooser();
		if (fileChooser.showSaveDialog(drawPanel) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
				out.writeObject(undoStack);
				System.out.println("Drawing saved to " + file.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(drawPanel, "Error saving drawing: " + e.getMessage(),
						"Save Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void loadDrawings() {
		JFileChooser fileChooser = new JFileChooser();
		if (fileChooser.showOpenDialog(drawPanel) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
				undoStack = (Stack<DrawAction>) in.readObject();
				redoStack.clear();
				tellYourBoss();
				System.out.println("Drawing loaded from " + file.getAbsolutePath());
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(drawPanel, "Error loading drawing: " + e.getMessage(),
						"Load Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void undoDrawAction() {
		System.out.println("Undo button clicked!");
		if (!undoStack.isEmpty()) {
			DrawAction action = undoStack.pop();
			redoStack.push(action);
			System.out.println("tell boss");
			tellYourBoss();
		}
	}

	public void redoDrawAction() {
		System.out.println("Redo button clicked!");
		if (!redoStack.isEmpty()) {
			DrawAction action = redoStack.pop();
			undoStack.push(action);
			tellYourBoss();
		}
	}

	public void eraseDrawAction() {
		System.out.println("Erase button clicked!");
		undoStack.clear();
		redoStack.clear();
		System.out.println("tell boss");
		tellYourBoss();
	}

	public void copyDrawAction() {
		if (selectedShape != null) {
			copyShape = createDrawAction(selectedShape);
			copyShape.setX(copyShape.getX() + 10);
			copyShape.setY(copyShape.getY() + 10);
		} else {
			System.out.println("Select a shape!");
		}
	}

	public void pasteDrawAction() {
		if (copyShape != null) {
			DrawAction pasteShape = createDrawAction(copyShape);
			pasteShape.setSelected(false);
			undoStack.push(pasteShape);
			tellYourBoss();
		} else {
			System.out.println("Paste failed!");
		}
	}

	public void tellYourBoss() {
		if (drawPanel != null) {
			drawPanel.repaint();
			notifyObservers();
		}
	}

	public void deselectAll() {
		for (DrawAction d : undoStack) {
			d.setSelected(false);
		}
	}

	public void shapeSelect(DrawAction shape) {
		selectedShape = shape;
		if (shape != null) {
			shape.setSelected(true);
			tellYourBoss();
		} else {
			deselectAll();
			tellYourBoss();
		}
	}

	@Override
	public void addObserver(Observer observer) {
		observers.add(observer);
	}

	@Override
	public void removeObserver(Observer observer) {
		observers.remove(observer);
	}

	@Override
	public void notifyObservers() {
		for (Observer observer : observers) {
			observer.update(undoStack);
		}
	}

	public DrawAction getDrawAction() {
		return drawAction;
	}

	public Stack<DrawAction> getUndoStack() {
		return undoStack;
	}

	public DrawAction getSelectedShape() {
		return selectedShape;
	}

	public boolean isDrawingOutline() {
		return isDrawingOutline;
	}

	public int getOutlineX() {
		return outlineX;
	}

	public int getOutlineY() {
		return outlineY;
	}

	public int getOutlineWidth() {
		return outlineWidth;
	}

	public int getOutlineHeight() {
		return outlineHeight;
	}

	public void updateSelectedShape(DrawAction newShape) {
		if (selectedShape != null) {
			int index = undoStack.indexOf(selectedShape);
			if (index >= 0) {
				undoStack.set(index, newShape);
				selectedShape = newShape;
			}
		}
	}
}
