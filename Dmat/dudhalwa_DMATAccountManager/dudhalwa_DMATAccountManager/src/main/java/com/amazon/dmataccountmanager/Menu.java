package com.amazon.dmataccountmanager;

import com.amazon.dmataccountmanager.controller.AuthenticationService;
import com.amazon.dmataccountmanager.controller.ShareService;
import com.amazon.dmataccountmanager.controller.TransactionService;
import com.amazon.dmataccountmanager.db.DB;
import com.amazon.dmataccountmanager.model.Share;
import com.amazon.dmataccountmanager.model.Transaction;
import com.amazon.dmataccountmanager.model.User;
import com.amazon.dmataccountmanager.model.UserShares;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Menu {

	AuthenticationService auth = AuthenticationService.getInstance();
	ShareService shareService = ShareService.getInstance();
	TransactionService transactionService = TransactionService.getInstance();
	Scanner scanner = new Scanner(System.in);

	public void showMenu() {
		while (true) {
			/*
				1 - Create Demat Account
				2 - Login
			 */
			System.out.println("1 - Create Demat Account");
			System.out.println("2 - Login");
			System.out.println("3 - Quit");

			System.out.println("Select an Option");
			int choice = Integer.parseInt(scanner.nextLine());

			if (choice == 3) {
				System.out.println("Thank You For using Demat App");

				// Close the DataBase Connection, when user has exited the application :)
				DB db = DB.getInstance();
				db.closeConnection();
				scanner.close();
				break;
			}

			if (choice == 1){
				// Create demat account
				showRegisterMenu();
			}
			else if (choice == 2){
				// Login
				showLoginMenu();
			}
			else{
				System.out.println("Invalid option");
			}
		}
	}

	public void showRegisterMenu(){
		boolean result;

		System.out.println("Navigating to Register Menu...");

		User user = new User();

		System.out.println("Enter User Name:");
		user.username = scanner.nextLine();

		System.out.println("Enter Password:");
		user.password = scanner.nextLine();

		try {
			// Hash the Password of User :)
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(user.password.getBytes(StandardCharsets.UTF_8));
			user.password = Base64.getEncoder().encodeToString(hash);
		}catch (Exception e) {
			System.err.println("Something Went Wrong: "+e);
		}

		result = auth.registerUser(user);
		if (result){
			System.out.println("New demat account  has been created");
			System.out.println("Your account number is " + user.accountNumber);
		}
	}

	public void showLoginMenu(){
		boolean result;
		User user = new User();

		System.out.println("Navigating to Login Menu...");
		System.out.println("Account number");

		try {
			user.accountNumber = Integer.parseInt(scanner.nextLine());
		}
		catch (Exception e){
			System.out.println("Invalid account number");
			return;
		}

		System.out.println("Password");
		user.password = scanner.nextLine();

		try {
			// Encoded to Hash i.e. SHA-256 so as to match correctly
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(user.password.getBytes(StandardCharsets.UTF_8));
			user.password = Base64.getEncoder().encodeToString(hash);
		}catch (Exception e) {
			System.err.println("Something Went Wrong: "+e);
		}

		result = auth.loginUser(user);
		if (result){
			System.out.println("You have logged in successfully");
			showUserMenu(user);
		}
		else{
			System.out.println("Login Failed");
		}
	}

	private void showUserMenu(User user) {
		/*
		    0 – Quit
			1 – Display Demat account details
			2 – Deposit Money
			3 – Withdraw Money
			4 – Buy transaction
			5 – Sell transaction
			6 – View transaction report
		 */
		while (true) {
			System.out.println("0 - Quit");
			System.out.println("1 – Display Demat account details");
			System.out.println("2 – Deposit Money");
			System.out.println("3 – Withdraw Money");
			System.out.println("4 – Buy transaction");
			System.out.println("5 – Sell transaction");
			System.out.println("6 – View transaction report");

			System.out.println("Select an Option");
			int choice = Integer.parseInt(scanner.nextLine());

			if (choice == 0) {
				break;
			}else if (choice == 1){
				showUserDetails(user);
			}else if (choice == 2){
				depositMoney(user);
			}else if (choice == 3){
				withdrawMoney(user);
			}else if (choice == 4){
				buyShares(user);
			}else if (choice == 5){
				sellShares(user);
			}else if (choice == 6){
				viewTransactions(user);
			}else{
				System.out.println("Invalid choice");
			}
		}
	}

	private void showUserDetails(User user){
		List<UserShares> userShares = shareService.getUserShares(user.id);

		System.out.println("~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("User Name:\t\t" + user.username);
		System.out.println("Account Number:\t\t" + user.accountNumber);
		System.out.println("Account Balance:\t\t" + user.accountBalance);

		System.out.println("_____________________________________________________________");
		for (int i=0; i<userShares.size(); i++){
			System.out.println("ID: " + userShares.get(i).shareId);
			System.out.println("Company Name: " + userShares.get(i).companyName);
			System.out.println("No. of Shares: " + userShares.get(i).shareCount);
			System.out.println("_____________________________________________________________");
		}

		System.out.println("~~~~~~~~~~~~~~~~~~~~~");
	}

	private void depositMoney(User user){
		System.out.println("Enter the amount to be deposited: ");
		int amount = Integer.parseInt(scanner.nextLine());

		user.accountBalance += amount;
		auth.updateUser(user);
	}

	private void withdrawMoney(User user){
		System.out.println("Enter the amount to be withdrawn: ");
		int amount = Integer.parseInt(scanner.nextLine());

		if (amount > user.accountBalance)
		{
			System.out.println("Insufficient Balance");
			System.out.println("Account Balance " + user.accountBalance);
		}
		else
		{
			user.accountBalance -= amount;
			auth.updateUser(user);
		}
	}

	private void displayShares(){
		List<Share> allShares = shareService.getAllShares();

		for (int i=0; i<allShares.size(); i++){
			System.out.println(allShares.get(i).id + " - " + allShares.get(i).companyName + " - Rs. " + allShares.get(i).price);
		}
	}

	private void buyShares(User user){
		List<Share> allShares = shareService.getAllShares();

		for (int i=0; i<allShares.size(); i++){
			System.out.println(i + " - " + allShares.get(i).companyName + " - Rs. " + allShares.get(i).price);
		}
		System.out.println("Choose the share to buy:");
		int shareId = Integer.parseInt(scanner.nextLine());
		if (shareId >= allShares.size()){
			System.out.println("Invalid option");
			return;
		}

		System.out.println("Enter number of shares to buy:");
		int shareCount = Integer.parseInt(scanner.nextLine());

		// Check cost is above user balacnce
		float totalCost = shareCount * allShares.get(shareId).price;
		if (totalCost <= user.accountBalance){
			UserShares userShare = new UserShares();
			userShare.userId = user.id;
			userShare.shareId = allShares.get(shareId).id;
			userShare.companyName = allShares.get(shareId).companyName;
			userShare.shareCount = shareCount;

			shareService.buyShare(userShare);

			Transaction transaction = new Transaction();
			transaction.userID = user.id;
			transaction.shareID = allShares.get(shareId).id;
			transaction.shareCount = shareCount;
			transaction.pricePerShare = allShares.get(shareId).price;

			transactionService.addBuyTransaction(transaction);

			user.accountBalance -= transaction.totalCost;
			auth.updateUser(user);
		}
		else{
			System.out.println("Insufficient funds");
			System.out.println("Account Balance is " + user.accountBalance);
		}
	}

	private void sellShares(User user){
		List<UserShares> userShares = shareService.getUserShares(user.id);

		for (int i=0; i<userShares.size(); i++){
			System.out.println("ID: " + i);
			System.out.println("Company Name: " + userShares.get(i).companyName);
			System.out.println("No. of Shares: " + userShares.get(i).shareCount);
			System.out.println("_____________________________________________________________");
		}
		System.out.println("Choose the share ID to sell:");
		int shareId = Integer.parseInt(scanner.nextLine());

		System.out.println("Enter number of shares to sell:");
		int shareCount = Integer.parseInt(scanner.nextLine());

		if (shareId < userShares.size() && shareCount <= userShares.get(shareId).shareCount){
			UserShares userShare = new UserShares();
			userShare.id = userShares.get(shareId).id;
			userShare.userId = userShares.get(shareId).userId;
			userShare.shareId = userShares.get(shareId).shareId;
			userShare.companyName = userShares.get(shareId).companyName;
			userShare.shareCount = userShares.get(shareId).shareCount - shareCount;

			shareService.sellShare(userShare);

			Transaction transaction = new Transaction();
			transaction.userID = user.id;
			transaction.shareID = userShares.get(shareId).shareId;
			transaction.shareCount = shareCount;

			// Get price of share
			Share thisShare = shareService.getShareDetails(userShares.get(shareId).shareId);
			transaction.pricePerShare = thisShare.price;

			transactionService.addSellTransaction(transaction);

			user.accountBalance += transaction.totalCost;
			auth.updateUser(user);
		}
		else if (shareId >= userShares.size()){
			System.out.println("Invalid Share ID");
		}
		else if (shareCount > userShares.get(shareId).shareCount){
			System.out.println("Insufficient no of share counts");
			System.out.println("No of shares: " + userShares.get(shareId).shareCount);
		}

	}

	private void viewTransactions(User user){
		// Date selection
		System.out.println("Enter start date in yyyy-MM-dd format (0 to show all dates data):");
		String startDate = scanner.nextLine();

		String endDate = "0";
		if (!startDate.equals("0")){
			System.out.println("Enter end date (0 to select today's date):");
			endDate = scanner.nextLine();
		}

		// Company selection
		displayShares();
		System.out.println("Select share ID (0 to display all shares):");
		int shareID = Integer.parseInt(scanner.nextLine());

		String sql = "SELECT * from [transaction] WHERE userID=" + user.id;

		if (!startDate.equals("0")){
			if (endDate.equals("0")){
				LocalDate today = LocalDate.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				endDate = today.format(formatter);
			}

			startDate += " 00:00:00";
			endDate += " 23:59:59";
			sql += " AND transactedOn BETWEEN '" + startDate + "' AND '" + endDate + "'";
		}

		if (shareID > 0){
			sql += " AND shareId=" + shareID;
		}

		List<Transaction> transactions = transactionService.getTransactions(sql);

		if (transactions.size() == 0){
			System.out.println("No transactions ");
		}

		for (Transaction transaction: transactions){
			String type = (transaction.type == 1)? "BUY":"SELL";
			System.out.println("User ID: " + transaction.userID);
			System.out.println("Share ID: " + transaction.shareID);
			System.out.println("Share Count: " + transaction.shareCount);
			System.out.println("Price Per Share: " + transaction.pricePerShare);
			System.out.println("Transacted On: " + transaction.transactedOn);
			System.out.println("Transaction Charges: " + transaction.transactionCharges);
			System.out.println("STT Charges: " + transaction.sttCharges);
			System.out.println("Type: " + type);
			System.out.println("________________________________________________");
		}
	}
}
