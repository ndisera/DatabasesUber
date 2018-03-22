package cs5530;

import java.sql.Statement;

/**
 * Contains the bare necessities for any user of UUber.
 * 
 * @author Nico DiSera and Enea Mano
 */
public abstract class UberUser {
	private String login;
	private String password;
	private Statement stmt;
	private String name;
	private String address;
	private int phoneNumber;

	/**
	 * Creates a user with the minimum required fields.
	 * 
	 * @param login		user login
	 * @param password	user password
	 * @param stmt		statement of connection used
	 */
	public UberUser(String login, String password, Statement stmt)
	{
		this.login = login;
		this.password = password;
		this.stmt = stmt;
	}
	
	/**
	 * Creates a user and allows non-required fields to be filled.
	 * 
	 * @param login			user login
	 * @param password		user password
	 * @param stmt			statement of connection used
	 * @param name			name of user
	 * @param address		address of user
	 * @param phoneNumber	phone number of user
	 */
	public UberUser(String login, String password, Statement stmt, String name, String address, int phoneNumber) {
		this.login = login;
		this.password = password;
		this.stmt = stmt;
		this.name = name;
		this.address = address;
		this.phoneNumber = phoneNumber;
	}
	
	/**
	 * Attempts to login to UUber and sets other properties of user if successful.
	 * 
	 * @param login		user login
	 * @param password	user password
	 * @return true if login was successful, false otherwise
	 */
	public abstract boolean loginToUber(String login, String password);
	
	/**
	 * Attempts to register for UUber.
	 * 
	 * @return true if registration successful, false otherwise
	 */
	public abstract boolean registerForUber();
	
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
