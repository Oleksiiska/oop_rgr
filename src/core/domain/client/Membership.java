package core.domain.client;

import core.interfaces.IMembershipStrategy;
import java.time.LocalDate;

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
        float cost = builder.cost;
        this.accessStrategy = MembershipStrategy.getStrategy(this.type);
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

    public static class Builder {
        private final MembershipType type;
        private final LocalDate startDate;
        private final float cost;

        private int durationInDays = 30;
        private String clubId = null;

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

        public Membership build() {
            if (type == null) {
                throw new IllegalArgumentException("Тип абонемента не може бути null.");
            }
            if (startDate == null) {
                throw new IllegalArgumentException("Дата початку не може бути null.");
            }
            if (durationInDays <= 0) {
                throw new IllegalArgumentException("Тривалість абонемента має бути позитивною.");
            }
            if (type == MembershipType.SINGLE_CLUB && (clubId == null || clubId.isBlank())) {
                throw new IllegalArgumentException("Club ID має бути вказаний для типу абонемента SINGLE_CLUB.");
            }
            if (type == MembershipType.NETWORK_WIDE && clubId != null) {
                this.clubId = null;
            }

            return new Membership(this);
        }
    }


}