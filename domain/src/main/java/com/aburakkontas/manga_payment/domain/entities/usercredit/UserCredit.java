package com.aburakkontas.manga_payment.domain.entities.usercredit;

import com.aburakkontas.manga_payment.domain.entities.payment.Payment;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
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

    @Column(nullable = false, columnDefinition = "double default 0")
    private Double credit;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Payment> payments;

    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ArrayList<CreditTransaction> creditTransactions;

    public boolean addCredit(Double credit) {
        this.credit += credit;

        this.addTransaction(credit, CreditTransactionTypes.ADD_CREDIT, "Credit added to account.", true);

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
        this.addTransaction(credit, CreditTransactionTypes.SUBTRACT_CREDIT, "Checking if user has enough credit.", true);
        return this.credit >= credit;
    }

    public boolean hasCredit() {
        return this.credit > 0;
    }

    public List<CreditTransaction> getTransactions() {
        return Collections.unmodifiableList(creditTransactions);
    }

    public List<CreditTransaction> getSuccessfulTransactions() {
        return creditTransactions.stream().filter(CreditTransaction::isTransactionSuccess).toList();
    }

    public List<CreditTransaction> getFailedTransactions() {
        return creditTransactions.stream().filter(t -> !t.isTransactionSuccess()).toList();
    }

    public Double getCredit() {
        this.addTransaction(this.credit, CreditTransactionTypes.CHECK_CREDIT, "Checking user credit.", true);
        return this.credit;
    }

    public boolean refundCredit(UUID transactionId) {
        var transaction = creditTransactions.stream().filter(t -> t.getTransactionId().equals(transactionId)).findFirst().orElse(null);
        if(transaction == null) {
            this.addTransaction(0.0, CreditTransactionTypes.REFUND_CREDIT, "Transaction not found.", false);
            return false;
        }

        this.credit += transaction.getAmount();
        this.addTransaction(transaction.getAmount(), CreditTransactionTypes.REFUND_CREDIT, "Credit refunded.", true);
        return true;
    }

    private CreditTransaction createTransaction(Double credit, CreditTransactionTypes transactionType, String description, boolean transactionSuccess) {
        var transaction = new CreditTransaction();
        transaction.setUserId(this.userId);
        transaction.setAmount(credit);
        transaction.setBalance(this.credit);
        transaction.setTransactionType(transactionType);
        transaction.setTransactionSuccess(transactionSuccess);
        transaction.setDescription(description);
        transaction.setTransactionDate(java.time.ZonedDateTime.now());

        return transaction;
    }

    private void addTransaction(CreditTransaction transaction) {
        creditTransactions.add(transaction);
    }

    private void addTransaction(Double credit, CreditTransactionTypes transactionType, String description, boolean transactionSuccess) {
        var transaction = createTransaction(credit, transactionType, description, transactionSuccess);
        addTransaction(transaction);
    }
}
