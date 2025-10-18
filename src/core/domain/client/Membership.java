package core.domain.client;

import java.time.LocalDate;

public class Membership {
    private final MembershipType type;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String clubId; // null, when NETWORK_WIDE

    private Membership(Builder builder) {
        this.type = builder.type;
        this.startDate = builder.startDate;
        this.endDate = builder.startDate.plusDays(builder.durationInDays);
        this.clubId = builder.clubId;
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

    public static class Builder {
        private final MembershipType type;
        private final LocalDate startDate;

        private int durationInDays = 30;
        private String clubId = null;

        public Builder(MembershipType type, LocalDate startDate) {
            this.type = type;
            this.startDate = startDate;
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