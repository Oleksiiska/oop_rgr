package core.event;

import java.time.LocalDateTime;

public class Event {
    public enum EventType {
        CLASS_CANCELLED,
        CLASS_FULL,
        LOW_INVENTORY,
        MEMBERSHIP_EXPIRED,
        BOOKING_CONFIRMED
    }
    
    private final EventType type;
    private final String message;
    private final LocalDateTime timestamp;
    private final Object source;
    
    public Event(EventType type, String message, Object source) {
        this.type = type;
        this.message = message;
        this.source = source;
        this.timestamp = LocalDateTime.now();
    }
    
    public EventType getType() {
        return type;
    }
    
    public String getMessage() {
        return message;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public Object getSource() {
        return source;
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s - %s", timestamp, type, message);
    }
}

