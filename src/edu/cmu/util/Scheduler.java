/**
 * 
 */
package edu.cmu.util;

import java.util.ArrayList;
import java.util.List;

import edu.cmu.model.Schedule;
import edu.cmu.model.URLList;

/**
 * @author Celine Patag
 * 
 */
public class Scheduler {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int duration = Integer.parseInt(args[0]);
		System.out.println(duration);

		UrlListReader urlListReader = new UrlListReader();
		URLList urlList = urlListReader
				.readScheduleUrls("Schedules/urlList.xml");

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
			CommonTimeFinder commonTimeFinder = new CommonTimeFinder(duration);
			Schedule commonTime = commonTimeFinder
					.schedulePairingForComparison(schedules);
			System.out.println(commonTime);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

	}

}
