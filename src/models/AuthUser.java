package models;

public class AuthUser extends User {
    private String username;
    private String password;
    private Role role; // Role object

    // Full constructor with all fields (User + AuthUser fields)
    public AuthUser(int id, String username, String password, Role role,
                    String name, String surname, boolean gender, String card,
                    double balance, String brand, String issuer) {
        super(id, name, surname, gender, card, balance, brand, issuer); // Call User constructor
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Overloaded constructor for AuthUser-specific fields only
    public AuthUser(int id, String username, String password, Role role) {
        super(id, "", "", false, "", 0.0, "", ""); // Default User fields
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return super.toString() +
                String.format("Username: %-20s\nRole: %-20s\n", username, role.getName());
    }
}