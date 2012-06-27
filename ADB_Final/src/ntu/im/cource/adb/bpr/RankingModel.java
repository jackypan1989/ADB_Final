package ntu.im.cource.adb.bpr;
import java.util.List;

interface RankingModel{
	
	//given a list of geotags visited by some user
	//return the ranked list of possbile places he may want to go (with scores)
	public List<ItemScore<String>> query(List<String> queryGeoTags);
	
	public List<String> allGeoTags();
	public List<String> allUsers();
}

