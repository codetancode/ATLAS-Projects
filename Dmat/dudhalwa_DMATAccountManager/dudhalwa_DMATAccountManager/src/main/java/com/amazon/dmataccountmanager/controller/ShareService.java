package com.amazon.dmataccountmanager.controller;

import com.amazon.dmataccountmanager.db.ShareDAO;
import com.amazon.dmataccountmanager.db.UserSharesDAO;
import com.amazon.dmataccountmanager.model.Share;
import com.amazon.dmataccountmanager.model.UserShares;

import java.util.ArrayList;
import java.util.List;

public class ShareService {
    private static ShareService service = new ShareService();

    ShareDAO shareDAO = new ShareDAO();
    UserSharesDAO userSharesDAO = new UserSharesDAO();

    private ShareService(){

    }

    public static ShareService getInstance(){return service;}

    public boolean buyShare(UserShares userShares){
        // Check if row with user id and share id exists
        String sql = "SELECT * from userShare WHERE userId=" + userShares.userId + "AND shareId=" + userShares.shareId;
        List<UserShares> sharesArrayList = userSharesDAO.retrieve(sql);

        if (sharesArrayList.size() > 0){
            userShares.shareCount += sharesArrayList.get(0).shareCount;
            return userSharesDAO.update(userShares) > 0;
        }
        else {
            return userSharesDAO.insert(userShares) > 0;
        }
    }

    public boolean sellShare(UserShares userShares){
        return userSharesDAO.update(userShares) > 0;
    }

    public List<Share> getAllShares(){
        return shareDAO.retrieve();
    }

    public List<UserShares> getUserShares(int userId){
        String sql = "SELECT * from userShare where userId="+userId;
        return userSharesDAO.retrieve(sql);
    }

    public Share getShareDetails(int shareId){
        String sql = "SELECT * from Share where id="+shareId;
        return shareDAO.retrieve(sql).get(0);
    }
}
