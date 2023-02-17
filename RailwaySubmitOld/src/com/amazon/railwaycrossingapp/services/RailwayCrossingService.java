package com.amazon.railwaycrossingapp.services;

import com.amazon.railwaycrossingapp.db.UserDAOImpl;
import com.amazon.railwaycrossingapp.db.CrossingDAOImpl;

public class RailwayCrossingService {
    public UserDAOImpl uDAO;
    public CrossingDAOImpl cDAO;

    public RailwayCrossingService(){
        uDAO = UserDAOImpl.getInstance();
        cDAO = CrossingDAOImpl.getInstance();
        //sync with db
        uDAO.populateUsers();
        cDAO.populateCrossings();
    }


}
