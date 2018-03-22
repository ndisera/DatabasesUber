package cs5530;

import java.sql.*;

/**
 * Provides all of the features for a driver of UUber.
 * 
 * @author Nico DiSera and Enea Mano
 */
public class Driver extends UberUser {

	/**
	 * Creates a driver with the minimum required fields.
	 * 
	 * @param login		driver login
	 * @param password	driver password
	 * @param stmt		statement of connection used
	 */
	public Driver(String login, String password, Statement stmt)
	{
		super(login, password, stmt);
	}
	
	/**
	 * Creates a driver and allows non-required fields to be filled.
	 * 
	 * @param login			driver login
	 * @param password		driver password
	 * @param stmt			statement of connection used
	 * @param name			name of driver
	 * @param address		address of driver
	 * @param phoneNumber	phone number of driver
	 */
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
	
	public boolean registerForUber() {		
		// make sure login returns false
		if (loginToUber(this.getLogin(), this.getPassword())) {
			return false;
		}
		String sql = String.format("insert into ud (login, name, address, phone, password) values('%s', '%s', '%s', %d, '%s');", 
					this.getLogin(), this.getName(), this.getAddress(), this.getPhoneNumber(), this.getPassword());
		
		int rs = 0;
//		System.out.println("executing " + sql);
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
	
	public boolean addCar(int vin, String category, String login) {
		return false;
	}
	
	public boolean updateCar(int vin, String category, String login) {
		return false;
	}
}
