package cs5530;

import java.sql.*;

public class Driver extends UberUser {

	// Constructor for the Driver class
	public Driver(String login, String password, Statement stmt)
	{
		super(login, password, stmt);
	}
	
	public Driver(String login, String password, Statement stmt, String name, String address, int phoneNumber) {
		super(login, password, stmt, name, address, phoneNumber);
	}
	
	public boolean loginToUber(String login, String password) {
		String sql = String.format("select * from ud where login = '%s' and password = '%s'", login, password);
		String output = "";
		ResultSet rs = null;
//		System.out.println("executing " + sql);
		try {
			rs = this.getStmt().executeQuery(sql);
			while (rs.next()) {
				output += rs.getString("login") + "\n";
				this.setName(rs.getString("name"));
				this.setAddress(rs.getString("address"));
				this.setPhoneNumber(rs.getInt("phone"));
			}

			rs.close();
		} catch (Exception e) {
			System.out.println("cannot execute the query");
		} finally {
			try {
				if (rs != null && !rs.isClosed())
					rs.close();
			} catch (Exception e) {
				System.out.println("cannot close resultset");
			}
		}
		if (output.equals("")) {
			return false;
		}
		return true;
	}
	
	public boolean registerForUber(String login, String password, String name, String address, int phoneNumber) {		
		// make sure login returns false
		if (loginToUber(login, password)) {
			return false;
		}
		// insert into table
		String sql = String.format("insert into ud (login, name, address, phone, password) values ('%s', '%s', '%s', %d, '%s');", 
				login, name, address, phoneNumber, password);
		int rs = 0;
		System.out.println("executing " + sql);
		try
		{
			rs = this.getStmt().executeUpdate(sql);
			
		} catch (Exception e)
		{
			System.out.println("cannot execute the query");
			e.printStackTrace(System.out);
		}
		
		if (rs > 0) {
			return true;
		}
		return false;
	}
}
