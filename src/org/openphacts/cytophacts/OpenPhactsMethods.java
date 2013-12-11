import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;


public class OpenPhactsMethods {

	/**
	 * @param args
	 * @throws IOException 
	 */

	
	public static void main(String[] args) throws IOException {
		URL url = new URL("http://ops2.few.vu.nl/pathways?app_id=b9d2be99&app_key=c5eaa930c723fcfda47c1b0e0f201b4f&pathway_organism=Homo+sapiens&_format=tsv");
		InputStream is = url.openStream();
		InputStreamReader inStream = new InputStreamReader(is); 
		BufferedReader buff= new BufferedReader(inStream);
		String nextLine;
	       // Read and print the lines from index.html
	        while (true){
	            nextLine =buff.readLine();  
	            if (nextLine !=null){
	                System.out.println(nextLine); 
	            }
	            else{
	               break;
	            } 
	        }
       
	}

}
