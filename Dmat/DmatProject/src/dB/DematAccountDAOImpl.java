package dB;

import model.DematAccount;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


public class DematAccountDAOImpl implements DAO<DematAccount> {

    LinkedHashMap<Long, DematAccount> dmatAccounts = new LinkedHashMap<>();

    private Connection connection;
    private Statement sqlStatement;
    private String conUrl = "";
    private String dbname;
    private String table;
    private String tableHeader;
    private String tableSchema;

    private static DematAccountDAOImpl accountDAOImpl;
    public static DematAccountDAOImpl getInstance(){
        if(accountDAOImpl == null){
            accountDAOImpl = new DematAccountDAOImpl();
        }
        return accountDAOImpl;
    }
    DematAccountDAOImpl(){
        this.connection = null;
        this.sqlStatement = null;
        this.dbname = "amazon_dmatDB";
        this.table = "account";
        this.tableHeader = "(accountId, accHolderName, password, balance, shareIdHeld, boughtTransactionId, soldTransactionId)";
        this.tableSchema = "(\n" +
                "        accountId int,\n" +
                "        accHolderName varchar(50),\n" +
                "        password varchar(50),\n" +
                "        balance int,\n" +
                "        shareIdHeld varchar(200),\n" +
                "        boughtTransactionId varchar(200),\n" +
                "        soldTransactionId varchar(200),\n" +
                " PRIMARY KEY (accountId) );";

        this.conUrl = "jdbc:mysql://localhost:3306/amazon_dmatDB?serverTimezone=UTC";
    }

