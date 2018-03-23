package cs5530;

import java.lang.*;
import java.sql.*;
import java.io.*;

public class testdriver2 {

	static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	static Driver driver;
	static User user;
	static Admin admin;
	static Connector2 con;

	/**
	 * Prints the initial display menu.
	 */
	public static void displayMenu() {
		System.out.println("        Welcome to UUber System     ");
		System.out.println("1. Login");
		System.out.println("2. Register");
		System.out.println("3. Close Connection:");
		System.out.println("Please enter your choice:");
	}

	/**
	 * Prints the logged in menu when participant is a driver and user.
	 */
	public static void displayLoginMenu() {
		System.out.println("Do you want to proceed as a User or a Driver?");
		System.out.println("1. User");
		System.out.println("2. Driver");
		System.out.println("3. Exit");
		System.out.println("Please enter your choice:");
	}

	/**
	 * Prints the user menu.
	 */
	public static void displayUserMenu() {
		System.out.println("        Welcome to UUber System     ");
		System.out.println("1. Make a reservation");
		System.out.println("2. Record a ride");
		System.out.println("3. Declare your favorite car");
		System.out.println("4. Record feedback for a car");
		System.out.println("5. Rate feedback usefulness");
		System.out.println("6. Edit trusted users");
		System.out.println("7. Browse cars");
		System.out.println("8. Get most useful feedback for a driver");
		System.out.println("9. Get suggested cars");
		System.out.println("10. Get user degrees of separation");
		System.out.println("11. Get Statistics");
		System.out.println("12. Exit");
		System.out.println("Please enter your choice:");
	}

	/**
	 * Prints the driver menu.
	 */
	public static void displayDriverMenu() {
		System.out.println("        Welcome, Driver     ");
		System.out.println("1. Add new car");
		System.out.println("2. Update car");
		System.out.println("3. Exit");
		System.out.println("Please enter your choice:");
	}

	/**
	 * Prints the admin menu.
	 */
	public static void displayAdminMenu() {
		System.out.println("        Welcome, Admin    ");
		System.out.println("1. Award Top Users");
		System.out.println("2. Exit");
		System.out.println("Please enter your choice:");
	}

	/**
	 * Prints the registration menu.
	 */
	public static void displayRegisterMenu() {
		System.out.println("What would you like to register as?");
		System.out.println("1. Register as User");
		System.out.println("2. Register as Driver");
		System.out.println("3. Exit");
		System.out.println("Please enter your choice:");
	}

	/**
	 * Prints the are you already a driver prompt.
	 */
	public static void displayUserRegisterMenu() {
		System.out.println("Are you already a driver?");
		System.out.println("1. Yes");
		System.out.println("2. No");
		System.out.println("3. Exit");
		System.out.println("Please enter your choice:");
	}

	/**
	 * Prints the are you already a user prompt.
	 */
	public static void displayDriverRegisterMenu() {
		System.out.println("Are you already a user?");
		System.out.println("1. Yes");
		System.out.println("2. No");
		System.out.println("3. Exit");
		System.out.println("Please enter your choice:");
	}

	/**
	 * Registers an existing driver as a new user.
	 * 
	 * @throws IOException
	 */
	public static void registerDriverAsUser() throws IOException {
		System.out.println("Register with your driver credentials");
		String username;
		String password;
		System.out.println("Please enter your username:");
		while ((username = in.readLine()) == null || username.length() == 0)
			;
		System.out.println("Please enter your password:");
		while ((password = in.readLine()) == null || password.length() == 0)
			;
		driver = new Driver(username, password, con.stmt);
		if (driver.loginToUber(username, password)) {
			// they have successfully logged in as a driver, create a user account for them
			user = new User(username, password, con.stmt);

			if (user.registerForUber()) {
				System.out.println("User registration successful");

				// at this point I know I can login as both
				handleLoginMenu();
			} else {
				System.out.println("User registration failed");
			}
		} else {
			System.out.println("We couldn't find any credentials that matched");
		}
	}

