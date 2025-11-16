package core.domain.scheduling;

import java.time.LocalDateTime;

public class ClassStateImpl {
    
    public static class ScheduledState implements ClassState {
        @Override
        public boolean canAddParticipant() {
            return true;
        }
        
        @Override
        public boolean canCancel() {
            return true;
        }
        
        @Override
        public boolean canStart() {
            return false;
        }
        
        @Override
        public String getStatusDescription() {
            return "Заплановано";
        }
    }
    
    public static class InProgressState implements ClassState {
        @Override
        public boolean canAddParticipant() {
            return false;
        }
        
        @Override
        public boolean canCancel() {
            return false;
        }
        
        @Override
        public boolean canStart() {
            return false;
        }
        
        @Override
        public String getStatusDescription() {
            return "Триває";
        }
    }
    
    public static class CompletedState implements ClassState {
        @Override
        public boolean canAddParticipant() {
            return false;
        }
        
        @Override
        public boolean canCancel() {
            return false;
        }
        
        @Override
        public boolean canStart() {
            return false;
        }
        
        @Override
        public String getStatusDescription() {
            return "Завершено";
        }
    }
    
    public static class CancelledState implements ClassState {
        @Override
        public boolean canAddParticipant() {
            return false;
        }
        
        @Override
        public boolean canCancel() {
            return false;
        }
        
        @Override
        public boolean canStart() {
            return false;
        }
        
        @Override
        public String getStatusDescription() {
            return "Скасовано";
        }
    }
    
    public static ClassState getState(LocalDateTime startTime, LocalDateTime endTime, boolean isCancelled) {
        if (isCancelled) {
            return new CancelledState();
        }
        
        LocalDateTime now = LocalDateTime.now();
        
        if (now.isBefore(startTime)) {
            return new ScheduledState();
        } else if (now.isAfter(endTime)) {
            return new CompletedState();
        } else {
            return new InProgressState();
        }
    }
}

