/**
 * 
 */
package edu.cmu.util;

/**
 * This exception is thrown when no
 * schedule tag is found in the given xml.
 * @author Celine Patag
 * 
 */
public class ScheduleXMLException extends Exception {
	
	public ScheduleXMLException() {
		super("No schedule found.");
	}
}
