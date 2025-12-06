package core.domain.staff;

import core.domain.client.Membership;
import core.domain.client.MembershipType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AdministratorTest {
    private Administrator admin;

    @BeforeEach
    void setUp() {
        admin = new Administrator("Петро Іваненко", 30000);
    }

    @Test
    void testAdministratorCreation() {
        assertEquals("Петро Іваненко", admin.getFullName());
        assertEquals(30000, admin.getSalary());
        assertEquals("Адміністратор", admin.getJobTitle());
    }

    @Test
    void testApproveNewMembership() {
        Membership membership = new Membership.Builder(MembershipType.SINGLE_CLUB, 
                LocalDate.now(), 500)
                .withDurationInDays(30)
                .forClub("club-123")
                .build();

        boolean approved = admin.approveNewMembership(membership);

        assertTrue(approved);
    }

    @Test
    void testApproveNewMembershipWithExpiredDate() {
        Membership expiredMembership = new Membership.Builder(MembershipType.SINGLE_CLUB, 
                LocalDate.now().minusDays(60), 500)
                .withDurationInDays(30)
                .forClub("club-123")
                .build();

        // Should still approve but with warning
        boolean approved = admin.approveNewMembership(expiredMembership);
        assertTrue(approved);
    }

    @Test
    void testApproveNewMembershipThrowsWhenNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            admin.approveNewMembership(null));
    }

    @Test
    void testManageSchedule() {
        // Test that method doesn't throw
        assertDoesNotThrow(() -> admin.manageSchedule());
    }
}

