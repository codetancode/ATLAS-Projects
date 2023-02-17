package com.amazon.railwaycrossingapp.db;

import com.amazon.railwaycrossingapp.model.RailwayCrossing;
import com.amazon.railwaycrossingapp.model.Schedule;
import com.amazon.railwaycrossingapp.model.User;

import java.sql.*;
import java.util.*;

public class CrossingDAOImpl implements DAO<RailwayCrossing>{

    // crossings is a collection which will hold Railway Crossing Objects, BUT Temporarily i.e. till the application is running
    //Person Incharge email to crossing object map
    private Map<String, RailwayCrossing> crossings = new LinkedHashMap<String, RailwayCrossing>();
    private Connection conn;
    private Statement sqlSt;
    private String dbname;
    private String table1;
    private String table1Schema;
    private String table2Schema;
    private String table2;
    private String connectURL;

    private static CrossingDAOImpl crossingDAO;

    private CrossingDAOImpl(){
        //initialize
        conn = null;
        sqlSt = null;
        dbname = "RailwayCrossing";
        table1 = "railwayUsers";
        table2 = "crossing";
        table1Schema = "([email], [name], [password] , [userType])";
        table2Schema = "([cname] , [address] , [status] , [personEmail] , [fromTime] , [to_time])";

        connectURL = "jdbc:sqlserver://localhost:49726;datbaseName=HR;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";
    }

    public static CrossingDAOImpl getInstance(){
        if(crossingDAO == null){
            crossingDAO = new CrossingDAOImpl();
        }
        return crossingDAO;
    }



    public void populateCrossings(){
        //get crossing from DB
        System.out.println("Auto populate crossings");
        List<RailwayCrossing> railCrossing = this.getCrossings();
        System.out.println("");
        //if db has values only then, set them up in MAP
        if(railCrossing.size() != 0){
            for(RailwayCrossing rc : railCrossing){
                System.out.println("Setting crossing in map from db");
                this.setInMap(rc);
            }
        }
    }

