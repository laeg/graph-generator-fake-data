package com.nineteen.laexample.tools.logging;

import org.apache.log4j.Logger;

import com.nineteen.laexample.tools.utility.Configuration;

/**
 * Logs Events.
 * @author LAEG
 */
public final class ToolLogger {

    /**
     * The String Representation of the Logger Name.
     */
    public static final String LOGGER_NAME;

    /**
     * Static Initialisation for Configuration Variables
     */
    static {

        // Initialise the Logging Configuration
        LOGGER_NAME = Configuration.getString("FileGraph.Logger");
    }

    /**
     * Returns the Migration Logger.
     * 
     * @return Logger the Migration Logger.
     */
    public static Logger getLogger() {

        // Return the Logger
        return Logger.getLogger(LOGGER_NAME);
    }

    /**
     * Private Constructor to prevent instantiation.
     */
    private ToolLogger() {
    	
    }
}
