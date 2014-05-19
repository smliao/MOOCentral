import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.io.*;
import java.util.Date;
import java.text.ParseException;

public class IversityScraper {

	private int start_id;
	private int course_id;
	private int prof_id;
	private int totalCourses;
	
	public IversityScraper(int start_id, int prof_id)
	{
		this.start_id = start_id;
		this.course_id = start_id;
		this.prof_id = prof_id;
	}
	
	public void scrape(Statement statement) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		// sets the print statements to write to file. the file name is based
		// upon the date.
		String iversityURL = "https://iversity.org/courses";

		ArrayList<String> pgcrs = new ArrayList<String>(); //stores url
		pgcrs.add(iversityURL);
		// print CSV header

		for (int a = 0; a < pgcrs.size(); a++) {
			String furl = (String) pgcrs.get(a);
			Document doc = Jsoup.connect(furl).get();
			// Code below is code I added
			Elements crspg = doc.select("article[class*=courses-list-item]");
			Elements link = crspg.select("a[href^=/courses/]");
			Elements metas = crspg.select("ul[class*=course-meta]");
			int metaSize = metas.size();
			totalCourses = metas.size();
			ArrayList<Integer> startDexs = new ArrayList<Integer>();
			ArrayList<Integer> endDexs = new ArrayList<Integer>();

			// pre parse data indices
			for (int j = 0; j < metaSize; j++) 
			{
				String metaTest = metas.get(j).toString();
				if (metaTest.contains("Start date")) 
				{
				   startDexs.add(j);
				}
				else if (metaTest.contains("End date")) 
				{
					endDexs.add(j);
				}
			}
			
			// pre select data for scraping
			Elements courseNames = crspg.select("h2[class*=truncate]");
			Elements categories = crspg.select("div[class*=ribbon-content]");
			Elements shortDescs = crspg.select("p[class*=description]");
			Elements startDates = crspg.select("li[title*=Start]");
			Elements endDates = crspg.select("li[title*=End]");
			Elements languages = crspg.select("li[title*=Language]");
			Elements instructors = crspg.select("p[class*=instructors]");
			Elements images = crspg.select("img");

			// looks through the tags for the information we want to extract
			for (int j = 0; j < link.size(); j++) {

				// Scrapping data here
				// course url
				String CrsURL = "https://iversity.org" + link.get(j).attr("href");
				
				String course_link = CrsURL; 
				
				try {
					Document course_page = Jsoup.connect(course_link).get();
					System.out.println("Connected to " + course_link);
				} catch (IOException e) {
					System.out.println("Could not connect to " + course_link);
				}
				
				// course name
				String CrsName = courseNames.get(j).text();
				CrsName = CrsName.replace("'", "''");
				CrsName = CrsName.replace(",", "\\,");				

				// category
				String CrsCategory = categories.get(j).text();

				// Short description
				String CrsShortDesc = shortDescs.get(j).text();
				CrsShortDesc = CrsShortDesc.replace("?", "\\?");
				CrsShortDesc = CrsShortDesc.replace("'", "''");
				CrsShortDesc = CrsShortDesc.replace(",", "\\,");

				// Date
				String StrDate;
				if (startDexs.contains(j)) 
				{
					StrDate = startDates.get(startDexs.indexOf(j)).text();
				} 
				else if (endDexs.contains(j)) 
				{
					StrDate = "0000-00-00";
				} 
				else 
				{
					StrDate = "BLANK_DATE";
				}

				// Language
				String CrsLangauge = languages.get(j).text();

				// Professor names
				String professors = instructors.get(j).text();
				String[] multProfName = professors.split(",");
				
				// Course image
				Element image = images.get(j);
				String CrsImg = image.attr("src");
				
				// After this point, we have to extract the rest of the
				// information from the courage page and not the home page
				Document courseDoc = Jsoup.connect(CrsURL).timeout(0).get();
				Elements video = courseDoc.select("iframe");
				String CrsVideoLink = video.attr("src");

				// returns long info, updated to scrape from the P tag in the
				// block-wrapper class which appeared to be what we were after
				String CrsLongDesc = courseDoc.select("div.block-wrapper > p").text();
				CrsLongDesc = CrsLongDesc.replace("?", "\\?");
				CrsLongDesc = CrsLongDesc.replace("'", "''");
				CrsLongDesc = CrsLongDesc.replace(",", "\\,");
				
				// Prints out all the professor image
				String profImg = "";
				for (Element img : courseDoc.select("img[alt*=Thumb]")) 
				{
					profImg = profImg + img.attr("src") + ",";
				}
				String[] multProfImg = profImg.split(",");
				
				// course fee
				String CrsFeeHolder = courseDoc.select("div[class=ribbon-content]").text();
				int CrsFee;
				if(CrsFeeHolder.equals("Free"))
				{
					CrsFee = 0;
				}
				else
				{
					CrsFeeHolder= CrsFeeHolder.replaceAll("[^0-9]", "");
					CrsFee = Integer.parseInt(CrsFeeHolder);
				}
				
				
				//outputting all scape data here for course details
//				String CrsProfName = "";
//				if(multProfName.length > 0) CrsProfName = multProfName[0];
//				if(CrsProfName.contains(","))
//				{
//					CrsProfName = CrsProfName.substring(0, CrsProfName.indexOf(","));
//				}
//				else if(CrsProfName.contains(" and "))
//				{
//					CrsProfName = CrsProfName.substring(0, CrsProfName.indexOf(" and "));
//				}
//				else if(CrsProfName.charAt(0) == ' ') CrsProfName = CrsProfName.replaceFirst(" ", "");
//				CrsProfName = CrsProfName.replace("'","''");
//				
//				String CrsProfImg = "";
//				if(multProfImg.length > 0) CrsProfImg = multProfImg[0];
						        
		        String CrsStartDate = null;
		        try{
		        	Date oldDate = new SimpleDateFormat("dd MMM. yyyy").parse(StrDate);	
		        	CrsStartDate = new SimpleDateFormat("yyyy-MM-dd").format(oldDate);
		        } catch(ParseException e){
		        	try{
			        	Date oldDate = new SimpleDateFormat("MMM yyyy").parse(StrDate);	
			        	CrsStartDate = new SimpleDateFormat("yyyy-MM-dd").format(oldDate);
			        	} catch(ParseException e2){
			        	CrsStartDate = StrDate;
			        	}
		        }
		        
		        int CrsLength = 0;
		        String CrsSite = "Iversity";
		        String CrsCertificate = "No";
		        String CrsSchoolName = "N/A";
		        		        
//		        System.out.println("ID:          " + j);
//				System.out.println("Title:       " + CrsName);
//				System.out.println("URL:         " + CrsURL);
//				System.out.println("Description: " + CrsLongDesc);
//				System.out.println("Short Desc:  " + CrsShortDesc);
//				System.out.println("Image:       " + CrsImg);
//				System.out.println("School Logo: " + CrsSchoolName);
//				System.out.println("Professor:   " + CrsProfName + " - " + CrsProfImg);
//				System.out.println("StrDate:     " + StrDate);
//				System.out.println("Start Date:  " + CrsStartDate);
//				System.out.println("Duration:    " + CrsLength);
//				System.out.println("Fee:         " + CrsFee);
//				System.out.println("-----------------------------------------------");
		        
		        //Date now = new Date();
		        //FileOutput.printCVS("Time scraped: " + new java.sql.Date(now.getTime()).toString(), true);
		        
				String course_data_query = 
						"INSERT INTO `moocs160`.`course_data` (`id`, `title`, `short_desc`, `long_desc`, `course_link`, `video_link`, `start_date`"
						+ ", `course_length`, `course_image`, `category`, `site`, `course_fee`, `language`, `certificate`, `university`, `time_scraped`)" +
						"VALUES('" + course_id + "','" + CrsName + "','" + CrsShortDesc + "','" + CrsLongDesc + "','" + CrsURL + "','" + 
						CrsVideoLink + "','" + CrsStartDate + "','" + CrsLength + "','" + CrsImg + "','" + CrsCategory 
						+ "','" + CrsSite + "'," + CrsFee + ",'" + CrsLangauge + "','" + CrsCertificate + "','" + CrsSchoolName + "',now());";
				
				statement.executeUpdate(course_data_query);
				
				for(int i = 0; i < multProfName.length; i++)
				{
					String CrsProfName = "";
					String CrsProfImg = "";
					
					CrsProfName = multProfName[i];
					
					//Get rid of the 'and' if it's there
					if(CrsProfName.matches(".*\\band\\b.*"))
						CrsProfName = CrsProfName.replaceFirst(" and ", "");
					//Get rid of the space from the start if it's there
					else if(CrsProfName.charAt(0) == ' ')
						CrsProfName = CrsProfName.replaceFirst(" ", "");
					
					CrsProfName = CrsProfName.replace("'","''");
					CrsProfImg = multProfImg[i];

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