package controller;

import customException.InvalidInputException;
import customException.UserException;
import dB.Validator;

import java.util.InputMismatchException;
import java.util.Scanner;

public class BaseController {

    private final static int MAX_PASSWORD_CONFIRM_TRIES = 3;
    private final static int MAX_PASSWORD_TRIES = 3;


    //users related------------------------------
    protected String getNameOfAccHolder() throws UserException {
        Scanner sc = ControllerFactory.getScannerInstance();

        String nameOfAccHolder;

        try {
            nameOfAccHolder = sc.next();
        } catch (InputMismatchException e) {
            throw new UserException("Entered Name is Invalid");
        }

        if (!Validator.isValidNameLength(nameOfAccHolder)) {
            throw new UserException("First Name should be below 50 characters ");
        }

        if (!Validator.isAlphabeticWithSpaceAndDots(nameOfAccHolder)) {
            throw new UserException("First Name can only contain alphabets, spaces and dots in " +
                    "appropriate manner\n");
        }
        return nameOfAccHolder;
    }

    protected long getAccNo() throws UserException {
        Scanner sc = ControllerFactory.getScannerInstance();

        long accNo;

        try {
            accNo = sc.nextInt();
        } catch (InputMismatchException e) {
            throw new UserException("\n Please enter correct Account Number. " +
                    "\n It is more than 3 digit number");
        }
        if (!Validator.isPositive(accNo)) {
            throw new UserException("\n Account Number cannot be a negative number.");
        }
        if (!Validator.isValidAccNoLength(accNo)) {
            throw new UserException("The entered value is not a valid Employee id" +
                    "\nIt is more that 3 digit number");
        }
        return accNo;
    }

    protected boolean arePasswordsMatching(String oldPassword, String newPassword) {
        return Validator.arePasswordsMatching(oldPassword,newPassword);
    }

    protected String getConfirmedPassword(String password) throws UserException {
        Scanner sc = ControllerFactory.getScannerInstance();

        String confirmedPassword;
        int passwordConfirmTries = 1;

        try {
            confirmedPassword = sc.next();
        } catch (InputMismatchException e) {
            throw new UserException("Entered Password is Invalid ");
        }

        while (passwordConfirmTries <= MAX_PASSWORD_CONFIRM_TRIES) {
            if (!Validator.arePasswordsMatching(password, confirmedPassword)) {
                ++passwordConfirmTries;
                System.out.println("Please Enter a password which matches previous password value " +
                        "[Only 3 tries Allowed]: \n");
                try {
                    confirmedPassword = sc.next();
                } catch (InputMismatchException e) {
                    throw new UserException("Entered Password is Invalid ");
                }
            } else {
                return confirmedPassword;
            }
        }
        if (!Validator.arePasswordsMatching(password, confirmedPassword)) {
            throw new UserException("Both Passwords do not match. Password tries Exhausted");
        }
        return confirmedPassword;
    }
    protected String getPassword() throws UserException {
        Scanner sc = ControllerFactory.getScannerInstance();

        String password;
        int passwordTries = 1;

        try {
            password = sc.next();
        } catch (InputMismatchException e) {
            throw new UserException("Entered Password is Invalid ");
        }

        while (passwordTries < MAX_PASSWORD_TRIES) {
            if (!Validator.isValidPassword(password)) {
                ++passwordTries;
                System.out.println("Please Enter a Valid password [Only 3 tries Allowed]: \n" +
                        " 1. A password must have at least eight characters.\n" +
                        " 2. A password consists of only letters and digits.\n" +
                        " 3. A password must contain at least two digits \n");
                try {
                    password = sc.next();
                } catch (InputMismatchException e) {
                    throw new UserException("Entered Password is Invalid ");
                }
            } else {
                return password;
            }
        }
        if (!Validator.isValidPassword(password)) {
            throw new UserException("Password tries Exhausted");
        }
        return password;
    }
    protected long getAmount() throws InvalidInputException {
        Scanner sc = ControllerFactory.getScannerInstance();
        long amount;
        amount = sc.nextInt();
        if (!Validator.isPositive(amount)) {
            throw new InvalidInputException("\n Amount cannot be a negative number.");
        }
        return amount;
    }
    protected long getAmount(long currentBal) throws InvalidInputException {
        Scanner sc = ControllerFactory.getScannerInstance();
        long amount;
        amount = sc.nextInt();
        if (!Validator.isPositive(amount)) {
            throw new InvalidInputException("\n Amount cannot be a negative number.");
        }
        if(amount > currentBal){
            throw new InvalidInputException("\n Amount greater than your current Balance, cannot withdraw.");
        }
        return amount;
    }
    protected long getShareId() throws InvalidInputException {
        System.out.println("Please enter the Share Id");
        Scanner sc = ControllerFactory.getScannerInstance();
        long id;
        id = sc.nextInt();
        if (!Validator.isPositive(id)) {
            throw new InvalidInputException("\n Id cannot be a negative number.");
        }
        return id;
    }
    protected int getUnitNum() throws InvalidInputException {
        System.out.println("Please enter unit");
        Scanner sc = ControllerFactory.getScannerInstance();
        int unit;
        unit = sc.nextInt();
        if (!Validator.isPositive(unit)) {
            throw new InvalidInputException("\n Units cannot be a negative number.");
        }
        return unit;
    }

}
