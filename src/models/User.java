package models;

public class User {
    private int id;
    private String name;
    private String surname;
    private int age;
    private boolean gender; // true = male, false = female
    private int creditCard;
    private int balance;
    private int writeOffs;
    private int deposit;

    // Constructor
    public User(String name, String surname, int age, boolean gender, int creditCard, int balance, int writeOffs, int deposit) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.gender = gender;
        this.creditCard = creditCard;
        this.balance = balance;
        this.writeOffs = writeOffs;
        this.deposit = deposit;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public boolean isGender() { return gender; }
    public void setGender(boolean gender) { this.gender = gender; }
    public int getCreditCard() { return creditCard; }
    public void setCreditCard(int creditCard) { this.creditCard = creditCard; }
    public int getBalance() { return balance; }
    public void setBalance(int balance) { this.balance = balance; }
    public int getWriteOffs() { return writeOffs; }
    public void setWriteOffs(int writeOffs) { this.writeOffs = writeOffs; }
    public int getDeposit() { return deposit; }
    public void setDeposit(int deposit) { this.deposit = deposit; }

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "', surname='" + surname + "', age=" + age + ", gender=" + (gender ? "Male" : "Female") +
                ", creditCard=" + creditCard + ", balance=" + balance + ", writeOffs=" + writeOffs + ", deposit=" + deposit + "}";
    }
}
