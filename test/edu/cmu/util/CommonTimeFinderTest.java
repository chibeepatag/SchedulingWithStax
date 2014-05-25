/**
 * 
 */
package edu.cmu.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.cmu.model.OpenSlot;
import edu.cmu.model.Schedule;

/**
 * @author Celine Patag
 *
 */
public class CommonTimeFinderTest {

	Schedule schedule1;
	Schedule schedule2;
	
	Date time1;//9:30
	Date time2;//10:30
	Date time3;//9:30
	Date time4;	//11:30
	Date time5; //16:30
	Date time6; //17:30
	
	
	OpenSlot openSlot1;
	OpenSlot openSlot2;
	OpenSlot openSlot3;
	List<OpenSlot> openSlots;
	
	CommonTimeFinder commonTimeFinder;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		//ScheduleReader scheduleReader = new ScheduleReader();
		//schedule1 = scheduleReader.readSchedule("Schedules/schedule1.xml");
		//schedule2 = scheduleReader.readSchedule("Schedules/schedule2.xml");
		
		Calendar calendar1 = Calendar.getInstance();
		calendar1.set(2014, 5, 25, 9, 30); //9:30 am
		time1 = calendar1.getTime();		
		Calendar calendar2 = Calendar.getInstance();
		calendar2.set(2014, 5, 25, 10, 30); //10:30 am
		time2 = calendar2.getTime();

		Calendar calendar3 = Calendar.getInstance();
		calendar3.set(2014, 5, 25, 9, 30); //9:30 am
		time3 = calendar3.getTime();		
		Calendar calendar4 = Calendar.getInstance();
		calendar4.set(2014, 5, 25, 11, 30); //11:30 am
		time4 = calendar4.getTime();
		
		Calendar calendar5 = Calendar.getInstance();
		calendar5.set(2014, 5, 25, 16, 30);
		time5 = calendar5.getTime(); //16:30
		
		Calendar calendar6 = Calendar.getInstance();
		calendar6.set(2014, 5, 25, 17, 30);
		time6 = calendar6.getTime();
		openSlot1 = new OpenSlot(time1, time2); //duration 1 hour 9:30-10:30
		openSlot2 = new OpenSlot(time3, time4); //duration 2 hour 9:30 - 11:30
		openSlot3 = new OpenSlot(time4, time5); //11:30 - 16:40
		openSlots = new ArrayList<OpenSlot>();
		openSlots.add(openSlot1);
		openSlots.add(openSlot2);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		schedule1 = null;
		schedule2 = null;
	}

	/**
	 * Test method for {@link edu.cmu.util.CommonTimeFinder#CommonTimeFinder(int)}.
	 */
	@Test
	public void testCommonTimeFinder() {
		fail("Not yet implemented");
	}

	
	@Test
	public void testRemoveShortOpenSlots(){
		commonTimeFinder = new CommonTimeFinder(7200); //2 hours						

		long duration = (time2.getTime() - time1.getTime())/(1000 * 3600);		
		assertEquals(1, duration);
		
		duration = (time4.getTime() - time3.getTime())/(1000 * 3600);		
		assertEquals(2, duration);
		
		assertEquals(2, openSlots.size());
		try {
			commonTimeFinder.removeShortOpenSlots(openSlots);
			assertEquals(1, openSlots.size());
		} catch (EmptyOpenSlotException e) {
			e.printStackTrace();
		}		
	
	}
	/**
	 * Test method for {@link edu.cmu.util.CommonTimeFinder#schedulePairingForComparison(java.util.List)}.
	 */
	@Test
	public void testSchedulePairingForComparison() {
		fail("Not yet implemented");
	}
	
	@Test 
	public void testCheckIfTimeSlotsCoincide(){
		commonTimeFinder = new CommonTimeFinder(7200);
		boolean result1 = commonTimeFinder.checkIfTimeSlotCoincides(openSlot1, openSlot2);			
		assertTrue(result1);
		
		boolean result2 = commonTimeFinder.checkIfTimeSlotCoincides(openSlot1, openSlot3); //9:30 - 10:30 and 11:30 - 16:30	
		assertTrue(!result2);
		
		//coinciding start2 and end1
		OpenSlot openSlotEndCoincidesWithStart = new OpenSlot(time2, time4);
		boolean result3 = commonTimeFinder.checkIfTimeSlotCoincides(openSlot1, openSlotEndCoincidesWithStart);
		assertFalse(result3);
	}
	
	@Test
	public void testGetCommonTimeOfDay(){
		commonTimeFinder = new CommonTimeFinder(3600);
		List<OpenSlot> openSlots1 = new ArrayList<OpenSlot>();
		OpenSlot openSlot1 = new OpenSlot(time1, time2);
		OpenSlot openSlot2 = new OpenSlot(time2, time4);
		OpenSlot openSlot3 = new OpenSlot(time5, time6);
		openSlots1.add(openSlot1);
		openSlots1.add(openSlot2);
		openSlots1.add(openSlot3);
		
		List<OpenSlot> openSlots2 = new ArrayList<OpenSlot>();
		OpenSlot openSlot4 = new OpenSlot(time1, time4);
		OpenSlot openSlot5 = new OpenSlot(time4, time6);		
		openSlots2.add(openSlot4);
		openSlots2.add(openSlot5);
				
		try {
			OpenSlot commonSlot = commonTimeFinder.getCommonTimeOfDay(openSlots1, openSlots2);
			System.out.println(commonSlot);
		} catch (EmptyOpenSlotException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

}
