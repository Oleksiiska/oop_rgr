package core.domain.staff;

/**
 * Interface for employee operations.
 */
public interface EmployeeOperation {
    String getJobTitle();
    String getFullName();
    double getSalary();
    void setFullName(String fullName);
    void setSalary(double salary);
}

