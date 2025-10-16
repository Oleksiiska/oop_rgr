import java.time.LocalDate;

public class Membership {
    private final MembershipType type;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String clubId; // null, when NETWORK_WIDE

    public Membership(MembershipType type, LocalDate startDate, int durationInDays, String clubId) {
        if (type == MembershipType.SINGLE_CLUB && clubId == null) {
            throw new IllegalArgumentException("Club ID must be provided for a single club membership.");
        }
        this.type = type;
        this.startDate = startDate;
        this.endDate = startDate.plusDays(durationInDays);
        this.clubId = (type == MembershipType.NETWORK_WIDE) ? null : clubId;
    }

    public boolean isActive() {
        LocalDate today = LocalDate.now();
        return !today.isBefore(startDate) && !today.isAfter(endDate);
    }

    public boolean hasAccessToClub(String clubId) {
        if (this.type == MembershipType.NETWORK_WIDE) {
            return true;
        }
        return this.type == MembershipType.SINGLE_CLUB && this.clubId.equals(clubId);
    }

    public MembershipType getType() {
        return type;
    }
}