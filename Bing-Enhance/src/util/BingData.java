package util;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class BingData {
	
	
	
	//Provide your account key here. 
	String accountKey = "xXxxxxxxxxxxxx";

	public String getFromBing(String query) {
		query.replaceAll(" ", "%20");
		String bingUrl = "https://api.datamarket.azure.com/Bing/Search/Web?Query=%27"+query+"%27&$top=10&$format=Json";
		byte[] accountKeyBytes = Base64.encodeBase64((accountKey + ":" + accountKey).getBytes());
		String accountKeyEnc = new String(accountKeyBytes);
		String content = null;
		URL url;
		try {
			
			url = new URL(bingUrl);
			
			URLConnection urlConnection = url.openConnection();
			urlConnection.setRequestProperty("Authorization", "Basic " + accountKeyEnc);
			InputStream inputStream = (InputStream) urlConnection.getContent();		
			byte[] contentRaw = new byte[urlConnection.getContentLength()];
			inputStream.read(contentRaw);
			content = new String(contentRaw);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
		
		return content;
	}
	public List<Doc> parseJson(String content){
		List <Doc> docs = new ArrayList<Doc>();
	    try {
			JSONObject jsonObj = new JSONObject(content);
			JSONObject d = jsonObj.getJSONObject("d");
			JSONArray result = d.getJSONArray("results");
			
			for (int i=0;i<result.length();i++){
				JSONObject doc = (JSONObject)result.get(i);
				String url = doc.getString("DisplayUrl");
				String desc = doc.getString("Description");
				String title = doc.getString("Title");
				Doc tempd = new Doc(url,desc,title);
				docs.add(tempd);				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return docs;
	}
	public List<String[]> getInput(List<Doc> docs){
		List<String[]> result = new ArrayList<String[]>();
		for (int i=0; i<docs.size();i++){			
			String sentence = docs.get(i).title+" "+docs.get(i).desc;
			//
			sentence= sentence.toLowerCase();
			Pattern p=Pattern.compile("[.,\"\\?!:'-\\)\\(&]");
			Matcher m=p.matcher(sentence);
			String r=m.replaceAll("");	
			r = r.trim();
			String[] tokens = r.split(" ");
		    result.add(tokens);			
		}
		return result;
	}
	public static void main(String[] args){
		
		BingData  d = new BingData();
		//call Bing API
		String s = d.getFromBing("columbia");
		//get doc objects
		List<Doc> docs= d.parseJson(s);
		//get vector input
		List<String[]> r = d.getInput(docs);
		for (int i=0;i<r.size();i++){
			System.out.println(r.get(i)[0]);
		}
	}
	
}
