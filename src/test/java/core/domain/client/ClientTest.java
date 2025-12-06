package core.domain.client;

import core.domain.staff.Administrator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {
    private Client client;
    private Administrator admin;

    @BeforeEach
    void setUp() {
        client = new Client("Олена Ковальчук", "+380991234567");
        admin = new Administrator("Петро Іваненко", 30000);
    }

    @Test
    void testClientCreation() {
        assertEquals("Олена Ковальчук", client.getFullName());
        assertEquals("+380991234567", client.getPhoneNumber());
        assertNull(client.getMembership());
        assertFalse(client.hasActiveMembership());
    }

    @Test
    void testAssignMembership() {
        Membership membership = new Membership.Builder(MembershipType.SINGLE_CLUB, LocalDate.now(), 500)
                .withDurationInDays(30)
                .forClub("club-123")
                .build();

        client.assignMembership(membership, admin);

        assertNotNull(client.getMembership());
        assertEquals(MembershipType.SINGLE_CLUB, client.getMembership().getType());
        assertTrue(client.hasActiveMembership());
    }

    @Test
    void testAssignMembershipThrowsWhenNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            client.assignMembership(null, admin));
    }

    @Test
    void testHasActiveMembershipWithExpiredMembership() {
        Membership expiredMembership = new Membership.Builder(MembershipType.SINGLE_CLUB, 
                LocalDate.now().minusDays(60), 500)
                .withDurationInDays(30)
                .forClub("club-123")
                .build();

        client.assignMembership(expiredMembership, admin);
        assertFalse(client.hasActiveMembership());
    }

    @Test
    void testSetPhoneNumber() {
        client.setPhoneNumber("+380991111111");
        assertEquals("+380991111111", client.getPhoneNumber());
    }

    @Test
    void testSetPhoneNumberThrowsWhenNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            client.setPhoneNumber(null));
    }

    @Test
    void testSetPhoneNumberThrowsWhenBlank() {
        assertThrows(IllegalArgumentException.class, () -> 
            client.setPhoneNumber("   "));
    }
}

