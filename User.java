package cs5530;

import java.sql.*;
import java.util.ArrayList;

import com.mysql.jdbc.exceptions.*;

/**
 * Provides all of the features for a user or "rider" of UUber.
 * 
 * @author Nico DiSera and Enea Mano
 */
public class User extends UberUser {
	/**
	 * Creates a user with the minimum required fields.
	 * 
	 * @param login
	 *            user login
	 * @param password
	 *            user password
	 * @param stmt
	 *            statement of connection used
	 */
	public User(String login, String password, Statement stmt) {
		super(login, password, stmt);
	}

	/**
	 * Creates a user and allows non-required fields to be filled.
	 * 
	 * @param login
	 *            user login
	 * @param password
	 *            user password
	 * @param stmt
	 *            statement of connection used
	 * @param name
	 *            name of user
	 * @param address
	 *            address of user
	 * @param phoneNumber
	 *            phone number of user
	 */
	public User(String login, String password, Statement stmt, String name, String address, int phoneNumber) {
		super(login, password, stmt, name, address, phoneNumber);
	}

	public boolean loginToUber(String login, String password) {
		String sql = String.format("select * from uu where login = '%s' and password = '%s'", login, password);
		String output = "";
		ResultSet rs = null;
		// System.out.println("executing " + sql);
		try {
			rs = this.getStmt().executeQuery(sql);
			while (rs.next()) {
				output += rs.getString("login") + "\n";
				this.setName(rs.getString("name"));
				this.setAddress(rs.getString("address"));
				this.setPhoneNumber(rs.getInt("phone"));
			}

			rs.close();
		} catch (Exception e) {
			System.out.println("cannot execute the query");
			System.out.println(e.getMessage());
//			e.printStackTrace(System.out);
		} finally {
			freeResultSetResources(rs);
		}
		if (output.equals("")) {
			return false;
		}
		return true;
	}

