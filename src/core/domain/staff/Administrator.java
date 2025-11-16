package core.domain.staff;

import core.domain.client.Membership;

public class Administrator extends Employee {
    public Administrator(String fullName, double salary) {
        super(fullName, salary);
    }

    @Override
    public String getJobTitle() {
        return "Адміністратор";
    }

    public boolean approveNewMembership(Membership membership) {
        if (membership == null) {
            throw new IllegalArgumentException("Абонемент не може бути null.");
        }
        
        // Validate membership dates
        if (membership.getStartDate() == null || membership.getEndDate() == null) {
            throw new IllegalArgumentException("Дата початку та кінця абонемента мають бути вказані.");
        }
        
        if (membership.getEndDate().isBefore(membership.getStartDate())) {
            throw new IllegalArgumentException("Дата кінця абонемента не може бути раніше дати початку.");
        }
        
        // Check if membership is already expired
        if (membership.getEndDate().isBefore(java.time.LocalDate.now())) {
            System.out.println("Попередження: Абонемент вже закінчився, але адміністратор " + fullName + " затверджує його.");
        }
        
        System.out.println("Адміністратор " + fullName + " затверджує новий абонемент типу: " + membership.getType());
        return true;
    }

    public void manageSchedule() {
        System.out.println("Адміністратор " + fullName + " погодив розклад.");
    }
}