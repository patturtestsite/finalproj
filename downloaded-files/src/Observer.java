package src;

import java.util.Stack;

/**
 * Allows for Observer Design Pattern
 *
 * @author: Pranay Tiru
 */
public interface Observer {
    void update(Stack<DrawAction> currentStack);
}
