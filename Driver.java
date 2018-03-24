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
			freeResultSetResources(rs);
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
	
	/**
	 * Creates and adds a car of the user.
	 * 
	 * @param vin
	 *            car vin
	 * @param category
	 *            car category (economy, comfort, luxury)
	 * @param year
	 *            car year
	 * @param make
	 *            car make
	 * @param model
	 *            car model
	 * @return true if car successfully added, false otherwise
	 */
	public boolean addCar(int vin, String category, int year, String make, String model) {
		// I will insert into uc the vin, category, login, and year
		String sql = String.format("insert into uc (vin, category, year, login) values(%d, '%s', %d, '%s');", vin,
				category, year, this.getLogin());
		int rs = 0;
		int tid = 0;
		ResultSet resultSet = null;
		try {
			rs = this.getStmt().executeUpdate(sql);
		} catch (Exception e) {
			System.out.println("cannot execute the query");
		}
		if (rs == 0) {
			return false;
		}

		// I will lookup the ctypes table to see if make and model match
		sql = String.format("select * from c_types where make = '%s' and model = '%s';", make, model);
		try {
			resultSet = this.getStmt().executeQuery(sql);
			while (resultSet.next()) {
				tid = resultSet.getInt("tid");
			}

			resultSet.close();
		} catch (Exception e) {
			System.out.println("cannot execute the query");
			return false;
		} finally {
			freeResultSetResources(resultSet);
		}

		if (tid == 0) {
			// there is no c_type with the given make and model, I need to insert one
			sql = String.format("insert into c_types (make, model) values('%s', '%s');", make, model);
			rs = 0;
			try {
				rs = this.getStmt().executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			} catch (Exception e) {
				System.out.println("cannot execute the query");
			}
			if (rs == 0) {
				return false;
			}
			// otherwise I think rs is the tid that I need
			try {
				ResultSet generatedKeys = this.getStmt().getGeneratedKeys();
				if (generatedKeys.next()) {
					tid = generatedKeys.getInt(1);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// insert new record into is_c_types
		sql = String.format("insert into is_c_types (vin, tid) values (%d, %d);", vin, tid);
		rs = 0;
		try {
			rs = this.getStmt().executeUpdate(sql);
		} catch (Exception e) {
			System.out.println("cannot execute the query");
		}
		if (rs == 0) {
			return false;
		}

		return true;
	}

	/**
	 * Updates a car of the user.
	 * 
	 * @param vin
	 *            car vin
	 * @param category
	 *            car category (economy, comfort, luxury)
	 * @param year
	 *            car year
	 * @param make
	 *            car make
	 * @param model
	 *            car model
	 * @return true if car successfully updated, false otherwise
	 */
	public boolean updateCar(int vin, String category, int year, String make, String model) {
		// update uc
		String sql = String.format("update uc set category = '%s', year = %d where vin = %d;", category, year, vin);
		int rs = 0;
		int tid = 0;
		ResultSet resultSet = null;
		try {
			rs = this.getStmt().executeUpdate(sql);
		} catch (Exception e) {
			System.out.println("cannot execute the query");
		}
		if (rs == 0) {
			return false;
		}

		// I will lookup the ctypes table to see if make and model match
		sql = String.format("select * from c_types where make = '%s' and model = '%s';", make, model);
		try {
			resultSet = this.getStmt().executeQuery(sql);
			while (resultSet.next()) {
				tid = resultSet.getInt("tid");
			}

			resultSet.close();
		} catch (Exception e) {
			System.out.println("cannot execute the query");
			return false;
		} finally {
			freeResultSetResources(resultSet);
		}

		if (tid == 0) {
			// there is no c_type with the given make and model, I need to insert one
			sql = String.format("insert into c_types (make, model) values('%s', '%s');", make, model);
			rs = 0;
			try {
				rs = this.getStmt().executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			} catch (Exception e) {
				System.out.println("cannot execute the query");
			}
			if (rs == 0) {
				return false;
			}
			
			try {
				ResultSet generatedKeys = this.getStmt().getGeneratedKeys();
				if (generatedKeys.next()) {
					tid = generatedKeys.getInt(1);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// now I update the tid for the vin
		sql = String.format("update is_c_types set tid = %d where vin = %d;", tid, vin);
		rs = 0;
		try {
			rs = this.getStmt().executeUpdate(sql);
		} catch (Exception e) {
			System.out.println("cannot execute the query");
		}
		if (rs == 0) {
			return false;
		}

		return true;
	}
	
	/**
	 * Inserts a new driver's availability.
	 * 
	 * @param startHour the first hour of the day a driver is available
	 * @param endHour the last hour of the day a driver is available
	 */
	public void insertAvailability(int startHour, int endHour) {
		String sql = String.format("select * from period where from_hour = %d and to_hour = %d;", startHour, endHour);
		ResultSet rs = null;
		int rs2 = 0;
		int pid = 0;
//		System.out.println("executing " + sql);
		try {
			rs = this.getStmt().executeQuery(sql);
			if (rs.next()) {
				pid = Integer.parseInt(rs.getString("pid"));
			}

			rs.close();
		} catch (Exception e) {
			System.out.println("cannot execute the query");
		} finally {
			freeResultSetResources(rs);
		}
		if (pid == 0) {
			// we didn't find anything that matched so we have to insert
			sql = String.format("insert into period (from_hour, to_hour) values(%d, %d)", startHour, endHour);
			try {
				rs2 = this.getStmt().executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			} catch (Exception e) {
				System.out.println("cannot execute the query");
			}
			
			// if rs2 == 0 then something is wrong with the database and it needs to be fixed
			try {
				ResultSet generatedKeys = this.getStmt().getGeneratedKeys();
				if (generatedKeys.next()) {
					pid = generatedKeys.getInt(1);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		sql = String.format("insert into available (login, pid) values('%s', %d)", this.getLogin(), pid);
		rs2 = 0;
		try {
			rs2 = this.getStmt().executeUpdate(sql);
		} catch (Exception e) {
			System.out.println("cannot execute the query");
		}
	}
}
