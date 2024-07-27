package src;

import java.awt.*;

/**
 * Circle Shape
 *
 * @author: Pranay Tiru
 */
public class Circle extends Shape {

    public Circle(int x, int y, int width, int height, Color color) {
        super("Circle", x, y, width, height, color);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(getColor());
        g.fillOval(getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void drawOutline(Graphics g) {
        g.setColor(Color.BLUE);
        g.drawOval(getX() - 2, getY() - 2, getWidth() + 4, getHeight() + 4);
    }

    @Override
    public boolean checkCoordinates(int xClick, int yClick) {
        int radius = getWidth() / 2;
        int centerX = getX() + radius;
        int centerY = getY() + radius;
        double distance = Math.sqrt(Math.pow(centerX - xClick, 2) + Math.pow(centerY - yClick, 2));
        return distance <= radius;
    }

    @Override
    public String toString() {
        return String.format("<circle cx=\"%d\" cy=\"%d\" r=\"%d\" fill=\"%s\"/>",
                getX() + getWidth() / 2, getY() + getHeight() / 2, getWidth() / 2, getColorAsString());
    }

}
