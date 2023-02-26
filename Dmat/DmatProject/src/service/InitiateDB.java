package service;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class InitiateDB {
    private Connection connection;
    private Statement sqlStatement;
    private String conUrl = "";
    private String dbname;
    private String table1;
    private String table2;
    private String table3;


    private String tableHeader1;
    private String tableHeader2;

    private String tableHeader3;

    private String tableSchema1;
    private String tableSchema2;

    private String tableSchema3;

    private static InitiateDB initiateDB;
    public static InitiateDB getInstance(){
        if(initiateDB == null){
            initiateDB = new InitiateDB();
        }
        return initiateDB;
    }

    private InitiateDB(){
        this.connection = null;
        this.sqlStatement = null;
        this.dbname = "amazon_dmatDB";
        this.table1 = "account";
        this.table2 = "share";
        this.table3 = "transaction";


        this.tableSchema1 = "(\n" +
                "        accountId int,\n" +
                "        accHolderName varchar(50),\n" +
                "        password varchar(50),\n" +
                "        balance int,\n" +
                "        shareIdHeld varchar(200),\n" +
                "        boughtTransactionId varchar(200),\n" +
                "        soldTransactionId varchar(200),\n" +
                " PRIMARY KEY (accountId) );";
        this.tableSchema2 = "(\n" +
                "        shareId int,"+
                "        companyName varchar(200),\n" +
                "        availableToBuy int,\n"+
                "        currentPrice int,\n" +
                "        totalUnits int,\n" +
                "        shareValue int, \n" +
                " PRIMARY KEY (shareId));";
        this.tableSchema3 = "(\n" +
                "        transactionId int,"+
                "        minCharge int,\n" +
                "        tranChargePercent varchar(10),\n" +
                "        type int,\n" +
                "        date varchar(500),\n" +
                "        accountId int,\n" +
                "        parentShareId varchar(500),\n"+
                "        transactionShareId varchar(500), \n" +
                "        totalCost int,\n" +
                " PRIMARY KEY(transactionId));";


        this.conUrl = "jdbc:mysql://localhost:3306/mysql?serverTimezone=UTC";
    }


    public void initConnectToDB() throws SQLException, ClassNotFoundException{
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //connecting and setting up databases and 2 tables by default
            this.connection = DriverManager.getConnection(this.conUrl, "root", "");
            if(this.connection != null) {
                this.sqlStatement = this.connection.createStatement();
                try {
                    this.sqlStatement.execute("use "+dbname+";");
                }catch (Exception e){
                    //only if DB does not exist
                    System.out.println("DB not exist so creating DB and using it "+dbname);
                    sqlStatement.execute("create database "+dbname+";");
                    sqlStatement.execute("use "+dbname+";");

                    sqlStatement.execute("create table "+table1+" "+tableSchema1);
                    System.out.println("SuccessFully created tables: "+table1);

                    sqlStatement.execute("create table "+table2+" "+tableSchema2);
                    System.out.println("SuccessFully created tables: "+table2);

                    sqlStatement.execute("create table "+table3+" "+tableSchema3);
                    System.out.println("SuccessFully created tables: "+table3);
                }
                sqlStatement.execute("use "+dbname+";");
                System.out.println("Using DB as database already exist");
            }

        } catch (SQLException e) {
            System.out.println("Error connecting to the database...Server might not be running.."+e.getMessage());
            this.initDisconnect();
        }
    }
    public boolean initDisconnect(){
        if(this.connection != null){
            try{
                this.connection.close();
                this.connection = null;
                System.out.println("Disconnecting from DB");
                return true;
            }catch (Exception e) {
                System.out.println("ERROR in disconnecting" + e.getMessage());
                this.connection = null;
                return false;
            }
        }else {
            System.out.println("No connection to disconnect");
            return false;
        }
    }



}
