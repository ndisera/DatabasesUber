package cs5530;

import java.sql.*;

public class Admin {
	public String login;
	public String password;
	public Statement stmt;

	// Constructor for the User class
	public Admin(String login, String password, Statement stmt) {
		this.login = login;
		this.password = password;
		this.stmt = stmt;
	}

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
