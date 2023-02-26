package dB;

import model.DematAccount;
import model.Share;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ShareDAOImpl implements DAO<Share>{

    LinkedHashMap<Long, Share> share = new LinkedHashMap<>();

    private Connection connection;
    private Statement sqlStatement;
    private String conUrl = "";
    private String dbname;
    private String table;
    private String tableHeader;
    private String tableSchema;
    private static ShareDAOImpl shareDAOImpl;
    public static ShareDAOImpl getInstance(){
        if(shareDAOImpl == null){
            shareDAOImpl = new ShareDAOImpl();
        }
        return shareDAOImpl;
    }

    ShareDAOImpl(){
        this.connection = null;
        this.sqlStatement = null;
        this.dbname = "amazon_dmatDB";
        this.table = "share";
        this.tableHeader = "(shareId, companyName, availableToBuy, currentPrice, totalUnits, shareValue)";

        this.tableSchema = "(\n" +
                "        shareId int,"+
                "        companyName varchar(200),\n" +
                "        availableToBuy int,\n"+
                "        currentPrice int,\n" +
                "        totalUnits int,\n" +
                "        shareValue int, \n" +
                " PRIMARY KEY (shareId));";

        this.conUrl = "jdbc:mysql://localhost:3306/amazon_dmatDB?serverTimezone=UTC";
    }

    public void connectToDB() throws SQLException, ClassNotFoundException{
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            this.connection = DriverManager.getConnection(this.conUrl, "root", "");
            this.sqlStatement = this.connection.createStatement();
            this.sqlStatement.execute("use "+dbname+";");

        } catch (SQLException e) {
            System.out.println("Error connecting to the database IN ShareDAOImpl dao..."+e.getMessage());
            this.disconnect();
        }
    }



    public boolean disconnect(){
        if(this.connection != null){
            try{
                this.connection.close();
                this.connection = null;
                System.out.println("Disconnecting from DB ShareDAOImpl");
                return true;
            }catch (Exception e) {
                System.out.println("ERROR in disconnecting ShareDAOImpl" + e.getMessage());
                this.connection = null;
                return false;
            }
        }else {
            System.out.println("No connection to disconnect ShareDAOImpl");
            return false;
        }
    }

    public void autoPopulateShare(){
        syncWithDB();
        if(this.share.isEmpty()){
            Share s1 = new Share(1L, "TCS", 1, 2040, 400, 816000);
            Share s2 = new Share(2L, "Wipro", 1, 200, 112, 22400);
            Share s3 = new Share(3L, "IBM", 1, 140, 100, 14000);
            Share s4 = new Share(4L, "Informatica", 1, 145, 80, 11600);
            Share s5 = new Share(5L, "TCS", 0, 2040, 80, 163200);
            Share s6 = new Share(6L, "Informatica", 0, 145, 8, 1160);
            add(s1);
            add(s2);
            add(s3);
            add(s4);
            add(s5);
            add(s6);

        }
    }

    private void syncWithDB() {
        List<Share> shareFromDB = this.retrieve();
        System.out.println("Retreive shares from db "+shareFromDB.size());
        if(shareFromDB.size() > this.share.keySet().size()){
            for(Share s : shareFromDB){
                if(!this.share.containsKey(s.getShareId())){
                    this.share.put(s.getShareId(), s);
                }
            }
        }

    }

    @Override
    public boolean add(Share s) {
        System.out.println("Adding share "+s.getCompanyName()+" sid"+s.getShareId());
        //add in map and in db
        this.share.put(s.getShareId(), s);
        try{
            if(this.connection == null) {
                this.connectToDB();
            }
            this.sqlStatement = this.connection.createStatement();
            this.sqlStatement.execute("insert into "+table+" "+tableHeader+" values ('"+s.getShareId()+"', '"+s.getCompanyName()+"', '"+s.getAvailableToBuy()+"','"+s.getCurrentPrice()+"', '"+s.getTotalUnits()+"', '"+s.getShareValue()+"'); ");
            this.disconnect();
            return true;
        }catch (Exception e) {
            System.out.println("Error in inserting share "+e.getMessage());
            this.disconnect();
            return false;
        }
    }

    //update share row if a transaction is made for it
    //parent share
    public boolean updateBuy(Long sid, int unit){
        System.out.println("Updating buy share.. unit"+unit);

        if(this.share.containsKey(sid)){
            Share s = this.get(sid);
            //if all units of available share are bought
            if(s.getTotalUnits() == unit){
                s.setTotalUnits(0);
                s.setShareValue(0);
                s.setAvailableToBuy(0);
            }else{
                s.setTotalUnits(s.getTotalUnits()-unit);
                s.setShareValue(s.getTotalUnits()*s.getCurrentPrice());
            }
            this.share.put(sid, s);
            this.update(s);
            return true;
        }else{
            return false;
        }
    }
    //parent share
    public boolean updateSold(Long sid, int unit){
        System.out.println("Updating parent share row sold");
        if(this.share.containsKey(sid)){
            Share s = this.get(sid);
            //if buyer sold the share, update the parent share which is available to buy
            s.setTotalUnits(s.getTotalUnits()+unit);
            s.setShareValue(s.getTotalUnits()*s.getCurrentPrice());
            this.share.put(sid, s);
            this.update(s);
            return true;
        }else{
            return false;
        }
    }
    @Override
    public boolean update(Share s) {
        System.out.println("Updating share by object and putting db");
        this.share.put(s.getShareId(), s);
        try{
            if(this.connection == null) {
                this.connectToDB();
            }
            this.sqlStatement = this.connection.createStatement();
            this.sqlStatement.execute("update "+table+" set availableToBuy='"+s.getAvailableToBuy()+"', totalUnits='"+s.getTotalUnits()+"', shareValue='"+s.getShareValue()+"' where shareId='"+s.getShareId()+"';");
            this.disconnect();
            return true;
        }catch (Exception e) {
            System.out.println("error "+e.getMessage());
            this.disconnect();
            return false;
        }
    }

    @Override
    public Share get(long sid) {
        if(this.share.containsKey(sid)){
            return this.share.get(sid);
        }else{
            return null;
        }
    }

    public boolean isValidShareAndUnitToSell(long sid, int unitToSell){
        if(this.share.containsKey(sid)){
            if(this.share.get(sid).getAvailableToBuy() == 0 && unitToSell <= this.share.get(sid).getTotalUnits() ){
                return true;
            }
        }
        return false;
    }

    public boolean isValidShareAndUnitToBuy(long sid, int unitToBuy){
        if(this.share.containsKey(sid)){
            if(this.share.get(sid).getAvailableToBuy() == 1 && unitToBuy <= this.share.get(sid).getTotalUnits() ){
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Share> retrieve() {
        ArrayList<Share> shareFromDB = new ArrayList<>();
        try{
            if(this.connection == null) {
                this.connectToDB();
            }
            this.sqlStatement = connection.createStatement();
            ResultSet rs = this.sqlStatement.executeQuery("select * from "+table+";");

            while(rs.next()){
                Share s = new Share();
                s.setShareId((long) rs.getInt(1));
                s.setCompanyName(rs.getString(2));
                s.setAvailableToBuy(rs.getInt(3));
                s.setCurrentPrice(rs.getInt(4));
                s.setTotalUnits(rs.getInt(5));
                s.setShareValue(rs.getInt(6));
                shareFromDB.add(s);
            }

//            for(Share u : shareFromDB){
//                System.out.println(u.getCompanyName());
//            }
            this.disconnect();
            return shareFromDB;

        }catch (Exception e) {
            System.out.println("Error in sql "+e.getMessage());
            this.disconnect();
            return shareFromDB;
        }
    }

    public long nextShareId(){
        syncWithDB();
        long nxtId = 0;
        for(Long id : this.share.keySet()){
            nxtId = Math.max(nxtId, id);
        }
        return nxtId+1;
    }
}
