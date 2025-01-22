package models;

public class UserModil {
    package models;

    public class User {
        private int id;
        private String name;
        private String surname;
        private String creditCard;
        private double balance;
        private double deposit;
        private double withdraw;

        public User(int id, String name, String surname, String creditCard, double balance, double deposit, double withdraw) {
            this.id = id;
            this.name = name;
            this.surname = surname;
            this.creditCard = creditCard;
            this.balance = balance;
            this.deposit = deposit;
            this.withdraw = withdraw;
        }

        public User(String name, String surname, String creditCard, double balance) {
            this.name = name;
            this.surname = surname;
            this.creditCard = creditCard;
            this.balance = balance;
        }

        // Getters and Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getSurname() { return surname; }
        public void setSurname(String surname) { this.surname = surname; }

        public String getCreditCard() { return creditCard; }
        public void setCreditCard(String creditCard) { this.creditCard = creditCard; }

        public double getBalance() { return balance; }
        public void setBalance(double balance) { this.balance = balance; }

        public double getDeposit() { return deposit; }
        public void setDeposit(double deposit) { this.deposit = deposit; }

        public double getWithdraw() { return withdraw; }
        public void setWithdraw(double withdraw) { this.withdraw = withdraw; }

        @Override
        public String toString() {
            return String.format("ID: %d, Name: %s, Surname: %s, CreditCard: %s, Balance: %.2f, Deposit: %.2f, Withdraw: %.2f",
                    id, name, surname, creditCard, balance, deposit, withdraw);
        }
    }

}
