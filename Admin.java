package cs5530;

import java.sql.*;

public class Admin {
	public String login;
	public String password;
	public Statement stmt;

	// Constructor for the User class
	public Admin(String login, String password, Statement stmt)
	{
		this.login = login;
		this.password = password;
		this.stmt = stmt;
	}

	public boolean loginAsAdmin(String login, String password)
	{
		return true;
	}
}
