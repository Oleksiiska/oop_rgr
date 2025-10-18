package src.core.domain.staff;

public class Administrator extends Employee {
    public Administrator(String fullName, double salary) {
        super(fullName, salary);
    }

    @Override
    public String getJobTitle() {
        return "Адміністратор";
    }

    public void approveNewMembership() {
        System.out.println("Адміністратор " + fullName + " затверджує новий абонемент.");
    }

    public void manageSchedule() {
        System.out.println("Адміністратор " + fullName + " погодив розклад.");
    }
}