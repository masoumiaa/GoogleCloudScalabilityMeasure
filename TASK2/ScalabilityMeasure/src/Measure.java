import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Measure {
	static PrintWriter writer ;
	public static void main(String[] args) throws IOException {
		// Note : I changed number of iteration in the loop and files names for each test 
		writer = new PrintWriter("Addresults10.txt", "UTF-8");
		
		for(int i=0;i<1000;i++){  
			//measureAddingAds();
			measureSearchingAds();
		}
		writer.close();
		

	}
	
	private static void measureAddingAds() throws IOException{
		//https://tlc-tp1-194513.appspot.com/add
		String urlParameters  = "content=facebook&price=123456&advertissmentName=default";
		byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
		int    postDataLength = postData.length;
		String request        = "https://tlc-tp1-194513.appspot.com/add";
		URL    url            = new URL( request );
		HttpURLConnection conn= (HttpURLConnection) url.openConnection();           
		conn.setDoOutput( true );
		conn.setInstanceFollowRedirects( false );
		conn.setRequestMethod( "POST" );
		conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("host","tlc-tp1-194513.appspot.com");
		conn.setRequestProperty("Connection","keep-alive");
		conn.setRequestProperty("Referer",
		"https://tlc-tp1-194513.appspot.com/advertissmentBoard.jsp?"
		+ "advertissmentName=default&filterValue=facebook");
		conn.setRequestProperty( "charset", "utf-8");
		conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
		conn.setUseCaches( false );
		long startTime = System.currentTimeMillis();
		try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
		   wr.write( postData );
		}
		long endTime = System.currentTimeMillis();
        System.out.println("- Round trip latency "+(startTime-endTime) +"ms");
        //writer.println((startTime-endTime));
		
	}
	
	private static void measureSearchingAds() throws IOException{
		String url = "https://tlc-tp1-194513.appspot.com/advertissmentBoard.jsp"
				+ "?advertissmentName=default&filterValue=facebook";	
		URL search = new URL(url);
		long startTime = System.currentTimeMillis();
        BufferedReader in = new BufferedReader(
        new InputStreamReader(search.openStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null)
           System.out.println(inputLine);
        in.close();
        
        long endTime = System.currentTimeMillis();
        System.out.println("- Round trip latency "+(startTime-endTime) +"ms");
        writer.println((startTime-endTime));
	}

}
