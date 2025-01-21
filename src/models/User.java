package models;

public class User {
    private int id;
    private String name;
    private String surname;
    private int age;
    private int creditcard;
    private int balance;
    private int writeOffs; // CamelCase for better naming
    private int deposit;

    // Default constructor
    public User() {
    }

    // Constructor with all fields
    public User(String name, String surname, int age, int creditcard, int balance, int writeOffs, int deposit) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.creditcard = creditcard;
        this.balance = balance;
        this.writeOffs = writeOffs;
        this.deposit = deposit;
    }

    // Constructor with ID and basic details
    public User(int id, String name, String surname, int age) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.age = age;
    }

    // Getter and Setter methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getCreditcard() {
        return creditcard;
    }

    public void setCreditcard(int creditcard) {
        this.creditcard = creditcard;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getWriteOffs() {
        return writeOffs;
    }

    public void setWriteOffs(int writeOffs) {
        this.writeOffs = writeOffs;
    }

    public int getDeposit() {
        return deposit;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +
                ", creditcard=" + creditcard +
                ", balance=" + balance +
                ", writeOffs=" + writeOffs +
                ", deposit=" + deposit +
                '}';
    }
}
