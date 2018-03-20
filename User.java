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

	public void loginAsUser(String login, String password, Statement stmt)
	{

	}

	public String rateUsefulness(int fid, int rating, Statement stmt)
	{
		// TODO check that login of fid is not same as login of user
		// TODO insert a new instance for rating the feedback in tables rates
		String sql = String.format("insert into rates values('%s', %d, %d)", this.login, fid, rating);
		String output="";
		ResultSet rs=null;
		System.out.println("executing "+sql);
		try{
			rs=stmt.executeQuery(sql);
			while (rs.next())
			{
				//System.out.print("cname:");
				output+=String.format("Feedback %d was rated %d by %s", rs.getInt("fid"), rs.getInt("rating"), rs.getString("login")); 
			}

			rs.close();
		}
		catch(Exception e)
		{
			System.out.println("cannot execute the query");
		}
		finally
		{
			try{
				if (rs!=null && !rs.isClosed())
					rs.close();
			}
			catch(Exception e)
			{
				System.out.println("cannot close resultset");
			}
		}
		return output;
	}

	public void setTrustee(String uuLogin, boolean isTrusted, Statement stmt)
	{
		// TODO make sure he login of trustee is not the same as login of the user 
		// TODO insert in table rates 
	}

	public void browseCars(String category, String address, String model, boolean sortByFeedbacks, Statement stmt)
	{
		// TODO categories from UC table, address from the UD table, model from the ctype table
		// TODO output cars that satisfy the attributes using the specified sorting 
	}

	public void getUsefulFeedbacks(String udLogin, int numberOfFeedbacks, Statement stmt)
	{
		// TODO get feedbacks from all cars owned by driver and pick the feedbacks rated the most 
	}

	public void getCarSuggestions(int vin, Statement stmt)
	{
		// TODO get all UCs that were used by users that rode the same car, sort by count/popularity
	}

	public void getSeparationDegree(String uuLogin1, String uuLogin2, Statement stmt)
	{
		// TODO check if they have favorites the same UC (1 degree)
		// TODO check if they are they are 1 degree away from same user (2 degree)
	}

}
