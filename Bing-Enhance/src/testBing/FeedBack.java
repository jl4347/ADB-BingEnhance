package testBing;

import java.util.List;
import java.util.Scanner;

import util.BingData;
import util.Doc;

public class FeedBack {

	public static void main(String[] args){
		String keywords = "columbia";
		double target = 0.9;
		double precision = 0;
		do{
			BingData bd = new BingData();
			String response=bd.getFromBing(keywords);
			List<Doc> docs = bd.parseJson(response);			
			List<String[]> input =  bd.getInput(docs);
			
			
			/*
			 * call jialun's function here
			 * input: input
			 * 
			 * output:
			 * 
			 */
			
			for(int i=0;i<docs.size();i++){
				docs.get(i).relevant=getFeedBack(docs.get(i));
			}
			
			
			
			double[] query={0.1,2.1,2,3,4,1,0.2,0.5};
			QueryExpand qe = new QueryExpand();
			
			/*
			query= qe.computeNewWeight(query, docs);
			int[] keys= qe.findKeywordsIndex(query);
			keywords=keys[0]+"%"+keys[1];
			precision = qe.computePrecision(docs);
			*/
			
			
			//delete me
			precision = precision+0.5;
			
			
			
		}while(precision<target);		
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
		}while(!str.equals("Y")&&!str.equals("N"));
		if(str.equals("Y")){
			return true;
		}		
		return false;
	}
	
}
