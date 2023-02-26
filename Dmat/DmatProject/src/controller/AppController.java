package controller;

import customException.ApplicationException;
import customException.UserException;
import model.DematAccount;
import service.DmatService;

import java.util.concurrent.TimeUnit;

public class AppController extends BaseController {

    private final static int maxLoginTries = 5;
    private static int loginTries = 0;
    private DmatService service = DmatService.getDmatService();
    public void initiate(){
        boolean exitCode = false;

        while (!exitCode) {
            System.out.println("_______________________________________________________________");
            System.out.println("|`Welcome to Dmat Account Project`|");
            loadScreen();
            System.out.println("\nPlease select options :");
            System.out.println("\n1. Create Demat Account. \n2. Login into Your Dmat account.\n0. Exit \n");

            String choice = ControllerFactory.getScannerInstance().next();

            switch (choice) {
                case "1":
                    System.out.println("Demat Account Creation/Sign Up details!\n");

                    try {
                        createDmatAccount();
                    } catch (ApplicationException e) {
                        e.printStackTrace();
                    } catch (UserException e) {
                        throw new RuntimeException(e);
                    }
                    break;

                case "2":
                    System.out.println("Welcome Account holder!\n");
                    try {
                        setLoginDetails();
                    } catch (ApplicationException e) {
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                    } catch (UserException e){
                        System.out.println(e.getMessage());
                    }
                    break;

                case "0":
                    exitCode = true;
                    break;

                default:
                    System.out.println("Please Enter Valid Option");
            }
        }
        System.out.println("Thank You For Using Dmat Accounts Application\n");
        loadScreen();
    }

    private void loadScreen(){
        System.out.println("_______________________________________________________________");

        int del = 0;
        while(del != 6){
            try {
                TimeUnit.MILLISECONDS.sleep(1000/(del+1*2));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int k = del;
            while(k!=0){
                System.out.print("_~`~");
                k--;
            }
            del++;
        }
        System.out.print("_~`~");
        System.out.println("\n---------------------------------------------------------------");
    }



    // Creates a User Account and sends to login page
    private void createDmatAccount() throws ApplicationException, UserException {
        System.out.println("\nCreate your new Dmat account");
        System.out.println("\nPlease Enter the below details as prompted and Press Enter to confirm entry." +
                "\nPress Enter Twice to return to Previous Menu. ");

        System.out.println("\n Account Number [4 Digit Number or more]: \n");
        long accNo = this.getAccNo();

        boolean accountAlreadyExists = AccountController.getInstance().isValidAccount(accNo);

        if (accountAlreadyExists) {
            System.out.println("This Account Number " + accNo + " already exists\n");
        }

        if (!accountAlreadyExists) {
            System.out.println("\n AccountHolder's Full Name : \n");
            String firstName = this.getNameOfAccHolder();

            System.out.println("\n Password : \n" +
                    "[Should be of at least 8 characters, contain only letters and digits and " +
                    "must contain at least 2 digits]");
            String password = this.getPassword();

            System.out.println("\n Confirm Password : \n" +
                    "[Should be the same value as entered before]");
            String confirmedPassword = this.getConfirmedPassword(password);

            if(this.arePasswordsMatching(password, confirmedPassword)){
                DematAccount account = new DematAccount(accNo, password, firstName, 0);

                AccountController.getInstance().createDmatAccount(account);

                System.out.println("Your Dmat Account with Account No  : " + account.getAccountId() +
                        " has been created ! \n");
                System.out.println("Please Login using your Account Number and Password below : \n");
            }
            setLoginDetails();
        }
    }

    // If the user and password combination exist, redirect to UserOperations
    private void login(long accNo, String password) throws ApplicationException, UserException {

        if (AccountController.getInstance().isValidAccountPassword(accNo, password) && AccountController.getInstance().isValidAccount(accNo)) {
            System.out.println("User Login Successful!");
            ControllerFactory.getAccountControllerInstance().showMenu(accNo);
        } else {
            System.out.println("\nUnable to load account with entered credentials. " +
                    "\nPlease make sure that the entered credentials are correct \n");
            setLoginDetails();
        }
    }

    private boolean setLoginDetails() throws ApplicationException, UserException {
        loginTries += 1;
        System.out.println("\nUser Login \n");
        System.out.println("Enter Account Number : \n");
        long accNo = this.getAccNo();

        System.out.println("Enter Password : \n");
        String password = this.getPassword();

        if (loginTries > maxLoginTries) {
            System.out.println("Maximum Login Tries Exceeded! \n Returning to Home.");
            loginTries = 0;
            return false;
        }
        login(accNo, password);
        return true;
    }
}


