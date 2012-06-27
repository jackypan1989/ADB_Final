package ntu.im.cource.adb.bpr;
import java.util.Random;

class RandomScorer implements Scorer{
	
	static Random ran = new Random();

	public double score( Pair<String,String> pair ){
		
		return ran.nextDouble();
	}
}
