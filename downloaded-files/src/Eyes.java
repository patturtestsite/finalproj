package src;

import java.awt.*;

/**
 * Eyes Decorator
 *
 * @author: Pranay Tiru
 */
public class Eyes extends ShapeDecorator {
    private int leftEyeX;
    private int rightEyeX;
    private int eyeY;
    public Eyes(DrawAction decoratedShape) {
        super(decoratedShape);
        updateEyePositions();
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        addEyes(g);
    }

    @Override
    public void setX(int x) {
        decoratedShape.setX(x);
        updateEyePositions();
    }

    @Override
    public void setY(int y) {
        decoratedShape.setY(y);
        updateEyePositions();
    }

    private void updateEyePositions() {
        leftEyeX = decoratedShape.getX() + decoratedShape.getWidth() / 4;
        rightEyeX = decoratedShape.getX() + 3 * decoratedShape.getWidth() / 4 - decoratedShape.getWidth() / 5;
        eyeY = decoratedShape.getY() + decoratedShape.getHeight() / 4;
    }

    private void addEyes(Graphics g) {
        int eyeWidth = width / 5;
        int eyeHeight = height / 5;

        g.setColor(Color.WHITE);
        g.fillOval(leftEyeX, eyeY, eyeWidth, eyeHeight);
        g.fillOval(rightEyeX, eyeY, eyeWidth, eyeHeight);

        g.setColor(Color.BLACK);
        g.fillOval(leftEyeX + eyeWidth / 4, eyeY + eyeHeight / 4, eyeWidth / 2, eyeHeight / 2);
        g.fillOval(rightEyeX + eyeWidth / 4, eyeY + eyeHeight / 4, eyeWidth / 2, eyeHeight / 2);
    }
}
