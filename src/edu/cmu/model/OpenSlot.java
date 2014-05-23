/**
 * 
 */
package edu.cmu.model;

import java.util.Date;

/**
 * @author Celine Patag
 *
 */
public class OpenSlot {
	
	private Date start;
	
	private Date end;

	public OpenSlot(Date start, Date end) {		
		this.start = start;
		this.end = end;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
		
}
