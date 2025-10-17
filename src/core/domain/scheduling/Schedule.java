package src.core.domain.scheduling;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Schedule {

    private final Map<LocalDate, List<GroupClass>> classesByDate;

    public Schedule() {
        this.classesByDate = new HashMap<>();
    }

    public boolean addClass(GroupClass newClass) {
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

    public void removeClass(GroupClass classToRemove) {
        LocalDate date = classToRemove.getStartTime().toLocalDate();
        if (classesByDate.containsKey(date)) {
            classesByDate.get(date).remove(classToRemove);
        }
    }

    public List<GroupClass> getClassesForDate(LocalDate date) {
        return classesByDate.getOrDefault(date, new ArrayList<>());
    }

    public List<GroupClass> getClassesForDateAndStudio(LocalDate date, Studio studio) {
        return getClassesForDate(date).stream()
                .filter(c -> c.getStudio().getId().equals(studio.getId()))
                .collect(Collectors.toList());
    }
}