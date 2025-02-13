package models;

import models.User;

public class TransactionService {

    // Calculate transaction fees between users
    public double calculateTransactionFee(User sender, User receiver) {
        return sender.getIssuer().equalsIgnoreCase(receiver.getIssuer()) ? 0 : 150;
    }

    // Add a transaction to both sender and receiver
    public void processTransaction(User sender, User receiver, double amount) {
        if (sender.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        // Deduct from sender and add to receiver
        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);

        // Record transactions
        String transactionRecord = String.format("Transferred %.2f to User ID: %d", amount, receiver.getId());
        sender.addTransaction(transactionRecord);

        String receiverRecord = String.format("Received %.2f from User ID: %d", amount, sender.getId());
        receiver.addTransaction(receiverRecord);
    }
}