package src;

import java.awt.*;

/**
 * Abstract class that represents a shape/decorator
 *
 * @author: Pranay Tiru
 * @author: Celine Ha
 */
public abstract class DrawAction {
    String shape;
    protected int x, y, width, height;
    protected Color color;
    protected boolean selected;

    public DrawAction(String shape, int x, int y, int width, int height, Color color) {
        this.shape = shape;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.selected = false;
    }

    public abstract void draw(Graphics g);

    public abstract void drawOutline(Graphics g);

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    public abstract boolean checkCoordinates(int xClick, int yClick);

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getShapeName() {
        return shape;
    }

    public void setShapeName(String shape) {
        this.shape = shape;
    }
}