	/**
	 * Registers a new user.
	 * 
	 * @throws IOException
	 */
	public static void registerNewUser() throws IOException {
		String username;
		String password;
		String password2;
		String name;
		String address;
		String phoneNumber;

		System.out.println("Please enter your desired username:");
		while ((username = in.readLine()) == null || username.length() == 0)
			;
		System.out.println("Please enter your desired password:");
		while ((password = in.readLine()) == null || password.length() == 0)
			;
		System.out.println("Please enter your password again for confirmation:");
		while ((password2 = in.readLine()) == null || password2.length() == 0 || password.equals(password2)) {
			if (password.length() > 0) {
				System.out.println("Error: passwords did not match");
			}
		}	
		System.out.println("Please enter your name:");
		while ((name = in.readLine()) == null || name.length() == 0)
			;
		System.out.println("Please enter your address:");
		while ((address = in.readLine()) == null || address.length() == 0)
			;
		System.out.println("Please enter your phone number (only digits):");
		while ((phoneNumber = in.readLine()) == null || phoneNumber.length() == 0)
			;
		try {
			Integer.parseInt(phoneNumber);
		} catch (Exception e) {
			System.out.println("Phone number can only consist of digits");
			return;
		}

		user = new User(username, password, con.stmt, name, address, Integer.parseInt(phoneNumber));
		if (user.registerForUber()) {
			System.out.println("User registration successful");
			handleUserMenu();
		} else {
			System.out.println("User registration failed");
		}
	}

	/**
	 * Registers an existing user as a new driver.
	 * 
	 * @throws IOException
	 */
	public static void registerUserAsDriver() throws IOException {
		System.out.println("Register with your user credentials");
		String username;
		String password;
		System.out.println("Please enter your username:");
		while ((username = in.readLine()) == null || username.length() == 0)
			;
		System.out.println("Please enter your password:");
		while ((password = in.readLine()) == null || password.length() == 0)
			;
		user = new User(username, password, con.stmt);
		if (user.loginToUber(username, password)) {
			// they have successfully logged in as a user, create a driver account for them
			// (probably some driver method)
			driver = new Driver(username, password, con.stmt, user.getName(), user.getAddress(), user.getPhoneNumber());
			if (driver.registerForUber()) {
				System.out.println("Driver registration successful");

				// at this point I know I can login as both
				handleLoginMenu();
			} else {
				System.out.println("Driver registration failed");
			}
		} else {
			System.out.println("We couldn't find any credentials that matched");
		}
	}

	/**
	 * Registers a new driver.
	 * 
	 * @throws IOException
	 */
	public static void registerNewDriver() throws IOException {
		String username;
		String password;
		String password2;
		String name;
		String address;
		String phoneNumber;
		System.out.println("Please enter your desired username:");
		while ((username = in.readLine()) == null || username.length() == 0)
			;
		System.out.println("Please enter your desired password:");
		while ((password = in.readLine()) == null || password.length() == 0)
			;
		System.out.println("Please enter your password again for confirmation:");
		while ((password2 = in.readLine()) == null || password2.length() == 0 || password.equals(password2)) {
			if (password.length() > 0) {
				System.out.println("Error: passwords did not match");
			}
		}		
		System.out.println("Please enter your name:");
		while ((name = in.readLine()) == null || name.length() == 0)
			;
		System.out.println("Please enter your address:");
		while ((address = in.readLine()) == null || address.length() == 0)
			;
		System.out.println("Please enter your phone number (only digits):");
		while ((phoneNumber = in.readLine()) == null || phoneNumber.length() == 0)
			;
		try {
			Integer.parseInt(phoneNumber);
		} catch (Exception e) {
			System.out.println("Phone number can only consist of digits");
			return;
		}

		driver = new Driver(username, password, con.stmt, name, address, Integer.parseInt(phoneNumber));
		if (driver.registerForUber()) {
			System.out.println("Driver registration successful");
			handleDriverMenu();
		} else {
			System.out.println("Driver registration failed");
		}
	}

	/**
	 * Handles user registration.
	 * 
	 * @throws IOException
	 */
	public static void handleUserRegistration() throws IOException {
		int c = 0;
		String choice;
		boolean level = true;
		while (level) {
			displayUserRegisterMenu();
			while ((choice = in.readLine()) == null || choice.length() == 0)
				;
			try {
				c = Integer.parseInt(choice);
			} catch (Exception e) {
				continue;
			}

			switch (c) {
			case 1:
				// register with driver credentials
				registerDriverAsUser();
				break;
			case 2:
				// make new credentials
				registerNewUser();
				break;
			case 3:
				// go back
				level = false;
				break;
			default:
				System.out.println("Your input didn't match any of the choices");
				break;
			}
		}
	}

