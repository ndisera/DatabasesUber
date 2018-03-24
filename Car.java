package cs5530;

/**
 * Basically a struct for displaying properties of a car back to the user.
 * 
 * @author Nico DiSera and Enea Mano
 */
public class Car {
	public int vin;
	public String category;
	
	public Car(int vin, String category) {
		this.vin = vin;
		this.category = category;
	}
}
