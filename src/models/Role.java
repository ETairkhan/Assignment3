package models;

public class Role {
    private String id;  // Change from int to String
    private String name; // "admin" or "user"

    public Role() {}

    public Role(String id, String name) { // Use String id
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {  // Change setter type
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
        return "Role{" +
                "id='" + id + '\'' + // Change int to String formatting
                ", name='" + name + '\'' +
                '}';
    }
}
