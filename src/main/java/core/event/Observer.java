package core.event;

public interface Observer<T> {
    void update(T event);
}

