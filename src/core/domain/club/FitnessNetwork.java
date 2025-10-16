import java.util.ArrayList;
import java.util.List;

public class FitnessNetwork {
    private final String name;
    private final List<FitnessClub> clubs;

    public FitnessNetwork(String name) {
        this.name = name;
        this.clubs = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<FitnessClub> getClubs() {
        return List.copyOf(clubs); /*unmutable copy*/
    }

    public void addClub(FitnessClub club) {
        this.clubs.add(club);
    }

    public void removeClub(FitnessClub club) {
        this.clubs.remove(club);
    }
}