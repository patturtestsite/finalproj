package src;

import java.awt.*;

/**
 * Rectangle Shape
 *
 * @author: Pranay Tiru
 */
public class Rectangle extends Shape {

    public Rectangle(int x, int y, int width, int height, Color color) {
        super("Rectangle", x, y, width, height, color);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(getColor());
        g.fillRect(getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void drawOutline(Graphics g) {
        g.setColor(Color.BLUE);
        g.drawRect(getX() - 2, getY() - 2, getWidth() + 4, getHeight() + 4);
    }

    @Override
    public boolean checkCoordinates(int xClick, int yClick) {
        int lowBoundX = Math.min(getX(), getX() + getWidth());
        int highBoundX = Math.max(getX(), getX() + getWidth());
        int lowBoundY = Math.min(getY(), getY() + getHeight());
        int highBoundY = Math.max(getY(), getY() + getHeight());

        return xClick >= lowBoundX && xClick <= highBoundX && yClick >= lowBoundY && yClick <= highBoundY;
    }

    @Override
    public String toString() {
        return String.format("<rect x=\"%d\" y=\"%d\" width=\"%d\" height=\"%d\" fill=\"%s\"/>",
                getX(), getY(), getWidth(), getHeight(), getColorAsString());
    }

}