package cs5530;

import java.sql.*;

public class User
{
	public String login;
	public String password;
	public Statement stmt;

	// Constructor for the User class
	public User(String login, String password, Statement stmt)
	{
		this.login = login;
		this.password = password;
		this.stmt = stmt;
	}

	public void loginAsUser(String login, String password)
	{

	}

	/**
	 * @author Enea
	 * @param fid
	 *            int variable representing the ID of a feedback
	 * @param rating
	 *            int variable representing the rating from 0 to 2 that the user
	 *            is giving to the feedback
	 * @return return String variable that outputs whether the query worked or
	 *         not
	 */
	public String rateUsefulness(int fid, int rating)
	{
		String sqlLoginOfFeedback = String.format("select login from feedback where fid = '%d'", fid);
		String loginOutput = "";
		ResultSet rsLogin = null;
		System.out.println("executing " + sqlLoginOfFeedback);
		try
		{
			rsLogin = this.stmt.executeQuery(sqlLoginOfFeedback);

			if (rsLogin.next())
				loginOutput = String.format("%s", rsLogin.getString("login"));
			else
				return "Feedback does not exist.";
			// System.out.println(loginOutput);
			rsLogin.close();

			if (loginOutput == this.login)
				return "You cannot rate your own feedback.";

		} catch (Exception e)
		{
			System.out.println("cannot execute the query");
			e.printStackTrace(System.out);
		} finally
		{
			freeResultSetResources(rsLogin);
		}

		String sql = String.format("insert into rates values('%s', %d, %d)", this.login, fid, rating);
		String output = "";
		int rs;
		System.out.println("executing " + sql);
		try
		{
			rs = this.stmt.executeUpdate(sql);
			if (rs > 0)
				output = String.format("Feedback %d was rated %d by %s\n", fid, rating, this.login);
			else
				output = "Rating couldn't be inserted.\n";

		} catch (Exception e)
		{
			System.out.println("cannot execute the query");
			e.printStackTrace(System.out);
		}

		// System.out.println(output);
		return output;
	}

	/**
	 * @author Enea
	 * @param uuLogin
	 *            String variable representing the ID of a user
	 * @param isTrusted
	 *            boolean variable representing whether specified user is
	 *            trusted (true) or not (false)
	 * @return return String variable that outputs whether the query worked or
	 *         not
	 */
	public String setTrustee(String uuLogin, boolean isTrusted)
	{
		if (uuLogin == this.login)
			return "You cannot trust yourself.";

		String sql = String.format("insert into trust values('%s', %s, %b)", this.login, uuLogin, isTrusted);
		String output = "";
		int rs;
		System.out.println("executing " + sql);
		try
		{
			rs = this.stmt.executeUpdate(sql);
			if (rs > 0)
				output = String.format("You updated your trust record for %s\n", uuLogin);
			else
				output = "Something went wrong.\n";

		} catch (Exception e)
		{
			System.out.println("cannot execute the query");
			e.printStackTrace(System.out);
		}

		// System.out.println(output);
		return output;
	}

	/**
	 * @author Enea
	 * @param category
	 *            String variable representing the category of the car (sedan,
	 *            suv, etc)
	 * @param address
	 *            String variable representing the location of the car (new york
	 *            city, los angeles, etc)
	 * @param model
	 *            String variable representing the model of the car (ford, bmw,
	 *            etc)
	 * @param sortByFeedbacks
	 *            boolean variable representing whether the query will be sorted
	 *            by the average score of all the available feedbacks (true) or
	 *            by the average score of just the trusted users (false)
	 * @return return String variable that outputs the result of the query
	 */
	public String browseCars(String category, String address, String model, boolean sortByFeedbacks)
	{
		String sql = "select uc.vin, category, address, model, avg(score) from feedback, ud, uc natural join is_c_types natural join c_types where ud.login=uc.login";
		if (category != "")
			sql += String.format(" and category = '%s'", category);
		if (model != "")
			sql += String.format(" and model = '%s'", model);
		if (address != "")
			sql += String.format(" and address = '%s'", address);
		if (sortByFeedbacks)
			sql += " and feedback.vin = uc.vin group by uc.vin, model order by avg(score) desc";
		else
			sql += String.format(
					" and feedback.vin = uc.vin and feedback.login in (select login2 from trust where login1 = '%s') group by uc.vin, model order by avg(score) desc",
					this.login);
		String output = "";
		ResultSet rs = null;
		System.out.println("executing " + sql);
		try
		{
			rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				output += String.format("%d %s %s %s %f", rs.getInt("vin"), rs.getString("category"),
						rs.getString("address"), rs.getString("model"), rs.getFloat("avg(score)"));
			}

			rs.close();
		} catch (Exception e)
		{
			System.out.println("cannot execute the query");
		} finally
		{
			freeResultSetResources(rs);
		}

