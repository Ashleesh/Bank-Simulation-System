package com.bankSiml.model;

import java.sql.Timestamp;

// This class represents a single transaction entry in the database
public class Transaction {
    private int id;
    private int accountId;
    private String txnType;
    private double amount;
    private double balanceAfter;
    private String note;
    private Timestamp createdAt;

    public Transaction() {
    }

    public Transaction(int id, int accountId, String txnType, double amount,
                       double balanceAfter, String note, Timestamp createdAt) {
        this.id = id;
        this.accountId = accountId;
        this.txnType = txnType;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.note = note;
        this.createdAt = createdAt;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getTxnType() {
        return txnType;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public String getNote() {
        return note;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setBalanceAfter(double balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
