/**
 * 
 */
package edu.cmu.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import edu.cmu.model.OpenSlot;
import edu.cmu.model.Schedule;

/**
 * @author Celine Patag
 * 
 */
public class ScheduleReader {
	static final String SCHEDULE = "schedule";

	static final String MONDAY = "Monday";
	static final String TUESDAY = "Tuesday";
	static final String WEDNESDAY = "Wednesday";
	static final String THURSDAY = "Thursday";
	static final String FRIDAY = "Friday";
	static final String SATURDAY = "Saturday";
	static final String SUNDAY = "Sunday";

	static final String OPENSLOT = "openSlot";
	static final String START = "start";
	static final String END = "end";

	public Schedule readSchedule(String url){
		Schedule schedule = null;
		Map<String, List<OpenSlot>> dayOpenSlotMap = null;
		try {
		      // First, create a new XMLInputFactory
		      XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		      // Setup a new eventReader
		      InputStream in = new FileInputStream(url);
		      XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
		      while(eventReader.hasNext()){
		    	  XMLEvent event = eventReader.nextEvent();
		    	  if(event.isStartDocument()){
		    		  schedule = new Schedule();
		    		 dayOpenSlotMap = schedule.getDayOpenSlotMap();
		    	  }else{
		    		  if(event.isStartElement()){
		    			  StartElement startElementDayOfWeek = event.asStartElement();
		    			  String dayOfWeek = startElementDayOfWeek.getName().getLocalPart();
		    			  switch (dayOfWeek) {
						case MONDAY:
							List<OpenSlot> openSlots = dayOpenSlotMap.get(MONDAY);
							Date[] time = readTimeSlot(eventReader);							
							OpenSlot openSlot = new OpenSlot(time[0], time[1]);							
							System.out.println(openSlot.getStart());
							System.out.println(openSlot.getEnd());
							break;
						default:
							break;
						}
		    		  }
		    	  }
		      }
		}catch (FileNotFoundException fnf){
			System.out.print("File "); 
			System.out.print(url); 
			System.out.println(" not found.");
		}catch (XMLStreamException xmlStream){
			
		}
			
		return schedule;
	}

	private Date[] readTimeSlot(XMLEventReader eventReader)
			throws XMLStreamException {
		String startTime = null;
		String endTime = null;
		XMLEvent openSlotEvent = eventReader.nextEvent();
		while(!openSlotEvent.isStartElement()){
			openSlotEvent = eventReader.nextEvent();
		}
		if(openSlotEvent.isStartElement()){
			StartElement startOpenSlotElement = openSlotEvent.asStartElement();
			if(startOpenSlotElement.getName().getLocalPart().equals(OPENSLOT)){
				openSlotEvent = eventReader.nextTag();								
				if(openSlotEvent.isStartElement()){
					StartElement startElement = openSlotEvent.asStartElement();
					if(startElement.getName().getLocalPart().equals(START)){			
						Characters chart = eventReader.nextEvent().asCharacters();
						startTime = chart.toString();
					}	
				}								
			
				openSlotEvent = eventReader.nextTag();
				while(!openSlotEvent.isStartElement()){
					openSlotEvent = eventReader.nextEvent();
				}
			
				if(openSlotEvent.isStartElement()){
					StartElement startElement = openSlotEvent.asStartElement();
					if(startElement.getName().getLocalPart().equals(END)){			
						Characters chart = eventReader.nextEvent().asCharacters();
						endTime = chart.toString();
					}	
				}
			
			
			}								
		}								
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
		
		Date[] time = new Date[2];
		try {
			time[0] = simpleDateFormat.parse(startTime);
			time[1] = simpleDateFormat.parse(endTime);
		} catch (ParseException e) {
			System.out.println(e.getMessage());			
		}
		return time;
	}
	

	public static void main(String[] args) {
		ScheduleReader xmlReader = new ScheduleReader();
		xmlReader.readSchedule("Schedules/schedule1.xml");
	}
}
