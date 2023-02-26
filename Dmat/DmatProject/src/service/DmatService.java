package service;


import dB.DematAccountDAOImpl;
import dB.ShareDAOImpl;
import dB.TransactionDAOImpl;

public class DmatService {
    private static DmatService dmatService;
    public DematAccountDAOImpl accountDAO;

    public ShareDAOImpl shareDAO;
    public TransactionDAOImpl transactionDAO;

    public static DmatService getDmatService(){
        if( dmatService == null){
            dmatService = new DmatService();
        }
        return dmatService;
    }
    public DmatService(){
        InitiateDB initDb = InitiateDB.getInstance();
        try{
            initDb.initConnectToDB();
        }catch(Exception e){
            System.out.println("Exception msg " + e.getMessage());
            System.out.println("Trouble in initiating DB");
        }

        accountDAO = DematAccountDAOImpl.getInstance();
        shareDAO = ShareDAOImpl.getInstance();
        transactionDAO = TransactionDAOImpl.getInstance();

        accountDAO.autoPopulateAccount();
        shareDAO.autoPopulateShare();
        transactionDAO.autoPopulateTransaction();

    }
}
