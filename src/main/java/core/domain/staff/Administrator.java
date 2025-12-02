package core.domain.staff;

import core.domain.client.Membership;
import core.util.Constants;
import core.util.ValidationUtils;
import java.time.LocalDate;

/**
 * Represents an administrator who manages club operations.
 * Administrators can approve memberships and manage schedules.
 */
public class Administrator extends Employee {
    
    /**
     * Creates a new administrator with the specified name and salary.
     *
     * @param fullName the full name of the administrator (must not be null or blank)
     * @param salary the salary of the administrator (must be non-negative)
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public Administrator(String fullName, double salary) {
        super(fullName, salary);
    }

    /**
     * Gets the job title of the administrator.
     *
     * @return "Адміністратор"
     */
    @Override
    public String getJobTitle() {
        return Constants.JOB_TITLE_ADMINISTRATOR;
    }

    /**
     * Approves a new membership for a client.
     * Validates the membership dates and logs the approval.
     *
     * @param membership the membership to approve (must not be null)
     * @return true if the membership is approved
     * @throws IllegalArgumentException if membership is null or has invalid dates
     */
    public boolean approveNewMembership(Membership membership) {
        ValidationUtils.requireNonNull(membership, Constants.ERROR_MEMBERSHIP_NULL);
        
        // Validate membership dates
        LocalDate startDate = membership.getStartDate();
        LocalDate endDate = membership.getEndDate();
        
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Дата початку та кінця абонемента мають бути вказані.");
        }
        
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException(Constants.ERROR_MEMBERSHIP_DATES_INVALID);
        }
        
        // Check if membership is already expired
        if (endDate.isBefore(LocalDate.now())) {
            System.out.println("Попередження: Абонемент вже закінчився, але адміністратор " + fullName + " затверджує його.");
        }
        
        System.out.println("Адміністратор " + fullName + " затверджує новий абонемент типу: " + membership.getType());
        return true;
    }

    /**
     * Manages the club schedule.
     * Logs the schedule management action.
     */
    public void manageSchedule() {
        System.out.println("Адміністратор " + fullName + " погодив розклад.");
    }
}