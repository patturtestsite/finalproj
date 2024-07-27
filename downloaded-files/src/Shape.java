package src;

import java.awt.*;

/**
 * Shape abstract class
 *
 * @author: Pranay Tiru
 */
public abstract class Shape extends DrawAction {
    protected int timesClicked = 0;

    public Shape(String shape, int x, int y, int width, int height, Color color) {
        super(shape, x, y, width, height, color);
    }

    @Override
    public abstract void draw(Graphics g);

    @Override
    public abstract void drawOutline(Graphics g);

    @Override
    public String toString() {
        return super.toString();
    }

    protected String getColorAsString() {
        Color color = getColor();
        return String.format("rgb(%d,%d,%d)", color.getRed(), color.getGreen(), color.getBlue());
    }
}
