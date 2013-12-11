package org.openphacts.cytophacts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OpenPhactsMethods 
{
	/**
	 * @param args
	 * @throws IOException 
	 */

	OpenPhactsMethods()
	{
	}
	
	
	public static void main(String[] args) throws IOException 
	{
		new OpenPhactsMethods().run();
	}
	
	/**
	 * Get the SMILES string for a compound by name
	 * @param chemicalName
	 * @return SMILES string.
	 */
	public String getSMILES (String chemicalName)
	{
		//TODO
		return null;
	}
	
	/**
	 * Use substructure search to get a list of similar compounds.
	 * @param SMILES, for example "CC(=O)Oc1ccccc1C(=O)O"
	 * @return list of compounds IDS
	 * @throws IOException 
	 */
	public List<String> getSubStructureSearch(String SMILES) throws IOException
	{
		URL url = new URL("https://beta.openphacts.org/1.3/structure/substructure?app_id=b9d2be99&app_key=c5eaa930c723fcfda47c1b0e0f201b4f&searchOptions.Molecule="+URLEncoder.encode(SMILES, "UTF-8")+"&_format=tsv");
		return getOpenPhacts(url);

	}
	
	/**
	 * Get a list of pathways for a given organism
	 * @param organismName
	 * @return pathway ids.
	 * @throws IOException 
	 */
	public List<String> getOpenPhacts(URL url) throws IOException{
		List<String> result = new ArrayList<String>();
		InputStream is = url.openStream();
		InputStreamReader inStream = new InputStreamReader(is); 
		BufferedReader buff= new BufferedReader(inStream);
		String nextLine;
		// Read and print the lines from index.html
		while (true){
			nextLine = buff.readLine();  
			if (nextLine !=null){
				result.add(nextLine); 
			}
			else{
				break;
			} 
		}
		
		return result;
	}
	
	public List<String> getPathways(String organismName) throws IOException
	{
		URL url = new URL("http://ops2.few.vu.nl/pathways?app_id=b9d2be99&app_key=c5eaa930c723fcfda47c1b0e0f201b4f&pathway_organism="+URLEncoder.encode(organismName, "UTF-8")+"&_format=tsv");
		return getOpenPhacts(url);
	}

	/**
	 * Get a list of pathways for a given species that contain a given compound.
	 * @param compoundID
	 * @param organismName
	 * @return a list of pathway identifiers.
	 * @throws IOException 
	 */
	public List<String> getPathwaysForCompound (String compoundURI, String organismName) throws IOException
	{
		URL url = new URL("https://beta.openphacts.org/1.3/pathways/byCompound?uri="+URLEncoder.encode(compoundURI, "UTF-8")+"&app_id=b9d2be99&app_key=c5eaa930c723fcfda47c1b0e0f201b4f&pathway_organism="+URLEncoder.encode(organismName, "UTF-8")+"&_format=tsv");
		return getOpenPhacts(url);
	}

	public void run() throws IOException
	{
		/*URL url = new URL("http://ops2.few.vu.nl/pathways?app_id=b9d2be99&app_key=c5eaa930c723fcfda47c1b0e0f201b4f&pathway_organism=Homo+sapiens&_format=tsv");
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
		}*/
		List<String> pathwayList = getPathways("Homo sapiens");
		for (String pwy : pathwayList)
		{
			System.out.println(pwy);
		}
		List<String> substructureList = getSubStructureSearch("CC(=O)Oc1ccccc1C(=O)O");
		for (String ssl : substructureList)
		{
			System.out.println(ssl);
		}
	}

}