	/**
	 * Handles driver registration.
	 * 
	 * @throws IOException
	 */
	public static void handleDriverRegistration() throws IOException {
		int c = 0;
		String choice;
		boolean level = true;
		while (level) {
			displayDriverRegisterMenu();
			while ((choice = in.readLine()) == null || choice.length() == 0)
				;
			try {
				c = Integer.parseInt(choice);
			} catch (Exception e) {
				continue;
			}

			switch (c) {
			case 1:
				// register with user credentials
				registerUserAsDriver();
				break;
			case 2:
				// make new credentials
				registerNewDriver();
				break;
			case 3:
				// go back
				level = false;
				break;
			default:
				System.out.println("Your input didn't match any of the choices");
				break;
			}
		}
	}

	/**
	 * Starts the registration process.
	 * 
	 * @throws IOException
	 */
	public static void handleRegisterMenu() throws IOException {
		int c = 0;
		String choice;
		boolean level = true;
		while (level) {
			displayRegisterMenu();
			while ((choice = in.readLine()) == null || choice.length() == 0)
				;
			try {
				c = Integer.parseInt(choice);
			} catch (Exception e) {
				continue;
			}

			switch (c) {
			case 1:
				// register as user
				handleUserRegistration();
				break;
			case 2:
				// register as driver
				handleDriverRegistration();
				break;
			case 3:
				// go back
				level = false;
				break;
			default:
				System.out.println("Your input didn't match any of the choices");
				break;
			}
		}
	}

	/**
	 * Handles login for someone who is a user and a driver.
	 * 
	 * @throws IOException
	 */
	public static void handleLoginMenu() throws IOException {
		int c = 0;
		String choice;
		boolean level = true;
		while (level) {
			displayLoginMenu();
			while ((choice = in.readLine()) == null || choice.length() == 0)
				;
			try {
				c = Integer.parseInt(choice);
			} catch (Exception e) {
				continue;
			}

			switch (c) {
			case 1:
				// proceed as user
				handleUserMenu();
				break;
			case 2:
				// proceed as driver
				handleDriverMenu();
				break;
			case 3:
				// go back
				level = false;
				break;
			default:
				System.out.println("Your input didn't match any of the choices");
				break;
			}
		}
	}

