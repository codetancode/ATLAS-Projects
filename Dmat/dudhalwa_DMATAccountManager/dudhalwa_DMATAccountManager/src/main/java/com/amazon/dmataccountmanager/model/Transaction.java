package com.amazon.dmataccountmanager.model;

public class Transaction {
    public int id;
    public int userID;
    public int shareID;
    public int shareCount;
    public float pricePerShare;
    public String transactedOn;
    public float transactionCharges;
    public int type;
    public float sttCharges;
    public float totalCost;

    public Transaction()
    {

    }

    public Transaction(int id, int userID, int shareID, int shareCount, float pricePerShare, String transactedOn, float transactionCharges, int type, float sttCharges) {
        this.id = id;
        this.userID = userID;
        this.shareID = shareID;
        this.shareCount = shareCount;
        this.pricePerShare = pricePerShare;
        this.transactedOn = transactedOn;
        this.transactionCharges = transactionCharges;
        this.type = type;
        this.sttCharges = sttCharges;
    }
}
