package core.domain.staff;

public abstract class Employee implements EmployeeOperation {
    protected String fullName;
    protected double salary;

    public Employee(String fullName, double salary) {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Ім'я не може бути пустим.");
        }
        if (salary < 0) {
            throw new IllegalArgumentException("Зарплатня лише додатня.");
        }
        this.fullName = fullName;
        this.salary = salary;
    }

    public abstract String getJobTitle();

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}