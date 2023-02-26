package model;

import java.util.List;

public class DematAccount {
    private Long accountId;
    private String accHolderName;
    private String password;
    private Integer balance;
    private String shareIdHeld;
    private String boughtTransactionId;
    private String soldTransactionId;

    public DematAccount(){}

    public DematAccount(Long accountId, String accHolderName, String password, Integer balance, String shareIdHeld, String boughtTransactionId, String soldTransactionId) {
        this.accountId = accountId;
        this.accHolderName = accHolderName;
        this.password = password;
        this.balance = balance;
        this.shareIdHeld = shareIdHeld;
        this.boughtTransactionId = boughtTransactionId;
        this.soldTransactionId = soldTransactionId;
    }

    public DematAccount(Long accountId, String password, String accHolderName, Integer balance) {
        this.accountId = accountId;
        this.password = password;
        this.accHolderName = accHolderName;
        this.balance = balance;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccHolderName() {
        return this.accHolderName;
    }

    public void setAccHolderName(String accHolderName) {
        this.accHolderName = accHolderName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }


    public String getShareIdHeld() {
        return this.shareIdHeld;
    }

    public void addShareIdHeld(String shareIdHeld) {
        if(this.shareIdHeld == null){
            this.shareIdHeld = shareIdHeld;
        }else{
            this.shareIdHeld.concat("|"+shareIdHeld);
        }
    }

    public void removeShareIdHeld(String shareIdToRemove) {
        if(this.shareIdHeld != null){
            List<String> shareIds = List.of(this.shareIdHeld.split("|"));
            shareIds.remove(shareIdToRemove);
            for(String s: shareIds){
                addShareIdHeld(s);
            }
        }
    }

    public String getBoughtTransactionId() {
        return this.boughtTransactionId;
    }

    public void addBoughtTransactionId(String boughtTransactionId) {
        if(this.boughtTransactionId == null){
            this.boughtTransactionId = boughtTransactionId;
        }else{
            this.boughtTransactionId.concat("|"+boughtTransactionId);
        }
    }
    public void removeBoughtTransactionId(String boughtTransactionIdToRemove) {
        if(this.boughtTransactionId != null){
            List<String> boughtIds = List.of(this.boughtTransactionId.split("|"));
            boughtIds.remove(boughtTransactionIdToRemove);
            for(String s: boughtIds){
                addBoughtTransactionId(s);
            }
        }
    }

    public String getSoldTransactionId() {
        return this.soldTransactionId;
    }

    public void addSoldTransactionId(String soldTransactionId) {
        if(this.soldTransactionId == null){
            this.soldTransactionId = soldTransactionId;
        }else{
            this.soldTransactionId.concat("|"+soldTransactionId);
        }
    }
    public void removeSoldTransactionId(String soldTransactionIdToRemove) {
        if(this.soldTransactionId != null){
            List<String> soldIds = List.of(this.soldTransactionId.split("|"));
            soldIds.remove(soldTransactionIdToRemove);
            for(String s: soldIds){
                addSoldTransactionId(s);
            }
        }
    }

}
