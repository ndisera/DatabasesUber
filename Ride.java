package cs5530;

/**
 * Basically a ride struct.
 * 
 * @author Nico DiSera and Enea Mano
 */
public class Ride {
	public int cost;
	public String date; // date
	public String login;
	public int vin;
	public int from_hour;
	public int to_hour;
	
	public Ride(int cost, String date, String login, int vin, int from_hour, int to_hour) {
		this.cost = cost;
		this.date = date;
		this.login = login;
		this.vin = vin;
		this.from_hour = from_hour;
		this.to_hour = to_hour;
	}
}
