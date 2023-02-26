package com.amazon.dmataccountmanager.controller;

import com.amazon.dmataccountmanager.Config;
import com.amazon.dmataccountmanager.db.TransactionDAO;
import com.amazon.dmataccountmanager.model.Transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TransactionService {
    private static TransactionService service = new TransactionService();

    TransactionDAO dao = new TransactionDAO();

    private TransactionService(){

    }

    public static TransactionService getInstance(){return service;}

    private String setTransactedTime(){
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return currentDateTime.format(formatter);
    }

    private float getTransactionCharges(int shareCount, float pricePerShare){
        float totalAmount = shareCount * pricePerShare;

        double transactionCharges = totalAmount * (Config.TransactionCharge / 100);

        if (transactionCharges < Config.MinimumTransactionCharge){
            transactionCharges = Config.MinimumTransactionCharge;
        }

        return (float) transactionCharges;
    }

    private float getSttCharges(int shareCount, float pricePerShare){
        float totalAmount = shareCount * pricePerShare;

        double sttCharges = totalAmount * (Config.SecuritiesTransferTax / 100);
        return (float) sttCharges;
    }

    public boolean addBuyTransaction(Transaction transaction){
        transaction.type = 1;

        // Set transacted time
        transaction.transactedOn = setTransactedTime();

        // Calculate transaction charges
        transaction.transactionCharges = getTransactionCharges(transaction.shareCount, transaction.pricePerShare);

        // Calculate stt charges
        transaction.sttCharges = getSttCharges(transaction.shareCount, transaction.pricePerShare);

        transaction.totalCost = (transaction.shareCount * transaction.pricePerShare) - (transaction.transactionCharges - transaction.sttCharges);

        return dao.insert(transaction) > 0;
    }

    public boolean addSellTransaction(Transaction transaction){
        transaction.type = 2;

        // Set transacted time
        transaction.transactedOn = setTransactedTime();

        // Calculate transaction charges
        transaction.transactionCharges = getTransactionCharges(transaction.shareCount, transaction.pricePerShare);

        // Calculate stt charges
        transaction.sttCharges = getSttCharges(transaction.shareCount, transaction.pricePerShare);

        transaction.totalCost = (transaction.shareCount * transaction.pricePerShare) - (transaction.transactionCharges - transaction.sttCharges);
        return dao.insert(transaction) > 0;
    }

    public List<Transaction> getTransactions(String sql){
        return dao.retrieve(sql);
    }
}
