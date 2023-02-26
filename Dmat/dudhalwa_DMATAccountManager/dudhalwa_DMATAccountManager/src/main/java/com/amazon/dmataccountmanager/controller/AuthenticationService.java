package com.amazon.dmataccountmanager.controller;

import com.amazon.dmataccountmanager.db.UserDAO;
import com.amazon.dmataccountmanager.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AuthenticationService {
    private static AuthenticationService service = new AuthenticationService();
    UserDAO dao = new UserDAO();

    private AuthenticationService(){

    }

    public static AuthenticationService getInstance() {
        return service;
    }

    public boolean registerUser(User user) {
        // Create account number
        user.accountNumber = (int)(Math.random()*(100000-1000+1)+1000);

        // Set account balance
        user.accountBalance = 0;

        // Set last updated on
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        user.lastUpdatedOn = currentDateTime.format(formatter);

        return dao.insert(user) > 0;
    }

    public boolean loginUser(User user) {
        String sql = "SELECT * FROM [User] WHERE accountNumber = '"+user.accountNumber+"' AND password = '"+user.password+"'";
        List<User> users = dao.retrieve(sql);

        if(users.size() > 0) {
            User u = users.get(0);
            user.id = u.id;
            user.username = u.username;
            user.accountBalance = u.accountBalance;
            user.lastUpdatedOn = u.lastUpdatedOn;
            return true;
        }

        return false;
    }

    public boolean updateUser(User user){
        // Set last updated on
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        user.lastUpdatedOn = currentDateTime.format(formatter);

        return dao.update(user) > 0;
    }
}
