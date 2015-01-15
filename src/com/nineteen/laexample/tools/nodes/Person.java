package com.nineteen.laexample.tools.nodes;

import java.util.Date;

public class Person {

	/**
	 * The persons identifier
	 */
	private Integer identifier;

	/**
	 * The persons first name
	 */
	private String firstName;

	/**
	 * The persons first name
	 */
	private String lastName;
	
	/**
	 * The persons sex
	 */
	private String gender;
	
	/**
	 * The persons dob from EPOCH time (01-01-1970)
	 */
	private Date dateOfBirth;

	/**
	 * The persons record status
	 */
	private String criminalRecord;

	/**
	 * Array of random first names
	 */
	public static final String[] firstNameOptions = { "Abigail", "Bradley", "Cameron", "Diana", "Elliott", "Frank", "Lewis", "Mark", "Conrado",
			"Yvonne", "Lisa", "Melissa", "Reu", "Tim", "Matthew", "Jeremy", "Xavier", "Erica", "Eddie", "Umang", "Chris", "Joe", "Lauren",
			"Laurence", "Dimitar", "Ajay", "Evan", "Eve", "Tracey", "Craig", "Claude", "Lynn", "Duncan", "Chris", "Rebecca", "Ashley", "Harry",
			"John", "Georgie" };

	/**
	 * Array of random last names
	 */
	public static final String[] lastNameOptions = { "Boakye", "Newland", "Gannon", "Shuffle", "Crawford", "Smith", "Park", "Gil", "Poole",
			"Taylor", "Richards", "Lewis", "Samuels", "Slater", "Hoppus", "Hall", "Wilson", "Goldman", "Corns", "Bell", "Brown", "Russell", "Bird",
			"Plant", "Harrison", "Gillies", "Gerber", "Barrick", "Padmanabhan", "Sadinov", "Baldwin", "Saxby", "Shaw", "Desai", "Pell", "Timbers",
			"Doey" };
	/**
	 * Array of genders
	 */
	public static final String[] genderOptions = {"male","female"};

	/**
	 * Person constructor
	 * @param identifier
	 * @param firstName
	 * @param lastName
	 * @param gender
	 * @param dateOfBirth
	 * @param criminalRecord
	 */
	public Person (Integer identifier, String firstName, String lastName, String gender, Date dateOfBirth, String criminalRecord) {
		this.identifier = identifier;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.criminalRecord = criminalRecord;
	}
	
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String fName) {
		this.firstName = fName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lName) {
		this.lastName = lName;
	}

	/**
	 * @return the dateOfBirth
	 */
	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	/**
	 * @param dateOfBirth
	 *            the dateOfBirth to set
	 */
	public void setDateOfBirth(Date dOBirth) {
		this.dateOfBirth = dOBirth;
	}

	/**
	 * @return the criminalRecord
	 */
	public String getCriminalRecord() {
		return criminalRecord;
	}

	/**
	 * @param i
	 *            the criminalRecord to set
	 */
	public void setCriminalRecord(String i) {
		this.criminalRecord = i;
	}

	/**
	 * @return the identifier
	 */
	public Integer getIdentifier() {
		return identifier;
	}

	/**
	 * @param identifier
	 *            the identifier to set
	 */
	public void setIdentifier(Integer identifier) {
		this.identifier = identifier;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	
	
}
