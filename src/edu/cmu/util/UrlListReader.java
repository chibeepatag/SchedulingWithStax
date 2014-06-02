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

import org.xml.sax.SAXException;

import edu.cmu.model.URLList;
import edu.cmu.xml.Validate;

/**
 * @author Celine Patag
 * 
 */
public class UrlListReader {

	/**
	 * This string is the name of the start and end tag of the urlList.xml
	 */
	static final String URLLIST = "URLList";

	/**
	 * This string is the name of tag representing a url entry.
	 */
	static final String URL = "URL";

	/**
	 * This method reads the urlList.xml and produces a list of urls of the
	 * schedules.
	 * 
	 * @param url
	 *            of the xml containing the list of schedules
	 * @return a list of urls of the schedules
	 */
	public URLList readScheduleUrls(String url) throws SAXException {

		Validate.validateXML(url);

		List<String> urls = new ArrayList<String>();

		try {
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			InputStream in = new FileInputStream(url);
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
			XMLEvent event = eventReader.nextTag();
			if (event.isStartElement()) {
				String eventName = event.asStartElement().getName()
						.getLocalPart();
				if (eventName.equals(URLLIST)) {
					XMLEvent urlEvent = eventReader.nextTag();
					while (eventReader.hasNext()) {

						if (urlEvent.isStartElement()
								&& urlEvent.asStartElement().getName()
										.getLocalPart().equals(URL)) {
							urlEvent = eventReader.nextEvent();
							urls.add(urlEvent.asCharacters().getData());
						}
						urlEvent = eventReader.nextEvent();
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException xmlStream) {
			xmlStream.printStackTrace();
		}

		URLList urlList = new URLList(url, urls);
		return urlList;
	}

}
