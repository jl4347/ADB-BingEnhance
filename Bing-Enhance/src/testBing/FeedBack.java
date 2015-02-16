package testBing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import WeightCalculation.CreateVector;
import util.BingData;
import util.Doc;
import util.Query;

public class FeedBack {

	public static void main(String[] args){
		String keywords = "columbia";
		double target = 0.9;
		double precision = 0;
		Query q = new Query();
		q.tokens=keywords.split(" ");
		String query = keywords;
		do{
			BingData bd = new BingData();
			//call Bing API
			String response=bd.getFromBing(query);
			//get doc objects
			List<Doc> docs = bd.parseJson(response);
			//get vector input
			List<String[]> input =  bd.getInput(docs);
			
			ArrayList<String> vocab_list = null;
			CreateVector cv = new CreateVector();
			vocab_list = cv.CalcWeight(docs, q, input);
			
			//Get user feedback
			for(int i=0;i<docs.size();i++){
				docs.get(i).relevant=getFeedBack(docs.get(i));
			}
			
			
			//query expand calculate and get query words
			QueryExpand qe = new QueryExpand();
			
			HashMap<Integer, Integer> tindex = new HashMap();
			for (int i=0; i<q.tokens.length;i++){
				tindex.put(vocab_list.indexOf(q.tokens[i]), 1);
			}
			q.weight= qe.computeNewWeight(q.weight, docs);
			int[] keys= qe.findKeywordsIndex(q.weight, tindex);
			//keywords=keys[0]+"%"+keys[1]; detele
			String key2 = vocab_list.get(keys[0]);
			String key3 = vocab_list.get(keys[1]);
			query = query+"%"+key2+"%"+key3;
			q.tokens=query.split("%");
			
			precision = qe.computePrecision(docs);
			
						
			System.out.println("------------------------------------");
			System.out.println("keywords="+query);
			System.out.println("precision="+precision);
			System.out.println("------------------------------------");			
			
			
		}while(precision<target || precision==0);		
	}
	private static boolean getFeedBack(Doc d){
		
		System.out.println(d.url);
		System.out.println(d.title);
		System.out.println(d.desc);
		System.out.println();
		String str=null;
		Scanner scan=null;
		do{
			System.out.println("Relevant (Y/N)?");
			scan = new Scanner(System.in);
			str = scan.nextLine();
		}while(!str.equalsIgnoreCase("Y")&&!str.equalsIgnoreCase("N"));
		if(str.equalsIgnoreCase("Y")){
			return true;
		}		
		return false;
	}
	
}
