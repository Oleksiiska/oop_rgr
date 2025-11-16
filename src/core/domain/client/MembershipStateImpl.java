package core.domain.client;

import java.time.LocalDate;

public class MembershipStateImpl {
    
    public static class ActiveState implements MembershipState {
        @Override
        public boolean isActive() {
            return true;
        }
        
        @Override
        public boolean canBook() {
            return true;
        }
        
        @Override
        public String getStatusDescription() {
            return "Активний";
        }
    }
    
    public static class ExpiredState implements MembershipState {
        @Override
        public boolean isActive() {
            return false;
        }
        
        @Override
        public boolean canBook() {
            return false;
        }
        
        @Override
        public String getStatusDescription() {
            return "Термін дії закінчився";
        }
    }
    
    public static class NotStartedState implements MembershipState {
        @Override
        public boolean isActive() {
            return false;
        }
        
        @Override
        public boolean canBook() {
            return false;
        }
        
        @Override
        public String getStatusDescription() {
            return "Ще не почався";
        }
    }
    
    public static MembershipState getState(LocalDate startDate, LocalDate endDate) {
        LocalDate today = LocalDate.now();
        
        if (today.isBefore(startDate)) {
            return new NotStartedState();
        } else if (today.isAfter(endDate)) {
            return new ExpiredState();
        } else {
            return new ActiveState();
        }
    }
}

