package ntu.im.cource.adb.bpr;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



class AUC{
	
	static List<Integer> ans = new ArrayList();
	static List<Double> predict = new ArrayList();
	static List<Integer> index = new ArrayList();
	static List<Integer> ans_sort = new ArrayList();
	
	static boolean is_prob=true;

	static Comparator<Integer> pred_comparator = new Comparator<Integer>(){
		
		public int compare(Integer a, Integer b){
			return predict.get(b).compareTo( predict.get(a) );
		}
	};
	
	//static double aucScore(

	public static void main(String[] args){
		
		if( args.length < 2){
			System.err.println("Usgae: java AUC [rank scores] [ground truth]");
			System.exit(0);
		}

		String scoreFName = args[0];
		String posFName = args[1];
		
		readFile2(scoreFName,posFName);

		for(int i=0;i<ans.size();i++)
			index.add(i);
		
		System.out.println("|RankList|="+predict.size());
		System.out.println("AUC="+auc());
		/*
		int Pcount=0;
		for(int i=0;i<ans.size();i++)
			if(ans.get(i)==1)Pcount++;
		int k = Pcount;
		*/
		int k = 1000;
		
		double prec = precision(k);
		double rec = recall(k);
		
		System.out.println("top-"+k+":");
		System.out.println("\tprec:"+prec);
		System.out.println("\trecall:"+rec);
		System.out.println("\tF-score="+Fscore(prec,rec));
	}
	
	static double Fscore(double prec,double rec){
		
		return 2*(prec*rec)/(prec+rec);
	}
	
	static double recall(int k){
		
		int Ptotal =0;
		for(Integer a: ans)
			if(a.equals(1))
				Ptotal++;
		
		int Pcount=0;
		for(int i=0;i<k;i++)
			if(ans_sort.get(i).equals(1))
				Pcount++;
		
		return ((double)Pcount)/Ptotal;
	}

	static double precision(int k){
		
		int Pcount=0;
		for(int i=0;i<k;i++)
			if(ans_sort.get(i).equals(1))
				Pcount++;

		return ((double)Pcount)/k;
	}

	static double auc(){

		Collections.sort(index,pred_comparator);
		for(int i=0;i<index.size();i++)
			ans_sort.add( ans.get(index.get(i)) );
		
		int P=0,N=0;
		for(int i=0;i<predict.size();i++){
			
			int lab2 = ans.get(i);

			if( lab2==1 )
				P += 1;
			else if(lab2==-1)
				N += 1;
		}

		double P_rate = 1.0/P;
		double N_rate = 1.0/N;
		
		double area=1.0;
		double auc=0.0;
		for(int i=0;i<predict.size();i++){
			if( ans_sort.get(i).equals(1) ){
				auc += area*P_rate;	
			}
			else{
				area -= N_rate;
			}
		}
		return auc;
	}

	
	static void readFile2(String scoreFName, String posFName){
		
		Set<Pair<String,String>> ans_set = new HashSet();
		try{	
			BufferedReader bufr_score = new BufferedReader(new FileReader(scoreFName));
			BufferedReader bufr_pos = new BufferedReader(new FileReader(posFName));
			
			String line;
			String[] tokens;
			while(	(line=bufr_pos.readLine()) != null ){
				
				tokens = line.split("\t");
				ans_set.add(new Pair(tokens[0],tokens[1]));
			}
			bufr_pos.close();
			
			while( (line=bufr_score.readLine())!= null ){
				
				tokens = line.split("\t"); //user, tag, score
				predict.add(Double.valueOf(tokens[2]));
				Pair<String,String> pair = new Pair(tokens[0],tokens[1]);
				
				ans.add( (ans_set.contains(pair))?1:-1 );
			}
			bufr_score.close();
			
		}catch(Exception e){
			e.printStackTrace();
			System.exit(0);
		}
	}

	static void readFile(String ansFname, String predFname){
		
		try{
			
			BufferedReader bufr = new BufferedReader(new FileReader(ansFname));
			String line;
			while( (line=bufr.readLine())!=null )
				ans.add( Integer.valueOf( line.split(" ")[0] ) );
			bufr.close();

			bufr = new BufferedReader(new FileReader(predFname));
			if(is_prob)bufr.readLine();
			while( (line=bufr.readLine())!=null )
				predict.add( Double.valueOf( line.split(" ")[ ((is_prob)?1:0) ] ));
			bufr.close();

		}catch(Exception e){
			e.printStackTrace();
			System.exit(0);
		}
	}
}
