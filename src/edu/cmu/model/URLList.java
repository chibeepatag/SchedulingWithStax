/**
 * 
 */
package edu.cmu.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Celine Patag
 * 
 */
public class URLList {

	private String url;
	
	private List<String> urlList;
	
	public URLList(String url, List<String> urlList) {
		this.url = url;
		this.urlList = urlList;
	}

	public int getNumURLs() throws Exception {
		if(urlList.size() < 2){
			throw new Exception("Not enough schedules in list.");
		}
		return urlList.size();
	}

	public String getURL(int i) throws Exception {
		return urlList.get(i);
	}
}
