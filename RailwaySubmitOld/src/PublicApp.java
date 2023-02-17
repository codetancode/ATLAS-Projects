import com.amazon.railwaycrossingapp.Exceptions.InpValidator;
import com.amazon.railwaycrossingapp.controller.RailwayCrossingController;
import com.amazon.railwaycrossingapp.controller.UserController;
import com.amazon.railwaycrossingapp.model.RailwayCrossing;
import com.amazon.railwaycrossingapp.model.User;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class PublicApp {

	UserController ucontroller;
	RailwayCrossingController railController;
	Scanner scanner;
	
	private static PublicApp app;
	
	public static PublicApp getInstance() {
		if(app == null) {
			app = new PublicApp();
		}
		return app;
	}
	
	private PublicApp(){
		ucontroller = UserController.getInstance();
		railController = RailwayCrossingController.getInstance();
		scanner = new Scanner(System.in);
	}
	
	void register() {
		User user = new User();

		InpValidator valid = new InpValidator();

		// Empty Next Line for Scanner Issue
		scanner.nextLine();
		
		System.out.println("Enter Name: ");

		user.setName(scanner.nextLine());
		
		System.out.println("Enter Email: ");
		try{
			String email = scanner.nextLine();
			valid.validateEmail(email);
			user.setEmail(email);

			System.out.println("Enter Password: ");
			user.setPassword(scanner.nextLine());

			user.setUserType(1);
			if(ucontroller.registerUser(user)) {
				//add db write code

				System.out.println(user.getName()+", You have Registered Successfully..");
				System.out.println("Navigating to the Railway Crossing Application");

				// Navigate to Home
				home();

			}else {
				System.err.println("Something Went Wrong. Please Try Again...");
			}
		}catch (Exception ex){
			System.out.println("Email is not valid "+ex.getMessage());
			System.err.println("Something Went Wrong. Please Try Again.");
		}

	}
	
	void login() {
		User user = new User();
		InpValidator valid = new InpValidator();
		// Empty Next Line for Scanner Issue
		scanner.nextLine();

		System.out.println("Enter Email: ");
		String email = scanner.nextLine();
		try {
			valid.validateEmail(email);
			user.setEmail(email);

			System.out.println("Enter Password: ");
			user.setPassword(scanner.nextLine());

			user.setUserType(1);

			if(ucontroller.loginUser(user)) {

				System.out.println(user.getName()+", You have Logged In Successfully..");
				System.out.println("Navigating to the Railway Crossing Application");

				// Navigate to Home
				home();
			}else {
				System.err.println("Something Went Wrong. Please Try Again(Your mail/password don't match (or) your user type is not 1(end user type) ");
			}

		}catch (Exception ex){
			System.out.println("Email is not valid"+ex.getMessage());
			System.err.println("Something Went Wrong. Please Try Again");
		}


	}
	
	void listCrossings() {
		Map<String, RailwayCrossing> crossings = (Map<String, RailwayCrossing>) railController.fetchCrossings();
		if(!crossings.isEmpty()){
			for(String key : crossings.keySet()) {
				System.out.println(crossings.get(key));
				System.out.println("-------------------------------");
			}
		}else{
			System.out.println("-------------------------------");
			System.out.println("No Crossings yet.");
			System.out.println("-------------------------------");
		}

	}
	
	void home() {
		
		while(true) {
		
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			System.out.println("Welcome to Railway Crossing Home");
			System.out.println("1: List Railway Crossings");
			System.out.println("2: Search Railway Crossings");
			System.out.println("3: Sort Railway Crossings by Schedule time FromString");
			System.out.println("4: Close Public Application");
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			
			int choice = scanner.nextInt();
			switch (choice) {
				
				case 1: 
					listCrossings();
					break;
				
				case 2:
					searchCrossing();
					break;
					
				case 3:
					sortAndShowRailwaycrossing();
					break;
					
				case 4:
					System.out.println("Thank You for using Railway Crossing App");
					break;
			
				default:
					System.err.println("Invalid Choice");
			}
			
			if(choice == 4) {
				break;
			}
		}
	}

	void searchCrossing() {
		System.out.printf("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("Enter name of crossing");
		String crossingName = scanner.next();
		RailwayCrossing railwayCrossing = railController.searchCrosing(crossingName);
		if (railwayCrossing != null)
			System.out.println(railwayCrossing);
		else
			System.out.println("Not found");
	}
	void sortAndShowRailwaycrossing(){
		List<Map.Entry<String, RailwayCrossing>> sortedList =  railController.sortRailWayCrossing();
		if(!sortedList.isEmpty()){
			for(int i=0;i < sortedList.size();i++){
				System.out.println(sortedList.get(i).getValue());
				System.out.println("-------------------------------");
			}
		}else{
			System.out.println("-------------------------------");
			System.out.println("No Crossings yet.");
			System.out.println("-------------------------------");
		}

	}

	void startPublicApp() {
		
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("Welcome User");
		System.out.println("We have "+ucontroller.getUserCount()+" Users in the DataBase");
		System.out.println("1: Register");
		System.out.println("2: Login");
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		
		int choice = scanner.nextInt();
		switch (choice) {
			
			case 1: 
				register();
				break;
			
			case 2:
				login();
				break;
		
			default:
				System.err.println("Invalid Choice");
		}		
	}

}
