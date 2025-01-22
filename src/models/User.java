package models;

public class User {
    private int id;
    private String name;
    private String surname;
    private boolean gender;
    private String creditCardNumber;

    public User(){

    }

    public User(String name, String surname, boolean gender, String creditCardNumber) {
        setName(name);
        setSurname(surname);
        setGender(gender);
    }

    public User(int id, String name, String surname, boolean gender, String creditCardNumber) {
        this(name, surname, gender, creditCardNumber);
        setId(id);
    }

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

    public boolean getGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }
    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", gender=" + gender + '\n' +
                ", creditCardNumber=" + creditCardNumber +
                '}';
    }
}
