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
	private int totalCourses;
	
	public Open2StudyScraper(int start_id)
	{
		this.start_id = start_id;
	}

	public void scrape() throws ParseException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException 
	{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		//java.sql.Connection connection = DriverManager.getConnection("jdbc:mysql://sjsu-cs.org:22/sjsucsor_160s2g42014s","sjsucsor_s2g414s","abcd#1234");
		java.sql.Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/moocs160", "root","root");
		Statement statement = connection.createStatement();
		
		String site = "https://www.open2study.com";
		String crcsUrl = "https://www.open2study.com/courses";
		Document doc = Jsoup.connect(crcsUrl).get();
		Elements ele = doc.select("div[class=views-field views-field-nothing]");
		totalCourses = ele.size();
		//Elements link = ele.select("a[href]");
		//for (int i = 0; i < link.size(); i++)
		for (int i = 0; i < ele.size(); i++)
		{
			//String node_id = link.get(i).attr("href");
			String node_id = ele.select("a[href]").get(i).attr("href");
			String course_link = site + node_id;
			
			String course_id = node_id.replace("/node/", "");
			
			String course_image = ele.select("img[class=image-style-course-logo-subjects-block]").get(i).attr("src");
			
			////// Visit each course page to scrape corresponding entries.
			Document crsdoc = Jsoup.connect(course_link).get();

			String short_desc = crsdoc.select("div[class=offering_body]").get(0).text();
			short_desc = short_desc.replace("?", "");
			short_desc = short_desc.replace("'", "''");
			short_desc = short_desc.replace(",", "");
			
			String long_desc_top = crsdoc.select("div[class=summary]").get(0).text();
			String long_desc_bottom = crsdoc.select("div[class=full-body]").get(0).text();
			String long_desc = long_desc_top + long_desc_bottom;
			long_desc = long_desc.replace("?", "");
			long_desc = long_desc.replace("'", "''");
			long_desc = long_desc.replace(",", "");
			
			String video_link = crsdoc.select("iframe").attr("src");
			
			String profname = crsdoc.select("div[id=subject-teacher-tagline]").get(0).text();
			profname = profname.replace("by ", "");
			if(profname.length() > 30)
			{
				if(profname.contains(","))
				{
					profname = profname.substring(0, profname.indexOf(","));
				}
				else if(profname.contains("and"))
				{
					profname = profname.substring(0, profname.indexOf(" and"));
				}
			}
			
			String profimage = crsdoc.select("img[class=image-style-teacher-small-profile]").get(0).attr("src");

			String title = crsdoc.select("h1[class=page-title offering_title]").get(0).text();
			title = title.replace("?", "");
			title = title.replace("'", "''");
			title = title.replace(",", "");
			
			String category = "N/A";
			String[] temp = title.split("[\\(\\)]");
			if(temp.length > 1){
				category = temp[1];
			}
			
			String university = crsdoc.select("div[id=provider-logo]").get(0).select("a").get(0).attr("href");
			university = university.replace("/educators/", "");
			university = university.replace("/", "");
			university = university.replace("-", " ");
			String[] arr = university.split(" ");
			StringBuffer sb = new StringBuffer();
			for (int loop = 0; loop < arr.length; loop++) {
				sb.append(Character.toUpperCase(arr[loop].charAt(0))).append(arr[loop].substring(1)).append(" ");
			}          
			university = sb.toString().trim();
			
			String start_date = "00/00/0000";
			String end_date = "00/00/0000";			
			int course_length = -1;
			Element date_ele = crsdoc.select("h2[class=offering_dates_date]").first();
			if (date_ele != null){
				start_date = date_ele.text();
				end_date = crsdoc.select("h2[class=offering_dates_date]").get(1).text();			
				
				java.util.Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(start_date);
				java.util.Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(end_date);
				long difference = date2.getTime() - date1.getTime(); 
				int days = (int) (difference / (1000*60*60*24));  
				course_length = (days / 7);
			}
			String[] start_date_split = start_date.split("/");
			String start_date_formatted = start_date_split[2] + "-" + start_date_split[1] + "-" + start_date_split[0];

			////// Not available, default values assumed.
			String course_fee = "0";
			String language = "English";
			String certificate = "no";
			String thesite = "Open2Study";
			
			//
			/*
			System.out.println("**************");
			System.out.println("course details");
			System.out.println("**************");
			System.out.println("       id: " + i);
			System.out.println(" profname: " + profname);
			System.out.println("profimage: " + profimage);
			System.out.println("course_id: " + course_id);
			System.out.println("***********");
			System.out.println("course data");
			System.out.println("***********");
			System.out.println("           id: " + i);
			System.out.println("        title: " + title);
			System.out.println("   short_desc: " + short_desc);
			System.out.println("    long_desc: " + long_desc);
			System.out.println("  course_link: " + course_link);
			System.out.println("   video_link: " + video_link);
			System.out.println("   start_date: " + start_date);
			System.out.println("course_length: " + course_length);
			System.out.println(" course_image: " + course_image);
			System.out.println("     category: " + category);
			System.out.println("         site: " + site);
			System.out.println("   course_fee: " + course_fee);
			System.out.println("     language: " + language);
			System.out.println("  certificate: " + certificate);
			System.out.println("   university: " + university);
			//*/
			
			String course_data_query = 
					"INSERT INTO `course_data` (`id`, `title`, `short_desc`, `long_desc`, `course_link`, `video_link`, `start_date`"
					+ ", `course_length`, `course_image`, `category`, `site`, `course_fee`, `language`, `certificate`, `university`, `time_scraped`) " +
					"VALUES('" + (i + start_id) + "','" + title + "','" + short_desc + "','" + long_desc + "','" + course_link + "','" + 
					video_link + "','" + start_date_formatted + "','" + course_length + "','" + course_image + "','" + category 
					+ "','" + thesite + "','" + course_fee + "','" + language + "','" + certificate + "','" + university + "',now()) ;";
			String coursedetails_query = 
					"INSERT INTO `coursedetails` (`id`, `profname`, `profimage`, `course_id`) " +
					" VALUES('" + (i + start_id) + "','" + profname + "','" + profimage + "','" + (i + start_id) + "');";
			
			statement.executeUpdate(course_data_query);
			statement.executeUpdate(coursedetails_query);
		}
		connection.close();   
	}
	
	public int getTotalCourses()
	{
		return start_id + totalCourses;
	}
}
