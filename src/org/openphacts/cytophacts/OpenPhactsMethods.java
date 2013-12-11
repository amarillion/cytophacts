package org.openphacts.cytophacts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
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
	 */
	public List<String> getSubStructureSearch(String SMILES)
	{
		//TODO
		return null;
	}
	
	/**
	 * Get a list of pathways for a given organism
	 * @param organismName
	 * @return pathway ids.
	 */
	public List<String> getPathways(String organismName)
	{
		//TODO
		return null;
	}

	/**
	 * Get a list of pathways for a given species that contain a given compound.
	 * @param compoundID
	 * @param organismName
	 * @return a list of pathway identifiers.
	 */
	public List<String> getPathwaysForCompound (String compoundID, String organismName)
	{
		//TODO
		return null;
	}

	public void run() throws IOException
	{
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
