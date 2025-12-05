package core.domain.scheduling;

import core.domain.client.Client;
import core.domain.club.Studio;
import core.domain.staff.Trainer;
import core.event.Observer;
import core.exceptions.BookingException;
import core.event.Event;
import core.event.Observable;
import core.util.Constants;
import core.util.ValidationUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a group fitness class scheduled at a specific time and studio.
 * Supports participant management and state tracking (scheduled, in progress, completed, cancelled).
 */
public class GroupClass {
    private final String id;
    private final String name;
    private final Trainer trainer;
    private final Studio studio;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final int maxCapacity;
    private final List<Client> participants;
    private boolean isCancelled;
    private final Observable<Event> eventObservable;

    /**
     * Creates a new group class with the specified parameters.
     *
     * @param name the name of the class (must not be null or blank)
     * @param trainer the trainer conducting the class (must not be null)
     * @param studio the studio where the class takes place (must not be null)
     * @param startTime the start time of the class (must not be null)
     * @param durationMinutes the duration of the class in minutes (must be positive)
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public GroupClass(String name, Trainer trainer, Studio studio, LocalDateTime startTime, int durationMinutes) {
        ValidationUtils.requirePositive(durationMinutes, Constants.ERROR_CLASS_DURATION_INVALID);
        ValidationUtils.requireNonBlank(name, "Назва заняття не може бути порожньою.");
        ValidationUtils.requireNonNull(trainer, "Тренер не може бути null.");
        ValidationUtils.requireNonNull(studio, "Студія не може бути null.");
        ValidationUtils.requireNonNull(startTime, "Час початку не може бути null.");

        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.trainer = trainer;
        this.studio = studio;
        this.startTime = startTime;
        this.endTime = startTime.plusMinutes(durationMinutes);
        this.maxCapacity = studio.getCapacity();
        this.participants = new ArrayList<>();
        this.isCancelled = false;
        this.eventObservable = new Observable<>();
    }
    
    public void addObserver(Observer<Event> observer) {
        eventObservable.addObserver(observer);
    }
    
    public void removeObserver(Observer<Event> observer) {
        eventObservable.removeObserver(observer);
    }

    /**
     * Adds a participant to the class.
     * Validates class state and capacity before adding.
     *
     * @param client the client to add as a participant (must not be null)
     * @throws BookingException if the class cannot accept new participants
     */
    public void addParticipant(Client client) throws BookingException {
        ValidationUtils.requireNonNull(client, "Клієнт не може бути null.");
        ClassState state = getCurrentState();
        
        if (!state.canAddParticipant()) {
            throw new BookingException("Запис неможливий: заняття в стані '" + state.getStatusDescription() + "'.");
        }
        
        if (participants.size() >= maxCapacity) {
            eventObservable.notifyObservers(new Event(Event.EventType.CLASS_FULL, 
                "Група '" + this.name + "' заповнена", this));
            throw new BookingException("Запис неможливий: група '" + this.name + "' заповнена.");
        }

        if (participants.contains(client)) {
            return;
        }

        participants.add(client);
        eventObservable.notifyObservers(new Event(Event.EventType.BOOKING_CONFIRMED, 
            "Клієнт записаний на заняття '" + this.name + "'", this));
    }
    
    public void cancel() {
        ClassState state = getCurrentState();
        if (state.canCancel()) {
            this.isCancelled = true;
            eventObservable.notifyObservers(new Event(Event.EventType.CLASS_CANCELLED, 
                "Заняття '" + this.name + "' скасовано", this));
        }
    }
    
    public ClassState getCurrentState() {
        return ClassStateImpl.getState(startTime, endTime, isCancelled);
    }
    
    public boolean isCancelled() {
        return isCancelled;
    }

    public void removeParticipant(Client client) {
        participants.remove(client);
    }

    public boolean overlapsWith(GroupClass other) {
        return this.startTime.isBefore(other.endTime) && this.endTime.isAfter(other.startTime);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public Studio getStudio() {
        return studio;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public int getCurrentSize() {
        return participants.size();
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public List<Client> getParticipants() {
        return List.copyOf(participants);
    }
}