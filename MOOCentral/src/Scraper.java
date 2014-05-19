import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;

public class Scraper {
	
	private static int lastCourseID = 1;
	private static int lastProfID = 1;

	public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, ParseException
	{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		//java.sql.Connection connection = DriverManager.getConnection("jdbc:mysql://sjsu-cs.org/cs160/2014spring/sec2group4/phpmyadmin/index.php?db=sjsucsor_160s2g42014s","sjsucsor_s2g414s","abcd#1234");
		java.sql.Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/moocs160", "root","root");
		Statement statement = connection.createStatement();	
		
		String sqlClearData = "DELETE FROM course_data WHERE id > -1";
		String sqlClearDetails = "DELETE FROM coursedetails WHERE id > -1";
		
		statement.executeUpdate(sqlClearDetails);
		statement.executeUpdate(sqlClearData);
		
		CanvasScraper scrapeCanvas = new CanvasScraper(lastCourseID, lastProfID);
		scrapeCanvas.scrape(statement);
		lastCourseID = scrapeCanvas.getLastCourseID();
		lastProfID = scrapeCanvas.getLastProfID();
		
		Open2StudyScraper scrapeO2S = new Open2StudyScraper(lastCourseID, lastProfID);
		scrapeO2S.scrape(statement);
		lastCourseID = scrapeO2S.getLastCourseID();
		lastProfID = scrapeO2S.getLastProfID();
		
		IversityScraper scrapeIversity = new IversityScraper(lastCourseID, lastProfID);
		scrapeIversity.scrape(statement);
		lastCourseID = scrapeIversity.getLastCourseID();
		lastProfID = scrapeIversity.getLastProfID();
		
		ArrayList<Course> udacity = UdacityScraper.fetchCourses();
		
		for (Course c : udacity)
		{
			lastProfID = Database.insertCourse(c, lastCourseID, lastProfID, statement);
			lastCourseID++;
		}
		
		ArrayList<Course> futurelearn = FutureLearnScraper.fetchCourses();
		
		for (Course c : futurelearn)
		{
			lastProfID = Database.insertCourse(c, lastCourseID, lastProfID, statement);
			lastCourseID++;
		}
		
		CourseraScraper scrapeCoursera = new CourseraScraper(lastCourseID, lastProfID); 
		scrapeCoursera.scrape(statement);
		lastCourseID = scrapeIversity.getLastCourseID();
		lastProfID = scrapeIversity.getLastProfID();
		
		connection.close();
	}
}