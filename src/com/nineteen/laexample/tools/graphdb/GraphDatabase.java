package com.nineteen.laexample.tools.graphdb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
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

			// create an index of all nodes
			nodeIndex = graphDb.index().forNodes("nodes");

			// Register a shutdown hook that will make sure the database shuts
			// when the JVM exits
			GraphUtil.registerShutdownHook(graphDb);

			// Clean up the graph
			GraphUtil.cleanUp(graphDb, nodeIndex);

			tx.success();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			tx.close();
		}

		return graphDb;
	}

	public static GraphDatabaseService logIntoDb(String databasePath) {
		// theLogger.info(Messages.getString("GraphDatabase.CreatingGraphDb"));
		System.out.println("Logging into " + databasePath);
		// Instantiates a new database
		GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(databasePath);

		// New transaction
		Transaction tx = graphDb.beginTx();

		try {

			// create an index of all nodes
			nodeIndex = graphDb.index().forNodes("nodes");

			// Register a shutdown hook that will make sure the database shuts
			// when the JVM exits
			GraphUtil.registerShutdownHook(graphDb);

			tx.success();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			tx.close();
		}

		return graphDb;
	}

	/**
	 * Create a node with attributes
	 * 
	 * @param graphDb
	 * @param nodeLabel
	 * @param nodeAttributes
	 * @return
	 */
	public static Node createNode(GraphDatabaseService graphDb, String nodeLabel, Map<String, String> nodeAttributes) {
		// Start a transaction
		Transaction tx = graphDb.beginTx();
		Node aNode = null;

		try {
			System.out.println("\n \t \t \t  Creating a node: " + nodeLabel);

			if (!nodeLabel.isEmpty()) {
				if (!nodeAttributes.isEmpty()) {
											
						// create the node
						aNode = graphDb.createNode();
	
						// Get each attribute and set node properties == to
						// attributes
						for (Map.Entry<String, String> attribute : nodeAttributes.entrySet()) {
							aNode.setProperty(attribute.getKey(), attribute.getValue());
						}
	
						// add the node label
						if (nodeLabel.equalsIgnoreCase ( "People" ) ){
							String criminal = nodeAttributes.get("criminalRecord");
							if (!criminal.contentEquals(Integer.toString(1))){
								aNode.addLabel(DynamicLabel.label(nodeLabel));
							} else {
								aNode.addLabel(DynamicLabel.label("BadPerson"));
							}
						} else {
							aNode.addLabel(DynamicLabel.label(nodeLabel));
						}
							
						
						
						// Get each attribute and add to the index
						for (Map.Entry<String, String> attribute : nodeAttributes.entrySet()) {
							nodeIndex.add(aNode, attribute.getKey(), attribute.getValue());
							System.out.println("\t \t \t \t added to index :- " + attribute.getKey() + " = " + attribute.getValue());
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

	/**
	 * Create a relationship with just a relationship type
	 * 
	 * @param graphDb
	 * @param aAttributeName
	 * @param aValue
	 * @param relType
	 * @param bAttributeName
	 * @param bValue
	 */
	public static void createRelationship(GraphDatabaseService graphDb, String aAttributeName, String aValue, RelTypes relType,
			String bAttributeName, String bValue) {
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
			for (Relationship rel : a.getRelationships(relType)) {
				if (rel.getOtherNode(a).equals(b)) {
					created = true;
					break;
				}
			}

			// Create the relationship if it's not already
			if (!created) {
				a.createRelationshipTo(b, relType);
				System.out.println("\t \t \t \t \t \t relation created between :- " + aAttributeName + " |  " + aValue + " - " + relType + " - "
						+ bAttributeName + " |  " + bValue);
			}

			tx.success();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			tx.close();
		}

	}

	/**
	 * Create a relationship with attributes
	 * 
	 * @param graphDb
	 * @param aAttributeName
	 * @param aValue
	 * @param relType
	 * @param relationshipAttributes
	 * @param bAttributeName
	 * @param bValue
	 */
	public static void createRelationship(GraphDatabaseService graphDb, String aAttributeName, String aValue, RelTypes relType,
			Map<String, String> relationshipAttributes, String bAttributeName, String bValue) {
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
			for (Relationship rel : a.getRelationships(relType)) {
				if (rel.getOtherNode(a).equals(b)) {
					created = true;
					break;
				}
			}

			// Create the relationship if it's not already
			if (!created) {
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

	/**
	 */
	public static void createPossibleRelationship(GraphDatabaseService graphDb, String aAttributeName, String aValue, String bAttributeName,
			String bValue) {
		// Start a transaction
		Transaction tx = graphDb.beginTx();

		// the starting person
		Node thePerson = nodeIndex.get(aAttributeName, aValue).getSingle();

		// Store to keep returned results of possible people
		Map<Integer, Node> possHold = new HashMap<Integer, Node>();
		try {
			// Return the nodes that match the searched
			for (Node aPerson : nodeIndex.get(aAttributeName, aValue)) {
				System.out.println(aPerson.getLabels() + " " + aPerson.getProperty("firstName") + " " + aPerson.getProperty("lastName"));

			}
			// Node a = nodeIndex.get(aAttributeName, aValue).getSingle();
			// Node b = nodeIndex.get(bAttributeName, bValue).getSingle();

			// lock objects
			// tx.acquireWriteLock(a);
			// tx.acquireWriteLock(b);

			// Boolean created = false;

			// check if the relationship is already created
			// for (Relationship rel : a.getRelationships(relType)) {
			// if (rel.getOtherNode(a).equals(b)) {
			// created = true;
			// break;
			// }
			// }

			// Create the relationship if it's not already
			// if (!created) {
			// a.createRelationshipTo(b, relType);
			// System.out.println();
			// }

			tx.success();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			tx.close();
		}

	}

	/**
	 * Shutdown the graph database
	 * 
	 * @param graphDb
	 */
	public static void shutdownGraphDatabase(GraphDatabaseService graphDb) {
		graphDb.shutdown();
	}

	public static void selectStarFromNodesWhere(GraphDatabaseService graphDb, String nodeType, String nodeAttribute, String nodeRequiredValue) {

		// Start a transaction
		Transaction tx = graphDb.beginTx();

		// Create a label from the node Type requested
		Label label = DynamicLabel.label(nodeType);

		try {
			ResourceIterator<Node> findables = graphDb.findNodesByLabelAndProperty(label, nodeAttribute, nodeRequiredValue).iterator();
			ArrayList<Node> foundNodes = new ArrayList<>();
			while (findables.hasNext()) {
				foundNodes.add(findables.next());
			}

			System.out.println("\tLabel - " + nodeType + "  | Attribute - " + nodeAttribute + " | Value - " + nodeRequiredValue
					+ "\nResults for query:");
			System.out
					.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

			StringBuilder columnRowBuilder = new StringBuilder();
			String columnRow;
			StringBuilder resultRowBuilder = new StringBuilder();
			String resultRow;
			StringBuilder relationshipBuilder = new StringBuilder();
			String relationshipsString;

			// Loop through the graph finding nodes that match
			if (!foundNodes.isEmpty()) {
				for (Node node : foundNodes) {

					columnRowBuilder.setLength(0);

					// Create the result wall
					columnRowBuilder.append(" |  ID |");

					// Get each of the nodes properties
					// Add the properties to create the column rows
					Iterable<String> propertyKeys = node.getPropertyKeys();
					Integer keyCount = 0;
					ArrayList<String> nodeProperties = new ArrayList<String>();
					for (String prop : propertyKeys) {
						columnRowBuilder.append(prop + " | ");
						nodeProperties.add(prop);
					}

					columnRowBuilder.append(" RELATIONSHIPS  |");

					// Set and print the column row
					columnRow = columnRowBuilder.toString();

					System.out.println(columnRow);

					// Reset result row
					resultRowBuilder.setLength(0);

					resultRowBuilder.append(" | " + node.getId() + " |  ");

					for (String nProp : nodeProperties) {
						resultRowBuilder.append(node.getProperty(nProp) + " | ");
					}

					// resultRowBuilder.append(" | \t" + node.getId() + " | \t"
					// + node.getProperty(nodeAttribute) + " | \t");

					// Get a count of relationships
					Iterable<Relationship> relationships = node.getRelationships();
					Integer relCount;
					Map<String, Integer> relsOfType = new HashMap<String, Integer>();

					for (Relationship rel : relationships) {

						if (relsOfType.containsKey(rel.getType().name())) {
							relsOfType.put(rel.getType().name(), relsOfType.get(rel.getType().name()) + 1);
						} else {
							relsOfType.put(rel.getType().name(), 1);
						}

					}

					for (Map.Entry<String, Integer> nodeRelAttributes : relsOfType.entrySet()) {
						if (relsOfType.entrySet().size() > 1) {
							resultRowBuilder.append(nodeRelAttributes.getKey().toString() + ",");
						} else {
							resultRowBuilder.append(nodeRelAttributes.getKey().toString());
						}

						// System.out.println(" | \t" + node.getId() + " | \t" +
						// node.getProperty(nodeAttribute) + " | \t"
						// + nodeRelAttributes.getKey().toString() + " | \t" +
						// nodeRelAttributes.getValue().toString() + " \t " +
						// "|");
					}

					// Set and print the column row
					resultRow = resultRowBuilder.toString();
					System.out.println(resultRow);

					System.out
							.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

				}
			} else {
				System.out.println(" |                            						No nodes found										  | ");
				System.out
						.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

			}

			tx.success();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			tx.close();
		}
	}

	public static void findNodes(GraphDatabaseService graphDb, String nodeType, String nodeAttribute, String nodeRequiredValue) {

		// Start a transaction
		Transaction tx = graphDb.beginTx();

		// Create a label from the node Type requested
		Label label = DynamicLabel.label(nodeType);

		try {
			ResourceIterator<Node> findables = graphDb.findNodesByLabelAndProperty(label, nodeAttribute, nodeRequiredValue).iterator();
			ArrayList<Node> foundNodes = new ArrayList<>();
			while (findables.hasNext()) {
				foundNodes.add(findables.next());
			}

			System.out.println("\tLabel - " + nodeType + " \t | Attribute - " + nodeAttribute + " \t| Value - " + nodeRequiredValue
					+ "\nResults for query:");
			System.out.println("-------------------------------------------------------------------------------------------");

			// Loop through the graph finding nodes that match
			if (!foundNodes.isEmpty()) {
				for (Node node : foundNodes) {

					System.out.println(" | \t" + node.getId() + " | \t" + node.getProperty(nodeAttribute) + " \t|");
				}
			} else {
				System.out.println(" |                            No nodes found                                             | ");
			}

			System.out.println("-------------------------------------------------------------------------------------------");

			tx.success();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			tx.close();
		}
	}
}
