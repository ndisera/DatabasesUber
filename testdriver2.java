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
		System.out.println(". Exit");
		System.out.println("Please enter your choice:");
	}
	
	public static void displayDriverMenu() {
		System.out.println(". Exit");
		System.out.println("Please enter your choice:");
	}
	
	public static void displayAdminMenu() {
		System.out.println(". Exit");
		System.out.println("Please enter your choice:");
	}

	public static void main(String[] args) {
		Connector con = null;
		String choice;
		String cname;
		String dname;
		String sql = null;
		int c = 0;
		try {
			// remember to replace the password
			con = new Connector();
			System.out.println("Database connection established");

			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

			while (true) {
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
				switch (c) {
				case 1:
					
					break;
				case 2:
					break;
				case 3:
					con.stmt.close();
					break;
				default:
					System.out.println("You're input didn't match any of the choices");
					break;
				}
				if (c == 1) {
					System.out.println("please enter a cname:");
					while ((cname = in.readLine()) == null && cname.length() == 0)
						;
					System.out.println("please enter a dname:");
					while ((dname = in.readLine()) == null && dname.length() == 0)
						;
					Course course = new Course();
					System.out.println(course.getCourse(cname, dname, con.stmt));
				} else if (c == 2) {
					/*
					 * System.out.println("please enter your query below:"); while ((sql =
					 * in.readLine()) == null && sql.length() == 0) System.out.println(sql);
					 * ResultSet rs=con.stmt.executeQuery(sql); ResultSetMetaData rsmd =
					 * rs.getMetaData(); int numCols = rsmd.getColumnCount(); while (rs.next()) {
					 * //System.out.print("cname:"); for (int i=1; i<=numCols;i++)
					 * System.out.print(rs.getString(i)+"  "); System.out.println(""); }
					 * System.out.println(" "); rs.close();
					 */
				} else {
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
