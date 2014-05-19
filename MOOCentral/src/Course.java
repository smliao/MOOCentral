import java.sql.Date;
import java.util.HashMap;


/**
 * This program models a course object, which holds all of its descriptive information.
 * @author: Christy
 */

public class Course {
	
	private HashMap<String, String> professors;
	private int course_id;	
	private int id;
	private String title;
	private String short_desc;
	private String long_desc;
	private String course_link;
	private String video_link;
	private Date start_date;
	private int course_length;
	private String course_image;
	private String category;
	private String site;
	private int course_fee;
	private String language;
	public enum Certificate {YES, NO;
		public String enumToString(){
			switch(this){
				case YES: return "YES";
				case NO: return "NO";
				default: return "N/A";
			}
		}
	}
	private Certificate certificate;
	private String university;
	private Date time_scraped;
	
	
	/**
	 * Constructs a course object. For the DB, the course_courseId is omitted in
	 * the construction of the object since the DB will automatically create
	 * a course courseId for each course added.
	 * 
	 * @param profname
	 * @param profimage
	 * @param course_id
	 * @param title
	 * @param short_desc
	 * @param long_desc
	 * @param course_link
	 * @param video_link
	 * @param start_date
	 * @param course_length
	 * @param course_image
	 * @param category
	 * @param site
	 * @param course_fee
	 * @param language
	 */
	public Course(HashMap<String, String> professors, String title, String short_desc,
			String long_desc, String course_link, String video_link, Date start_date, int course_length,
			String course_image, String category, String site, int course_fee, String language, 
			Certificate certificate, String university, Date time_scraped)
	{
		this.professors = professors;
		this.title = title;
		this.short_desc = short_desc;
		this.long_desc = long_desc;
		this.course_link = course_link;
		this.video_link = video_link;
		this.start_date = start_date;
		this.course_length = course_length;
		this.course_image = course_image;
		this.category = category;
		this.site = site;
		this.course_fee = course_fee;
		this.language = language;
		this.certificate = certificate;
		this.university = university;
		this.time_scraped = time_scraped;
	}
	
	/**
	 * Constructs a course object. For the DB, the course_courseId is omitted in
	 * the construction of the object since the DB will automatically create
	 * a course courseId for each course added.
	 * 
	 * @param id
	 * @param profname
	 * @param profimage
	 * @param course_id
	 * @param title
	 * @param short_desc
	 * @param long_desc
	 * @param course_link
	 * @param video_link
	 * @param start_date
	 * @param course_length
	 * @param course_image
	 * @param category
	 * @param site
	 * @param course_fee
	 * @param language
	 * @param certificate
	 * @param university
	 * @param time_scraped
	 */
	public Course(int id, HashMap<String, String> professors, int course_id, String title, String short_desc,
			String long_desc, String course_link, String video_link, Date start_date, int course_length,
			String course_image, String category, String site, int course_fee, String language, 
			Certificate certificate, String university, Date time_scraped)
	{
		this.id = id;
		this.professors = professors;
		this.course_id = 0;
		this.title = title;
		this.short_desc = short_desc;
		this.long_desc = long_desc;
		this.course_link = course_link;
		this.video_link = video_link;
		this.start_date = start_date;
		this.course_length = course_length;
		this.course_image = course_image;
		this.category = category;
		this.site = site;
		this.course_fee = course_fee;
		this.language = language;
		this.certificate = certificate;
		this.university = university;
		this.time_scraped = time_scraped;
	}
	
	public Course() {
		// empty constructor in order to add data to a course incrementally in other classes
		
	}
	
	/**
	  @return the course id
	 */
	public int getID(){
		return id;
	}
	
	/**
	  @return the course ID
	 */
	public int getCourseId() {
		return id;
	}
	
	/**
	  Sets the professor name.
	  @param profName the name of the professor
	 */
	public void setProfessors(HashMap<String, String> professors){
		this.professors = professors;
	}
	
	/**
	  @return the professor's name
	 */
	public HashMap<String, String> getProfessors(){
		return professors;
	}
	
	/**
	  Sets the course ID
	  @param courseId the id
	 */
	public void setCourseId(int courseId) {
		course_id = courseId;
	}
	
	/**
	  @return the course ID
	 */
	public int getCourseID(){
		return course_id;
	}
	
	/**
	  Sets the title of the course.
	  @param newTitle the title to be added
	 */
	public void setTitle(String newTitle){
		title = newTitle;
	}
	
	/**
	  Returns the title of the course.
	 */
	public String getTitle(){
		return title;
	}
	
	/**
	  Sets the short course description.
	  @param newDescription the short course description
	 */
	public void setShortDescription(String newDescription){
		short_desc = newDescription;
	}
	
	/**
	  Returns the short course description.
	 */
	public String getShortDescription(){
		return short_desc;
	}
	
	/**
	  Sets the long course description.
	  @param newDescription the course description
	 */
	public void setLongDescription(String newDescription){
		long_desc = newDescription;
	}
	
	/**
	  Returns the long course description.
	 */
	public String getLongDescription(){
		return long_desc;
	}
	
	/**
	  Sets the link to the original course webpage.
	  @param link the link to the page
	 */
	public void setCourseLink(String link){
		course_link = link;
	}
	
