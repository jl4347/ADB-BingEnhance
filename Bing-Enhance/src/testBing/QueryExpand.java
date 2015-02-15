package testBing;


import java.util.List;
import util.Doc;



public class QueryExpand {

	public double[] computeNewWeight(double[] query, List<Doc> docs){
		double[] q = new double[query.length];
		int Dr = countRelevant(docs);
		int Dnr = 10-Dr;		
		double[] beta=new double[query.length];
		double[] gamma=new double[query.length];
	    
		for (int i=0;i<docs.size();i++){			
			if (docs.get(i).relevant){
				for(int j=0;j<docs.get(i).docWeight.length;j++)
					beta[j]+=docs.get(i).docWeight[j];
			}else{
				for(int j=0;j<docs.get(i).docWeight.length;j++)
					gamma[j]+=docs.get(i).docWeight[j];
			}
		}
		
		for (int i=0;i<q.length;i++){
			q[i]=query[i]+0.75*beta[i]/Dr-0.25*gamma[i]/Dnr;
		}		
		return q;		
	}	
	private static int countRelevant(List<Doc> docs){
		int count =0;
		for (int i=0;i<docs.size();i++)
			if(docs.get(i).relevant)
				count++;
		return count;		
	}	
	public int[] findKeywordsIndex(double[] query){
		int[] indexes= new int[2];
		int max_index = 0;
		int nex_index = 0;
		double max = Double.MIN_VALUE;
		double next = Double.MIN_VALUE;
		for(int i=0;i<query.length;i++){
			if(max<query[i]){
				max=query[i];
				max_index=i;
			}
		}
		for(int i=0;i<query.length;i++){
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
	
	public double computePrecision(List<Doc> docs){
		int count =countRelevant(docs);
		double precision = count/docs.size();			
		return precision;
	}
	
	public static void main(String[] args){
		double[] d ={0.3, 3.0, 3.2, 3.0};
		QueryExpand q = new QueryExpand();
		int[] a =q.findKeywordsIndex(d);
		for(int i=0;i<2;i++){
			System.out.println(a[i]);
		}
	}
	
	
}
