/**
 * 
 */
package edu.cmu.util;

/**
 * This exception is thrown if there are no open slots
 * available for the given day.
 * @author Celine Patag
 *
 */
public class NoOpenSlotException extends Exception {

	public NoOpenSlotException(String day) {
		super("There is no open slot for this day: " + day);
	}
}