	/**
	  Returns the course link.
	 */
	public String getCourseLink(){
		return course_link;
	}
	
	/**
	  Sets the course video URL.
	  @param videoLink the link to the course video
	 */
	public void setVideoLink(String videoLink){
		video_link = videoLink;
	}
	
	/**
	  @return the video link
	 */
	public String getVideoLink(){
		return video_link;
	}
	
	/**
	  Sets the start date of the course.
	  @param newString the start date.
	 */
	public void setStartDate(Date newDate){
		start_date = newDate;
	}
	
	/**
	  Returns the course start date.
	 */
	public Date getStartDate(){
		return start_date;
	}
	
	/**
	  Sets the duration of the course (in weeks).
	  @param newDuration the number of weeks the course lasts
	 */
	public void setCourseLength(int newLength){
		course_length = newLength;
	}
	
	/**
	  Returns the duration of the course.
	 */
	public int getCourseLength(){
		return course_length;
	}
	
	/**
	 * Sets the course image address
	 * @param image the image url
	 */
	public void setCourseImage(String image) {
		this.course_image = image;
	}
	
	/**
	 * Returns the course image address
	 * @return the image url
	 */
	public String getCourseImage() {
		return course_image;
	}

	/**
	  Sets the course category.
	  @param category the course category
	 */
	public void setCategory(String category){
		this.category = category;
	}
	
	/**
	  @return the course category
	 */
	public String getCategory(){
		return category;
	}
	
	/**
	  Sets the course website.
	  @param website the course site
	 */
	public void setSite(String website){
		site = website;
	}
	
	/**
	  @return the course website
	 */
	public String getSite(){
		return site;
	}
	
	/**
	  Sets the course fee.
	  @param cost the cost of the course
	 */
	public void setCourseFee(int cost){
		course_fee = cost;
	}
	
	/**
	  @return the course fee
	 */
	public int getCourseFee(){
		return course_fee;
	}
	
	/**
	  Sets the language of the course.
	  @param language the course language
	 */
	public void setLanguage(String language){
		this.language = language;
	}
	
	/**
	  @return the course language
	 */
	public String getLanguage(){
		return language;
	}
	
	/**
	  Sets the certificate
	  @param cert the certificate status (yes or no)
	 */
	public void setCertificate(Certificate cert){
		certificate = cert;
	}
	
	/**
	  @return yes or no, depending on whether or not the course offers a certificate
	 */
	public Certificate getCertificate(){
		return certificate;
	}
	
	/**
	  Sets the university which offers the course.
	  @param newUni the university
	 */
	public void setUniversity(String newUni){
		university = newUni;
	}
	
	/**
	  Returns the university which offers the course.
	 */
	public String getUniversity(){
		return university;
	}
	
	/**
	  Set the time scraped.
	  @param time the time of scraping with date and time
	 */
	public void setTimeScraped(Date time){
		time_scraped = time;
	}
	
	/**
	  @return the time scraped
	 */
	public Date getTimeScraped(){
		return time_scraped;
	}
	
	/**
	  Reverts important values back to null.
	 */
	public void cleanseData() {
		if (title == null)
			title = "N/A";
		
		if (short_desc == null)
			short_desc = "N/A";
		
		if (long_desc == null)
			long_desc = "N/A";
		
		if (course_image == null)
			course_image = "N/A";
		
		if (university == null)
			university = "N/A";
		
		if (professors == null) {
			professors = new HashMap<String, String>();
			//professors.put("N/A", "N/A");
		}
		
		if (course_link == null)
			course_link = "N/A";
		
		if (video_link == null)
			 video_link = "N/A";
		
		if (start_date == null)
			start_date = new Date(0);
		
		if (category == null)
			category = "N/A";
		
		if (site == null)
			 site = "N/A";
		
		if (language == null)
			language = "N/A";
		
		if (certificate == null)
			certificate = Certificate.NO;
		
		if (time_scraped == null)
			time_scraped = new Date(0);
	}
	
	/**
	 * Displays the course as a list of its components.
	 */
	public String toString() {
		String result = "Title: " + title + "\n";
		result += "Category: " + category + "\n";
		result += "University: " + university + "\n";
		
		if (professors.size()==1){
			result += "Instructor: " + professors.keySet() + "\n";
			result += "Instructor Image: " + professors.get(0) + "\n";
		}
		else{
			for (String key : professors.keySet()){
				result += "Instructor: " + key + "\n";
				result += "Instructor Image: " + professors.get(key) + "\n";
			}
		}		
		
		result += "Course Link: " + course_link + "\n";
		result += "Site: " + site + "\n";
		result += "Video Link: " + video_link + "\n";
		result += "Start Date: " + start_date.toString() + "\n";
		result += "Duration: " + course_length + " Week(s)\n";
		result += "Short Description: " + short_desc + "\n";
		result += "Long Description: " + long_desc + "\n";
		result += "Image: " + course_image + "\n";
		result += "Course Fee: " + course_fee + "\n";
		result += "Language: " + language + "\n";
		result += "Certificate: " + certificate + "\n";
		result += "Time Scraped: " + time_scraped + "\n";
		return result;
	}
}
