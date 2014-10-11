package com.nineteen.laexample.tools.startGraph;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
		// theGraphDatabase = new GraphDatabase();
		// theGraphManager = new GraphManager();
		// theFileDirectory = new FileDirectory();

	}

	public static void main(String[] args) {

		// Create a database
		GraphDatabaseService graphDb = GraphDatabase.createDb();

		//Flight flight = createATestFlight(Integer.parseInt(args[0]));
		for (String carrierName : Flight.carrierNameOptions){
			GraphDatabase.createNode(graphDb, "Carrier", createAMapForCarriers("airlineName", carrierName));
		}
		
		Map<Integer, Flight> listOfFlights = new HashMap<Integer, Flight>();
		
		listOfFlights.putAll(Flight.generateFlights(Integer.parseInt(args[0]), Integer.parseInt(args[1])));
		
		// Get each flight from the generated flights
		for (Entry<Integer, Flight> fAttribute : listOfFlights.entrySet()){
			Flight flight = fAttribute.getValue();
			GraphDatabase.createNode(graphDb, "Flight", getDetails(flight));
			
			// Create a relationship to the airline
			GraphDatabase.createRelationship(graphDb, "airlineName", flight.getCarrierName(), RelTypes.HAS_FLIGHT, "carrierName", flight.getCarrierName());
			
			// Get each list of users from generated users
			for (Entry<Integer, Person> attribute : flight.getPeopleOnPlane().entrySet()) {
				Person p = attribute.getValue();
				
				GraphDatabase.createNode(graphDb, "People", getDetails(p));
				//System.out.println("passportNumber" + Integer.toString(p.getIdentifier()) + RelTypes.ON_FLIGHT + "flightNumber" + Integer.toString(flight.getFlightNumber()));
				GraphDatabase.createRelationship(graphDb, "passportNumber", Integer.toString(p.getIdentifier()), RelTypes.ON_FLIGHT, "flightNumber", Integer.toString(flight.getFlightNumber()));
			}
		}
		
		//for (Entry<Integer, Person> attribute : flight.getPeopleOnPlane().entrySet()) {
		//	Person p = attribute.getValue();
			
		//	GraphDatabase.createNode(graphDb, "People", getDetails(p));
			//System.out.println("passportNumber" + Integer.toString(p.getIdentifier()) + RelTypes.ON_FLIGHT + "flightNumber" + Integer.toString(flight.getFlightNumber()));
		//	GraphDatabase.createRelationship(graphDb, "passportNumber", Integer.toString(p.getIdentifier()), RelTypes.ON_FLIGHT, "flightNumber", Integer.toString(flight.getFlightNumber()));
		//}

		System.out.println("Finished");
		
		// Shutdown database
		GraphDatabase.shutdownGraphDatabase(graphDb);

	}

	/**private static Flight createATestFlight(Integer peopleOnBoard) {
		Flight newFlight = new Flight();
		newFlight.setFlightNumber(123456789);
		newFlight.setCarrierName("British Airways");
		newFlight.setDeparture("Washington Dulles");
		newFlight.setArrival("London Heathrow");
		newFlight.setDepartureDateTime((int) Math.abs(System.currentTimeMillis() - RandomUtils.nextLong()));
		newFlight.setArrivalDateTime((int) Math.abs(System.currentTimeMillis() - RandomUtils.nextLong()));
		newFlight.setPeopleOnPlane(newFlight.generatePassengers(peopleOnBoard));

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

	} */

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
		personAttr.put("dateOfBirth", person.getDateOfBirth().toString());
		personAttr.put("criminalRecord", person.getCriminalRecord());

		return personAttr;

	}
	
	private static Map<String, String> createAMapForCarriers(String key, String value){
		Map<String, String> aMap = new HashMap<String, String>();
		aMap.put(key, value);
		return aMap;
	}
	
	
}
