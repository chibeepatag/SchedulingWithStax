/**
 * 
 */
package edu.cmu.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.cmu.util.NoOpenSlotException;

/**
 * @author Celine Patag
 *
 */
public class Schedule {

	public String name;
	
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

	public List<OpenSlot> getAvailable(String day) throws NoOpenSlotException{
		List<OpenSlot> openSlots = dayOpenSlotMap.get(day);
		if(openSlots.isEmpty()){
			throw new NoOpenSlotException(day);
		}
		return openSlots;
	}
	
	public void printSchedule() {
		System.out.println("Monday: ");
		for (OpenSlot openslot : dayOpenSlotMap.get("Monday")) {
			System.out.println(openslot.toString());
		}		
		
		System.out.println("Tuesday: ");
		for (OpenSlot openslot : dayOpenSlotMap.get("Tuesday")) {
			System.out.println(openslot.toString());
		}
		
		System.out.println("Wedesday: ");
		for (OpenSlot openslot : dayOpenSlotMap.get("Wednesday")) {
			System.out.println(openslot.toString());
		}
		
		System.out.println("Thursday: ");
		for (OpenSlot openslot : dayOpenSlotMap.get("Thursday")) {
			System.out.println(openslot.toString());
		}
		
		System.out.println("Friday: ");
		for (OpenSlot openslot : dayOpenSlotMap.get("Friday")) {
			System.out.println(openslot.toString());
		}
		
		System.out.println("Saturday: ");
		for (OpenSlot openslot : dayOpenSlotMap.get("Saturday")) {
			System.out.println(openslot.toString());
		}
		
		System.out.println("Sunday: ");
		for (OpenSlot openslot : dayOpenSlotMap.get("Sunday")) {
			System.out.println(openslot.toString());
		}
		
	}
}
