package core.domain.staff;

public class Trainer extends Employee {
    private String specialization; // Спеціалізація (напр., "Йога", "Силові тренування")

    public Trainer(String fullName, double salary, String specialization) {
        super(fullName, salary);
        this.specialization = specialization;
    }

    @Override
    public String getJobTitle() {
        return "Тренер";
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}