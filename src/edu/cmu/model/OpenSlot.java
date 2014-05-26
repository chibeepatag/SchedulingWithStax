/**
 * 
 */
package edu.cmu.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
	
	@Override
	public String toString() {		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		
		
		
		Calendar startC = Calendar.getInstance();
				 startC.setTime(this.start);
		Calendar endC = Calendar.getInstance();
				 endC.setTime(this.end);
		StringBuffer buffer = new StringBuffer();
		buffer.append("Start: ");
		buffer.append(sdf.format(start));
		//buffer.append(startC.get(Calendar.HOUR));
		//buffer.append(":");
		//buffer.append(startC.get(Calendar.MINUTE));
		buffer.append(" End: ");
		buffer.append(sdf.format(end));
		//buffer.append(endC.get(Calendar.HOUR));
		//buffer.append(":");
		//buffer.append(endC.get(Calendar.MINUTE));
		return buffer.toString();
	}
		
}
