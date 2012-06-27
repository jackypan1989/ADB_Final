package ntu.im.cource.adb.bpr;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Ranker{

	public Scorer scorer;

	public Ranker(Scorer s){
		this.scorer = s;
	}

	List<ItemScore<Pair<String,String>>> rank(List<String> users,List<String> items){
		
		List<ItemScore<Pair<String,String>>> list = new ArrayList();
		for(String u: users){
			for(String i: items){
				
				Pair<String,String> pair = new Pair(u,i);
				
				list.add( new ItemScore(pair,scorer.score(pair)) );
			}
		}

		Collections.sort(list);
		return list;
	}

	public static void main(String[] args){
		
		if( args.length < 3 ){
			System.err.println("Usgae: java Ranker [userList] [itemList] [modelName] (output)");
			System.exit(0);
		}

		List<String> users = parseList(args[0]);
		List<String> items = parseList(args[1]);
		
		Scorer scorer;
		if( args[2].equals("random") ){
			scorer = new RandomScorer(); 
		}
		else if( args[2].equals("count") ){
			scorer = new CountScorer(parsePairList("data/data2.train"));
		}
		else if( args[2].equals("BPR") ){
			scorer = new BPRScorer(users,items,parsePairList("data/data2.train"));
		}
		else{
			scorer = new RandomScorer();
		}
		
		Ranker ranker = new Ranker(scorer);
		
		writeList( ranker.rank(users,items) , (args.length>=4)?args[3]:"ranklist.txt" );
	}
	
	static List<Pair<String,String>> parsePairList(String fname){
		
		try{
			BufferedReader bufr = new BufferedReader(new FileReader(fname));
			String line;
			String[] tokens;
			List<Pair<String,String>> list = new ArrayList();
			while( (line=bufr.readLine()) != null ){
			
				tokens = line.split("\t");
				list.add( new Pair(tokens[0],tokens[1]) );
			}
			bufr.close();
			
			return list;

		}catch(Exception e){
			e.printStackTrace();
			System.exit(0);
		}

		return null;

	}

	static List<String> parseList(String fname){
			
		try{
			BufferedReader bufr = new BufferedReader(new FileReader(fname));
			String line;
			List<String> list = new ArrayList();
			while( (line=bufr.readLine()) != null ){
				list.add(line);
			}
			bufr.close();
			
			return list;

		}catch(Exception e){
			e.printStackTrace();
			System.exit(0);
		}

		return null;
	}	
	
	static void writeList( List< ItemScore< Pair<String,String> > > list, String fname){
			
		try{
			BufferedWriter bufw = new BufferedWriter(new FileWriter(fname));
			for(ItemScore<Pair<String,String>> e: list){	
				bufw.write(e.toString());
				bufw.newLine();
			}
			bufw.close();
		
		}catch(Exception e){
			e.printStackTrace();
			System.exit(0);
		}
	}	
}
