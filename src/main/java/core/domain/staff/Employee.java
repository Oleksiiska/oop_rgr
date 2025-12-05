package core.domain.staff;

import core.util.Constants;
import core.util.ValidationUtils;

/**
 * Abstract base class representing an employee of the fitness club.
 * All staff members (trainers, administrators, cleaners) extend this class.
 */
public abstract class Employee implements EmployeeOperation {
    protected String fullName;
    protected double salary;

    /**
     * Creates a new employee with the specified name and salary.
     *
     * @param fullName the full name of the employee (must not be null or blank)
     * @param salary the salary of the employee (must be non-negative)
     * @throws IllegalArgumentException if fullName is null/blank or salary is negative
     */
    public Employee(String fullName, double salary) {
        this.fullName = ValidationUtils.requireNonBlank(fullName, Constants.ERROR_EMPLOYEE_NAME_BLANK);
        this.salary = ValidationUtils.requireNonNegative(salary, Constants.ERROR_EMPLOYEE_SALARY_NEGATIVE);
    }

    public abstract String getJobTitle();

    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public void setFullName(String fullName) {
        this.fullName = ValidationUtils.requireNonBlank(fullName, Constants.ERROR_EMPLOYEE_NAME_BLANK);
    }

    @Override
    public double getSalary() {
        return salary;
    }

    @Override
    public void setSalary(double salary) {
        this.salary = ValidationUtils.requireNonNegative(salary, Constants.ERROR_EMPLOYEE_SALARY_NEGATIVE);
    }
}