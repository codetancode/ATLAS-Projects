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

    public void addShareIdHeld(String shareId) {
        if(this.shareIdHeld == null){
            this.shareIdHeld = shareId;
        }else{
            this.shareIdHeld = this.shareIdHeld + "|"+shareId;
        }
    }

    public void removeShareIdHeld(String shareIdToRemove) {
        if(this.shareIdHeld != null){
            String ids = "";
            String finalString = "";
            for(int i=0;i < this.shareIdHeld.length();i++){
                if(this.shareIdHeld.charAt(i) == '|'){
                    if(!ids.equals(shareIdToRemove)){
                        if(finalString == ""){
                            finalString = finalString + ids;
                        }else{
                            finalString = finalString+"|"+ids;
                        }
                    }
                    ids="";

                }else{
                    ids = ids + shareIdHeld.charAt(i);
                }
            }
            if(!ids.equals(shareIdToRemove)){
                finalString = finalString +"|"+ids;
            }
            this.shareIdHeld = finalString;
        }
    }

    public String getBoughtTransactionId() {
        return this.boughtTransactionId;
    }

    public void addBoughtTransactionId(String boughtTransactionId) {
        if(this.boughtTransactionId == null){
            this.boughtTransactionId = boughtTransactionId;
        }else{
            this.boughtTransactionId = this.boughtTransactionId + "|"+boughtTransactionId;
        }
    }
    public void removeBoughtTransactionId(String boughtTransactionIdToRemove) {
        if(this.boughtTransactionId != null){
            String ids = "";
            String finalString = "";
            for(int i=0;i < this.boughtTransactionId.length();i++){
                if(this.boughtTransactionId.charAt(i) == '|'){
                    if(!ids.equals(boughtTransactionIdToRemove)){
                        if(finalString == ""){
                            finalString = finalString + ids;
                        }else{
                            finalString = finalString+"|"+ids;
                        }
                    }
                    ids="";
                }else{
                    ids = ids + this.boughtTransactionId.charAt(i);
                }
            }
            if(!ids.equals(boughtTransactionIdToRemove)){
                finalString = finalString +"|"+ids;
            }
            this.boughtTransactionId = finalString;
        }
    }

    public String getSoldTransactionId() {
        return this.soldTransactionId;
    }

    public void addSoldTransactionId(String soldTransactionId) {
        if(this.soldTransactionId == null){
            this.soldTransactionId = soldTransactionId;
        }else{
            this.soldTransactionId = this.soldTransactionId + "|"+soldTransactionId;
        }
    }
    public void removeSoldTransactionId(String soldTransactionIdToRemove) {
        if(this.soldTransactionId != null){
            String ids = "";
            String finalString = "";
            for(int i=0;i < this.soldTransactionId.length();i++){
                if(this.soldTransactionId.charAt(i) == '|'){
                    if(!ids.equals(soldTransactionIdToRemove)){
                        if(finalString == ""){
                            finalString = finalString + ids;
                        }else{
                            finalString = finalString+"|"+ids;
                        }
                    }
                    ids="";

                }else{
                    ids = ids + this.soldTransactionId.charAt(i);
                }
            }
            if(!ids.equals(soldTransactionIdToRemove)){
                finalString = finalString +"|"+ids;
            }
            this.soldTransactionId = finalString;
        }
    }

}
