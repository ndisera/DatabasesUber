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
	
	public void rateFeedback(int fid, int rating)
	{
		// TODO check that login of fid is not same as login of user
		// TODO insert a new instance for rating the feedback in tables rates
	}
	
	public void setTrustee(String uuLogin, boolean isTrusted)
	{
		// TODO make sure he login of trustee is not the same as login of the user 
		// TODO insert in table rates 
	}
	
	public void browseCars(String category, String address, String model, boolean sortByFeedbacks)
	{
		// TODO categories from UC table, address from the UD table, model from the ctype table
		// TODO output cars that satisfy the attributes using the specified sorting 
	}
	
	public void getUsefulFeedbacks(String udLogin, int numberOfFeedbacks)
	{
		// TODO get feedbacks from all cars owned by driver and pick the feedbacks rated the most 
	}
	
}
