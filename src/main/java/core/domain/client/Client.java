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

    public Client(String fullName, String phoneNumber) {
        this.fullName = ValidationUtils.requireNonBlank(fullName, "Ім'я клієнта не може бути порожнім.");
        this.phoneNumber = ValidationUtils.requireNonBlank(phoneNumber, Constants.ERROR_CLIENT_PHONE_BLANK);
    }

    public void assignMembership(Membership membership, Administrator administrator) {
        ValidationUtils.requireNonNull(membership, Constants.ERROR_MEMBERSHIP_NULL);
        ValidationUtils.requireNonNull(administrator, Constants.ERROR_ADMINISTRATOR_NULL);
        this.membership = membership;
    }

    public boolean hasActiveMembership() {
        return membership != null && membership.isActive();
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = ValidationUtils.requireNonBlank(phoneNumber, Constants.ERROR_CLIENT_PHONE_BLANK);
    }

    public String getFullName() {
        return fullName;
    }

    public Membership getMembership() {
        return membership;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}