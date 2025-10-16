import java.util.ArrayList;
import java.util.List;

public class Client {
    private String fullName;
    private String phoneNumber;
    private Membership membership; // Может быть null
    private final List<GroupClass> bookingHistory;
    private final List<Order> orderHistory;

    public Client(String fullName, String phoneNumber) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.bookingHistory = new ArrayList<>();
        this.orderHistory = new ArrayList<>();
    }

    public void assignMembership(Membership membership) {
        this.membership = membership;
    }

    public boolean hasActiveMembership() {
        return membership != null && membership.isActive();
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public Membership getMembership() {
        return membership;
    }
}