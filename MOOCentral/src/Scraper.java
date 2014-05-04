import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

public class Scraper {

	public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, ParseException
	{
		CanvasScraper scrapeCanvas = new CanvasScraper(1);
		scrapeCanvas.scrape();
		int totalCanvas = scrapeCanvas.getTotalCourses();
		
		Open2StudyScraper scrapeO2S = new Open2StudyScraper(totalCanvas);
		scrapeO2S.scrape();
		int totalO2S = scrapeO2S.getTotalCourses();
	}
}