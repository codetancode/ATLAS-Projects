package model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Transaction {

    private Long transactionId;
    private int minCharge = 100;
    private double tranChargePercent = 0.5;
    private int type;//0 bought, 1 sold
    private String date;
    private Long accountId;
    private String parentShareId;
    private String transactionShareId;
    private Long totalCost;

    public Transaction(){}
    public Transaction(Long transactionId, int type, Date date, Long accountId, String parentShareId,String transactionShareId, Long totalCost) {
        this.transactionId = transactionId;
        this.type = type;
        SimpleDateFormat formater = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        this.date = formater.format(date);
        this.accountId = accountId;
        this.parentShareId = parentShareId;
        this.transactionShareId = transactionShareId;
        this.totalCost = totalCost;
    }

    public int getMinCharge() {
        return minCharge;
    }

    public void setMinCharge(int min) {
        this.minCharge = min;
    }

    public double getTranChargePercent() {
        return tranChargePercent;
    }

    public void setTranChargePercent(double tranCharge) {
        this.tranChargePercent = tranCharge;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(Date date) {
        SimpleDateFormat formater = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        this.date = formater.format(date);
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
//only 1 transaction id

    public String getTransactionShareId() {
        return this.transactionShareId;
    }

//only 1 transaction id
    public void setTransactionShareId(String tranShareId) {
        this.transactionShareId = tranShareId;
    }


    public String getParentShareId() {
        return parentShareId;
    }

    public void setParentShareId(String parentShareId) {
        this.parentShareId = parentShareId;
    }

    public Long getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Long totalCost) {
        this.totalCost = totalCost;
    }
}
