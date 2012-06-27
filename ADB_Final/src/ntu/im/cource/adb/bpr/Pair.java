package ntu.im.cource.adb.bpr;
import java.util.HashSet;
import java.util.Set;


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

	public boolean equals( Object pair_obj ){
		
		Pair<A,B> pair = (Pair<A,B>) pair_obj;
		
		return  first.equals(pair.first) && second.equals(pair.second);
	}

	public String toString(){
		return first.toString()+"\t"+second.toString();
	}

	public static void main(String[] args){
		
		Pair<String,String> pair = new Pair("A","B");
		Pair<String,String> pair2 = new Pair("A","D");
		Pair<String,String> pair3 = new Pair("A","B");

		Set<Pair<String,String>> set = new HashSet();
		set.add(pair);
		set.add(pair2);
		set.add(pair3);

		System.out.println(set.size());
		System.out.println(pair.equals(pair3));
	}
}
