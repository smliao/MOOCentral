import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import java.sql.*;
import java.io.*;
import java.util.*;
import java.text.*;

public class Open2StudyScraper {
	
	private int start_id;
	private int course_id;
	private int prof_id;
	private int totalCourses;
	
	public Open2StudyScraper(int start_id, int prof_id)
	{
		this.start_id = start_id;
		this.course_id = start_id;
		this.prof_id = prof_id;
	}

	public void scrape(Statement statement) throws ParseException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException 
	{		
		String site = "https://www.open2study.com";
		String crcsUrl = "https://www.open2study.com/courses";
		Document doc = Jsoup.connect(crcsUrl).get();
		Elements ele = doc.select("div[class=views-field views-field-nothing]");
		totalCourses = ele.size();

		for (int i = 0; i < ele.size(); i++)
		{
			String node_id = ele.select("a[href]").get(i).attr("href");
			String CrsURL = site + node_id;
			
			String course_link = CrsURL; 
			
			try {
				Document course_page = Jsoup.connect(course_link).get();
				System.out.println("Connected to " + course_link);
			} catch (IOException e) {
				System.out.println("Could not connect to " + course_link);
			}
			
			String CrsImg = ele.select("img[class=image-style-course-logo-subjects-block]").get(i).attr("src");
			
			// Visit each course page to scrape corresponding entries.
			Document crsdoc = Jsoup.connect(CrsURL).get();

			String CrsShortDesc = crsdoc.select("div[class=offering_body]").get(0).text();
			CrsShortDesc = CrsShortDesc.replace("?", "\\?");
			CrsShortDesc = CrsShortDesc.replace("'", "''");
			CrsShortDesc = CrsShortDesc.replace(",", "\\,");
			
			String long_desc_top = crsdoc.select("div[class=summary]").get(0).text();
			String long_desc_bottom = crsdoc.select("div[class=full-body]").get(0).text();
			String CrsLongDesc = long_desc_top + long_desc_bottom;
			CrsLongDesc = CrsLongDesc.replace("?", "\\?");
			CrsLongDesc = CrsLongDesc.replace("'", "''");
			CrsLongDesc = CrsLongDesc.replace(",", "\\,");
			
			String CrsVideoLink = crsdoc.select("iframe").attr("src");
			
			Elements profname = crsdoc.select("div[class=views-field views-field-nothing]")
								.select("span[class=field-content] > h3");
			Elements profimg = crsdoc.select("img[class=image-style-teacher-small-profile]");
			
			String CrsName = crsdoc.select("h1[class=page-title offering_title]").get(0).text();
			CrsName = CrsName.replace("?", "\\?");
			CrsName = CrsName.replace("'", "''");
			CrsName = CrsName.replace(",", "\\,");
			
			String CrsCategory = "N/A";
			String[] temp = CrsName.split("[\\(\\)]");
			if(temp.length > 1){
				CrsCategory = temp[1];
			}
			
			String CrsSchoolName = crsdoc.select("div[id=provider-logo]").get(0).select("a").get(0).attr("href");
			CrsSchoolName = CrsSchoolName.replace("/educators/", "");
			CrsSchoolName = CrsSchoolName.replace("/", "");
			CrsSchoolName = CrsSchoolName.replace("-", " ");
			String[] arr = CrsSchoolName.split(" ");
			StringBuffer sb = new StringBuffer();
			for (int loop = 0; loop < arr.length; loop++) {
				sb.append(Character.toUpperCase(arr[loop].charAt(0))).append(arr[loop].substring(1)).append(" ");
			}          
			CrsSchoolName = sb.toString().trim();
			
			String start_date = "00/00/0000";
			String end_date = "00/00/0000";			
			int CrsLength = -1;
			Element date_ele = crsdoc.select("h2[class=offering_dates_date]").first();
			if (date_ele != null){
				start_date = date_ele.text();
				end_date = crsdoc.select("h2[class=offering_dates_date]").get(1).text();			
				
				java.util.Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(start_date);
				java.util.Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(end_date);
				long difference = date2.getTime() - date1.getTime(); 
				int days = (int) (difference / (1000*60*60*24));  
				CrsLength = (days / 7);
			}
			String[] start_date_split = start_date.split("/");
			String CrsStartDate = start_date_split[2] + "-" + start_date_split[1] + "-" + start_date_split[0];

			////// Not available, default values assumed.
			String CrsFee = "0";
			String CrsLanguage = "English";
			String CrsCertificate = "no";
			String CrsSite = "Open2Study";
			
//			System.out.println("**************");
//			System.out.println("course details");
//			System.out.println("**************");
//			System.out.println("       id: " + i);
//			System.out.println(" profname: " + CrsProfName);
//			System.out.println("profimage: " + CrsProfImg);
//			System.out.println("course_id: " + course_id);
//			System.out.println("***********");
//			System.out.println("course data");
//			System.out.println("***********");
//			System.out.println("           id: " + i);
//			System.out.println("        title: " + CrsName);
//			System.out.println("   short_desc: " + CrsShortDesc);
//			System.out.println("    long_desc: " + CrsLongDesc);
//			System.out.println("  course_link: " + CrsURL);
//			System.out.println("   video_link: " + CrsVideoLink);
//			System.out.println("   start_date: " + start_date);
//			System.out.println("course_length: " + CrsLength);
//			System.out.println(" course_image: " + CrsImg);
//			System.out.println("     category: " + CrsCategory);
//			System.out.println("         site: " + site);
//			System.out.println("   course_fee: " + CrsFee);
//			System.out.println("     language: " + CrsLanguage);
//			System.out.println("  certificate: " + CrsCertificate);
//			System.out.println("   university: " + CrsSchoolName);

			
			String course_data_query = 
					"INSERT INTO `course_data` (`id`, `title`, `short_desc`, `long_desc`, `course_link`, `video_link`, `start_date`"
					+ ", `course_length`, `course_image`, `category`, `site`, `course_fee`, `language`, `certificate`, `university`, `time_scraped`) " +
					"VALUES('" + course_id + "','" + CrsName + "','" + CrsShortDesc + "','" + CrsLongDesc + "','" + CrsURL + "','" + 
					CrsVideoLink + "','" + CrsStartDate + "','" + CrsLength + "','" + CrsImg + "','" + CrsCategory 
					+ "','" + CrsSite + "','" + CrsFee + "','" + CrsLanguage + "','" + CrsCertificate + "','" + CrsSchoolName + "',now());";
			
			statement.executeUpdate(course_data_query);
			
			for(int k = 0; k < profname.size(); k++)
			{
				String CrsProfName = "";
				String CrsProfImg = "";
				
				CrsProfName = profname.get(k).text();				
				CrsProfName = CrsProfName.replace("'","''");
				
				if(profimg.size() > 0) CrsProfImg = profimg.get(k).attr("src");
				else CrsProfImg = "N/A";
				
				String coursedetails_query = 
						"INSERT INTO `moocs160`.`coursedetails` (`id`, `profname`, `profimage`, `course_id`) " +
						" VALUES('" + prof_id + "','" + CrsProfName + "','" + CrsProfImg + "','" + course_id + "')";
				
				prof_id++;
				statement.executeUpdate(coursedetails_query);
			}
			
			course_id++;
		} 
	}
	
	public int getLastCourseID()
	{
		return course_id;
	}
	
	public int getLastProfID()
	{
		return prof_id;
	}
	
	public int getTotalCourses()
	{
		return totalCourses;
	}
}