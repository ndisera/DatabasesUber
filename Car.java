package cs5530;

/**
 * Basically a struct for keeping track of cars in reservations
 * 
 * @author Nico DiSera and Enea Mano
 */
public class Car {
	public int vin;
	public String category;
	public int pid;
	
	public Car(int vin, String category, int pid) {
		this.vin = vin;
		this.category = category;
		this.pid = pid;
	}
}
