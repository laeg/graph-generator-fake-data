package com.nineteen.laexample.tools.graphdb;

import org.neo4j.graphdb.RelationshipType;

public enum RelTypes implements RelationshipType{
	HAS_FLIGHT, ON_FLIGHT, FROM_AIRPORT, TO_AIRPORT, IS_THE_SAME_PERSON
}
