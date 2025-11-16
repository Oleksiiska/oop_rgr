package core.patterns.observer;

import java.util.ArrayList;
import java.util.List;

public class NotificationService implements Observer<Event> {
    private final List<String> notifications;
    
    public NotificationService() {
        this.notifications = new ArrayList<>();
    }
    
    @Override
    public void update(Event event) {
        String notification = formatNotification(event);
        notifications.add(notification);
        System.out.println("🔔 [СПОВІЩЕННЯ] " + notification);
    }
    
    private String formatNotification(Event event) {
        return switch (event.getType()) {
            case CLASS_CANCELLED -> "⚠️ Заняття скасовано: " + event.getMessage();
            case CLASS_FULL -> "📊 Група заповнена: " + event.getMessage();
            case LOW_INVENTORY -> "📦 Низький залишок: " + event.getMessage();
            case MEMBERSHIP_EXPIRED -> "⏰ Абонемент закінчився: " + event.getMessage();
            case BOOKING_CONFIRMED -> "✅ Бронювання підтверджено: " + event.getMessage();
        };
    }
    
    public List<String> getNotifications() {
        return List.copyOf(notifications);
    }
    
    public void clearNotifications() {
        notifications.clear();
    }
}

