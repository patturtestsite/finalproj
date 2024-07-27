package src;

import java.awt.*;

/**
 * Arc Decorator
 *
 * @author: Pranay Tiru
 */
public class Arc extends Shape {

    public Arc(int x, int y, int width, int height, Color color) {
        super("Arc", x, y, width, height, color);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(getColor());
        g.fillArc(getX(), getY(), getWidth(), getHeight(), 0, 180);
    }

    @Override
    public void drawOutline(Graphics g) {
        g.setColor(Color.BLUE);
        g.drawArc(getX() - 2, getY() - 2, getWidth() + 4, getHeight() + 4, 0, 180);
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
        return String.format("<path d=\"M %d %d A %d %d 0 0 1 %d %d\" fill=\"none\" stroke=\"%s\"/>",
                getX(), getY(), getWidth() / 2, getHeight() / 2, getX() + getWidth(), getY() + getHeight(), getColorAsString());
    }
}