    public void connectToDB() throws SQLException, ClassNotFoundException{
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            this.connection = DriverManager.getConnection(this.conUrl, "root", "");
            this.sqlStatement = this.connection.createStatement();
            this.sqlStatement.execute("use "+dbname+";");

        } catch (SQLException e) {
            System.out.println("Error connecting to the database IN accountDAO dao..."+e.getMessage());
            this.disconnect();
        }
    }



    public boolean disconnect(){
        if(this.connection != null){
            try{
                this.connection.close();
                this.connection = null;
                System.out.println("Disconnecting from DB accountDAO");
                return true;
            }catch (Exception e) {
                System.out.println("ERROR in disconnecting accountDAO" + e.getMessage());
                this.connection = null;
                return false;
            }
        }else {
            System.out.println("No connection to disconnect accountDAO");
            return false;
        }
    }

    public void autoPopulateAccount(){
        syncWithDB();
        if(this.dmatAccounts.isEmpty()){
            DematAccount acc1 = new DematAccount(2124L, "random name","12password12", 0, "5", "1002", null);
            DematAccount acc2 = new DematAccount(42632L, "random name1","12password12", 2450, "6", "1003", null);
            DematAccount acc3 = new DematAccount(98742L, "random name2", "12password12", 1950, null, null, null);
            DematAccount acc4 = new DematAccount(2112L, "random name3", "12password12", 210,null, null, null);
            add(acc1);
            add(acc2);
            add(acc3);
            add(acc4);
        }

    }

    @Override
    public boolean add(DematAccount dAccount) {
        //add in map and in db
        this.dmatAccounts.put(dAccount.getAccountId(), dAccount);
        try{
            if(this.connection == null) {
                this.connectToDB();
            }
            this.sqlStatement = this.connection.createStatement();
            this.sqlStatement.execute("insert into "+table+" "+tableHeader+" values ('"+dAccount.getAccountId()+"', '"+dAccount.getAccHolderName()+"', '"+dAccount.getPassword()+"', '"+dAccount.getBalance()+"', '"+dAccount.getShareIdHeld()+"', '"+dAccount.getBoughtTransactionId()+"','"+dAccount.getSoldTransactionId()+"' );");
            this.disconnect();
            return true;
        }catch (Exception e) {
            this.disconnect();
            return false;
        }
    }

    @Override
    public boolean update(DematAccount dAccount) {
        System.out.println("Updating account.."+dAccount.getAccHolderName());
        this.dmatAccounts.put(dAccount.getAccountId(), dAccount);
        try{
            if(this.connection == null) {
                this.connectToDB();
            }
            this.sqlStatement = this.connection.createStatement();
            this.sqlStatement.execute("update "+table+" set balance='"+dAccount.getBalance()+"', shareIdHeld='"+dAccount.getShareIdHeld()+"', boughtTransactionId='"+dAccount.getBoughtTransactionId()+"', soldTransactionId='"+dAccount.getSoldTransactionId()+"' where accountId='"+dAccount.getAccountId()+"';");
            this.disconnect();
            return true;
        }catch (Exception e) {
            System.out.println("error "+e.getMessage());
            this.disconnect();
            return false;
        }
    }

    @Override
    public DematAccount get(long accId) {
        if(this.dmatAccounts.containsKey(accId)){
            return this.dmatAccounts.get(accId);
        }else{
            return null;
        }
    }

    @Override
    public List<DematAccount> retrieve() {
        ArrayList<DematAccount> accountFromDB = new ArrayList<>();
        try{
            if(this.connection == null) {
                this.connectToDB();
            }
            this.sqlStatement = connection.createStatement();
            ResultSet rs = this.sqlStatement.executeQuery("select * from "+table+";");

            while(rs.next()){
                DematAccount acc = new DematAccount();
                acc.setAccountId((long) rs.getInt(1));
                acc.setAccHolderName(rs.getString(2));
                acc.setPassword(rs.getString(3));
                acc.setBalance(rs.getInt(4));
                acc.addShareIdHeld(rs.getString(5));
                acc.addBoughtTransactionId(rs.getString(6));
                acc.addBoughtTransactionId(rs.getString(7));

                accountFromDB.add(acc);
            }

//            for(DematAccount u : accountFromDB){
//                System.out.println(u.getAccHolderName());
//            }
            this.disconnect();
            return accountFromDB;

        }catch (Exception e) {
            System.out.println("Error in sql "+e.getMessage());
            this.disconnect();
            return accountFromDB;
        }
    }

    public void syncWithDB(){
        List<DematAccount> accountFromDB = this.retrieve();
        System.out.println("Retreive accounts from db "+accountFromDB.size());
        if (accountFromDB.size() > this.dmatAccounts.keySet().size()){
            for(DematAccount u : accountFromDB){
                if(!this.dmatAccounts.containsKey(u.getAccountId())){
                    this.dmatAccounts.put(u.getAccountId(), u);
                }
            }
        }
    }

    public boolean isValidDmatAccount(long accNo){
        System.out.println("filled dmat map"+this.dmatAccounts.get(accNo).getAccHolderName());

        return this.dmatAccounts.containsKey(accNo);
    }
    public boolean isValidShareToSell(long accNo, long sid){
        if(this.dmatAccounts.containsKey(accNo)){
            DematAccount a = this.dmatAccounts.get(accNo);
            if(a.getShareIdHeld() != null && a.getBoughtTransactionId() != null){
                //have some share to sell
                List<String> shareIdHeld = List.of(a.getShareIdHeld().split("|"));
                System.out.println("held share "+shareIdHeld);
                if(shareIdHeld.contains(Long.toString(sid))){
                    return true;
                }else{
                    System.out.println("You hav not heled this ShareId ");
                    return false;
                }
            }else{
                System.out.println("No bought shares in account");
                return false;
            }
        }else {
            System.out.println("account not found");
            return false;
        }
    }
    public boolean isValidAccountAndPassword(long accNo, String password){
        if(this.dmatAccounts.containsKey(accNo)){
            System.out.println("valid acc pass");
            if(this.dmatAccounts.get(accNo).getPassword().equals(password)){
                System.out.println("filled dmat map"+this.dmatAccounts.get(accNo).getAccHolderName());
                System.out.println("valid acc pass, password equals");
                return true;
            }
        }
        return false;
    }



}
