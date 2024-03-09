package cs.ubbcluj.lab7_8_9map.observer;

import cs.ubbcluj.lab7_8_9map.events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}
