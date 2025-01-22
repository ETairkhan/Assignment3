package models;

public class User {
    private int id;
    private String name;
    private String surname;
<<<<<<< HEAD
    private boolean gender;
    private String email;
    private int securityPin;

    public User(){
=======
    private int age;
    private boolean gender; // true = male, false = female
<<<<<<< HEAD
=======
    private int creditCard; // Renamed for better readability
>>>>>>> 5bb5980bb629060b5f3abcbd8033620850152cf4
    private int balance;
    private int writeOffs;
    private int deposit;
>>>>>>> 9d344cb9b7bd7def0e717201cf8ef39f0cf94f8b

    // Default constructor
    public User(int id, String name, String surname, int age, boolean gender, int creditCard, int balance, int writeOffs, int deposit) {
    }

    // Constructor with basic details
    public User(int id, String name, String surname, boolean gender) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.gender = gender;
    }

    // Constructor with all fields
    public User(String name, String surname, int age, boolean gender, int creditCard, int balance, int writeOffs, int deposit) {
        this.name = name;
        this.surname = surname;
        this.age = validateAge(age);
        this.gender = gender;
<<<<<<< HEAD
        this.balance = balance;
        this.writeOffs = writeOffs;
        this.deposit = deposit;
=======
        this.creditCard = validateNonNegative(creditCard, "Credit Card");
        this.balance = validateNonNegative(balance, "Balance");
        this.writeOffs = validateNonNegative(writeOffs, "Write Offs");
        this.deposit = validateNonNegative(deposit, "Deposit");
>>>>>>> 5bb5980bb629060b5f3abcbd8033620850152cf4
    }

    // Constructor with ID and basic details
    public User(int id, String name, String surname, int age) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.age = validateAge(age);
    }

    public User(Object o) {
    }

    // Validation for age
    private int validateAge(int age) {
        if (age < 0 || age > 120) {
            throw new IllegalArgumentException("Age must be between 0 and 120.");
        }
        return age;
    }

    // Generic validation for non-negative fields
    private int validateNonNegative(int value, String fieldName) {
        if (value < 0) {
            throw new IllegalArgumentException(fieldName + " cannot be negative.");
        }
        return value;
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
        this.age = validateAge(age);
    }

    public boolean isGender() { // Renamed getter to isGender
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public int getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(int creditCard) {
        this.creditCard = validateNonNegative(creditCard, "Credit Card");
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = validateNonNegative(balance, "Balance");
    }

    public int getWriteOffs() {
        return writeOffs;
    }

    public void setWriteOffs(int writeOffs) {
        this.writeOffs = validateNonNegative(writeOffs, "Write Offs");
    }

    public int getDeposit() {
        return deposit;
    }

    public void setDeposit(int deposit) {
        this.deposit = validateNonNegative(deposit, "Deposit");
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +
                ", gender=" + (gender ? "Male" : "Female") +
<<<<<<< HEAD
                ", creditcard=" + creditCard +
=======
                ", creditCard=" + creditCard +

                ", balance=" + balance +
                ", writeOffs=" + writeOffs +
                ", deposit=" + deposit +
                '}';
    }
}
