package core.domain.club;

import core.util.ValidationUtils;
import core.util.Constants;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a network of fitness clubs.
 * Uses the Singleton pattern to ensure only one network instance exists.
 */
public class FitnessNetwork {
    private static volatile FitnessNetwork instance;
    private static final Object lock = new Object();

    private final String name;
    private final List<FitnessClub> clubs;

    /**
     * Private constructor for singleton pattern.
     *
     * @param name the name of the network (must not be null or blank)
     * @throws IllegalArgumentException if name is null or blank
     */
    private FitnessNetwork(String name) {
        this.name = ValidationUtils.requireNonBlank(name, "Назва мережі не може бути порожньою.");
        this.clubs = new ArrayList<>();
    }

    /**
     * Gets the singleton instance of the fitness network.
     * Creates a new instance if one doesn't exist (double-checked locking).
     *
     * @param name the name of the network (used only on first creation)
     * @return the singleton instance
     * @throws IllegalArgumentException if name is null or blank on first creation
     */
    public static FitnessNetwork getInstance(String name) {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new FitnessNetwork(name);
                }
            }
        }
        return instance;
    }

    /**
     * Gets the name of the network.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the list of clubs in the network.
     *
     * @return an immutable copy of the clubs list
     */
    public List<FitnessClub> getClubs() {
        return List.copyOf(clubs);
    }

    /**
     * Adds a club to the network.
     *
     * @param club the club to add (must not be null)
     * @throws IllegalArgumentException if club is null
     */
    public void addClub(FitnessClub club) {
        ValidationUtils.requireNonNull(club, Constants.ERROR_CLUB_NULL);
        this.clubs.add(club);
    }

    /**
     * Removes a club from the network.
     *
     * @param club the club to remove (must not be null)
     * @throws IllegalArgumentException if club is null
     */
    public void removeClub(FitnessClub club) {
        ValidationUtils.requireNonNull(club, Constants.ERROR_CLUB_NULL);
        this.clubs.remove(club);
    }
}