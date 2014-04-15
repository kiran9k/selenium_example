package music_download;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Logger;

public class config_reader {

	public config_reader() {
		// TODO Auto-generated constructor stub
	}
	 public static ArrayList<String> get_prop()
	    {
	    	Properties prop = new Properties();
	    	ArrayList<String> s= new ArrayList<String>();
	    	try {
	               //load a properties file
	    		prop.load(new FileInputStream("config"));
	    			
	               //get the property value and print it out
	               s.add(prop.getProperty("download_location"));
	               s.add(prop.getProperty("start_page"));
	               s.add(prop.getProperty("end_page"));
	               s.add(prop.getProperty("start_album_id"));
	               s.add(prop.getProperty("end_album_id"));
	               s.add(prop.getProperty("kbps"));
	               
	            
	    	} catch (IOException ex) {
	    		ex.printStackTrace();
	        }
			return s;
	    }
}
