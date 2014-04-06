import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import java.sql.*;
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
	 */
	public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		String url = "https://www.canvas.net/";		
		 
		ArrayList<String> pgcrs = new ArrayList<String>(); //Array which will store each course URLs 
		pgcrs.add(url);
		 //The following few lines of code are used to connect to a database so the scraped course content can be stored.
		//Class.forName("com.mysql.jdbc.Driver").newInstance();
		//java.sql.Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/scrapedcourse","root","");
		//make sure you create a database named scrapedcourse in your local mysql database before running this code
		//default mysql database in your local machine is ID:root with no password
		//you can download scrapecourse database template from your Canvas account->modules->Team Project area
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
				//Statement statement = connection.createStatement();
				//Course Name
				String CrsName = link.get(j).attr("title");
	//print			System.out.println("Title:       " + CrsName);
				CrsName = CrsName.replace("'", "''");
				CrsName = CrsName.replace(",", "");
				//Course URL
				String CrsURL = link.get(j).attr("href");
	//print			System.out.println("URL:         " + CrsURL);
				//Connect to the course page
				Document crsdoc = Jsoup.connect(CrsURL).get();
				//Get the course description element
				Elements desc = crsdoc.select("div[class=block-box two-thirds first-box] > p");
				String CrsDesc = desc.text();
				CrsDesc = CrsDesc.replace("?", "");
				CrsDesc = CrsDesc.replace("'", "''");
				CrsDesc = CrsDesc.replace(",", "");
	//print			System.out.println("Description: " + CrsDesc);
				//Get the course image
				Element img = crsdoc.select("meta[property=og:image:secure_url]").first();
				String CrsImg = img.attr("content");
	//print			System.out.println("Image:       " + CrsImg);
				//Get the course school image
				Element schoolimg = crsdoc.select("div.school-logo > img").first();
				String CrsSchoolImg = schoolimg.attr("src");
				CrsSchoolImg = "https://www.canvas.net" + CrsSchoolImg;
	//print				System.out.println("School Logo: " + CrsSchoolImg);
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
	//print			System.out.println("Professor:   " + CrsProfName + " - " + CrsProfImg);
	//print			System.out.println("-----------------------------------------------");
				Elements crsheadele = crsdoc.select("div[class=gray-noise-box pad-box no-sides]");
				String youtube = "write your own code";
				Elements crsbodyele = crsdoc.select("div[class=light-bg pad-box no-sides top-rule-box]");
				String CrsDes = "write your own code";
				CrsDes = CrsDes.replace("'", "''");
				CrsDes = CrsDes.replace(",", "");
				if(CrsDes.contains("?"))
				{
					CrsDes = CrsDes.replace("?", "");
				}
				
				String video_link = "does not exist";
				String start_date = "1900-01-01";
				String course_length = "have to compute";
				String category = "does not exist";
				String site = url;
				
				String query = "INSERT INTO `MOOCentral`.`coursedata` (`id`, `title`, `short_desc`,`course_link`, `video_link`, `start_date`, `course_length`, `course_image`, `category`, `site`, `profname`, `profimage` )" + 
						"VALUES(NULL, '" + CrsName + "','" + CrsDesc + "','" + CrsURL + "','" + video_link + "','" + start_date + "','" + course_length + "','" + CrsImg + "','" + category + "','" + site + "','" + CrsProfName + "','" + CrsProfImg + "')";
				
				System.out.println(query);
				statement.executeUpdate(query);
				
				
				
//				String Date = crsdoc.select("h4[class=emboss-light] > p").text();
//				String StrDate = Date.substring(Date.indexOf(":")+1, Date.length()); //Start date after the :
//				String datechk = StrDate.substring(0, StrDate.indexOf(" "));
//				if(!datechk.matches(".*\\d.*"))
//				{
//					if(StrDate.contains("n/a"))
//					{
//						StrDate = "write you own code";
//					}
//					else
//					{
//						StrDate = "write your own code";
//					}
//				}
//				else
//				{
//					String date = StrDate.substring(0, StrDate.indexOf(" "));
//					String month = StrDate.substring(StrDate.indexOf(" ")+1, StrDate.indexOf(" ")+4);
//					String year = StrDate.substring(StrDate.length()-4,StrDate.length());
//					StrDate = "write your own code";
//				}
//				Element chk = crsdoc.select("div[class=effort last]").first();
//				Element crslenschk = crsdoc.select("div[class*=duration]").first();
//				String crsduration;
//				if (crslenschk==null)
//				{
//					crsduration = "0";
//				}
//				else if(StrDate.contains("n/a self-paced"))
//				{
//					crsduration = "0";
//				}
//				else
//				{
//					try{
//						String crsdurationtmp = crsdoc.select("div[class*=duration]").text();
//						int start = crsdurationtmp.indexOf(":")+1;
//						int end = crsdurationtmp.indexOf((" "),crsdurationtmp.indexOf(":"));
//						crsduration = crsdurationtmp.substring(start, end);
//					}
//					catch (Exception e)
//					{
//						crsduration ="0";
//						System.out.println("Exception");
//					}
//				}
				//The following is used to insert scraped data into your database table. Need to uncomment all database related code to run this.
//				String query = "insert into course_data values(null,'"+CourseName+"','"+SCrsDesrpTemp+"','"+CrsDes+"','"+crsurl+"','"+youtube+"',"+StrDate+","+crsduration+",'"+CrsImg+"','','Edx')";
//				System.out.println(query);                	
				//statement.executeUpdate(query);// skip writing to database; focus on data printout to a text file instead.
				//statement.close(); 
			 }
		}
		//connection.close();   
	}
}