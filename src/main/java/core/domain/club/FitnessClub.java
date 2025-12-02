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

    /**
     * Creates a new fitness club with the specified address.
     *
     * @param address the address of the club (must not be null or blank)
     * @throws IllegalArgumentException if address is null or blank
     */
    public FitnessClub(String address) {
        this.id = UUID.randomUUID().toString();
        this.address = ValidationUtils.requireNonBlank(address, "Адреса клубу не може бути порожньою.");
        this.schedule = new Schedule();
        this.inventory = new Inventory();
        this.staff = new ArrayList<>();
        this.studios = new ArrayList<>();
    }

    /**
     * Adds a staff member to the club.
     *
     * @param employee the employee to add (must not be null)
     * @throws IllegalArgumentException if employee is null
     */
    public void addStaff(Employee employee) {
        ValidationUtils.requireNonNull(employee, Constants.ERROR_EMPLOYEE_NULL);
        this.staff.add(employee);
    }

    /**
     * Adds a studio to the club.
     *
     * @param studio the studio to add (must not be null)
     * @throws IllegalArgumentException if studio is null
     */
    public void addStudio(Studio studio) {
        ValidationUtils.requireNonNull(studio, "Студія не може бути null.");
        this.studios.add(studio);
    }

    /**
     * Gets the unique identifier of the club.
     *
     * @return the club ID
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the address of the club.
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Gets the schedule of the club.
     *
     * @return the schedule
     */
    public Schedule getSchedule() {
        return schedule;
    }

    /**
     * Gets the inventory of the club.
     *
     * @return the inventory
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Gets the list of staff members.
     *
     * @return an immutable copy of the staff list
     */
    public List<EmployeeOperation> getStaff() {
        return List.copyOf(staff);
    }

    /**
     * Gets the list of studios.
     *
     * @return an immutable copy of the studios list
     */
    public List<Studio> getStudios() {
        return List.copyOf(studios);
    }
}