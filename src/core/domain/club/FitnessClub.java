package core.domain.club;

import core.domain.scheduling.Schedule;
import core.domain.shop.Inventory;
import core.domain.staff.Employee;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FitnessClub {
    private final String id;
    private final String address;
    private final Schedule schedule;
    private final Inventory inventory;
    private final List<Employee> staff;
    private final List<Studio> studios;

    public FitnessClub(String address) {
        this.id = UUID.randomUUID().toString();
        this.address = address;
        this.schedule = new Schedule();
        this.inventory = new Inventory();
        this.staff = new ArrayList<>();
        this.studios = new ArrayList<>();
    }

    public void addStaff(Employee employee) {
        this.staff.add(employee);
    }

    public void addStudio(Studio studio) {
        this.studios.add(studio);
    }

    public String getId() {
        return id;
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
}