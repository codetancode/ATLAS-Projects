import com.amazon.railwaycrossingapp.Exceptions.InpValidator;

import java.util.Scanner;


public class RailwayCrossingApp {

	public static void main(String[] args) {
		
		while(true) {
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			System.out.println("Welcome to Railway Crossing App");
			System.out.println("1: End User App");
			System.out.println("2: Government App");
			System.out.println("3: Close App");
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			
			Scanner scanner = new Scanner(System.in);
			//exception handling..
			InpValidator valid = new InpValidator();
			try{
				int choice = scanner.nextInt();
				if(valid.validateSelection(choice, 3)) {
					switch (choice) {
						case 1:
							PublicApp.getInstance().startPublicApp();
							break;

						case 2:
							GovernmentApp.getInstance().startGovernmentApp();
							break;

						case 3:
							System.out.println("Thank you for using the application");
							System.exit(0);
							break;

						default:
							System.err.println("Invalid Choice");
					}
				}
			}catch (Exception ex){
				System.out.println(ex.getMessage());
			}
			
		}
	}

}
