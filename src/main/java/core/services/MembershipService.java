package core.services;

import core.domain.client.Client;
import core.domain.client.Membership;
import core.domain.staff.Administrator;
import core.exceptions.MembershipAccessException;
import core.util.Constants;
import core.util.ValidationUtils;
import org.springframework.stereotype.Service;

/**
 * Service for managing membership operations.
 * Handles membership assignment and access validation.
 */
@Service
public class MembershipService {

    /**
     * Assigns a membership to a client after administrator approval.
     *
     * @param client the client to assign the membership to (must not be null)
     * @param membership the membership to assign (must not be null)
     * @param administrator the administrator approving the membership (must not be null)
     * @throws IllegalArgumentException if any parameter is null or approval is denied
     */
    public void assignMembership(Client client, Membership membership, Administrator administrator) {
        ValidationUtils.requireNonNull(administrator, Constants.ERROR_ADMINISTRATOR_NULL);
        ValidationUtils.requireNonNull(membership, Constants.ERROR_MEMBERSHIP_NULL);
        ValidationUtils.requireNonNull(client, "Клієнт не може бути null.");

        // Get administrator approval before assigning
        boolean approved = administrator.approveNewMembership(membership);

        if (!approved) {
            throw new IllegalArgumentException(Constants.ERROR_MEMBERSHIP_NOT_APPROVED);
        }

        // Assign membership after approval
        client.assignMembership(membership, administrator);
    }

    public void validateClubAccess(Client client, String clubId) throws MembershipAccessException {
        ValidationUtils.requireNonNull(client, "Клієнт не може бути null.");
        ValidationUtils.requireNonBlank(clubId, "ID клубу не може бути порожнім.");

        if (!client.hasActiveMembership()) {
            throw new MembershipAccessException(Constants.ERROR_MEMBERSHIP_INACTIVE);
        }

        if (!client.getMembership().hasAccessToClub(clubId)) {
            throw new MembershipAccessException(Constants.ERROR_MEMBERSHIP_INVALID_CLUB);
        }
    }
}

