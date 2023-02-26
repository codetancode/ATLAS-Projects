package com.amazon.dmat.model;

public class Share {
    public int id;
    public String companyName;
    public float price;
    public String lastUpdatedOn;

    public Share() {
    }

    public Share(int id, String companyName, float price, String lastUpdatedOn) {
        this.id = id;
        this.companyName = companyName;
        this.price = price;
        this.lastUpdatedOn = lastUpdatedOn;
    }
}
