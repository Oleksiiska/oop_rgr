package core.patterns.observer;

public interface Observer<T> {
    void update(T event);
}

