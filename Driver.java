package cs5530;

import java.sql.*;

public class Driver {

	public String login;
	public String password;
	public Statement stmt;

	// Constructor for the Driver class
	public Driver(String login, String password, Statement stmt)
	{
		this.login = login;
		this.password = password;
		this.stmt = stmt;
	}

	public boolean loginAsDriver(String login, String password)
	{
		return true;
	}
	
	public boolean registerAsDriver(String login, String password) {
		return false;
	}
}
