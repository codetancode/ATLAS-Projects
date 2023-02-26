package com.amazon.dmataccountmanager.db;

import com.amazon.dmataccountmanager.model.Share;
import com.amazon.dmataccountmanager.model.UserShares;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ShareDAO implements DAO<Share> {
    DB db = DB.getInstance();
    @Override
    public int insert(Share object) {
        return 0;
    }

    @Override
    public int update(Share object) {
        return 0;
    }

    @Override
    public int delete(Share object) {
        return 0;
    }

    @Override
    public List<Share> retrieve() {
        String sql = "SELECT * from Share";
        ResultSet set = db.executeQuery(sql);

        ArrayList<Share> shares = new ArrayList<Share>();

        try {
            while(set.next()) {

                Share share = new Share();

                // Read the row from ResultSet and put the data into User Object
                share.id = set.getInt("id");
                share.price = set.getFloat("price");
                share.companyName = set.getString("companyName");
                share.lastUpdatedOn = set.getString("lastUpdatedOn");
                shares.add(share);
            }
        } catch (Exception e) {
            System.err.println("Something Went Wrong: "+e);
        }
        return shares;
    }

    @Override
    public List<Share> retrieve(String sql) {
        ResultSet set = db.executeQuery(sql);

        ArrayList<Share> shares = new ArrayList<Share>();

        try {
            while(set.next()) {

                Share share = new Share();

                // Read the row from ResultSet and put the data into User Object
                share.id = set.getInt("id");
                share.price = set.getInt("price");
                share.companyName = set.getString("companyName");
                share.lastUpdatedOn = set.getString("lastUpdatedOn");
                shares.add(share);
            }
        } catch (Exception e) {
            System.err.println("Something Went Wrong: "+e);
        }
        return shares;
    }
}
