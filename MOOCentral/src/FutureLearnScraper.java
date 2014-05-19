import org.jsoup.*;

import java.sql.Date;

import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FutureLearnScraper {

	public static String SITE = "FutureLearn";
	public static String URL = "https://www.futurelearn.com";
	public static String courseURL = "https://www.futurelearn.com/courses/upcoming";
	
	public static void main(String[] args) {
		fetchCourses();
	}
	
	public static ArrayList<Course> fetchCourses() {

		ArrayList<Course> courseList = new ArrayList<Course>();
		Document doc;
		try {
			doc = Jsoup.connect(courseURL).get();
			System.out.println("Connected to " + URL);
		} catch (IOException e) {
			System.out.println("Could not connect to " + URL);
			return courseList;
		}
		
		Elements course_index = doc
				.select("ul[class=list course-list]")
				.select("li[class=media media-large clearfix]")
				.select("div[class=media_body]");
		Elements course_header = course_index
				.select("header[class=header header-medium]");
		Elements dates = course_index
				.select("footer[class=media_details clearfix]")
				.select("span[class=media_attributes small]");
		Elements link = course_header
				.select("a[class=title]")
				.select("[href]");
		
		for (int i = 0; i < link.size(); i++) {
			Course new_course = new Course();

			// Set site name
			new_course.setSite(SITE);
			
			// course link.
			String course_link = URL + link.get(i).attr("href");
			new_course.setCourseLink(course_link);

			// Connect to the course page
			Document course_page;
			try {
				course_page = Jsoup.connect(course_link).get();
				System.out.println("Connected to " + course_link);
			} catch (IOException e) {
				System.out.println("Could not connect to: " + course_link);
				continue;
			}

			/* ************ */
			/*              */
			/* Catalog Page */
			/*              */
			/* ************ */
			
			// university.
			Element universityLink = course_header
					.select("h3[class=organisation headline headline-secondary]")
					.get(i).select("a").first();
			new_course.setUniversity(universityLink.text());
			
			// short_desc.
			String description = course_index
					.select("section[class=media_description]")
					.select("p[class=introduction]")
					.get(i).text();
			new_course.setShortDescription(description);

			// image.
			String image = doc
					.select("ul[class=list course-list]")
					.select("a[class=media_image").get(i).child(0).attr("src");
			new_course.setCourseImage(image);

			/* *********** */
			/*             */
			/* Course Page */
			/*             */
			/* *********** */

			// profimage.
			// FutureLearn has only 1 image for all professors
			Elements educator_imgs = course_page
					.select("div[class=educator]")
					.select("img");
			String profimage = educator_imgs.attr("src");
			
			// instructor.
			Elements teachers = course_page
					.select("div[class=course-educators clearfix]")
					.select("div[class=small]")
					.select("a");
			
			HashMap<String, String> professors = new HashMap<String, String>();
			for (int j = 0; j < teachers.size(); j++) {
				String[] teacherList = teachers.get(j).text().split("(, )|( & )|( and )");
				for (String s : teacherList)
					professors.put(s, profimage);
			}
			new_course.setProfessors(professors);
			
			// title.
			String course_title = course_header
					.select("a[class=title]")
					.select("[title]")
					.get(i)
					.text();
			new_course.setTitle(course_title);

			// start_date and course_length.
			if (dates.get(i).select("time").hasAttr("datetime")) {
				Element date = dates.get(i);
				String dateInfo = date.select("time").attr("datetime");
				
				new_course.setStartDate(java.sql.Date.valueOf(dateInfo));
				String[] components = date.text().split(",");
				Pattern p = Pattern.compile("[0-9]+");
				Matcher m = p.matcher(components[1]);
				if (m.find()) {
					new_course.setCourseLength(Integer.parseInt(m.group()));
				}
			}
			
			// video_link.
			Elements video = course_page.select("iframe");
			if (video.size() > 0) 
				new_course.setVideoLink("http:" + video.attr("src"));
			
			// long_desc
			Elements long_description = course_page
					.select("div[class=course-description]")
					.select("section[class=small]");
			new_course.setLongDescription(long_description.text());
			
			// time_scraped
			new_course.setTimeScraped(new Date(new java.util.Date().getTime()));

			/* ****************** */
			/*                    */
			/* Data not available */
			/*                    */
			/* ****************** */
			
			// (N/A) category    FutureLearn has no categories
			// (N/A) course_fee  FutureLearn provides all free courses.
			// (N/A) language    FutureLearn does not provide language information presumably only English
			// (N/A) certificate FutureLearn does not have any certificates
			
			new_course.cleanseData();
			courseList.add(new_course);
			//System.out.println(new_course);
		}
		return courseList;
	}
}
