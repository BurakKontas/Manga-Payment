package com.aburakkontas.manga_payment.domain.entities.usercredit;

import com.aburakkontas.manga_payment.domain.entities.payment.Payment;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user_credit")
@Data
@Setter(AccessLevel.PRIVATE)
public class UserCredit {
    @Id
    private UUID userId;

    @Column(nullable = false)
    private Double credit;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Getter(AccessLevel.PRIVATE)
    private List<Payment> payments = List.of();

    @Transient
    private ArrayList<Payment> paymentsList = this.getPaymentList();

    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Getter(AccessLevel.PRIVATE)
    private List<CreditTransaction> creditTransactions = List.of();

    @Transient
    private ArrayList<CreditTransaction> transactions = this.getTransactions();

    @Transient
    private ArrayList<CreditTransaction> successfulTransactions = this.getSuccessfulTransactions();

    @Transient
    private ArrayList<CreditTransaction> failedTransactions = this.getFailedTransactions();

    public static UserCredit create(UUID userId) {
        var user = new UserCredit();
        user.setUserId(userId);
        user.setCredit(0.0);
        user.setPayments(new ArrayList<>());
        user.setCreditTransactions(new ArrayList<>());

        return user;
    }

    public boolean addCredit(Double credit, String paymentId) {
        this.credit += credit;

        this.addTransaction(credit, CreditTransactionTypes.ADD_CREDIT, "Credit added to account.", true, paymentId);

        return true;
    }

    public boolean subtractCredit(Double price) {
        if(!this.hasCredit(price)) {
            this.addTransaction(price, CreditTransactionTypes.SUBTRACT_CREDIT, "Insufficient credit to remove.", false);
            return false;
        }

        this.credit -= price;
        this.addTransaction(price, CreditTransactionTypes.SUBTRACT_CREDIT, "Credit removed from account.", true);
        return true;
    }

    public boolean hasCredit(Double credit) {
        this.addTransaction(credit, CreditTransactionTypes.CHECK_CREDIT, "Checking if user has enough credit.", true);
        return this.credit >= credit;
    }

    public ArrayList<Payment> getPaymentList() {
        return new ArrayList<>(payments);
    }

    public ArrayList<CreditTransaction> getTransactions() {
        return new ArrayList<>(creditTransactions);
    }

    public ArrayList<CreditTransaction> getSuccessfulTransactions() {
        return new ArrayList<>(creditTransactions.stream().filter(CreditTransaction::isTransactionSuccess).toList());
    }

    public ArrayList<CreditTransaction> getFailedTransactions() {
        return new ArrayList<>(creditTransactions.stream().filter(transaction -> !transaction.isTransactionSuccess()).toList());
    }

    private CreditTransaction createTransaction(Double credit, CreditTransactionTypes transactionType, String description, boolean transactionSuccess, String paymentId) {
        var transaction = new CreditTransaction();
        transaction.setUserId(this.userId);
        transaction.setAmount(credit);
        transaction.setBalance(this.credit);
        transaction.setTransactionType(transactionType);
        transaction.setTransactionSuccess(transactionSuccess);
        transaction.setDescription(description);
        transaction.setTransactionDate(java.time.ZonedDateTime.now());
        transaction.setPaymentId(paymentId);

        return transaction;
    }

    private void addTransaction(CreditTransaction transaction) {
        creditTransactions.add(transaction);
    }

    private void addTransaction(Double credit, CreditTransactionTypes transactionType, String description, boolean transactionSuccess, String paymentId) {
        var transaction = createTransaction(credit, transactionType, description, transactionSuccess, paymentId);
        addTransaction(transaction);
    }

    private void addTransaction(Double credit, CreditTransactionTypes transactionType, String description, boolean transactionSuccess) {
        var transaction = createTransaction(credit, transactionType, description, transactionSuccess, null);
        addTransaction(transaction);
    }
}
