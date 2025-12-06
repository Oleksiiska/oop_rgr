package core.services;

import core.domain.client.Client;
import core.domain.client.Membership;
import core.domain.client.MembershipType;
import core.domain.staff.Administrator;
import core.exceptions.MembershipAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MembershipServiceTest {
    private MembershipService membershipService;
    private Client client;
    private Administrator admin;

    @BeforeEach
    void setUp() {
        membershipService = new MembershipService();
        client = new Client("Олена Ковальчук", "+380991234567");
        admin = new Administrator("Петро Іваненко", 30000);
    }

    @Test
    void testAssignMembershipSuccess() {
        Membership membership = new Membership.Builder(MembershipType.SINGLE_CLUB, 
                LocalDate.now(), 500)
                .withDurationInDays(30)
                .forClub("club-123")
                .build();

        membershipService.assignMembership(client, membership, admin);

        assertNotNull(client.getMembership());
        assertTrue(client.hasActiveMembership());
    }

    @Test
    void testAssignMembershipThrowsWhenAdministratorNull() {
        Membership membership = new Membership.Builder(MembershipType.SINGLE_CLUB, 
                LocalDate.now(), 500)
                .withDurationInDays(30)
                .forClub("club-123")
                .build();

        assertThrows(IllegalArgumentException.class, () -> 
            membershipService.assignMembership(client, membership, null));
    }

    @Test
    void testAssignMembershipThrowsWhenMembershipNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            membershipService.assignMembership(client, null, admin));
    }

    @Test
    void testValidateClubAccessSuccess() {
        Membership membership = new Membership.Builder(MembershipType.SINGLE_CLUB, 
                LocalDate.now(), 500)
                .withDurationInDays(30)
                .forClub("club-123")
                .build();

        membershipService.assignMembership(client, membership, admin);
        
        assertDoesNotThrow(() -> 
            membershipService.validateClubAccess(client, "club-123"));
    }

    @Test
    void testValidateClubAccessThrowsWhenNoMembership() {
        assertThrows(MembershipAccessException.class, () -> 
            membershipService.validateClubAccess(client, "club-123"));
    }

    @Test
    void testValidateClubAccessThrowsWhenExpiredMembership() {
        Membership expiredMembership = new Membership.Builder(MembershipType.SINGLE_CLUB, 
                LocalDate.now().minusDays(60), 500)
                .withDurationInDays(30)
                .forClub("club-123")
                .build();

        membershipService.assignMembership(client, expiredMembership, admin);

        assertThrows(MembershipAccessException.class, () -> 
            membershipService.validateClubAccess(client, "club-123"));
    }

    @Test
    void testValidateClubAccessThrowsWhenWrongClub() {
        Membership membership = new Membership.Builder(MembershipType.SINGLE_CLUB, 
                LocalDate.now(), 500)
                .withDurationInDays(30)
                .forClub("club-123")
                .build();

        membershipService.assignMembership(client, membership, admin);

        assertThrows(MembershipAccessException.class, () -> 
            membershipService.validateClubAccess(client, "other-club"));
    }

    @Test
    void testValidateClubAccessWithNetworkWideMembership() {
        Membership membership = new Membership.Builder(MembershipType.NETWORK_WIDE, 
                LocalDate.now(), 1000)
                .withDurationInDays(60)
                .build();

        membershipService.assignMembership(client, membership, admin);

        assertDoesNotThrow(() -> 
            membershipService.validateClubAccess(client, "any-club-id"));
    }
}

