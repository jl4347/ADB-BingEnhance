package testBing;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

//Download and add this library to the build path.
import org.apache.commons.codec.binary.Base64;

public class BingTest {

	public static void main(String[] args) throws IOException {
		String bingUrl = "https://api.datamarket.azure.com/Bing/Search/Web?Query=%27gates%27&$top=10&$format=Atom";
		//Provide your account key here. 
		String accountKey = "R3HX3U71ionA0Z6uGfNdn2r06vaMF2SMqfyhIjh2H1A";
		
		byte[] accountKeyBytes = Base64.encodeBase64((accountKey + ":" + accountKey).getBytes());
		String accountKeyEnc = new String(accountKeyBytes);

		URL url = new URL(bingUrl);
		URLConnection urlConnection = url.openConnection();
		urlConnection.setRequestProperty("Authorization", "Basic " + accountKeyEnc);
				
		InputStream inputStream = (InputStream) urlConnection.getContent();		
		byte[] contentRaw = new byte[urlConnection.getContentLength()];
		inputStream.read(contentRaw);

		String content = new String(contentRaw);

		//The content string is the xml/json output from Bing.
		System.out.println(content);
	}

}
