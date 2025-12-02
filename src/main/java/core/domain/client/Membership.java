package core.domain.client;

import core.interfaces.IMembershipStrategy;
import core.util.Constants;
import core.util.ValidationUtils;
import java.time.LocalDate;

/**
 * Represents a membership that grants a client access to fitness club facilities.
 * Supports different membership types (single club or network-wide) and tracks
 * membership state (active, expired, not started).
 */
public class Membership {
    private final MembershipType type;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String clubId; // null, when NETWORK_WIDE
    private final IMembershipStrategy accessStrategy;
    private MembershipState currentState;

    private Membership(Builder builder) {
        this.type = builder.type;
        this.startDate = builder.startDate;
        this.endDate = builder.startDate.plusDays(builder.durationInDays);
        this.clubId = builder.clubId;
        this.accessStrategy = MembershipStrategy.getStrategy(this.type);
        this.currentState = MembershipStateImpl.getState(startDate, endDate);
    }

    /**
     * Checks if the membership is currently active.
     *
     * @return true if the membership is active, false otherwise
     */
    public boolean isActive() {
        updateState();
        return currentState.isActive();
    }
    
    /**
     * Checks if the membership allows booking classes.
     *
     * @return true if booking is allowed, false otherwise
     */
    public boolean canBook() {
        updateState();
        return currentState.canBook();
    }
    
    /**
     * Gets a human-readable description of the membership status.
     *
     * @return the status description
     */
    public String getStatusDescription() {
        updateState();
        return currentState.getStatusDescription();
    }
    
    /**
     * Updates the membership state based on current date.
     */
    private void updateState() {
        this.currentState = MembershipStateImpl.getState(startDate, endDate);
    }

    /**
     * Checks if this membership grants access to a specific club.
     *
     * @param clubId the ID of the club to check access for
     * @return true if access is granted, false otherwise
     */
    public boolean hasAccessToClub(String clubId) {
        return accessStrategy.hasAccessToClub(clubId, this.clubId);
    }

    /**
     * Gets the membership type.
     *
     * @return the membership type
     */
    public MembershipType getType() {
        return type;
    }

    /**
     * Gets the start date of the membership.
     *
     * @return the start date
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Gets the end date of the membership.
     *
     * @return the end date
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Gets the club ID associated with this membership.
     * Returns null for network-wide memberships.
     *
     * @return the club ID, or null for network-wide memberships
     */
    public String getClubId() {
        return clubId;
    }

    /**
     * Builder class for creating Membership instances.
     * Provides a fluent API for constructing memberships with optional parameters.
     */
    public static class Builder {
        private final MembershipType type;
        private final LocalDate startDate;
        private final float cost;

        private int durationInDays = Constants.DEFAULT_MEMBERSHIP_DURATION_DAYS;
        private String clubId = null;

        /**
         * Creates a new membership builder.
         *
         * @param type the membership type (must not be null)
         * @param startDate the start date of the membership (must not be null)
         * @param cost the cost of the membership
         */
        public Builder(MembershipType type, LocalDate startDate, float cost) {
            this.type = type;
            this.startDate = startDate;
            this.cost = cost;
        }

        /**
         * Sets the duration of the membership in days.
         *
         * @param duration the duration in days (must be positive)
         * @return this builder for method chaining
         */
        public Builder withDurationInDays(int duration) {
            this.durationInDays = duration;
            return this;
        }

        /**
         * Sets the club ID for single-club memberships.
         *
         * @param clubId the club ID
         * @return this builder for method chaining
         */
        public Builder forClub(String clubId) {
            this.clubId = clubId;
            return this;
        }

        /**
         * Builds a new Membership instance with the configured parameters.
         *
         * @return a new Membership instance
         * @throws IllegalArgumentException if any validation fails
         */
        public Membership build() {
            ValidationUtils.requireNonNull(type, Constants.ERROR_MEMBERSHIP_TYPE_NULL);
            ValidationUtils.requireNonNull(startDate, Constants.ERROR_MEMBERSHIP_START_DATE_NULL);
            ValidationUtils.requirePositive(durationInDays, Constants.ERROR_MEMBERSHIP_DURATION_INVALID);
            
            if (type == MembershipType.SINGLE_CLUB && (clubId == null || clubId.isBlank())) {
                throw new IllegalArgumentException(Constants.ERROR_MEMBERSHIP_CLUB_ID_REQUIRED);
            }
            if (type == MembershipType.NETWORK_WIDE && clubId != null) {
                this.clubId = null;
            }

            return new Membership(this);
        }
    }


}