package com.nineteen.laexample.tools.startGraph;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.GraphDatabaseService;

import com.nineteen.laexample.tools.graphdb.GraphDatabase;
import com.nineteen.laexample.tools.graphdb.RelTypes;
import com.nineteen.laexample.tools.logging.ToolLogger;
import com.nineteen.laexample.tools.nodes.Flight;
import com.nineteen.laexample.tools.nodes.Person;
import com.nineteen.laexample.tools.utility.Configuration;

public class StartGraphBuild {

	/**
	 * The Migration Logger.
	 */
	private final Logger theLogger;

	// private static GraphDatabase theGraphDatabase;

	private final static String DB_PATH;

	private final static String CSV_PATH;

	private final static String CSV_SPLITTER;

	private final static String JSON_PATH;

	
	// private static FileDirectory theFileDirectory;

	/**
	 * Static Initialisation for Configuration Variables
	 */
	static {
		DB_PATH = Configuration.getString("neo4j.graph.db.path");
		CSV_PATH = Configuration.getString("csv.data.input");
		CSV_SPLITTER = Configuration.getString("csv.data.splitter");
		JSON_PATH = Configuration.getString("json.data.input");
	}

	/**
	 * Instantiate a Start Graph Build
	 */
	public StartGraphBuild() {

		// Instantiate the Logger
		theLogger = ToolLogger.getLogger();

	}

	public static void main(String[] args) {

		// Create a database
		GraphDatabaseService graphDb = GraphDatabase.createDb();

		// Input scanner
		Scanner input = new Scanner(System.in);

		for (String carrierName : Flight.carrierNameOptions) {
			GraphDatabase.createNode(graphDb, "Carrier", createAMapForNoAttributes("airlineName", carrierName));
		}

		for (String airportName : Flight.airportNameOptions) {
			GraphDatabase.createNode(graphDb, "Airports", createAMapForNoAttributes("airportName", airportName));
		}

		Map<Integer, Flight> listOfFlights = new HashMap<Integer, Flight>();

		System.out.println("Please enter the number of flights you would like to generate? : ");

		// NEEDS ERROR HANDLING 
		Integer numOfFlights = Integer.parseInt(input.nextLine().replaceAll("[^0-9.]", ""));

		System.out.println("Please enter the number of people on each flight you would like to generate? : ");

		// NEEDS ERROR HANDLING 
		Integer numOfPeople = Integer.parseInt(input.nextLine().replaceAll("[^0-9.]", ""));

		listOfFlights.putAll(Flight.generateFlights(numOfFlights, numOfPeople));

		// Get each flight from the generated flights
		for (Entry<Integer, Flight> fAttribute : listOfFlights.entrySet()) {
			Flight flight = fAttribute.getValue();
			GraphDatabase.createNode(graphDb, "Flight", getDetails(flight));

			// Create a relationship to the airline
			GraphDatabase.createRelationship(graphDb, "airlineName", flight.getCarrierName(), RelTypes.HAS_FLIGHT, "carrierName",
					flight.getCarrierName());

			// Create a relationship to the airports
			GraphDatabase.createRelationship(graphDb, "airportName", flight.getDeparture(), RelTypes.FROM_AIRPORT, "flightNumber",
					Integer.toString(flight.getFlightNumber()));
			GraphDatabase.createRelationship(graphDb, "flightNumber", Integer.toString(flight.getFlightNumber()), RelTypes.TO_AIRPORT, "airportName",
					flight.getArrival());

			// Get each list of users from generated users
			for (Entry<Integer, Person> attribute : flight.getPeopleOnPlane().entrySet()) {
				Person p = attribute.getValue();

				GraphDatabase.createNode(graphDb, "People", getDetails(p));
				GraphDatabase.createRelationship(graphDb, "passportNumber", Integer.toString(p.getIdentifier()), RelTypes.ON_FLIGHT, "flightNumber",
						Integer.toString(flight.getFlightNumber()));

			}
		}
		
		// Shutdown database
		GraphDatabase.shutdownGraphDatabase(graphDb);

		System.out.println("\nFinished creating and querying your graph \n \n---- Created by LAExample ----");

	}

	private static Map<String, String> getDetails(Flight flight) {
		Map<String, String> flightAttr = new HashMap<String, String>();
		flightAttr.put("flightNumber", Integer.toString(flight.getFlightNumber()));
		flightAttr.put("carrierName", flight.getCarrierName());
		flightAttr.put("departureAirport", flight.getDeparture());
		flightAttr.put("arrivalAirport", flight.getArrival());
		flightAttr.put("departureDateTime", Integer.toString(flight.getDepartureDateTime()));
		flightAttr.put("arrivalDateTime", Integer.toString(flight.getArrivalDateTime()));

		return flightAttr;

	}

	private static Map<String, String> getDetails(Person person) {
		Map<String, String> personAttr = new HashMap<String, String>();
		personAttr.put("passportNumber", Integer.toString(person.getIdentifier()));
		personAttr.put("firstName", person.getFirstName());
		personAttr.put("lastName", person.getLastName());
		personAttr.put("gender", person.getGender());
		personAttr.put("dateOfBirth", person.getDateOfBirth().toString());
		personAttr.put("criminalRecord", person.getCriminalRecord());

		return personAttr;

	}

	private static Map<String, String> createAMapForNoAttributes(String key, String value) {
		Map<String, String> aMap = new HashMap<String, String>();
		aMap.put(key, value);
		return aMap;
	}
	
	

}
