package com.bankSiml.model;

public class Account {
    private int id;
    private String accountNumber;
    private String holderName;
    private String email;
    private String accountType;
    private double balance;
    private String password;

    // default constructor
    public Account() {
    }

    // parameterized constructor
    public Account(int id, String accountNumber, String holderName, String email,
                   String accountType, double balance, String password) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.email = email;
        this.accountType = accountType;
        this.balance = balance;
        this.password = password;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public String getEmail() {
        return email;
    }

    public String getAccountType() {
        return accountType;
    }

    public double getBalance() {
        return balance;
    }

    public String getPassword() {
        return password;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
