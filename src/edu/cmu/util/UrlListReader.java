/**
 * 
 */
package edu.cmu.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import edu.cmu.model.URLList;

/**
 * @author Celine Patag
 *
 */
public class UrlListReader {

	static final String URLLIST = "URLList";
	static final String URL = "URL";
	/**
	 * This method reads the urlList.xml and produces a list
	 * of urls of the schedules.
	 * @param url of the xml containing the list of schedules
	 * @return a list of urls of the schedules
	 */
	public URLList readScheduleUrls(String url){		
		List<String> urls = new ArrayList<String>();
		
		try {
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			InputStream in = new FileInputStream(url);
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
			XMLEvent event = eventReader.nextTag();
			if(event.isStartElement()){
				String eventName = event.asStartElement().getName().getLocalPart();
				if(eventName.equals(URLLIST)){
					XMLEvent urlEvent = eventReader.nextTag();
					while(eventReader.hasNext()){
						
						if(urlEvent.isStartElement() && urlEvent.asStartElement().getName().getLocalPart().equals(URL)){
							urlEvent = eventReader.nextEvent();
							urls.add(urlEvent.asCharacters().getData());
						}
						urlEvent = eventReader.nextEvent();
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLStreamException xmlStream) {
			xmlStream.printStackTrace();
		}
		
		URLList urlList = new URLList(url, urls);
		return urlList;
	}
	
	public static void main(String[] args) {
		UrlListReader urlListReader = new UrlListReader();
		urlListReader.readScheduleUrls("Schedules/urlList.xml");
	}

}
