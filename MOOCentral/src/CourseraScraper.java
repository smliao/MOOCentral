import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CourseraScraper {
//    public static int profId = 1;
//    public static int courseId = 1;

    private int start_id;
    private int course_id;
    private int prof_id;
	private int totalCourses;
	
	public CourseraScraper(int start_id, int prof_id)
	{
		this.start_id = start_id;
		this.course_id = start_id;
		this.prof_id = prof_id;
	}
	
	public void scrape(Statement statement) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, ParseException
	{    	
    	try {
			// get URL content
			URL url = new URL("https://www.coursera.org/maestro/api/topic/list?full=1");
			URLConnection conn = url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
   
            Gson gson = new GsonBuilder().create();
            CourseraCourse[] enums = gson.fromJson(br, CourseraCourse[].class);
            
            totalCourses = enums.length;
            for (int i = 0; i < totalCourses; i++) {
                enums[i].printCourse(start_id); //This prints the course_data csv
                start_id = start_id + enums[i].getNumOfSections(); 

//              Comment out above, and uncomment this if you want the coursedetails CSV 
	            try {
					// get URL content
	            	URL url2 = new URL("https://www.coursera.org/maestro/api/user/instructorprofile?topic_short_name=" + enums[i].getShort_name() + "&exclude_topics=1");
					URLConnection conn2 = url2.openConnection();
					BufferedReader br2 = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
	
	                Instructs[] enums2 = gson.fromJson(br2, Instructs[].class);
	
	                int totalProfessors = enums2.length;
	                int actualProfessors = enums2.length;
	                int numberToSkip = enums[i].getNumOfSections();
	                for (int k = 0; k < totalProfessors; k++) {
	                    if (enums2[k].getLast_name().equals("") || enums2[k].getFirst_name().equals("") || enums2[k].getLast_name() == null || enums2[k].getFirst_name() == null){
	                        actualProfessors--;
	                    }
	                }
	                
	                if(actualProfessors == 0 && numberToSkip != 0) course_id++;
	                		                
	                int divider = 0;
	                int count = numberToSkip;
	                while(count > 0)
	                {
		                for(int j = 0; j < totalProfessors; j++) {
		                    if(enums2[j].getLast_name().equals("") || enums2[j].getFirst_name().equals("") || enums2[j].getLast_name() == null || enums2[j].getFirst_name() == null){
		                        continue;
		                    }
		                    
		                    String CrsProfName = enums2[j].getFirst_name() + " " + enums2[j].getLast_name();
		                    CrsProfName = CrsProfName.replace("'","''");
		                    String CrsProfImg = enums2[j].getPhoto();
		                    
		                    String coursedetails_query = 
		    						"INSERT INTO `moocs160`.`coursedetails` (`id`, `profname`, `profimage`, `course_id`) " +
		    						" VALUES('" + prof_id + "','" + CrsProfName + "','" + CrsProfImg + "','" + course_id + "')";
		                    
		                    statement.executeUpdate(coursedetails_query);
		                    
		                    prof_id++;
		                    divider++;
		                    if(divider % actualProfessors == 0) course_id++;
		                }
		                count--;
	                }
	            } finally {
		            int numberToSkip = enums[i].getNumOfSections();
		            if (numberToSkip <= 0){
//			                courseId++;
		            }else{
//			                courseId += numberToSkip;
		            }
	            }
            }
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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