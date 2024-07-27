package src;

import java.awt.*;

/**
 * Mouth Decorator
 *
 * @author: Pranay Tiru
 */
public class Mouth extends ShapeDecorator {
    private int mouthX;
    private int mouthY;

    public Mouth(DrawAction decoratedShape) {
        super(decoratedShape);
        updateMouthPositions();
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        addOpenMouth(g);
    }

    @Override
    public void setX(int x) {
        decoratedShape.setX(x);
        updateMouthPositions();
    }

    @Override
    public void setY(int y) {
        decoratedShape.setY(y);
        updateMouthPositions();
    }

    private void updateMouthPositions() {
        mouthX = decoratedShape.getX() + decoratedShape.getWidth() / 4;
        mouthY = decoratedShape.getY() + 3 * decoratedShape.getHeight() / 4 - decoratedShape.getHeight() / 5;
    }

    private void addOpenMouth(Graphics g) {
        int mouthWidth = width / 2;
        int mouthHeight = height / 5;

        g.setColor(Color.RED);
        g.fillArc(mouthX, mouthY, mouthWidth, mouthHeight, 0, -180);
    }
}
