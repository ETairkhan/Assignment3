package models;

import java.util.UUID;

public class Role {
    private String id;
    private String name;

    // Constructor for name only (auto-generate id)
    public Role(String name) {
        this.id = UUID.randomUUID().toString(); // Generate unique id
        this.name = name;
    }

    // Constructor for both id and name
    public Role(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("Role{id='%s', name='%s'}", id, name);
    }
}