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
		String pre_query = "";
		Scanner scan=null;
		
		String keywords = "";
		
		
		//verify user input
		if (args.length<3){
			System.out.println("Please enter one keyword for query: <bing account key> <precision> <query>");
			return;
		}
		double target = 0;
		String bingAPIkey = args[0];
		keywords = args[2];
		try{
			target = Double.parseDouble(args[1]);
		}catch(NumberFormatException ne){
			System.out.println("<precision> must be a number between 0 and 1");
			System.out.println("Please enter one keyword for query: <bing account key> <precision> <query>");
			return;
		}
		
		
		//System.out.println(keywords);
		
		
		double precision = 0;
		Query q = new Query();
		q.tokens=keywords.split(" ");
		String query = keywords;
		
		int iteration = 0;
		do{
			iteration++;
			BingData bd = new BingData();
			//call Bing API
			String response=bd.getFromBing(query, bingAPIkey);
			//get doc objects
			List<Doc> docs = bd.parseJson(response);
			//get vector input
			List<String[]> input =  bd.getInput(docs);
			
			//Docs less than 10, stop the iteration
			if(docs.size()<10)
				break;
			
			
			//Get user feedback before calculating the weight vector
			for(int i=0;i<docs.size();i++){
				docs.get(i).relevant=getFeedBack(docs.get(i));
			}
			
			ArrayList<String> vocab_list = null;
			CreateVector cv = new CreateVector();
			vocab_list = cv.CalcWeight(docs, q, input);
			
			
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
			
			pre_query = query;
			query = query+"%20"+key2+"%20"+key3;
			q.tokens=query.split("%20");
			
			precision = qe.computePrecision(docs);
			
						
			System.out.println("------------------------------------");
			System.out.println("Summary:");
			System.out.println("Iteration " + iteration);
			System.out.println("previous keywords= " + pre_query);
			System.out.println("augmented keywords= " + key2 + " " + key3);
			System.out.println("new keywords= "+ pre_query + " " + key2 + " " + key3);
			System.out.println("precision="+precision);
			System.out.println("------------------------------------");			
			
			
		}while(precision < target && precision != 0);
		// Terminates when the precision is 0 at first iteration
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
			System.out.println("\n");
		}while(!str.equalsIgnoreCase("Y")&&!str.equalsIgnoreCase("N"));
		if(str.equalsIgnoreCase("Y")){
			return true;
		}		
		return false;
	}
	
}
