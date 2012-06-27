package ntu.im.cource.adb.bpr;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class CountScorer implements Scorer{

	Map<String,Integer> user_count = new HashMap();
	Map<String,Integer> item_count = new HashMap();

	public CountScorer(List<Pair<String,String>> records){
		
		Integer sum;
		for( Pair<String,String> pair: records ){
			
			String user = pair.first;
			String item = pair.second;
			
			if( (sum=user_count.get(user)) == null)
				sum = 0;
			user_count.put( user, sum + 1 );

			if( (sum=item_count.get(item)) == null)
				sum = 0;
			item_count.put( item, sum + 1 );
		}
	}

	public double score(Pair<String,String> pair){
		
		String user = pair.first;
		String item = pair.second;

		Integer count_u,count_i;
		if( (count_u=user_count.get(user)) == null)
			count_u = 0;

		if( (count_i=item_count.get(item)) == null)
			count_i = 0;

		return count_u+count_i;
	}
}
