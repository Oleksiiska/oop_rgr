package core.event;

import java.util.ArrayList;
import java.util.List;

public class Observable<T> {
    private final List<Observer<T>> observers = new ArrayList<>();
    
    public void addObserver(Observer<T> observer) {
        if (observer == null) {
            throw new IllegalArgumentException("Observer cannot be null");
        }
        observers.add(observer);
    }
    
    public void removeObserver(Observer<T> observer) {
        observers.remove(observer);
    }
    
    public void notifyObservers(T event) {
        for (Observer<T> observer : observers) {
            observer.update(event);
        }
    }
    
    public int getObserverCount() {
        return observers.size();
    }
}

