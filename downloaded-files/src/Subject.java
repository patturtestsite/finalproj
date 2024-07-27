package src;

/**
 * Subject interface allows for Observer design pattern
 *
 * @author: Pranay Tiru
 */
public interface Subject {
    void addObserver(Observer observer);

    void removeObserver(Observer observer);

    void notifyObservers();
}
