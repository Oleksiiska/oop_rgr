package core.domain.client;

import core.domain.staff.Administrator;
import core.util.Constants;
import core.util.ValidationUtils;

/**
 * Represents a client of the fitness club.
 * A client can have a membership that grants access to club facilities and services.
 */
public class Client {
    private final String fullName;
    private Membership membership; // can be null
    private String phoneNumber;

    /**
     * Creates a new client with the specified name and phone number.
     *
     * @param fullName the full name of the client (must not be null or blank)
     * @param phoneNumber the phone number of the client (must not be null or blank)
     * @throws IllegalArgumentException if fullName or phoneNumber is null or blank
     */
    public Client(String fullName, String phoneNumber) {
        this.fullName = ValidationUtils.requireNonBlank(fullName, "Ім'я клієнта не може бути порожнім.");
        this.phoneNumber = ValidationUtils.requireNonBlank(phoneNumber, Constants.ERROR_CLIENT_PHONE_BLANK);
    }

    /**
     * Assigns a membership to this client.
     * The membership must be approved by an administrator before assignment.
     *
     * @param membership the membership to assign (must not be null)
     * @param administrator the administrator approving the membership (must not be null)
     * @throws IllegalArgumentException if membership or administrator is null
     */
    public void assignMembership(Membership membership, Administrator administrator) {
        ValidationUtils.requireNonNull(membership, Constants.ERROR_MEMBERSHIP_NULL);
        ValidationUtils.requireNonNull(administrator, Constants.ERROR_ADMINISTRATOR_NULL);
        this.membership = membership;
    }

    /**
     * Checks if the client has an active membership.
     *
     * @return true if the client has a membership and it is currently active, false otherwise
     */
    public boolean hasActiveMembership() {
        return membership != null && membership.isActive();
    }

    /**
     * Updates the client's phone number.
     *
     * @param phoneNumber the new phone number (must not be null or blank)
     * @throws IllegalArgumentException if phoneNumber is null or blank
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = ValidationUtils.requireNonBlank(phoneNumber, Constants.ERROR_CLIENT_PHONE_BLANK);
    }

    /**
     * Gets the full name of the client.
     *
     * @return the full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Gets the client's membership.
     *
     * @return the membership, or null if no membership is assigned
     */
    public Membership getMembership() {
        return membership;
    }

    /**
     * Gets the client's phone number.
     *
     * @return the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
}