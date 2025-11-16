package core.interfaces;

/**
 * Interface for employee operations.
 */
public interface IEmployee {
    String getJobTitle();
    String getFullName();
    double getSalary();
    void setFullName(String fullName);
    void setSalary(double salary);
}

