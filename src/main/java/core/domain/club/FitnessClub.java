package core.domain.club;

import core.domain.scheduling.Schedule;
import core.domain.shop.Inventory;
import core.domain.staff.Employee;
import core.domain.staff.EmployeeOperation;
import core.util.Constants;
import core.util.ValidationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a fitness club with studios, staff, schedule, and inventory.
 * Each club has a unique ID and address, and manages its own resources.
 */
public class FitnessClub {
    private final String id;
    private final String address;
    private final Schedule schedule;
    private final Inventory inventory;
    private final List<EmployeeOperation> staff;  // Using interface for low coupling
    private final List<Studio> studios;

    public FitnessClub(String address) {
        this.id = UUID.randomUUID().toString();
        this.address = ValidationUtils.requireNonBlank(address, "Адреса клубу не може бути порожньою.");
        this.schedule = new Schedule();
        this.inventory = new Inventory();
        this.staff = new ArrayList<>();
        this.studios = new ArrayList<>();
    }

    public void addStaff(Employee employee) {
        ValidationUtils.requireNonNull(employee, Constants.ERROR_EMPLOYEE_NULL);
        this.staff.add(employee);
    }

    public void addStudio(Studio studio) {
        ValidationUtils.requireNonNull(studio, "Студія не може бути null.");
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

    public List<EmployeeOperation> getStaff() {
        return List.copyOf(staff);
    }

    public List<Studio> getStudios() {
        return List.copyOf(studios);
    }
}