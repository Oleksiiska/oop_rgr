package core.domain.club;

import core.domain.staff.Cleaner;
import core.util.Constants;
import core.util.ValidationUtils;

import java.util.UUID;

/**
 * Represents a studio within a fitness club.
 * A studio has a capacity and can be cleaned by assigned cleaners.
 */
public class Studio {
    private final String id;
    private String name;
    private final int capacity;
    private boolean cleanliness;

    /**
     * Creates a new studio with the specified name, capacity, and cleanliness status.
     *
     * @param name the name of the studio (must not be null or blank)
     * @param capacity the maximum capacity of the studio (must be positive)
     * @param cleanliness the initial cleanliness status
     * @throws IllegalArgumentException if name is null/blank or capacity is not positive
     */
    public Studio(String name, int capacity, boolean cleanliness) {
        this.id = UUID.randomUUID().toString();
        this.name = ValidationUtils.requireNonBlank(name, Constants.ERROR_STUDIO_NAME_BLANK);
        this.capacity = ValidationUtils.requirePositive(capacity, Constants.ERROR_STUDIO_CAPACITY_INVALID);
        this.cleanliness = cleanliness;
    }

    /**
     * Cleans the studio using the specified cleaner.
     * The cleaner must be assigned to this studio's zone.
     *
     * @param cleaner the cleaner to perform the cleaning (must not be null)
     * @return true if cleaning was successful, false if cleaner is not assigned to this zone
     * @throws IllegalArgumentException if cleaner is null
     */
    public boolean clean(Cleaner cleaner) {
        ValidationUtils.requireNonNull(cleaner, Constants.ERROR_CLEANER_NULL);
        
        if (cleaner.getAssignedZones().contains(this.name)) {
            this.cleanliness = true;
            System.out.println("Прибиральник " + cleaner.getFullName() + " прибрав студію '" + this.name + "'.");
            return true;
        } else {
            System.out.println("Прибиральник " + cleaner.getFullName() + " не призначений для студії '" + this.name + "'.");
            return false;
        }
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isClean() {
        return cleanliness;
    }

    public void setCleanliness(boolean cleanliness) {
        this.cleanliness = cleanliness;
    }

    public void setName(String name) {
        this.name = ValidationUtils.requireNonBlank(name, Constants.ERROR_STUDIO_NAME_BLANK);
    }

    public String getId() {
        return id;
    }
}