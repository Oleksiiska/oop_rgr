package core.domain.scheduling;

import core.domain.club.Studio;
import core.util.ValidationUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manages the schedule of group classes for a fitness club.
 * Organizes classes by date and prevents scheduling conflicts in the same studio.
 */
public class Schedule {

    private final Map<LocalDate, List<GroupClass>> classesByDate;

    /**
     * Creates a new empty schedule.
     */
    public Schedule() {
        this.classesByDate = new HashMap<>();
    }

    /**
     * Adds a class to the schedule.
     * Checks for time conflicts in the same studio before adding.
     *
     * @param newClass the class to add (must not be null)
     * @return true if the class was added successfully, false if there's a conflict
     * @throws IllegalArgumentException if newClass is null
     */
    public boolean addClass(GroupClass newClass) {
        ValidationUtils.requireNonNull(newClass, "Заняття не може бути null.");
        
        LocalDate date = newClass.getStartTime().toLocalDate();
        List<GroupClass> classesOnThisDay = classesByDate.getOrDefault(date, new ArrayList<>());

        for (GroupClass existingClass : classesOnThisDay) {
            if (existingClass.getStudio().getId().equals(newClass.getStudio().getId()) && existingClass.overlapsWith(newClass)) {
                System.out.println("Студія '" + newClass.getStudio().getName() + "' уже зайнята на цей час.");
                return false;
            }
        }

        classesOnThisDay.add(newClass);
        classesByDate.put(date, classesOnThisDay);
        return true;
    }

    /**
     * Removes a class from the schedule.
     *
     * @param classToRemove the class to remove (must not be null)
     * @throws IllegalArgumentException if classToRemove is null
     */
    public void removeClass(GroupClass classToRemove) {
        ValidationUtils.requireNonNull(classToRemove, "Заняття не може бути null.");
        
        LocalDate date = classToRemove.getStartTime().toLocalDate();
        if (classesByDate.containsKey(date)) {
            classesByDate.get(date).remove(classToRemove);
        }
    }

    /**
     * Gets all classes scheduled for a specific date.
     *
     * @param date the date to query (must not be null)
     * @return a list of classes for that date (empty list if none)
     * @throws IllegalArgumentException if date is null
     */
    public List<GroupClass> getClassesForDate(LocalDate date) {
        ValidationUtils.requireNonNull(date, "Дата не може бути null.");
        return classesByDate.getOrDefault(date, new ArrayList<>());
    }

    /**
     * Gets all classes scheduled for a specific date and studio.
     *
     * @param date the date to query (must not be null)
     * @param studio the studio to filter by (must not be null)
     * @return a list of classes matching the criteria (empty list if none)
     * @throws IllegalArgumentException if date or studio is null
     */
    public List<GroupClass> getClassesForDateAndStudio(LocalDate date, Studio studio) {
        ValidationUtils.requireNonNull(date, "Дата не може бути null.");
        ValidationUtils.requireNonNull(studio, "Студія не може бути null.");
        
        return getClassesForDate(date).stream()
                .filter(c -> c.getStudio().getId().equals(studio.getId()))
                .collect(Collectors.toList());
    }
}