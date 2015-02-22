package WeightCalculation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import util.*;

public class CreateVector {
	
	public ArrayList<String> CalcWeight(List<Doc> Docs, Query query, List<String[]> Doc_tokens){
		
		Map<String, ArrayList<Integer>> Vocab = new HashMap<String, ArrayList<Integer>>();
		Map<String, HashSet<Integer>> Vocab_df = new HashMap<String, HashSet<Integer>>();
		
		Map<String, Integer> query_dic = new HashMap<String, Integer>();
		
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
		List<double[]> Doc_tf = new ArrayList<double[]>();
		List<double[]> Doc_df = new ArrayList<double[]>();
				
		for (int i=0;i<Docs.size();i++){
			double[] doc = new double[Vocab_len];
			double[] doc2 = new double[Vocab_len];
			double[] doc3 = new double[Vocab_len];
			Doc_weight.add(doc);
			Doc_tf.add(doc2);
			Doc_df.add(doc3);
		}

		// Initialize query vector
		double[] query_vector = new double[Vocab_len];
		
		int Vocab_index = 0;
		
		// Calculate the tf for each doc
		for (String token : Vocab_list){
			ArrayList<Integer> doc_indexs = Vocab.get(token);
			// Iterate through the doc index for each token
			for (Integer entry : doc_indexs){
				// Compute the tf
				Doc_tf.get(entry-1)[Vocab_index]++;
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
		
		// Calculate the df for each doc
		for (int i = 0; i < Vocab_list.size(); i++ ){
			for (int j = 0; j < Doc_tokens.size(); j++){
				// Method 1
				Doc_df.get(j)[i] = Vocab_df.get(Vocab_list.get(i)).size();
				
				// Method 3: 0.5 for relevant and 1 for irrelevant
//				if (Docs.get(j).relevant){
//					Doc_df.get(j)[i] = 0.5*Vocab_df.get(Vocab_list.get(i)).size();
//				}
//				else{
//					Doc_df.get(j)[i] = Vocab_df.get(Vocab_list.get(i)).size();
//				}
				
				
			}
		}
		
		System.out.println("Print tf-------------------------");
		for (double[] doc : Doc_tf){
			for (double tf : doc){
				System.out.print(tf + ", ");
			}
			System.out.println("");
		}
		
		System.out.println("Print df-------------------------");
		for (double[] doc : Doc_df){
			for (double df : doc){
				System.out.print(df + ", ");
			}
			System.out.println("");
		}
		
		for (int i = 0; i < Doc_tf.size(); i++){
			for (int j = 0; j < Doc_tf.get(i).length; j++){
				
				// Method 1: Normal tf-idf
				Doc_weight.get(i)[j] = Doc_tf.get(i)[j] * Math.log10((double) Doc_tokens.size()/Doc_df.get(i)[j]);	
				
				// Method 2: wtf-idf, use Method 1 in computing the df
				// Some bugs in it
				//Doc_weight.get(i)[j] = (1 + Math.log10(Doc_tf.get(i)[j])) * Math.log10((double) Doc_tokens.size()/Doc_df.get(i)[j]);
				
				// Method 3: tf/df
				// Not very well
				//Doc_weight.get(i)[j] = Doc_tf.get(i)[j]/Doc_df.get(i)[j];
			}
		}
		
		
		query_vector = NormalizeVector(query_vector);
		query.weight = query_vector;
		
		//Calculate normal for each doc
		for (int i = 0; i < Doc_weight.size(); i++){
			double[] doc = Doc_weight.get(i);
			doc = NormalizeVector(doc);
			Docs.get(i).docWeight = doc;
		}
		
//		System.out.println("\n");
		
		// Doc weight after normalization
		for (double[] doc : Doc_weight){
			for (double tf : doc){
				System.out.print(tf + ", ");
			}
			System.out.println("");
		}
		
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
