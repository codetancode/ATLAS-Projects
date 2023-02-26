package com.amazon.dmat.dB;

import com.amazon.dmat.model.User;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements DAO<User>{

    DB db = DB.getInstance();

    public int insert(User object) {
        String sql = "INSERT INTO [User] (username, accountNumber, password, accountBalance, lastUpdatedOn) VALUES ('"+object.username+"', "+object.accountNumber+", '"+object.password+"', "+object.accountBalance+", '"+object.lastUpdatedOn+"')";
        return db.executeSQL(sql);
    }

    public int update(User user) {
        String sql = "UPDATE [User] set accountBalance=" + user.accountBalance + ", lastUpdatedOn='" + user.lastUpdatedOn + "' WHERE id=" + user.id;
        return db.executeSQL(sql);
    }

    public int delete(User object) {
        return 0;
    }

    public List<User> retrieve() {
        return null;
    }

    public List<User> retrieve(String sql) {
        ResultSet set = db.executeQuery(sql);

        ArrayList<User> users = new ArrayList<User>();

        try {
            while(set.next()) {

                User user = new User();

                // Read the row from ResultSet and put the data into User Object
                user.id = set.getInt("id");
                user.username = set.getString("username");
                user.accountNumber = set.getInt("accountNumber");
                user.password = set.getString("password");
                user.accountBalance = set.getFloat("accountBalance");
                user.lastUpdatedOn = set.getString("lastUpdatedOn");

                users.add(user);
            }
        } catch (Exception e) {
            System.err.println("Something Went Wrong: "+e);
        }


        return users;
    }

}
