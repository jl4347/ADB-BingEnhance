package util;

import java.util.HashSet;

public class Doc {
	public String url = null;
	public String desc =null;
	public String title=null;
	public boolean relevant = false;
	public double normal = 0;
	public double[] docWeight = null;
	
	// Hashset for title tokens
	public HashSet<String> title_token = new HashSet<String>();
	
	public Doc(String url, String desc, String title){
		this.url=url;
		this.desc=desc;
		this.title=title;
		
		String[] title_arr = title.split("\\W");
		for (String token : title_arr){
			if (title_token.contains(token) != true){
				title_token.add(token);
			}
		}
    }
	
//	public static void Doc_Title(){
//		
//	}
//	
}
