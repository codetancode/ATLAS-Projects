package com.amazon.dmataccountmanager.model;

public class UserShares {
    public int id;
    public int userId;
    public int shareId;
    public String companyName;
    public int shareCount;

    public UserShares() {
    }

    public UserShares(int id, int userId, int shareId, String companyName, int shareCount) {
        this.id = id;
        this.userId = userId;
        this.shareId = shareId;
        this.companyName = companyName;
        this.shareCount = shareCount;
    }

    public void prettyPrint(){
        System.out.println("Share ID: " + shareId);
        System.out.println("Company Name: " + companyName);
        System.out.println("No. of Shares: " + shareCount);
        System.out.println("_____________________________________________________________");
    }
}
