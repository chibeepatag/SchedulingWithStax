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
	 * This method pairs the schedule by two and calls getTimeSlotsPerDay to
	 * read the timeslots of each day in the two schedules.
	 * 
	 * @param schedules
	 *            - list of schedules to be searched.
	 * 
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

	/**
	 * This method combines the two schedules by selecting only the common time
	 * slots.
	 * 
	 * @param sched1
	 * @param sched2
	 * @return combined schedule with common timeslots of sched1 and sched2.
	 */
	Schedule getTimeSlotsPerDay(Schedule sched1, Schedule sched2) {
		Schedule schedule = new Schedule();
		schedule.setName("Combined: " + sched1.getName() + sched2.getName());
		System.out.println(schedule.getName());
		for (String day : daysOfTheWeek) {
			try {
				System.out.println(day);
				List<OpenSlot> dayOpenSlots1 = sched1.getAvailable(day);
				List<OpenSlot> dayOpenSlots2 = sched2.getAvailable(day);
				OpenSlot openSlot = getCommonTimeOfDay(dayOpenSlots1,
						dayOpenSlots2); 
				if (null != openSlot) {
					schedule.getDayOpenSlotMap().get(day).add(openSlot);
				}
			} catch (NoOpenSlotException e) {
				System.out.println(e.getMessage());
			} catch (EmptyOpenSlotException e) {
				System.out.println(e.getMessage());
			}
		}

		return schedule;
	}

	/**
	 * Compares two timeslots if they are common. 1. Remove all open slots that
	 * do not fit the required meeting duration. 2. Compare each time slots of
	 * they coincide with the time length of at least the required duration.
	 * 
	 * @param openSlots1
	 * @param openSlots2
	 * @return the first time slot encountered that meets the criteria. Null is
	 *         returned if no common time is found.
	 * @throws EmptyOpenSlotException
	 *             if no timeslots are left after filtering out too short
	 *             timeslots.
	 */
	OpenSlot getCommonTimeOfDay(List<OpenSlot> openSlots1,
			List<OpenSlot> openSlots2) throws EmptyOpenSlotException {
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

	/**
	 * Checks if the timeslots occur at the same time or if one open slot is
	 * contained within the other. Ex. openSlot1 = 9:30 - 10:30 openSlot2 = 8:30
	 * - 11:30 Returns true;
	 * 
	 * @param openSlot1
	 * @param openSlot2
	 * @return true if the timeslots coincide or if one is contained in the
	 *         other.
	 * 
	 */
	boolean checkIfTimeSlotCoincides(OpenSlot openSlot1, OpenSlot openSlot2) {
		boolean result = false;

		Date start1 = openSlot1.getStart();
		Date end1 = openSlot1.getEnd();
		Date start2 = openSlot2.getStart();
		Date end2 = openSlot2.getEnd();

		
		long start = Math.max(openSlot1.getStart().getTime(), openSlot2.getStart().getTime());
		long end = Math.min(openSlot1.getEnd().getTime(), openSlot2.getEnd().getTime());
		
		long durationOfSlot = end - start;
		
		if(durationOfSlot > this.duration){
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
	void removeShortOpenSlots(List<OpenSlot> openSlots)
			throws EmptyOpenSlotException {
		Iterator<OpenSlot> openSlotIterator = openSlots.iterator();
		while (openSlotIterator.hasNext()) {
			OpenSlot openSlot = openSlotIterator.next();
			long slotDuration = openSlot.getEnd().getTime()
					- openSlot.getStart().getTime();
			slotDuration = slotDuration / 1000;
			if (slotDuration < this.duration) {
				System.out.print("Removed " + openSlot);
				System.out.println(" duration is " + slotDuration);
				openSlotIterator.remove();
			}
		}

		if (openSlots.isEmpty()) {
			throw new EmptyOpenSlotException();
		}
	}

}
