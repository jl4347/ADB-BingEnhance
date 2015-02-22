package WeightCalculation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import util.*;

public class CreateVector {
	
	public static Map<String, ArrayList<Integer>> Vocab = new HashMap<String, ArrayList<Integer>>();
	public static Map<String, HashSet<Integer>> Vocab_df = new HashMap<String, HashSet<Integer>>();
	
	public static Map<String, Integer> query_dic = new HashMap<String, Integer>();
	
	public ArrayList<String> CalcWeight(List<Doc> Docs, Query query, List<String[]> Doc_tokens){
		int Doc_index = 1;
		
		// Iterate through the list of docs
		for (String[] doc : Doc_tokens){
			// Iterate through the token of each doc
			for (String token : doc){
				// Updating the Vocabulary
				ArrayList<Integer> Doc_f = Vocab.get(token);
				if (Doc_f == null){
					Doc_f = new ArrayList<Integer>();
					Vocab.put(token, Doc_f);
				}
				Doc_f.add(Doc_index);
				
				// Updating the Vocabulary to calculate df
				HashSet<Integer> df = Vocab_df.get(token);
				if (df == null){
					df = new HashSet<Integer>();
					Vocab_df.put(token, df);
				}
				df.add(Doc_index);
				
			}
			Doc_index++;
		}
		
		// Create a dictionary for query
		for (String token : query.tokens){
			if(query_dic.get(token) == null){
				query_dic.put(token, 1);
			}
			else{
				int frequency = query_dic.get(token) + 1;
				query_dic.put(token, frequency);
			}
		}
		
		//1
		System.out.println(Vocab);
		//2
		System.out.println(Vocab_df);
		//3
		System.out.println(query_dic);
		
		List<String> Vocab_list = new ArrayList<String>();
		
		// Iterate through the key set of Vocabulary
		for (String key : Vocab.keySet()){
			Vocab_list.add(key);
		}
		// Sort the vocabulary in the docs in Alphabetical order
		Collections.sort(Vocab_list);
		
		//3
		System.out.println(Vocab_list);
		
		int Vocab_len = Vocab_list.size();
		
		List<double[]> Doc_weight = new ArrayList<double[]>();
				
		for (int i=0;i<Docs.size();i++){
			double[] doc = new double[Vocab_len];
			Doc_weight.add(doc);
		}

		// Initialize query vector
		double[] query_vector = new double[Vocab_len];
		
		int Vocab_index = 0;
		
		// Calculate the weight for each doc
		for (String token : Vocab_list){
			ArrayList<Integer> doc_indexs = Vocab.get(token);
			// Iterate through the doc index for each token
			for (Integer entry : doc_indexs){
				// compute tf-idf for weight
				Doc_weight.get(entry-1)[Vocab_index] += Math.log10((double) Doc_tokens.size()/(Vocab_df.get(token).size()));
			}
			
			// To generate a new vector for query
			if(query_dic.get(token) == null){
				query_vector[Vocab_index] = 0;
			}
			else{
				query_vector[Vocab_index] = query_dic.get(token);
			}
			
			// For next vocab in the vocab_list
			Vocab_index++;
		}
		
		// Print doc vector
//		for (double[] doc : Doc_weight){
//			for (double tf : doc){
//				System.out.print(tf + ", ");
//			}
//			System.out.println("");
//		}
		
		query_vector = NormalizeVector(query_vector);
		query.weight = query_vector;
		
		//Print query vector
//		System.out.println("Print query vector: ");
//		for (int i = 0; i < query_vector.length; i++){
//			System.out.print(query_vector[i] + ", ");
//		}
//		System.out.println("\n");
		
		//Calculate normal for each doc
		for (int i = 0; i < Doc_weight.size(); i++){
			double[] doc = Doc_weight.get(i);
			doc = NormalizeVector(doc);
			Docs.get(i).docWeight = doc;
		}
		
//		System.out.println("\n");
		
		// Doc weight after normalization
//		for (double[] doc : Doc_weight){
//			for (double tf : doc){
//				System.out.print(tf + ", ");
//			}
//			System.out.println("");
//		}
		
		return (ArrayList<String>) Vocab_list;
		
	}
	
	public static double[] NormalizeVector(double[] Doc_weight){
		double normal = 0;
		
		for (double weight : Doc_weight){
			normal += Math.pow(weight, 2);
		}
		
		normal = Math.sqrt(normal);
		System.out.println(normal);
		
		for (int i = 0; i < Doc_weight.length; i++){
			Doc_weight[i] = Doc_weight[i]/normal;
		}
		
		return Doc_weight;
	}
	
}
