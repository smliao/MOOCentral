import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 * This class contains all the info to connect to the DB.
 * @author Christy and Thinh and Jason
 */

public class Database {
	
	private int start_id;
	private static int course_id;
	private static int prof_id;
	private int totalCourses;
	
	/**
	  Inserts a course into the database.
	  @param course the course to be added (course id not needed)
	 * @return 
	 * @throws UnsupportedEncodingException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static int insertCourse(Course course, int last_course, int last_prof, Statement statement) throws SQLException, UnsupportedEncodingException, InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		course_id = last_course;
		prof_id = last_prof;
							
		String CrsName = course.getTitle();
		CrsName = CrsName.replace("'", "''");
		CrsName = CrsName.replace(",", "\\,");
		String CrsShortDesc = course.getShortDescription();
		CrsShortDesc = CrsShortDesc.replace("?", "\\?");
		CrsShortDesc = CrsShortDesc.replace("'", "''");
		CrsShortDesc = CrsShortDesc.replace(",", "\\,");
		String CrsLongDesc = course.getLongDescription();
		CrsLongDesc = CrsLongDesc.replace("?", "\\?");
		CrsLongDesc = CrsLongDesc.replace("'", "''");
		CrsLongDesc = CrsLongDesc.replace(",", "\\,");
		String CrsURL = course.getCourseLink();
		String CrsVideoLink = course.getVideoLink();
		Date CrsStartDate = course.getStartDate();
		int CrsLength = course.getCourseLength();
		String CrsImg = course.getCourseImage();
		String CrsCategory = course.getCategory();
		String CrsSite = course.getSite();
		int CrsFee = course.getCourseFee();
		String CrsLanguage = course.getLanguage();
		String CrsCertificate = course.getCertificate().enumToString();
		String CrsSchoolName = course.getUniversity();
				
		String course_data_query = 
				"INSERT INTO `moocs160`.`course_data` (`id`, `title`, `short_desc`, `long_desc`, `course_link`, `video_link`, `start_date`"
				+ ", `course_length`, `course_image`, `category`, `site`, `course_fee`, `language`, `certificate`, `university`, `time_scraped`)" +
				"VALUES('" + course_id + "','" + CrsName + "','" + CrsShortDesc + "','" + CrsLongDesc + "','" + CrsURL + "','" + 
				CrsVideoLink + "','" + CrsStartDate + "','" + CrsLength + "','" + CrsImg + "','" + CrsCategory 
				+ "','" + CrsSite + "'," + CrsFee + ",'" + CrsLanguage + "','" + CrsCertificate + "','" + CrsSchoolName + "',now());";
		
		statement.executeUpdate(course_data_query);
		
		String CrsProfName = null;
		String CrsProfImg = null;
		HashMap<String, String> profList = course.getProfessors();
		for (String key : profList.keySet()){			 
			CrsProfName = key.toString();
			CrsProfImg = profList.get(key);
			CrsProfName = CrsProfName.replace("'","''");
			
			String coursedetails_query = 
					"INSERT INTO `moocs160`.`coursedetails` (`id`, `profname`, `profimage`, `course_id`) " +
					" VALUES('" + prof_id + "','" + CrsProfName + "','" + CrsProfImg + "','" + course_id + "')";
			
			prof_id++;
			statement.executeUpdate(coursedetails_query);
		}
		
		course_id++;
		
		return prof_id;
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
