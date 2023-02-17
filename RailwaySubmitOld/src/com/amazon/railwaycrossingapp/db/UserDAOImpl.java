package com.amazon.railwaycrossingapp.db;

import com.amazon.railwaycrossingapp.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UserDAOImpl implements DAO<User>{

    // users is a collection which will hold User Objects, BUT Temporarily i.e. till the application is running
    //user to User object map
    private LinkedHashMap<String, User> users = new LinkedHashMap<String, User>();
    private Connection conn;
    private Statement sqlSt;
    private String dbname;
    private String table1;
    private String table1Schema;
    private String table2Schema;
    private String table2;
    private String connectURL;



    private static UserDAOImpl userDAO;

    private UserDAOImpl(){
        conn = null;
        sqlSt = null;
        dbname = "RailwayCrossing";
        table1 = "railwayUsers";
        table2 = "crossing";
        table1Schema = "([email], [name], [password] , [userType])";
        table2Schema = "([cname] , [address] , [status] , [personEmail] , [fromTime] , [to_time])";

        connectURL = "jdbc:sqlserver://localhost:49726;datbaseName=HR;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";
    }

    public static UserDAOImpl getInstance(){
        if(userDAO == null){
            userDAO = new UserDAOImpl();
        }
        return userDAO;
    }

    public void populateUsers(){
        //get users from db
        List<User> railWayUsers = this.getUsers();
        //if db has values only then, set them up in MAP
        if(railWayUsers.size() != 0){
            for(User u : railWayUsers){
                System.out.println("Setting users in map from db");

                this.setInMap(u);
            }
        }else{
            //adding some admin users in map and db both using just set
            User u1 = new User("Tanay", "tan@abc.com", "1234", 2);
            User u2 = new User("Veer", "veer@abc.com", "4321", 2);
            User u3 = new User("Ravi", "ravi@abc.com", "1234", 3);
            railWayUsers.add(u1);
            railWayUsers.add(u2);
            railWayUsers.add(u3);
            //set in both map and db
            for(User u : railWayUsers){
                this.set(u);
            }
        }
    }
    //only set in map not in db
    public boolean setInMap(User u){
        if(!this.users.containsKey(u.getEmail())){
            this.users.put(u.getEmail(), u);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean set(User u) {
        if(!this.users.containsKey(u.getEmail())){
            this.users.put(u.getEmail(), u);
            this.insertUser(u);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean delete(User u) {
        if(this.users.containsKey(u.getEmail())){
            this.users.remove(u.getEmail());
            this.deleteUser(u);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public Map<String, ?> retrieve(User u) {
        return this.users;
    }

    @Override
    public User retrieve(String key) {
        if(this.users.containsKey(key)){
            return this.users.get(key);
        }else{
            return null;
        }
    }
    public int getUserCount() {
        return this.users.size();
    }

    //SQL operations for insertion and deletion

    private void connectToDB() {
        try {
            //connecting and setting up databases and 2 tables by default
            conn = DriverManager.getConnection(connectURL);
            if(conn != null) {
                System.out.println("Connected to the SQLServer...Success");
                System.out.println("Checking RailwayCrossing DB exist or not ");
                this.sqlSt = this.conn.createStatement();
                try {
                    sqlSt.execute("use "+dbname+";");
                }catch (Exception e){
                    //only if DB does not exist
                    System.out.println("DB not exist so creating DB and using it "+dbname);
                    sqlSt.execute("create database "+dbname+";");
                    sqlSt.execute("use "+dbname+";");
                    sqlSt.execute("create table "+table1+" ([email] [varchar](20) primary key not null , [name] [varchar](14), [password] [varchar](10), [userType] [int]);");
                    sqlSt.execute("create table "+table2+" ([crossingId] [int] primary key not null IDENTITY(1,1), [cname] [varchar](14), [address] [varchar](20), [status] [int], [personEmail] [varchar](20), [fromTime] [int], [to_time] [int]);");
                    System.out.println("SuccessFully created tables: "+table1);
                }
                sqlSt.execute("use "+dbname+";");
                System.out.println("Using DB as database already exist");
            }

        } catch (SQLException e) {
            System.out.println("Error connecting to the database...");
            this.disconnect();
        }
    }

    private boolean disconnect(){
        if(this.conn != null){
            try{
                this.conn.close();
                this.conn = null;
                System.out.println("Disconnecting from DB");
                return true;
            }catch (Exception e) {
                System.out.println("ERROR in disconnecting" + e.getMessage());
                conn = null;
                return false;
            }
        }else {
            System.out.println("No connection to disconnect");
            return false;
        }
    }


    public boolean insertUser(User u) {
        try{
            if(this.conn == null) {
                this.connectToDB();
            }
            sqlSt.execute("insert into "+table1+" "+table1Schema+" values ('"+u.getEmail()+"', '"+u.getName()+"', '"+u.getPassword()+"', '"+u.getUserType()+"');");
            System.out.println("Successfully added user: "+u.getEmail()+" into sql server ->"+dbname+" into table "+table1);
            this.disconnect();
            return true;
        }catch (Exception e) {
            System.out.println("ERROR inserting query" + e.getMessage());
            this.disconnect();
            return false;
        }
    }


    public boolean deleteUser(User u) {
        try{
            if(this.conn == null) {
                this.connectToDB();
            }
            sqlSt.execute("delete from "+table1+" where email = '"+u.getEmail()+"';");
            System.out.println("Successfully deleted crossing: "+u.getName()+" from sql server ->"+dbname+" from table "+table1);
            this.disconnect();
            return true;
        }catch (Exception e) {
            System.out.println("ERROR DELETING user query" + e.getMessage());
            this.disconnect();
            return false;
        }
    }



    public ArrayList<User> getUsers(){
        //retreaving crossings from db
        ArrayList<User> usersFromDB = new ArrayList<>();
        try{
            if(this.conn == null) {
                this.connectToDB();
            }
            ResultSet rs = sqlSt.executeQuery("select * from "+table1+";");
            while(rs.next()){
                User u = new User();
                u.setEmail(rs.getString(1));
                u.setName(rs.getString(2));
                u.setPassword(rs.getString(3));
                u.setUserType(rs.getInt(4));
                usersFromDB.add(u);
            }
//
//            System.out.println("User Obj created are");
//            for(User u : usersFromDB){
//                System.out.println(u.toString());
//            }

            this.disconnect();
            return usersFromDB;
        }catch (Exception e) {
            System.out.println("ERROR Creating objects of USER from query data " + e.getMessage());
            this.disconnect();
            return usersFromDB;
        }
    }



}
