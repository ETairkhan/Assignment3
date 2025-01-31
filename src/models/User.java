package models;
import java.util.ArrayList;
import java.util.List;


public class User {
    private int id;
    private String name;
    private String surname;
    private boolean gender;
    private String card;
    private double balance;
    private String brand;  // New field
    private String issuer;
    private List<String> transactions;


    public User(){
        transactions = new ArrayList<>();

    }

    public User(String name, String surname, boolean gender, String card, double balance, String brand, String issuer) {
        setName(name);
        setSurname(surname);
        setGender(gender);
        setCard(card);
        setBalance(balance);
        setBrand(brand);
        setIssuer(issuer);
        transactions = new ArrayList<>();
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

    public List<String> getTransactions() {
        return transactions;
    }

    public void addTransaction(String transaction) {
        transactions.add(transaction);
    }

    public double calculateTransactionFee(User receiver) {
        return this.issuer.equalsIgnoreCase(receiver.getIssuer()) ? 0 : 150;
    }


    @Override
    public String toString() {
        return String.format(
                "======================= User Details =======================\n" +
                        "ID:             %-10d\n" +
                        "Name:           %-20s\n" +
                        "Surname:        %-20s\n" +
                        "Gender:         %-10s\n" +
                        "Card:           %-20s\n" +
                        "Balance:        $%-10.2f\n" +
                        "Brand:          %-20s\n" +
                        "Issuer:         %-20s\n" +
                        "===========================================================\n",
                id, name, surname, (gender ? "Male" : "Female"), card, balance, brand, issuer
        );
    }

}
