package core.domain.client;

public class Client {
    private final String fullName;
    private Membership membership; // can be null
    private String phoneNumber;

    public Client(String fullName, String phoneNumber) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    public void assignMembership(Membership membership) {
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