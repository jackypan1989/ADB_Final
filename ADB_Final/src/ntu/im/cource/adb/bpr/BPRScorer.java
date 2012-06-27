package ntu.im.cource.adb.bpr;
import java.util.Arrays;
import java.util.List;

class BPRScorer implements Scorer {
	
	final static int USER = 0;
	final static int GEOTAG = 1;

	BPRQuery bprQ;

	public BPRScorer(List<String> users, List<String> items, List<Pair<String,String>> records){
		
		BPRTrain bprTrain = new BPRTrain();
		for(String user: users)
			bprTrain.addEntity(user,USER);
		for(String item: items)
			bprTrain.addEntity(item,GEOTAG);
		for(Pair<String,String> pair : records){
			bprTrain.addData(Arrays.asList(pair.first,pair.second));
		}
		
		for(int i=0;i<20;i++){
			bprTrain.trainUniform(100000);
			System.out.println("Training Err:"+ bprTrain.sampleTrainErr() );

		}

		bprQ = new BPRQuery(bprTrain);
	}

	public double score(Pair<String,String> pair){
		
		return bprQ.computeScore(pair.first,pair.second);
	}
}
