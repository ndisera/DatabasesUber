package cs5530;

import java.lang.*;
import java.sql.*;
import java.io.*;

public class testdriver2 {
	
	static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

	/**
	 * Initial display menu
	 */
	public static void displayMenu() {
		System.out.println("        Welcome to UUber System     ");
		System.out.println("1. Login");
		System.out.println("2. Register");
		System.out.println("3. Close Connection:");
		System.out.println("Please enter your choice:");
	}
	
	/**
	 * Logged in menu when participant is a driver and user
	 */
	public static void displayLoginMenu() {
		System.out.println("Do you want to proceed as a User or a Driver?");
		System.out.println("1. User");
		System.out.println("2. Driver");
		System.out.println("3. Exit");
		System.out.println("Please enter your choice:");
	}
	
	/**
	 * User menu
	 */
	public static void displayUserMenu() {
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
	 * Driver menu
	 */
	public static void displayDriverMenu() {
		System.out.println("1. Add new car");
		System.out.println("2. Update car");
		System.out.println("3. Exit");
		System.out.println("Please enter your choice:");
	}
	
	/**
	 * Admin menu
	 */
	public static void displayAdminMenu() {
		System.out.println("1. Award Top Users");
		System.out.println("2. Exit");
		System.out.println("Please enter your choice:");
	}
	
	/**
	 * Registration menu
	 */
	public static void displayRegisterMenu() {
		System.out.println("What would you like to register as?");
		System.out.println("1. Register as User");
		System.out.println("2. Register as Driver");
		System.out.println("3. Exit");
		System.out.println("Please enter your choice:");
	}
	
	/**
	 * Are you already a driver prompt
	 */
	public static void displayUserRegisterMenu() {
		System.out.println("Are you already a driver?");
		System.out.println("1. Yes");
		System.out.println("2. No");
		System.out.println("3. Exit");
		System.out.println("Please enter your choice:");
	}
	
	/**
	 * Are you already a user prompt
	 */
	public static void displayDriverRegisterMenu() {
		System.out.println("Are you already a user?");
		System.out.println("1. Yes");
		System.out.println("2. No");
		System.out.println("3. Exit");
		System.out.println("Please enter your choice:");
	}
	
	/**
	 * Asks for username and password
	 * @throws IOException
	 */
	public static void loginPrompt() throws IOException {
		String username;
		String password;
		System.out.println("Please enter your username:");
		while ((username = in.readLine()) == null && username.length() == 0)
			;
		System.out.println("Please enter your password:");
		while ((password = in.readLine()) == null && password.length() == 0)
			;
		
		// probably return some kind of user/driver/both/admin object to identify what they are
	}
	
	/**
	 * Asks for a username, password, and password confirmation
	 * @throws IOException
	 */
	public static void registrationPrompt() throws IOException {
		String username;
		String password;
		String password2;
		System.out.println("Please enter your desired username:");
		while ((username = in.readLine()) == null && username.length() == 0)
			;
		System.out.println("Please enter your desired password:");
		while ((password = in.readLine()) == null && password.length() == 0)
			;
		System.out.println("Please enter your password again for confirmation:");
		while ((password2 = in.readLine()) == null && password.length() == 0)
			;
		
		// probably return some kind of user/driver/both/ object to identify what they are
	}
	
	/**
	 * Registers an existing driver as a new user
	 * @throws IOException 
	 */
	public static void registerDriverAsUser() throws IOException {
		System.out.println("Register with your driver credentials");
		loginPrompt();
	}
	
	/**
	 * Registers a new user 
	 * @throws IOException 
	 */
	public static void registerNewUser() throws IOException {
		registrationPrompt();
	}
	
	/**
	 * Registers an existing user as a new driver
	 * @throws IOException 
	 */
	public static void registerUserAsDriver() throws IOException {
		System.out.println("Register with your user credentials");
		loginPrompt();
	}
	
	/**
	 * Registers a new driver
	 * @throws IOException 
	 */
	public static void registerNewDriver() throws IOException {
		registrationPrompt();
	}
	
	/**
	 * Handles user registration.
	 * @throws IOException
	 */
	public static void handleUserRegistration() throws IOException {
		displayDriverRegisterMenu();
		int c = 0;
		String choice;
		boolean level = true;
		while (level) {
			displayUserRegisterMenu();
			while ((choice = in.readLine()) == null && choice.length() == 0)
				;
			try {
				c = Integer.parseInt(choice);
			} catch (Exception e) {
				continue;
			}
			
			switch(c) {
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
				System.out.println("You're input didn't match any of the choices");
				break;
			}
		}
	}
	
	/**
	 * Handles driver registration
	 * @throws IOException
	 */
	public static void handleDriverRegistration() throws IOException {
		displayDriverRegisterMenu();
		int c = 0;
		String choice;
		boolean level = true;
		while (level) {
			displayDriverRegisterMenu();
			while ((choice = in.readLine()) == null && choice.length() == 0)
				;
			try {
				c = Integer.parseInt(choice);
			} catch (Exception e) {
				continue;
			}
			
			switch(c) {
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
				System.out.println("You're input didn't match any of the choices");
				break;
			}
		}
	}
	
	/**
	 * Starts the registration process
	 * @throws IOException
	 */
	public static void handleRegisterMenu() throws IOException {
		int c = 0;
		String choice;
		boolean level = true;
		while (level) {
			displayRegisterMenu();
			while ((choice = in.readLine()) == null && choice.length() == 0)
				;
			try {
				c = Integer.parseInt(choice);
			} catch (Exception e) {
				continue;
			}
			
			switch(c) {
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
				System.out.println("You're input didn't match any of the choices");
				break;
			}
		}
	}
	
	/**
	 * Handles login for someone who is a user and a driver
	 * @throws IOException
	 */
	public static void handleLoginMenu() throws IOException {
		int c = 0;
		String choice;
		boolean level = true;
		while (level) {
			displayLoginMenu();
			while ((choice = in.readLine()) == null && choice.length() == 0)
				;
			try {
				c = Integer.parseInt(choice);
			} catch (Exception e) {
				continue;
			}
			
			switch(c) {
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
				System.out.println("You're input didn't match any of the choices");
				break;
			}
		}
	}
	
	/**
	 * User options
	 * @throws IOException
	 */
	public static void handleUserMenu() throws IOException {
		int c = 0;
		String choice;
		boolean level = true;
		while (level) {
			displayUserMenu();
			while ((choice = in.readLine()) == null && choice.length() == 0)
				;
			try {
				c = Integer.parseInt(choice);
			} catch (Exception e) {
				continue;
			}
			
			switch(c) {
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
				break;
			case 6:
				// edit trusted users
				break;
			case 7:
				// browse cars
				break;
			case 8:
				// get most useful feedback for a driver
				break;
			case 9:
				// get suggested cars
				break;
			case 10:
				// get user degrees of separation
				break;
			case 11:
				// get statistics
				break;
			case 12: 
				// go back
				level = false;
				break;
			default:
				System.out.println("You're input didn't match any of the choices");
				break;
			}
		}
	}
	
	/**
	 * Driver options
	 * @throws IOException
	 */
	public static void handleDriverMenu() throws IOException {
		int c = 0;
		String choice;
		boolean level = true;
		while (level) {
			displayDriverMenu();
			while ((choice = in.readLine()) == null && choice.length() == 0)
				;
			try {
				c = Integer.parseInt(choice);
			} catch (Exception e) {
				continue;
			}
			
			switch(c) {
			case 1:
				// add new car
				break;
			case 2:
				// update car
				break;
			case 3:
				// go back
				level = false;
				break;
			default:
				System.out.println("You're input didn't match any of the choices");
				break;
			}
		}
	}
	
	/**
	 * Admin options
	 * @throws IOException
	 */
	public static void handleAdminMenu() throws IOException {
		int c = 0;
		String choice;
		boolean level = true;
		while (level) {
			displayAdminMenu();
			while ((choice = in.readLine()) == null && choice.length() == 0)
				;
			try {
				c = Integer.parseInt(choice);
			} catch (Exception e) {
				continue;
			}
			
			switch(c) {
			case 1:
				// award top users
				break;
			case 2:
				// go back
				level = false;
				break;
			default:
				System.out.println("You're input didn't match any of the choices");
				break;
			}			
		}
	}

	public static void main(String[] args) {
		Connector2 con = null;
		String choice;
//		String cname;
//		String dname;
		String sql = null;
		int c = 0;
		String username;
		String password;
		try {
			// remember to replace the password
			con = new Connector2();
			System.out.println("Database connection established");

			while (true) {
				displayMenu();
				while ((choice = in.readLine()) == null && choice.length() == 0)
					;
				try {
					c = Integer.parseInt(choice);
				} catch (Exception e) {

					continue;
				}
//				if (c < 1 | c > 3)
//					continue;
				
				switch (c) {
				case 1:
					// login
					System.out.println("Please enter your username:");
					while ((username = in.readLine()) == null && username.length() == 0)
						;
					System.out.println("Please enter your password:");
					while ((password = in.readLine()) == null && password.length() == 0)
						;
					
					// then actually login
					
					// display user menu if user
					handleUserMenu();
					// display driver menu if driver
					handleDriverMenu();
					// display login menu if both
					handleLoginMenu();
					break;
				case 2:
					// register
					handleRegisterMenu();
					break;
				case 3:
					// exit
					con.stmt.close();
					break;
				default:
					System.out.println("You're input didn't match any of the choices");
					break;
				}
				
				/*if (c == 1) {
					System.out.println("please enter a cname:");
					while ((cname = in.readLine()) == null && cname.length() == 0)
						;
					System.out.println("please enter a dname:");
					while ((dname = in.readLine()) == null && dname.length() == 0)
						;
					Course course = new Course();
					System.out.println(course.getCourse(cname, dname, con.stmt));
				} else if (c == 2) {
					 System.out.println("please enter your query below:"); while ((sql =
					 in.readLine()) == null && sql.length() == 0) System.out.println(sql);
					 ResultSet rs=con.stmt.executeQuery(sql); ResultSetMetaData rsmd =
					 rs.getMetaData(); int numCols = rsmd.getColumnCount(); while (rs.next()) {
					 System.out.print("cname:"); for (int i=1; i<=numCols;i++)
					 System.out.print(rs.getString(i)+"  "); System.out.println(""); }
					 System.out.println(" "); rs.close();
				} else {
					con.stmt.close();
					break;
				}*/
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
