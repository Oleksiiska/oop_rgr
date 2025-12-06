package core.domain.club;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FitnessNetworkTest {

    @BeforeEach
    void resetSingleton() throws Exception {
        Field instanceField = FitnessNetwork.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);
    }

    @Test
    void testGetInstanceCreatesSingleton() {
        FitnessNetwork network1 = FitnessNetwork.getInstance("Network 1");
        FitnessNetwork network2 = FitnessNetwork.getInstance("Network 2");
        
        assertSame(network1, network2, "Should return same instance");
        assertEquals("Network 1", network1.getName(), "Name should be from first call");
    }

    @Test
    void testAddClub() {
        FitnessNetwork network = FitnessNetwork.getInstance("MyFitness");
        FitnessClub club = new FitnessClub("м. Київ, пр. Оболонський, 1");
        
        network.addClub(club);
        
        List<FitnessClub> clubs = network.getClubs();
        assertEquals(1, clubs.size());
        assertTrue(clubs.contains(club));
    }

    @Test
    void testAddMultipleClubs() {
        FitnessNetwork network = FitnessNetwork.getInstance("MyFitness");
        FitnessClub club1 = new FitnessClub("Address 1");
        FitnessClub club2 = new FitnessClub("Address 2");
        
        network.addClub(club1);
        network.addClub(club2);
        
        assertEquals(2, network.getClubs().size());
    }

    @Test
    void testRemoveClub() {
        FitnessNetwork network = FitnessNetwork.getInstance("MyFitness");
        FitnessClub club = new FitnessClub("Address 1");
        
        network.addClub(club);
        assertEquals(1, network.getClubs().size());
        
        network.removeClub(club);
        assertEquals(0, network.getClubs().size());
    }

    @Test
    void testGetClubsReturnsImmutableCopy() {
        FitnessNetwork network = FitnessNetwork.getInstance("MyFitness");
        FitnessClub club = new FitnessClub("Address 1");
        network.addClub(club);
        
        List<FitnessClub> clubs = network.getClubs();
        assertThrows(UnsupportedOperationException.class, () -> 
            clubs.add(new FitnessClub("Address 2")));
    }

    @Test
    void testGetName() {
        FitnessNetwork network = FitnessNetwork.getInstance("MyFitness Kyiv");
        assertEquals("MyFitness Kyiv", network.getName());
    }

    @Test
    void testThreadSafety() throws InterruptedException {
        FitnessNetwork network = FitnessNetwork.getInstance("Test");
        FitnessClub[] clubs = new FitnessClub[10];
        
        for (int i = 0; i < 10; i++) {
            clubs[i] = new FitnessClub("Address " + i);
        }
        
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            final int index = i;
            threads[i] = new Thread(() -> network.addClub(clubs[index]));
            threads[i].start();
        }
        
        for (Thread thread : threads) {
            thread.join();
        }
        
        assertEquals(10, network.getClubs().size());
    }
}

