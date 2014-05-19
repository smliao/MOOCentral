import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CourseraCourse {
    private static int ids = 1;
    private String photo;

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }
    private int id; 
    private String short_name;
    private String video;
    private String name;
    private String language;
    private Sections[] courses;
    private Categories[] categories;
    private String short_description;
    private String price = "0";
    private University[] universities;
    private Hashtable<String, String> langs = new Hashtable<String, String>();
    
	public int getNumOfSections(){
	    return courses.length;
	}
    
    
    public void printCourse(int start_id) throws ParseException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {

    	id = start_id;
    	Class.forName("com.mysql.jdbc.Driver").newInstance();
		//java.sql.Connection connection = DriverManager.getConnection("jdbc:mysql://sjsu-cs.org:22/sjsucsor_160s2g42014s","sjsucsor_s2g414s","abcd#1234");
		java.sql.Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/moocs160", "root","root");
		Statement statement = connection.createStatement();
		
        langs.clear();
        langs.put("en", "English");
        langs.put("fr", "French");
        langs.put("he", "Hebrew");
        langs.put("it", "Italian");
        langs.put("de", "German");
        langs.put("zh-tw", "Chinese");
        langs.put("zh-cn", "Chinese");
        langs.put("zh-Hant", "Chinese");
        langs.put("es", "Spanish");
        langs.put("pt", "Portuguese");
        langs.put("ru", "Russian");
        langs.put("tr", "Turkish");

        // get the page for the course!!
        CoursePage page = null;
        String coursePageJSON = "https://www.coursera.org/maestro/api/topic/information?topic-id="
                + short_name;
        	
        String course_link = "https://www.coursera.org/course/" + short_name;
		try {
			Document course_page = Jsoup.connect(course_link).get();
			System.out.println("Connected to " + course_link);
		} catch (IOException e) {
			System.out.println("Could not connect to " + course_link);
		}
        
        try {
        	URL url = new URL(coursePageJSON);
			URLConnection conn = url.openConnection();
 
			// open the stream and put it into BufferedReader
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            Gson gson = new GsonBuilder().create();
            page = gson.fromJson(br, CoursePage.class);

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        for (Sections s : courses) {
        	
            String CrsName = name;
			CrsName = CrsName.replace("'", "''");
			CrsName = CrsName.replace(",", "\\,");
			String CrsShortDesc = short_description;
			CrsShortDesc = CrsShortDesc.replace("?", "\\?");
			CrsShortDesc = CrsShortDesc.replace("'", "''");
			CrsShortDesc = CrsShortDesc.replace(",", "\\,");
			String CrsLongDesc = page.getLongDesc();
			CrsLongDesc = CrsLongDesc.replace("?", "\\?");
			CrsLongDesc = CrsLongDesc.replace("'", "''");
			CrsLongDesc = CrsLongDesc.replace(",", "\\,");
			String CrsURL = "https://www.coursera.org/course/" + short_name;
			String CrsVideoLink = "http://www.youtube.com/watch?v=" + video;
			String CrsStartDate = s.getStartDate();
			String CrsLength = s.getDuration_string();
			String CrsImg = photo;
			String CrsCategory = "";
			if(categories.length > 0)
			{
				CrsCategory = categories[0].toString();
			}
			String CrsSite = "Coursera";
			String CrsFee = price;
			String CrsLanguage = "";
			if (langs.get(language) != null) {
				CrsLanguage = langs.get(language);
            } else {
            	CrsLanguage = language;
            }
			String CrsCertificate = "";
			if (s.getEligible_for_certificates().equals("FALSE")) {
				CrsCertificate = "no";
            } else {
            	CrsCertificate = "yes";
            }
			String CrsSchoolName = "";
			if (universities.length > 0) {
                for(University u: universities){
                	CrsSchoolName = CrsSchoolName + " " + u.getName();
                }
	        }
            
            String course_data_query = 
					"INSERT INTO `moocs160`.`course_data` (`id`, `title`, `short_desc`, `long_desc`, `course_link`, `video_link`, `start_date`"
					+ ", `course_length`, `course_image`, `category`, `site`, `course_fee`, `language`, `certificate`, `university`, `time_scraped`)" +
					"VALUES('" + (id) + "','" + CrsName + "','" + CrsShortDesc + "','" + CrsLongDesc + "','" + CrsURL + "','" + 
					CrsVideoLink + "','" + CrsStartDate + "','" + CrsLength + "','" + CrsImg + "','" + CrsCategory 
					+ "','" + CrsSite + "'," + CrsFee + ",'" + CrsLanguage + "','" + CrsCertificate + "','" + CrsSchoolName + "',now());";
            
            statement.executeUpdate(course_data_query);
            
            id++;
        }
        connection.close();      
    }

    public String toString() {
       return "This is a course";
    }

}

class Sections {
    public int getStart_year() {
        return start_year;
    }

    public void setStart_year(int start_year) {
        this.start_year = start_year;
    }

    public String getDuration_string() {
        if (duration_string != null) {
            int idx = duration_string.indexOf(" ");
            if (idx >= 0) {
                return duration_string.substring(0, idx);
            }
        }
        return "0";
    }

    public void setDuration_string(String duration_string) {
        this.duration_string = duration_string;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStart_month() {
        return start_month;
    }

    public int getStart_day() {
        return start_day;
    }

    public String getStart_date_string() {
        return start_date_string;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getHome_link() {
        return home_link;
    }

    private int start_year;
    private int start_month;
    private int start_day;
    private String duration_string;
    private int id;
    private String start_date_string;
    private String end_date;
    private String home_link;
    private String eligible_for_certificates;

    public String getStartDate() {
        if (start_month > 0 && start_day > 0 && start_year > 0){
            return (start_year + "-" + start_month + "-" + start_day).trim();
        }
        if (start_date_string == null) {
            // no date avail. No future sessions offered.
            start_month = 0;
            start_day = 0;
            start_year = 3000;
        } else if (start_date_string.equals("Self-service")) {
            // self-paced class
            start_month = 0;
            start_day = 0;
            start_year = 0000;
        }
        // normal scheduled class.
        return (start_year + "-" + start_month + "-" + start_day).trim();
    }

    public String getEligible_for_certificates() {
        return eligible_for_certificates;
    }

    public void setEligible_for_certificates(String eligible_for_certificates) {
        this.eligible_for_certificates = eligible_for_certificates;
    }

}

class Categories {
    private String name;

    public String toString() {
        return name;

    }

}

class CoursePage {
    private int start_month;
    private int start_day;
    private int start_year;
    private String about_the_course = null;
    private String about_the_instructor = null;

    public String getLongDesc() {
        if (about_the_course == null) {
            return "None";
        }
        return about_the_course;
    }

    public String getProfImage() {
        int end = -1;
        if (about_the_instructor == null) {
            return "None";
        }
        int beg = about_the_instructor.indexOf("src=") + 5;
        if (beg >= 5) {
            end = about_the_instructor.substring(beg).indexOf("\"");
        }

        if (beg > -1 && end > -1) {
            return about_the_instructor.substring(beg, end + beg);
        } else {
            return "None";

        }
    }

    public int getStart_month() {
        return start_month;
    }

    public void setStart_month(int start_month) {
        this.start_month = start_month;
    }

    public int getStart_year() {
        return start_year;
    }

    public void setStart_year(int start_year) {
        this.start_year = start_year;
    }

    public int getStart_day() {
        return start_day;
    }

    public void setStart_day(int start_day) {
        this.start_day = start_day;
    }

}

class University {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
