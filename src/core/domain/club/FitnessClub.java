import java.util.ArrayList;
import java.util.List;

import src.core.domain.staff.Employee;
import src.core.domain.club.Studio;
import src.core.domain.scheduling.Schedule;
import src.core.domain.shop.Inventory;

public class FitnessClub {
    private final String address;
    private final Schedule schedule;
    private final Inventory inventory;
    private final List<Employee> staff;
    private final List<Studio> studios;

    public FitnessClub(String address) {
        this.address = address;
        this.schedule = new Schedule();
        this.inventory = new Inventory();
        this.staff = new ArrayList<>();
    }

    public void addStaff(Employee employee) {
        this.staff.add(employee);
    }

    public void addStudio(Studio studio) {
        this.studios.add(studio);
    }

    public String getAddress() {
        return address;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public List<Employee> getStaff() {
        return List.copyOf(staff);
    }

    public List<Studio> getStudios() {
        return List.copyOf(studios);
}