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
		URLList urlList = urlListReader.readScheduleUrls("Schedules/urlList.xml");
		
		ScheduleReader scheduleReader = new ScheduleReader();
		List<Schedule> schedules = new ArrayList<Schedule>();
		try {
			for(int i = 0; i < urlList.getNumURLs(); i++){
				try {
					Schedule schedule = scheduleReader.readSchedule(urlList.getURL(i));
					schedules.add(schedule);
					CommonTimeFinder commonTimeFinder = new CommonTimeFinder();
					String commonTime = commonTimeFinder.findCommonTime(schedules, duration);
					System.out.println(commonTime);
				} catch (ScheduleXMLException e) {
					e.printStackTrace();
				}			
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		
		
		

	}
	

}
