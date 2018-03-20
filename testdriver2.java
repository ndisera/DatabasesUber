package cs5530;

import java.lang.*;
import java.sql.*;
import java.io.*;

public class testdriver2 {

	/**
	 * @param args
	 */
	public static void displayMenu() {
		System.out.println("        Welcome to UUber System     ");
		System.out.println("1. Login");
		System.out.println("2. Register");
		System.out.println("3. Close Connection:");
		System.out.println("Please enter your choice:");
	}
	
	public static void displayLoginMenu() {
		System.out.println("Do you want to proceed as a User or a Driver?");
		System.out.println("1. User");
		System.out.println("2. Driver");
		System.out.println("3. Exit");
		System.out.println("Please enter your choice:");
	}
	
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
	
	public static void displayDriverMenu() {
		System.out.println("1. Add new car");
		System.out.println("2. Update car");
		System.out.println("3. Exit");
		System.out.println("Please enter your choice:");
	}
	
	public static void displayAdminMenu() {
		System.out.println("1. Award Top Users");
		System.out.println("2. Exit");
		System.out.println("Please enter your choice:");
	}

	public static void main(String[] args) {
		Connector2 con = null;
		String choice;
		String cname;
		String dname;
		String sql = null;
		int c = 0;
		try {
			// remember to replace the password
			con = new Connector2();
			System.out.println("Database connection established");

			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			boolean firstLevel = true;

			while (firstLevel) {
				displayMenu();
				while ((choice = in.readLine()) == null && choice.length() == 0)
					;
				try {
					c = Integer.parseInt(choice);
				} catch (Exception e) {

					continue;
				}
				if (c < 1 | c > 3)
					continue;
				
				// Nico Start
				switch (c) {
				case 1:
					// login
					break;
				case 2:
					// register
					break;
				case 3:
					// exit
					con.stmt.close();
					break;
				default:
					System.out.println("You're input didn't match any of the choices");
					break;
				}
				
				// Nico End
				
				if (c == 1) 
				{
					System.out.println("please enter a cname:");
					while ((cname = in.readLine()) == null && cname.length() == 0)
						;
					System.out.println("please enter a dname:");
					while ((dname = in.readLine()) == null && dname.length() == 0)
						;
					Course course = new Course();
					System.out.println(course.getCourse(cname, dname, con.stmt));
				} 
				else if (c == 2) 
				{
					 System.out.println("please enter your query below:"); 
					 while ((sql = in.readLine()) == null && sql.length() == 0) 
						 System.out.println(sql);
					 ResultSet rs=con.stmt.executeQuery(sql); 
					 ResultSetMetaData rsmd = rs.getMetaData(); 
					 int numCols = rsmd.getColumnCount(); 
					 while (rs.next()) 
					 {
						 System.out.print("cname:"); 
						 for (int i=1; i<=numCols;i++)
							 System.out.print(rs.getString(i)+"  "); 
						 System.out.println(""); 
					 }
					 	System.out.println(" "); 
					 	rs.close();
				} 
				else 
				{
					con.stmt.close();
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