    //only set in map not in db
    public boolean setInMap(RailwayCrossing railwayCrossing){
        if(!this.crossings.containsKey(railwayCrossing.getPersonInCharge().getEmail())){
            this.crossings.put(railwayCrossing.getPersonInCharge().getEmail(), railwayCrossing);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean set(RailwayCrossing railwayCrossing) {
        if(!this.crossings.containsKey(railwayCrossing.getPersonInCharge().getEmail())){
            this.crossings.put(railwayCrossing.getPersonInCharge().getEmail(), railwayCrossing);
            //insert into db
            this.insertCrossing(railwayCrossing, railwayCrossing.getPersonInCharge(), railwayCrossing.getSchedules());
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean delete(RailwayCrossing railwayCrossing) {
        if(this.crossings.containsKey(railwayCrossing.getPersonInCharge().getEmail())){
            this.crossings.remove(railwayCrossing.getPersonInCharge().getEmail());
            this.deleteCrossing(railwayCrossing);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public Map<String, ?> retrieve(RailwayCrossing railwayCrossing) {
        return this.crossings;
    }

    @Override
    public RailwayCrossing retrieve(String key) {
        if(this.crossings.containsKey(key)){
            return this.crossings.get(key);
        }else{
            return null;
        }
    }

    public int getCrossingsCount() {
        return this.crossings.size();
    }


    public RailwayCrossing getCrossingByName(String crossingName) {
        for (RailwayCrossing railwayCrossing : this.crossings.values()) {
            if (railwayCrossing.getName().equalsIgnoreCase(crossingName))
                return railwayCrossing;
        }
      return null;
    }

    public List<Map.Entry<String, RailwayCrossing>> sortRailwayCrossingByFromString() {
        //just making Linked to tree map as keys are sorted
        List<Map.Entry<String, RailwayCrossing>> temp = new LinkedList<>(this.crossings.entrySet());
        //sort based on from time of the crossings
        Collections.sort(temp, Comparator.comparingInt(o -> o.getValue().getSchedules().from));
        return temp;
    }





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
                    sqlSt.execute("create table "+table2+" ([crossingId] [int] primary key not null IDENTITY(1,1), [cname] [varchar](40) not null, [address] [varchar](100), [status] [int], [personEmail] [varchar](40), [fromTime] [int], [to_time] [int]);");
                    System.out.println("SuccessFully created tables: "+table2);
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




    public boolean insertCrossing(RailwayCrossing c, User personIncharge, Schedule sc) {
        try{
            if(this.conn == null) {
                this.connectToDB();
            }
            //need person incharge id
            sqlSt.execute("insert into "+table2+" "+table2Schema+" values ('"+c.getName()+"', '"+c.getAddress()+"', "+c.getStatus()+", '"+personIncharge.getEmail()+"', '"+sc.from+"', '"+sc.to+"');");
            System.out.println("Successfully added crossing: "+c.getName()+" into sql server ->"+dbname+" into table "+table2);
            this.disconnect();
            return true;
        }catch (Exception e) {
            System.out.println("ERROR inserting query" + e.getMessage());
            this.disconnect();
            return false;
        }
    }


    public boolean deleteCrossing(RailwayCrossing c) {
        try{
            if(this.conn == null) {
                this.connectToDB();
            }
            sqlSt.execute("delete from "+table2+" where personEmail = '"+c.getPersonInCharge().getEmail()+"';");
            System.out.println("Successfully deleted crossing: "+c.getName()+" from sql server ->"+dbname+" from table "+table2);
            this.disconnect();
            return true;
        }catch (Exception e) {
            System.out.println("ERROR DELETING crossing query" + e.getMessage());
            this.disconnect();
            return false;
        }
    }


    public ArrayList<RailwayCrossing> getCrossings(){
        //retrieving crossings from db
        ArrayList<RailwayCrossing> crossingFromDB = new ArrayList<>();
        try{
            if(this.conn == null) {
                this.connectToDB();
            }
            ResultSet rs = sqlSt.executeQuery("select * from dbo."+table2+", dbo."+table1+" where dbo."+table2+".personEmail = dbo."+table1+".email;");
            while(rs.next()){
                //for every row
                RailwayCrossing rc = new RailwayCrossing();
                rc.setID(rs.getInt(1));
                rc.setName(rs.getString(2));
                rc.setAddress(rs.getString(3));
                rc.setStatus(rs.getInt(4));
                //getting values for user and schedule oj
                User personIn = new User();
                Schedule sch = new Schedule(rs.getInt(6), rs.getInt(7));
                personIn.setEmail(rs.getString(5));
                personIn.setName(rs.getString(9));
                personIn.setPassword(rs.getString(10));
                personIn.setUserType(rs.getInt(11));

                //adding 2 obj userPerson and schedule in RailwayCrossing,
                rc.setPersonInCharge(personIn);
                rc.setSchedules(sch);

                crossingFromDB.add(rc);
            }

            System.out.println("Crossing Obj created are..");
            for(RailwayCrossing u : crossingFromDB){
                System.out.println(u.toString());
            }

            this.disconnect();
            return crossingFromDB;
        }catch (Exception e) {
            System.out.println("ERROR creating objects of CROSSING query" + e.getMessage());
            this.disconnect();
            return crossingFromDB;
        }
    }

    public boolean updateCrossingStatus(RailwayCrossing rc){
        try{
            if(this.conn == null) {
                this.connectToDB();
            }
            sqlSt.execute("update "+table2+" set [status] = "+rc.getStatus()+" where personEmail = '"+rc.getPersonInCharge().getEmail()+"' and cname like '"+rc.getName()+"';");
            System.out.println("Successfully changed crossing status : "+rc.getName()+" from sql server ->"+dbname+" from table "+table2);
            this.disconnect();
            return true;
        }catch (Exception e) {
            System.out.println("ERROR UPDATING crossing status query" + e.getMessage());
            this.disconnect();
            return false;
        }
    }



}
