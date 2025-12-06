package core.domain.scheduling;

import core.domain.club.Studio;
import core.domain.staff.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleTest {
    private Schedule schedule;
    private Trainer trainer;
    private Studio studio1;
    private Studio studio2;

    @BeforeEach
    void setUp() {
        schedule = new Schedule();
        trainer = new Trainer("Анна Шевченко", 25000, "Йога");
        studio1 = new Studio("Зал для йоги", 20, true);
        studio2 = new Studio("Басейн", 50, true);
    }

    @Test
    void testAddClass() {
        LocalDateTime startTime = LocalDateTime.now().plusDays(1).withHour(18).withMinute(0);
        GroupClass yogaClass = new GroupClass("Вечірня йога", trainer, studio1, startTime, 60);

        boolean added = schedule.addClass(yogaClass);

        assertTrue(added);
        LocalDate date = startTime.toLocalDate();
        List<GroupClass> classes = schedule.getClassesForDate(date);
        assertEquals(1, classes.size());
        assertTrue(classes.contains(yogaClass));
    }

    @Test
    void testAddClassWithOverlappingTimeSameStudio() {
        LocalDateTime startTime = LocalDateTime.now().plusDays(1).withHour(18).withMinute(0);
        GroupClass class1 = new GroupClass("Йога", trainer, studio1, startTime, 60);
        GroupClass class2 = new GroupClass("Йога 2", trainer, studio1, 
                startTime.plusMinutes(30), 60);

        assertTrue(schedule.addClass(class1));
        boolean added = schedule.addClass(class2);

        assertFalse(added, "Should not add overlapping class in same studio");
    }

    @Test
    void testAddClassWithOverlappingTimeDifferentStudio() {
        LocalDateTime startTime = LocalDateTime.now().plusDays(1).withHour(18).withMinute(0);
        GroupClass class1 = new GroupClass("Йога", trainer, studio1, startTime, 60);
        GroupClass class2 = new GroupClass("Плавання", trainer, studio2, startTime, 60);

        assertTrue(schedule.addClass(class1));
        boolean added = schedule.addClass(class2);

        assertTrue(added, "Should allow overlapping classes in different studios");
    }

    @Test
    void testAddClassNonOverlappingSameStudio() {
        LocalDateTime startTime = LocalDateTime.now().plusDays(1).withHour(18).withMinute(0);
        GroupClass class1 = new GroupClass("Йога", trainer, studio1, startTime, 60);
        GroupClass class2 = new GroupClass("Йога 2", trainer, studio1, 
                startTime.plusHours(2), 60);

        assertTrue(schedule.addClass(class1));
        boolean added = schedule.addClass(class2);

        assertTrue(added, "Should allow non-overlapping classes in same studio");
    }

    @Test
    void testRemoveClass() {
        LocalDateTime startTime = LocalDateTime.now().plusDays(1).withHour(18).withMinute(0);
        GroupClass yogaClass = new GroupClass("Вечірня йога", trainer, studio1, startTime, 60);

        schedule.addClass(yogaClass);
        LocalDate date = startTime.toLocalDate();
        assertEquals(1, schedule.getClassesForDate(date).size());

        schedule.removeClass(yogaClass);
        assertEquals(0, schedule.getClassesForDate(date).size());
    }

    @Test
    void testGetClassesForDate() {
        LocalDate date1 = LocalDate.now().plusDays(1);
        LocalDate date2 = LocalDate.now().plusDays(2);

        GroupClass class1 = new GroupClass("Class 1", trainer, studio1, 
                date1.atTime(10, 0), 60);
        GroupClass class2 = new GroupClass("Class 2", trainer, studio1, 
                date1.atTime(14, 0), 60);
        GroupClass class3 = new GroupClass("Class 3", trainer, studio1, 
                date2.atTime(10, 0), 60);

        schedule.addClass(class1);
        schedule.addClass(class2);
        schedule.addClass(class3);

        List<GroupClass> classesDate1 = schedule.getClassesForDate(date1);
        assertEquals(2, classesDate1.size());
        assertTrue(classesDate1.contains(class1));
        assertTrue(classesDate1.contains(class2));

        List<GroupClass> classesDate2 = schedule.getClassesForDate(date2);
        assertEquals(1, classesDate2.size());
        assertTrue(classesDate2.contains(class3));
    }

    @Test
    void testGetClassesForDateAndStudio() {
        LocalDate date = LocalDate.now().plusDays(1);
        LocalDateTime time1 = date.atTime(10, 0);
        LocalDateTime time2 = date.atTime(14, 0);

        GroupClass class1 = new GroupClass("Йога", trainer, studio1, time1, 60);
        GroupClass class2 = new GroupClass("Плавання", trainer, studio2, time1, 60);
        GroupClass class3 = new GroupClass("Йога 2", trainer, studio1, time2, 60);

        schedule.addClass(class1);
        schedule.addClass(class2);
        schedule.addClass(class3);

        List<GroupClass> studio1Classes = schedule.getClassesForDateAndStudio(date, studio1);
        assertEquals(2, studio1Classes.size());
        assertTrue(studio1Classes.contains(class1));
        assertTrue(studio1Classes.contains(class3));

        List<GroupClass> studio2Classes = schedule.getClassesForDateAndStudio(date, studio2);
        assertEquals(1, studio2Classes.size());
        assertTrue(studio2Classes.contains(class2));
    }

    @Test
    void testGetClassesForNonExistentDate() {
        LocalDate futureDate = LocalDate.now().plusDays(10);
        List<GroupClass> classes = schedule.getClassesForDate(futureDate);
        assertTrue(classes.isEmpty());
    }
}

