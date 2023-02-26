package controller;

import java.util.Scanner;

public class ControllerFactory {

//    public AccountController getAccountControllerInstance(){return new AccountController();}

//    public AccountLoginController getAccountLoginControllerInstance(){return new AccountLoginController();}

    public static AccountController getAccountControllerInstance(){return AccountController.getInstance();}
    public static Scanner getScannerInstance() {
        return new Scanner(System.in).useDelimiter("\n");
    }

}
