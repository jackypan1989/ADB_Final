package ntu.im.course.adb;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ForMika {
	public static void main(String[] args){
		Set<String> set = new HashSet<String>();
		String s[] = null;
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader("geotags.txt"));
            
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
            	s = line.split("\\$");
            	System.out.println(s[0]);
            	for(int i=0; i<s.length;i++){
            		set.add(s[i].trim());
            	}
            }
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //Close the BufferedReader
            try {
                if (bufferedReader != null)
                    bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		
		System.out.println(set.size());
		
		
		BufferedWriter bufWriter;
		try {
			bufWriter = new BufferedWriter(new FileWriter(
					"new_location_mika.txt"));
			for (int i=0;i<s.length;i++){
				bufWriter.write("\""+s[i]+"\",");
				   bufWriter.newLine();
			}
			
			bufWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		//System.out.println(map);
		
		//{id: "Singapore", name: "Singapore"}
	}
}
