package models;

public class User {
    private int id;
    private String name;
    private String surname;
    private boolean gender;
    private String card;

    public User(){

    }

    public User(String name, String surname, boolean gender, String card) {
        setName(name);
        setSurname(surname);
        setGender(gender);
        setCard(card);
    }

    public User(int id, String name, String surname, boolean gender, String card) {
        this(name, surname, gender, card);
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", gender=" + gender + '\n' +
                ", creditCardNumber=" + card +
                '}';
    }
}
