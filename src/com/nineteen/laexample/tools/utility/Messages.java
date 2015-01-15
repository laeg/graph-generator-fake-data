package com.nineteen.laexample.tools.utility;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

/**
 * Static Class to obtain String Messages.
 * @author LAEG
 */
public final class Messages {

    /**
     * The name of the Message Resource Bundle.
     */
    private static final String BUNDLE_NAME = "com.nineteen.laexample.tools.messages";

    /**
     * The Message Resource Bundle.
     */
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
            .getBundle(BUNDLE_NAME);

    /**
     * Returns the String message associated with the key.
     * 
     * @param 	key
     *          	the key of the Message to be returned
     * @return 	String
     * 				the Message associated with the key
     */
    public static String getString(final String key) {

        // Obtain the Message from the Key
        try {

            // Return the Message if found
            return RESOURCE_BUNDLE.getString(key);

        } catch (MissingResourceException e) {

            // Log an Exception
            Logger.getLogger("com.nineteen.laexample.tools").error(key + " could not be found");
            return '!' + key + '!';
        }
    }

    /**
     * Private Constructor to prevent instantiation of the Object.
     */
    private Messages() {

        // Call to Super Constructor
        super();
    }
}