	public boolean registerForUber() {
		// make sure login returns false
		if (loginToUber(this.getLogin(), this.getPassword())) {
			return false;
		}
		// insert into table
		String sql = String.format(
				"insert into uu (login, name, address, phone, password) values('%s', '%s', '%s', %d, '%s');",
				this.getLogin(), this.getName(), this.getAddress(), this.getPhoneNumber(), this.getPassword());

		int rs = 0;
		// System.out.println("executing " + sql);
		try {
			rs = this.getStmt().executeUpdate(sql);

		} catch (Exception e) {
			System.out.println("cannot execute the query");
			System.out.println(e.getMessage());
//			e.printStackTrace(System.out);
		}

		if (rs > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Grabs all of the cars available for reservation where the driver is working
	 * and not already reserved.
	 * 
	 * @param time
	 *            the time of the reservation
	 * @return An ArrayList of the cars available for reservation
	 */
	public ArrayList<Car> getAvailableCars(String date, String time) {
		int hour = Integer.parseInt(time.substring(0, 2));
		// get all cars where the driver is available and not already reserved
		ArrayList<Car> cars = new ArrayList<Car>();
		String datetime = date + " " + time + ":00";
		// might also want to display price here if it correlates with category
		String sql = String.format("SELECT cars.vin as vin, cars.category as category, a.pid as pid "
				+ "FROM 5530db34.uc cars, 5530db34.available a " + "WHERE cars.login = a.login "
				+ "and a.pid in (SELECT pid " + "FROM 5530db34.period " + "WHERE from_hour <= %d AND to_hour >= %d) "
				+ "and cars.vin NOT IN (SELECT r.vin " + "FROM 5530db34.reserve r "
				+ "WHERE (r.date > ADDTIME('%s', '-0:30:0') AND r.date < ADDTIME('%s', '0:30:0'))"
				+ "and r.date > NOW());", hour, hour, datetime, datetime);

		ResultSet rs = null;
		try {
			rs = this.getStmt().executeQuery(sql);
			while (rs.next()) {
				cars.add(new Car(rs.getInt("vin"), rs.getString("category"), rs.getInt("pid")));
			}

			rs.close();
		} catch (Exception e) {
			System.out.println("cannot execute the query");
			System.out.println(e.getMessage());
//			e.printStackTrace(System.out);
		} finally {
			freeResultSetResources(rs);
		}

		return cars;
	}

	/**
	 * Creates a reservation with a cost linked to the car category
	 * 
	 * @param datetime
	 *            date and time of reservation
	 * @param car
	 *            other details for the reservation
	 * @return a new Reservation
	 */
	public Reservation makeReservation(String datetime, Car car) {
		// getting cost from category
		// most of the time will be economy
		int cost = 15;
		if (car.category.equals("comfort")) {
			cost = 20;
		} else if (car.category.equals("luxury")) {
			cost = 25;
		}
		return new Reservation(this.getLogin(), car.vin, car.pid, cost, datetime);
	}

	/**
	 * Insets all of the user's added reservations.
	 * 
	 * @param reservations
	 *            Arraylist of reservations
	 * @return true if successful, false if otherwise
	 */
	public boolean submitReservations(ArrayList<Reservation> reservations) {
		// Here I insert each one of these reservations

		// assume that reservations isn't empty here
		String sql = "insert into reserve (login, vin, pid, cost, date) values";
		int rs = 0;
		Reservation res;
		boolean val = true;
		// if reservations size isn't zero
		for (int i = 0; i < reservations.size() - 1; i++) {
			res = reservations.get(i);
			sql += String.format("('%s', %d, %d, %d, '%s'),", this.getLogin(), res.vin, res.pid, res.cost, res.date);
		}
		res = reservations.get(reservations.size() - 1);
		sql += String.format("('%s', %d, %d, %d, '%s');", this.getLogin(), res.vin, res.pid, res.cost, res.date);
		try {
			rs = this.getStmt().executeUpdate(sql);
		} catch (Exception e) {
			System.out.println("cannot execute the query");
			System.out.println(e.getMessage());
//			e.printStackTrace(System.out);
		}
		val = rs > 0 ? true : false;
		return val;
	}

	/**
	 * Checks to see if a ride being recorded is valid, meaning a driver works
	 * during this time period.
	 * 
	 * @param cost
	 *            cost of the ride
	 * @param date
	 *            date of the ride
	 * @param vin
	 *            car vin
	 * @param from_hour
	 *            hour ride started
	 * @param to_hour
	 *            hour ride ended
	 * @return Ride if ride is valid, null otherwise
	 */
	public Ride recordRide(int cost, String date, int vin, int from_hour, int to_hour) {
		// check that this will be a valid ride
		// get driver for vin and check that he matches with a pid containing these
		// hours
		String sql = String.format(
				"SELECT login FROM 5530db34.available WHERE pid IN "
						+ "(SELECT pid FROM 5530db34.period WHERE from_hour <= %d AND to_hour >= %d);",
				from_hour, to_hour);
		ResultSet rs = null;
		String output = "";
		try {
			rs = this.getStmt().executeQuery(sql);
			while (rs.next()) {
				output += rs.getString("login");
			}
			rs.close();
		} catch (Exception e) {
			System.out.println("cannot execute the query");
			System.out.println(e.getMessage());
//			e.printStackTrace(System.out);
		} finally {
			freeResultSetResources(rs);
		}

		return output.equals("") ? null : new Ride(cost, date, this.getLogin(), vin, from_hour, to_hour);
	}

	/**
	 * Inserts all of the rides a user has recorded into the database.
	 * 
	 * @param rides
	 *            an ArrayList of rides
	 * @return true if insert successful, false otherwise
	 */
	public boolean submitRides(ArrayList<Ride> rides) {
		// Here I insert each one of these rides
		String sql = "insert into ride (cost, date, login, vin, from_hour, to_hour) values";
		Ride ride = null;
		int rs = 0;
		for (int i = 0; i < rides.size() - 1; i++) {
			ride = rides.get(i);
			sql += String.format("(%d, '%s', '%s', %d, %d, %d),", ride.cost, ride.date, this.getLogin(), ride.vin,
					ride.from_hour, ride.to_hour);
		}
		ride = rides.get(rides.size() - 1);
		sql += String.format("(%d, '%s', '%s', %d, %d, %d);", ride.cost, ride.date, this.getLogin(), ride.vin,
				ride.from_hour, ride.to_hour);
		try {
			rs = this.getStmt().executeUpdate(sql);
		} catch (Exception e) {
			System.out.println("cannot execute the query");
			System.out.println(e.getMessage());
//			e.printStackTrace(System.out);
		}
		boolean val = rs > 0 ? true : false;
		return val;
	}

	/**
	 * Attempts to add a car to favorites.
	 * 
	 * @param vin
	 *            the vin of the car being favorited
	 * @return true if car successfully favorited, false otherwise
	 */
	public boolean favoriteCar(int vin) {
		String sql = String.format("insert into favorites (vin, login, fv_date) values(%d, '%s', DATE(NOW()));", vin,
				this.getLogin());
		int rs;
		try {
			rs = this.getStmt().executeUpdate(sql);
			if (rs > 0)
				return true;
		} catch (Exception e) {
			System.out.println("cannot execute the query");
			System.out.println(e.getMessage());
		}
		return false;
	}

	public boolean leaveFeedback(int vin, int score, String comment) {
		String sql = String.format(
				"insert into feedback (vin, login, score, text, fb_date) values(%d, '%s', %d, '%s', DATE(NOW()));", vin,
				this.getLogin(), score, comment);
		int rs;
		try {
			rs = this.getStmt().executeUpdate(sql);
			if (rs > 0)
				return true;
		} catch (Exception e) {
			System.out.println("cannot execute the query");
			System.out.println(e.getMessage());
		}
		return false;
	}

	/**
	 * @param fid
	 *            int variable representing the ID of a feedback
	 * @param rating
	 *            int variable representing the rating from 0 to 2 that the user is
	 *            giving to the feedback
	 * @return return String variable that outputs whether the query worked or not
	 */
	public String rateUsefulness(int fid, int rating) {
		String sqlLoginOfFeedback = String.format("select login from feedback where fid = '%d'", fid);
		String loginOutput = "";
		ResultSet rsLogin = null;
		// System.out.println("executing " + sqlLoginOfFeedback);
		try {
			rsLogin = this.getStmt().executeQuery(sqlLoginOfFeedback);

			if (rsLogin.next())
				loginOutput = String.format("%s", rsLogin.getString("login"));
			else
				return "Feedback does not exist.";
			// System.out.println(loginOutput);
			rsLogin.close();

			if (loginOutput.equals(this.getLogin()))
				return "You cannot rate your own feedback.";

		} catch (Exception e) {
			System.out.println("cannot execute the query");
			// e.printStackTrace(System.out);
		} finally {
			freeResultSetResources(rsLogin);
		}

		String sql = String.format("insert into rates values('%s', %d, %d)", this.getLogin(), fid, rating);
		String output = "";
		int rs;
		// System.out.println("executing " + sql);
		try {
			rs = this.getStmt().executeUpdate(sql);

			if (rs > 0)
				output = String.format("Feedback %d was rated %d by %s\n", fid, rating, this.getLogin());
			else
				output = "Rating couldn't be inserted.\n";

		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("cannot execute the query");
		}

		// System.out.println(output);
		return output;
	}

	/**
	 * @param uuLogin
	 *            String variable representing the ID of a user
	 * @param isTrusted
	 *            boolean variable representing whether specified user is trusted
	 *            (true) or not (false)
	 * @return return String variable that outputs whether the query worked or not
	 */
	public String setTrustee(String uuLogin, boolean isTrusted) {
		if (uuLogin.equals(this.getLogin()))
			return "You cannot trust yourself.";

		String sql = String.format("insert into trust values('%s', '%s', %b)", this.getLogin(), uuLogin, isTrusted);
		String output = "";
		int rs;
		// System.out.println("executing " + sql);
		try {
			rs = this.getStmt().executeUpdate(sql);
			if (rs > 0)
				output = String.format("You updated your trust record for %s\n", uuLogin);
			else
				output = "Something went wrong.\n";

		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("cannot execute the query");
		}

		// System.out.println(output);
		return output;
	}

	/**
	 * @param category
	 *            String variable representing the category of the car (sedan, suv,
	 *            etc)
	 * @param address
	 *            String variable representing the location of the car (new york
	 *            city, los angeles, etc)
	 * @param model
	 *            String variable representing the model of the car (ford, bmw, etc)
	 * @param sortByFeedbacks
	 *            boolean variable representing whether the query will be sorted by
	 *            the average score of all the available feedbacks (true) or by the
	 *            average score of just the trusted users (false)
	 * @return return String variable that outputs the result of the query
	 */
	public String browseCars(String category, String address, String model, String model_address,
			String category_address, String model_category, boolean sortByFeedbacks) {
		/*
		 * select uc.vin, uc.category, ct.model, ud.address, avg(score) from ud,
		 * is_c_types ict natural join c_types ct, uc left join feedback f on
		 * uc.vin=f.vin where ud.login=uc.login and ict.vin=uc.vin
		 * 
		 * and (category = 'luxury' or model='Focus' or address='something')
		 * 
		 * and f.login in (select login2 from trust where login1 = '%s')
		 * 
		 * group by uc.vin, ct.model, ud.address order by avg(score) desc
		 * 
		 */
		String sql = "select uc.vin, uc.category, ct.model, ud.address, avg(score) "
				+ "from ud, is_c_types ict natural join c_types ct, uc left join feedback f on uc.vin=f.vin "
				+ "where ud.login=uc.login and ict.vin=uc.vin";

		if (!category.equals("") && !address.equals("") && !model.equals(""))
			sql += String.format(" and (category = '%s' %s address='%s' %s model='%s')", category, category_address,
					address, model_address, model);

		else if (!category.equals("") && !address.equals(""))
			sql += String.format(" and (category = '%s' %s address='%s')", category, category_address, address);
		else if (!category.equals("") && !model.equals(""))
			sql += String.format(" and (category = '%s' %s model='%s')", category, model_category, model);
		else if (!address.equals("") && !model.equals(""))
			sql += String.format(" and (address = '%s' %s model='%s')", address, model_address, model);

		else if (!category.equals(""))
			sql += String.format(" and (category = '%s')", category);
		else if (!address.equals(""))
			sql += String.format(" and (address = '%s')", address);
		else if (!model.equals(""))
			sql += String.format(" and (model = '%s')", model);

		if (!sortByFeedbacks)
			sql += String.format(" and f.login in (select login2 from trust where login1 = '%s')", this.getLogin());
		sql += " group by uc.vin, ct.model, ud.address order by avg(score) desc";

		String output = "";
		ResultSet rs = null;
		System.out.println("executing " + sql);
		try {
			rs = this.getStmt().executeQuery(sql);
			while (rs.next()) {
				output += String.format("VIN: %d, Category: %s, Address: %s, Model: %s, Average Score: %f \n",
						rs.getInt("vin"), rs.getString("category"), rs.getString("address"), rs.getString("model"),
						rs.getFloat("avg(score)"));
			}

			rs.close();
		} catch (Exception e) {
			System.out.println("cannot execute the query");
			System.out.println(e.getMessage());
//			e.printStackTrace(System.out);
		} finally {
			freeResultSetResources(rs);
		}

		if (output.equals(""))
			output = "No results found";
		return output;
	}

	/**
	 * @param udLogin
	 *            String variable representing a specific uber driver
	 * @param numberofFeedbacks
	 *            int variable representing how many feedbacks should be displayed
	 * @return return String variable that outputs the result of the query
	 */
	public String getUsefulFeedbacks(String udLogin, int numberOfFeedbacks) {
		String sql = String
				.format("select f.fid, f.vin, f.login, f.score, f.text " + "from feedback f, rates r, uc, ud "
						+ "where r.fid = f.fid and f.vin = uc.vin and uc.login = ud.login and ud.login = '%s' "
						+ "group by f.fid order by avg(rating) desc", udLogin);
		String output = "";
		ResultSet rs = null;
		// System.out.println("executing " + sql);
		try {
			rs = this.getStmt().executeQuery(sql);
			int row = 0;
			while (rs.next() && row < numberOfFeedbacks) {
				output += String.format("Feedback ID: %d, VIN:  %d, Login: %s, Score: %d, Text: %s \n",
						rs.getInt("fid"), rs.getInt("vin"), rs.getString("login"), rs.getInt("score"),
						rs.getString("text"));
				row++;
			}

			rs.close();
		} catch (Exception e) {
			System.out.println("cannot execute the query");
			System.out.println(e.getMessage());
//			e.printStackTrace(System.out);
		} finally {
			freeResultSetResources(rs);
		}

		if (output.equals(""))
			output = "No results found";
		return output;
	}

	/**
	 * @return return String variable -> outputs all feedbacks 
	 */
	public String getFeedbacks() {
		String sql = "select * from feedback";
		String output = "";
		ResultSet rs = null;	
		// System.out.println("executing " + sql);
		try {
			rs = this.getStmt().executeQuery(sql);
			while (rs.next()) {
				output += String.format("%d. VIN: %d, Login: %s, Score: %d, "
						+ "Text: %s \n", rs.getInt("fid"), rs.getInt("vin"), rs.getString("login"), rs.getInt("score"), rs.getString("text"));
			}
			rs.close();
		} catch (Exception e) {
			System.out.println("cannot execute the query");
			System.out.println(e.getMessage());
//			e.printStackTrace(System.out);
		} finally {
			freeResultSetResources(rs);
		}

		if (output.equals(""))
			output = "No results found";
		
		return output;
	}
	
	/**
	 * @param vin
	 *            int variable representing the car that was just reserved
	 * @return return String variable that outputs all the suggested cars based on
	 *         the one picked by user
	 */
	public String getCarSuggestions(int vin) {
		String sql = String.format(
				"select rs.vin, count(distinct rd.rid) as totalRides from ride rd right outer join reserve rs on rd.vin=rs.vin"
						+ " where rs.vin <> %d and rs.login in"
						+ " (select login from reserve where vin = %d and login<>'%s') group by rs.vin order by count(distinct rd.rid) desc",
				vin, vin, this.getLogin());
		String output = "";
		ResultSet rs = null;
		// System.out.println("executing " + sql);
		try {
			rs = this.getStmt().executeQuery(sql);
			while (rs.next()) {
				output += String.format("VIN: %d, Total Rides: %d \n", rs.getInt("vin"), rs.getInt("totalRides"));
			}

			rs.close();
		} catch (Exception e) {
			System.out.println("cannot execute the query");
			System.out.println(e.getMessage());
//			e.printStackTrace(System.out);
		} finally {
			freeResultSetResources(rs);
		}

		if (output.equals(""))
			output = "No results found";
		return output;
	}

	/**
	 * @param uuLogin1
	 *            String variable representing the first user
	 * @param uuLogin2
	 *            String variable representing the second user
	 * @return output String variable which represents the degree of separation
	 *         between the two
	 */
	public String getSeparationDegree(String uuLogin1, String uuLogin2) {
		String sql = String.format(
				"select f.vin from favorites f where f.login = '%s' and exists "
						+ "(select f1.vin from favorites f1 where f1.login = '%s' and f.vin = f1.vin)",
				uuLogin1, uuLogin2);
		String output = "";
		ResultSet rs = null;
		// System.out.println("executing " + sql);
		try {
			rs = this.getStmt().executeQuery(sql);
			while (rs.next()) {
				output = "1-degree away \n";
				return output;
			}

			rs.close();
		} catch (Exception e) {
			System.out.println("cannot execute the query");
			System.out.println(e.getMessage());
//			e.printStackTrace(System.out);
		} finally {
			freeResultSetResources(rs);
		}
		String sqlSecondDegree = String.format("select f1.login from favorites f, favorites f1 "
				+ "where f.vin=f1.vin and f.login<>f1.login and f.login='%s' and f1.login in "
				+ "(select f1.login from favorites f, favorites f1 "
				+ "where f.vin=f1.vin and f.login<>f1.login and f.login='%s')", uuLogin1, uuLogin2);
		ResultSet rsSecondDegree = null;
		// System.out.println("executing " + sqlSecondDegree);
		try {
			rsSecondDegree = this.getStmt().executeQuery(sqlSecondDegree);
			while (rsSecondDegree.next()) {
				output = "2-degree away \n";
				return output;
			}

			rsSecondDegree.close();
		} catch (Exception e) {
			System.out.println("cannot execute the query");
			System.out.println(e.getMessage());
//			e.printStackTrace(System.out);
		} finally {
			freeResultSetResources(rsSecondDegree);
		}

		return "More than 2 degree of separation \n";
	}

	/**
	 * @return -> returns an arraylist object contaning all the possible categories
	 **/
	public ArrayList<String> getCategories() {
		ArrayList<String> categories = new ArrayList<>();
		String sql = "select distinct category from uc";
		ResultSet rs = null;
		// System.out.println("executing " + sql);
		try {
			rs = this.getStmt().executeQuery(sql);
			while (rs.next()) {
				categories.add(rs.getString("category"));
			}

			rs.close();
		} catch (Exception e) {
			System.out.println("cannot execute the query");
			System.out.println(e.getMessage());
//			e.printStackTrace(System.out);
		} finally {
			freeResultSetResources(rs);
		}

		return categories;
	}

	/**
	 * @param m
	 *            int variable -> m
	 * @return output String variable which represents the most popular rides for
	 *         each category
	 */
	public String getMostPopularUCs(int m) {
		ArrayList<String> categories = getCategories();
		String output = "";
		String sql = "";

		for (String category : categories) {
			sql = String.format(
					"select r.vin, uc.category, count(*) as totalRides from uc, ride r where r.vin=uc.vin and uc.category='%s' group by uc.category, r.vin order by count(*) desc limit %d",
					category, m);
			ResultSet rs = null;
			// System.out.println("executing " + sql);
			try {
				rs = this.getStmt().executeQuery(sql);
				while (rs.next()) {
					output += String.format("VIN: %d, Category:  %s, Total Rides: %d \n", rs.getInt("vin"),
							rs.getString("category"), rs.getInt("totalRides"));
				}

				rs.close();
			} catch (Exception e) {
				System.out.println("cannot execute the query");
				System.out.println(e.getMessage());
//				e.printStackTrace(System.out);
			} finally {
				freeResultSetResources(rs);
			}
		}

		if (output.equals(""))
			output = "No results found";
		return output;
	}

	/**
	 * @param m
	 *            int variable -> m
	 * @return output String variable which represents the most expensive rides for
	 *         each category
	 */
	public String getMostExpensiveUCs(int m) {
		ArrayList<String> categories = getCategories();
		String output = "";
		String sql = "";

		for (String category : categories) {
			sql = String.format(
					"select r.vin, uc.category, avg(cost) as avgCost from uc, ride r where r.vin=uc.vin and uc.category= '%s' group by uc.category, r.vin order by avg(cost) desc limit %d",
					category, m);
			ResultSet rs = null;
			// System.out.println("executing " + sql);
			try {
				rs = this.getStmt().executeQuery(sql);
				while (rs.next()) {
					output += String.format("VIN: %d, Category:  %s, Average Cost: %f \n", rs.getInt("vin"),
							rs.getString("category"), rs.getFloat("avgCost"));
				}

				rs.close();
			} catch (Exception e) {
				System.out.println("cannot execute the query");
				System.out.println(e.getMessage());
//				e.printStackTrace(System.out);
			} finally {
				freeResultSetResources(rs);
			}
		}

		if (output.equals(""))
			output = "No results found";
		return output;
	}

	/**
	 * @param m
	 *            int variable -> m
	 * @return output String variable which represents the best drivers for each
	 *         category
	 */
	public String getBestUDs(int m) {
		ArrayList<String> categories = getCategories();
		String output = "";
		String sql = "";

		for (String category : categories) {
			sql = String.format(
					"select uc.login, uc.category, avg(score) as avgScore from uc, feedback f where uc.vin=f.vin and uc.category='%s' group by uc.login, uc.category order by avg(score) desc limit %d",
					category, m);
			ResultSet rs = null;
			// System.out.println("executing " + sql);
			try {
				rs = this.getStmt().executeQuery(sql);
				while (rs.next()) {
					output += String.format("Login: %s, Category:  %s, Average Score: %f \n", rs.getString("login"),
							rs.getString("category"), rs.getFloat("avgScore"));
				}

				rs.close();
			} catch (Exception e) {
				System.out.println("cannot execute the query");
				System.out.println(e.getMessage());
//				e.printStackTrace(System.out);
			} finally {
				freeResultSetResources(rs);
			}
		}

		if (output.equals(""))
			output = "No results found";
		return output;
	}

}
