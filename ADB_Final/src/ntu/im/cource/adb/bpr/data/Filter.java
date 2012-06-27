package ntu.im.cource.adb.bpr.data;

import java.io.*;
import java.util.*;


class Filter{
	
	public static Set<String> users = new HashSet();
	public static Set<String> items = new HashSet();
	public static Map<String,Set<String>> user_items = new HashMap();
	public static Map<String,Set<String>> item_users = new HashMap();
	public static List<Pair<String,String>> records = new ArrayList();
	
	public static Random ran = new Random();

	public static void main(String[] args){
		
		String dataFName = args[0];
		
		try{
			BufferedReader bufr = new BufferedReader(new FileReader(dataFName));
			String line;
			String[] tokens;
			int count=0;
			while( (line=bufr.readLine()) != null ){
				
				tokens = line.split(",");
				String user = tokens[0];
				
				if( tokens.length < 2 )
					continue;

				//System.err.println((count++)+": "+line);
				StringBuffer sbuf = new StringBuffer(tokens[1]);
				for(int i=2;i<tokens.length;i++){
					sbuf.append(","+tokens[i]);
				}
				String geoTag = sbuf.toString();
				
				
				users.add(user);
				items.add(geoTag);

				if( !user_items.containsKey(user) )
					user_items.put(user,new HashSet());
				if( !item_users.containsKey(geoTag) )
					item_users.put(geoTag,new HashSet());

				user_items.get( user ).add( geoTag );
				item_users.get( geoTag ).add( user );
			}
		
			records = toRecords(user_items);
			
		}catch(Exception e){
			
			e.printStackTrace();
			System.exit(0);
		}

		System.out.println("User Size:"+users.size());
		System.out.println("Item Size:"+items.size());
		System.out.println("record size:"+records.size());
		

		//remove user with only 1 item and vice versa.
		for( String user: user_items.keySet() ){
			if(user_items.get(user).size() < 4 ){
				users.remove(user);
			}
		}
		for( String item: item_users.keySet() ){
			if(item_users.get(item).size() < 4 ){
				items.remove(item);
			}
		}
		
		String user;
		String item;
		for( Pair<String,String> pair : records ){
			user = pair.first;
			item = pair.second;
			if( !users.contains( user ) ){
				user_items.remove(user);
				if(item_users.containsKey(item))
					item_users.get(item).remove(user);
			}
			if( !items.contains( item ) ){
				item_users.remove(item);
				if(user_items.containsKey(user))
					user_items.get(user).remove(item);
			}
		}

		records = toRecords(user_items);
		
		System.out.println("User Size:"+users.size());
		System.out.println("Item Size:"+items.size());
		System.out.println("record size:"+records.size());

		try{	
			
			BufferedWriter bufw = new BufferedWriter(new FileWriter("data2.txt"));
			for(String u : user_items.keySet()){
				for(String i: user_items.get(u)){
					bufw.write(u+"\t"+i+"\n");
				}
			}
			bufw.close();
			
			
			BufferedWriter bufw_train = new BufferedWriter(new FileWriter("data2.train"));
			BufferedWriter bufw_test = new BufferedWriter(new FileWriter("data2.test"));
			for(String u: user_items.keySet()){
				for(String i: user_items.get(u)){

					double r =  ran.nextDouble() ;
					
					if( r>0.5 )
						bufw_train.write(u+"\t"+i+"\n");
					else
						bufw_test.write(u+"\t"+i+"\n");
				}
			}
			bufw_train.close();
			bufw_test.close();
			

			BufferedWriter bufw_user = new BufferedWriter(new FileWriter("users2.txt"));
			BufferedWriter bufw_item = new BufferedWriter(new FileWriter("geotags2.txt"));
			for(String u: users){
				bufw_user.write(u+"\n");
			}
			bufw_user.close();
			
			for(String i: items){

				bufw_item.write(i+"\n");
			}
			bufw_item.close();
			
		}catch(Exception e){
			e.printStackTrace();
			System.exit(0);
		}
	}

	static List<Pair<String,String>> toRecords(Map<String,Set<String>> user_items){
			
			List<Pair<String,String>> records = new ArrayList();
			for(String user: user_items.keySet()){
				for(String item: user_items.get(user)){
					records.add(new Pair(user,item));
				}
			}

			return records;
	}
}
