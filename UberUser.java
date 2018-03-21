package cs5530;

import java.sql.ResultSet;
import java.sql.Statement;

public abstract class UberUser {
	private String login;
	private String password;
	private Statement stmt;
	private String name;
	private String address;
	private int phoneNumber;

	// Constructor for the User class
	public UberUser(String login, String password, Statement stmt)
	{
		this.login = login;
		this.password = password;
		this.stmt = stmt;
	}
	
	public UberUser(String login, String password, Statement stmt, String name, String address, int phoneNumber) {
		this.login = login;
		this.password = password;
		this.stmt = stmt;
		this.name = name;
		this.address = address;
		this.phoneNumber = phoneNumber;
	}
	
	public abstract boolean loginToUber(String login, String password);
	
	public abstract boolean registerForUber(String login, String password, String name, String address, int phoneNumber);
	
	// getters
	
	public String getLogin() {
		return this.login;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public Statement getStmt() {
		return this.stmt;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public int getPhoneNumber() {
		return this.phoneNumber;
	}
	
	// setters
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setStmt(Statement stmt) {
		this.stmt = stmt;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public void setPhoneNumber(int phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
