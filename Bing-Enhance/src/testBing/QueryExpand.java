package testBing;


import java.util.HashMap;
import java.util.List;

import util.Doc;



public class QueryExpand {

	//Compute for the next round weight
	public double[] computeNewWeight(double[] query, List<Doc> docs){
		
		/*
		 * We use Rocchio algorithm to compute weight: 
		 * q(t+1) = alpha*q(t) + beta sum[d(j)]/Dr + gamma sum[d(j)]/Dnr
		 * 
		 * Here, we set alpha to be 1, beta to be 0.75 and gmmma to be 0.15 
		 * 
		 */
		
		double[] q = new double[query.length];
		//Dr is the number of docs that are relevant, Dnr is the # of docs not relevant 
		int Dr = countRelevant(docs);		
		int Dnr = 10-Dr;
				
		//the beta array stores sum of doc weight in which docs are relevant
		double[] beta=new double[query.length];
		//the gamma array stores sum of doc weight in which docs are not relevant
		double[] gamma=new double[query.length];
	    
		//compute the sum of weight
		for (int i=0;i<docs.size();i++){			
			if (docs.get(i).relevant){
				for(int j=0;j<docs.get(i).docWeight.length;j++)
					beta[j]+=docs.get(i).docWeight[j];
			}else{
				for(int j=0;j<docs.get(i).docWeight.length;j++)
					gamma[j]+=docs.get(i).docWeight[j];
			}
		}
		
		//compute q(t+1)
		for (int i=0;i<q.length;i++){
			q[i]=query[i]+0.75*beta[i]/Dr-0.15*gamma[i]/Dnr;
		}		
		return q;		
	}
	
	//count the feedback relevant of docs
	private static int countRelevant(List<Doc> docs){
		int count =0;
		for (int i=0;i<docs.size();i++)
			if(docs.get(i).relevant)
				count++;
		return count;		
	}	
	
	
	//find two keywords in query weight
	public int[] findKeywordsIndex(double[] query, HashMap hm){
		int[] indexes= new int[2];
		int max_index = 0;
		int nex_index = 0;
		double max = Double.MIN_VALUE;
		double next = Double.MIN_VALUE;
		for(int i=0;i<query.length;i++){
			if(hm.containsKey(i))
				continue;
			if(max<query[i]){
				max=query[i];
				max_index=i;
			}
		}
		for(int i=0;i<query.length;i++){
			if(hm.containsKey(i))
				continue;
			if(i==max_index)
				continue;
			if(next<query[i]){
				next=query[i];
				nex_index=i;
			}
		}
		indexes[0]=max_index;
		indexes[1]=nex_index;
		return indexes;
	}
	
	//compute the Precision, that is # of relevant docs/ # of total docs
	public double computePrecision(List<Doc> docs){
		int count =countRelevant(docs);
		double precision = (double)count/docs.size();			
		return precision;
	}
	

	
	
}
