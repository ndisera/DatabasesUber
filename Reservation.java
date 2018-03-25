package cs5530;

/**
 * Basically a reservation struct.
 * 
 * @author Nico DiSera and Enea Mano
 */
public class Reservation {
	public String login;
	public int vin;
	public int pid;
	public int cost;
	public String date; // datetime
	public String operation; // update or insert
	
	public Reservation(String login, int vin, int pid, int cost, String date, String operation) {
		this.login = login;
		this.vin = vin;
		this.pid = pid;
		this.cost = cost;
		this.date = date;
		this.operation = operation;
	}
}
