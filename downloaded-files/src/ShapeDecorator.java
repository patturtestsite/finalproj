package src;

import java.awt.*;

/**
 * Shape Decorator Abstract Class
 *
 * @author: Pranay Tiru
 */
public abstract class ShapeDecorator extends DrawAction {
    protected DrawAction decoratedShape;

    public ShapeDecorator(DrawAction decoratedShape) {
        super(decoratedShape.getShapeName(), decoratedShape.getX(), decoratedShape.getY(),
                decoratedShape.getWidth(), decoratedShape.getHeight(), decoratedShape.getColor());
        this.decoratedShape = decoratedShape;
    }

    public DrawAction getDecoratedShape() {
        return decoratedShape;
    }

    @Override
    public void draw(Graphics g) {
        decoratedShape.draw(g);
    }

    @Override
    public void drawOutline(Graphics g) {
        decoratedShape.drawOutline(g);
    }

    @Override
    public String toString() {
        return decoratedShape.toString();
    }

    @Override
    public int getX() {
        return decoratedShape.getX();
    }

    @Override
    public int getY() {
        return decoratedShape.getY();
    }

    @Override
    public int getWidth() {
        return decoratedShape.getWidth();
    }

    @Override
    public void setWidth(int width) {
        decoratedShape.setWidth(width);
    }

    @Override
    public int getHeight() {
        return decoratedShape.getHeight();
    }

    @Override
    public void setHeight(int height) {
        decoratedShape.setHeight(height);
    }

    @Override
    public Color getColor() { return decoratedShape.getColor(); }

    @Override
    public void setColor(Color color) {
        decoratedShape.setColor(color);
    }

    @Override
    public boolean isSelected() { return decoratedShape.isSelected(); }

    @Override
    public void setSelected(boolean selected) {
        decoratedShape.setSelected(selected);
    }

    @Override
    public String getShapeName() {
        return decoratedShape.getShapeName();
    }

    @Override
    public void setShapeName(String shape) {
        decoratedShape.setShapeName(shape);
    }

    @Override
    public boolean checkCoordinates(int xClick, int yClick) {
        return decoratedShape.checkCoordinates(xClick, yClick);
    }

}

