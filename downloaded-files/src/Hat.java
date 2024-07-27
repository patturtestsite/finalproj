package src;

import java.awt.*;

/**
 * Hat Decorator
 *
 * @author: Pranay Tiru
 */
public class Hat extends ShapeDecorator {
    private int hatX;
    private int hatY;
    private int brimX;
    private int brimY;

    public Hat(DrawAction decoratedShape) {
        super(decoratedShape);
        updateHatPositions();
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        addTopHat(g);
    }

    @Override
    public void setX(int x) {
        decoratedShape.setX(x);
        updateHatPositions();
    }

    @Override
    public void setY(int y) {
        decoratedShape.setY(y);
        updateHatPositions();
    }

    private void updateHatPositions() {
        hatX = decoratedShape.getX();
        hatY = decoratedShape.getY() - decoratedShape.getHeight() / 2;
        brimX = decoratedShape.getX() - decoratedShape.getWidth() / 2;
        brimY = decoratedShape.getY();
    }

    private void addTopHat(Graphics g) {
        int hatWidth = width;
        int hatHeight = height / 2;

        g.setColor(Color.BLACK);
        g.fillRect(hatX, hatY, hatWidth, hatHeight);

        int brimWidth = width * 2;
        int brimHeight = height / 8;

        g.fillRect(brimX, brimY, brimWidth, brimHeight);
    }
}

