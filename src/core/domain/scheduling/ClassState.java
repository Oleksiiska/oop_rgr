package core.domain.scheduling;

import java.time.LocalDateTime;

public interface ClassState {
    boolean canAddParticipant();
    boolean canCancel();
    boolean canStart();
    String getStatusDescription();
}

