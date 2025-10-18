package core.domain.staff;

import java.util.ArrayList;
import java.util.List;

public class Cleaner extends Employee {
    private final List<String> assignedZones;

    public Cleaner(String fullName, double salary, List<String> assignedZones) {
        super(fullName, salary);
        this.assignedZones = new ArrayList<>(assignedZones);
    }

    @Override
    public String getJobTitle() {
        return "Прибиральник";
    }

    public List<String> getAssignedZones() {
        return List.copyOf(assignedZones);
    }

    public void addAssignedZone(String zone) {
        this.assignedZones.add(zone);
    }

    public void removeAssignedZone(String zone) {
        this.assignedZones.remove(zone);
    }
}