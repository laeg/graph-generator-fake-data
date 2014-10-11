package com.nineteen.laexample.tools.graphdb;

import java.util.Map;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;

import com.nineteen.laexample.tools.graphdb.GraphUtil;
import com.nineteen.laexample.tools.logging.ToolLogger;
import com.nineteen.laexample.tools.utility.Configuration;

public class GraphDatabase {

	/**
	 * The Tool Logger.
	 */
	private static Logger theLogger;

	/**
	 * String representation of NEO4J dir path
	 * 
	 */
	private static final String DB_PATH;

	/**
	 * Graph database service
	 * 
	 */
	private static GraphDatabaseService graphDb;

	private static Index<Node> nodeIndex;

	/**
	 * Static Initialisation for Configuration Variables
	 */
	static {
		// Set the graph db path
		DB_PATH = Configuration.getString("neo4j.graph.db.path");
	}

	/**
	 * Instantiate a Graph Database
	 */
	public GraphDatabase() {

		// Instantiate the Logger
		theLogger = ToolLogger.getLogger();
		// GraphDatabase.createDb();
	}

	public static GraphDatabaseService createDb() {
		// theLogger.info(Messages.getString("GraphDatabase.CreatingGraphDb"));
		System.out.println("Creating DB");
		// Instantiates a new database
		GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);

		// New transaction
		Transaction tx = graphDb.beginTx();

		try {

			nodeIndex = graphDb.index().forNodes("nodes");

			// Register a shutdown hook that will make sure the database shuts
			// when
			// the JVM exits
			GraphUtil.registerShutdownHook(graphDb);

			GraphUtil.cleanUp(graphDb, nodeIndex);

			tx.success();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			tx.close();
		}

		return graphDb;
	}

	public static Node createNode(GraphDatabaseService graphDb, String nodeLabel, Map<String, String> nodeAttributes) {
		// Start a transaction
		Transaction tx = graphDb.beginTx();
		Node aNode = null;

		try {
			System.out.println("\n \t \t Creating a node: " + nodeLabel + "\n");

			if (!nodeLabel.isEmpty()) {
				if (!nodeAttributes.isEmpty()) {
					// create the node
					aNode = graphDb.createNode();

					// add the node label
					aNode.addLabel(DynamicLabel.label(nodeLabel));

					//
					for (Map.Entry<String, String> attribute : nodeAttributes.entrySet()) {
						aNode.setProperty(attribute.getKey(), attribute.getValue());
						
					}

					for (Map.Entry<String, String> attribute : nodeAttributes.entrySet()) {
						nodeIndex.add(aNode, attribute.getKey(), attribute.getValue());
						System.out.println("\t \t \t added to index :- " + attribute.getKey() + " = " + attribute.getValue());
					}

					tx.success();
					
				} else {
					System.out.println("Node attributes were empty");
				}
			} else {
				System.out.println("Node attributes label was empty");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			tx.close();
		}

		return aNode;

	}

	public static void createRelationship(GraphDatabaseService graphDb, String aAttributeName, String aValue, RelTypes relType,String bAttributeName, String bValue) {
		// Start a transaction
		Transaction tx = graphDb.beginTx();

		try {
						// Return the nodes that match the searched 
			Node a = nodeIndex.get(aAttributeName, aValue).getSingle();
			Node b = nodeIndex.get(bAttributeName, bValue).getSingle();

			// lock objects
			tx.acquireWriteLock(a);
			tx.acquireWriteLock(b);
			
			Boolean created = false;
			
			// check if the relationship is already created 
			for(Relationship rel : a.getRelationships(relType)){
				if (rel.getOtherNode(a).equals(b)){
					created = true;
					break;
				}
			}
			
			// Create the relationship if it's not already
			if (!created){
				a.createRelationshipTo(b, relType);
				System.out.println();
			}
			
			tx.success();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			tx.close();
		}

	}

	public static void shutdownGraphDatabase(GraphDatabaseService graphDb) {
		graphDb.shutdown();
	}

}
