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
			e.printStackTrace(System.out);
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
	
	/**
	 * Get the most trusted users from the database. 
	 * @param m -> int variable that specifies the number of users to be listed
	 * @return output -> string that contains the most trusted users
	 */
	public String getMostTrustedUsers(int m) {
		String sql = String.format("select t.login2, count(t.login1)-(select count(t1.login1) from trust t1 "
				+ "where t1.is_Trusted=0 and t1.login2=t.login2) as totalTrustees "
				+ "from trust t group by t.login2 order by totalTrustees desc limit %d", m);
		String output = "";
		ResultSet rs = null;
//		System.out.println("executing " + sql);
		try {
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				output += String.format(" %s %d \n", rs.getString("login2"), rs.getInt("totalTrustees"));
			}

			rs.close();
		} catch (Exception e) {
			System.out.println("cannot execute the query");
			e.printStackTrace(System.out);
		} finally {
			try {
				if (rs != null && !rs.isClosed())
					rs.close();
			} catch (Exception e) {
				System.out.println("cannot close resultset");
			}
		}
		return output;
	}
	
	/**
	 * Get the most usuful users from the database. 
	 * @param m -> int variable that specifies the number of users to be listed
	 * @return output -> string that contains the most useful users
	 */
	public String getMostUsefulUsers(int m) {
		String sql = String.format(" select f.login, avg(r.rating) as avgRating from rates r, feedback f "
				+ "where r.fid=f.fid group by f.login order by avgRating desc limit %d", m);
		String output = "";
		ResultSet rs = null;
//		System.out.println("executing " + sql);
		try {
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				output += String.format(" %s %f \n", rs.getString("login"), rs.getFloat("avgRating"));
			}

			rs.close();
		} catch (Exception e) {
			System.out.println("cannot execute the query");
			e.printStackTrace(System.out);
		} finally {
			try {
				if (rs != null && !rs.isClosed())
					rs.close();
			} catch (Exception e) {
				System.out.println("cannot close resultset");
			}
		}
		return output;
	}
}
