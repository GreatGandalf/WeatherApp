import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CountryFilter {
	
	private String[] countries = {"UNITED STATES","GERMANY","UNITED KINGDOM","SPAIN","FRANCE","INDIA","CHINA","BURMA"};
	
	public ArrayList<String> ids = new ArrayList<String>();
	
	public void testFile() {
		try (BufferedReader br = new BufferedReader(new FileReader("D:\\Users\\levis\\Documents\\GitHub\\WeatherApp\\SimpleFileServer\\src\\station_country_data.dat"))) {
			String line;
			while ((line = br.readLine()) != null) {
				// process the line.
				//System.out.println(line);
				Pattern p = Pattern.compile("\"([^\"]*)\"");
				Matcher m = p.matcher(line);
				List<String> matches = new ArrayList<>();
				while (m.find()) {
					matches.add(m.group(1));
				}
				if(Arrays.asList(countries).contains(matches.get(1))) {
					ids.add(matches.get(0));
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(ids.size());
	}
}
