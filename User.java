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

	public String rateUsefulness(int fid, int rating)
	{
		// TODO check that login of fid is not same as login of user
		// TODO insert a new instance for rating the feedback in tables rates
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
//			System.out.println(loginOutput);
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

//		System.out.println(output);
		return output;
	}

	public void setTrustee(String uuLogin, boolean isTrusted)
	{
		// TODO make sure he login of trustee is not the same as login of the
		// user
		// TODO insert in table rates
	}

	public void browseCars(String category, String address, String model, boolean sortByFeedbacks)
	{
		// TODO categories from UC table, address from the UD table, model from
		// the ctype table
		// TODO output cars that satisfy the attributes using the specified
		// sorting
	}

	public void getUsefulFeedbacks(String udLogin, int numberOfFeedbacks)
	{
		// TODO get feedbacks from all cars owned by driver and pick the
		// feedbacks rated the most
	}

	public void getCarSuggestions(int vin)
	{
		// TODO get all UCs that were used by users that rode the same car, sort
		// by count/popularity
	}

	public void getSeparationDegree(String uuLogin1, String uuLogin2)
	{
		// TODO check if they have favorites the same UC (1 degree)
		// TODO check if they are they are 1 degree away from same user (2
		// degree)
	}
	
	// Helper method
	// Reduce duplicate code within the finally blocks
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
