package core.domain.scheduling;

import core.domain.client.Client;
import core.domain.club.Studio;
import core.domain.staff.Trainer;
import core.exceptions.BookingException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GroupClass {
    private final String id;
    private final String name;
    private final Trainer trainer;
    private final Studio studio;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final int maxCapacity;
    private final List<Client> participants;

    public GroupClass(String name, Trainer trainer, Studio studio, LocalDateTime startTime, int durationMinutes) {
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("Тривалість заняття повинна бути позитивною");
        }

        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.trainer = trainer;
        this.studio = studio;
        this.startTime = startTime;
        this.endTime = startTime.plusMinutes(durationMinutes);
        this.maxCapacity = studio.getCapacity();
        this.participants = new ArrayList<>();
    }

    public void addParticipant(Client client) throws BookingException {
        if (participants.size() >= maxCapacity) {
            throw new BookingException("Запис неможливий: група '" + this.name + "' заповнена.");
        }

        if (participants.contains(client)) {
            return;
        }

        participants.add(client);
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