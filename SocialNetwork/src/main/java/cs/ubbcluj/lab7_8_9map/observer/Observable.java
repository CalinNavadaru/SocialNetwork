package cs.ubbcluj.lab7_8_9map.observer;

import cs.ubbcluj.lab7_8_9map.events.Event;

public interface Observable<E extends Event> {
    void addObserver(Observer<E> e);
    void removeObserver(Observer<E> e);
    void notifyObservers(E t);
}
