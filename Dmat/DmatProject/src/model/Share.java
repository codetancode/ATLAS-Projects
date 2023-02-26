package model;

import java.util.Date;
public class Share {
    private Long shareId;
    private String companyName;
    private int availableToBuy;
    private int currentPrice;
    private int totalUnits;
    private int shareValue;

    public Share(){}
    public Share(Long shareId, String companyName, int availableToBuy, int currentPrice, int totalUnits, int shareValue) {
        this.shareId = shareId;
        this.companyName = companyName;
        this.availableToBuy = availableToBuy;
        this.currentPrice = currentPrice;
        this.totalUnits = totalUnits;
        this.shareValue = shareValue;
    }

    public Long getShareId() {
        return shareId;
    }

    public void setShareId(long shareId) {
        this.shareId = shareId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getAvailableToBuy() {
        return availableToBuy;
    }

    public void setAvailableToBuy(int availableToBuy) {
        this.availableToBuy = availableToBuy;
    }

    public int getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(int currentPrice) {
        this.currentPrice = currentPrice;
    }

    public int getTotalUnits() {
        return totalUnits;
    }

    public void setTotalUnits(int totalUnits) {
        this.totalUnits = totalUnits;
    }

    public int getShareValue() {
        return shareValue;
    }

    public void setShareValue(int shareValue) {
        this.shareValue = shareValue;
    }
}
