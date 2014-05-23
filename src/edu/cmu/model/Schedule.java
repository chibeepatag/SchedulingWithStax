/**
 * 
 */
package edu.cmu.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Celine Patag
 *
 */
public class Schedule {

	private Map<String, List<OpenSlot>> dayOpenSlotMap;
	
	public Schedule() {
		dayOpenSlotMap = new HashMap<String, List<OpenSlot>>();
		dayOpenSlotMap.put("Monday", new ArrayList<OpenSlot>());
		dayOpenSlotMap.put("Tuesday",   new ArrayList<OpenSlot>());
		dayOpenSlotMap.put("Wednesday",   new ArrayList<OpenSlot>());
		dayOpenSlotMap.put("Thursday",  new ArrayList<OpenSlot>());
		dayOpenSlotMap.put("Friday",   new ArrayList<OpenSlot>());
		dayOpenSlotMap.put("Saturday",   new ArrayList<OpenSlot>());
		dayOpenSlotMap.put("Sunday",  new ArrayList<OpenSlot>());
	}
	
	
	public Map<String, List<OpenSlot>> getDayOpenSlotMap() {
		return dayOpenSlotMap;
	}

	public List<OpenSlot> getAvailable(String day) throws Exception{
		List<OpenSlot> openSlots = dayOpenSlotMap.get(day);
		return openSlots;
	}
}
