import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.sql.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UdacityScraper {
	
	public static String SITE = "Udacity";
	public static String URL = "https://www.udacity.com";
	public static String CATALOG = "https://www.udacity.com/courses#!/All";
	public static String courseURL = "http://www.udacity.com/course/";
	public static String JSON_URL = "https://www.udacity.com/api/nodes?depth=2&fresh=false&keys[]=course_catalog&projection=catalog&required_behavior=find";
	public static String YOUTUBE = "https://www.youtube.com/watch?v=";
	
	public static void main(String[] args) {
		fetchCourses();
	}

	public static ArrayList<Course> fetchCourses() {
		ArrayList<Course> courseList = initializeCourses();
		Iterator<Course> iterator = courseList.iterator();
		
		while (iterator.hasNext()) {
			Course course = iterator.next();
			Document doc;
			try {
				doc = Jsoup.connect(course.getCourseLink()).get();
				System.out.println("Connected to " + course.getCourseLink());
			} catch (Exception e) {
				System.out.println("Could not connect to " + course.getCourseLink());
				iterator.remove();
				continue;
			}
			
			// site.
			course.setSite(SITE);
			
			// title.
			Elements title = doc.select("title");
			course.setTitle(title.text().replaceAll(" - Udacity", ""));
			
			// long_desc.
			// <!-- Left Column With Enrollment Buttons and Course Information -->
			Elements courseInfo = doc.select("div[class=col-md-8 col-md-offset-2]");
			Elements courseSummary = courseInfo.select("div[class=pretty-format]");
			course.setLongDescription(courseSummary.get(1).text());
			
			// profname and university.
			Elements instructor = doc
					.select("div[class=row row-gap-medium instructor-information-entry]")
					.select("div[class=col-md-12 instructor-information pull-left]");
			
//			System.out.println(instructor.toString());
//			System.out.println("TEST");
//			String prof2 = instructor.text();
//			prof2 = instructor.children().select("h3[class=h-slim]").text();
//			System.out.println(prof2);
			
			Pattern p = Pattern.compile("(?<=').*(?=')");
			Matcher m;
			String prof, image_link;
			HashMap<String, String> professors = new HashMap<String, String>();
			for (Element e : instructor) {
				// profname
				prof = e.children().select("h3[class=h-slim]").text();
				
				// profimage
				image_link = e.children().select("img[class=img-circle instructor-picture]").attr("data-ng-src");
				m = p.matcher(image_link);
				if (m.find()) {
					professors.put(prof, "http:" + m.group());
				} else {
//					professors.put(prof, "N/A");
				}
			}
			course.setProfessors(professors);
			
			// course_length.
			// <!-- Duration -->
			String text;
			Elements duration = doc
					.select("div[class*=duration-information]")
					.select("div[class=col-md-10");
			text = duration.get(0).text();
			
			// Use regex to find the approx. months
			// I've estimated the weeks in a month by multiplying by 4
			p = Pattern.compile("[0-9]");
			m = p.matcher(text);
			if (m.find()) {
				course.setCourseLength(Integer.parseInt(m.group())*4);
			}

			// video_link.
			// <!-- View trailer modal -->
			Elements trailerInfo = doc
					.select("div[class=scale-media]")
					.select("div");
			if (trailerInfo.attr("data-video-id").length() != 0)
				course.setVideoLink(YOUTUBE + trailerInfo.attr("data-video-id"));

			// time_scraped.
			course.setTimeScraped(new Date(new java.util.Date().getTime()));
			
			/* ****************** */
			/*                    */
			/* Data not available */
			/*                    */
			/* ****************** */

			// (N/A) startDate   Udacity has only self-paced courses
			// (N/A) category    Udacity has no categories other than tracks
			// (N/A) course_fee  Udacity provides all free courses
			// (N/A) language    Udacity does not provide language information presumably only English
			// (N/A) certificate Udacity does not have any certificates
			
			course.cleanseData();
			//System.out.println(course);
//			for (String key : professors.keySet()){			 
//				System.out.println(key.toString());
//				System.out.println(professors.get(key));
//			}
		}
		return courseList;
	}
	
	/**
	 * initializeCourses uses the JSON file to find course links, course image, and short description
	 * @return The list of courses
	 */
	public static ArrayList<Course> initializeCourses() {
		URL url;
	    InputStream is = null;
	    ArrayList<Course> courseList = new ArrayList<Course>();
	    try {
	        url = new URL(JSON_URL);

			is = url.openStream();
	        String index = IOUtils.toString(is);

	        Pattern p = Pattern.compile("(?s)\\{.*");
	        Matcher m = p.matcher(index);
	        
	    	Gson gson = new Gson();
	        Map<String, Object> map = (Map)((Map)gson.fromJson(index, Map.class).get("references")).get("Node");
	        Set<String> set = map.keySet();
	        
	        Course course;
	        String image, desc;
	        for (String s : set) {
		        course = new Course();
		        
		        /* **** */
		        /* JSON */
		        /* **** */
		        
		        // course_link.
				course.setCourseLink(courseURL + s);
				
				// course_image.
				try {
					image = (String)((Map)((Map)((Map)map.get(s)).get("catalog_entry")).get("_image")).get("serving_url");
					course.setCourseImage("http:" + image);
				} catch (Exception e) {
					// Do nothing
				}
				
				// short_desc.
				try {
					desc = (String)((Map)((Map)map.get(s)).get("catalog_entry")).get("short_summary");
					course.setShortDescription(desc);
				} catch (Exception e) {
					// Do nothing
				}
				
				courseList.add(course);
	        }
	        return courseList;
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	    return null;
	}
}