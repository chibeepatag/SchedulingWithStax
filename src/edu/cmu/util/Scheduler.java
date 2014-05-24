/**
 * 
 */
package edu.cmu.util;

import java.util.ArrayList;
import java.util.List;

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
		for(String url : urlList){
			try {
				scheduleReader.readSchedule(url);
			} catch (ScheduleXMLException e) {
				e.printStackTrace();
			}			
		}
		

	}
	

}
