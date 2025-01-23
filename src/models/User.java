package models;

public class User {
    private int id;
    private String name;
    private String surname;
    private boolean gender;
    private String card;
    private double balance;
    private String brand;  // New field
    private String issuer; // New field

    public User(){

    }

    public User(String name, String surname, boolean gender, String card, double balance, String brand, String issuer) {
        setName(name);
        setSurname(surname);
        setGender(gender);
        setCard(card);
        setBalance(balance);
        setBrand(brand);
        setIssuer(issuer);
    }

    public User(int id, String name, String surname, boolean gender, String card, double balance, String brand, String issuer) {
        this(name, surname, gender, card, balance, brand, issuer);
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

    public String getCard() {
        return card;
    }
    public void setCard(String card) {
        this.card = card;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }


    @Override
    public String toString() {

        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", gender=" + gender + '\n' +
                ", card='" + card + '\'' +
                ", balance=" + balance +
                ", brand='" + brand + '\'' +
                ", issuer='" + issuer + '\'' +
                '}';

    }

}
