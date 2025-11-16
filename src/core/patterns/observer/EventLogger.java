package core.patterns.observer;

import java.util.ArrayList;
import java.util.List;

public class EventLogger implements Observer<Event> {
    private final List<Event> loggedEvents;
    
    public EventLogger() {
        this.loggedEvents = new ArrayList<>();
    }
    
    @Override
    public void update(Event event) {
        loggedEvents.add(event);
        System.out.println("📋 [ЛОГ] " + event);
    }
    
    public List<Event> getLoggedEvents() {
        return List.copyOf(loggedEvents);
    }
    
    public void clearLog() {
        loggedEvents.clear();
    }
    
    public int getEventCount() {
        return loggedEvents.size();
    }
}

