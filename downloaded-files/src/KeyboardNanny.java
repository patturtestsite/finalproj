package src;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Allows for user keyboard interaction
 *
 * @author: Celine Ha
 * @author: Pranay Tiru
 */
public class KeyboardNanny implements KeyListener {
    private Officer officer;

    public KeyboardNanny(Officer officer) {
        this.officer = officer;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // No implementation needed
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.isControlDown()) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_C:
                    officer.copyDrawAction();
                    break;
                case KeyEvent.VK_V:
                    officer.pasteDrawAction();
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // No implementation needed
    }
}
