package com.amazon.dmat.dB;

import com.amazon.dmat.model.UserShares;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserSharesDAO implements DAO<UserShares>{
    DB db = DB.getInstance();
    @Override
    public int insert(UserShares object) {
        String sql = "INSERT INTO userShare (userId, shareId, companyName, shareCount) VALUES ("+object.userId+","+object.shareId+",'"+object.companyName+"',"+object.shareCount+")";
        return db.executeSQL(sql);
    }

    @Override
    public int update(UserShares object) {
        String sql = "UPDATE userShare set shareCount="+object.shareCount;
        return db.executeSQL(sql);
    }

    @Override
    public int delete(UserShares object) {
        return 0;
    }

    @Override
    public List<UserShares> retrieve() {
        return null;
    }

    @Override
    public List<UserShares> retrieve(String sql) {
        ResultSet set = db.executeQuery(sql);

        ArrayList<UserShares> userShares = new ArrayList<UserShares>();

        try {
            while(set.next()) {

                UserShares userShare = new UserShares();

                // Read the row from ResultSet and put the data into User Object
                userShare.id = set.getInt("id");
                userShare.userId = set.getInt("userId");
                userShare.shareId = set.getInt("shareId");
                userShare.companyName = set.getString("companyName");
                userShare.shareCount = set.getInt("shareCount");
                userShares.add(userShare);
            }
        } catch (Exception e) {
            System.err.println("Something Went Wrong: "+e);
        }
        return userShares;
    }
}
