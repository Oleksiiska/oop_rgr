package core.domain.club;

import core.domain.staff.Cleaner;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudioTest {
    
    @Test
    void testStudioCreation() {
        Studio studio = new Studio("Зал для йоги", 20, true);
        
        assertNotNull(studio.getId());
        assertEquals("Зал для йоги", studio.getName());
        assertEquals(20, studio.getCapacity());
        assertTrue(studio.isClean());
    }

    @Test
    void testStudioCreationThrowsWhenNameBlank() {
        assertThrows(IllegalArgumentException.class, () -> 
            new Studio("   ", 20, true));
        
        assertThrows(IllegalArgumentException.class, () -> 
            new Studio(null, 20, true));
    }

    @Test
    void testStudioCreationThrowsWhenCapacityInvalid() {
        assertThrows(IllegalArgumentException.class, () -> 
            new Studio("Test", 0, true));
        
        assertThrows(IllegalArgumentException.class, () -> 
            new Studio("Test", -5, true));
    }

    @Test
    void testSetName() {
        Studio studio = new Studio("Original Name", 20, true);
        
        studio.setName("New Name");
        assertEquals("New Name", studio.getName());
    }

    @Test
    void testSetNameThrowsWhenBlank() {
        Studio studio = new Studio("Original Name", 20, true);
        
        assertThrows(IllegalArgumentException.class, () -> 
            studio.setName("   "));
        
        assertThrows(IllegalArgumentException.class, () -> 
            studio.setName(null));
    }

    @Test
    void testCleanWithAssignedCleaner() {
        Studio studio = new Studio("Зал для йоги", 20, false);
        Cleaner cleaner = new Cleaner("Марія Сидоренко", 15000, 
                List.of("Зал для йоги", "Басейн"));
        
        assertFalse(studio.isClean());
        
        boolean cleaned = studio.clean(cleaner);
        
        assertTrue(cleaned);
        assertTrue(studio.isClean());
    }

    @Test
    void testCleanWithUnassignedCleaner() {
        Studio studio = new Studio("Зал для йоги", 20, false);
        Cleaner cleaner = new Cleaner("Марія Сидоренко", 15000, 
                List.of("Басейн"));
        
        assertFalse(studio.isClean());
        
        boolean cleaned = studio.clean(cleaner);
        
        assertFalse(cleaned);
        assertFalse(studio.isClean());
    }

    @Test
    void testCleanThrowsWhenCleanerNull() {
        Studio studio = new Studio("Зал для йоги", 20, false);
        
        assertThrows(IllegalArgumentException.class, () -> 
            studio.clean(null));
    }

    @Test
    void testSetCleanliness() {
        Studio studio = new Studio("Зал для йоги", 20, true);
        
        studio.setCleanliness(false);
        assertFalse(studio.isClean());
        
        studio.setCleanliness(true);
        assertTrue(studio.isClean());
    }
}

