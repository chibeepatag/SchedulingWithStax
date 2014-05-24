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

	public Schedule readSchedule(String url) throws ScheduleXMLException {
		Schedule schedule = null;		
		try {
			// First, create a new XMLInputFactory
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			// Setup a new eventReader
			InputStream in = new FileInputStream(url);
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
			XMLEvent scheduleEvent = eventReader.nextTag();
			if (scheduleEvent.isStartElement()) {
				StartElement scheduleDoc = scheduleEvent.asStartElement();
				if (scheduleDoc.getName().getLocalPart().equals(SCHEDULE)) {
					schedule = new Schedule();		
					Map<String, List<OpenSlot>> scheduleMap = schedule.getDayOpenSlotMap();
					readDaySchedule(eventReader, scheduleMap);										
				} else {
					throw new ScheduleXMLException();
				}
			}
		
		} catch (FileNotFoundException fnf) {
			System.out.print("File ");
			System.out.print(url);
			System.out.println(" not found.");
		} catch (XMLStreamException xmlStream) {
			xmlStream.printStackTrace();
		}

		schedule.printSchedule();
		return schedule;
	}

	private void readDaySchedule(XMLEventReader eventReader, Map<String, List<OpenSlot>> scheduleMap) throws XMLStreamException {
		while(eventReader.hasNext()){
			XMLEvent event = eventReader.nextEvent();
			if(event.isStartElement()){
				StartElement startElement = event.asStartElement();
				String day = startElement.getName().getLocalPart();
				List<OpenSlot> openSlots;
				switch (day) {
				case MONDAY:							
					openSlots = scheduleMap.get(MONDAY);
					readOpenSlots(eventReader, openSlots, MONDAY);
					break;
				case TUESDAY:
					openSlots = scheduleMap.get(TUESDAY);
					readOpenSlots(eventReader, openSlots, TUESDAY);
					break;
				case WEDNESDAY:
					openSlots = scheduleMap.get(WEDNESDAY);
					readOpenSlots(eventReader, openSlots, WEDNESDAY);
					break;
				case THURSDAY:
					openSlots = scheduleMap.get(THURSDAY);
					readOpenSlots(eventReader, openSlots, THURSDAY);
					break;
				case FRIDAY:
					openSlots = scheduleMap.get(FRIDAY);
					readOpenSlots(eventReader, openSlots, FRIDAY);
					break;
				case SATURDAY:
					openSlots = scheduleMap.get(SATURDAY);
					readOpenSlots(eventReader, openSlots, SATURDAY);
					break;
				case SUNDAY:
					openSlots = scheduleMap.get(SUNDAY);
					readOpenSlots(eventReader, openSlots, SUNDAY);
					break;
				default:
					break;
				}
			}
			
		}			
		
	
	}

	private List<OpenSlot> readOpenSlots(XMLEventReader eventReader, List<OpenSlot> openSlots, String day)
			throws XMLStreamException {		
		
		XMLEvent openSlotTag = eventReader.nextTag();

		while(!openSlotTag.isEndElement() || !openSlotTag.asEndElement().getName().getLocalPart().equals(day)){		
			if (openSlotTag.isStartElement()) {
				StartElement startElement = openSlotTag.asStartElement();
				if (startElement.getName().getLocalPart().equals(OPENSLOT)) {
					Date[] startEnd = readStartEndTime(eventReader);
					OpenSlot openSlot = new OpenSlot(startEnd[0], startEnd[1]);
					openSlots.add(openSlot);
				}
			}			
			openSlotTag = eventReader.nextEvent();
		}

		return openSlots;
	}

	private Date[] readStartEndTime(XMLEventReader eventReader)
			throws XMLStreamException {
		String startTime = null;
		String endTime = null;
		XMLEvent openSlotEvent = eventReader.nextEvent();
		while (!openSlotEvent.isStartElement()) {
			openSlotEvent = eventReader.nextEvent();
		}
		if (openSlotEvent.isStartElement()) {
			if (openSlotEvent.isStartElement()) {
				StartElement startElement = openSlotEvent.asStartElement();
				if (startElement.getName().getLocalPart().equals(START)) {
					Characters chart = eventReader.nextEvent().asCharacters();
					startTime = chart.toString();
				}
			}

			openSlotEvent = eventReader.nextTag();
			while (!openSlotEvent.isStartElement()) {
				openSlotEvent = eventReader.nextEvent();
			}

			if (openSlotEvent.isStartElement()) {
				StartElement startElement = openSlotEvent.asStartElement();
				if (startElement.getName().getLocalPart().equals(END)) {
					Characters chart = eventReader.nextEvent().asCharacters();
					endTime = chart.toString();
				}
			}
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

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
		try {
			/*xmlReader.readSchedule("Schedules/schedule1.xml");
			System.out.println("*****************************");
			xmlReader.readSchedule("Schedules/schedule2.xml");
			System.out.println("*****************************");*/
			xmlReader.readSchedule("Schedules/schedule3.xml");
			/*
			System.out.println("*****************************");
			xmlReader.readSchedule("Schedules/schedule4.xml");
			*/
		} catch (ScheduleXMLException e) {
			System.out.println(e.getMessage());
		}
	}
}
