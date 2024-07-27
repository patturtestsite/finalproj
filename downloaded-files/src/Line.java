package src;

import java.awt.*;

/**
 * Line Shape
 *
 * @author: Tenzin Konchok
 */
public class Line extends Shape {

    public Line(int x, int y, int width, int height, Color color) {
        super("Line", x, y,x + width, y + height, color);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(getColor());
        g.drawLine(getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void drawOutline(Graphics g) {
        g.setColor(Color.BLUE);
        g.drawLine(getX(), getY(), getWidth(), getHeight());
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
        return String.format("<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" stroke=\"%s\"/>",
                getX(), getY(), getX() + getWidth(), getY() + getHeight(), getColorAsString());
    }
}