/**
 * 
 */
package edu.cmu.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.MalformedInputException;
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

	/**
	 * This method reads the schedule in the xml located in the url provided. It
	 * logs the read schedule in the console.
	 * 
	 * @param url
	 *            - location of the schedule xml
	 * @return The schedule object contained in the xml
	 * @throws ScheduleXMLException
	 *             when the xml does not have the Schedule node
	 */
	public Schedule readSchedule(String url) throws ScheduleXMLException {
		Schedule schedule = null;
		try {
			// First, create a new XMLInputFactory
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			// Setup a new eventReader
			InputStream in = new URL(url).openStream();// new
														// FileInputStream(url);
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
			XMLEvent scheduleEvent = eventReader.nextTag();
			if (scheduleEvent.isStartElement()) {
				StartElement scheduleDoc = scheduleEvent.asStartElement();
				if (scheduleDoc.getName().getLocalPart().equals(SCHEDULE)) {
					schedule = new Schedule();
					Map<String, List<OpenSlot>> scheduleMap = schedule
							.getDayOpenSlotMap();
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
		} catch (MalformedInputException mie) {
			mie.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		schedule.printSchedule();
		return schedule;
	}

	/**
	 * This method reads the part of the xml associated with each day of the
	 * week.
	 * 
	 * @param eventReader
	 *            - The event reader of the xml being read.
	 * @param scheduleMap
	 *            - Map of List of open slots where the key is the day of the
	 *            week the list of open slot applies.
	 * @throws XMLStreamException
	 */
	private void readDaySchedule(XMLEventReader eventReader,
			Map<String, List<OpenSlot>> scheduleMap) throws XMLStreamException {
		while (eventReader.hasNext()) {
			XMLEvent event = eventReader.nextEvent();
			if (event.isStartElement()) {
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

	/**
	 * This method reads the part of the xml associated with openSlots.
	 * 
	 * @param eventReader
	 * @param openSlots
	 *            - List of openslots where the read openSlot will be stored.
	 * @param day
	 *            - Day of the week of which the open slot should be read.
	 * @return
	 * @throws XMLStreamException
	 */
	private List<OpenSlot> readOpenSlots(XMLEventReader eventReader,
			List<OpenSlot> openSlots, String day) throws XMLStreamException {

		XMLEvent openSlotTag = eventReader.nextTag();

		while (!openSlotTag.isEndElement()
				|| !openSlotTag.asEndElement().getName().getLocalPart()
						.equals(day)) {
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

	/**
	 * This method reads the part of the xml associated with start and end times
	 * of open slots.
	 * 
	 * @param eventReader
	 * @return Array of Dates where the first element represents the start time of the open slot
	 * and the second element represents the end time of the open slot.
	 * @throws XMLStreamException
	 */
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

}
