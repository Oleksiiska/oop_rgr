package core.domain.staff;

/**
 * Interface defining operations available on employees.
 * Provides access to employee information and allows modification of certain properties.
 */
public interface EmployeeOperation {
    /**
     * Gets the job title of the employee.
     *
     * @return the job title
     */
    String getJobTitle();
    
    /**
     * Gets the full name of the employee.
     *
     * @return the full name
     */
    String getFullName();
    
    /**
     * Gets the salary of the employee.
     *
     * @return the salary
     */
    double getSalary();
    
    /**
     * Sets the full name of the employee.
     *
     * @param fullName the new full name (must not be null or blank)
     * @throws IllegalArgumentException if fullName is null or blank
     */
    void setFullName(String fullName);
    
    /**
     * Sets the salary of the employee.
     *
     * @param salary the new salary (must be non-negative)
     * @throws IllegalArgumentException if salary is negative
     */
    void setSalary(double salary);
}

