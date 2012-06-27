package ntu.im.cource.adb.bpr;
class ItemScore<T> implements Comparable<ItemScore>{
	
	public T item;
	public Double score;
	public ItemScore(T item, Double score){
		
		this.item = item;
		this.score = score;
	}

	public int compareTo(ItemScore i2){
		return -score.compareTo(i2.score);
	}

	public String toString(){
		return item.toString() +"\t"+ score.toString();
	}
}
