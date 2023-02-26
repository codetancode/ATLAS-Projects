package dB;

import model.Transaction;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class TransactionDAOImpl implements DAO<Transaction> {

    LinkedHashMap<Long, Transaction> transaction = new LinkedHashMap<>();
    private int minCharge = 100;
    private double tranChargePercent = 0.5;
    private double sttPercent = 0.1;
    private static TransactionDAOImpl tranDAOImpl;
    private Connection connection;
    private Statement sqlStatement;
    private String conUrl = "";
    private String dbname;
    private String table;
    private String tableHeader;
    private String tableSchema;

    public static TransactionDAOImpl getInstance(){
        if(tranDAOImpl == null){
            tranDAOImpl = new TransactionDAOImpl();
        }
        return tranDAOImpl;
    }


    TransactionDAOImpl(){
        this.connection = null;
        this.sqlStatement = null;
        this.dbname = "amazon_dmatDB";
        this.table = "transaction";

        this.tableHeader = "(transactionId, minCharge, tranChargePercent, type, date, accountId, parentShareId,transactionShareId, totalCost)";

        this.tableSchema = "(\n" +
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

    public void autoPopulateTransaction(){
        syncWithDB();
        if(this.transaction.isEmpty()){
            Transaction t1 = new Transaction(1002L, 0, new Date(), 2124L, "1","5", addedChargeBuying(163200));
            Transaction t2 = new Transaction(1003L, 0, new Date(), 42632L, "4", "6", addedChargeBuying(1160));
            add(t1);
            add(t2);
        }
    }

    public long addedChargeBuying(int totalCost){
        return totalCost < minCharge?100L:(long)(totalCost*tranChargePercent + totalCost + totalCost*sttPercent);
    }

    public long addedChargeSelling(int sellAmt){
        return sellAmt < minCharge?-100L:(long)(sellAmt - sellAmt*tranChargePercent - sellAmt*sttPercent);
    }

    @Override
    public boolean add(Transaction tran) {
        //add in map and in db
        this.transaction.put(tran.getTransactionId(), tran);
        try{
            if(this.connection == null) {
                this.connectToDB();
            }
            this.sqlStatement = this.connection.createStatement();
            this.sqlStatement.execute("insert into "+table+" "+tableHeader+" values ('"+tran.getTransactionId()+"', '"+tran.getMinCharge()+"', '"+tran.getTranChargePercent()+"','"+tran.getType()+"', '"+tran.getDate()+"', '"+tran.getAccountId()+"', '"+tran.getParentShareId()+"','"+tran.getTransactionShareId()+"', '"+tran.getTotalCost()+"'); ");
            this.disconnect();
            return true;
        }catch (Exception e) {
            System.out.println("Error tran"+e.getMessage());
            this.disconnect();
            return false;
        }

    }

    @Override
    public boolean update(Transaction transaction) {
        return false;
    }

    @Override
    public Transaction get(long id) {
        if(this.transaction.containsKey(id)){
            return this.transaction.get(id);
        }else{
            return null;
        }
    }

    public String getParentShareId(long childShareId){
        for(Long trId : this.transaction.keySet()){
            if(this.transaction.get(trId).getTransactionShareId().equals(Long.toString(childShareId))){
                return this.transaction.get(trId).getParentShareId();
            }
        }
        return null;
    }

    public List<Transaction> getBoughtTransaction(Long accNo){
        ArrayList<Transaction> tranFromDB = new ArrayList<>();
        try{
            if(this.connection == null) {
                this.connectToDB();
            }
            this.sqlStatement = connection.createStatement();
            System.out.println("select * from "+table+" where type='"+0+"' AND accountId='"+accNo+"';");
            ResultSet rs = this.sqlStatement.executeQuery("select * from "+table+" where type='"+0+"' AND accountId='"+accNo+"';");

            while(rs.next()){
                Transaction t = new Transaction();
                t.setTransactionId(rs.getLong(1));
                t.setMinCharge(rs.getInt(2));
                t.setTranChargePercent(rs.getDouble(3));
                t.setType(rs.getInt(4));
                t.setDate(new SimpleDateFormat("dd-M-yyyy hh:mm:ss").parse(rs.getString(5)));
                t.setAccountId(rs.getLong(6));
                t.setParentShareId(rs.getString(7));
                t.setTransactionShareId(rs.getString(8));
                t.setTotalCost(rs.getLong(9));
                tranFromDB.add(t);
            }

            for(Transaction t : tranFromDB){
                System.out.println(t.getTransactionId()+" "+t.getTransactionShareId());
            }
            this.disconnect();
            return tranFromDB;

        }catch (Exception e) {
            System.out.println("Error in sql "+e.getMessage());
            this.disconnect();
            return tranFromDB;
        }

    }

    @Override
    public List<Transaction> retrieve() {
        ArrayList<Transaction> tranFromDB = new ArrayList<>();
        try{
            if(this.connection == null) {
                this.connectToDB();
            }
            this.sqlStatement = connection.createStatement();
            ResultSet rs = this.sqlStatement.executeQuery("select * from "+table+";");

            while(rs.next()){
                Transaction t = new Transaction();
                t.setTransactionId(rs.getLong(1));
                t.setMinCharge(rs.getInt(2));
                t.setTranChargePercent(rs.getDouble(3));
                t.setType(rs.getInt(4));
                t.setDate(new SimpleDateFormat("dd-M-yyyy hh:mm:ss").parse(rs.getString(5)));
                t.setAccountId(rs.getLong(6));
                t.setParentShareId(rs.getString(7));
                t.setTransactionShareId(rs.getString(8));
                t.setTotalCost(rs.getLong(9));
                tranFromDB.add(t);
            }

//            for(Transaction t : tranFromDB){
//                System.out.println(t.getTransactionId()+" "+t.getTransactionShareId());
//            }
            this.disconnect();
            return tranFromDB;

        }catch (Exception e) {
            System.out.println("Error in sql "+e.getMessage());
            this.disconnect();
            return tranFromDB;
        }
    }


    public void syncWithDB(){
        List<Transaction> tranFromDB = this.retrieve();
        System.out.println("Retreive transactions from db "+tranFromDB.size());
        if(tranFromDB.size() > this.transaction.keySet().size()){
            for(Transaction u : tranFromDB){
                if(!this.transaction.containsKey(u.getTransactionId())){
                    this.transaction.put(u.getTransactionId(), u);
                }
            }
        }
    }
    public long nextTransactionId(){
        syncWithDB();
        long nxtId = 0;
        for(Long id : this.transaction.keySet()){
            nxtId = Math.max(nxtId, id);
        }
        return nxtId+1;
    }

}
