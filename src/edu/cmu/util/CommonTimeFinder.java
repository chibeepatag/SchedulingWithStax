/**
 * 
 */
package edu.cmu.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import edu.cmu.model.OpenSlot;
import edu.cmu.model.Schedule;

/**
 * @author Celine Patag
 * 
 */
public class CommonTimeFinder {

	private List<String> daysOfTheWeek;

	private int duration;

	public CommonTimeFinder(int duration) {
		this.daysOfTheWeek = new ArrayList<>();
		this.daysOfTheWeek.add("Monday");
		this.daysOfTheWeek.add("Tuesday");
		this.daysOfTheWeek.add("Wednesday");
		this.daysOfTheWeek.add("Thursday");
		this.daysOfTheWeek.add("Friday");
		this.daysOfTheWeek.add("Saturday");
		this.daysOfTheWeek.add("Sunday");

		this.duration = duration;
	}

	/**
	 * This method pairs the schedule by two and calls findCommonTime to
	 * searches a common time among the 2 given list of schedules.
	 * 
	 * @param schedules
	 *            - list of schedules to be searched.
	 * @param durationOfMeeting
	 *            - the minimum duration of time required to be in common among
	 *            all schedules.
	 * @return A text representation of the common time.
	 */
	public Schedule schedulePairingForComparison(List<Schedule> schedules) {
		int schedulesSize = schedules.size();

		// combine first two schedules to start
		Schedule schedule1 = schedules.get(0);
		Schedule schedule2 = schedules.get(1);
		Schedule scheduleCombined = getTimeSlotsPerDay(schedule1, schedule2);

		// combine succeeding schedules with previously combined schedules
		for (int i = 2; i < schedulesSize; i++) {
			scheduleCombined = getTimeSlotsPerDay(scheduleCombined,
					schedules.get(i));
		}

		return scheduleCombined;
	}

	Schedule getTimeSlotsPerDay(Schedule sched1, Schedule sched2) {
		Schedule schedule = new Schedule();
		for (String day : daysOfTheWeek) {
			try {
				List<OpenSlot> dayOpenSlots1 = sched1.getAvailable(day);
				List<OpenSlot> dayOpenSlots2 = sched2.getAvailable(day);
				OpenSlot openSlot = getCommonTimeOfDay(dayOpenSlots1, dayOpenSlots2); // throw
				if(null != openSlot){
					schedule.getAvailable(day).add(openSlot);
				}																
			} catch (NoOpenSlotException e) {
				System.out.println(e.getMessage());
			} catch (EmptyOpenSlotException e) {
				System.out.println("There is no open slot for " + day);
			}
		}

		return schedule;
	}

	OpenSlot getCommonTimeOfDay(List<OpenSlot> openSlots1,
			List<OpenSlot> openSlots2) throws EmptyOpenSlotException{
		OpenSlot commonTime = null;
		// check if duration fits timeslot, if not remove it.
		removeShortOpenSlots(openSlots1);
		removeShortOpenSlots(openSlots2);			

		// pair each timeslot in openSlots1 with openSlots 2
		outer: for (OpenSlot openSlot1 : openSlots1) {
			for (OpenSlot openSlot2 : openSlots2) {
				long start1 = openSlot1.getStart().getTime();
				long end1 = openSlot1.getEnd().getTime();

				long start2 = openSlot2.getStart().getTime();
				long end2 = openSlot2.getEnd().getTime();
				// have to check if its within or equal!
				if (checkIfTimeSlotCoincides(openSlot1, openSlot2)) {
					Date start = null;
					if (start1 <= start2) {
						start = openSlot2.getStart();
					} else {
						start = openSlot1.getStart();
					}

					Date end;
					if (end1 <= end2) {
						end = openSlot1.getEnd();
					} else {
						end = openSlot2.getEnd();
					}

					long foundDuration = (end.getTime() - start.getTime()) / 1000;

					if (foundDuration >= this.duration) {
						commonTime = new OpenSlot(start, end);
						break outer;
					}
				}

			}
		}
		return commonTime;
	}

	boolean checkIfTimeSlotCoincides(OpenSlot openSlot1,
			OpenSlot openSlot2) {
		boolean result = false;

		Date start1 = openSlot1.getStart();
		Date end1 = openSlot1.getEnd();
		Date start2 = openSlot2.getStart();
		Date end2 = openSlot2.getEnd();

		// Openslots coincide
		if (start1.equals(start2) && end1.equals(end2)) {
			result = true;
		}

		// OpenSlot2 is within openslot1
		if ((start1.getTime() <= start2.getTime())
				&& (end1.getTime() >= end2.getTime())) {
			result = true;
		}

		// OpenSlot1 is within openSlot2
		if ((start1.getTime() >= start2.getTime())
				&& (end1.getTime() <= end2.getTime())) {
			result = true;
		}
		
		return result;
	}

	/**
	 * This method removes time slots that are shorter than the required
	 * duration.
	 * 
	 * @param openSlots
	 *            - list of time slots to filter
	 */
	void removeShortOpenSlots(List<OpenSlot> openSlots) throws EmptyOpenSlotException{
		Iterator<OpenSlot> openSlotIterator = openSlots.iterator();
		while (openSlotIterator.hasNext()) {
			OpenSlot openSlot = openSlotIterator.next();
			long slotDuration = openSlot.getEnd().getTime()
					- openSlot.getStart().getTime();
			slotDuration = slotDuration / 1000;
			if (slotDuration < this.duration) {
				System.out.println("Removed " + openSlot);
				openSlotIterator.remove();
			}
		}
		
		if(openSlots.isEmpty()){
			throw new EmptyOpenSlotException();
		}
	}

	public static void main(String[] args) {

	}

}
