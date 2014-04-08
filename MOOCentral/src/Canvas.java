import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.util.ArrayList;

public class Canvas {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws SQLException 
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, ParseException {
		String url = "https://www.canvas.net/";		
		 
		ArrayList<String> pgcrs = new ArrayList<String>();
		pgcrs.add(url);
		
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		java.sql.Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/MOOCentral","root","root");
		Statement statement = connection.createStatement();
		
		
		for(int a=0; a < pgcrs.size(); a++)
		{
			String furl = (String) pgcrs.get(a);
			Document doc = Jsoup.connect(furl).get();
			Elements ele = doc.select("div[class=featured-course-image]");
			Elements crspg = ele.select("a[title]");
			Elements link = ele.select("a[href]");
			
			for (int j=0; j < link.size(); j++)
			{
				//Course Name
				String CrsName = link.get(j).attr("title");
//				System.out.println("Title:       " + CrsName);
				CrsName = CrsName.replace("'", "''");
				CrsName = CrsName.replace(",", "");
				//Course URL
				String CrsURL = link.get(j).attr("href");
//				System.out.println("URL:         " + CrsURL);
				//Connect to the course page
				Document crsdoc = Jsoup.connect(CrsURL).get();
				//Get the course description element
				Elements desc = crsdoc.select("div[class=block-box two-thirds first-box] > p");
				String CrsDesc = desc.text();
				CrsDesc = CrsDesc.replace("?", "");
				CrsDesc = CrsDesc.replace("'", "''");
				CrsDesc = CrsDesc.replace(",", "");
//				System.out.println("Description: " + CrsDesc);
				//Get the course image
				Element img = crsdoc.select("meta[property=og:image:secure_url]").first();
				String CrsImg = img.attr("content");
//				System.out.println("Image:       " + CrsImg);
				//Get the course school image
				Element schoolimg = crsdoc.select("div.school-logo > img").first();
				String CrsSchoolImg = schoolimg.attr("src");
				CrsSchoolImg = "https://www.canvas.net" + CrsSchoolImg;
//				System.out.println("School Logo: " + CrsSchoolImg);
				//Get the professor logo
				//Get the course school image
				Element profimg = crsdoc.select("div.instructor-bio > img").first();
				Element profname = crsdoc.select("div.instructor-bio > h3").first();
				String CrsProfImg;
				if(profimg != null) 
				{
					CrsProfImg = profimg.attr("src");
				}
				else
				{
					CrsProfImg = "/null";
				}
				String CrsProfName = profname.text();
				CrsProfImg = "https://www.canvas.net" + CrsProfImg;
//				System.out.println("Professor:   " + CrsProfName + " - " + CrsProfImg);
				//Get the course date, duration, and cost.
				Element date = crsdoc.select("div.course-detail-info > p").first();
				String CrsInfo = date.text();
				String CrsDate = CrsInfo.substring(0, CrsInfo.indexOf(" Cost"));
				String CrsEnd = "N/A";
				String CrsLength = "N/A";
				SimpleDateFormat convertToDate = new SimpleDateFormat("MMM d, yyyy");
				SimpleDateFormat convertToSQLDate = new SimpleDateFormat("yyyy-MM-dd");
				if(CrsDate.contains("Self-paced"))
				{
					CrsDate = CrsDate.substring(22);
					java.util.Date startDate = convertToDate.parse(CrsDate);
					CrsDate = convertToSQLDate.format(startDate);
					CrsLength = "Self-paced";
				}
				else if(CrsDate.contains("Coming"))
				{
					CrsDate = CrsDate.substring(7);
					java.util.Date startDate = convertToDate.parse(CrsDate);
					CrsDate = convertToSQLDate.format(startDate);
				}
				else
				{
					CrsEnd = CrsDate.substring(CrsDate.indexOf("to") + 3);
					CrsDate = CrsDate.substring(0, CrsDate.indexOf(" to"));
					java.util.Date startDate = convertToDate.parse(CrsDate);
					java.util.Date endDate = convertToDate.parse(CrsEnd);
					CrsDate = convertToSQLDate.format(startDate);
					CrsEnd = convertToSQLDate.format(endDate);
					long startTime = startDate.getTime();
					long endTime = endDate.getTime();
					long diffTime = endTime - startTime;
					diffTime = (diffTime / (1000 * 60 * 60 * 24)) / 7;
					CrsLength = diffTime + " weeks";
				}
				String CrsFee = CrsInfo.substring(CrsInfo.indexOf("ment:") + 6);
//				System.out.println("Start Date:  " + CrsDate);
//				System.out.println("End Date:    " + CrsEnd);
//				System.out.println("Duration:    " + CrsLength);
//				System.out.println("Fee:         " + CrsFee);
//				System.out.println("-----------------------------------------------");
				
				String video_link = "N/A";
				String category = "N/A";
				String site = "Canvas";
				
				String query = "INSERT INTO `MOOCentral`.`coursedata` (`id`, `title`, `short_desc`,`course_link`, `video_link`, `start_date`, `course_length`, `course_image`, `category`, `site`, `profname`, `profimage` )" + 
						"VALUES(NULL, '" + CrsName + "','" + CrsDesc + "','" + CrsURL + "','" + video_link + "','" + CrsDate + "','" + CrsLength + "','" + CrsImg + "','" + category + "','" + site + "','" + CrsProfName + "','" + CrsProfImg + "')";
				
				System.out.println(query);
				statement.executeUpdate(query);
//				statement.close(); 
			 }
		}
		connection.close();   
	}
}