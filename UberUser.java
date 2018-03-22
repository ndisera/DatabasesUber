package cs5530;

import java.sql.ResultSet;
import java.sql.Statement;

public abstract class UberUser {
	private String login;
	private String password;
	private Statement stmt;
	private String name;
	private String address;
	private Integer phoneNumber;

	// Constructor 1
	public UberUser(String login, String password, Statement stmt)
	{
		this.login = login;
		this.password = password;
		this.stmt = stmt;
	}
	
	// Constructor 2
	public UberUser(String login, String password, Statement stmt, String name, String address, Integer phoneNumber) {
		this.login = login;
		this.password = password;
		this.stmt = stmt;
		this.name = name;
		this.address = address;
		this.phoneNumber = phoneNumber;
	}
	
	/**
	 * Attempts to login to UUber
	 * @param login
	 * @param password
	 * @return true if login was successful, false otherwise
	 */
	public abstract boolean loginToUber(String login, String password);
	
	/**
	 * Attempts to register for UUber
	 * @param login
	 * @param password
	 * @param name
	 * @param address
	 * @param phoneNumber
	 * @return true if registration successful, false otherwise
	 */
	public abstract boolean registerForUber(String login, String password, String name, String address, Integer phoneNumber);
	
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
	
	public void setPhoneNumber(Integer phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
