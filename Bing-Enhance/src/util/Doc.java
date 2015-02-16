package util;

public class Doc {
	public String url = null;
	public String desc =null;
	public String title=null;
	public boolean relevant = false;
	public double normal = 0;
	public double[] docWeight = null;
	public Doc(String url, String desc, String title){
		this.url=url;
		this.desc=desc;
		this.title=title;
    }
	
}
