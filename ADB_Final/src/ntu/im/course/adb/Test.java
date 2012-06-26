package ntu.im.course.adb;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.xml.sax.SAXException;

import com.aetrion.flickr.FlickrException;

public class Test implements Runnable {

	public static int num_of_threads = 0;
	private int thread_id;
	public DataCollector dc = new DataCollector();
	public static Set<String> sets = new HashSet<String>();
	
	public Test(){
		num_of_threads++;
		thread_id = num_of_threads;
	}
	
	@Override
	public void run(){
		System.out.println("running thread : Thread"+thread_id); 
		Set<String> set = dc.findLocationSet("23424977",thread_id);
		sets.addAll(set);
    }
	
	public static void main(String[] args){
		
		ArrayList<Thread> thread_list = new ArrayList<Thread>();
		
		for(int i = 0 ; i<10 ; i++){
			thread_list.add(new Thread(new Test()));
		}
		
	    for(Thread t : thread_list){
	    	
	    		t.start();
			
	    }
	    for(Thread t : thread_list){
	    	
    		try {
				t.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
        }
	    
	    // print the count of active threads
	    System.out.println(sets);
	    BufferedWriter bufWriter;
		try {
			bufWriter = new BufferedWriter(new FileWriter(
					"location_travel_muti_500.txt"));
			Iterator<String> iterator = sets.iterator();
			while (iterator.hasNext()) {
				bufWriter.write(iterator.next());
				bufWriter.newLine();
			}

			bufWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	    System.out.println("there are "+Thread.activeCount()+" threads."); 
	}

}

