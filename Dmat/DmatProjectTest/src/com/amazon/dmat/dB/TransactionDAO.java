package com.amazon.dmat.dB;

import com.amazon.dmat.model.Transaction;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO implements DAO<Transaction> {
    DB db = DB.getInstance();
    @Override
    public int insert(Transaction object) {
        String sql = "INSERT INTO [transaction] (userID, shareID, shareCount, pricePerShare, transactedOn, transactionCharges, type, sttCharges) VALUES (" + object.userID + "," + object.shareID + "," + object.shareCount + "," + object.pricePerShare + ",'" + object.transactedOn + "',"+object.transactionCharges + "," + object.type + ","+object.sttCharges+")";
        return db.executeSQL(sql);
    }

    @Override
    public int update(Transaction object) {
        return 0;
    }

    @Override
    public int delete(Transaction object) {
        return 0;
    }

    @Override
    public List<Transaction> retrieve() {
        return null;
    }

    @Override
    public List<Transaction> retrieve(String sql) {
        ResultSet set = db.executeQuery(sql);

        ArrayList<Transaction> transactions = new ArrayList<Transaction>();

        try {
            while(set.next()) {

                Transaction transaction = new Transaction();

                // Read the row from ResultSet and put the data into User Object
                transaction.id = set.getInt("id");
                transaction.userID = set.getInt("userId");
                transaction.shareID = set.getInt("shareId");
                transaction.shareCount = set.getInt("shareCount");
                transaction.pricePerShare = set.getFloat("pricePerShare");
                transaction.transactedOn = set.getString("transactedOn");
                transaction.transactionCharges = set.getFloat("transactionCharges");
                transaction.type = set.getInt("type");
                transaction.sttCharges = set.getFloat("sttCharges");

                transactions.add(transaction);
            }
        } catch (Exception e) {
            System.err.println("Something Went Wrong: "+e);
        }

        return transactions;
    }
}
