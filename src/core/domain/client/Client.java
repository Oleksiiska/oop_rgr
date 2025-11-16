package core.domain.client;

import core.domain.staff.Administrator;

public class Client {
    private final String fullName;
    private Membership membership; // can be null
    private String phoneNumber;

    public Client(String fullName, String phoneNumber) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Assigns a membership to this client.
     */
    public void assignMembership(Membership membership, Administrator administrator) {
        if (membership == null) {
            throw new IllegalArgumentException("Абонемент не може бути null.");
        }
        this.membership = membership;
    }

    public boolean hasActiveMembership() {
        return membership != null && membership.isActive();
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("Номер телефону не може бути порожнім.");
        }
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public Membership getMembership() {
        return membership;
    }

    public String getPhoneNumber() {return phoneNumber;}
}