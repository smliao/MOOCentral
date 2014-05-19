import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class CanvasScraper {

	private int start_id;
	private int course_id;
	private int prof_id;
	private int totalCourses;
	
	public CanvasScraper(int start_id, int prof_id)
	{
		this.start_id = start_id;
		this.course_id = start_id;
		this.prof_id = prof_id;
	}
	
	public void scrape(Statement statement) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, ParseException
	{
		String url = "https://www.canvas.net/";		
		 
		ArrayList<String> pgcrs = new ArrayList<String>();
		pgcrs.add(url);	
		
		for(int a=0; a < pgcrs.size(); a++)
		{
			String furl = (String) pgcrs.get(a);
			Document doc = Jsoup.connect(furl).get();
			Elements ele = doc.select("div[class=featured-course-image]");
			Elements crspg = ele.select("a[title]");
			Elements link = ele.select("a[href]");
			totalCourses = link.size();
			
			for (int j=0; j < link.size(); j++)
			{
				String course_link = link.get(j).attr("href"); 
				
				try {
					Document course_page = Jsoup.connect(course_link).get();
					System.out.println("Connected to " + course_link);
				} catch (IOException e) {
					System.out.println("Could not connect to " + course_link);
				}
				
				//Course Name
				String CrsName = link.get(j).attr("title");
				CrsName = CrsName.replace("'", "''");
				CrsName = CrsName.replace(",", "\\,");
				
				//Course URL
				String CrsURL = link.get(j).attr("href");

				//Connect to the course page
				Document crsdoc = Jsoup.connect(CrsURL).get();
				//Get the course description element
				Elements desc = crsdoc.select("div[class=block-box two-thirds first-box] > p");
				String CrsLongDesc = desc.text();
				CrsLongDesc = CrsLongDesc.replace("?", "\\?");
				CrsLongDesc = CrsLongDesc.replace("'", "''");
				CrsLongDesc = CrsLongDesc.replace(",", "\\,");

				String CrsShortDesc = "";
				CrsShortDesc = CrsLongDesc.substring(0, CrsLongDesc.length()/4);

				//Get the course image
				Element img = crsdoc.select("meta[property=og:image:secure_url]").first();
				String CrsImg = img.attr("content");
				
				//Get the course school image
				Element schoolimg = crsdoc.select("div.school-logo > img").first();
				String CrsSchoolImg = schoolimg.attr("src");
				String CrsSchoolName = schoolimg.attr("title");
				CrsSchoolName = CrsSchoolName.replace(",", "\\,");
				CrsSchoolImg = "https://www.canvas.net" + CrsSchoolImg;
				
				//Get the professor info
				Elements profimg = crsdoc.select("div.instructor-bio > img");
				Elements profname = crsdoc.select("div.instructor-bio > h3");

				//Get the course date, duration, and cost.
				Element date = crsdoc.select("div.course-detail-info > p").first();
				String CrsInfo = date.text();
				String CrsStartDate = CrsInfo.substring(0, CrsInfo.indexOf(" Cost"));
				String CrsEnd = "N/A";
				int CrsLength = 0;
				SimpleDateFormat convertToDate = new SimpleDateFormat("MMM d, yyyy");
				SimpleDateFormat convertToSQLDate = new SimpleDateFormat("yyyy-MM-dd");
				if(CrsStartDate.contains("Self-paced"))
				{
					CrsStartDate = CrsStartDate.substring(22);
					java.util.Date startDate = convertToDate.parse(CrsStartDate);
					CrsStartDate = convertToSQLDate.format(startDate);
				}
				else if(CrsStartDate.contains("Coming"))
				{
					CrsStartDate = CrsStartDate.substring(7);
					java.util.Date startDate = convertToDate.parse(CrsStartDate);
					CrsStartDate = convertToSQLDate.format(startDate);
				}
				else
				{
					CrsEnd = CrsStartDate.substring(CrsStartDate.indexOf("to") + 3);
					CrsStartDate = CrsStartDate.substring(0, CrsStartDate.indexOf(" to"));
					java.util.Date startDate = convertToDate.parse(CrsStartDate);
					java.util.Date endDate = convertToDate.parse(CrsEnd);
					CrsStartDate = convertToSQLDate.format(startDate);
					CrsEnd = convertToSQLDate.format(endDate);
					long startTime = startDate.getTime();
					long endTime = endDate.getTime();
					long diffTime = endTime - startTime;
					diffTime = (diffTime / (1000 * 60 * 60 * 24)) / 7;
					CrsLength = (int) diffTime;
				}
				String CrsFeeHolder = CrsInfo.substring(CrsInfo.indexOf("ment:") + 6);
				int CrsFee = 0;
				if(!CrsFeeHolder.equalsIgnoreCase("Free"))
				{
					CrsFeeHolder= CrsFeeHolder.replaceAll("[^0-9]", "");
					CrsFee = Integer.parseInt(CrsFeeHolder);
				}
				
//				System.out.println("Title:       " + CrsName);
//				System.out.println("URL:         " + CrsURL);
//				System.out.println("Description: " + CrsDesc);
//				System.out.println("Short Desc:  " + CrsShortDesc);
//				System.out.println("Image:       " + CrsImg);
//				System.out.println("School Logo: " + CrsSchoolName + " - " + CrsSchoolImg);
//				System.out.println("Professor:   " + CrsProfName + " - " + CrsProfImg);
//				System.out.println(CrsFeeHolder);
//				System.out.println("Start Date:  " + CrsDate);
//				System.out.println("End Date:    " + CrsEnd);
//				System.out.println("Duration:    " + CrsLength);
//				System.out.println("Fee:         " + CrsFee);
//				System.out.println("-----------------------------------------------");

				String CrsVideoLink = "N/A";
				String CrsCategory = "N/A";
				String CrsSite = "Canvas";
				String CrsLanguage = "English";
				String CrsCertificate = "No";
				
				String course_data_query = 
						"INSERT INTO `moocs160`.`course_data` (`id`, `title`, `short_desc`, `long_desc`, `course_link`, `video_link`, `start_date`"
						+ ", `course_length`, `course_image`, `category`, `site`, `course_fee`, `language`, `certificate`, `university`, `time_scraped`)" +
						"VALUES('" + course_id + "','" + CrsName + "','" + CrsShortDesc + "','" + CrsLongDesc + "','" + CrsURL + "','" + 
						CrsVideoLink + "','" + CrsStartDate + "','" + CrsLength + "','" + CrsImg + "','" + CrsCategory 
						+ "','" + CrsSite + "'," + CrsFee + ",'" + CrsLanguage + "','" + CrsCertificate + "','" + CrsSchoolName + "',now());";
				
				statement.executeUpdate(course_data_query);
								
				for(int i = 0; i < profname.size(); i++)
				{
					String CrsProfName = "";
					String CrsProfImg = "";
					
					CrsProfName = profname.get(i).text();				
					CrsProfName = CrsProfName.replace("'","''");
					
					if(profimg.size() > 0) CrsProfImg = "https://www.canvas.net" + profimg.get(i).attr("src");
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