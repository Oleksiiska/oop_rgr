package core.domain.staff;

import core.util.Constants;
import core.util.ValidationUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a cleaner who maintains cleanliness in assigned zones of the fitness club.
 */
public class Cleaner extends Employee {
    private final List<String> assignedZones;

    /**
     * Creates a new cleaner with the specified name, salary, and assigned zones.
     *
     * @param fullName the full name of the cleaner (must not be null or blank)
     * @param salary the salary of the cleaner (must be non-negative)
     * @param assignedZones the list of zones assigned to this cleaner (must not be null)
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public Cleaner(String fullName, double salary, List<String> assignedZones) {
        super(fullName, salary);
        ValidationUtils.requireNonNull(assignedZones, "Список зон не може бути null.");
        this.assignedZones = new ArrayList<>(assignedZones);
    }

    /**
     * Gets the job title of the cleaner.
     *
     * @return "Прибиральник"
     */
    @Override
    public String getJobTitle() {
        return Constants.JOB_TITLE_CLEANER;
    }

    /**
     * Gets the list of zones assigned to this cleaner.
     *
     * @return an immutable copy of the assigned zones list
     */
    public List<String> getAssignedZones() {
        return List.copyOf(assignedZones);
    }

    /**
     * Adds a zone to the cleaner's assigned zones.
     *
     * @param zone the zone to add (must not be null or blank)
     * @throws IllegalArgumentException if zone is null or blank
     */
    public void addAssignedZone(String zone) {
        ValidationUtils.requireNonBlank(zone, "Назва зони не може бути порожньою.");
        this.assignedZones.add(zone);
    }

    /**
     * Removes a zone from the cleaner's assigned zones.
     *
     * @param zone the zone to remove
     */
    public void removeAssignedZone(String zone) {
        this.assignedZones.remove(zone);
    }
}