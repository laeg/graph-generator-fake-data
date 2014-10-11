package com.nineteen.laexample.tools.nodes;

import java.util.ArrayList;

/**
 * An Audit Document
 * @author Luke Gannon
 */
	
public class PersonArray extends ArrayList<Person> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8978723331099629673L;
	
	/**
	 * The Person ArrayList
	 */
	private ArrayList<Person> thePeopleArray;
	
	/**
	 * Initialise an Person ArrayList 
	 */
	public PersonArray() {
		
		//Call Super
		super();
		
		//Initialise the Array list
		if (thePeopleArray != null){
			// Clears down the array list
			thePeopleArray.clear();
		}
		
	}

}
