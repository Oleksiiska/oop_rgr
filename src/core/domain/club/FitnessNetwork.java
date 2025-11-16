package core.domain.club;

import java.util.ArrayList;
import java.util.List;

public class FitnessNetwork {
    private static volatile FitnessNetwork instance;
    private static final Object lock = new Object();

    private final String name;
    private final List<FitnessClub> clubs;

    private FitnessNetwork(String name) {
        this.name = name;
        this.clubs = new ArrayList<>();
    }

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

    public String getName() {
        return name;
    }

    public List<FitnessClub> getClubs() {
        return List.copyOf(clubs); /*immutable copy*/
    }

    public void addClub(FitnessClub club) {
        this.clubs.add(club);
    }

    public void removeClub(FitnessClub club) {
        this.clubs.remove(club);
    }
}