	/**
	 * Handles user options.
	 * 
	 * @throws IOException
	 */
	public static void handleUserMenu() throws IOException {
		int c = 0;
		String choice;
		boolean level = true;
		while (level) {
			displayUserMenu();
			while ((choice = in.readLine()) == null || choice.length() == 0)
				;
			try {
				c = Integer.parseInt(choice);
			} catch (Exception e) {
				continue;
			}
			String input;
			switch (c) {
			case 1:
				// make a reservation
				break;
			case 2:
				// record a ride
				break;
			case 3:
				// declare your favorite car
				break;
			case 4:
				// record feedback for a car
				break;
			case 5:
				// rate feedback usefulness
				int fid, rating;
				try {
					System.out.println("Please enter the feedback fid you want to rate:");
					while ((input = in.readLine()) == null || input.length() == 0);
					fid = Integer.parseInt(input);
					input = null;
					System.out.println("Please enter the rating for the feedback between 0-2:");
					while ((input = in.readLine()) == null || input.length() == 0);
					rating = Integer.parseInt(input);
					if (rating < 0 || rating > 2) {
						System.out.println("Wrong input range");
						break;
					}
				} catch (NumberFormatException e) {
					System.out.println("Wrong number format");
					break;
				}
				String usefulness = user.rateUsefulness(fid, rating);
				System.out.println(usefulness);
				break;
			case 6:
				// edit trusted users
				System.out.println("Please enter the login of the user you want to trust/distrust:");
				String uuLogin;
				while ((uuLogin = in.readLine()) == null || uuLogin.length() == 0);
				System.out.println("Do you trust him ? (yes/no) ");
				while ((input = in.readLine()) == null || input.length() == 0);
				boolean isTrusted;
				if (input.toLowerCase().equals("yes"))
					isTrusted = true;
				else if (input.toLowerCase().equals("no"))
					isTrusted = false;
				else {
					System.out.println("Wrong input");
					break;
				}
				String trustInfo = user.setTrustee(uuLogin, isTrusted);
				System.out.println(trustInfo);
				break;
			case 7:
				// browse cars
				System.out.println("Please enter car's category or leave it blank");
				String category;
				while ((category = in.readLine()) == null)
					;

				System.out.println("Please enter car's address or leave it blank");
				String address;
				while ((address = in.readLine()) == null)
					;

				System.out.println("Please enter car's model or leave it blank");
				String model;
				while ((model = in.readLine()) == null);
				
				System.out.println("Do you want the results sorted by the feedback of only the users you trust? (yes/no) ");
				while ((input = in.readLine()) == null || input.length() == 0);
				boolean sortByFeedbacks;
				if (input.toLowerCase().equals("no"))
					sortByFeedbacks = true;
				else if (input.toLowerCase().equals("yes"))
					sortByFeedbacks = false;
				else {
					System.out.println("Wrong input");
					break;
				}
				String cars = user.browseCars(category, address, model, sortByFeedbacks);
				System.out.println(cars);
				break;
			case 8:
				// get most useful feedback for a driver
				System.out.println("Please enter the login of the driver you want feedback for:");
				String udLogin;
				while ((udLogin = in.readLine()) == null || udLogin.length() == 0);
				System.out.println("Please enter the number of feedbacks you want:");
				while ((input = in.readLine()) == null || input.length() == 0);
				int numberOfFeedbacks;
				try {
					numberOfFeedbacks = Integer.parseInt(input);
				} catch (NumberFormatException e) {
					System.out.println("Wrong number format");
					break;
				}
				String feedbacks = user.getUsefulFeedbacks(udLogin, numberOfFeedbacks);
				System.out.println(feedbacks);
				break;
			case 9:
				// get suggested cars
				System.out.println("Please enter the user vin of the car you want to get suggestions based on");
				while ((input = in.readLine()) == null || input.length() == 0);
				int vin;
				try {
					vin = Integer.parseInt(input);
				} catch (NumberFormatException e) {
					System.out.println("Wrong number format");
					break;
				}
				String suggestions = user.getCarSuggestions(vin);
				System.out.println(suggestions);
				break;
			case 10:
				// get user degrees of separation
				System.out
						.println("Please enter the user login of the user you want to know the separation degree of:");
				;
				String uuLogin2;
				while ((uuLogin2 = in.readLine()) == null || uuLogin2.length() == 0);
				String degree = user.getSeparationDegree(user.getLogin(), uuLogin2);
				System.out.println(degree);
				break;
			case 11:
				// get statistics
				displayStats();
				while ((input = in.readLine()) == null || input.length() == 0);
				switch (Integer.parseInt(input)) {
				case 1:
					System.out.println("Please enter the number of cars you want to display for each category:");
					while ((input = in.readLine()) == null || input.length() == 0);
					String popularCars = user.getMostPopularUCs(Integer.parseInt(input));
					System.out.println(popularCars);
					break;
				case 2:
					System.out.println("Please enter the number of cars you want to display for each category:");
					while ((input = in.readLine()) == null || input.length() == 0);
					String expensiveCars = user.getMostExpensiveUCs(Integer.parseInt(input));
					System.out.println(expensiveCars);
					break;
				case 3:
					System.out.println("Please enter the number of drivers you want to display for each category:");
					while ((input = in.readLine()) == null || input.length() == 0);
					String bestDrivers = user.getBestUDs(Integer.parseInt(input));
					System.out.println(bestDrivers);
					break;
				default:
					System.out.println("Your input didn't match any of the choices");
					break;
				}
				break;
			case 12:
				// go back
				level = false;
				break;
			default:
				System.out.println("Your input didn't match any of the choices");
				break;
			}
		}
	}

	/**	 
	 * Stats options
	 * 
	 * @throws IOException
	 */
	public static void displayStats() throws IOException {
		System.out.println("1. List of the m most popular UCs for each category");
		System.out.println("2. The list of m most expensive UCs for each category");
		System.out.println("3. the list of m highly rated UDs for each category");
	}
	
	/**
	 * Driver options
	 * 
	 * @throws IOException
	 */
	public static void handleDriverMenu() throws IOException {
		int c = 0;
		String choice;
		boolean level = true;
		while (level) {
			displayDriverMenu();
			while ((choice = in.readLine()) == null || choice.length() == 0)
				;
			try {
				c = Integer.parseInt(choice);
			} catch (Exception e) {
				continue;
			}

			switch (c) {
			case 1:
				// add new car
				changeCars(0);
				break;
			case 2:
				// update car
				changeCars(1);
				break;
			case 3:
				// go back
				level = false;
				break;
			default:
				System.out.println("Your input didn't match any of the choices");
				break;
			}
		}
	}

