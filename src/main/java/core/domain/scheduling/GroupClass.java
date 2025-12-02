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
    
    /**
     * Adds an observer to be notified of class events.
     *
     * @param observer the observer to add
     */
    public void addObserver(Observer<Event> observer) {
        eventObservable.addObserver(observer);
    }
    
    /**
     * Removes an observer from the notification list.
     *
     * @param observer the observer to remove
     */
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
    
    /**
     * Cancels the class if it is in a cancellable state.
     * Notifies observers of the cancellation.
     */
    public void cancel() {
        ClassState state = getCurrentState();
        if (state.canCancel()) {
            this.isCancelled = true;
            eventObservable.notifyObservers(new Event(Event.EventType.CLASS_CANCELLED, 
                "Заняття '" + this.name + "' скасовано", this));
        }
    }
    
    /**
     * Gets the current state of the class based on time and cancellation status.
     *
     * @return the current class state
     */
    public ClassState getCurrentState() {
        return ClassStateImpl.getState(startTime, endTime, isCancelled);
    }
    
    /**
     * Checks if the class has been cancelled.
     *
     * @return true if cancelled, false otherwise
     */
    public boolean isCancelled() {
        return isCancelled;
    }

    /**
     * Removes a participant from the class.
     *
     * @param client the client to remove
     */
    public void removeParticipant(Client client) {
        participants.remove(client);
    }

    /**
     * Checks if this class overlaps in time with another class.
     *
     * @param other the other class to check against
     * @return true if the classes overlap in time, false otherwise
     */
    public boolean overlapsWith(GroupClass other) {
        return this.startTime.isBefore(other.endTime) && this.endTime.isAfter(other.startTime);
    }

    /**
     * Gets the unique identifier of the class.
     *
     * @return the class ID
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the name of the class.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the trainer conducting the class.
     *
     * @return the trainer
     */
    public Trainer getTrainer() {
        return trainer;
    }

    /**
     * Gets the studio where the class takes place.
     *
     * @return the studio
     */
    public Studio getStudio() {
        return studio;
    }

    /**
     * Gets the start time of the class.
     *
     * @return the start time
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Gets the end time of the class.
     *
     * @return the end time
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Gets the current number of participants.
     *
     * @return the current size
     */
    public int getCurrentSize() {
        return participants.size();
    }

    /**
     * Gets the maximum capacity of the class.
     *
     * @return the maximum capacity
     */
    public int getMaxCapacity() {
        return maxCapacity;
    }

    /**
     * Gets the list of participants.
     *
     * @return an immutable copy of the participants list
     */
    public List<Client> getParticipants() {
        return List.copyOf(participants);
    }
}