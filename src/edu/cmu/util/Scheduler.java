/**
 * 
 */
package edu.cmu.util;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.SAXException;

import edu.cmu.model.OpenSlot;
import edu.cmu.model.Schedule;
import edu.cmu.model.URLList;

/**
 * @author Celine Patag
 * 
 */
public class Scheduler {

	/**
	 * This is the main method that runs the SchedulingWithStax application It
	 * needs two arguments : 1. The duration of the meeting args[0] in seconds 2. Path to
	 * the location of the urlList.xml args[1]
	 * 
	 * It first calls the method readScheduleUrls of the class URLListReader to
	 * get a list of schedule locations. This url list is then used to read the
	 * contents and the corresponding Schedule objects are created. A common
	 * time is then found among these schedule. This is printed on the command
	 * line.
	 * 
	 * @param args
	 *            - array of command line arguments
	 */
	public static void main(String[] args) {
		int duration = Integer.parseInt(args[0]);
		System.out.println("Meeting duration: " + duration);

		UrlListReader urlListReader = new UrlListReader();
		URLList urlList;
		try {
			urlList = urlListReader.readScheduleUrls(args[1]);
			ScheduleReader scheduleReader = new ScheduleReader();
			List<Schedule> schedules = new ArrayList<Schedule>();
			try {
				for (int i = 0; i < urlList.getNumURLs(); i++) {
					try {
						System.out.println("Schedule: " + i);
						Schedule schedule = scheduleReader.readSchedule(urlList
								.getURL(i));
						schedule.setName("Schedule: " + i);
						schedules.add(schedule);

					} catch (ScheduleXMLException e) {
						e.printStackTrace();
					}
				}
				CommonTimeFinder commonTimeFinder = new CommonTimeFinder(
						duration);
				Schedule commonTime = commonTimeFinder
						.schedulePairingForComparison(schedules);
				printAvailableTimes(commonTime);

			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}

		} catch (SAXException e1) {
			System.out.println("Schedules/urlList.xml is invalid.");
		}

	}

	/**
	 * This method prints the results of the application. For each day of the
	 * resulting schedule, a common time that meets the minimum duration is
	 * printed.
	 * 
	 * @param commonTime
	 *            A schedule of options when the meeting can be set.
	 * @throws NoOpenSlotException
	 */
	private static void printAvailableTimes(Schedule commonTime)
			throws NoOpenSlotException {
		System.out.println("***********************************************");
		String[] daysArray = { "Monday", "Tuesday", "Wednesday", "Thursday",
				"Friday", "Saturday", "Sunday" };
		for (int i = 0; i < daysArray.length; i++) {
			String day = daysArray[i];
			List<OpenSlot> openSlots;
			try {
				openSlots = commonTime.getAvailable(day);
				if (!openSlots.isEmpty()) {
					System.out.print("Can meet on " + day);
					System.out
							.println(". The following timeslots are available: ");
					for (OpenSlot openSlot : openSlots) {
						System.out.println(openSlot);
					}
				}

			} catch (NoOpenSlotException e) {
				System.out.println("This group can't meet on " + day);
			}

		}

	}

}
