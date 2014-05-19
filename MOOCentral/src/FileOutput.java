import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;

public class FileOutput {
    
    /*private static final String IVERSITY_HEADERS[] = {"id", "title", "short_desc", "long_desc",
        "course_link", "video_link", "start_date", "course_length", "course_image",
        "category", "site", "course_fee", "language", "certificate", "university", "time_scraped"};
    */
    private static final String IVERSITY_HEADERS[] = { "id",
			"profname", "profimage", "course_id"}; 
    
	private static final String COURSERA_HEADERS[] =  {"title", "short_desc", "long_desc",
        "course_link", "video_link", "start_date", "course_length", "course_image",
        "category", "site", "course_fee", "language", "certificate", "university", "time_scraped"};
    
	private static final String SPLIT_STRING = ";";

	public static void setupOutputFile(String site) {
		Date now = new Date();
		String fName = site + new java.sql.Date(now.getTime()).toString()
				+ ".csv";
		try {
			System.setOut(new PrintStream(new FileOutputStream(fName)));
		} catch (IOException ex) {
			ex.printStackTrace();

			// set printsteam to stdout if unable to get file
			System.setOut(System.out);
		}
	}

	public static void printIversityHeader() {
		System.out.println("sep=" + SPLIT_STRING);
		for (int i = 0; i < IVERSITY_HEADERS.length; i++) {
			System.out.print(IVERSITY_HEADERS[i]
					+ (i == IVERSITY_HEADERS.length - 1 ? "\n" : SPLIT_STRING));
		}
	}

	public static void printCourseraHeader() {
		System.out.println("sep=" + SPLIT_STRING);
		for (int i = 0; i < COURSERA_HEADERS.length; i++) {
			System.out.print(COURSERA_HEADERS[i]
					+ (i == COURSERA_HEADERS.length - 1 ? "\n" : SPLIT_STRING));
		}
	}

	public static void printValue(String value) {
		System.out.print(value + " ");
	}

	public static void printCVS(String msg, boolean isEnd) {
		System.out.print(msg + (isEnd ? "\n" : SPLIT_STRING));
	}

	public static void printCVS(String msg, String replacementCharacter,
			boolean isEnd) {
		msg = msg.replaceAll(SPLIT_STRING, replacementCharacter);
		printCVS(msg, isEnd);
	}

	public static String stripHTML(String in) {
		return in.replaceAll("<[^>]+>", "");
	}
}
