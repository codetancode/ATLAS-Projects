package controller;

import customException.InvalidInputException;
import dB.Validator;
import model.DematAccount;
import model.Share;
import model.Transaction;
import service.DmatService;

import javax.swing.*;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public class AccountController extends BaseController {
    private static AccountController accController;

    private AccountController(){}

    private DmatService accountService = DmatService.getDmatService();

    public static AccountController getInstance(){
        if(accController == null){
            accController = new AccountController();
        }
        return accController;
    }


    public boolean showMenu(long accNo){
        boolean exitCode = false;
        String choice = "";

        while (!exitCode) {
            System.out.println("\nWelcome to the Account Holder User Menu");
            System.out.println("\nPlease Select an Option : ");

            System.out.println("\n1. Display Demat account details\n"+
                    "2. Deposit Money\n" +
                    "3. Withdraw Money\n" +
                    "4. Buy transaction\n" +
                    "5. Sell transaction\n" +
                    "6. View transaction report\n" +
                    "0. Exit to Main Menu\n");
            choice = ControllerFactory.getScannerInstance().next();

            switch (choice) {
                case "1":
                    displayAccountDetails(accNo);
                    break;
                case "2":
                    depositeMoneyInAccount(accNo);
                    break;
                case "3":
                    withDrawMoney(accNo);
                    break;
                case "4":
                    System.out.println("Select Share from the below details---------");
                    displayAllSharePrice();
                    selectAndBuyShare(accNo);

                    break;
                case "5":
                    System.out.println("Select Share from the below details---------");
                    displayBoughtShareDetails(accNo);
                    selectAndSellShare(accNo);
                    break;
                case "6":
                    displayBoughtSoldTransactionDetails(accNo);
                case "0":
                    exitCode = true;
                    break;

                default:
                    System.out.println("Please Enter Valid Option\n");
            }
        }

        System.out.println("Returning to Previous Menu");

        return true;

    }

    private void depositeMoneyInAccount(long accNo) {
        System.out.println("Please enter the amount to deposit in the account balance.");
        DematAccount acc = accountService.accountDAO.get(accNo);
        int currentBal = acc.getBalance();
        long toDeposit;
        try{
            toDeposit = this.getAmount();
            acc.setBalance((int)(currentBal+toDeposit));
            accountService.accountDAO.update(acc);
            System.out.println("You balance is updated to :"+(currentBal+toDeposit));
        }catch(InvalidInputException e){
            System.out.println("Error "+e.getMessage());
        }
    }

    protected boolean isValidAccount(long accNo){
        return accountService.accountDAO.isValidDmatAccount(accNo);
    }

    protected boolean isAccountNumberExist(long accNo){
        return accountService.accountDAO.accountExist(accNo);
    }

    protected void createDmatAccount(DematAccount acc){
        accountService.accountDAO.add(acc);

    }
    protected boolean isValidAccountPassword(long accNo, String password){
        return accountService.accountDAO.isValidAccountAndPassword(accNo, password);
    }
    private void withDrawMoney(long accNo){
        DematAccount acc = accountService.accountDAO.get(accNo);
        Long currentBalance = Long.valueOf(acc.getBalance());
        System.out.println("WithDrawing money from account");
        System.out.println("Your Current balance is:"+currentBalance);
        try{
            Long toWithDraw = this.getAmount(currentBalance);
            acc.setBalance((int)(currentBalance - toWithDraw));
            accountService.accountDAO.update(acc);
            System.out.println("Your updated current balance is:"+(currentBalance - toWithDraw));
        }catch (InvalidInputException e){
            System.out.println("Error cannot withDraw"+e.getMessage());
        }
    }

    private void displayAccountDetails(long accNo) {
        System.out.println("Dmat Account Details are");
        DematAccount acc = accountService.accountDAO.get(accNo);
        System.out.println("Account No: "+acc.getAccountId()+"| Account Holder Name: "+acc.getAccHolderName()+"| Account Balance:"+acc.getBalance()+"| ShareHeldIds:"+acc.getShareIdHeld());
        System.out.println("Bought transactionIds: "+acc.getBoughtTransactionId()+"| Sold transactionIds:"+acc.getSoldTransactionId());
    }

    private boolean selectAndBuyShare(long accNo){
        long shareId;
        int unit;
        try{
            shareId = this.getShareId();
            unit = this.getUnitNum();
            if(!Validator.isPositive(shareId) || !Validator.isPositive(unit)){
                throw new InvalidInputException("-ve numbers are not allowed");
            }
            if(accountService.shareDAO.isValidShareAndUnitToBuy(shareId, unit)){
                buyShare(accNo, shareId, unit);

                return true;
            }else{
                throw new InvalidInputException("Error, either share is not available or entered units to Buy are more, or Insufficient balance");
            }

        }catch(Exception e){
            System.out.println("Error "+e.getMessage());
            return false;

        }
    }

    private boolean selectAndSellShare(long accNo){
        try{
            long shareId;
            int unit;
            shareId = this.getShareId();
            unit = this.getUnitNum();
            if(!Validator.isPositive(shareId) || !Validator.isPositive(unit)){
                throw new InvalidInputException("-ve numbers are not allowed");
            }

            if(accountService.accountDAO.isValidShareToSell(accNo, shareId) &&
            accountService.shareDAO.isValidShareAndUnitToSell(shareId, unit)){

                sellShare(accNo, shareId, unit);
                return true;
            }else{
                System.out.println("Not a valid share selected, or units");
                return false;
            }

        }catch(Exception e){
            System.out.println("Error.. did not sell"+e.getMessage());
            return false;
        }
    }

    private boolean buyShare(long accNo, long shareId, int unit){
        //see if total cost + addedCharge <= current balance
        //update share with units, total value
        //add new share row with new ShareId, availableToBuy(0), values etc
        //add a transaction bought(0), with dates and other details other
        //update this accounts boughtShare id, held ids etc
        Share s = accountService.shareDAO.get(shareId);
        DematAccount acc = accountService.accountDAO.get(accNo);
        int currBal = acc.getBalance();
        int totalCost = s.getCurrentPrice()*unit;
        int finalAfterTransactionCharge = (int)accountService.transactionDAO.addedChargeBuying(totalCost);
        if(finalAfterTransactionCharge <= currBal){
            //associated availableToBuy(0) share ID row in table
            Share newShareRecord = new Share();
            newShareRecord.setShareId(accountService.shareDAO.nextShareId());
            newShareRecord.setAvailableToBuy(0);
            newShareRecord.setCompanyName(s.getCompanyName());
            newShareRecord.setTotalUnits(unit);
            newShareRecord.setShareValue(totalCost);
            newShareRecord.setCurrentPrice(s.getCurrentPrice());

            //buy transaction
            Transaction t = new Transaction();
            t.setTransactionId(accountService.transactionDAO.nextTransactionId());
            t.setType(0);
            t.setDate((new Date()));
            t.setAccountId(accNo);
            t.setParentShareId(String.valueOf(shareId));
            t.setTotalCost((long)finalAfterTransactionCharge);
            t.setTransactionShareId(Long.toString(newShareRecord.getShareId()));

            //update user with balance, sharedheling, bought transaction
            acc.setBalance(currBal - finalAfterTransactionCharge);
            acc.addShareIdHeld(Long.toString(newShareRecord.getShareId()));
            acc.addBoughtTransactionId(Long.toString(t.getTransactionId()));

            //update account, add transaction, add share row
            accountService.accountDAO.update(acc);
            accountService.shareDAO.add(newShareRecord);
            accountService.shareDAO.updateBuy(shareId, unit);
            accountService.transactionDAO.add(t);
            System.out.println("You successfully bought "+unit+" units of "+s.getCompanyName()+" Company |At "+finalAfterTransactionCharge+"| your Current Balance: "+acc.getBalance());
            return true;
        }else{
            System.out.println("Not enough Balance to Buy.."+currBal+" VS "+finalAfterTransactionCharge);
            return false;
        }

    }

    private boolean sellShare(long accNo, long shareId, int unit){
        System.out.println("Selling selected share....");
        //get the amount of the share
        //update user balance, remove/modefy bought, add sold id
        //update share row with units and availability status
        //add a sold transaction
        DematAccount acc = accountService.accountDAO.get(accNo);
        Share selling = accountService.shareDAO.get(shareId);
        int soldAmt = selling.getCurrentPrice()*unit;
        int finalSoldAmt = (int) accountService.transactionDAO.addedChargeSelling(soldAmt);

        String parentShareId = accountService.transactionDAO.getParentShareId(shareId);
        //adding a share row in table
        Share newShareRow = new Share();
        newShareRow.setShareId(accountService.shareDAO.nextShareId());
        newShareRow.setTotalUnits(unit);
        newShareRow.setCurrentPrice(selling.getCurrentPrice());
        newShareRow.setCompanyName(selling.getCompanyName());
        newShareRow.setAvailableToBuy(3);//sold by individuals so just for records
        newShareRow.setShareValue(finalSoldAmt);

        //add sold transaction row
        Transaction t = new Transaction();
        t.setTransactionId(accountService.transactionDAO.nextTransactionId());
        t.setDate((new Date()));
        t.setType(1);//selling transaction
        t.setAccountId(accNo);
        t.setParentShareId(parentShareId);
        t.setTransactionShareId(String.valueOf(newShareRow.getShareId()));
        t.setTotalCost((long) soldAmt);


        //update user balance, ids strings
        acc.setBalance(finalSoldAmt + acc.getBalance());
        acc.addSoldTransactionId(String.valueOf(t.getTransactionId()));

        //need to update parent share of xample TSC as they are available now
        if(selling.getTotalUnits() == unit){
//            selling all units of with held share
            acc.removeShareIdHeld(Long.toString(shareId));
        }
        accountService.accountDAO.update(acc);
        accountService.shareDAO.add(newShareRow);
        accountService.shareDAO.updateSold(Long.valueOf(parentShareId), unit);
        accountService.shareDAO.updateBuy(Long.valueOf(shareId), unit);
        accountService.transactionDAO.add(t);
        System.out.println("You successfully Sold "+unit+" units of "+selling.getCompanyName()+" Company |At "+finalSoldAmt);
        System.out.println("Amount to be credited in you balance is :"+finalSoldAmt);

        return false;
    }

    private void displayAllSharePrice(){
        List<Share> allShare = accountService.shareDAO.retrieve();
        System.out.println("All available share to Buy are ...");
        for(Share s: allShare){
            if(s.getAvailableToBuy() == 1){
                System.out.println("Share Id: "+s.getShareId()+"| Company Name:"+s.getCompanyName()+"| Current Price:"+s.getCurrentPrice()+"| Available Share Units:"+s.getTotalUnits());
            }
        }
    }

    private void displayBoughtShareDetails(long accNo){
        List<String> heldIdShare = List.of(accountService.accountDAO.get(accNo).getShareIdHeld().split("|"));
        List<Transaction> boughtTransactionId = accountService.transactionDAO.getBoughtTransaction(accNo);

        System.out.println("You had following shareIds held/bought... "+heldIdShare.toString());
        for(Transaction t: boughtTransactionId){
            Share s = accountService.shareDAO.get(Long.parseLong(t.getTransactionShareId()));
            System.out.println("----------------");
            System.out.println("Share Id:"+t.getTransactionShareId()+"| Of parent Share "+t.getParentShareId()+"| Company Name:"+s.getCompanyName()+"| total Units held:"+s.getTotalUnits()+"\n| Share value:"+s.getShareValue()+"|Of Company name:"+s.getCompanyName()+"| You bought it a CostOf:"+t.getTotalCost());
            System.out.println("----------------");

        }
    }

    private void displayBoughtSoldTransactionDetails(long accNo){
        List<Transaction> allTransaction = accountService.transactionDAO.retrieve();
        for(Transaction t : allTransaction){
            Share s = accountService.shareDAO.get(Long.parseLong(t.getTransactionShareId()));
            if(t.getAccountId().equals(accNo) && t.getType() == 0){
                System.out.println("You bought amount: "+t.getTotalCost()+"| On :"+t.getDate()+"| of Company:"+s.getCompanyName()+"| units:"+s.getTotalUnits());
            }
            if(t.getAccountId().equals(accNo) && t.getType() == 1){
                System.out.println("You sold amount: "+t.getTotalCost()+"| On :"+t.getDate()+"| of Company:"+s.getCompanyName()+"| units:"+s.getTotalUnits());
            }
        }
    }

}
