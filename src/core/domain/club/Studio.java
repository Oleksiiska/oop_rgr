package core.domain.club;

import java.util.UUID;

public class Studio {
    private final String id;
    private String name;
    private final int capacity;

    public Studio(String name, int capacity) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getId() {
        return id;
    }
}