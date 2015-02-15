package testBing;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.*;

//Download and add this library to the build path.
import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class BingTest {

	public static void main(String[] args) throws IOException {
//		String bingUrl = "https://api.datamarket.azure.com/Bing/Search/Web?Query=%27gates%27&$top=10&$format=Atom";
//		//Provide your account key here. 
//		String accountKey = "R3HX3U71ionA0Z6uGfNdn2r06vaMF2SMqfyhIjh2H1A";
//		
//		byte[] accountKeyBytes = Base64.encodeBase64((accountKey + ":" + accountKey).getBytes());
//		String accountKeyEnc = new String(accountKeyBytes);
//
//		URL url = new URL(bingUrl);
//		URLConnection urlConnection = url.openConnection();
//		urlConnection.setRequestProperty("Authorization", "Basic " + accountKeyEnc);
//				
//		InputStream inputStream = (InputStream) urlConnection.getContent();		
//		byte[] contentRaw = new byte[urlConnection.getContentLength()];
//		inputStream.read(contentRaw);
//
//		String content = new String(contentRaw);
//
//		//The content string is the xml/json output from Bing.
//		System.out.println(content);
		//parseXmlFile();
		
		String s = "I have a lot of homework, abd, adc.";
		
		Pattern p=Pattern.compile("[.,\"\\?!:']");
		Matcher m=p.matcher(s);
		String r=m.replaceAll("");
		
		String w[]=r.split(" ");
		for (int i=0; i<w.length;i++)
			System.out.println(w[i]);
		
	}
	private static void parseXmlFile(){
		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {

			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			//parse using builder to get DOM representation of the XML file
			Document dom = db.parse(new File("src/testBing/NewFile.xml"));
			Element docEle = dom.getDocumentElement();
			NodeList nl = docEle.getElementsByTagName("entry");
			for (int i=0; i<nl.getLength();i++){
				Node e = nl.item(i);
				System.out.println(e.getTextContent());
			}
			System.out.println(nl);
			

		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
