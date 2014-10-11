package com.nineteen.laexample.tools.nodes;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.math.RandomUtils;

public class Flight {

	/**
	 * The flight carrier name
	 */
	private String carrierName;

	/**
	 * The flight number
	 */
	private Integer flightNumber;

	/**
	 * The flight departure destination
	 */
	private String departure;

	/**
	 * The flight arrival destination
	 */
	private String arrival;

	/**
	 * The flight departure date/time from EPOCH time (01-01-1970)
	 */
	private Integer departureDateTime;

	/**
	 * The flight arrival date/time from EPOCH time (01-01-1970)
	 */
	private Integer arrivalDateTime;

	/**
	 * A collection of people on the flight
	 */
	private Map<Integer, Person> peopleOnPlane;

	/**
	 * Array of random carrier names
	 */
	public static final String[] carrierNameOptions = { "British Airways", "Virgin Atlantic", "American Airlines", "Southwest Airlines",
			"Delta Air Lines" };

	/**
	 * Array of random airport names
	 */
	public static final String[] airportNameOptions = { "London Heathrow", "Washington Dulles", "San Francisco International Airport",
			"London Gatwick", "Sangster International Airport", "Marco Polo Airport", "Mehrabad International Airport",
			"Albuquerque International Sunport", "Meenambakam Airport", "Meenambakam Airport", "Chhatrapati Shivaji International Airport",
			"Netaji Subhas Chandra Bose International", "F'aaa International Airport", "Charles De Gaulle International Airport",
			"Don Muang International Airport" };

	/**
	 * @return the carrierName
	 */
	public String getCarrierName() {
		return carrierName;
	}

	/**
	 * @param carrierName
	 *            the carrierName to set
	 */
	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}

	/**
	 * @return the flightNumber
	 */
	public Integer getFlightNumber() {
		return flightNumber;
	}

	/**
	 * @param flightNumber
	 *            the flightNumber to set
	 */
	public void setFlightNumber(Integer flightNumber) {
		this.flightNumber = flightNumber;
	}

	/**
	 * @return the departure
	 */
	public String getDeparture() {
		return departure;
	}

	/**
	 * @param departure
	 *            the departure to set
	 */
	public void setDeparture(String departure) {
		this.departure = departure;
	}

	/**
	 * @return the arrival
	 */
	public String getArrival() {
		return arrival;
	}

	/**
	 * @param arrival
	 *            the arrival to set
	 */
	public void setArrival(String arrival) {
		this.arrival = arrival;
	}

	/**
	 * @return the departureDateTime
	 */
	public Integer getDepartureDateTime() {
		return departureDateTime;
	}

	/**
	 * @param departureDateTime
	 *            the departureDateTime to set
	 */
	public void setDepartureDateTime(Integer departureDateTime) {
		this.departureDateTime = departureDateTime;
	}

	/**
	 * @return the arrivalDateTime
	 */
	public Integer getArrivalDateTime() {
		return arrivalDateTime;
	}

	/**
	 * @param arrivalDateTime
	 *            the arrivalDateTime to set
	 */
	public void setArrivalDateTime(Integer arrivalDateTime) {
		this.arrivalDateTime = arrivalDateTime;
	}

	/**
	 * @return the peopleOnPlane
	 */
	public Map<Integer, Person> getPeopleOnPlane() {
		return peopleOnPlane;
	}

	/**
	 * @param peopleArrayList
	 *            the peopleOnPlane to set
	 */
	public void setPeopleOnPlane(Map<Integer, Person> peopleArrayList) {
		this.peopleOnPlane = peopleArrayList;
	}

	/**
	 * 
	 * @param numberOfFlights, peopleOnBoardEachFlight
	 * @return
	 */
	public static Map<Integer, Flight> generateFlights(Integer numberOfFlights, Integer peopleOnBoardEachFlight) {
		Map<Integer, Flight> generatedFlights = new HashMap<Integer, Flight>();
		Flight f;
		int i;
		for (i = 0; i < numberOfFlights - 1; i++) {
			f = createARandomFlight(peopleOnBoardEachFlight);
			generatedFlights.put(f.getFlightNumber(), f);
		}

		return generatedFlights;
	}
	/**
	 * Creates a random person
	 * 
	 * @return Person
	 */
	public static Flight createARandomFlight(Integer peopleOnBoard) {
		// Create a new flight object
		Flight newFlight = new Flight();

		newFlight.setFlightNumber((int) Math.abs(System.currentTimeMillis() - RandomUtils.nextLong()));
		newFlight.setCarrierName(carrierNameOptions[(int) (Math.random() * carrierNameOptions.length)]);
		newFlight.setDeparture(airportNameOptions[(int) (Math.random() * airportNameOptions.length)]);
		newFlight.setArrival(airportNameOptions[(int) (Math.random() * airportNameOptions.length)]);
		newFlight.setDepartureDateTime((int) Math.abs(System.currentTimeMillis() - RandomUtils.nextLong()));
		newFlight.setArrivalDateTime((int) Math.abs(System.currentTimeMillis() - RandomUtils.nextLong()));
		newFlight.setPeopleOnPlane(generatePassengers(peopleOnBoard));

		System.out.println("New flight created");
		System.out.println("\n \t" + newFlight.getCarrierName());
		System.out.println("\t" + newFlight.getArrival());
		System.out.println("\t" + newFlight.getDeparture());

		System.out.println("\n \t \t Users on flight \n");
		// For each users in the flight map
		for (Entry<Integer, Person> attribute : newFlight.getPeopleOnPlane().entrySet()) {
			Person p = attribute.getValue();
			System.out.println("\t \t \t " + attribute.getKey() + " | " + p.getFirstName() + " " + p.getLastName());
		}
		
		return newFlight;

	}

	/**
	 * 
	 * @param userAmount
	 * @return
	 */
	public static Map<Integer, Person> generatePassengers(Integer userAmount) {
		Map<Integer, Person> generatedUsers = new HashMap<Integer, Person>();
		Person p;
		int i;
		for (i = 0; i < userAmount - 1; i++) {
			p = createARandomPerson();
			generatedUsers.put(p.getIdentifier(), p);
		}

		return generatedUsers;
	}

	/**
	 * Creates a random person
	 * 
	 * @return Person
	 */
	public static Person createARandomPerson() {
		// Create a new person object
		Person newPerson = new Person();

		// Random name pickers
		int firstNameRandomSelector = (int) (Math.random() * Person.firstNameOptions.length);
		int lastNameRandomSelector = (int) (Math.random() * Person.lastNameOptions.length);
		newPerson.setIdentifier((int) Math.abs(System.currentTimeMillis() - RandomUtils.nextLong()));
		newPerson.setFirstName(Person.firstNameOptions[firstNameRandomSelector]);
		newPerson.setLastName(Person.lastNameOptions[lastNameRandomSelector]);
		newPerson.setDateOfBirth(new Date(Math.abs(System.currentTimeMillis() - RandomUtils.nextLong())));
		newPerson.setCriminalRecord("0");

		return newPerson;

	}

}