		return output;
	}

	/**
	 * @author Enea
	 * @param udLogin
	 *            String variable representing a specific uber driver
	 * @param numberofFeedbacks
	 *            int variable representing how many feedbacks should be
	 *            displayed
	 * @return return String variable that outputs the result of the query
	 */
	public String getUsefulFeedbacks(String udLogin, int numberOfFeedbacks)
	{
		String sql = String.format("select f.fid, f.vin, f.login, f.score, f.text" // ,
																					// f.fb_date
																					// "
				+ "from feedback f, rates r, uc, ud "
				+ "where r.fid = f.fid and f.vin = uc.vin and uc.login = ud.login and ud.login = '%s'"
				+ "group by f.fid order by avg(rating) desc", udLogin);
		String output = "";
		ResultSet rs = null;
		System.out.println("executing " + sql);
		try
		{
			rs = stmt.executeQuery(sql);
			int row = 0;
			while (rs.next() && row < numberOfFeedbacks)
			{
				output += String.format("%d %d %s %d %s", rs.getInt("fid"), rs.getInt("vin"), rs.getString("login"),
						rs.getInt("score"), rs.getString("text"));
				row++;
			}

			rs.close();
		} catch (Exception e)
		{
			System.out.println("cannot execute the query");
		} finally
		{
			freeResultSetResources(rs);
		}

		return output;
	}

	/**
	 * @author Enea
	 * @param vin
	 *            int variable representing the car that was just reserved
	 * @return return String variable that outputs all the suggested cars based
	 *         on the one picked by user
	 */
	public String getCarSuggestions(int vin)
	{
		String sql = String.format("select r.vin " + "from uc, reserve r, uu "
				+ "where uc.vin = r.vin and r.login = uu.login and r.login in "
				+ "(select login from reserve where vin = %d) " + "group by r.vin order by count(*) desc", vin);
		String output = "";
		ResultSet rs = null;
		System.out.println("executing " + sql);
		try
		{
			rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				output += String.format("%d \n", rs.getInt("vin"));
			}

			rs.close();
		} catch (Exception e)
		{
			System.out.println("cannot execute the query");
		} finally
		{
			freeResultSetResources(rs);
		}

		return output;
	}

	/**
	 * @author Enea
	 * @param uuLogin1
	 *            String variable representing the first user
	 * @param uuLogin2
	 *            String variable representing the second user
	 * @return output String variable which represents the degree of separation
	 *         between the two
	 */
	public String getSeparationDegree(String uuLogin1, String uuLogin2)
	{
		// TODO check if they are 1 degree away from same user (2
		// degree)
		String sql = String.format(
				"select f.vin from favorites f where f.login = '%s' and exists "
						+ "(select f1.vin from favorites f1 where f1.login = '%s' and f.vin = f1.vin)",
				uuLogin1, uuLogin2);
		String output = "";
		ResultSet rs = null;
		System.out.println("executing " + sql);
		try
		{
			rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				output = "1-degree away \n";
				break;
			}

			rs.close();
		} catch (Exception e)
		{
			System.out.println("cannot execute the query");
		} finally
		{
			freeResultSetResources(rs);
		}
		
		return output;
	}

	// Helper method
	// Reduce duplicate code within the finally blocks
	// Frees up the database query stored in the resultset
	public void freeResultSetResources(ResultSet rs)
	{
		try
		{
			if (rs != null && !rs.isClosed())
				rs.close();
		} catch (Exception e)
		{
			System.out.println("cannot close resultset");
		}
	}

}
