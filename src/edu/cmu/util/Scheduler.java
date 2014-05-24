/**
 * 
 */
package edu.cmu.util;

import java.util.ArrayList;
import java.util.List;

import edu.cmu.model.Schedule;

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
		List<String> urlList = urlListReader.readScheduleUrls("Schedules/urlList.xml");
		
		ScheduleReader scheduleReader = new ScheduleReader();
		List<Schedule> schedules = new ArrayList<Schedule>();
		for(String url : urlList){
			try {
				Schedule schedule = scheduleReader.readSchedule(url);
				schedules.add(schedule);
				CommonTimeFinder commonTimeFinder = new CommonTimeFinder();
				String commonTime = commonTimeFinder.findCommonTime(schedules);
				System.out.println(commonTime);
			} catch (ScheduleXMLException e) {
				e.printStackTrace();
			}			
		}
		
		
		
		

	}
	

}
