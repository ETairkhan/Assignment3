package models;

public class User {
    private int id;
    private String name;
    private String surname;
    private boolean gender;
    private String card;
    private double balance;

    public User(){

    }

public User(String name, String surname, boolean gender, String card, double balance) {
        setName(name);
        setSurname(surname);
        setGender(gender);
        setCard(card);
        setBalance(balance);


    }

    public User(int id, String name, String surname, boolean gender, String card, double balance) {
        this(name, surname, gender, card, balance);
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



    @Override
    public String toString() {
        return "User {\n" +
                "  id: " + id + ",\n" +
                "  name: '" + name + "',\n" +
                "  surname: '" + surname + "',\n" +
                "  gender: " + (gender ? "Male" : "Female") + ",\n" +
                "  creditCardNumber: '" + card + "',\n" +
                "  balance: " + balance + "\n" +
                "}";
    }

}
