package core.domain.client;

import core.domain.shop.DiscountStrategy;
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
    private final MembershipStrategyAccess accessStrategy;
    private final float originalCost;
    private final core.domain.shop.DiscountOperation discountStrategy;
    private MembershipState currentState;

    private Membership(Builder builder) {
        this.type = builder.type;
        this.startDate = builder.startDate;
        this.endDate = builder.startDate.plusDays(builder.durationInDays);
        this.clubId = builder.clubId;
        this.accessStrategy = MembershipStrategy.getStrategy(this.type);
        this.originalCost = builder.cost;
        this.discountStrategy = builder.discountStrategy != null 
            ? builder.discountStrategy 
            : DiscountStrategy.noDiscount();
        this.currentState = MembershipStateImpl.getState(startDate, endDate);
    }

    public boolean isActive() {
        updateState();
        return currentState.isActive();
    }
    
    public boolean canBook() {
        updateState();
        return currentState.canBook();
    }
    
    public String getStatusDescription() {
        updateState();
        return currentState.getStatusDescription();
    }
    
    private void updateState() {
        this.currentState = MembershipStateImpl.getState(startDate, endDate);
    }

    public boolean hasAccessToClub(String clubId) {
        return accessStrategy.hasAccessToClub(clubId, this.clubId);
    }

    public MembershipType getType() {
        return type;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getClubId() {
        return clubId;
    }
    
    public float getOriginalCost() {
        return originalCost;
    }
    
    public float getCost() {
        return (float) discountStrategy.applyDiscount(originalCost);
    }
    
    public float getDiscountAmount() {
        return (float) discountStrategy.getDiscountAmount(originalCost);
    }
    
    public core.domain.shop.DiscountOperation getDiscountStrategy() {
        return discountStrategy;
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
        private core.domain.shop.DiscountOperation discountStrategy = null;

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

        public Builder withDurationInDays(int duration) {
            this.durationInDays = duration;
            return this;
        }

        public Builder forClub(String clubId) {
            this.clubId = clubId;
            return this;
        }
        
        public Builder withDiscount(core.domain.shop.DiscountOperation discountStrategy) {
            ValidationUtils.requireNonNull(discountStrategy, "Стратегія знижки не може бути null.");
            this.discountStrategy = discountStrategy;
            return this;
        }

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