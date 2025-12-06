package core.domain.scheduling;

import core.domain.client.Client;
import core.domain.club.Studio;
import core.domain.staff.Trainer;
import core.exceptions.BookingException;
import core.event.Event;
import core.event.Observer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GroupClassTest {
    private Trainer trainer;
    private Studio studio;
    private LocalDateTime futureTime;
    private GroupClass groupClass;

    @BeforeEach
    void setUp() {
        trainer = new Trainer("Анна Шевченко", 25000, "Йога");
        studio = new Studio("Зал для йоги", 20, true);
        futureTime = LocalDateTime.now().plusDays(1).withHour(18).withMinute(0);
        groupClass = new GroupClass("Вечірня йога", trainer, studio, futureTime, 60);
    }

    @Test
    void testGroupClassCreation() {
        assertNotNull(groupClass.getId());
        assertEquals("Вечірня йога", groupClass.getName());
        assertEquals(trainer, groupClass.getTrainer());
        assertEquals(studio, groupClass.getStudio());
        assertEquals(20, groupClass.getMaxCapacity());
        assertEquals(0, groupClass.getCurrentSize());
        assertFalse(groupClass.isCancelled());
    }

    @Test
    void testGroupClassCreationThrowsWhenInvalidDuration() {
        assertThrows(IllegalArgumentException.class, () -> 
            new GroupClass("Test", trainer, studio, futureTime, 0));
    }

    @Test
    void testAddParticipant() throws BookingException {
        Client client = new Client("Олена Ковальчук", "+380991234567");
        
        groupClass.addParticipant(client);
        
        assertEquals(1, groupClass.getCurrentSize());
        assertTrue(groupClass.getParticipants().contains(client));
    }

    @Test
    void testAddParticipantTwiceDoesNotDuplicate() throws BookingException {
        Client client = new Client("Олена Ковальчук", "+380991234567");
        
        groupClass.addParticipant(client);
        groupClass.addParticipant(client);
        
        assertEquals(1, groupClass.getCurrentSize());
    }

    @Test
    void testAddParticipantToFullClass() throws BookingException {
        Studio smallStudio = new Studio("Small Studio", 2, true);
        GroupClass smallClass = new GroupClass("Small Class", trainer, smallStudio, 
                futureTime, 60);
        
        Client client1 = new Client("Client 1", "+380991111111");
        Client client2 = new Client("Client 2", "+380992222222");
        
        smallClass.addParticipant(client1);
        smallClass.addParticipant(client2);
        
        Client client3 = new Client("Client 3", "+380993333333");
        assertThrows(BookingException.class, () -> 
            smallClass.addParticipant(client3));
    }

    @Test
    void testRemoveParticipant() throws BookingException {
        Client client = new Client("Олена Ковальчук", "+380991234567");
        
        groupClass.addParticipant(client);
        assertEquals(1, groupClass.getCurrentSize());
        
        groupClass.removeParticipant(client);
        assertEquals(0, groupClass.getCurrentSize());
    }

    @Test
    void testCancel() {
        assertFalse(groupClass.isCancelled());
        
        groupClass.cancel();
        
        assertTrue(groupClass.isCancelled());
    }

    @Test
    void testCancelEmitsEvent() {
        TestObserver observer = new TestObserver();
        groupClass.addObserver(observer);
        
        groupClass.cancel();
        
        Event event = observer.getLastEvent();
        assertNotNull(event);
        assertEquals(Event.EventType.CLASS_CANCELLED, event.getType());
        assertTrue(event.getMessage().contains("скасовано"));
    }

    @Test
    void testBookingConfirmedEvent() throws BookingException {
        TestObserver observer = new TestObserver();
        groupClass.addObserver(observer);
        
        Client client = new Client("Олена Ковальчук", "+380991234567");
        groupClass.addParticipant(client);
        
        Event event = observer.getLastEvent();
        assertNotNull(event);
        assertEquals(Event.EventType.BOOKING_CONFIRMED, event.getType());
    }

    @Test
    void testClassFullEvent() throws BookingException {
        Studio smallStudio = new Studio("Small Studio", 1, true);
        GroupClass smallClass = new GroupClass("Small Class", trainer, smallStudio, 
                futureTime, 60);
        
        TestObserver observer = new TestObserver();
        smallClass.addObserver(observer);
        
        Client client1 = new Client("Client 1", "+380991111111");
        smallClass.addParticipant(client1);
        
        Client client2 = new Client("Client 2", "+380992222222");
        try {
            smallClass.addParticipant(client2);
            fail("Should have thrown BookingException");
        } catch (BookingException e) {
            // Expected
        }
        
        // Note: Class full event is emitted before exception
        Event event = observer.getLastEvent();
        assertNotNull(event);
        assertEquals(Event.EventType.CLASS_FULL, event.getType());
    }

    @Test
    void testOverlapsWith() {
        LocalDateTime start1 = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
        LocalDateTime start2 = LocalDateTime.now().plusDays(1).withHour(10).withMinute(30);
        
        GroupClass class1 = new GroupClass("Class 1", trainer, studio, start1, 60);
        GroupClass class2 = new GroupClass("Class 2", trainer, studio, start2, 60);
        
        assertTrue(class1.overlapsWith(class2));
    }

    @Test
    void testDoesNotOverlap() {
        LocalDateTime start1 = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
        LocalDateTime start2 = LocalDateTime.now().plusDays(1).withHour(12).withMinute(0);
        
        GroupClass class1 = new GroupClass("Class 1", trainer, studio, start1, 60);
        GroupClass class2 = new GroupClass("Class 2", trainer, studio, start2, 60);
        
        assertFalse(class1.overlapsWith(class2));
    }

    @Test
    void testGetCurrentState() {
        LocalDateTime futureStart = LocalDateTime.now().plusHours(2);
        GroupClass futureClass = new GroupClass("Future", trainer, studio, futureStart, 60);
        
        assertTrue(futureClass.getCurrentState().canAddParticipant());
        assertTrue(futureClass.getCurrentState().canCancel());
    }

    private static class TestObserver implements Observer<Event> {
        private final List<Event> events = new ArrayList<>();

        @Override
        public void update(Event event) {
            events.add(event);
        }

        Event getLastEvent() {
            return events.isEmpty() ? null : events.get(events.size() - 1);
        }
    }
}

