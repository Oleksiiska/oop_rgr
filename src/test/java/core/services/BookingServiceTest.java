package core.services;

import core.domain.client.Client;
import core.domain.client.Membership;
import core.domain.client.MembershipType;
import core.domain.club.FitnessClub;
import core.domain.scheduling.GroupClass;
import core.domain.staff.Administrator;
import core.domain.staff.Trainer;
import core.domain.club.Studio;
import core.exceptions.BookingException;
import core.exceptions.MembershipAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingServiceTest {
    private BookingService bookingService;
    private MembershipService membershipService;
    private Client client;
    private FitnessClub club;
    private GroupClass groupClass;
    private Administrator admin;

    @BeforeEach
    void setUp() {
        membershipService = new MembershipService();
        bookingService = new BookingService(membershipService);
        client = new Client("Олена Ковальчук", "+380991234567");
        admin = new Administrator("Петро Іваненко", 30000);
        club = new FitnessClub("м. Київ, пр. Оболонський, 1");
        
        Trainer trainer = new Trainer("Анна Шевченко", 25000, "Йога");
        Studio studio = new Studio("Зал для йоги", 20, true);
        LocalDateTime classTime = LocalDateTime.now().plusDays(1).withHour(18).withMinute(0);
        groupClass = new GroupClass("Вечірня йога", trainer, studio, classTime, 60);
    }

    @Test
    void testBookClassSuccess() throws BookingException, MembershipAccessException {
        Membership membership = new Membership.Builder(MembershipType.SINGLE_CLUB, 
                LocalDate.now(), 500)
                .withDurationInDays(30)
                .forClub(club.getId())
                .build();
        
        membershipService.assignMembership(client, membership, admin);
        bookingService.bookClass(client, groupClass, club);

        assertEquals(1, groupClass.getCurrentSize());
        assertTrue(groupClass.getParticipants().contains(client));
    }

    @Test
    void testBookClassThrowsWhenNoActiveMembership() {
        assertThrows(MembershipAccessException.class, () -> 
            bookingService.bookClass(client, groupClass, club));
    }

    @Test
    void testBookClassThrowsWhenWrongClub() throws Exception {
        Membership membership = new Membership.Builder(MembershipType.SINGLE_CLUB, 
                LocalDate.now(), 500)
                .withDurationInDays(30)
                .forClub("other-club-id")
                .build();
        
        membershipService.assignMembership(client, membership, admin);
        
        assertThrows(MembershipAccessException.class, () -> 
            bookingService.bookClass(client, groupClass, club));
    }

    @Test
    void testBookClassWithNetworkWideMembership() throws BookingException, MembershipAccessException {
        Membership membership = new Membership.Builder(MembershipType.NETWORK_WIDE, 
                LocalDate.now(), 1000)
                .withDurationInDays(60)
                .build();
        
        membershipService.assignMembership(client, membership, admin);
        bookingService.bookClass(client, groupClass, club);

        assertEquals(1, groupClass.getCurrentSize());
    }

    @Test
    void testCancelBooking() throws BookingException {
        groupClass.addParticipant(client);
        assertEquals(1, groupClass.getCurrentSize());

        bookingService.cancelBooking(client, groupClass);

        assertEquals(0, groupClass.getCurrentSize());
    }

    @Test
    void testCancelBookingWhenNotParticipating() {
        bookingService.cancelBooking(client, groupClass);
        assertEquals(0, groupClass.getCurrentSize());
    }
}

