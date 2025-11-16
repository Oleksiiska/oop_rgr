package core.domain.club;

import core.domain.staff.Cleaner;

import java.util.UUID;

public class Studio {
    private final String id;
    private String name;
    private final int capacity;
    private boolean cleanliness;

    public Studio(String name, int capacity, boolean cleanliness) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Назва студії не може бути порожньою.");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("Місткість студії має бути позитивною.");
        }
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.capacity = capacity;
        this.cleanliness = cleanliness;
    }

    public boolean clean(Cleaner cleaner) {
        if (cleaner == null) {
            throw new IllegalArgumentException("Прибиральник не може бути null.");
        }
        
        if (cleaner.getAssignedZones().contains(this.name)) {
            this.cleanliness = true;
            System.out.println("Прибиральник " + cleaner.getFullName() + " прибрав студію '" + this.name + "'.");
            return true;
        } else {
            System.out.println("Прибиральник " + cleaner.getFullName() + " не призначений для студії '" + this.name + "'.");
            return false;
        }
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isClean() {
        return cleanliness;
    }

    public void setCleanliness(boolean cleanliness) {
        this.cleanliness = cleanliness;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Назва студії не може бути порожньою.");
        }
        this.name = name;
    }

    public String getId() {
        return id;
    }
}