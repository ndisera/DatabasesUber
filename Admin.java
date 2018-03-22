package cs5530;

import java.sql.*;

/**
 * Provides all of the features for an admin of UUber.
 * 
 * @author Nico DiSera and Enea Mano
 */
public class Admin {
	public String login;
	public String password;
	public Statement stmt;

	/**
	 * Creates an admin.
	 * @param login		admin login
	 * @param password	admin password
	 * @param stmt		statement of connection used
	 */
	public Admin(String login, String password, Statement stmt) {
		this.login = login;
		this.password = password;
		this.stmt = stmt;
	}

	/**
	 * Attempts to login to UUber as an admin. 
	 * @param login		admin login
	 * @param password	admin password
	 * @return
	 */
	public boolean loginAsAdmin(String login, String password) {
		String sql = String.format("select * from admin where login = '%s' and password = '%s'", login, password);
		String output = "";
		ResultSet rs = null;
//		System.out.println("executing " + sql);
		try {
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				output += rs.getString("login") + "\n";
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
}
