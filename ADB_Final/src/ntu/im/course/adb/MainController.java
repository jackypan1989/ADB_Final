package ntu.im.course.adb;

import java.io.IOException;

import org.xml.sax.SAXException;

import com.aetrion.flickr.FlickrException;

public class MainController {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DataCollector dc = new DataCollector();
		try {
			dc.run();
		} catch (FlickrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
