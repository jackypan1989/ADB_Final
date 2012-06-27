package ntu.im.cource.adb.bpr.data;
class Pair<A,B>{
	public A first;
	public B second;
	public Pair(A a, B b){
		first = a;
		second = b;
	}
	public int hashCode(){
		
		return first.hashCode() + second.hashCode();
	}

	public boolean equals( Pair<A,B> pair ){
		
		return  first.equals(pair.first) && second.equals(pair.second);
	}
}