	/**
	 * Attempts to add or update a car.
	 * 
	 * @param method		0 for add, 1 for update
	 * @throws IOException
	 */
	public static void changeCars(int method) throws IOException {
		// vin, category, year, make, model
		String vin;
		String category;
		String year;
		String make;
		String model;

		System.out.println("Please enter your car's vin:");
		while ((vin = in.readLine()) == null || vin.length() == 0)
			;
		try {
			Integer.parseInt(vin);
		} catch (Exception e) {
			System.out.println("Your car's vin can only consist of digits");
			return;
		}
		System.out.println("Please enter your car's category (economy, comfort, luxury):");
		while ((category = in.readLine()) == null || category.length() == 0 
				|| (!category.equals("economy") && !category.equals("comfort") && !category.equals("luxury"))) {
			if (category.length() > 0) {
				System.out.println("Category has to be economy, comfort, or luxury");
			}
		}
		System.out.println("Please enter your car's year:");
		while ((year = in.readLine()) == null || year.length() == 0)
			;
		try {
			Integer.parseInt(year);
		} catch (Exception e) {
			System.out.println("Your car's year can only consist of digits");
		}
		System.out.println("Please enter your car's make:");
		while ((make = in.readLine()) == null || make.length() == 0)
			;
		System.out.println("Please enter your car's model:");
		while ((model = in.readLine()) == null || model.length() == 0)
			;
		
		// now either add or update
		switch (method) {
		case 0: 
			if (user.addCar(Integer.parseInt(vin), category, Integer.parseInt(year), make, model)) {
				System.out.println("Car successfully added");
			} else {
				System.out.println("Car could not be added");
			}
			break;
		case 1: 
			if (user.updateCar(Integer.parseInt(vin), category, Integer.parseInt(year), make, model)) {
				System.out.println("Car successfully updated");
			} else {
				System.out.println("Car could not be updated");
			}
			break;
		}
	}

	/**
	 * Handles admin options.
	 * 
	 * @throws IOException
	 */
	public static void handleAdminMenu() throws IOException {
		int c = 0;
		String choice;
		boolean level = true;
		while (level) {
			displayAdminMenu();
			while ((choice = in.readLine()) == null || choice.length() == 0)
				;
			try {
				c = Integer.parseInt(choice);
			} catch (Exception e) {
				continue;
			}

			switch (c) {
			case 1:
				// award top users
				break;
			case 2:
				// go back
				level = false;
				break;
			default:
				System.out.println("Your input didn't match any of the choices");
				break;
			}
		}
	}

	public static void main(String[] args) {
		con = null;
		String choice;
		String sql = null;
		int c = 0;
		String username;
		String password;
		try {
			// remember to replace the password
			con = new Connector2();
			System.out.println("Database connection established");
			boolean level = true;

			while (level) {
				displayMenu();
				while ((choice = in.readLine()) == null || choice.length() == 0)
					;
				try {
					c = Integer.parseInt(choice);
				} catch (Exception e) {

					continue;
				}

				switch (c) {
				case 1:
					// login
					System.out.println("Please enter your username:");
					while ((username = in.readLine()) == null || username.length() == 0)
						;
					System.out.println("Please enter your password:");
					while ((password = in.readLine()) == null || password.length() == 0)
						;

					// then actually login
					// test if admin first
					admin = new Admin(username, password, con.stmt);
					if (admin.loginAsAdmin(username, password)) {
						handleAdminMenu();
						break;
					}
					user = new User(username, password, con.stmt);
					driver = new Driver(username, password, con.stmt);

					boolean userLoginSuccess = user.loginToUber(username, password);
					boolean driverLoginSuccess = driver.loginToUber(username, password);

					if (userLoginSuccess && driverLoginSuccess) {
						// display login menu if both
						handleLoginMenu();
					} else if (userLoginSuccess) {
						// display user menu if user
						handleUserMenu();
					} else if (driverLoginSuccess) {
						// display driver menu if driver
						handleDriverMenu();
					} else {
						// login failed
						System.out.println("Login failed.");
					}
					break;
				case 2:
					// register
					handleRegisterMenu();
					break;
				case 3:
					// exit
					con.stmt.close();
					level = false;
					break;
				default:
					System.out.println("Your input didn't match any of the choices");
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Either connection error or query execution error!");
		} finally {
			if (con != null) {
				try {
					con.closeConnection();
					System.out.println("Database connection terminated");
				}

				catch (Exception e) {
					/* ignore close errors */ }
			}
		}
	}
}
