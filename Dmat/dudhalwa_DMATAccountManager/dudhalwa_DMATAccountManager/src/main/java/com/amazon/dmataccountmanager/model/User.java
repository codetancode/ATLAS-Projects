package com.amazon.dmataccountmanager.model;

public class User {
    // Attributes
    public int id;
    public String username;
    public int accountNumber;
    public String password;
    public float accountBalance;
    public String lastUpdatedOn;

    public User() {
    }

    public User(int id, String username, int accountNumber, String password, float accountBalance, String lastUpdatedOn) {
        this.id = id;
        this.username = username;
        this.accountNumber = accountNumber;
        this.password = password;
        this.accountBalance = accountBalance;
        this.lastUpdatedOn = lastUpdatedOn;
    }

    public void prettyPrint() {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("User Name:\t\t" + username);
        System.out.println("Account Number:\t\t" + accountNumber);
        System.out.println("Account Balance:\t\t" + accountBalance);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~");
    }

}
