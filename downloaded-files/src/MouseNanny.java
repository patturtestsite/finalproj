package src;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * MouseNanny listens for mouse events to facilitate drawing operations.
 * Sends information to Officer for handling drawing actions.
 * @author javiergs
 *
 * @author: Pranay Tiru
 * @author: Tenzin Konchok
 * @version 3.0
 */
public class MouseNanny implements MouseListener, MouseMotionListener {
	private boolean drawingInProgress = false;
	private boolean draggingShape = false;
	private int initialX, initialY;
	private Officer officer;

	public MouseNanny(Officer officer) {
		this.officer = officer;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (!drawingInProgress) {
			if (clickShape(e)) {
				DrawAction selectedShape = officer.getSelectedShape();
				if (selectedShape != null) {
					DrawAction decoratedShape = decorationDetermine(selectedShape);
					if(decoratedShape != null) {
						officer.updateSelectedShape(decoratedShape);
					}
					officer.tellYourBoss();
				}
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		officer.deselectAll();
		if (!drawingInProgress) {
			int x = e.getX();
			int y = e.getY();
			officer.getDrawAction().setX(x);
			officer.getDrawAction().setY(y);
			if (clickShape(e)) {
				initialX = x;
				initialY = y;
				draggingShape = true;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (drawingInProgress) {
			int x = e.getX();
			int y = e.getY();
			int startX = officer.getDrawAction().getX();
			int startY = officer.getDrawAction().getY();
			int width = Math.abs(x - startX);
			int height = Math.abs(y - startY);

			if (x < startX) {
				startX = x;
			}
			if (y < startY) {
				startY = y;
			}

			officer.getDrawAction().setX(startX);
			officer.getDrawAction().setY(startY);
			officer.getDrawAction().setWidth(width);
			officer.getDrawAction().setHeight(height);
			officer.performDrawAction();
			drawingInProgress = false;
		}

		if (draggingShape) {
			draggingShape = false;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// Not used in this implementation
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// Not used in this implementation
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();

		if (draggingShape && officer.getSelectedShape() != null) {
			int deltaX = x - initialX;
			int deltaY = y - initialY;
			officer.getSelectedShape().setX(officer.getSelectedShape().getX() + deltaX);
			officer.getSelectedShape().setY(officer.getSelectedShape().getY() + deltaY);
			initialX = x;
			initialY = y;
			officer.tellYourBoss();
		} else {
			int startX = officer.getDrawAction().getX();
			int startY = officer.getDrawAction().getY();
			int width = Math.abs(x - startX);
			int height = Math.abs(y - startY);

			if (x < startX) {
				startX = x;
			}
			if (y < startY) {
				startY = y;
			}

			drawingInProgress = true;
			officer.drawOutline(startX, startY, width, height);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// Not used in this implementation
	}

	private boolean clickShape(MouseEvent e) {
		boolean selected = false;
		int x = e.getX();
		int y = e.getY();
		for (int i = officer.getUndoStack().size() - 1; i >= 0; i--) {
			DrawAction shape = officer.getUndoStack().get(i);
			if (shape.checkCoordinates(x, y)) {
				officer.shapeSelect(shape);
				selected = true;
				break;
			}
		}
		if (!selected) {
			officer.shapeSelect(null);
		}
		return selected;
	}

	private DrawAction decorationDetermine(DrawAction shape) {
		DrawAction returnShape = null;

		if(shape instanceof ShapeDecorator) {
			if(((ShapeDecorator) shape).getDecoratedShape().getShapeName().equals("Rectangle")
			|| ((ShapeDecorator) shape).getDecoratedShape().getShapeName().equals("Circle")) {
				if(shape instanceof Eyes) {
					returnShape = new Mouth(shape);
				} else if(shape instanceof Mouth) {
					returnShape =  new Hat(shape);
				} else {
					returnShape = ((Hat) shape).getDecoratedShape();
					returnShape = ((Mouth) returnShape).getDecoratedShape();
					returnShape = ((Eyes) returnShape).getDecoratedShape();
				}
			} else if(((ShapeDecorator) shape).getDecoratedShape().getShapeName().equals("Arc")) {
				if(shape instanceof Eyes) {
					returnShape = new Hat(shape);
				} else {
					returnShape = ((Hat) shape).getDecoratedShape();
					returnShape = ((Eyes) returnShape).getDecoratedShape();
				}
			}
		} else {
			if(!(shape instanceof Line)) {
				returnShape = new Eyes(shape);
			}
		}

		return returnShape;
	}
}

