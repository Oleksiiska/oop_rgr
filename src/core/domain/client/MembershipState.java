package core.domain.client;

import java.time.LocalDate;

public interface MembershipState {
    boolean isActive();
    boolean canBook();
    String getStatusDescription();
}

