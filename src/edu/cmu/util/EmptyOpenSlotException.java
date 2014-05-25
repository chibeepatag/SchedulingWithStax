/**
 * 
 */
package edu.cmu.util;

/**
 * This exception is thrown if the open slot list is empty
 * after removing all open slots 
 * shorter than the required duration.
 * @author Celine Patag
 *
 */
public class EmptyOpenSlotException extends Exception {

	public EmptyOpenSlotException() {
		super("There are no open slots left.");
	}
}
