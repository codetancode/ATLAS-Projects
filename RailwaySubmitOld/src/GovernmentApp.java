import com.amazon.railwaycrossingapp.Exceptions.InpValidator;
import com.amazon.railwaycrossingapp.controller.RailwayCrossingController;
import com.amazon.railwaycrossingapp.model.RailwayCrossing;
import com.amazon.railwaycrossingapp.model.User;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class GovernmentApp {
	
	RailwayCrossingController controller;
	Scanner scanner;
	
	private static GovernmentApp app;
	
	public static GovernmentApp getInstance() {
		if(app == null) {
			app = new GovernmentApp();
		}
		return app;
	}
	
	private GovernmentApp(){
		controller = RailwayCrossingController.getInstance();
		scanner = new Scanner(System.in);
	}
	
	void listCrossings() {
		
		Map<String, RailwayCrossing> crossings = (Map<String, RailwayCrossing>) controller.fetchCrossings();
		if(crossings.isEmpty()){
			System.out.println("----!!No logs of crossing yet!!----");
		}else{
			for(String key : crossings.keySet()) {
				System.out.println(crossings.get(key));
				System.out.println("-------------------------------");
			}
		}


	}
	
	void addCrossing() {
		
		// Empty Next Line for Scanner Issue
		scanner.nextLine();
		InpValidator valid = new InpValidator();

		User user = new User();
		RailwayCrossing crossing = new RailwayCrossing();
		try{
			//setting random int as id for crossing
			crossing.setID(randomIntCID());

			System.out.println("Enter Person InCharge Details");
			System.out.println("Enter Name: ");
			user.setName(scanner.nextLine());
			System.out.println("Enter Email: ");
			String email = scanner.nextLine();
			valid.validateEmail(email);
			user.setEmail(email);
			System.out.println("Enter Password: ");
			user.setPassword(scanner.nextLine());

			user.setUserType(3);

			System.out.println("Enter Railway Crossing Details(1 WORD)");
			System.out.println("Enter Crossing Name(1 WORD): ");
			crossing.setName(scanner.nextLine());

			if(controller.fetchCrossings().containsKey(crossing.getName())){
				System.err.println("This crossing already exist.");
				return;
			}

			System.out.println("Enter Crossing Address: ");
			crossing.setAddress(scanner.nextLine());

			System.out.println("Enter Crossing Schedule From(0-23hrs): ");
			String fromTime = scanner.nextLine();
			valid.validate24Hours(fromTime);

			System.out.println("Enter Crossing Schedule To(0-23hrs): ");
			String toTime = scanner.nextLine();
			valid.validate24Hours(toTime);

			crossing.getSchedules().from = Math.min(Integer.parseInt(fromTime), Integer.parseInt(toTime));
			crossing.getSchedules().to = Math.max(Integer.parseInt(fromTime), Integer.parseInt(toTime));

			crossing.setPersonInCharge(user);

			if(controller.addOrUpdateCrossing(crossing)) {
				System.out.println(crossing.getName()+" Added Successfully...");
			}else {
				System.err.println("Something Went Wrong. Please Try Again");
			}

		}catch (Exception ex){
			System.err.println("Something Went Wrong. Please Try Again "+ex.getMessage());
			System.out.println("I am assuming Person Incharge should handle only 1 crossing");
		}
	}

	void login() {
		User user = new User();
		
		// Empty Next Line for Scanner Issue
		
		System.out.println("Enter Email: ");
		user.setEmail(scanner.nextLine());
		
		System.out.println("Enter Password: ");
		user.setPassword(scanner.nextLine());
				
		if(controller.loginUser(user)) {
			System.out.println(user.getName()+", You have Logged In Successfully..");
			System.out.println("Navigating to the Government Railway Crossing Application");
			
			// Navigate to Home
			home();
		}else {
			System.err.println("Something Went Wrong. Please Try Again.(Your mail/password don't match (or) your user type is not 2(admin user type)");
		}
	}
	
	void home() {

		while (true) {

			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			System.out.println("Welcome to Government Railway Crossing Home");
			System.out.println("We have " + controller.getCrossingsCount() + " Crossings in the DataBase");
			System.out.println("1: List Railway Crossings");
			System.out.println("2: Search Railway Crossings");
			System.out.println("3: Add Railway Crossing");
			System.out.println("4: Delete Railway Crossing");
			System.out.println("5: Sort Railway Crossing and show");
			System.out.println("6: Change Status of a crossing");
			System.out.println("7: Close Government Application");
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			try {
				InpValidator valid = new InpValidator();
				int choice = scanner.nextInt();
				valid.validateSelection(choice, 7);
				switch (choice) {

					case 1:
						listCrossings();
						break;

					case 2:
						searchCrossing();
						break;

					case 3:
						addCrossing();
						break;

					case 4:
						deleteCrossing();
						break;

					case 5:
						sortRailwayCrossing();
						break;

					case 6:
						changeStatus();
						break;

					case 7:
						System.out.println("Thank You for using Railway Crossing App.");
						System.exit(0);
						break;

					default:
						System.err.println("Invalid Choice");
				}

				if (choice == 5) {
					break;
				}
			}catch(Exception ex){
				System.out.println("Something went wrong " + ex.getMessage());
			}
		}
	}


	private void deleteCrossing() {
		System.out.println("Please enter email of personIncharge:");
		String email = scanner.next();
		RailwayCrossing railwayCrossing = new RailwayCrossing();
		User user = new User();
		user.setEmail(email);
		railwayCrossing.setPersonInCharge(user);
		boolean b = controller.deleteCrossing(railwayCrossing);
		if (b)
			System.out.println("Crossing deleted");
		else
			System.out.println("not deleted");
	}

	private void searchCrossing() {
		System.out.printf("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("Enter name of crossing");
		String crossingName = scanner.next();
		scanner.next();
		System.out.println("checking this cname "+crossingName);
		RailwayCrossing railwayCrossing = controller.searchCrosing(crossingName);
		if (railwayCrossing != null)
			System.out.println(railwayCrossing);
		else
			System.out.println("Not found");
	}


	void startGovernmentApp() {
		
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("Welcome Admin User");
		System.out.println("Proceed to Login");
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		login();
	}

	void sortRailwayCrossing(){
		List<Map.Entry<String, RailwayCrossing>> sortedList =  controller.sortRailWayCrossing();
		if(!sortedList.isEmpty()){
			for(int i=0;i < sortedList.size();i++){
				System.out.println(sortedList.get(i).getValue());
				System.out.println("-------------------------------");
			}
		}else{
			System.out.println("----!!No logs of crossing yet!!----");
		}

	}

	void changeStatus(){
		System.out.printf("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("Enter name of crossing");
		String crossingName = scanner.next();
		RailwayCrossing railwayCrossing = controller.searchCrosing(crossingName);
		if (railwayCrossing != null) {
			String status = (railwayCrossing.getStatus() == 0)?"OPEN" : "CLOSE";
			System.out.println("Found the crossing its present status is : " + status);
			System.out.println("After changing status..");
			railwayCrossing.setStatus(railwayCrossing.getStatus()==0?1:0);
//			controller update the change in map object into db
			controller.changeStatus(railwayCrossing);
			System.out.println(railwayCrossing);
		}else {
			System.out.println("Not found");
		}
	}

	int randomIntCID(){
		return (int)(Math.random()*1000);
	}
}
