package core.domain.club;

import core.domain.scheduling.Schedule;
import core.domain.shop.Inventory;
import core.domain.staff.Administrator;
import core.domain.staff.EmployeeOperation;
import core.domain.staff.Cleaner;
import core.domain.staff.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FitnessClubTest {
    private FitnessClub club;

    @BeforeEach
    void setUp() {
        club = new FitnessClub("м. Київ, пр. Оболонський, 1");
    }

    @Test
    void testFitnessClubCreation() {
        assertNotNull(club.getId());
        assertEquals("м. Київ, пр. Оболонський, 1", club.getAddress());
        assertNotNull(club.getSchedule());
        assertNotNull(club.getInventory());
        assertTrue(club.getStaff().isEmpty());
        assertTrue(club.getStudios().isEmpty());
    }

    @Test
    void testAddStudio() {
        Studio studio = new Studio("Зал для йоги", 20, false);
        
        club.addStudio(studio);
        
        List<Studio> studios = club.getStudios();
        assertEquals(1, studios.size());
        assertTrue(studios.contains(studio));
    }

    @Test
    void testAddMultipleStudios() {
        Studio studio1 = new Studio("Зал для йоги", 20, false);
        Studio studio2 = new Studio("Басейн", 50, true);
        
        club.addStudio(studio1);
        club.addStudio(studio2);
        
        assertEquals(2, club.getStudios().size());
    }

    @Test
    void testAddStaff() {
        Trainer trainer = new Trainer("Анна Шевченко", 25000, "Йога");
        Administrator admin = new Administrator("Петро Іваненко", 30000);
        Cleaner cleaner = new Cleaner("Марія Сидоренко", 15000, 
                List.of("Роздягальні", "Басейн"));
        
        club.addStaff(trainer);
        club.addStaff(admin);
        club.addStaff(cleaner);
        
        List<?> staff = club.getStaff();
        assertEquals(3, staff.size());
        assertTrue(staff.contains(trainer));
        assertTrue(staff.contains(admin));
        assertTrue(staff.contains(cleaner));
    }

    @Test
    void testAddStaffThrowsWhenNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            club.addStaff(null));
    }

    @Test
    void testGetSchedule() {
        Schedule schedule = club.getSchedule();
        assertNotNull(schedule);
        assertTrue(schedule.getClassesForDate(java.time.LocalDate.now()).isEmpty());
    }

    @Test
    void testGetInventory() {
        Inventory inventory = club.getInventory();
        assertNotNull(inventory);
    }

    @Test
    void testGetStaffReturnsImmutableCopy() {
        Trainer trainer = new Trainer("Анна Шевченко", 25000, "Йога");
        club.addStaff(trainer);
        
        List<EmployeeOperation> staff = club.getStaff();
        assertThrows(UnsupportedOperationException.class, () -> 
            staff.add(new Administrator("Test", 20000)));
    }

    @Test
    void testGetStudiosReturnsImmutableCopy() {
        Studio studio = new Studio("Зал для йоги", 20, false);
        club.addStudio(studio);
        
        List<Studio> studios = club.getStudios();
        assertThrows(UnsupportedOperationException.class, () -> 
            studios.add(new Studio("Test", 10, true)));
    }
}

