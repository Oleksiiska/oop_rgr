package core.domain.staff;

import core.util.Constants;
import core.util.ValidationUtils;

/**
 * Represents a trainer who conducts fitness classes.
 * Trainers have a specialization (e.g., "Йога", "Силові тренування").
 */
public class Trainer extends Employee {
    private String specialization;

    /**
     * Creates a new trainer with the specified name, salary, and specialization.
     *
     * @param fullName the full name of the trainer (must not be null or blank)
     * @param salary the salary of the trainer (must be non-negative)
     * @param specialization the trainer's specialization (must not be null or blank)
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public Trainer(String fullName, double salary, String specialization) {
        super(fullName, salary);
        this.specialization = ValidationUtils.requireNonBlank(specialization, "Спеціалізація не може бути порожньою.");
    }

    /**
     * Gets the job title of the trainer.
     *
     * @return "Тренер"
     */
    @Override
    public String getJobTitle() {
        return Constants.JOB_TITLE_TRAINER;
    }

    /**
     * Gets the trainer's specialization.
     *
     * @return the specialization
     */
    public String getSpecialization() {
        return specialization;
    }

    /**
     * Sets the trainer's specialization.
     *
     * @param specialization the new specialization (must not be null or blank)
     * @throws IllegalArgumentException if specialization is null or blank
     */
    public void setSpecialization(String specialization) {
        this.specialization = ValidationUtils.requireNonBlank(specialization, "Спеціалізація не може бути порожньою.");
    }
}